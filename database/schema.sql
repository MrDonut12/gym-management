-- Gym Management System | SRS Baseline 1.1 | MUST ONLY
-- SQL Server 2022 | T01 | schema.sql
-- First-time setup only. Does not DROP or overwrite existing data.
-- Business DATETIME2(0) values are stored in Vietnam local time (UTC+7).
-- Execute this file BEFORE seed.sql using SSMS/Azure Data Studio/sqlcmd (-i).

USE master;
GO
IF DB_ID(N'GymDB') IS NULL
    EXEC(N'CREATE DATABASE GymDB');
GO
USE GymDB;
GO
SET XACT_ABORT ON;
GO

IF EXISTS (
    SELECT 1 FROM sys.tables
    WHERE schema_id = SCHEMA_ID(N'dbo')
      AND name IN (N'accounts',N'members',N'plan_types',N'memberships',N'payment_transactions',N'visits')
) OR EXISTS (
    SELECT 1 FROM sys.sequences WHERE schema_id = SCHEMA_ID(N'dbo') AND name = N'seq_paid_sequence'
)
    THROW 51000, N'MUST schema objects already exist. Use a fresh database or a reviewed migration; no objects were dropped.', 1;
GO

BEGIN TRY
    BEGIN TRANSACTION;

    CREATE TABLE dbo.accounts (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_accounts PRIMARY KEY,
        username NVARCHAR(50) COLLATE Latin1_General_100_CI_AS NOT NULL CONSTRAINT UQ_accounts_username UNIQUE,
        password_hash VARCHAR(255) NOT NULL,
        full_name NVARCHAR(100) NOT NULL,
        phone VARCHAR(10) NOT NULL CONSTRAINT UQ_accounts_phone UNIQUE,
        email NVARCHAR(254) NULL,
        role VARCHAR(20) NOT NULL CONSTRAINT CK_accounts_role CHECK (role IN ('ADMINISTRATOR','OWNER','MANAGER','RECEPTIONIST','ACCOUNTANT')),
        status VARCHAR(10) NOT NULL CONSTRAINT DF_accounts_status DEFAULT 'ACTIVE'
            CONSTRAINT CK_accounts_status CHECK (status IN ('ACTIVE','LOCKED')),
        is_deleted BIT NOT NULL CONSTRAINT DF_accounts_deleted DEFAULT 0,
        deleted_at DATETIME2(0) NULL,
        created_at DATETIME2(0) NOT NULL CONSTRAINT DF_accounts_created DEFAULT (DATEADD(HOUR,7,SYSUTCDATETIME())),
        updated_at DATETIME2(0) NULL,
        CONSTRAINT CK_accounts_phone CHECK (phone NOT LIKE '%[^0-9]%' AND LEN(phone)=10),
        CONSTRAINT CK_accounts_name CHECK (LEN(LTRIM(RTRIM(full_name))) BETWEEN 2 AND 100),
        CONSTRAINT CK_accounts_deleted CHECK (
            (is_deleted=0 AND deleted_at IS NULL) OR (is_deleted=1 AND deleted_at IS NOT NULL)
        )
    );

    CREATE TABLE dbo.members (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_members PRIMARY KEY,
        full_name NVARCHAR(100) NOT NULL,
        phone VARCHAR(10) NOT NULL CONSTRAINT UQ_members_phone UNIQUE,
        email NVARCHAR(254) NULL,
        birth_date DATE NULL,
        gender NVARCHAR(10) NULL CONSTRAINT CK_members_gender CHECK (gender IN (N'Nam',N'Nữ',N'Khác')),
        created_at DATETIME2(0) NOT NULL CONSTRAINT DF_members_created DEFAULT (DATEADD(HOUR,7,SYSUTCDATETIME())),
        is_deleted BIT NOT NULL CONSTRAINT DF_members_deleted DEFAULT 0,
        deleted_at DATETIME2(0) NULL,
        deleted_by INT NULL CONSTRAINT FK_members_deleted_by REFERENCES dbo.accounts(id),
        CONSTRAINT CK_members_phone CHECK (phone LIKE '0[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
        CONSTRAINT CK_members_name CHECK (LEN(LTRIM(RTRIM(full_name))) BETWEEN 2 AND 100),
        CONSTRAINT CK_members_deleted CHECK (
            (is_deleted=0 AND deleted_at IS NULL AND deleted_by IS NULL)
            OR (is_deleted=1 AND deleted_at IS NOT NULL AND deleted_by IS NOT NULL)
        )
    );

    CREATE TABLE dbo.plan_types (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_plan_types PRIMARY KEY,
        name NVARCHAR(100) NOT NULL,
        name_normalized NVARCHAR(100) COLLATE Vietnamese_100_CI_AS NOT NULL CONSTRAINT UQ_plan_types_name UNIQUE,
        duration_value INT NOT NULL,
        duration_unit VARCHAR(10) NOT NULL,
        price DECIMAL(12,0) NOT NULL CONSTRAINT CK_plan_types_price CHECK (price > 0),
        created_at DATETIME2(0) NOT NULL CONSTRAINT DF_plan_types_created DEFAULT (DATEADD(HOUR,7,SYSUTCDATETIME())),
        created_by INT NOT NULL CONSTRAINT FK_plan_types_created_by REFERENCES dbo.accounts(id),
        updated_at DATETIME2(0) NULL,
        updated_by INT NULL CONSTRAINT FK_plan_types_updated_by REFERENCES dbo.accounts(id),
        CONSTRAINT UQ_plan_types_duration UNIQUE (duration_value,duration_unit),
        CONSTRAINT CK_plan_types_duration CHECK (
            (duration_value=1 AND duration_unit IN ('DAY','MONTH','YEAR'))
            OR (duration_value=6 AND duration_unit='MONTH')
        )
    );

    CREATE TABLE dbo.memberships (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_memberships PRIMARY KEY,
        member_id INT NOT NULL CONSTRAINT FK_memberships_member REFERENCES dbo.members(id),
        plan_type_id INT NOT NULL CONSTRAINT FK_memberships_plan REFERENCES dbo.plan_types(id),
        plan_name_snapshot NVARCHAR(100) NOT NULL,
        duration_value_snapshot INT NOT NULL CONSTRAINT CK_memberships_duration_value CHECK (duration_value_snapshot>0),
        duration_unit_snapshot VARCHAR(10) NOT NULL CONSTRAINT CK_memberships_duration_unit CHECK (duration_unit_snapshot IN ('DAY','MONTH','YEAR')),
        created_at DATETIME2(0) NOT NULL CONSTRAINT DF_memberships_created DEFAULT (DATEADD(HOUR,7,SYSUTCDATETIME())),
        created_by INT NOT NULL CONSTRAINT FK_memberships_creator REFERENCES dbo.accounts(id),
        start_at DATETIME2(0) NULL,
        end_exclusive DATETIME2(0) NULL,
        CONSTRAINT UQ_memberships_id_member UNIQUE (id,member_id),
        CONSTRAINT CK_memberships_period CHECK (
            (start_at IS NULL AND end_exclusive IS NULL) OR
            (start_at IS NOT NULL AND end_exclusive IS NOT NULL AND
             end_exclusive>start_at AND CAST(start_at AS TIME)='00:00:00')
        )
    );

    CREATE TABLE dbo.payment_transactions (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_payment_transactions PRIMARY KEY,
        membership_id INT NOT NULL CONSTRAINT UQ_payment_membership UNIQUE,
        member_id INT NOT NULL CONSTRAINT FK_payment_member REFERENCES dbo.members(id),
        plan_name_snapshot NVARCHAR(100) NOT NULL,
        amount DECIMAL(12,0) NOT NULL CONSTRAINT CK_payment_amount CHECK (amount>0),
        method VARCHAR(15) NOT NULL CONSTRAINT CK_payment_method CHECK (method IN ('CASH','BANK_TRANSFER')),
        status VARCHAR(10) NOT NULL CONSTRAINT CK_payment_status CHECK (status IN ('PENDING','PAID','CANCELLED')),
        created_at DATETIME2(0) NOT NULL CONSTRAINT DF_payment_created DEFAULT (DATEADD(HOUR,7,SYSUTCDATETIME())),
        created_by INT NOT NULL CONSTRAINT FK_payment_created_by REFERENCES dbo.accounts(id),
        paid_at DATETIME2(0) NULL,
        confirmed_by INT NULL CONSTRAINT FK_payment_confirmed_by REFERENCES dbo.accounts(id),
        paid_sequence BIGINT NULL,
        cancelled_at DATETIME2(0) NULL,
        cancelled_by INT NULL CONSTRAINT FK_payment_cancelled_by REFERENCES dbo.accounts(id),
        proof_image_path NVARCHAR(500) NULL,
        CONSTRAINT FK_payment_membership_member FOREIGN KEY (membership_id,member_id)
            REFERENCES dbo.memberships(id,member_id),
        CONSTRAINT CK_payment_state CHECK (
            (status='PENDING' AND paid_at IS NULL AND confirmed_by IS NULL AND paid_sequence IS NULL
             AND cancelled_at IS NULL AND cancelled_by IS NULL)
            OR (status='PAID' AND paid_at IS NOT NULL AND confirmed_by IS NOT NULL AND paid_sequence IS NOT NULL
                AND cancelled_at IS NULL AND cancelled_by IS NULL)
            OR (status='CANCELLED' AND paid_at IS NULL AND confirmed_by IS NULL AND paid_sequence IS NULL
                AND cancelled_at IS NOT NULL AND cancelled_by IS NOT NULL)
        )
    );

    CREATE TABLE dbo.visits (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_visits PRIMARY KEY,
        member_id INT NOT NULL CONSTRAINT FK_visits_member REFERENCES dbo.members(id),
        membership_id INT NOT NULL,
        created_by INT NOT NULL CONSTRAINT FK_visits_creator REFERENCES dbo.accounts(id),
        check_in_date DATE NOT NULL,
        check_in_at DATETIME2(0) NOT NULL,
        check_out_at DATETIME2(0) NULL,
        status VARCHAR(12) NOT NULL CONSTRAINT CK_visits_status CHECK (status IN ('OPEN','COMPLETED','AUTO_CLOSED')),
        closed_by_account_id INT NULL CONSTRAINT FK_visits_closed_by REFERENCES dbo.accounts(id),
        closed_by_system BIT NOT NULL CONSTRAINT DF_visits_system DEFAULT 0,
        CONSTRAINT FK_visits_membership_member FOREIGN KEY (membership_id,member_id)
            REFERENCES dbo.memberships(id,member_id),
        CONSTRAINT CK_visits_date CHECK (
            CAST(check_in_at AS DATE)=check_in_date
            AND CAST(check_in_at AS TIME)>='07:00:00'
            AND CAST(check_in_at AS TIME)<'20:00:00'
        ),
        CONSTRAINT CK_visits_state CHECK (
            (status='OPEN' AND check_out_at IS NULL AND closed_by_account_id IS NULL AND closed_by_system=0)
            OR (status='COMPLETED' AND check_out_at IS NOT NULL AND check_out_at>check_in_at
                AND CAST(check_out_at AS DATE)=check_in_date
                AND CAST(check_out_at AS TIME)<'20:00:00'
                AND closed_by_account_id IS NOT NULL AND closed_by_system=0)
            OR (status='AUTO_CLOSED' AND check_out_at IS NOT NULL
                AND check_out_at=DATEADD(HOUR,20,CAST(check_in_date AS DATETIME2(0)))
                AND closed_by_account_id IS NULL AND closed_by_system=1)
        )
    );

    CREATE UNIQUE INDEX UX_visits_one_open ON dbo.visits(member_id) WHERE status='OPEN';
    CREATE UNIQUE INDEX UX_payment_paid_sequence ON dbo.payment_transactions(paid_sequence)
        WHERE paid_sequence IS NOT NULL;
    CREATE INDEX IX_payment_paid_at ON dbo.payment_transactions(paid_at) INCLUDE(amount) WHERE status='PAID';
    CREATE INDEX IX_payment_created_at ON dbo.payment_transactions(created_at)
        WHERE status IN ('PENDING','CANCELLED');
    CREATE INDEX IX_visits_check_in ON dbo.visits(member_id,check_in_at);
    CREATE INDEX IX_memberships_member_period ON dbo.memberships(member_id,start_at,end_exclusive);
    CREATE SEQUENCE dbo.seq_paid_sequence AS BIGINT START WITH 1 INCREMENT BY 1;

    COMMIT TRANSACTION;
    PRINT N'Created six MUST tables, constraints, indexes, and paid sequence in GymDB.';
END TRY
BEGIN CATCH
    IF @@TRANCOUNT>0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
GO

-- Out of the v1.1 MUST scope: VisitChanges, AuditLogs, FUT-01/02/03/04.
-- Cross-table invariants (atomic PENDING + Membership creation, PAID + schedule,
-- rolling dates and authorization) must also be checked in service transactions.
