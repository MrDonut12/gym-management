-- TEST: After schema.sql + seed.sql, Owner can create another 1-MONTH plan.
-- The INSERT is rolled back so the test does not change seed data.
USE GymDB;
GO
SET XACT_ABORT ON;
GO
BEGIN TRY
    BEGIN TRANSACTION;

    DECLARE @ownerId INT = (
        SELECT TOP (1) id FROM dbo.accounts
        WHERE role='OWNER' AND status='ACTIVE' AND is_deleted=0 ORDER BY id
    );
    IF @ownerId IS NULL
        THROW 51111, N'Test requires a seeded active Owner account.', 1;

    IF NOT EXISTS (
        SELECT 1 FROM dbo.plan_types
        WHERE duration_value=1 AND duration_unit='MONTH'
    )
        THROW 51112, N'Test requires one existing 1-MONTH plan.', 1;

    DECLARE @testName NVARCHAR(100) = N'Monthly Plus Test ' + CONVERT(NVARCHAR(36), NEWID());
    DECLARE @createdAt DATETIME2(0) = DATEADD(HOUR, 7, SYSUTCDATETIME());

    INSERT INTO dbo.plan_types
        (name, name_normalized, duration_value, duration_unit, price, created_at, created_by)
    VALUES
        (@testName, LOWER(@testName), 1, 'MONTH', 700000, @createdAt, @ownerId);

    IF @@ROWCOUNT <> 1
        THROW 51113, N'Insert failed unexpectedly.', 1;

    SELECT id, name, duration_value, duration_unit, price
    FROM dbo.plan_types
    WHERE duration_value=1 AND duration_unit='MONTH'
    ORDER BY id;

    PRINT N'PASS: two or more distinct 1-MONTH plans are allowed; rolling back the test row.';
    ROLLBACK TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
GO
