Phiên bản hiệu chỉnh cho SRS v1.1 – phạm vi Must  |  SQL Server 2022

Căn cứ đối chiếu: tài liệu phân công triển khai SRS v1.1 (T01–T20) do nhóm cung cấp. Đây là phương án thiết kế dành riêng cho giai đoạn Must; không thay thế toàn văn SRS v1.1 hoặc chữ ký model/DAO của mã nguồn.

# THIẾT KẾ CƠ SỞ DỮ LIỆU – GYM MANAGEMENT SYSTEM
**Phiên bản:** SRS v1.1 – Must | **DBMS:** SQL Server 2022

# 1. Kết quả rà soát và phạm vi hiệu chỉnh

| Nội dung | Bản cũ | Bản sửa theo v1.1 |
| --- | --- | --- |
| Phạm vi | 8 bảng | 6 bảng: accounts, members, plan_types, memberships, payment_transactions, visits |
| Bảng không còn Must | VisitChanges, AuditLogs | Loại khỏi schema Must; không triển khai trong T01 |
| Tên bảng | MembershipPlans; Transactions | plan_types; payment_transactions |
| Tên cột trọng yếu | paid_seq; visit_date | paid_sequence; check_in_date |
| Sửa/hủy lượt | CN-16 và lịch sử thay đổi | Không triển khai; FUT-01 |
| Tự đóng lúc 20:00 | Job nền + xử lý lúc khởi động | Đóng lười khi có request; job/timer thuộc FUT-02 |
| Ngày bắt đầu PAID | Cho chọn ngày tương lai | Không có form chọn ngày; FUT-03 |
| Sơ đồ ERD | 8 thực thể | 6 thực thể; 1:M, 1:1 và M:N nghiệp vụ |

Nguyên tắc tương thích: không sửa model, enum, exception và chữ ký public đã đóng băng trong code skeleton. T01 cần đối chiếu tên cột thực tế với DAO TODO trước khi chốt schema.sql; các dấu * là tên cần xác nhận.

# 2. Mô hình dữ liệu và mối quan hệ

## 2.1. Sáu bảng thuộc Must

| Bảng                 | Vai trò                                     | Chủ đề/owner  |
| -------------------- | ------------------------------------------- | ------------- |
| accounts             | Đăng nhập, nhân viên, 5 vai trò, xóa mềm    | T01 + T07/T08 |
| members              | Hồ sơ hội viên, tra cứu, xóa mềm            | T01 + T10     |
| plan_types           | 4 loại gói cố định, cấu hình tên/giá        | T01 + T12     |
| memberships          | Đăng ký PENDING và thời hạn sau PAID        | T01 + T13     |
| payment_transactions | Snapshot số tiền, trạng thái, paid_sequence | T01 + T15     |
| visits               | Check-in/check-out, auto-close lười         | T01 + T17     |

## 2.2. Sơ đồ ERD

![Hình 1. ERD sáu bảng Must: quan hệ 1:M và 1:1 (đường nét đứt: FK người thực hiện).](assets/Gym_Management_ERD_SRS_v1.1_MUST.jpg)

*Hình 1. ERD sáu bảng Must: quan hệ 1:M và 1:1 (đường nét đứt: FK người thực hiện).*

M:N ở mức nghiệp vụ: một member có thể mua nhiều plan_type; một plan_type được nhiều member mua. Quan hệ M:N được triển khai bởi memberships gồm member_id và khóa tham chiếu plan_types, không tạo khóa ngoại M:N trực tiếp.

Ký hiệu plan_type_id* trong hình là tên cột dự kiến. Nếu model/DAO hiện có đang dùng plan_id hoặc tên khác, giữ đúng hợp đồng code và cập nhật sơ đồ/DDL đồng bộ qua T01.

| Từ | Đến | Bản số / lưu ý |
| --- | --- | --- |
| members | memberships | 1:M; một hội viên có nhiều lần mua |
| plan_types | memberships | 1:M; một loại gói xuất hiện ở nhiều lần mua |
| memberships | payment_transactions | 1:1 nghiệp vụ; UNIQUE(membership_id); tạo cặp bản ghi trong một DB transaction |
| members | payment_transactions | 1:M nếu lưu member_id dư thừa; kiểm tra bằng composite FK |
| members | visits | 1:M; mỗi hội viên tối đa 1 OPEN |
| memberships | visits | 1:M; lượt tập gắn gói đã PAID |
| accounts | payment_transactions; visits | 1:M theo các FK created_by/confirmed_by/closed_by_account_id |

# 3. Đặc tả sáu bảng dữ liệu

## 3.1. accounts – tài khoản và nhân viên

| Cột | Kiểu tham khảo | Quy tắc |
| --- | --- | --- |
| id | INT IDENTITY | PK |
| username | NVARCHAR(50) | NOT NULL, UNIQUE không phân biệt hoa/thường |
| password_hash | VARCHAR(255) | Hash PBKDF2/BCrypt theo định dạng T07 thống nhất |
| full_name | NVARCHAR(100) | NOT NULL, 2–100 ký tự |
| phone | VARCHAR(10) | NOT NULL, đúng 10 chữ số, UNIQUE trong accounts |
| email | NVARCHAR(254) | NULL, không cần UNIQUE |
| role | VARCHAR(20) | Năm vai trò cố định |
| status | VARCHAR(10) | ACTIVE hoặc LOCKED |
| is_deleted / deleted_at | BIT / DATETIME2(0) | Xóa mềm, không DELETE vật lý |
| created_at / updated_at | DATETIME2(0) | Thời gian Việt Nam |

Không cần bảng session, roles động hay cookie: phiên trong HttpSession/JSESSIONID, cookie gym_username chỉ ghi nhớ tên đăng nhập (T05, T07). RBAC thực hiện ở backend.

## 3.2. members – hồ sơ hội viên

| Cột | Kiểu | Quy tắc |
| --- | --- | --- |
| id | INT IDENTITY | PK, mã hiển thị sinh từ id |
| full_name | NVARCHAR(100) | NOT NULL, 2–100 ký tự |
| phone | VARCHAR(10) | NOT NULL, UNIQUE, biểu thức ^0\d{9}$ |
| email | NVARCHAR(254) | NULL, đúng định dạng nếu nhập |
| birth_date | DATE | NULL, không sau hôm nay (backend) |
| gender | NVARCHAR(10) | NULL, Nam/Nữ/Khác |
| created_at | DATETIME2(0) | NOT NULL, không đổi, báo cáo hội viên mới |
| is_deleted | BIT | NOT NULL, mặc định 0 |
| deleted_at / deleted_by | DATETIME2(0) / INT | NULL, FK accounts cho người xóa |

Xóa mềm không tự hủy giao dịch PENDING hiện có. Hội viên đã xóa mềm không tạo PENDING mới, không xác nhận PAID và không check-in; PENDING cũ có thể hủy thủ công (T10, T15).

## 3.3. plan_types – bốn loại gói

| Cột | Kiểu | Quy tắc |
| --- | --- | --- |
| id | INT IDENTITY | PK |
| name / name_normalized | NVARCHAR(100) | Tên hiển thị; tên chuẩn hóa UNIQUE, cập nhật cùng nhau |
| duration_value | INT | 1 hoặc 6 theo tổ hợp hợp lệ; không sửa sau tạo |
| duration_unit | VARCHAR(10) | DAY/MONTH/YEAR |
| price | DECIMAL(12,0) | Số nguyên dương |
| created_at / created_by | DATETIME2(0) / INT | NOT NULL, FK account Owner |
| updated_at / updated_by | DATETIME2(0) / INT | NULL, chỉ sửa tên/giá theo RBAC |

Chỉ bốn tổ hợp (1 DAY), (1 MONTH), (6 MONTH), (1 YEAR), không thêm thời hạn khác. Không có xóa/ngừng dùng gói. Mọi Membership đã tạo phải giữ snapshot tên/thời hạn/giá cũ khi gói bị đổi tên/giá.

## 3.4. memberships – đăng ký gói và lịch PAID

| Cột | Kiểu | Quy tắc |
| --- | --- | --- |
| id | INT IDENTITY | PK |
| member_id | INT | FK members, NOT NULL |
| plan_type_id* | INT | FK plan_types; xác nhận tên với model/DAO T01/T13 |
| plan_name_snapshot | NVARCHAR(100) | Bản chụp tên gói khi tạo PENDING |
| duration_value_snapshot | INT | Bản chụp thời hạn, > 0 |
| duration_unit_snapshot | VARCHAR(10) | DAY/MONTH/YEAR |
| created_at / created_by | DATETIME2(0) / INT | Thời điểm và nhân viên tạo PENDING |
| start_at | DATETIME2(0) | NULL khi PENDING/CANCELLED; 00:00 khi PAID |
| end_exclusive | DATETIME2(0) | NULL khi PENDING/CANCELLED; > start_at khi PAID |

Không có requested_start_date: ngày bắt đầu không do người dùng chọn trong Must (FUT-03). Tình trạng từng đăng ký suy ra từ payment_transactions.status và thời điểm hiện tại; không tạo enum membership_state mới.

## 3.5. payment_transactions – thanh toán thủ công

| Cột | Kiểu | Quy tắc |
| --- | --- | --- |
| id | INT IDENTITY | PK, mã giao dịch hiển thị sinh từ id |
| membership_id | INT | FK memberships, UNIQUE, NOT NULL |
| member_id | INT | FK members, khớp memberships.member_id |
| plan_name_snapshot | NVARCHAR(100) | Bản chụp từ đăng ký, giữ nguyên |
| amount | DECIMAL(12,0) | Snapshot giá, >0, không tính lại |
| method | VARCHAR(15) | CASH hoặc BANK_TRANSFER |
| status | VARCHAR(10) | PENDING, PAID, CANCELLED |
| created_at / created_by | DATETIME2(0) / INT | Thời điểm/người tạo PENDING |
| paid_at / confirmed_by | DATETIME2(0) / INT | Chỉ có khi PAID |
| paid_sequence | BIGINT | NULL khi chưa PAID; cấp từ SEQUENCE, UNIQUE |
| cancelled_at / cancelled_by | DATETIME2(0) / INT | Chỉ có khi CANCELLED |
| proof_image_path | NVARCHAR(500) | Ảnh minh chứng tùy chọn, không xác nhận tiền |

Mỗi Membership có đúng một giao dịch do service T13 tạo nguyên tử. PAID không sửa, hủy, hoàn tiền. Quyền xác nhận/hủy thuộc Manager và Receptionist. Ảnh minh chứng không tự xác nhận tiền đã nhận.

## 3.6. visits – lượt tập

| Cột | Kiểu | Quy tắc |
| --- | --- | --- |
| id | INT IDENTITY | PK |
| member_id / membership_id | INT / INT | FK member và Membership cùng hội viên |
| created_by | INT | FK accounts; người tạo lượt |
| check_in_date | DATE | Ngày check-in theo giờ Việt Nam |
| check_in_at | DATETIME2(0) | Giờ vào, 07:00 ≤ time < 20:00 |
| check_out_at | DATETIME2(0) | NULL khi OPEN; lúc ra/tự đóng khi kết thúc |
| status | VARCHAR(12) | OPEN / COMPLETED / AUTO_CLOSED (Must) |
| closed_by_account_id | INT | NULL nếu chưa đóng hoặc đóng bởi hệ thống |
| closed_by_system | BIT | 1 nếu đóng lười đúng 20:00, ngược lại 0 |

Không có thao tác EDIT/CANCEL trên lượt và không có bảng VisitChanges trong Must (FUT-01). Không tạo job/timer tự đóng (FUT-02). Có thể dùng kiểm soát đồng thời ở tầng DAO/DB, nhưng không tự ý thêm field vào model đã đóng băng.

# 4. Ràng buộc SQL và chỉ mục cốt lõi

SQL dưới đây là mẫu áp dụng sau khi tạo 6 bảng trong database/schema.sql; T01 xác nhận tên cột với code skeleton trước khi chạy. Các CHECK nội bảng không thay thế kiểm tra giao dịch liên bảng.

## 4.1. CHECK và UNIQUE

```sql
-- Khoảng hiệu lực: hai mốc cùng NULL hoặc cùng hợp lệ
ALTER TABLE memberships ADD CONSTRAINT CK_Membership_Period
CHECK ((start_at IS NULL AND end_exclusive IS NULL)
   OR (start_at IS NOT NULL AND end_exclusive IS NOT NULL
       AND end_exclusive > start_at
       AND CAST(start_at AS TIME) = '00:00:00'));

ALTER TABLE plan_types ADD CONSTRAINT CK_Plan_Duration
CHECK ((duration_value = 1 AND duration_unit = 'DAY')
    OR (duration_value = 1 AND duration_unit = 'MONTH')
    OR (duration_value = 6 AND duration_unit = 'MONTH')
    OR (duration_value = 1 AND duration_unit = 'YEAR'));

ALTER TABLE plan_types ADD CONSTRAINT UQ_Plan_Duration
UNIQUE (duration_value, duration_unit);

ALTER TABLE visits ADD CONSTRAINT CK_Visit_Status
CHECK (status IN ('OPEN', 'COMPLETED', 'AUTO_CLOSED'));
```

CHECK khoảng hiệu lực có cả end_exclusive IS NOT NULL để tránh SQL Server chấp nhận UNKNOWN. Việc một giao dịch PAID có lịch còn PENDING/CANCELLED không có lịch phải được xác minh trong cùng transaction trước COMMIT.

## 4.2. FK nhất quán và indexes

```sql
-- Composite FK bảo đảm liên kết cùng hội viên
ALTER TABLE memberships ADD CONSTRAINT UQ_Membership_IdMember
  UNIQUE(id, member_id);
ALTER TABLE payment_transactions ADD CONSTRAINT FK_Payment_Member
  FOREIGN KEY (membership_id, member_id)
  REFERENCES memberships(id, member_id);
ALTER TABLE visits ADD CONSTRAINT FK_Visit_Member
  FOREIGN KEY (membership_id, member_id)
  REFERENCES memberships(id, member_id);

-- Mỗi hội viên tối đa một lượt OPEN kể cả khi request đồng thời
CREATE UNIQUE INDEX UX_Visits_OneOpen
  ON visits(member_id) WHERE status = 'OPEN';

CREATE SEQUENCE seq_paid_sequence AS BIGINT
  START WITH 1 INCREMENT BY 1;
CREATE UNIQUE INDEX UX_Payment_PaidSequence
  ON payment_transactions(paid_sequence)
  WHERE paid_sequence IS NOT NULL;

CREATE INDEX IX_Payment_PaidAt
  ON payment_transactions(paid_at) INCLUDE (amount)
  WHERE status = 'PAID';
CREATE INDEX IX_Payment_CreatedAt
  ON payment_transactions(created_at)
  WHERE status IN ('PENDING','CANCELLED');
CREATE INDEX IX_Visits_CheckInAt
  ON visits(member_id, check_in_at);
```

Ngoài ra cần UNIQUE username/phone/name_normalized, chuẩn hóa chuỗi trước lưu và phân quyền SELECT/INSERT/UPDATE thích hợp. Chỉ số sequence có thể có khoảng trống khi rollback; không được suy ra số giao dịch thành công từ hiệu số sequence.

# 5. Xử lý nghiệp vụ trọng yếu ở tầng DB / Service

## 5.1. Tạo PENDING (T13 + T15)

Một DB transaction tạo đồng thời memberships và payment_transactions; mọi snapshot được lấy từ plan_types lúc tạo. Không cấp start_at/end_exclusive cho PENDING. Nếu lỗi một INSERT phải ROLLBACK cả hai. Từ chối hội viên xóa mềm.

## 5.2. Xác nhận PAID, chọn chuỗi, rolling (T13 + T15)

Chỉ Manager/Receptionist xác nhận khi đã thực nhận đủ tiền snapshot. Hai request với cùng PENDING chỉ một request thành công. Hai giao dịch khác nhau của cùng hội viên phải nối lịch theo paid_sequence và không chồng lấn; tuyệt đối không để PAID mà thiếu lịch.

- 1. Đọc transaction lấy member_id để định tuyến, sau đó khóa dòng members của hội viên (UPDLOCK, HOLDLOCK) theo thứ tự chung của toàn bộ luồng xác nhận. Đọc và khóa lại transaction (UPDLOCK); xác minh đúng hội viên và PENDING.
- 2. Lấy @now theo Asia/Ho_Chi_Minh sau khi khóa; nếu chuỗi PAID còn hiệu lực/chờ hiệu lực thì start_at = end_exclusive lớn nhất; nếu không có chuỗi sống thì start_at = 00:00 hôm nay. Không nhận ngày bắt đầu do client cung cấp (FUT-03).
- 3. Tính end_exclusive bằng DAY/MONTH/YEAR theo snapshot của riêng gói (rolling). Chốt ngày cuối tháng khi ngày tương ứng không tồn tại.
- 4. Chuyển PENDING → PAID có điều kiện, cấp paid_sequence; UPDATE schedule có điều kiện start_at IS NULL. Kiểm tra @@ROWCOUNT từng bước, khoảng PAID không giao nhau, snapshot không thay đổi, COMMIT khi tất cả thành công.
- 5. Nếu bất kỳ bước nào thất bại hoặc request trùng, ROLLBACK; không đổi tiền, paid_at, lịch hay báo cáo.
Ghi chú điều phối T13/T15: T15 mô tả khóa giao dịch trước trong task board; để tránh đảo thứ tự khóa khi có nhiều giao dịch của cùng hội viên, nhóm cần thống nhất thứ tự khóa thành một quy ước duy nhất mà vẫn giữ nguyên chữ ký public các method đã đóng băng.

## 5.3. Hủy PENDING (T15)

```sql
SET XACT_ABORT ON;
BEGIN TRY
  BEGIN TRAN;
  UPDATE payment_transactions
  SET status='CANCELLED',
      cancelled_at=@now, cancelled_by=@staff
  WHERE id=@tx AND status='PENDING';
  IF @@ROWCOUNT <> 1
    THROW 50002, N'Giao dịch không còn PENDING', 1;
  COMMIT TRAN;
END TRY
BEGIN CATCH
  IF @@TRANCOUNT > 0 ROLLBACK TRAN;
  THROW;
END CATCH;
```

Trạng thái CANCELLED làm đăng ký tương ứng vô hiệu, nhưng không xóa vật lý. Không hủy PAID. Hủy PENDING của member đã xóa mềm vẫn hợp lệ theo quyền.

## 5.4. Check-in, check-out và auto-close lười (T17)

Ở mọi entry point getMemberStatus/getOpenVisit/listOpenVisits/searchHistory/checkIn/checkOut, thực thi xử lý bù OPEN đã quá 20:00 bằng câu UPDATE idempotent trước khi dùng dữ liệu. Khi chưa có request, không chạy job nền, timer hay lịch tự đóng (FUT-02).

```sql
-- @now: thời điểm giờ Việt Nam do DateTimeUtil.now() cung cấp
UPDATE visits
SET status = 'AUTO_CLOSED',
    check_out_at = DATEADD(HOUR, 20,
       CAST(check_in_date AS DATETIME2(0))),
    closed_by_system = 1
WHERE status = 'OPEN'
  AND DATEADD(HOUR, 20,
       CAST(check_in_date AS DATETIME2(0))) <= @now;
```

Check-in: sau đóng bù, kiểm tra member tồn tại/chưa xóa mềm, PAID có start_at ≤ now < end_exclusive, now ≥ paid_at, giờ 07:00 đến trước 20:00 và chưa OPEN. Kiểm tra cùng transaction với INSERT; unique filtered index chống hai request đồng thời.

Check-out: sau đóng bù, chỉ chuyển OPEN → COMPLETED khi now > check_in_at, cùng check_in_date và trước 20:00. UPDATE có WHERE status=OPEN và kiểm tra @@ROWCOUNT. Lượt AUTO_CLOSED luôn ghi mốc 20:00 ngày check-in (thời điểm quy ước). Không có sửa/hủy Visits trong Must.

# 6. Truy vấn báo cáo (T19/T20)

Khoảng ngày: [00:00 của ngày bắt đầu, 00:00 ngày sau ngày kết thúc). Doanh thu chỉ gồm PAID theo paid_at; PENDING/CANCELLED không được tính. Hệ thống trả về 0 khi không có kết quả.

## 6.1. Doanh thu và số giao dịch

```sql
SELECT COALESCE(SUM(amount),0) AS revenue,
       COUNT(*) AS transaction_count
FROM payment_transactions
WHERE status = 'PAID'
  AND paid_at >= @from
  AND paid_at < @toExclusive;
```

## 6.2. Lần mua đầu và gia hạn

```sql
WITH ranked AS (
 SELECT member_id, paid_at,
        ROW_NUMBER() OVER (
         PARTITION BY member_id ORDER BY paid_sequence) AS rn
 FROM payment_transactions
 WHERE status='PAID'
)
SELECT COALESCE(SUM(CASE WHEN rn=1 THEN 1 ELSE 0 END),0)
         AS new_purchases,
       COALESCE(SUM(CASE WHEN rn>1 THEN 1 ELSE 0 END),0)
         AS renewals
FROM ranked
WHERE paid_at >= @from AND paid_at < @toExclusive;
```

## 6.3. Hội viên và phân quyền thống kê

Tổng số hội viên gồm hồ sơ đã xóa mềm, có bộ lọc trạng thái; số hội viên mới theo members.created_at. Accountant được xem giao dịch/doanh thu, không có quyền thống kê hội viên. Doanh thu theo gói nhóm theo plan_name_snapshot chứ không lấy tên cấu hình mới.

# 7. Truy vết và kiểm thử tối thiểu

| Nhóm Must | Đối tượng thực hiện | Bằng chứng/test cần có |
| --- | --- | --- |
| T01 / DL-01–DL-07 | 6 bảng, FK, CHECK, seed | 5 accounts mỗi vai trò, 4 gói, ≥100 members; build schema.sql/seed.sql |
| T05 + T07 | accounts | RBAC đúng 5 vai trò; hash, session timeout, cookie chỉ username |
| T10–T12 | members / plan_types | Trùng SĐT/username/tên chuẩn hóa; xóa mềm; sửa tên/giá không đổi snapshot |
| T13 + T15 | memberships / payment_transactions | PAID atomic; xác nhận trùng; rolling; nối live chain; không chọn start tương lai |
| T17 + T18 | visits | 07:00–<20:00; một OPEN; đóng lười; cùng ngày; không edit/cancel |
| T19 + T20 | báo cáo | paid_at; paid_sequence; trả 0; Accountant không xem báo cáo member |

# 8. Các điểm cần xác nhận trước khi merge T01

- Tên cột plan_type_id* phải kiểm tra trực tiếp model và SQL TODO của PlanTypeDAO/MembershipDAO; đổi tên cột chỉ sau khi mọi owner chốt hợp đồng.
- Chốt danh sách field của model/enums đã đóng băng; không tự thêm row_version, CANCELLED visit hoặc trạng thái Membership mới nếu skeleton không có.
- Kiểm tra chiều khóa và thứ tự dùng UPDLOCK giữa T13/T15/T17, đồng thời bảo vệ UNIQUE 1 OPEN và không chồng lấn gói.
- Danh sách cột/constraint ở đây là thiết kế đề xuất theo Implementation Task Board v1.1, chưa chứng minh đã khớp toàn văn SRS v1.1 hoặc repository thực tế.
- Seed tài khoản theo định dạng password hash T07 chốt, không hard-code mật khẩu plaintext; dữ liệu test đủ 100 hội viên.
- Các bảng VisitChanges, AuditLogs; chức năng sửa/hủy lượt, job nền, chọn ngày tương lai chỉ đưa trở lại khi SRS ưu tiên FUT/Should được phê duyệt.
