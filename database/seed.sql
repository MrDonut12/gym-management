-- Gym Management System | SRS Baseline 1.1 | MUST ONLY
-- SQL Server 2022 | T01 | seed.sql
-- DEV/TEST DATA. Execute AFTER schema.sql on an EMPTY 6-table GymDB.
-- This script refuses to run if ANY of the six tables already contains rows.
-- Demo password hashes require T07 PasswordUtil to implement this exact PBKDF2 encoding.

USE GymDB;
GO
SET XACT_ABORT ON;
GO

IF EXISTS (
    SELECT 1 FROM (VALUES (N'accounts'),(N'members'),(N'plan_types'),
        (N'memberships'),(N'payment_transactions'),(N'visits')) t(table_name)
    WHERE OBJECT_ID(N'dbo.'+t.table_name,N'U') IS NULL
) OR OBJECT_ID(N'dbo.seq_paid_sequence',N'SO') IS NULL
    THROW 51010, N'Schema missing. Run schema.sql before seed.sql.', 1;
GO

-- Guard before doing anything: seed only a clean, empty schema.
IF EXISTS (SELECT 1 FROM dbo.accounts)
    OR EXISTS (SELECT 1 FROM dbo.members)
    OR EXISTS (SELECT 1 FROM dbo.plan_types)
    OR EXISTS (SELECT 1 FROM dbo.memberships)
    OR EXISTS (SELECT 1 FROM dbo.payment_transactions)
    OR EXISTS (SELECT 1 FROM dbo.visits)
    THROW 51011, N'Seed refused: tables are not empty; no data was replaced.', 1;
GO

BEGIN TRY
    BEGIN TRANSACTION;

    -- PBKDF2 hashes below are TEST DATA, NOT production credentials.
    -- Format: pbkdf2_sha256$210000$<16-byte salt hex>$<32-byte digest hex>.
    -- T07 must use PBKDF2WithHmacSHA256 and match this format for demo login.
    INSERT INTO dbo.accounts (username,password_hash,full_name,phone,email,role,status,created_at)
    VALUES
      (N'admin', 'pbkdf2_sha256$210000$5c158bdba0255631b7e51cb8bdced2e3$53c36892bc52d23819e7030e836a5f95b1407e54b7c2b80f2447d1a7b03021e3', N'Nguyễn Minh Quân', '0901000001',N'admin@example.invalid','ADMINISTRATOR','ACTIVE','2026-09-15T08:00:00'),
      (N'owner', 'pbkdf2_sha256$210000$2ed46ebbe5d2de1a3289cbd39d5215cb$4f9a1979758a6c8b1a118c608076cf1749801f81cfa85d31df6cbffbdce236e2', N'Trần Hoàng Anh', '0901000002',N'owner@example.invalid','OWNER','ACTIVE','2026-09-15T08:00:00'),
      (N'manager', 'pbkdf2_sha256$210000$88d5899637c6b0a90c7866ad33414c48$8f8c37fa8f19ae43cf744ae839b8fb1dc4e082360a748588b506d8043b419fbd', N'Lê Quốc Bảo', '0901000003',N'manager@example.invalid','MANAGER','ACTIVE','2026-09-15T08:00:00'),
      (N'receptionist', 'pbkdf2_sha256$210000$86f31de819bebce86e6c7ea06ae4129d$a2a932b6b5d14963d715c34bfeba783a13f2ade06511ea280e26c2c67a222d74', N'Phạm Ngọc Mai', '0901000004',N'receptionist@example.invalid','RECEPTIONIST','ACTIVE','2026-09-15T08:00:00'),
      (N'accountant', 'pbkdf2_sha256$210000$56fbe257afb7edb6932f0d4e8eef50c5$49cf7b1f75fac86546db415c4bad873c3bd55b08c4a9834ac9bb559f0c9d2afc', N'Võ Thanh Hà', '0901000005',N'accountant@example.invalid','ACCOUNTANT','ACTIVE','2026-09-15T08:00:00')
    ;

    DECLARE @ownerId INT = (SELECT id FROM dbo.accounts WHERE username=N'owner');
    DECLARE @managerId INT = (SELECT id FROM dbo.accounts WHERE username=N'manager');
    DECLARE @receptionistId INT = (SELECT id FROM dbo.accounts WHERE username=N'receptionist');
    IF @ownerId IS NULL OR @managerId IS NULL OR @receptionistId IS NULL
        THROW 51012, N'Account seed failed.', 1;

    -- A deterministic set of 100 members. No personal/real-world contact data.
    DECLARE @memberIds TABLE (ordinal INT PRIMARY KEY, id INT NOT NULL UNIQUE);
    DECLARE @i INT=1, @memberId INT, @g NVARCHAR(10), @phone VARCHAR(10);
    WHILE @i<=100
    BEGIN
        SET @g=CASE @i%3 WHEN 0 THEN N'Nam' WHEN 1 THEN N'Nữ' ELSE N'Khác' END;
        SET @phone='09'+RIGHT('00000000'+CONVERT(VARCHAR(8),@i),8);
        INSERT INTO dbo.members (full_name,phone,email,birth_date,gender,created_at)
        VALUES (N'Hội viên mẫu '+RIGHT('000'+CONVERT(VARCHAR(3),@i),3),
                @phone, N'member'+RIGHT('000'+CONVERT(VARCHAR(3),@i),3)+N'@example.invalid',
                DATEFROMPARTS(1990+@i%15,1+@i%12,1+@i%28),@g,'2026-09-15T08:00:00');
        SET @memberId=CONVERT(INT,SCOPE_IDENTITY());
        INSERT INTO @memberIds (ordinal,id) VALUES (@i,@memberId);
        SET @i+=1;
    END;

    -- Demo soft-deletion states (independent of membership status).
    UPDATE dbo.members SET is_deleted=1, deleted_at='2026-10-15T09:00:00', deleted_by=@managerId
    WHERE id IN (SELECT id FROM @memberIds WHERE ordinal IN (99,100));

    INSERT INTO dbo.plan_types (name,name_normalized,duration_value,duration_unit,price,created_at,created_by)
    VALUES
      (N'Daily Pass', N'daily pass', 1,'DAY',50000,'2026-09-15T09:00:00',@ownerId),
      (N'Monthly Pass',N'monthly pass',1,'MONTH',500000,'2026-09-15T09:00:00',@ownerId),
      (N'6-Month Pass',N'6-month pass',6,'MONTH',2700000,'2026-09-15T09:00:00',@ownerId),
      (N'Yearly Pass',N'yearly pass',1,'YEAR',5000000,'2026-09-15T09:00:00',@ownerId);

    DECLARE @planDay INT = (SELECT id FROM dbo.plan_types WHERE duration_value=1 AND duration_unit='DAY');
    DECLARE @planMonth INT = (SELECT id FROM dbo.plan_types WHERE duration_value=1 AND duration_unit='MONTH');
    DECLARE @planSix INT = (SELECT id FROM dbo.plan_types WHERE duration_value=6 AND duration_unit='MONTH');
    DECLARE @planYear INT = (SELECT id FROM dbo.plan_types WHERE duration_value=1 AND duration_unit='YEAR');

    -- 30 Memberships + 30 Transactions:
    -- i=1..20 PAID, 21..24 PENDING, 25..28 CANCELLED, 29..30 PAID renewals of members 1 and 2.
    -- The first two members have a Monthly Pass from 01/10 to 01/11, then a PAID renewal from 01/11 to 01/12.
    DECLARE @membershipIds TABLE (ordinal INT PRIMARY KEY, id INT NOT NULL UNIQUE);
    DECLARE @ordinal INT, @planId INT, @membershipId INT, @duration INT, @unit VARCHAR(10),
            @planName NVARCHAR(100), @price DECIMAL(12,0), @status VARCHAR(10),
            @start DATETIME2(0), @end DATETIME2(0), @created DATETIME2(0),
            @paid DATETIME2(0), @cancelled DATETIME2(0), @seq BIGINT;
    SET @i=1;
    WHILE @i<=30
    BEGIN
        SET @ordinal=CASE WHEN @i IN (29,30) THEN @i-28 ELSE @i END;
        SET @memberId=(SELECT id FROM @memberIds WHERE ordinal=@ordinal);
        SET @planId=CASE WHEN @i IN (1,2,29,30) THEN @planMonth
                         WHEN @i%4=0 THEN @planDay
                         WHEN @i%4=1 THEN @planMonth
                         WHEN @i%4=2 THEN @planSix
                         ELSE @planYear END;
        SELECT @duration=duration_value, @unit=duration_unit,
               @planName=name, @price=price FROM dbo.plan_types WHERE id=@planId;
        SET @status=CASE WHEN @i<=20 OR @i>=29 THEN 'PAID'
                          WHEN @i<=24 THEN 'PENDING' ELSE 'CANCELLED' END;
        SET @created=CASE WHEN @i>=29 THEN '2026-10-19T09:00:00' ELSE '2026-09-30T09:00:00' END;
        SET @paid=CASE WHEN @status='PAID'
                        THEN CASE WHEN @i>=29 THEN '2026-10-20T10:00:00' ELSE '2026-09-30T10:00:00' END
                        ELSE NULL END;
        SET @cancelled=CASE WHEN @status='CANCELLED' THEN '2026-10-02T10:00:00' ELSE NULL END;
        SET @start=CASE WHEN @status='PAID'
                         THEN CASE WHEN @i>=29 THEN '2026-11-01T00:00:00' ELSE '2026-10-01T00:00:00' END
                         ELSE NULL END;
        SET @end=CASE WHEN @start IS NULL THEN NULL
                       WHEN @unit='DAY' THEN DATEADD(DAY,@duration,@start)
                       WHEN @unit='MONTH' THEN DATEADD(MONTH,@duration,@start)
                       ELSE DATEADD(YEAR,@duration,@start) END;
        SET @seq=NULL;
        IF @status='PAID' SELECT @seq=NEXT VALUE FOR dbo.seq_paid_sequence;

        INSERT INTO dbo.memberships
            (member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,
             duration_unit_snapshot,created_at,created_by,start_at,end_exclusive)
        VALUES (@memberId,@planId,@planName,@duration,@unit,@created,@receptionistId,@start,@end);
        SET @membershipId=CONVERT(INT,SCOPE_IDENTITY());
        INSERT INTO @membershipIds (ordinal,id) VALUES (@i,@membershipId);

        INSERT INTO dbo.payment_transactions
            (membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,
             paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by,proof_image_path)
        VALUES (@membershipId,@memberId,@planName,@price,
                CASE WHEN @i%2=0 THEN 'CASH' ELSE 'BANK_TRANSFER' END,
                @status,@created,@receptionistId,@paid,
                CASE WHEN @status='PAID' THEN @managerId ELSE NULL END,
                @seq,@cancelled,
                CASE WHEN @status='CANCELLED' THEN @receptionistId ELSE NULL END,NULL);
        SET @i+=1;
    END;

    -- Visits for the first 15 paid initial memberships, all dated inside their paid intervals.
    -- 10 COMPLETED, 4 AUTO_CLOSED, 1 intentionally overdue OPEN to test lazy closure (FUT-02 omitted).
    DECLARE @visitDate DATE='2026-10-01';
    DECLARE @checkIn DATETIME2(0), @checkOut DATETIME2(0), @visitStatus VARCHAR(12);
    SET @i=1;
    WHILE @i<=15
    BEGIN
        SET @membershipId=(SELECT id FROM @membershipIds WHERE ordinal=@i);
        SELECT @memberId=member_id FROM dbo.memberships WHERE id=@membershipId;
        SET @checkIn=DATEADD(MINUTE,@i,CAST('2026-10-01T09:00:00' AS DATETIME2(0)));
        SET @visitStatus=CASE WHEN @i<=10 THEN 'COMPLETED'
                              WHEN @i<=14 THEN 'AUTO_CLOSED' ELSE 'OPEN' END;
        SET @checkOut=CASE WHEN @visitStatus='COMPLETED' THEN DATEADD(HOUR,1,@checkIn)
                            WHEN @visitStatus='AUTO_CLOSED' THEN '2026-10-01T20:00:00'
                            ELSE NULL END;
        INSERT INTO dbo.visits
            (member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,
             status,closed_by_account_id,closed_by_system)
        VALUES (@memberId,@membershipId,@receptionistId,@visitDate,@checkIn,@checkOut,
                @visitStatus,
                CASE WHEN @visitStatus='COMPLETED' THEN @receptionistId ELSE NULL END,
                CASE WHEN @visitStatus='AUTO_CLOSED' THEN 1 ELSE 0 END);
        SET @i+=1;
    END;

    -- Postconditions checked before committing test data.
    IF (SELECT COUNT(*) FROM dbo.accounts)<>5 OR (SELECT COUNT(*) FROM dbo.members)<>100
       OR (SELECT COUNT(*) FROM dbo.plan_types)<>4 OR (SELECT COUNT(*) FROM dbo.memberships)<>30
       OR (SELECT COUNT(*) FROM dbo.payment_transactions)<>30 OR (SELECT COUNT(*) FROM dbo.visits)<>15
        THROW 51013, N'Unexpected seed record counts; rolling back.', 1;
    IF (SELECT COUNT(*) FROM dbo.payment_transactions WHERE status='PAID')<>22
       OR (SELECT COUNT(*) FROM dbo.payment_transactions WHERE status='PENDING')<>4
       OR (SELECT COUNT(*) FROM dbo.payment_transactions WHERE status='CANCELLED')<>4
        THROW 51014, N'Unexpected transaction-status counts; rolling back.', 1;

    COMMIT TRANSACTION;
    PRINT N'Seed completed: 5 accounts, 100 members, 4 plans, 30 memberships, 30 payments, 15 visits.';
END TRY
BEGIN CATCH
    IF @@TRANCOUNT>0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
GO

SELECT N'accounts' AS table_name, COUNT(*) AS total_rows FROM dbo.accounts
UNION ALL SELECT N'members',COUNT(*) FROM dbo.members
UNION ALL SELECT N'plan_types',COUNT(*) FROM dbo.plan_types
UNION ALL SELECT N'memberships',COUNT(*) FROM dbo.memberships
UNION ALL SELECT N'payment_transactions',COUNT(*) FROM dbo.payment_transactions
UNION ALL SELECT N'visits',COUNT(*) FROM dbo.visits;
GO
