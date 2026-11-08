CREATE DATABASE GymDB;
GO
USE GymDB;
GO

-- 1. Staff accounts
CREATE TABLE dbo.accounts (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) COLLATE Latin1_General_100_CI_AS NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    phone CHAR(10) NOT NULL UNIQUE,
    email VARCHAR(254) NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN','MANAGER','RECEPTIONIST')),
    status VARCHAR(10) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','LOCKED')),
    created_at DATETIME2(0) NOT NULL
);

-- 2. Member profiles (is_deleted: 0 = in use, 1 = soft-deleted)
CREATE TABLE dbo.members (
    id INT IDENTITY(1,1) PRIMARY KEY,
    full_name NVARCHAR(100) NOT NULL,
    phone CHAR(10) NOT NULL UNIQUE,
    email VARCHAR(254) NULL,
    date_of_birth DATE NULL,
    gender NVARCHAR(10) NULL CHECK (gender IN (N'Male',N'Female',N'Other')),
    is_deleted BIT NOT NULL DEFAULT 0,
    created_at DATETIME2(0) NOT NULL,
    created_by INT NOT NULL REFERENCES dbo.accounts(id)
);

-- 3. Package catalog (is_active: 1 = on sale, 0 = discontinued)
CREATE TABLE dbo.packages (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) COLLATE Latin1_General_100_CI_AS NOT NULL UNIQUE,
    duration_months TINYINT NOT NULL CHECK (duration_months IN (1,3,6,12)),
    price INT NOT NULL CHECK (price BETWEEN 1 AND 100000000),
    is_active BIT NOT NULL DEFAULT 1,
    created_at DATETIME2(0) NOT NULL
);

-- 4. Package registrations and payment details
-- Snapshot = the package name/duration/price captured at registration time.
CREATE TABLE dbo.registrations (
    id INT IDENTITY(1,1) PRIMARY KEY,
    member_id INT NOT NULL REFERENCES dbo.members(id),
    package_id INT NOT NULL REFERENCES dbo.packages(id),
    package_name_snapshot NVARCHAR(100) NOT NULL,
    duration_months_snapshot TINYINT NOT NULL CHECK (duration_months_snapshot IN (1,3,6,12)),
    price_snapshot INT NOT NULL CHECK (price_snapshot BETWEEN 1 AND 100000000),
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','PAID','CANCELLED')),
    created_at DATETIME2(0) NOT NULL,
    created_by INT NOT NULL REFERENCES dbo.accounts(id),
    payment_method VARCHAR(10) NULL CHECK (payment_method IN ('CASH','TRANSFER')),
    paid_at DATETIME2(0) NULL,
    confirmed_by INT NULL REFERENCES dbo.accounts(id),
    start_date DATE NULL,
    end_date DATE NULL,
    cancelled_at DATETIME2(0) NULL,
    cancelled_by INT NULL REFERENCES dbo.accounts(id),
    cancel_reason NVARCHAR(255) NULL,

    -- Ensure the payment / cancellation columns are consistent with the status.
    CHECK (
        (status='PENDING'
         AND payment_method IS NULL AND paid_at IS NULL AND confirmed_by IS NULL
         AND start_date IS NULL AND end_date IS NULL
         AND cancelled_at IS NULL AND cancelled_by IS NULL AND cancel_reason IS NULL)
        OR
        (status='PAID'
         AND payment_method IS NOT NULL AND paid_at IS NOT NULL AND confirmed_by IS NOT NULL
         AND start_date IS NOT NULL AND end_date IS NOT NULL AND end_date>=start_date
         AND cancelled_at IS NULL AND cancelled_by IS NULL AND cancel_reason IS NULL)
        OR
        (status='CANCELLED'
         AND payment_method IS NULL AND paid_at IS NULL AND confirmed_by IS NULL
         AND start_date IS NULL AND end_date IS NULL
         AND cancelled_at IS NOT NULL AND cancelled_by IS NOT NULL AND cancel_reason IS NOT NULL
         AND LEN(LTRIM(RTRIM(cancel_reason))) BETWEEN 3 AND 255)
    )
);

-- The sample data below uses plain INSERT statements, with no loops or variables.
-- To create the schema only: run the script up to the end of the CREATE TABLE statements above.

-- The sample data is anchored to 08/11/2026 ("today" in every comment below).
-- The new tables must be empty; IDENTITY starts at 1.
-- Demo password: 12345678; the database stores only the PBKDF2 hash.
--
-- Compact test set: 30 members (26 managed, 4 soft-deleted; 4 created in November, 1 of them deleted)
-- and 48 registrations (28 PAID, 13 PENDING, 7 CANCELLED). Each member row is commented with the
-- scenario it covers: every membership status (BR-07), the expiring-soon window edges (BR-08),
-- soft delete allowed/rejected (BR-09), schedule chaining (BR-05, section 4.2), the discontinued
-- package (BR-04), an old snapshot name/price (BR-03) and month-boundary paid_at values (BR-13).
-- Every PAID start/end date follows the BR-05 formula, except member 7, whose start_date is set
-- by hand so that the member is UPCOMING only.

INSERT INTO dbo.accounts (username, password_hash, full_name, phone, email, role, status, created_at)
VALUES
    ('admin', 'pbkdf2$600000$7WQQGUu3ng1ewCHURbcuYg==$QyBFYHPFjizc73URrNAVqPFpV0rzvysZFgnso6EYe7k=', N'Avery Thompson', '0900000001', 'admin1@example.invalid', 'ADMIN', 'ACTIVE', '2026-05-11T08:00:00'),
    ('manager1', 'pbkdf2$600000$M3Tt0QBsPCisZFfMv3IzGg==$YH7k6qvAO8XNuQmrI6wK/SjpWySbqD1flgi4k67d6e4=', N'Cameron Lee', '0900000003', 'manager1@example.invalid', 'MANAGER', 'ACTIVE', '2026-05-11T08:00:00'),
    ('manager2', 'pbkdf2$600000$JNuG8cwRSG/CsJD57MptVA==$2krZZMmoFoXmh09eJ4LjeOcWDm9EJpR5RCoCzhbH2Mc=', N'Devon Walker', '0900000004', 'manager2@example.invalid', 'MANAGER', 'ACTIVE', '2026-05-11T08:00:00'),
    ('reception1', 'pbkdf2$600000$gwd/BrgH1De31Rjat451+A==$pbnMK6sTBV3e6lnI+loJnysozIHeIIQDCEs3DmEjg6Q=', N'Gray Harris', '0900000005', 'reception1@example.invalid', 'RECEPTIONIST', 'ACTIVE', '2026-05-11T08:00:00'),
    ('reception2', 'pbkdf2$600000$tUgZY/An7jqQpMOuvJwk7Q==$pGhydXkZxxoM7slhb0IGkkuhmi0N6Ltr3H3nNC98SxY=', N'Hayden Lewis', '0900000006', 'reception2@example.invalid', 'RECEPTIONIST', 'LOCKED', '2026-05-11T08:00:00');

INSERT INTO dbo.packages (name, duration_months, price, is_active, created_at)
VALUES
    (N'One-Month Package', 1, 500000, 1, '2026-05-11T09:00:00'),
    (N'Three-Month Package', 3, 1350000, 1, '2026-05-11T09:00:00'),
    (N'Six-Month Package', 6, 2400000, 1, '2026-05-11T09:00:00'),
    (N'Twelve-Month Package', 12, 4500000, 1, '2026-05-11T09:00:00'),
    (N'Legacy One-Month Package', 1, 450000, 0, '2026-05-11T09:00:00');

INSERT INTO dbo.members (full_name, phone, email, date_of_birth, gender, is_deleted, created_at, created_by)
VALUES
    (N'Alex Smith', '0910000001', 'member001@example.invalid', '1990-03-14', N'Female', 0, '2026-05-12T08:30:00', 4), -- 1: ACTIVE 12 months; cancelled a PENDING first
    (N'Blake Johnson', '0910000002', 'member002@example.invalid', '1985-07-22', N'Male', 0, '2026-05-13T09:15:00', 4), -- 2: ACTIVE with a chained PAID (renewed early)
    (N'Casey Williams', '0910000003', 'member003@example.invalid', '1992-11-05', N'Other', 0, '2026-05-16T10:00:00', 5), -- 3: expiring: end_date = today 08/11 (0 days)
    (N'Drew Brown', '0910000004', 'member004@example.invalid', '1988-01-30', N'Male', 0, '2026-05-18T14:20:00', 4), -- 4: expiring: end_date 14/11 (6 days, last day in list)
    (N'Emery Jones', '0910000005', 'member005@example.invalid', '1995-06-18', N'Female', 0, '2026-05-20T08:45:00', 2), -- 5: NOT expiring: end_date 15/11 (7 days)
    (N'Finley Miller', '0910000006', 'member006@example.invalid', '1979-09-09', N'Male', 0, '2026-05-23T16:10:00', 4), -- 6: ends 12/11 but has a follow-up PAID: not expiring
    (N'Harper Davis', '0910000007', 'member007@example.invalid', '2001-02-28', N'Female', 0, '2026-05-25T11:30:00', 5), -- 7: UPCOMING only + 2 PENDING: soft delete rejected
    (N'Jordan Wilson', '0910000008', 'member008@example.invalid', '1983-12-01', N'Other', 0, '2026-05-28T09:00:00', 4), -- 8: ACTIVE + 2 PENDING: soft delete rejected
    (N'Logan Taylor', '0910000009', 'member009@example.invalid', '1997-04-04', N'Male', 0, '2026-05-31T13:00:00', 5), -- 9: EXPIRED + 2 PENDING: soft delete allowed
    (N'Morgan Moore', '0910000010', 'member010@example.invalid', '1986-08-25', N'Female', 0, '2026-06-03T10:00:00', 4), -- 10: EXPIRED, two chained PAID + a cancelled duplicate
    (N'Alex Johnson', '0910000011', NULL, NULL, NULL, 0, '2026-06-14T08:00:00', 4), -- 11: NONE, no registrations; optional fields empty
    (N'Casey Smith', '0910000012', 'member012@example.invalid', '1999-10-10', N'Male', 0, '2026-06-22T15:00:00', 5), -- 12: NONE with a PENDING (schedule starts today)
    (N'Riley O''Brien', '0910000013', 'member013@example.invalid', '1993-03-03', N'Female', 0, '2026-06-30T09:40:00', 4), -- 13: NONE, only a CANCELLED; apostrophe in name
    (N'Anne-Marie Clarke', '0910000014', 'member014@example.invalid', '1987-05-27', N'Female', 0, '2026-07-07T11:00:00', 4), -- 14: PAID to 12/11 (expiring) + PENDING A (3m, older) and B (1m)
    (N'Quinn Taylor', '0910000015', 'member015@example.invalid', '1991-07-31', N'Other', 0, '2026-07-13T10:30:00', 2), -- 15: PAID to 30/01/2027 + two 1-month PENDING
    (N'Rowan Miller', '0910000016', 'member016@example.invalid', '1975-12-12', N'Male', 0, '2026-07-20T17:20:00', 4), -- 16: PAID + PENDING of the discontinued package
    (N'Sage Brown', '0910000017', 'member017@example.invalid', '1998-02-14', N'Female', 0, '2026-08-13T08:10:00', 5), -- 17: created in October, paid in November
    (N'Taylor Wilson', '0910000018', 'member018@example.invalid', '1994-09-30', N'Male', 0, '2026-08-24T18:00:00', 4), -- 18: paid 31/10 23:30 (month boundary)
    (N'Jamie Davis', '0910000019', 'member019@example.invalid', '1989-04-17', N'Other', 0, '2026-09-01T12:00:00', 5), -- 19: paid 01/11 00:00 (month boundary)
    (N'Reese Jones', '0910000020', 'member020@example.invalid', '1982-06-06', N'Male', 0, '2026-05-26T10:00:00', 4), -- 20: old snapshot name/price (renamed package)
    (N'Peyton Moore', '0910000021', 'member021@example.invalid', '2000-01-01', N'Female', 0, '2026-09-06T09:30:00', 4), -- 21: cancelled 1 month, then paid 3 months
    (N'Charlie Smith', '0910000022', 'member022@example.invalid', '1996-11-11', N'Male', 0, '2026-09-17T16:45:00', 5), -- 22: ACTIVE, confirmed by the now-LOCKED receptionist
    (N'Dakota Williams', '0910000023', 'member023@example.invalid', '1980-08-08', N'Female', 0, '2026-06-06T07:50:00', 4), -- 23: EXPIRED, single PAID
    (N'Elliot Johnson', '0910000024', 'member024@example.invalid', '2003-03-21', N'Male', 0, '2026-11-02T09:00:00', 4), -- 24: new in November, ACTIVE
    (N'Frankie Brown', '0910000025', 'member025@example.invalid', '2004-12-24', N'Other', 0, '2026-11-04T09:20:00', 5), -- 25: new in November, only a PENDING
    (N'Kendall Taylor', '0910000026', NULL, '1990-10-26', N'Female', 0, '2026-11-07T17:45:00', 4), -- 26: new in November, no registrations
    (N'Parker Wilson', '0910000027', 'member027@example.invalid', '1984-02-02', N'Male', 1, '2026-05-22T10:15:00', 4), -- 27: DELETED; PENDING auto-cancelled (restore test)
    (N'Robin Davis', '0910000028', 'member028@example.invalid', '1999-05-05', N'Female', 1, '2026-11-03T14:30:00', 5), -- 28: DELETED, new in November, no registrations
    (N'Skyler Moore', '0910000029', 'member029@example.invalid', '1978-03-15', N'Other', 1, '2026-06-17T11:10:00', 4), -- 29: DELETED; two PENDING auto-cancelled
    (N'Morgan Smith', '0910000030', 'member030@example.invalid', NULL, NULL, 1, '2026-06-27T15:00:00', 4); -- 30: DELETED; phone reuse check (VR-02)

INSERT INTO dbo.registrations (member_id, package_id, package_name_snapshot, duration_months_snapshot, price_snapshot, status, created_at, created_by, payment_method, paid_at, confirmed_by, start_date, end_date, cancelled_at, cancelled_by, cancel_reason)
VALUES
    (1, 1, N'One-Month Package', 1, 500000, 'CANCELLED', '2026-05-14T09:00:00', 4, NULL, NULL, NULL, NULL, NULL, '2026-05-14T09:20:00', 4, N'Member chose a different package'),
    (1, 4, N'Twelve-Month Package', 12, 4500000, 'PAID', '2026-05-14T09:25:00', 4, 'TRANSFER', '2026-05-14T09:40:00', 2, '2026-05-14', '2027-05-13', NULL, NULL, NULL),
    (9, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-06-01T08:00:00', 5, 'CASH', '2026-06-01T08:10:00', 4, '2026-06-01', '2026-06-30', NULL, NULL, NULL),
    (20, 2, N'Quarterly Package', 3, 1200000, 'PAID', '2026-06-01T15:00:00', 4, 'CASH', '2026-06-01T15:30:00', 3, '2026-06-01', '2026-08-31', NULL, NULL, NULL),
    (10, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-06-04T10:30:00', 4, 'CASH', '2026-06-04T10:40:00', 4, '2026-06-04', '2026-07-03', NULL, NULL, NULL),
    (23, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-06-06T08:00:00', 4, 'CASH', '2026-06-06T08:10:00', 3, '2026-06-06', '2026-07-05', NULL, NULL, NULL),
    (27, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-06-08T09:00:00', 4, 'CASH', '2026-06-08T09:10:00', 4, '2026-06-08', '2026-07-07', NULL, NULL, NULL),
    (29, 2, N'Three-Month Package', 3, 1350000, 'PAID', '2026-06-18T08:00:00', 4, 'TRANSFER', '2026-06-18T08:15:00', 3, '2026-06-18', '2026-09-17', NULL, NULL, NULL),
    (30, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-06-27T15:10:00', 4, 'CASH', '2026-06-27T15:20:00', 2, '2026-06-27', '2026-07-26', NULL, NULL, NULL),
    (13, 1, N'One-Month Package', 1, 500000, 'CANCELLED', '2026-06-30T10:00:00', 4, NULL, NULL, NULL, NULL, NULL, '2026-07-07T10:00:00', 2, N'Member changed their registration plans'),
    (10, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-07-02T10:00:00', 5, 'TRANSFER', '2026-07-02T10:10:00', 2, '2026-07-04', '2026-08-03', NULL, NULL, NULL),
    (16, 5, N'Legacy One-Month Package', 1, 450000, 'PAID', '2026-07-22T17:00:00', 4, 'CASH', '2026-07-22T17:10:00', 4, '2026-07-22', '2026-08-21', NULL, NULL, NULL),
    (8, 3, N'Six-Month Package', 6, 2400000, 'PAID', '2026-07-27T10:00:00', 4, 'CASH', '2026-07-27T10:20:00', 3, '2026-07-27', '2027-01-26', NULL, NULL, NULL),
    (15, 3, N'Six-Month Package', 6, 2400000, 'PAID', '2026-07-31T08:00:00', 2, 'CASH', '2026-07-31T08:20:00', 3, '2026-07-31', '2027-01-30', NULL, NULL, NULL),
    (10, 2, N'Three-Month Package', 3, 1350000, 'CANCELLED', '2026-08-01T09:00:00', 4, NULL, NULL, NULL, NULL, NULL, '2026-08-01T09:05:00', 4, N'Duplicate registration created by mistake'),
    (2, 2, N'Three-Month Package', 3, 1350000, 'PAID', '2026-08-22T08:00:00', 5, 'CASH', '2026-08-22T08:10:00', 4, '2026-08-22', '2026-11-21', NULL, NULL, NULL),
    (21, 1, N'One-Month Package', 1, 500000, 'CANCELLED', '2026-09-13T10:00:00', 4, NULL, NULL, NULL, NULL, NULL, '2026-09-13T10:15:00', 4, N'Member chose a different package'),
    (21, 2, N'Three-Month Package', 3, 1350000, 'PAID', '2026-09-13T10:20:00', 4, 'CASH', '2026-09-13T10:30:00', 4, '2026-09-13', '2026-12-12', NULL, NULL, NULL),
    (27, 2, N'Three-Month Package', 3, 1350000, 'CANCELLED', '2026-09-22T10:00:00', 4, NULL, NULL, NULL, NULL, NULL, '2026-10-24T14:00:00', 2, N'Member profile deleted'),
    (9, 2, N'Three-Month Package', 3, 1350000, 'PENDING', '2026-09-27T09:00:00', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (29, 1, N'One-Month Package', 1, 500000, 'CANCELLED', '2026-10-02T09:00:00', 5, NULL, NULL, NULL, NULL, NULL, '2026-10-27T11:00:00', 3, N'Member profile deleted'),
    (29, 3, N'Six-Month Package', 6, 2400000, 'CANCELLED', '2026-10-04T09:00:00', 4, NULL, NULL, NULL, NULL, NULL, '2026-10-27T11:00:00', 3, N'Member profile deleted'),
    (3, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-10-09T18:00:00', 5, 'CASH', '2026-10-09T18:10:00', 4, '2026-10-09', '2026-11-08', NULL, NULL, NULL),
    (6, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-10-13T08:00:00', 4, 'CASH', '2026-10-13T08:05:00', 4, '2026-10-13', '2026-11-12', NULL, NULL, NULL),
    (14, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-10-13T09:00:00', 4, 'TRANSFER', '2026-10-13T09:10:00', 4, '2026-10-13', '2026-11-12', NULL, NULL, NULL),
    (9, 1, N'One-Month Package', 1, 500000, 'PENDING', '2026-10-14T09:00:00', 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (4, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-10-15T07:30:00', 4, 'TRANSFER', '2026-10-15T07:35:00', 4, '2026-10-15', '2026-11-14', NULL, NULL, NULL),
    (5, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-10-16T19:00:00', 5, 'CASH', '2026-10-16T19:10:00', 2, '2026-10-16', '2026-11-15', NULL, NULL, NULL),
    (16, 5, N'Legacy One-Month Package', 1, 450000, 'PENDING', '2026-10-17T16:00:00', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (8, 2, N'Three-Month Package', 3, 1350000, 'PENDING', '2026-10-22T11:00:00', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (20, 4, N'Twelve-Month Package', 12, 4500000, 'PAID', '2026-10-22T11:00:00', 4, 'TRANSFER', '2026-10-22T11:20:00', 2, '2026-10-22', '2027-10-21', NULL, NULL, NULL),
    (8, 1, N'One-Month Package', 1, 500000, 'PENDING', '2026-10-24T11:00:00', 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (17, 2, N'Three-Month Package', 3, 1350000, 'PAID', '2026-10-25T16:00:00', 5, 'TRANSFER', '2026-11-03T09:00:00', 2, '2026-11-03', '2027-02-02', NULL, NULL, NULL),
    (14, 2, N'Three-Month Package', 3, 1350000, 'PENDING', '2026-10-27T10:00:00', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (15, 1, N'One-Month Package', 1, 500000, 'PENDING', '2026-10-30T14:00:00', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (15, 1, N'One-Month Package', 1, 500000, 'PENDING', '2026-10-31T14:00:00', 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (18, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-10-31T23:00:00', 4, 'CASH', '2026-10-31T23:30:00', 4, '2026-10-31', '2026-11-29', NULL, NULL, NULL),
    (19, 3, N'Six-Month Package', 6, 2400000, 'PAID', '2026-10-31T23:50:00', 5, 'TRANSFER', '2026-11-01T00:00:00', 4, '2026-11-01', '2027-04-30', NULL, NULL, NULL),
    (22, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-11-01T08:00:00', 5, 'TRANSFER', '2026-11-01T08:05:00', 5, '2026-11-01', '2026-11-30', NULL, NULL, NULL),
    (14, 1, N'One-Month Package', 1, 500000, 'PENDING', '2026-11-01T10:00:00', 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (2, 1, N'One-Month Package', 1, 500000, 'PAID', '2026-11-01T17:00:00', 4, 'TRANSFER', '2026-11-01T17:05:00', 3, '2026-11-22', '2026-12-21', NULL, NULL, NULL),
    (24, 2, N'Three-Month Package', 3, 1350000, 'PAID', '2026-11-02T09:10:00', 4, 'CASH', '2026-11-02T09:15:00', 4, '2026-11-02', '2027-02-01', NULL, NULL, NULL),
    (7, 2, N'Three-Month Package', 3, 1350000, 'PAID', '2026-11-03T09:00:00', 5, 'TRANSFER', '2026-11-03T09:15:00', 2, '2026-11-17', '2027-02-16', NULL, NULL, NULL),
    (25, 2, N'Three-Month Package', 3, 1350000, 'PENDING', '2026-11-04T09:30:00', 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (7, 1, N'One-Month Package', 1, 500000, 'PENDING', '2026-11-04T15:00:00', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (7, 3, N'Six-Month Package', 6, 2400000, 'PENDING', '2026-11-05T15:30:00', 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
    (6, 2, N'Three-Month Package', 3, 1350000, 'PAID', '2026-11-06T10:00:00', 4, 'TRANSFER', '2026-11-06T10:30:00', 3, '2026-11-13', '2027-02-12', NULL, NULL, NULL),
    (12, 4, N'Twelve-Month Package', 12, 4500000, 'PENDING', '2026-11-07T18:00:00', 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
