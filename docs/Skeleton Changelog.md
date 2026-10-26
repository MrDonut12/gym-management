# Ghi chú thay đổi skeleton source code — căn chỉnh theo SRS Baseline 2.0

**Ngày:** 26/10/2026  
**Phạm vi:** `src/`, `pom.xml`. Không thay đổi `database/` và các tài liệu khác (xem mục 7).  
**Căn cứ:** `docs/Software Requirement Specification.md` — Baseline 2.0.  
**Trạng thái kiểm tra:** Toàn bộ 80 file Java biên dịch thành công bằng `javac --release 21` với `jakarta.servlet-api 6.0.0`. Chưa deploy lên Tomcat và chưa chạy với SQL Server: skeleton vẫn là stub `TODO`, chưa có logic nghiệp vụ.

## 1. Kết luận rà soát

Skeleton trước đây được viết theo Baseline 1.x và **không còn bám** Baseline 2.0. Các điểm lệch chính:

| Nội dung | Skeleton cũ | Baseline 2.0 |
| --- | --- | --- |
| Vai trò | 5: ADMINISTRATOR, OWNER, MANAGER, RECEPTIONIST, ACCOUNTANT | 3: ADMIN, MANAGER, RECEPTIONIST (mục 1.3) |
| Dữ liệu | `plan_types`, `memberships`, `payment_transactions`, `visits` | `accounts`, `members`, `packages`, `registrations` (mục 5) |
| Lượt tập | Check-in/check-out, tự đóng lúc 20:00 | Ngoài phạm vi (mục 11.1) |
| Thời hạn gói | `DurationUnit` DAY/MONTH/YEAR, Daily Pass, `end_exclusive` | 1/3/6/12 tháng, `start_date`/`end_date` kiểu DATE, tính cả hai ngày |
| Tài khoản | Xóa mềm, Manager quản lý lễ tân, phạm vi quản lý | Chỉ ADMIN quản lý; chỉ khóa/mở khóa (QT-14, QT-16) |
| Thanh toán | `BANK_TRANSFER`, ảnh minh chứng, `paid_sequence` | `CASH`/`TRANSFER`, xác nhận hai bước (CN-25) |
| Báo cáo | Doanh thu theo tên snapshot, NEW/RENEWAL | Theo `package_id` + tên hiện hành; tổng quan hội viên; bỏ NEW/RENEWAL |
| Phân quyền | `Permission` + `RbacPolicy`, hai filter, URL không tiền tố | Tiền tố `/admin`, `/manager`, `/staff`; một `AuthFilter` (mục 3) |
| Phiên | Timeout 60 phút, chỉ lưu accountId | 30 phút, `SessionUser` trong session, đọc lại trạng thái/vai trò từ DB mỗi request |
| Kiến trúc | `dao/`, `service/`, `model/` phẳng; có `dto/`, `security/` | Package riêng theo phân hệ trong từng lớp (CL-01) |
| Nền tảng | JDK 11, full `jakartaee-api` (JPA, JAX-RS, CDI) | JDK 21, chỉ Servlet/JSP + JDBC (mục 1.4) |
| Hạ tầng | Chưa có `AppClock`, `db.properties`, phân trang | `AppClock` (QT-15), `db.properties` (CL-10), `OFFSET ... FETCH` (CL-09) |

Vì toàn bộ skeleton cũ chỉ là stub, skeleton được **dựng lại** theo Baseline 2.0, vẫn giữ quy ước cũ: model/enum có code thật; DAO/Service/Servlet/JSP để `// TODO:` + `// Output:` và ghi mã yêu cầu (CN/QT/HL/CL/KT) trong Javadoc.

## 2. Build và cấu hình

| File | Thay đổi |
| --- | --- |
| `pom.xml` | Biên dịch `release 21`. Thay `jakarta.jakartaee-api` bằng `jakarta.servlet-api 6.0.0` + `jakarta.servlet.jsp-api 3.1.0` (scope `provided`, đúng Tomcat 10.1). Giữ mssql-jdbc và JSTL. |
| `WEB-INF/web.xml` | Timeout 30 phút (CN-03, CL-05). Chỉ còn hai filter `EncodingFilter` → `AuthFilter`. Thêm `welcome-file` `index.jsp`, thêm error-page cho `java.lang.Throwable` (CL-08). Bỏ ghi chú `<secure>`. |
| `resources/db.properties.example` | **Mới** (CL-10). Mỗi máy copy thành `db.properties`; file này đã nằm trong `.gitignore`. |
| `webapp/index.jsp` | **Mới**, thay `index.html` ("Hello World"): chuyển về `/home`. |
| Đã xóa | `JakartaRestConfiguration.java`, `resources/JakartaEE10Resource.java`, `META-INF/persistence.xml`, `WEB-INF/beans.xml`: đây là template NetBeans cho JAX-RS/JPA/CDI, Tomcat không hỗ trợ và SRS không dùng. |

## 3. Phần nền dùng chung

| File | Trạng thái | Nội dung |
| --- | --- | --- |
| `database/DBContext` | Sửa | `static getConnection()` đúng chữ ký mục 9.1. Đọc `db.properties` (không còn hard-code URL/mật khẩu). Mỗi lần gọi trả về **connection mới** để Service tự quản lý transaction (QT-06, CL-06). Thêm `rollbackQuietly`. *Đây là file duy nhất có code chạy thật; logic cũ đã được viết lại, không chỉ để TODO.* |
| `filter/AuthFilter` | Mới | Gộp `AuthenticationFilter` + `AuthorizationFilter`. Kiểm tra session, đọc lại tài khoản từ DB mỗi request (LOCKED/đổi vai trò/lỗi DB → hủy phiên), phân quyền theo tiền tố URL, header chống cache (CN-02, CN-03, mục 3). |
| `filter/EncodingFilter` | Giữ nguyên | — |
| `util/AppClock` | Mới | `now()`, `today()` theo `Asia/Ho_Chi_Minh` (QT-15, TV4). |
| `util/DateRange` | Mới | Khoảng ngày `[A 00:00, B+1 00:00)`, mặc định tháng hiện tại, kiểm tra A ≤ B (QT-13, HL-16, TV4). |
| `util/Page` | Mới | Kết quả phân trang 20 dòng + `offsetOf` (CL-09, TV2). |
| `util/DateTimeUtil` | Sửa | Chỉ còn parse/format. Bỏ toàn bộ logic giờ mở cửa, 20:00, `end_exclusive`, `addDuration`. |
| `util/ValidationUtil` | Sửa | Chỉ còn hàm kiểm tra từng trường theo HL-01 – HL-15 (TV3). SĐT nhân viên đổi sang `^0[0-9]{9}$`. Thêm username, mật khẩu 8–64, giá, thời hạn, lý do hủy. Việc gom lỗi theo form chuyển về Service của từng phân hệ. |
| `util/RequestUtil` | Sửa | Thêm `getRawString` (username/mật khẩu không trim), `getPositiveId` (HL-17), `getPage` (HL-07). Bỏ `getLong`, `isPost`. |
| `util/SessionUtil` | Sửa | Lưu `SessionUser`, timeout 30 phút, thêm flash message cho PRG (CL-07). Bỏ `getAccountId`, `getCurrentAccount`. |
| `util/CookieUtil` | Sửa | Theo QT-17: HttpOnly, 30 ngày, Path ứng dụng. Giá trị sai HL-08 thì bỏ qua (KT-04). Bỏ URL-encode, SameSite, Secure. |
| `util/PasswordUtil` | Sửa Javadoc | Theo CL-03: BCrypt cost ≥ 10 hoặc PBKDF2-HMAC-SHA256, hash vừa `VARCHAR(100)`. |
| `util/AppConstants` | Sửa | `SESSION_USER`, timeout 30, `PAGE_SIZE` 20, `EXPIRING_MAX_DAYS_AHEAD` 6, khóa flash, thông báo đăng nhập chung, thông báo QT-02 và lý do hủy QT-09. Bỏ `SESSION_ACCOUNT_ID`, `REQUEST_CURRENT_ACCOUNT`, `SYSTEM_ACTOR_NAME`. |
| `exception/NotFoundException` | Mới | Trả 404 (HL-17). Javadoc của các exception khác đổi sang mã yêu cầu mới. |
| Đã xóa | `security/Permission`, `security/RbacPolicy` | Mục 3: vai trò cố định trong code, không có bảng quyền động; phân quyền nằm ở `AuthFilter` và Service. |

## 4. Cấu trúc package theo phân hệ (CL-01)

```
group4.gym.management
├── controller/   HomeServlet + account/ member/ registration/ payment/
├── service/      account/ member/ registration/ payment/
├── dao/          account/ member/ registration/ payment/
├── model/        account/ member/ registration/ payment/
├── database/ filter/ exception/ util/      (nền dùng chung)
```

| Package | Phân hệ | Người phụ trách |
| --- | --- | --- |
| `account` | A — Truy cập và tài khoản (gồm cả đăng nhập/đăng xuất) | TV1 |
| `member` | B — Hội viên | TV2 |
| `registration` | C — Gói tập và đăng ký | TV3 |
| `payment` | D — Thanh toán và báo cáo | TV4 |

Package `dto/` và `model/enums/` đã bỏ: DTO và enum chuyển vào `model/<phân hệ>`.

## 5. Thay đổi theo phân hệ

### 5.1. Phân hệ A — `account`

- **Model:** `Account` (bỏ `deleted`, `updatedAt`), `Role` (3 giá trị), `AccountStatus`, `SessionUser` (**mới**, mục 9.1).
- **DAO:** `AccountDAO` — `search(keyword, role, status)`, `update` gồm cả vai trò, bỏ `softDelete`/`updateProfile`/`updateRole`.
- **Service:** `AuthService.loadAccount` (cho AuthFilter); `AccountService` bỏ xóa mềm và phạm vi quản lý, thêm QT-14 (không tự khóa, không tự đổi vai trò, không tự đặt lại mật khẩu).
- **Servlet:** `LoginServlet`, `LogoutServlet` (chỉ POST), `ChangePasswordServlet`, `AccountList/Create/Edit/Lock/ResetPasswordServlet`. Đã xóa `AccountDeleteServlet`.
- **JSP (5 màn hình):** `account/login.jsp`, `change-password.jsp`, `list.jsp`, `form.jsp`, `reset-password.jsp`.

### 5.2. Phân hệ B — `member`

- **Model:** `Member` (thêm `createdBy`, `getCode()` → `HV000123`; bỏ `deletedAt`/`deletedBy`), `Gender` (lưu nhãn `Nam`/`Nữ`/`Khác`), **mới:** `MembershipStatus` (QT-07), `MemberScope`, `MemberListItem`, `MemberDetailView`, `ExpiringMember`.
- **DAO:** `MemberDAO` — `lockForUpdate(conn, id)` và `searchActive` (mục 9.1), `search`/`count` phân trang, `findExpiringSoon` (QT-08), `markDeleted(conn, id)`, `restore`.
- **Service:** `MemberService` — xóa mềm trong transaction theo QT-06/QT-09 (hủy PENDING), khôi phục theo QT-10, chặn RECEPTIONIST xem hồ sơ đã xóa (QT-11). **Mới:** `MembershipStatusService` (`statusOf`, `expiringSoon` theo mục 9.1).
- **Servlet:** `MemberList/Create/Detail/EditServlet`, `MemberDeleteServlet` (`/manager`), **mới:** `MemberRestoreServlet`, `ExpiringMemberListServlet`.
- **JSP (4):** `member/list.jsp`, `form.jsp`, `detail.jsp`, `expiring.jsp` (mới).

### 5.3. Phân hệ C — `registration`

- **Model:** `GymPackage` (thay `PlanType`; đặt tên này để tránh trùng `java.lang.Package`), `Registration` (gộp `Membership` + `PaymentTransaction`), `RegistrationStatus`, `PaymentMethod` (`CASH`/`TRANSFER`), **mới:** `DateAxis`, `RegistrationFilter`, `RegistrationView`.
- **DAO:** `PackageDAO` (thêm `updateActive`), `RegistrationDAO` gồm các chữ ký mục 9.1 (`findByMember`, `cancelAllPending`, `hasUnexpiredPaid`) và `cancelIfPending`, `findMaxPaidEndDate`, `markPaidIfPending`.
- **Service:** `PackageService` (RECEPTIONIST chỉ thấy gói đang bán, ngừng bán/mở bán), `RegistrationService` (tạo PENDING trong transaction QT-06, hủy có lý do, lọc theo trục thời gian).
- **Servlet:** `PackageList/Create/Edit/StatusServlet`, `RegistrationCreate/Cancel/ListServlet`.
- **JSP (4):** `registration/package-list.jsp`, `package-form.jsp`, `create.jsp`, `list.jsp`.

### 5.4. Phân hệ D — `payment`

- **Model:** **mới:** `SchedulePreview`, `RevenueRow` (dùng cho cả CN-26, CN-27 và dòng tổng), `MemberOverview`; giữ `RevenueGranularity`.
- **DAO:** `ReportDAO` — doanh thu theo kỳ, theo `package_id`, các số đếm cho tổng quan.
- **Service:** `PaymentService` — `previewSchedule`, `confirm(regId, method, byId)` theo mục 9.1; công thức QT-05 tách thành `computeStartDate`/`computeEndDate` (hàm thuần, dễ kiểm KT-21). `ReportService` — `revenueByPeriod`, `revenueByPackage`, `totalOf`, `memberOverview`.
- **Servlet:** `PaymentConfirmServlet` (GET = rà soát, POST = xác nhận, sau đó PRG), `RevenueReportServlet`, `PackageRevenueReportServlet`, `MemberOverviewServlet`.
- **JSP (4):** `payment/confirm.jsp`, `revenue.jsp`, `package-revenue.jsp`, `overview.jsp`.

### 5.5. Đã xóa vì ngoài phạm vi Baseline 2.0

`Visit`, `VisitStatus`, `VisitDAO`, `VisitService`, `CheckIn/CheckOut/VisitHistoryServlet`, `visit/*.jsp`; `DurationUnit`, `MembershipState`; `PurchaseStatistics`, `PurchaseReportServlet`, `report/purchases.jsp` (NEW/RENEWAL → FUT-08); `home/dashboard.jsp` (trang chủ nay chuyển hướng theo vai trò); `transaction/detail.jsp`.

## 6. Bảng URL và phân quyền (mục 3)

| URL | Vai trò | Chức năng |
| --- | --- | --- |
| `/login` | Công khai | CN-01, CN-04 |
| `/home`, `/logout` (POST), `/change-password` | Mọi vai trò | Trang chủ theo vai trò, CN-02, CN-09 |
| `/admin/accounts`, `/create`, `/edit`, `/lock` (POST), `/reset-password` | ADMIN | CN-05 – CN-08, CN-10 |
| `/staff/members`, `/create`, `/detail`, `/edit`, `/expiring` | MANAGER, RECEPTIONIST | CN-11 – CN-14, CN-17 |
| `/manager/members/delete`, `/restore` (POST) | MANAGER | CN-15, CN-16 |
| `/staff/packages` | MANAGER, RECEPTIONIST | CN-18 |
| `/manager/packages/create`, `/edit`, `/status` (POST) | MANAGER | CN-19 – CN-21 |
| `/staff/registrations`, `/create`, `/cancel` (POST) | MANAGER, RECEPTIONIST | CN-22 – CN-24 |
| `/staff/payments/confirm` | MANAGER, RECEPTIONIST | CN-25 |
| `/manager/reports/revenue`, `/revenue-by-package`, `/manager/overview` | MANAGER | CN-26 – CN-28 |

Trang chủ sau đăng nhập: ADMIN → `/admin/accounts`, MANAGER → `/manager/overview`, RECEPTIONIST → `/staff/members`.  
Các quy tắc chi tiết hơn tiền tố URL (lọc hồ sơ đã xóa, gói ngừng bán, QT-14) được kiểm tra ở Service.

## 7. Việc còn tồn đọng ngoài `src/`

1. **`database/schema.sql`, `seed.sql` và các script khác vẫn theo Baseline 1.1** (6 bảng, 5 vai trò, `plan_types`, `visits`, `birth_date`…). Cần viết lại theo DL-01 – DL-05 trước khi bắt đầu viết DAO. Tên cột trong TODO của DAO đã theo mục 5 của SRS 2.0, ví dụ `date_of_birth`, `packages`, `registrations`.
2. **`docs/Database Design.md` vẫn ghi Baseline 1.1.**
3. **Bootstrap 5:** `header.jsp`/`footer.jsp` đã trỏ tới `/assets/vendor/bootstrap/css/bootstrap.min.css` và `/assets/vendor/bootstrap/js/bootstrap.bundle.min.js`; cần chép file Bootstrap vào đó để chạy offline (CL-11).
4. Mỗi thành viên tạo `src/main/resources/db.properties` từ `db.properties.example`.
5. `.gitignore` còn mục `uploads/` dành cho ảnh minh chứng (đã ngoài phạm vi); có thể xóa.

## 8. Một số quyết định đã thực hiện ở những điểm SRS không nói rõ (có thể cần xác nhận lại)

- Tên package phân hệ D là `payment`, gồm cả báo cáo (CN-26 – CN-28).
- Phân hệ A gộp `auth` vào `account` để mỗi phân hệ có một package.
- Tham số người thực hiện của Service là `SessionUser` (chữ ký mục 9.1), không dùng `Account`.
- `DateRange.of`: ô ngày nào để trống thì lấy cận tương ứng của tháng hiện tại.
- Đăng xuất chỉ nhận POST; GET trả về 405.
- Ô lý do hủy nằm ngay trong danh sách đăng ký (CN-24), vì phân hệ C chỉ có 4 màn hình.
