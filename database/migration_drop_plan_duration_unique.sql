-- Gym Management | SRS v1.1 | Migration for already-created GymDB
-- Drops only the incorrectly restrictive UNIQUE constraint on plan duration.
-- Make a DB backup first. Does not delete data or reset sequences.
USE GymDB;
GO
SET XACT_ABORT ON;
GO
IF OBJECT_ID(N'dbo.plan_types', N'U') IS NULL
    THROW 51101, N'dbo.plan_types does not exist; run schema.sql first.', 1;
GO
BEGIN TRY
    BEGIN TRANSACTION;

    IF EXISTS (
        SELECT 1 FROM sys.key_constraints
        WHERE parent_object_id = OBJECT_ID(N'dbo.plan_types')
          AND name = N'UQ_plan_types_duration'
          AND type = N'UQ'
    )
    BEGIN
        ALTER TABLE dbo.plan_types DROP CONSTRAINT UQ_plan_types_duration;
    END;

    IF NOT EXISTS (
        SELECT 1 FROM sys.key_constraints
        WHERE parent_object_id = OBJECT_ID(N'dbo.plan_types')
          AND name = N'UQ_plan_types_name'
          AND type = N'UQ'
    )
        THROW 51102, N'UQ_plan_types_name is missing. Inspect schema before proceeding.', 1;

    IF NOT EXISTS (
        SELECT 1 FROM sys.check_constraints
        WHERE parent_object_id = OBJECT_ID(N'dbo.plan_types')
          AND name = N'CK_plan_types_duration'
    )
        THROW 51103, N'CK_plan_types_duration is missing. Inspect schema before proceeding.', 1;

    COMMIT TRANSACTION;
    PRINT N'Duration uniqueness removed. Name uniqueness and valid-duration CHECK remain.';
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
GO
