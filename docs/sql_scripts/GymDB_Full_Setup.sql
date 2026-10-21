-- GYM MANAGEMENT SYSTEM | SRS v1.1 MUST | SQL Server 2022
-- DEV/TEST ONLY. Does NOT drop an existing database. Stops if any target table exists.
-- Times stored as Vietnam local DATETIME2(0), not UTC.
USE master;
GO
IF DB_ID(N'GymDB') IS NULL EXEC(N'CREATE DATABASE GymDB');
GO
USE GymDB;
GO
SET XACT_ABORT ON;
IF EXISTS (SELECT 1 FROM sys.tables WHERE name IN ('accounts','members','plan_types','memberships','payment_transactions','visits') AND schema_id=SCHEMA_ID('dbo'))
    THROW 51000, 'Target tables already exist. Run on a fresh GymDB to avoid overwriting data.', 1;
GO
BEGIN TRY
BEGIN TRAN;
CREATE TABLE dbo.accounts (
 id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_accounts PRIMARY KEY,
 username NVARCHAR(50) COLLATE Latin1_General_100_CI_AS NOT NULL CONSTRAINT UQ_accounts_username UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 full_name NVARCHAR(100) NOT NULL,
 phone VARCHAR(10) NOT NULL CONSTRAINT UQ_accounts_phone UNIQUE,
 email NVARCHAR(254) NULL,
 role VARCHAR(20) NOT NULL CONSTRAINT CK_accounts_role CHECK(role IN ('ADMINISTRATOR','OWNER','MANAGER','RECEPTIONIST','ACCOUNTANT')),
 status VARCHAR(10) NOT NULL CONSTRAINT DF_accounts_status DEFAULT 'ACTIVE' CONSTRAINT CK_accounts_status CHECK(status IN ('ACTIVE','LOCKED')),
 is_deleted BIT NOT NULL CONSTRAINT DF_accounts_deleted DEFAULT 0,
 deleted_at DATETIME2(0) NULL,
 created_at DATETIME2(0) NOT NULL CONSTRAINT DF_accounts_created DEFAULT (DATEADD(HOUR,7,SYSUTCDATETIME())),
 updated_at DATETIME2(0) NULL,
 CONSTRAINT CK_accounts_phone CHECK(phone NOT LIKE '%[^0-9]%' AND LEN(phone)=10),
 CONSTRAINT CK_accounts_name CHECK(LEN(LTRIM(RTRIM(full_name))) BETWEEN 2 AND 100),
 CONSTRAINT CK_accounts_deleted CHECK((is_deleted=0 AND deleted_at IS NULL) OR (is_deleted=1 AND deleted_at IS NOT NULL))
);
CREATE TABLE dbo.members (
 id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_members PRIMARY KEY,
 full_name NVARCHAR(100) NOT NULL,
 phone VARCHAR(10) NOT NULL CONSTRAINT UQ_members_phone UNIQUE,
 email NVARCHAR(254) NULL,
 birth_date DATE NULL,
 gender NVARCHAR(10) NULL CONSTRAINT CK_members_gender CHECK(gender IN (N'Nam',N'Nữ',N'Khác')),
 created_at DATETIME2(0) NOT NULL CONSTRAINT DF_members_created DEFAULT (DATEADD(HOUR,7,SYSUTCDATETIME())),
 is_deleted BIT NOT NULL CONSTRAINT DF_members_deleted DEFAULT 0,
 deleted_at DATETIME2(0) NULL,
 deleted_by INT NULL CONSTRAINT FK_members_deleted_by REFERENCES dbo.accounts(id),
 CONSTRAINT CK_members_phone CHECK(phone LIKE '0[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
 CONSTRAINT CK_members_name CHECK(LEN(LTRIM(RTRIM(full_name))) BETWEEN 2 AND 100),
 CONSTRAINT CK_members_deleted CHECK((is_deleted=0 AND deleted_at IS NULL AND deleted_by IS NULL) OR (is_deleted=1 AND deleted_at IS NOT NULL AND deleted_by IS NOT NULL))
);
CREATE TABLE dbo.plan_types (
 id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_plan_types PRIMARY KEY,
 name NVARCHAR(100) NOT NULL,
 name_normalized NVARCHAR(100) COLLATE Vietnamese_100_CI_AS NOT NULL CONSTRAINT UQ_plan_types_name UNIQUE,
 duration_value INT NOT NULL,
 duration_unit VARCHAR(10) NOT NULL,
 price DECIMAL(12,0) NOT NULL CONSTRAINT CK_plan_types_price CHECK(price>0),
 created_at DATETIME2(0) NOT NULL CONSTRAINT DF_plan_types_created DEFAULT (DATEADD(HOUR,7,SYSUTCDATETIME())),
 created_by INT NOT NULL CONSTRAINT FK_plan_types_created_by REFERENCES dbo.accounts(id),
 updated_at DATETIME2(0) NULL,
 updated_by INT NULL CONSTRAINT FK_plan_types_updated_by REFERENCES dbo.accounts(id),
 CONSTRAINT UQ_plan_types_duration UNIQUE(duration_value,duration_unit),
 CONSTRAINT CK_plan_types_duration CHECK ((duration_value=1 AND duration_unit IN ('DAY','MONTH','YEAR')) OR (duration_value=6 AND duration_unit='MONTH'))
);
CREATE TABLE dbo.memberships (
 id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_memberships PRIMARY KEY,
 member_id INT NOT NULL CONSTRAINT FK_memberships_member REFERENCES dbo.members(id),
 plan_type_id INT NOT NULL CONSTRAINT FK_memberships_plan REFERENCES dbo.plan_types(id),
 plan_name_snapshot NVARCHAR(100) NOT NULL,
 duration_value_snapshot INT NOT NULL CONSTRAINT CK_memberships_duration_value CHECK(duration_value_snapshot>0),
 duration_unit_snapshot VARCHAR(10) NOT NULL CONSTRAINT CK_memberships_duration_unit CHECK(duration_unit_snapshot IN ('DAY','MONTH','YEAR')),
 created_at DATETIME2(0) NOT NULL CONSTRAINT DF_memberships_created DEFAULT (DATEADD(HOUR,7,SYSUTCDATETIME())),
 created_by INT NOT NULL CONSTRAINT FK_memberships_creator REFERENCES dbo.accounts(id),
 start_at DATETIME2(0) NULL,
 end_exclusive DATETIME2(0) NULL,
 CONSTRAINT UQ_memberships_id_member UNIQUE(id,member_id),
 CONSTRAINT CK_memberships_period CHECK((start_at IS NULL AND end_exclusive IS NULL) OR (start_at IS NOT NULL AND end_exclusive IS NOT NULL AND end_exclusive>start_at AND CAST(start_at AS TIME)='00:00:00'))
);
CREATE TABLE dbo.payment_transactions (
 id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_payment_transactions PRIMARY KEY,
 membership_id INT NOT NULL CONSTRAINT UQ_payment_membership UNIQUE,
 member_id INT NOT NULL CONSTRAINT FK_payment_member REFERENCES dbo.members(id),
 plan_name_snapshot NVARCHAR(100) NOT NULL,
 amount DECIMAL(12,0) NOT NULL CONSTRAINT CK_payment_amount CHECK(amount>0),
 method VARCHAR(15) NOT NULL CONSTRAINT CK_payment_method CHECK(method IN ('CASH','BANK_TRANSFER')),
 status VARCHAR(10) NOT NULL CONSTRAINT CK_payment_status CHECK(status IN ('PENDING','PAID','CANCELLED')),
 created_at DATETIME2(0) NOT NULL CONSTRAINT DF_payment_created DEFAULT (DATEADD(HOUR,7,SYSUTCDATETIME())),
 created_by INT NOT NULL CONSTRAINT FK_payment_created_by REFERENCES dbo.accounts(id),
 paid_at DATETIME2(0) NULL,
 confirmed_by INT NULL CONSTRAINT FK_payment_confirmed_by REFERENCES dbo.accounts(id),
 paid_sequence BIGINT NULL,
 cancelled_at DATETIME2(0) NULL,
 cancelled_by INT NULL CONSTRAINT FK_payment_cancelled_by REFERENCES dbo.accounts(id),
 proof_image_path NVARCHAR(500) NULL,
 CONSTRAINT FK_payment_membership_member FOREIGN KEY(membership_id,member_id) REFERENCES dbo.memberships(id,member_id),
 CONSTRAINT CK_payment_state CHECK(
 (status='PENDING' AND paid_at IS NULL AND confirmed_by IS NULL AND paid_sequence IS NULL AND cancelled_at IS NULL AND cancelled_by IS NULL)
 OR (status='PAID' AND paid_at IS NOT NULL AND confirmed_by IS NOT NULL AND paid_sequence IS NOT NULL AND cancelled_at IS NULL AND cancelled_by IS NULL)
 OR (status='CANCELLED' AND paid_at IS NULL AND confirmed_by IS NULL AND paid_sequence IS NULL AND cancelled_at IS NOT NULL AND cancelled_by IS NOT NULL))
);
CREATE TABLE dbo.visits (
 id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_visits PRIMARY KEY,
 member_id INT NOT NULL CONSTRAINT FK_visits_member REFERENCES dbo.members(id),
 membership_id INT NOT NULL,
 created_by INT NOT NULL CONSTRAINT FK_visits_creator REFERENCES dbo.accounts(id),
 check_in_date DATE NOT NULL,
 check_in_at DATETIME2(0) NOT NULL,
 check_out_at DATETIME2(0) NULL,
 status VARCHAR(12) NOT NULL CONSTRAINT CK_visits_status CHECK(status IN ('OPEN','COMPLETED','AUTO_CLOSED')),
 closed_by_account_id INT NULL CONSTRAINT FK_visits_closed_by REFERENCES dbo.accounts(id),
 closed_by_system BIT NOT NULL CONSTRAINT DF_visits_system DEFAULT 0,
 CONSTRAINT FK_visits_membership_member FOREIGN KEY(membership_id,member_id) REFERENCES dbo.memberships(id,member_id),
 CONSTRAINT CK_visits_date CHECK(CAST(check_in_at AS DATE)=check_in_date AND CAST(check_in_at AS TIME)>='07:00:00' AND CAST(check_in_at AS TIME)<'20:00:00'),
 CONSTRAINT CK_visits_state CHECK(
 (status='OPEN' AND check_out_at IS NULL AND closed_by_account_id IS NULL AND closed_by_system=0)
 OR (status='COMPLETED' AND check_out_at IS NOT NULL AND check_out_at>check_in_at AND CAST(check_out_at AS DATE)=check_in_date AND CAST(check_out_at AS TIME)<'20:00:00' AND closed_by_account_id IS NOT NULL AND closed_by_system=0)
 OR (status='AUTO_CLOSED' AND check_out_at=DATEADD(HOUR,20,CAST(check_in_date AS DATETIME2(0))) AND closed_by_account_id IS NULL AND closed_by_system=1))
);
CREATE UNIQUE INDEX UX_visits_one_open ON dbo.visits(member_id) WHERE status='OPEN';
CREATE UNIQUE INDEX UX_payment_paid_sequence ON dbo.payment_transactions(paid_sequence) WHERE paid_sequence IS NOT NULL;
CREATE INDEX IX_payment_paid_at ON dbo.payment_transactions(paid_at) INCLUDE(amount) WHERE status='PAID';
CREATE INDEX IX_payment_created_at ON dbo.payment_transactions(created_at) WHERE status IN ('PENDING','CANCELLED');
CREATE INDEX IX_visits_check_in ON dbo.visits(member_id,check_in_at);
CREATE INDEX IX_memberships_member_period ON dbo.memberships(member_id,start_at,end_exclusive);
CREATE SEQUENCE dbo.seq_paid_sequence AS BIGINT START WITH 1 INCREMENT BY 1;

INSERT INTO dbo.accounts(username,password_hash,full_name,phone,email,role,created_at) VALUES (N'admin','pbkdf2_sha256$210000$ec1ce2b70f259b09c2cc2e9ef6a0c4ea$d03a984d23fb1c677a7e396a5db0503a327fd82cdce31b85081f9a13e173e105',N'Nguyễn Minh Quân','0901000001','admin@gmail.com','ADMINISTRATOR','2026-09-01T09:00:00');
INSERT INTO dbo.accounts(username,password_hash,full_name,phone,email,role,created_at) VALUES (N'owner','pbkdf2_sha256$210000$1a589e3f3a9dfcae8773bab94ab8af71$6d4fdda8fa22bc7b1e0884c44545cbb01f64a79dad002ab083a1eeaceaa6f7e6',N'Trần Hoàng Anh','0901000002','owner@gmail.com','OWNER','2026-09-01T09:00:00');
INSERT INTO dbo.accounts(username,password_hash,full_name,phone,email,role,created_at) VALUES (N'manager','pbkdf2_sha256$210000$2e091447334f33f33663579ca3db1ba8$5cb1e413479c67a9aac8a9c0521a27aa04c0a1ca68eecc56960afe56b9741558',N'Lê Quốc Bảo','0901000003','manager@gmail.com','MANAGER','2026-09-01T09:00:00');
INSERT INTO dbo.accounts(username,password_hash,full_name,phone,email,role,created_at) VALUES (N'receptionist','pbkdf2_sha256$210000$77f3b78d503a6149d3d4fc4e9bcb8808$b16abcec315d1e1133f0d4ead8684621ac4c722503e6e1c619760892c6a9a7bc',N'Phạm Ngọc Mai','0901000004','receptionist@gmail.com','RECEPTIONIST','2026-09-01T09:00:00');
INSERT INTO dbo.accounts(username,password_hash,full_name,phone,email,role,created_at) VALUES (N'accountant','pbkdf2_sha256$210000$ead5ffb5b77c213582c14f6dfc5c28e7$9a017062117bc735ef92b0ead881e47b1cba3624ec10b99728ef034e1db6fd5c',N'Võ Thanh Hà','0901000005','accountant@gmail.com','ACCOUNTANT','2026-09-01T09:00:00');
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Nguyễn Văn Lan','0900000001','member001@gmail.com','1991-02-02',N'Nữ','2026-09-02T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Trần Văn Thảo','0900000002','member002@gmail.com','1992-03-03',N'Khác','2026-09-03T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Lê Văn Bình','0900000003','member003@gmail.com','1993-04-04',N'Nam','2026-09-04T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Phạm Văn Long','0900000004','member004@gmail.com','1994-05-05',N'Nữ','2026-09-05T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Hoàng Văn Trang','0900000005','member005@gmail.com','1995-06-06',N'Khác','2026-09-06T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Võ Văn Chi','0900000006','member006@gmail.com','1996-07-07',N'Nam','2026-09-07T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đặng Văn Mai','0900000007','member007@gmail.com','1997-08-08',N'Nữ','2026-09-08T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Bùi Văn Trúc','0900000008','member008@gmail.com','1998-09-09',N'Khác','2026-09-09T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đỗ Văn Dũng','0900000009','member009@gmail.com','1999-10-10',N'Nam','2026-09-10T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Hồ Văn Nam','0900000010','member010@gmail.com','2000-11-11',N'Nữ','2026-09-11T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Ngô Văn Tú','0900000011','member011@gmail.com','2001-12-12',N'Khác','2026-09-12T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Dương Văn Hà','0900000012','member012@gmail.com','2002-01-13',N'Nam','2026-09-13T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Lý Văn Ngân','0900000013','member013@gmail.com','1990-02-14',N'Nữ','2026-09-14T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Huỳnh Văn Vy','0900000014','member014@gmail.com','1991-03-15',N'Khác','2026-09-15T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Mai Văn Hùng','0900000015','member015@gmail.com','1992-04-16',N'Nam','2026-09-16T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đinh Văn Phúc','0900000016','member016@gmail.com','1993-05-17',N'Nữ','2026-09-17T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Tạ Văn Yến','0900000017','member017@gmail.com','1994-06-18',N'Khác','2026-09-18T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Cao Văn Khánh','0900000018','member018@gmail.com','1995-07-19',N'Nam','2026-09-19T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Châu Văn Quân','0900000019','member019@gmail.com','1996-08-20',N'Nữ','2026-09-20T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Trịnh Văn An','0900000020','member020@gmail.com','1997-09-21',N'Khác','2026-09-21T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Nguyễn Thị Lan','0900000021','member021@gmail.com','1998-10-22',N'Nam','2026-09-22T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Trần Thị Thảo','0900000022','member022@gmail.com','1999-11-23',N'Nữ','2026-09-23T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Lê Thị Bình','0900000023','member023@gmail.com','2000-12-24',N'Khác','2026-09-24T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Phạm Thị Long','0900000024','member024@gmail.com','2001-01-25',N'Nam','2026-09-25T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Hoàng Thị Trang','0900000025','member025@gmail.com','2002-02-26',N'Nữ','2026-09-01T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Võ Thị Chi','0900000026','member026@gmail.com','1990-03-27',N'Khác','2026-09-02T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đặng Thị Mai','0900000027','member027@gmail.com','1991-04-01',N'Nam','2026-09-03T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Bùi Thị Trúc','0900000028','member028@gmail.com','1992-05-02',N'Nữ','2026-09-04T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đỗ Thị Dũng','0900000029','member029@gmail.com','1993-06-03',N'Khác','2026-09-05T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Hồ Thị Nam','0900000030','member030@gmail.com','1994-07-04',N'Nam','2026-09-06T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Ngô Thị Tú','0900000031','member031@gmail.com','1995-08-05',N'Nữ','2026-09-07T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Dương Thị Hà','0900000032','member032@gmail.com','1996-09-06',N'Khác','2026-09-08T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Lý Thị Ngân','0900000033','member033@gmail.com','1997-10-07',N'Nam','2026-09-09T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Huỳnh Thị Vy','0900000034','member034@gmail.com','1998-11-08',N'Nữ','2026-09-10T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Mai Thị Hùng','0900000035','member035@gmail.com','1999-12-09',N'Khác','2026-09-11T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đinh Thị Phúc','0900000036','member036@gmail.com','2000-01-10',N'Nam','2026-09-12T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Tạ Thị Yến','0900000037','member037@gmail.com','2001-02-11',N'Nữ','2026-09-13T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Cao Thị Khánh','0900000038','member038@gmail.com','2002-03-12',N'Khác','2026-09-14T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Châu Thị Quân','0900000039','member039@gmail.com','1990-04-13',N'Nam','2026-09-15T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Trịnh Thị An','0900000040','member040@gmail.com','1991-05-14',N'Nữ','2026-09-16T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Nguyễn Minh Lan','0900000041','member041@gmail.com','1992-06-15',N'Khác','2026-09-17T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Trần Minh Thảo','0900000042','member042@gmail.com','1993-07-16',N'Nam','2026-09-18T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Lê Minh Bình','0900000043','member043@gmail.com','1994-08-17',N'Nữ','2026-09-19T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Phạm Minh Long','0900000044','member044@gmail.com','1995-09-18',N'Khác','2026-09-20T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Hoàng Minh Trang','0900000045','member045@gmail.com','1996-10-19',N'Nam','2026-09-21T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Võ Minh Chi','0900000046','member046@gmail.com','1997-11-20',N'Nữ','2026-09-22T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đặng Minh Mai','0900000047','member047@gmail.com','1998-12-21',N'Khác','2026-09-23T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Bùi Minh Trúc','0900000048','member048@gmail.com','1999-01-22',N'Nam','2026-09-24T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đỗ Minh Dũng','0900000049','member049@gmail.com','2000-02-23',N'Nữ','2026-09-25T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Hồ Minh Nam','0900000050','member050@gmail.com','2001-03-24',N'Khác','2026-09-01T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Ngô Minh Tú','0900000051','member051@gmail.com','2002-04-25',N'Nam','2026-09-02T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Dương Minh Hà','0900000052','member052@gmail.com','1990-05-26',N'Nữ','2026-09-03T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Lý Minh Ngân','0900000053','member053@gmail.com','1991-06-27',N'Khác','2026-09-04T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Huỳnh Minh Vy','0900000054','member054@gmail.com','1992-07-01',N'Nam','2026-09-05T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Mai Minh Hùng','0900000055','member055@gmail.com','1993-08-02',N'Nữ','2026-09-06T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đinh Minh Phúc','0900000056','member056@gmail.com','1994-09-03',N'Khác','2026-09-07T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Tạ Minh Yến','0900000057','member057@gmail.com','1995-10-04',N'Nam','2026-09-08T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Cao Minh Khánh','0900000058','member058@gmail.com','1996-11-05',N'Nữ','2026-09-09T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Châu Minh Quân','0900000059','member059@gmail.com','1997-12-06',N'Khác','2026-09-10T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Trịnh Minh An','0900000060','member060@gmail.com','1998-01-07',N'Nam','2026-09-11T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Nguyễn Quốc Lan','0900000061','member061@gmail.com','1999-02-08',N'Nữ','2026-09-12T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Trần Quốc Thảo','0900000062','member062@gmail.com','2000-03-09',N'Khác','2026-09-13T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Lê Quốc Bình','0900000063','member063@gmail.com','2001-04-10',N'Nam','2026-09-14T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Phạm Quốc Long','0900000064','member064@gmail.com','2002-05-11',N'Nữ','2026-09-15T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Hoàng Quốc Trang','0900000065','member065@gmail.com','1990-06-12',N'Khác','2026-09-16T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Võ Quốc Chi','0900000066','member066@gmail.com','1991-07-13',N'Nam','2026-09-17T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đặng Quốc Mai','0900000067','member067@gmail.com','1992-08-14',N'Nữ','2026-09-18T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Bùi Quốc Trúc','0900000068','member068@gmail.com','1993-09-15',N'Khác','2026-09-19T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đỗ Quốc Dũng','0900000069','member069@gmail.com','1994-10-16',N'Nam','2026-09-20T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Hồ Quốc Nam','0900000070','member070@gmail.com','1995-11-17',N'Nữ','2026-09-21T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Ngô Quốc Tú','0900000071','member071@gmail.com','1996-12-18',N'Khác','2026-09-22T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Dương Quốc Hà','0900000072','member072@gmail.com','1997-01-19',N'Nam','2026-09-23T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Lý Quốc Ngân','0900000073','member073@gmail.com','1998-02-20',N'Nữ','2026-09-24T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Huỳnh Quốc Vy','0900000074','member074@gmail.com','1999-03-21',N'Khác','2026-09-25T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Mai Quốc Hùng','0900000075','member075@gmail.com','2000-04-22',N'Nam','2026-09-01T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đinh Quốc Phúc','0900000076','member076@gmail.com','2001-05-23',N'Nữ','2026-09-02T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Tạ Quốc Yến','0900000077','member077@gmail.com','2002-06-24',N'Khác','2026-09-03T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Cao Quốc Khánh','0900000078','member078@gmail.com','1990-07-25',N'Nam','2026-09-04T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Châu Quốc Quân','0900000079','member079@gmail.com','1991-08-26',N'Nữ','2026-09-05T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Trịnh Quốc An','0900000080','member080@gmail.com','1992-09-27',N'Khác','2026-09-06T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Nguyễn Ngọc Lan','0900000081','member081@gmail.com','1993-10-01',N'Nam','2026-09-07T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Trần Ngọc Thảo','0900000082','member082@gmail.com','1994-11-02',N'Nữ','2026-09-08T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Lê Ngọc Bình','0900000083','member083@gmail.com','1995-12-03',N'Khác','2026-09-09T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Phạm Ngọc Long','0900000084','member084@gmail.com','1996-01-04',N'Nam','2026-09-10T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Hoàng Ngọc Trang','0900000085','member085@gmail.com','1997-02-05',N'Nữ','2026-09-11T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Võ Ngọc Chi','0900000086','member086@gmail.com','1998-03-06',N'Khác','2026-09-12T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đặng Ngọc Mai','0900000087','member087@gmail.com','1999-04-07',N'Nam','2026-09-13T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Bùi Ngọc Trúc','0900000088','member088@gmail.com','2000-05-08',N'Nữ','2026-09-14T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đỗ Ngọc Dũng','0900000089','member089@gmail.com','2001-06-09',N'Khác','2026-09-15T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Hồ Ngọc Nam','0900000090','member090@gmail.com','2002-07-10',N'Nam','2026-09-16T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Ngô Ngọc Tú','0900000091','member091@gmail.com','1990-08-11',N'Nữ','2026-09-17T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Dương Ngọc Hà','0900000092','member092@gmail.com','1991-09-12',N'Khác','2026-09-18T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Lý Ngọc Ngân','0900000093','member093@gmail.com','1992-10-13',N'Nam','2026-09-19T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Huỳnh Ngọc Vy','0900000094','member094@gmail.com','1993-11-14',N'Nữ','2026-09-20T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Mai Ngọc Hùng','0900000095','member095@gmail.com','1994-12-15',N'Khác','2026-09-21T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Đinh Ngọc Phúc','0900000096','member096@gmail.com','1995-01-16',N'Nam','2026-09-22T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Tạ Ngọc Yến','0900000097','member097@gmail.com','1996-02-17',N'Nữ','2026-09-23T09:00:00',0,NULL,NULL);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Cao Ngọc Khánh','0900000098','member098@gmail.com','1997-03-18',N'Khác','2026-09-24T09:00:00',1,'2026-10-10T10:00:00',3);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Châu Ngọc Quân','0900000099','member099@gmail.com','1998-04-19',N'Nam','2026-09-25T09:00:00',1,'2026-10-10T10:00:00',3);
INSERT INTO dbo.members(full_name,phone,email,birth_date,gender,created_at,is_deleted,deleted_at,deleted_by) VALUES (N'Trịnh Ngọc An','0900000100','member100@gmail.com','1999-05-20',N'Nữ','2026-09-01T09:00:00',0,NULL,NULL);
INSERT INTO dbo.plan_types(name,name_normalized,duration_value,duration_unit,price,created_at,created_by) VALUES (N'Daily Pass',N'daily pass',1,'DAY',50000,'2026-09-01T10:00:00',2);
INSERT INTO dbo.plan_types(name,name_normalized,duration_value,duration_unit,price,created_at,created_by) VALUES (N'Monthly Pass',N'monthly pass',1,'MONTH',500000,'2026-09-01T10:00:00',2);
INSERT INTO dbo.plan_types(name,name_normalized,duration_value,duration_unit,price,created_at,created_by) VALUES (N'6-Month Pass',N'6-month pass',6,'MONTH',2700000,'2026-09-01T10:00:00',2);
INSERT INTO dbo.plan_types(name,name_normalized,duration_value,duration_unit,price,created_at,created_by) VALUES (N'Yearly Pass',N'yearly pass',1,'YEAR',5000000,'2026-09-01T10:00:00',2);
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (1,2,N'Monthly Pass',1,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2026-11-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (2,2,N'Monthly Pass',1,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2026-11-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (3,3,N'6-Month Pass',6,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2027-04-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (4,4,N'Yearly Pass',1,'YEAR','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2027-10-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (5,1,N'Daily Pass',1,'DAY','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2026-10-02T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (6,2,N'Monthly Pass',1,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2026-11-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (7,3,N'6-Month Pass',6,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2027-04-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (8,4,N'Yearly Pass',1,'YEAR','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2027-10-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (9,1,N'Daily Pass',1,'DAY','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2026-10-02T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (10,2,N'Monthly Pass',1,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2026-11-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (11,3,N'6-Month Pass',6,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2027-04-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (12,4,N'Yearly Pass',1,'YEAR','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2027-10-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (13,1,N'Daily Pass',1,'DAY','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2026-10-02T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (14,2,N'Monthly Pass',1,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2026-11-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (15,3,N'6-Month Pass',6,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2027-04-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (16,4,N'Yearly Pass',1,'YEAR','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2027-10-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (17,1,N'Daily Pass',1,'DAY','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2026-10-02T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (18,2,N'Monthly Pass',1,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2026-11-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (19,3,N'6-Month Pass',6,'MONTH','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2027-04-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (20,4,N'Yearly Pass',1,'YEAR','2026-09-30T09:00:00',4,'2026-10-01T00:00:00','2027-10-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (21,1,N'Daily Pass',1,'DAY','2026-09-30T09:00:00',4,NULL,NULL);
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (22,2,N'Monthly Pass',1,'MONTH','2026-09-30T09:00:00',4,NULL,NULL);
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (23,3,N'6-Month Pass',6,'MONTH','2026-09-30T09:00:00',4,NULL,NULL);
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (24,4,N'Yearly Pass',1,'YEAR','2026-09-30T09:00:00',4,NULL,NULL);
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (25,1,N'Daily Pass',1,'DAY','2026-09-30T09:00:00',4,NULL,NULL);
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (26,2,N'Monthly Pass',1,'MONTH','2026-09-30T09:00:00',4,NULL,NULL);
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (27,3,N'6-Month Pass',6,'MONTH','2026-09-30T09:00:00',4,NULL,NULL);
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (28,4,N'Yearly Pass',1,'YEAR','2026-09-30T09:00:00',4,NULL,NULL);
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (1,2,N'Monthly Pass',1,'MONTH','2026-09-30T09:00:00',4,'2026-11-01T00:00:00','2026-12-01T00:00:00');
INSERT INTO dbo.memberships(member_id,plan_type_id,plan_name_snapshot,duration_value_snapshot,duration_unit_snapshot,created_at,created_by,start_at,end_exclusive) VALUES (2,2,N'Monthly Pass',1,'MONTH','2026-09-30T09:00:00',4,'2026-11-01T00:00:00','2026-12-01T00:00:00');
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (1,1,N'Monthly Pass',500000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:01:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (2,2,N'Monthly Pass',500000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:02:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (3,3,N'6-Month Pass',2700000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:03:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (4,4,N'Yearly Pass',5000000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:04:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (5,5,N'Daily Pass',50000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:05:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (6,6,N'Monthly Pass',500000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:06:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (7,7,N'6-Month Pass',2700000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:07:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (8,8,N'Yearly Pass',5000000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:08:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (9,9,N'Daily Pass',50000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:09:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (10,10,N'Monthly Pass',500000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:10:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (11,11,N'6-Month Pass',2700000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:11:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (12,12,N'Yearly Pass',5000000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:12:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (13,13,N'Daily Pass',50000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:13:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (14,14,N'Monthly Pass',500000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:14:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (15,15,N'6-Month Pass',2700000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:15:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (16,16,N'Yearly Pass',5000000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:16:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (17,17,N'Daily Pass',50000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:17:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (18,18,N'Monthly Pass',500000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:18:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (19,19,N'6-Month Pass',2700000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:19:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (20,20,N'Yearly Pass',5000000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:20:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (21,21,N'Daily Pass',50000,'CASH','PENDING','2026-09-30T09:00:00',4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (22,22,N'Monthly Pass',500000,'BANK_TRANSFER','PENDING','2026-09-30T09:00:00',4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (23,23,N'6-Month Pass',2700000,'CASH','PENDING','2026-09-30T09:00:00',4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (24,24,N'Yearly Pass',5000000,'BANK_TRANSFER','PENDING','2026-09-30T09:00:00',4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (25,25,N'Daily Pass',50000,'CASH','CANCELLED','2026-09-30T09:00:00',4,NULL,NULL,NULL,'2026-10-02T12:00:00',3);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (26,26,N'Monthly Pass',500000,'BANK_TRANSFER','CANCELLED','2026-09-30T09:00:00',4,NULL,NULL,NULL,'2026-10-02T12:00:00',3);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (27,27,N'6-Month Pass',2700000,'CASH','CANCELLED','2026-09-30T09:00:00',4,NULL,NULL,NULL,'2026-10-02T12:00:00',3);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (28,28,N'Yearly Pass',5000000,'BANK_TRANSFER','CANCELLED','2026-09-30T09:00:00',4,NULL,NULL,NULL,'2026-10-02T12:00:00',3);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (29,1,N'Monthly Pass',500000,'CASH','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:29:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.payment_transactions(membership_id,member_id,plan_name_snapshot,amount,method,status,created_at,created_by,paid_at,confirmed_by,paid_sequence,cancelled_at,cancelled_by) VALUES (30,2,N'Monthly Pass',500000,'BANK_TRANSFER','PAID','2026-09-30T09:00:00',4,'2026-09-30T10:30:00',3,NEXT VALUE FOR dbo.seq_paid_sequence,NULL,NULL);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (1,1,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T11:00:00','COMPLETED',4,0);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (2,2,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T11:00:00','COMPLETED',4,0);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (3,3,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T11:00:00','COMPLETED',4,0);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (4,4,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T11:00:00','COMPLETED',4,0);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (6,6,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T11:00:00','COMPLETED',4,0);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (7,7,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T11:00:00','COMPLETED',4,0);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (8,8,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T11:00:00','COMPLETED',4,0);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (10,10,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T11:00:00','COMPLETED',4,0);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (11,11,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T11:00:00','COMPLETED',4,0);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (12,12,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T11:00:00','COMPLETED',4,0);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (14,14,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T20:00:00','AUTO_CLOSED',NULL,1);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (15,15,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T20:00:00','AUTO_CLOSED',NULL,1);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (16,16,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T20:00:00','AUTO_CLOSED',NULL,1);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (18,18,4,'2026-10-02','2026-10-02T08:00:00','2026-10-02T20:00:00','AUTO_CLOSED',NULL,1);
INSERT INTO dbo.visits(member_id,membership_id,created_by,check_in_date,check_in_at,check_out_at,status,closed_by_account_id,closed_by_system) VALUES (19,19,4,'2026-10-02','2026-10-02T08:00:00',NULL,'OPEN',NULL,0);
COMMIT TRAN;
END TRY
BEGIN CATCH
 IF @@TRANCOUNT>0 ROLLBACK TRAN;
 THROW;
END CATCH;
GO
SELECT 'accounts' AS table_name,COUNT(*) AS total FROM dbo.accounts
UNION ALL SELECT 'members',COUNT(*) FROM dbo.members
UNION ALL SELECT 'plan_types',COUNT(*) FROM dbo.plan_types
UNION ALL SELECT 'memberships',COUNT(*) FROM dbo.memberships
UNION ALL SELECT 'payment_transactions',COUNT(*) FROM dbo.payment_transactions
UNION ALL SELECT 'visits',COUNT(*) FROM dbo.visits;
GO

