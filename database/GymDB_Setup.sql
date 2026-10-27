-- Gym Management System - SRS Baseline 2.0
-- SQL Server 2022 | Fresh database setup | UTF-8
-- Source: https://github.com/MrDonut12/gym-management
-- Commit: 226a9b970206cb866f77427901c606764fc5566e | Reviewed 2026-10-27
-- Source of truth: docs/Software Requirement Specification.md (DL-01..DL-05)
-- and current Java model/DAO. Existing database/*.sql is Baseline 1.1 and obsolete.
--
-- RUN THE WHOLE FILE in SSMS (normal query mode), or:
-- sqlcmd -S localhost -E -C -b -f 65001 -i GymDB_Setup.sql
-- For SQL authentication, replace -E with -U <login>; sqlcmd prompts for password.
-- Set @IncludeDemoData=0 below for schema only (no initial login account).
-- No SQLCMD directives, DROP DATABASE, DROP TABLE, triggers or stored procedures.
-- Existing application/legacy objects are rejected before any DDL is run.
-- This creates GymDB; it is NOT an upgrade/migration for an existing database.

USE [master];
GO
SET NOCOUNT ON;
IF DB_ID(N'GymDB') IS NULL
BEGIN
    EXEC(N'CREATE DATABASE [GymDB]');
END;
GO
USE [GymDB];
GO
-- A failed USE must never let a client that continues after errors create in master.
IF DB_NAME() <> N'GymDB'
BEGIN
    ;THROW 52000, N'Wrong database context. Stop and connect to GymDB.', 1;
END;

SET NOCOUNT ON;
SET XACT_ABORT ON;
SET ANSI_NULLS ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET ARITHABORT ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET QUOTED_IDENTIFIER ON;
SET NUMERIC_ROUNDABORT OFF;

DECLARE @IncludeDemoData BIT = 1; -- 1 = schema + sample data; 0 = schema only
DECLARE @Now DATETIME2(0) = CONVERT(DATETIME2(0),
    SYSDATETIMEOFFSET() AT TIME ZONE 'SE Asia Standard Time');
DECLARE @Today DATE = CAST(@Now AS DATE);

-- Guard and every mutation below stay in ONE batch, so THROW cannot be bypassed
-- by a client continuing with a later GO batch. A second run stops without changes.
IF EXISTS (
    SELECT 1 FROM sys.objects
    WHERE schema_id = SCHEMA_ID(N'dbo')
      AND name IN (N'accounts',N'members',N'packages',N'registrations',
                   N'plan_types',N'memberships',N'payment_transactions',N'visits',
                   N'seq_paid_sequence')
)
BEGIN
    ;THROW 52001, N'Application objects already exist. Use a fresh GymDB or a reviewed migration. No objects or data were replaced.', 1;
END;

BEGIN TRY
    BEGIN TRANSACTION;

    CREATE TABLE dbo.accounts (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_accounts PRIMARY KEY,
        username VARCHAR(50) COLLATE Latin1_General_100_CI_AS NOT NULL,
        password_hash VARCHAR(100) NOT NULL,
        full_name NVARCHAR(100) NOT NULL,
        phone CHAR(10) NOT NULL,
        email VARCHAR(254) NULL,
        role VARCHAR(20) NOT NULL,
        status VARCHAR(10) NOT NULL CONSTRAINT DF_accounts_status DEFAULT ('ACTIVE'),
        created_at DATETIME2(0) NOT NULL,
        CONSTRAINT UQ_accounts_username UNIQUE (username),
        CONSTRAINT UQ_accounts_phone UNIQUE (phone),
        CONSTRAINT CK_accounts_username CHECK (
            DATALENGTH(username) BETWEEN 4 AND 50
            AND username COLLATE Latin1_General_100_BIN2 LIKE '[A-Za-z]%'
            AND username COLLATE Latin1_General_100_BIN2 NOT LIKE '%[^A-Za-z0-9_]%'
        ),
        CONSTRAINT CK_accounts_password_hash CHECK (LEN(password_hash) > 0),
        CONSTRAINT CK_accounts_full_name CHECK (
            LEN(LTRIM(RTRIM(full_name))) BETWEEN 2 AND 100
            AND full_name COLLATE Latin1_General_100_BIN2 NOT LIKE N'%[0-9]%'
        ),
        CONSTRAINT CK_accounts_phone CHECK (
            phone COLLATE Latin1_General_100_BIN2 LIKE '0[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'
        ),
        CONSTRAINT CK_accounts_role CHECK (role IN ('ADMIN','MANAGER','RECEPTIONIST')),
        CONSTRAINT CK_accounts_status CHECK (status IN ('ACTIVE','LOCKED'))
    );

    CREATE TABLE dbo.members (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_members PRIMARY KEY,
        full_name NVARCHAR(100) NOT NULL,
        phone CHAR(10) NOT NULL,
        email VARCHAR(254) NULL,
        date_of_birth DATE NULL,
        gender NVARCHAR(10) NULL,
        is_deleted BIT NOT NULL CONSTRAINT DF_members_is_deleted DEFAULT (0),
        created_at DATETIME2(0) NOT NULL,
        created_by INT NOT NULL,
        -- Uniqueness also covers soft-deleted profiles (QT-10, HL-02).
        CONSTRAINT UQ_members_phone UNIQUE (phone),
        CONSTRAINT FK_members_created_by FOREIGN KEY (created_by) REFERENCES dbo.accounts(id),
        CONSTRAINT CK_members_full_name CHECK (
            LEN(LTRIM(RTRIM(full_name))) BETWEEN 2 AND 100
            AND full_name COLLATE Latin1_General_100_BIN2 NOT LIKE N'%[0-9]%'
        ),
        CONSTRAINT CK_members_phone CHECK (
            phone COLLATE Latin1_General_100_BIN2 LIKE '0[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'
        ),
        CONSTRAINT CK_members_gender CHECK (gender IS NULL OR gender IN (N'Nam',N'Nữ',N'Khác')),
        CONSTRAINT CK_members_date_of_birth CHECK (date_of_birth IS NULL OR date_of_birth >= CONVERT(DATE,'19000101',112))
    );

    CREATE TABLE dbo.packages (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_packages PRIMARY KEY,
        name NVARCHAR(100) COLLATE Vietnamese_100_CI_AS NOT NULL,
        duration_months TINYINT NOT NULL,
        price INT NOT NULL,
        is_active BIT NOT NULL CONSTRAINT DF_packages_is_active DEFAULT (1),
        created_at DATETIME2(0) NOT NULL,
        CONSTRAINT UQ_packages_name UNIQUE (name),
        CONSTRAINT CK_packages_name CHECK (LEN(LTRIM(RTRIM(name))) BETWEEN 2 AND 100),
        CONSTRAINT CK_packages_duration CHECK (duration_months IN (1,3,6,12)),
        CONSTRAINT CK_packages_price CHECK (price BETWEEN 1 AND 100000000)
        -- Multiple different package names may have the same duration.
        -- Whitespace normalization is performed by ValidationUtil/PackageDAO (HL-11).
    );

    CREATE TABLE dbo.registrations (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_registrations PRIMARY KEY,
        member_id INT NOT NULL,
        package_id INT NOT NULL,
        package_name_snapshot NVARCHAR(100) NOT NULL,
        duration_months_snapshot TINYINT NOT NULL,
        price_snapshot INT NOT NULL,
        status VARCHAR(10) NOT NULL CONSTRAINT DF_registrations_status DEFAULT ('PENDING'),
        created_at DATETIME2(0) NOT NULL,
        created_by INT NOT NULL,
        payment_method VARCHAR(10) NULL,
        paid_at DATETIME2(0) NULL,
        confirmed_by INT NULL,
        start_date DATE NULL,
        end_date DATE NULL,
        cancelled_at DATETIME2(0) NULL,
        cancelled_by INT NULL,
        cancel_reason NVARCHAR(255) NULL,
        CONSTRAINT FK_registrations_member FOREIGN KEY (member_id) REFERENCES dbo.members(id),
        CONSTRAINT FK_registrations_package FOREIGN KEY (package_id) REFERENCES dbo.packages(id),
        CONSTRAINT FK_registrations_created_by FOREIGN KEY (created_by) REFERENCES dbo.accounts(id),
        CONSTRAINT FK_registrations_confirmed_by FOREIGN KEY (confirmed_by) REFERENCES dbo.accounts(id),
        CONSTRAINT FK_registrations_cancelled_by FOREIGN KEY (cancelled_by) REFERENCES dbo.accounts(id),
        CONSTRAINT CK_registrations_snapshot_name CHECK (LEN(LTRIM(RTRIM(package_name_snapshot))) BETWEEN 2 AND 100),
        CONSTRAINT CK_registrations_duration CHECK (duration_months_snapshot IN (1,3,6,12)),
        CONSTRAINT CK_registrations_price CHECK (price_snapshot BETWEEN 1 AND 100000000),
        CONSTRAINT CK_registrations_status CHECK (status IN ('PENDING','PAID','CANCELLED')),
        CONSTRAINT CK_registrations_payment_method CHECK (payment_method IS NULL OR payment_method IN ('CASH','TRANSFER')),
        -- Explicit NOT NULL terms are necessary: CHECK also accepts UNKNOWN.
        CONSTRAINT CK_registrations_state CHECK (
            (status = 'PENDING'
             AND payment_method IS NULL AND paid_at IS NULL AND confirmed_by IS NULL
             AND start_date IS NULL AND end_date IS NULL
             AND cancelled_at IS NULL AND cancelled_by IS NULL AND cancel_reason IS NULL)
            OR
            (status = 'PAID'
             AND payment_method IS NOT NULL AND paid_at IS NOT NULL AND confirmed_by IS NOT NULL
             AND start_date IS NOT NULL AND end_date IS NOT NULL AND end_date >= start_date
             AND cancelled_at IS NULL AND cancelled_by IS NULL AND cancel_reason IS NULL)
            OR
            (status = 'CANCELLED'
             AND payment_method IS NULL AND paid_at IS NULL AND confirmed_by IS NULL
             AND start_date IS NULL AND end_date IS NULL
             AND cancelled_at IS NOT NULL AND cancelled_by IS NOT NULL AND cancel_reason IS NOT NULL
             AND LEN(LTRIM(RTRIM(cancel_reason))) BETWEEN 3 AND 255)
        )
    );

    CREATE INDEX IX_members_full_name ON dbo.members(full_name);
    CREATE INDEX IX_registrations_member_status ON dbo.registrations(member_id,status)
        INCLUDE (start_date,end_date,created_at);
    CREATE INDEX IX_registrations_paid_at ON dbo.registrations(paid_at)
        INCLUDE (price_snapshot,package_id) WHERE status = 'PAID';
    CREATE INDEX IX_registrations_created_at ON dbo.registrations(created_at DESC,id DESC)
        INCLUDE (member_id,package_id,status);
    CREATE INDEX IX_registrations_package_status ON dbo.registrations(package_id,status)
        INCLUDE (price_snapshot,paid_at);

    -- No server-local clock defaults: runtime INSERTs supply AppClock.now() (QT-15).
    -- Cross-row scheduling, final-state immutability, normalized package names,
    -- future-birth-date validation, snapshots and RBAC remain in Java Service/DAO.
    -- For QT-06, lock members WITH (UPDLOCK,HOLDLOCK) first, then write atomically.

    -- Optional deterministic DEV/TEST fixtures for the current four-table SRS.
    -- Password hashes use compact PBKDF2-HMAC-SHA256 encoding; see setup guide.
    -- All six test accounts use GymDemo@2026; PasswordUtil is still TODO in source.
    IF @IncludeDemoData = 1
    BEGIN
        IF EXISTS (SELECT 1 FROM dbo.accounts)
           OR EXISTS (SELECT 1 FROM dbo.members)
           OR EXISTS (SELECT 1 FROM dbo.packages)
           OR EXISTS (SELECT 1 FROM dbo.registrations)
        BEGIN
            ;THROW 52100, N'Demo seeding requires all four tables to be empty.', 1;
        END;

        DECLARE @SeedAccountCreated DATETIME2(0) = DATEADD(HOUR,8,
            CAST(DATEADD(DAY,-181,@Today) AS DATETIME2(0)));
        INSERT INTO dbo.accounts
            (username,password_hash,full_name,phone,email,role,status,created_at)
        VALUES
            ('admin1','pbkdf2$600000$BrrU4X3e5FCgwfTUy4rEEg==$pIaylwbsOMf5TTlVUyk3UMT949WLwVJrpyeNJ993Sis=',N'Quản trị An','0900000001','admin1@example.invalid','ADMIN','ACTIVE',@SeedAccountCreated),
            ('admin2','pbkdf2$600000$6PKV9TZsfGWruw5jhb7lfg==$z2Y+Yt8DIAOIaa1H+Xg0nPZBM1TdQdwSpa1uuOc7fEk=',N'Quản trị Bình','0900000002','admin2@example.invalid','ADMIN','ACTIVE',@SeedAccountCreated),
            ('manager1','pbkdf2$600000$4XVluJjEG4ghduQUoj1lCg==$SzvmZsTlzSC2+k9WNwrWkKshtX0Nys9zrXZe1Aa22gw=',N'Quản lý Châu','0900000003','manager1@example.invalid','MANAGER','ACTIVE',@SeedAccountCreated),
            ('manager2','pbkdf2$600000$GxWBsbLmJHhtmfmGqKy5Qw==$VshI+i3OCOl2sta6qsapDPXmfBgRKPGI3OzelvUyAho=',N'Quản lý Dung','0900000004','manager2@example.invalid','MANAGER','ACTIVE',@SeedAccountCreated),
            ('reception1','pbkdf2$600000$yOamvi0oe+UPdBvYX2nhIA==$QCeRXAKkjZUaCz05VtOGWoU7Pk+zH8eIgSj2ZQxph5Y=',N'Lễ tân Giang','0900000005','reception1@example.invalid','RECEPTIONIST','ACTIVE',@SeedAccountCreated),
            ('reception2','pbkdf2$600000$Zw3JbSWj4h4njwfmfZf7zg==$cjF2idVBx3BrDmJHEYei0OcspIU5NFe4gihZGnpSRCE=',N'Lễ tân Hạnh','0900000006','reception2@example.invalid','RECEPTIONIST','LOCKED',@SeedAccountCreated);

        DECLARE @SeedManagerId INT = (SELECT id FROM dbo.accounts WHERE username='manager1');
        DECLARE @SeedReceptionId INT = (SELECT id FROM dbo.accounts WHERE username='reception1');
        DECLARE @SeedPackageSpecs TABLE (
            ordinal INT PRIMARY KEY, name NVARCHAR(100) NOT NULL,
            duration_months TINYINT NOT NULL, price INT NOT NULL, is_active BIT NOT NULL
        );
        INSERT INTO @SeedPackageSpecs VALUES
            (1,N'Gói Một Tháng',1,500000,1),
            (2,N'Gói Ba Tháng',3,1350000,1),
            (3,N'Gói Sáu Tháng',6,2400000,1),
            (4,N'Gói Mười Hai Tháng',12,4500000,1),
            (5,N'Gói Một Tháng Cũ',1,450000,0);
        INSERT INTO dbo.packages (name,duration_months,price,is_active,created_at)
        SELECT name,duration_months,price,is_active,DATEADD(HOUR,1,@SeedAccountCreated)
        FROM @SeedPackageSpecs;

        DECLARE @SeedPackageIds TABLE (
            ordinal INT PRIMARY KEY, id INT NOT NULL,
            name NVARCHAR(100) NOT NULL, duration_months TINYINT NOT NULL, price INT NOT NULL
        );
        INSERT INTO @SeedPackageIds
        SELECT s.ordinal,p.id,p.name,p.duration_months,p.price
        FROM @SeedPackageSpecs s JOIN dbo.packages p ON p.name=s.name;

        DECLARE @SeedMemberIds TABLE (ordinal INT PRIMARY KEY, id INT NOT NULL UNIQUE);
        DECLARE @SeedI INT=1, @SeedMemberId INT, @SeedPackageId INT,
                @SeedPackageOrdinal INT, @SeedName NVARCHAR(100),
                @SeedPackageName NVARCHAR(100), @SeedDuration TINYINT, @SeedPrice INT,
                @SeedPaidDate DATE, @SeedLastEnd DATE, @SeedStart DATE, @SeedEnd DATE,
                @SeedCreated DATETIME2(0), @SeedPaid DATETIME2(0);
        WHILE @SeedI <= 100
        BEGIN
            -- Ten surnames x ten given names; names contain no numeric suffixes.
            SET @SeedName = CASE (@SeedI-1)/10
                WHEN 0 THEN N'Nguyễn' WHEN 1 THEN N'Trần' WHEN 2 THEN N'Lê'
                WHEN 3 THEN N'Phạm' WHEN 4 THEN N'Hoàng' WHEN 5 THEN N'Vũ'
                WHEN 6 THEN N'Đặng' WHEN 7 THEN N'Bùi' WHEN 8 THEN N'Đỗ' ELSE N'Hồ' END
                + N' ' + CASE (@SeedI-1)%10
                WHEN 0 THEN N'Minh An' WHEN 1 THEN N'Thanh Bình' WHEN 2 THEN N'Ngọc Châu'
                WHEN 3 THEN N'Thùy Dung' WHEN 4 THEN N'Hương Giang' WHEN 5 THEN N'Thu Hà'
                WHEN 6 THEN N'Quang Huy' WHEN 7 THEN N'Gia Khánh' WHEN 8 THEN N'Bảo Linh' ELSE N'Hoàng Nam' END;
            SET @SeedCreated = DATEADD(HOUR,8,
                CAST(DATEADD(DAY,-(180-(@SeedI*7)%140),@Today) AS DATETIME2(0)));
            INSERT INTO dbo.members
                (full_name,phone,email,date_of_birth,gender,is_deleted,created_at,created_by)
            VALUES
                (@SeedName,'091'+RIGHT('0000000'+CONVERT(VARCHAR(7),@SeedI),7),
                 'member'+RIGHT('000'+CONVERT(VARCHAR(3),@SeedI),3)+'@example.invalid',
                 DATEFROMPARTS(1980+@SeedI%25,1+@SeedI%12,1+@SeedI%28),
                 CASE @SeedI%3 WHEN 0 THEN N'Nam' WHEN 1 THEN N'Nữ' ELSE N'Khác' END,
                 CASE WHEN @SeedI>=93 THEN 1 ELSE 0 END,@SeedCreated,@SeedReceptionId);
            SET @SeedMemberId=CONVERT(INT,SCOPE_IDENTITY());
            INSERT INTO @SeedMemberIds VALUES (@SeedI,@SeedMemberId);
            SET @SeedI+=1;
        END;

        -- 80 initial PAID purchases, spread over the recent six months.
        -- Fixtures: member 1 expired, 2 active, 3 expiring, 4 active + queued renewal.
        SET @SeedI=1;
        WHILE @SeedI <= 80
        BEGIN
            SET @SeedMemberId=(SELECT id FROM @SeedMemberIds WHERE ordinal=@SeedI);
            SET @SeedPackageOrdinal=CASE WHEN @SeedI<=4 THEN 1 ELSE 1+@SeedI%4 END;
            SELECT @SeedPackageId=id,@SeedPackageName=name,@SeedDuration=duration_months,@SeedPrice=price
            FROM @SeedPackageIds WHERE ordinal=@SeedPackageOrdinal;
            SET @SeedPaidDate=DATEADD(DAY,
                CASE @SeedI WHEN 1 THEN -150 WHEN 2 THEN -10 WHEN 3 THEN -27 WHEN 4 THEN -10
                     ELSE -(160-(@SeedI*7)%140) END,@Today);
            SET @SeedCreated=DATEADD(HOUR,9,CAST(@SeedPaidDate AS DATETIME2(0)));
            SET @SeedPaid=DATEADD(HOUR,10,CAST(@SeedPaidDate AS DATETIME2(0)));
            SET @SeedStart=@SeedPaidDate;
            SET @SeedEnd=DATEADD(DAY,-1,DATEADD(MONTH,@SeedDuration,@SeedStart));
            INSERT INTO dbo.registrations
                (member_id,package_id,package_name_snapshot,duration_months_snapshot,price_snapshot,
                 status,created_at,created_by,payment_method,paid_at,confirmed_by,start_date,end_date,
                 cancelled_at,cancelled_by,cancel_reason)
            VALUES
                (@SeedMemberId,@SeedPackageId,@SeedPackageName,@SeedDuration,@SeedPrice,
                 'PAID',@SeedCreated,@SeedReceptionId,
                 CASE WHEN @SeedI%2=0 THEN 'CASH' ELSE 'TRANSFER' END,
                 @SeedPaid,@SeedManagerId,@SeedStart,@SeedEnd,NULL,NULL,NULL);
            SET @SeedI+=1;
        END;

        -- 20 subsequent confirmations for members 4..23.
        -- Recompute the start from existing PAID history in the same order as QT-05.
        SET @SeedI=4;
        WHILE @SeedI <= 23
        BEGIN
            SET @SeedMemberId=(SELECT id FROM @SeedMemberIds WHERE ordinal=@SeedI);
            SELECT @SeedPackageId=id,@SeedPackageName=name,@SeedDuration=duration_months,@SeedPrice=price
            FROM @SeedPackageIds WHERE ordinal=1;
            SET @SeedPaidDate=DATEADD(DAY,CASE WHEN @SeedI=4 THEN -5 ELSE -(2+@SeedI%12) END,@Today);
            SET @SeedLastEnd=(SELECT MAX(end_date) FROM dbo.registrations
                WHERE member_id=@SeedMemberId AND status='PAID');
            SET @SeedStart=CASE WHEN @SeedLastEnd>=@SeedPaidDate THEN DATEADD(DAY,1,@SeedLastEnd)
                               ELSE @SeedPaidDate END;
            SET @SeedEnd=DATEADD(DAY,-1,DATEADD(MONTH,@SeedDuration,@SeedStart));
            SET @SeedCreated=DATEADD(HOUR,9,CAST(@SeedPaidDate AS DATETIME2(0)));
            SET @SeedPaid=DATEADD(HOUR,10,CAST(@SeedPaidDate AS DATETIME2(0)));
            INSERT INTO dbo.registrations
                (member_id,package_id,package_name_snapshot,duration_months_snapshot,price_snapshot,
                 status,created_at,created_by,payment_method,paid_at,confirmed_by,start_date,end_date,
                 cancelled_at,cancelled_by,cancel_reason)
            VALUES
                (@SeedMemberId,@SeedPackageId,@SeedPackageName,@SeedDuration,@SeedPrice,
                 'PAID',@SeedCreated,@SeedReceptionId,
                 CASE WHEN @SeedI%2=0 THEN 'TRANSFER' ELSE 'CASH' END,
                 @SeedPaid,@SeedManagerId,@SeedStart,@SeedEnd,NULL,NULL,NULL);
            SET @SeedI+=1;
        END;

        -- 30 PENDING purchases for nondeleted members 61..90.
        -- Some refer to the now-inactive package: QT-04 still permits confirmation.
        SET @SeedI=1;
        WHILE @SeedI <= 30
        BEGIN
            SET @SeedMemberId=(SELECT id FROM @SeedMemberIds WHERE ordinal=60+@SeedI);
            SET @SeedPackageOrdinal=CASE WHEN @SeedI%6=0 THEN 5 ELSE 1+@SeedI%4 END;
            SELECT @SeedPackageId=id,@SeedPackageName=name,@SeedDuration=duration_months,@SeedPrice=price
            FROM @SeedPackageIds WHERE ordinal=@SeedPackageOrdinal;
            SET @SeedCreated=DATEADD(HOUR,9,
                CAST(DATEADD(DAY,-(1+(@SeedI*5)%35),@Today) AS DATETIME2(0)));
            INSERT INTO dbo.registrations
                (member_id,package_id,package_name_snapshot,duration_months_snapshot,price_snapshot,
                 status,created_at,created_by,payment_method,paid_at,confirmed_by,start_date,end_date,
                 cancelled_at,cancelled_by,cancel_reason)
            VALUES
                (@SeedMemberId,@SeedPackageId,@SeedPackageName,@SeedDuration,@SeedPrice,
                 'PENDING',@SeedCreated,@SeedReceptionId,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
            SET @SeedI+=1;
        END;

        -- 20 CANCELLED purchases for members 81..100, including the deleted eight.
        SET @SeedI=1;
        WHILE @SeedI <= 20
        BEGIN
            SET @SeedMemberId=(SELECT id FROM @SeedMemberIds WHERE ordinal=80+@SeedI);
            SET @SeedPackageOrdinal=1+@SeedI%4;
            SELECT @SeedPackageId=id,@SeedPackageName=name,@SeedDuration=duration_months,@SeedPrice=price
            FROM @SeedPackageIds WHERE ordinal=@SeedPackageOrdinal;
            SET @SeedCreated=DATEADD(HOUR,9,
                CAST(DATEADD(DAY,-(1+@SeedI%20),@Today) AS DATETIME2(0)));
            INSERT INTO dbo.registrations
                (member_id,package_id,package_name_snapshot,duration_months_snapshot,price_snapshot,
                 status,created_at,created_by,payment_method,paid_at,confirmed_by,start_date,end_date,
                 cancelled_at,cancelled_by,cancel_reason)
            VALUES
                (@SeedMemberId,@SeedPackageId,@SeedPackageName,@SeedDuration,@SeedPrice,
                 'CANCELLED',@SeedCreated,@SeedReceptionId,NULL,NULL,NULL,NULL,NULL,
                 DATEADD(HOUR,2,@SeedCreated),@SeedManagerId,
                 CASE WHEN @SeedI>=13 THEN N'Xóa hồ sơ hội viên'
                      ELSE N'Hội viên thay đổi nhu cầu đăng ký' END);
            SET @SeedI+=1;
        END;

        -- Postconditions are checked before the parent's transaction can commit.
        IF (SELECT COUNT(*) FROM dbo.accounts)<>6
           OR (SELECT COUNT(*) FROM dbo.packages)<>5
           OR (SELECT COUNT(*) FROM dbo.members)<>100
           OR (SELECT COUNT(*) FROM dbo.members WHERE is_deleted=1)<>8
           OR (SELECT COUNT(*) FROM dbo.registrations)<>150
           OR (SELECT COUNT(*) FROM dbo.registrations WHERE status='PAID')<>100
           OR (SELECT COUNT(*) FROM dbo.registrations WHERE status='PENDING')<>30
           OR (SELECT COUNT(*) FROM dbo.registrations WHERE status='CANCELLED')<>20
        BEGIN
            ;THROW 52101, N'Demo seed row counts do not match the required fixtures.', 1;
        END;
        IF EXISTS (SELECT role FROM dbo.accounts GROUP BY role HAVING COUNT(*)<>2)
           OR (SELECT COUNT(*) FROM dbo.accounts WHERE status='LOCKED')<>1
           OR NOT EXISTS (SELECT 1 FROM dbo.accounts WHERE username='reception2' AND status='LOCKED')
        BEGIN
            ;THROW 52102, N'Demo account roles or locked-account fixture is invalid.', 1;
        END;
        IF EXISTS (
            SELECT 1 FROM dbo.registrations a JOIN dbo.registrations b
                ON a.member_id=b.member_id AND a.id<b.id
               AND a.status='PAID' AND b.status='PAID'
               AND a.start_date<=b.end_date AND b.start_date<=a.end_date
        )
        BEGIN
            ;THROW 52103, N'Demo PAID schedules overlap.', 1;
        END;
        IF EXISTS (
            SELECT 1 FROM dbo.registrations r
            OUTER APPLY (
                SELECT MAX(p.end_date) AS previous_end FROM dbo.registrations p
                WHERE p.member_id=r.member_id AND p.status='PAID'
                  AND (p.paid_at<r.paid_at OR (p.paid_at=r.paid_at AND p.id<r.id))
            ) prior
            WHERE r.status='PAID' AND (
                r.end_date<>DATEADD(DAY,-1,DATEADD(MONTH,r.duration_months_snapshot,r.start_date))
                OR r.start_date<>CASE WHEN prior.previous_end>=CAST(r.paid_at AS DATE)
                    THEN DATEADD(DAY,1,prior.previous_end) ELSE CAST(r.paid_at AS DATE) END
            )
        )
        BEGIN
            ;THROW 52104, N'Demo PAID schedules do not follow QT-05 confirmation order.', 1;
        END;
        IF EXISTS (
            SELECT 1 FROM dbo.registrations r JOIN dbo.members m ON m.id=r.member_id
            WHERE r.created_at<m.created_at
               OR (r.paid_at IS NOT NULL AND r.paid_at<r.created_at)
               OR (r.cancelled_at IS NOT NULL AND r.cancelled_at<r.created_at)
               OR r.created_at>@Now OR r.paid_at>@Now OR r.cancelled_at>@Now
        )
        BEGIN
            ;THROW 52105, N'Demo history contains an invalid creation/payment/cancellation time.', 1;
        END;

        DECLARE @SeedExpiredId INT=(SELECT id FROM @SeedMemberIds WHERE ordinal=1);
        DECLARE @SeedActiveId INT=(SELECT id FROM @SeedMemberIds WHERE ordinal=2);
        DECLARE @SeedExpiringId INT=(SELECT id FROM @SeedMemberIds WHERE ordinal=3);
        DECLARE @SeedRenewalId INT=(SELECT id FROM @SeedMemberIds WHERE ordinal=4);
        DECLARE @SeedNoPlanId INT=(SELECT id FROM @SeedMemberIds WHERE ordinal=91);
        IF NOT EXISTS (SELECT 1 FROM dbo.registrations WHERE member_id=@SeedExpiredId AND status='PAID')
           OR EXISTS (SELECT 1 FROM dbo.registrations WHERE member_id=@SeedExpiredId AND status='PAID' AND end_date>=@Today)
           OR NOT EXISTS (SELECT 1 FROM dbo.registrations WHERE member_id=@SeedActiveId AND status='PAID'
                          AND start_date<=@Today AND end_date>=@Today)
           OR NOT EXISTS (SELECT 1 FROM dbo.registrations WHERE member_id=@SeedExpiringId AND status='PAID'
                          AND start_date<=@Today AND end_date BETWEEN @Today AND DATEADD(DAY,6,@Today))
           OR EXISTS (SELECT 1 FROM dbo.registrations WHERE member_id=@SeedExpiringId AND status='PAID'
                      AND end_date>DATEADD(DAY,6,@Today))
           OR NOT EXISTS (SELECT 1 FROM dbo.registrations WHERE member_id=@SeedRenewalId AND status='PAID'
                          AND start_date<=@Today AND end_date>=@Today)
           OR NOT EXISTS (SELECT 1 FROM dbo.registrations WHERE member_id=@SeedRenewalId AND status='PAID' AND start_date>@Today)
           OR (SELECT COUNT(*) FROM dbo.registrations WHERE member_id=@SeedRenewalId AND status='PAID')<>2
           OR EXISTS (SELECT 1 FROM dbo.registrations WHERE member_id=@SeedNoPlanId AND status='PAID')
           OR NOT EXISTS (SELECT 1 FROM dbo.members WHERE id=@SeedNoPlanId AND is_deleted=0)
        BEGIN
            ;THROW 52106, N'Demo expired/active/expiring/renewal/no-plan fixtures are invalid.', 1;
        END;
        IF NOT EXISTS (SELECT 1 FROM dbo.registrations r JOIN dbo.packages p ON p.id=r.package_id
                       WHERE r.status='PENDING' AND p.is_active=0)
           OR NOT EXISTS (SELECT 1 FROM dbo.registrations WHERE status='PAID' AND payment_method='CASH')
           OR NOT EXISTS (SELECT 1 FROM dbo.registrations WHERE status='PAID' AND payment_method='TRANSFER')
           OR EXISTS (SELECT 1 FROM dbo.members m JOIN dbo.registrations r ON r.member_id=m.id
                      WHERE m.is_deleted=1 AND (r.status='PENDING' OR (r.status='PAID' AND r.end_date>=@Today)))
        BEGIN
            ;THROW 52107, N'Demo payment/deactivation/soft-delete fixtures are invalid.', 1;
        END;
        -- UPCOMING-only membership cannot arise from QT-05 with complete retained history:
        -- a future start requires an earlier unexpired PAID interval for this member.
        -- The seed includes valid future follow-on purchases while membership remains ACTIVE.
        PRINT N'Demo data ready: 6 accounts, 5 packages, 100 members (8 deleted), 150 registrations (100 PAID / 30 PENDING / 20 CANCELLED).';
    END;

    -- Everything created by this run commits together. CREATE DATABASE is separate.
    COMMIT TRANSACTION;
    PRINT N'GymDB Baseline 2.0 setup completed.';
END TRY
BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;

-- Verification output (also useful after schema-only setup).
SELECT N'accounts' AS table_name, COUNT_BIG(*) AS total_rows FROM dbo.accounts
UNION ALL SELECT N'members', COUNT_BIG(*) FROM dbo.members
UNION ALL SELECT N'packages', COUNT_BIG(*) FROM dbo.packages
UNION ALL SELECT N'registrations', COUNT_BIG(*) FROM dbo.registrations;

SELECT role,status,COUNT_BIG(*) AS account_count
FROM dbo.accounts GROUP BY role,status ORDER BY role,status;
SELECT status, payment_method, COUNT_BIG(*) AS registration_count,
       SUM(CAST(price_snapshot AS BIGINT)) AS snapshot_total_vnd
FROM dbo.registrations GROUP BY status,payment_method ORDER BY status,payment_method;

-- QT-07: status is derived, never stored on members.
SELECT m.id, N'HV' + CASE WHEN m.id < 1000000 THEN RIGHT(N'000000' + CONVERT(NVARCHAR(10),m.id),6)
                  ELSE CONVERT(NVARCHAR(10),m.id) END AS member_code,
       m.full_name, m.is_deleted,
       CASE
         WHEN EXISTS (SELECT 1 FROM dbo.registrations r WHERE r.member_id=m.id
             AND r.status='PAID' AND r.start_date<=@Today AND r.end_date>=@Today) THEN N'Đang hiệu lực'
         WHEN EXISTS (SELECT 1 FROM dbo.registrations r WHERE r.member_id=m.id
             AND r.status='PAID' AND r.start_date>@Today) THEN N'Chờ hiệu lực'
         WHEN EXISTS (SELECT 1 FROM dbo.registrations r WHERE r.member_id=m.id
             AND r.status='PAID') THEN N'Hết hạn'
         ELSE N'Chưa có gói'
       END AS membership_status,
       (SELECT MAX(r.end_date) FROM dbo.registrations r
        WHERE r.member_id=m.id AND r.status='PAID') AS latest_paid_end_date,
       CASE WHEN m.is_deleted=0
         AND EXISTS (SELECT 1 FROM dbo.registrations r WHERE r.member_id=m.id
             AND r.status='PAID' AND r.start_date<=@Today AND r.end_date>=@Today)
         AND (SELECT MAX(r.end_date) FROM dbo.registrations r
             WHERE r.member_id=m.id AND r.status='PAID') BETWEEN @Today AND DATEADD(DAY,6,@Today)
         THEN 1 ELSE 0 END AS is_expiring_soon
FROM dbo.members m ORDER BY m.id;
GO
