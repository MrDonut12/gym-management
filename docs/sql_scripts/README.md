# Gym Management System — Tài khoản kiểm thử

**Môi trường:** SQL Server 2022, database `GymDB` (SRS v1.1 – Must). Chỉ dùng cho local/dev/test.

## Tài khoản đăng nhập

| Vai trò | Username | Email | Mật khẩu | Trạng thái |
|---|---|---|---|---|
| `ADMINISTRATOR` | `admin` | `admin@gmail.com` | `GymDemo@2026!` | `ACTIVE` |
| `OWNER` | `owner` | `owner@gmail.com` | `GymDemo@2026!` | `ACTIVE` |
| `MANAGER` | `manager` | `manager@gmail.com` | `GymDemo@2026!` | `ACTIVE` |
| `RECEPTIONIST` | `receptionist` | `receptionist@gmail.com` | `GymDemo@2026!` | `ACTIVE` |
| `ACCOUNTANT` | `accountant` | `accountant@gmail.com` | `GymDemo@2026!` | `ACTIVE` |

Mỗi vai trò có đúng **1 tài khoản**. Email `@gmail.com` là dữ liệu minh họa, không phải hộp thư đã được xác minh.

## Thông tin nhân viên

| Username | Họ tên | Số điện thoại |
|---|---|---|
| `admin` | Nguyễn Minh Quân | `0901000001` |
| `owner` | Trần Hoàng Anh | `0901000002` |
| `manager` | Lê Quốc Bảo | `0901000003` |
| `receptionist` | Phạm Ngọc Mai | `0901000004` |
| `accountant` | Võ Thanh Hà | `0901000005` |

## Dữ liệu seed

| Bảng | Số dòng |
|---|---:|
| `accounts` | 5 |
| `members` | 100 |
| `plan_types` | 4 (đúng 4 loại gói theo SRS) |
| `memberships` | 30 |
| `payment_transactions` | 30 |
| `visits` | 15 |
| **Tổng** | **184** |

Giao dịch gồm 22 `PAID`, 4 `PENDING`, 4 `CANCELLED`. Lượt tập gồm 10 `COMPLETED`, 4 `AUTO_CLOSED`, 1 `OPEN` đã quá hạn để thử cơ chế đóng lười. Có hai hồ sơ hội viên xóa mềm. Hai lần mua tiếp của hội viên 1 và 2 bắt đầu vào ngày 01/11/2026 sau gói tháng đầu tiên.

## Chạy SQL

1. Mở `GymDB_Full_Setup.sql` trong SSMS hoặc Azure Data Studio.
2. Kết nối SQL Server 2022 với tài khoản có quyền tạo database.
3. Chạy toàn bộ script. Script tạo `GymDB` nếu chưa tồn tại, sau đó tạo 6 bảng, constraint, index, sequence và dữ liệu mẫu.
4. Cuối script có truy vấn đếm số bản ghi từng bảng.

**Script không xóa database cũ.** Nếu các bảng đích đã tồn tại, script dừng thay vì ghi đè. Để chạy lại, dùng một database thử nghiệm sạch.

## Định dạng mật khẩu và tích hợp Java

Mật khẩu seed được băm bằng **PBKDF2-HMAC-SHA256**, 210.000 iterations, salt 16 bytes và digest 32 bytes. Chuỗi lưu ở cột `password_hash` có định dạng:

```text
pbkdf2_sha256$210000$<salt-hex>$<digest-hex>
```

Đây là **quy ước seed được chọn**, chưa được xác nhận tương thích với `PasswordUtil`/T07 trong Java skeleton. Nếu code Java dùng BCrypt hoặc format PBKDF2 khác, **đăng nhập sẽ không hoạt động** cho tới khi điều chỉnh seed hoặc bộ kiểm tra mật khẩu. Không so sánh mật khẩu plaintext với `password_hash`.

Tất cả tài khoản dùng cùng mật khẩu thử nghiệm. **Không đưa tài khoản/mật khẩu này vào môi trường production**, và nên tránh commit README chứa mật khẩu vào repository public.

## Các điểm cần đối chiếu với code

- `memberships.plan_type_id` là tên dự kiến trong tài liệu; nếu DAO dùng `plan_id` thì phải đồng bộ.
- Không có bảng `roles`, `VisitChanges`, `AuditLogs` vì bản Must sử dụng enum `accounts.role` và chỉ 6 bảng.
- SQL bảo vệ FK, CHECK và unique index; các quy tắc liên bảng như PAID phải có lịch, không chồng lấn và kiểm tra quyền cần được service/DAO thực thi trong transaction.
- Dữ liệu `OPEN` quá hạn được cố ý giữ lại để kiểm thử auto-close lười khi có request; không nên coi đó là trạng thái hợp lệ sau khi cơ chế bù đã chạy.
