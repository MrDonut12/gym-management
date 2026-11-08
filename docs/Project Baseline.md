# Project Baseline

## 1. Mô tả dự án

### 1.1. Mục đích

Tài liệu xác định chức năng, quyền truy cập, quy tắc nghiệp vụ, dữ liệu và điều kiện chấp nhận của Gym Management System. Đây là căn cứ duy nhất để thiết kế schema, viết code, seed dữ liệu và kiểm thử; các tiêu chí trong tài liệu không phải kết quả kiểm thử.

### 1.2. Phạm vi

Website nội bộ cho nhân viên phòng gym, gồm 4 phân hệ:

| Mã | Phân hệ | Nội dung | Chức năng |
| --- | --- | --- | --- |
| A | Truy cập và tài khoản | Đăng nhập/đăng xuất, phiên, cookie, quản lý tài khoản nhân viên, mật khẩu | FR-01 – FR-10 |
| B | Hội viên | CRUD hồ sơ, tìm kiếm, xóa mềm/khôi phục, tình trạng gói, cảnh báo sắp hết hạn | FR-11 – FR-17 |
| C | Gói tập và đăng ký | Loại gói, tạo/hủy đăng ký PENDING, tra cứu đăng ký | FR-18 – FR-24 |
| D | Thanh toán và báo cáo | Xác nhận thanh toán, xếp lịch gói, báo cáo doanh thu và hội viên | FR-25 – FR-28 |

Hệ thống không quản lý lượt ra/vào; hiệu lực gói suy ra từ các đăng ký đã thanh toán. Thanh toán là xác nhận thủ công (tiền mặt hoặc chuyển khoản), không tích hợp ngân hàng.

### 1.3. Vai trò người dùng

Mỗi tài khoản thuộc đúng một trong ba vai trò cố định. Hội viên không có tài khoản đăng nhập.

| Vai trò | Mã lưu trữ | Phạm vi chính |
| --- | --- | --- |
| Quản trị viên | ADMIN | Quản lý tài khoản nhân viên. Không có quyền nghiệp vụ hội viên/gói/thanh toán. |
| Quản lý | MANAGER | Toàn bộ nghiệp vụ: hội viên (gồm xóa mềm/khôi phục), loại gói, đăng ký, thanh toán, báo cáo. |
| Lễ tân | RECEPTIONIST | Nghiệp vụ tại quầy: tạo/sửa/tìm hội viên, tạo/hủy đăng ký, xác nhận thanh toán. |

### 1.4. Môi trường và công nghệ

| Nội dung | Yêu cầu |
| --- | --- |
| Nền tảng | Java Servlet/JSP (JSTL, EL), JDK 21, Apache Tomcat 10.1 (Jakarta EE 10) |
| Cơ sở dữ liệu | Microsoft SQL Server 2022, truy cập qua JDBC với PreparedStatement |
| Kiến trúc | MVC ba lớp: Controller (Servlet) → Service (nghiệp vụ) → DAO (SQL); View là JSP |
| Thư viện bắt buộc | JDBC driver SQL Server, JSTL, jBCrypt (hoặc PBKDF2 có sẵn trong JDK) |
| Trình duyệt | Chrome bản hiện hành; chạy local một máy, không cần Internet |
| Múi giờ | `Asia/Ho_Chi_Minh` cho mọi ngày giờ nghiệp vụ |

### 1.5. Thuật ngữ

| Thuật ngữ | Ý nghĩa |
| --- | --- |
| Hội viên (Member) | Người tập có hồ sơ trong hệ thống |
| Nhân viên / Người dùng | Người có tài khoản đăng nhập (Account) |
| Loại gói (Package) | Cấu hình tên, thời hạn (số tháng), giá và trạng thái đang bán/ngừng bán |
| Đăng ký (Registration) | Một lần mua gói của hội viên, đồng thời là giao dịch thanh toán của lần mua đó |
| PENDING / PAID / CANCELLED | Chờ thanh toán / đã nhận đủ tiền / đã hủy — trạng thái của đăng ký |
| Snapshot | Bản sao tên gói, thời hạn và giá lưu vào đăng ký lúc tạo; không đổi về sau |
| start\_date / end\_date | Ngày bắt đầu / ngày cuối cùng có hiệu lực của gói (cả hai đều tính), chỉ có khi PAID |
| Hôm nay | Ngày hiện tại theo giờ Việt Nam, lấy từ lớp đồng hồ nghiệp vụ (BR-15) |

### 1.6. Quy ước ký hiệu

| Tiền tố | Viết tắt của | Loại yêu cầu | Ví dụ |
| --- | --- | --- | --- |
| FR | Functional Requirement | Chức năng | FR-01 |
| BR | Business Rule | Quy tắc nghiệp vụ | BR-05 |
| DR | Data Requirement | Yêu cầu dữ liệu | DR-04 |
| VR | Validation Rule | Kiểm tra hợp lệ | VR-03 |
| NFR | Non-Functional Requirement | Yêu cầu phi chức năng (chất lượng) | NFR-02 |
| AC | Acceptance Criteria | Điều kiện chấp nhận (kiểm thử) | AC-10 |

## 2. Các chức năng

Hệ thống có 28 chức năng chia 4 phân hệ, mỗi phân hệ do một thành viên phụ trách (mục 9). Mọi chức năng phải đồng thời tuân thủ phân quyền (mục 3), quy tắc nghiệp vụ (mục 4) và kiểm tra hợp lệ (mục 6).

### 2.1. Phân hệ A — Truy cập và tài khoản

| ID | Chức năng | Mô tả và điều kiện chấp nhận | Vai trò |
| --- | --- | --- | --- |
| FR-01 | Đăng nhập | Nhập username và mật khẩu; username không phân biệt hoa/thường, mật khẩu so với hash (NFR-03). Chỉ tài khoản ACTIVE đăng nhập được. Thành công: đổi session ID, lưu accountId, vai trò, họ tên vào session, chuyển tới trang chủ theo vai trò. Thất bại: một thông báo chung, không nói rõ sai username hay mật khẩu. | Tất cả |
| FR-02 | Đăng xuất | Hủy session hiện tại và về trang đăng nhập. Trang bảo vệ gửi header chống cache để nút Back không hiển thị lại dữ liệu. | Tất cả |
| FR-03 | Quản lý phiên | `AuthFilter` chặn mọi URL trừ trang đăng nhập và tài nguyên tĩnh. Chưa đăng nhập thì chuyển về trang đăng nhập. Mỗi request đọc lại trạng thái và vai trò tài khoản từ DB; LOCKED hoặc vai trò đã đổi thì hủy phiên. Lỗi đọc DB thì từ chối. Timeout 30 phút không hoạt động. Không lưu session vào DB. | Tất cả |
| FR-04 | Ghi nhớ tên đăng nhập | Ô "Ghi nhớ tên đăng nhập" trên trang đăng nhập, theo BR-17. Chỉ ghi nhớ username, không tự đăng nhập. | Tất cả |
| FR-05 | Danh sách tài khoản | Tìm theo username hoặc họ tên (khớp một phần, không phân biệt hoa/thường); lọc theo vai trò và trạng thái. Cột: username, họ tên, SĐT, vai trò, trạng thái, ngày tạo. Không hiển thị hash. | ADMIN |
| FR-06 | Tạo tài khoản | Nhập username, họ tên, SĐT, email (tùy chọn), vai trò, mật khẩu ban đầu và nhập lại. Trạng thái mặc định ACTIVE. | ADMIN |
| FR-07 | Cập nhật tài khoản | Sửa họ tên, SĐT, email, vai trò. Username không sửa được. Không đổi vai trò của chính mình (BR-14). | ADMIN |
| FR-08 | Khóa/mở khóa tài khoản | Chuyển ACTIVE ↔ LOCKED có hộp xác nhận. Không tự khóa (BR-14). Phiên của tài khoản bị khóa mất hiệu lực ở request kế tiếp (FR-03). Không xóa tài khoản. | ADMIN |
| FR-09 | Đổi mật khẩu cá nhân | Nhập mật khẩu hiện tại, mật khẩu mới và nhập lại. Sai mật khẩu hiện tại thì từ chối. Giữ phiên hiện tại. | Tất cả |
| FR-10 | Đặt lại mật khẩu | Đặt mật khẩu mới cho tài khoản khác (không cần mật khẩu cũ). Không áp dụng cho chính mình; không thay đổi trạng thái khóa. | ADMIN |

### 2.2. Phân hệ B — Hội viên

| ID | Chức năng | Mô tả và điều kiện chấp nhận | Vai trò |
| --- | --- | --- | --- |
| FR-11 | Danh sách và tìm kiếm hội viên | Một ô tìm kiếm, kiểu tìm do nội dung nhập quyết định theo thứ tự: (1) đúng dạng `HV` + 6 chữ số như `HV000123`, tiền tố không phân biệt hoa/thường → tìm theo mã, khớp chính xác; (2) đúng dạng SĐT của VR-02 (`^0[0-9]{9}$`) → tìm theo SĐT, khớp chính xác; (3) không chứa chữ số nào → tìm theo họ tên, khớp một phần, không phân biệt hoa/thường; (4) còn lại (như `123`, `HV123`, một phần SĐT, `abc123`) → không có kết quả. Lọc: Đang quản lý (mặc định), Đã xóa, Tất cả — hai lựa chọn sau chỉ MANAGER. Phân trang 20 dòng, mới tạo trước. Cột: mã, họ tên, SĐT, tình trạng gói (BR-07), ngày hết hạn gói hiện tại. | MANAGER, RECEPTIONIST |
| FR-12 | Tạo hội viên | Nhập họ tên, SĐT, email, ngày sinh, giới tính. Mã tự sinh; lưu created\_at, created\_by. SĐT trùng (kể cả hồ sơ đã xóa mềm) thì từ chối và hiển thị mã hồ sơ đang dùng số đó. Tạo được hồ sơ chưa mua gói. | MANAGER, RECEPTIONIST |
| FR-13 | Xem chi tiết hội viên | Hiển thị thông tin hồ sơ, nhãn đã xóa mềm (nếu có), tình trạng gói hôm nay (BR-07), cảnh báo sắp hết hạn (BR-08) và lịch sử đăng ký mọi trạng thái (mới nhất trước). Có nút chuyển sang tạo đăng ký (FR-22) nếu hồ sơ chưa xóa mềm. | MANAGER, RECEPTIONIST |
| FR-14 | Cập nhật hội viên | Sửa họ tên, SĐT, email, ngày sinh, giới tính. Mã và created\_at không sửa. Không sửa hồ sơ đã xóa mềm. | MANAGER, RECEPTIONIST |
| FR-15 | Xóa mềm hội viên | Có hộp xác nhận. Thực hiện theo BR-09; bị chặn thì nêu lý do (còn gói đã thanh toán chưa hết hạn). | MANAGER |
| FR-16 | Khôi phục hội viên | Khôi phục hồ sơ đã xóa mềm theo BR-10. | MANAGER |
| FR-17 | Danh sách sắp hết hạn | Liệt kê hội viên thỏa BR-08 với mã, họ tên, SĐT, tên gói, ngày hết hạn, số ngày còn lại; sắp xếp ngày hết hạn tăng dần. Chỉ hiển thị, không gửi thông báo. | MANAGER, RECEPTIONIST |

### 2.3. Phân hệ C — Gói tập và đăng ký

| ID | Chức năng | Mô tả và điều kiện chấp nhận | Vai trò |
| --- | --- | --- | --- |
| FR-18 | Danh sách loại gói | Cột: tên, thời hạn, giá, trạng thái bán. MANAGER xem tất cả và lọc đang bán/ngừng bán; RECEPTIONIST chỉ xem loại đang bán, không có nút thao tác. | MANAGER, RECEPTIONIST |
| FR-19 | Tạo loại gói | Nhập tên, chọn thời hạn 1/3/6/12 tháng, nhập giá. Mặc định đang bán. | MANAGER |
| FR-20 | Cập nhật loại gói | Sửa tên và giá; thời hạn chỉ đọc. Không ảnh hưởng đăng ký đã tạo (BR-03). | MANAGER |
| FR-21 | Ngừng bán/mở bán lại | Đổi trạng thái bán theo BR-04. Không xóa loại gói. | MANAGER |
| FR-22 | Tạo đăng ký | Chọn hội viên chưa xóa mềm và loại gói đang bán; tạo đăng ký PENDING kèm snapshot tên, thời hạn, giá (BR-03), created\_at, created\_by. Giá lấy từ DB, không nhận từ form. Thành công thì chuyển tới bước rà soát FR-25. | MANAGER, RECEPTIONIST |
| FR-23 | Hủy đăng ký PENDING | Nhập lý do hủy; chuyển PENDING → CANCELLED và lưu cancelled\_at, cancelled\_by, lý do. Chỉ áp dụng cho PENDING (BR-02). | MANAGER, RECEPTIONIST |
| FR-24 | Danh sách đăng ký/giao dịch | Lọc theo trạng thái, loại gói, từ khóa hội viên (mã, SĐT hoặc họ tên, cùng quy tắc phân loại như FR-11), và khoảng ngày trên một trục thời gian do người dùng chọn: Ngày tạo (mọi trạng thái) hoặc Ngày thanh toán (chỉ PAID) theo BR-13. Phân trang 20 dòng. Cột: mã, hội viên, gói (snapshot), số tiền, trạng thái, phương thức, ngày tạo, ngày thanh toán, ngày bắt đầu–hết hạn. Dòng PENDING có nút Xác nhận và Hủy. | MANAGER, RECEPTIONIST |

### 2.4. Phân hệ D — Thanh toán và báo cáo

| ID | Chức năng | Mô tả và điều kiện chấp nhận | Vai trò |
| --- | --- | --- | --- |
| FR-25 | Xác nhận thanh toán | Hai bước, dùng JSP thuần. Bước 1 (rà soát): hiển thị hội viên, gói, số tiền snapshot, ngày bắt đầu và ngày hết hạn dự kiến (BR-05); người dùng chọn phương thức Tiền mặt hoặc Chuyển khoản. Bước 2 (xác nhận cuối): backend thực hiện BR-06 rồi lưu PAID, paid\_at, confirmed\_by, payment\_method, start\_date, end\_date trong một transaction. Quay lại từ bước 1 không đổi dữ liệu. Không nhận số tiền hay ngày từ client. Thành công thì hiển thị lịch gói thực tế đã lưu. | MANAGER, RECEPTIONIST |
| FR-26 | Doanh thu theo kỳ | Chọn khoảng ngày và mức gộp (ngày hoặc tháng). Bảng: kỳ, số giao dịch PAID, doanh thu; dòng tổng cuối bảng. Theo BR-12, BR-13. | MANAGER |
| FR-27 | Doanh thu theo loại gói | Cùng khoảng ngày với FR-26; nhóm theo loại gói (BR-12), hiển thị tên hiện hành, số giao dịch, doanh thu, tỷ trọng %. Tổng bằng tổng FR-26 cùng kỳ. | MANAGER |
| FR-28 | Tổng quan hội viên | Trang chủ của MANAGER. Chỉ số tại hôm nay: số hồ sơ đang quản lý (chưa xóa mềm), số hồ sơ đã xóa mềm, số hội viên đang có gói hiệu lực, số hội viên sắp hết hạn (BR-08). Chỉ số theo khoảng ngày: số hội viên mới (theo created\_at, kể cả hồ sơ sau đó bị xóa mềm). | MANAGER |

## 3. Phân quyền

Quyền được kiểm tra ở backend cho mọi request (GET và POST), kể cả khi gọi URL trực tiếp; ẩn nút trên giao diện không thay thế kiểm tra này. **C** = được phép; **K** = bị từ chối bằng HTTP 403, dữ liệu không đổi.

| Hành động | ADMIN | MANAGER | RECEPTIONIST | Chức năng |
| --- | --- | --- | --- | --- |
| Đăng nhập, đăng xuất, đổi mật khẩu cá nhân | C | C | C | FR-01 – FR-04, FR-09 |
| Xem, tạo, sửa, khóa/mở khóa tài khoản; đặt lại mật khẩu | C | K | K | FR-05 – FR-08, FR-10 |
| Xem, tìm, tạo, sửa hội viên; danh sách sắp hết hạn | K | C | C | FR-11 – FR-14, FR-17 |
| Xem hội viên đã xóa mềm; xóa mềm; khôi phục | K | C | K | FR-11, FR-15, FR-16 |
| Xem loại gói đang bán | K | C | C | FR-18 |
| Xem loại gói ngừng bán; tạo, sửa, ngừng bán/mở bán lại | K | C | K | FR-18 – FR-21 |
| Tạo đăng ký, hủy PENDING, xem/lọc đăng ký | K | C | C | FR-22 – FR-24 |
| Xác nhận thanh toán | K | C | C | FR-25 |
| Báo cáo doanh thu, tổng quan hội viên | K | C | K | FR-26 – FR-28 |
| Sửa/hủy đăng ký PAID, xóa vật lý bất kỳ dữ liệu nào | K | K | K | BR-02, BR-16 |

Triển khai: mỗi URL thuộc một tiền tố theo nhóm quyền (`/admin/*`, `/manager/*`, `/staff/*` cho hành động chung MANAGER và RECEPTIONIST); `AuthFilter` (FR-03) so vai trò trong session với tiền tố. Vai trò cố định trong code, không có bảng quyền động.

## 4. Quy tắc nghiệp vụ

17 quy tắc dưới đây là phần chấm "Business rules/logic" (30%); mỗi quy tắc phải được kiểm tra tại tầng Service, không chỉ ở giao diện.

### 4.1. Hội viên và đăng ký

| ID | Quy tắc | Nội dung |
| --- | --- | --- |
| BR-01 | Hồ sơ độc lập với gói | Dấu xóa mềm của hội viên và tình trạng gói là hai thông tin riêng. Hồ sơ được tạo trước khi mua gói. |
| BR-02 | Vòng đời đăng ký | Chỉ có hai chuyển trạng thái: PENDING → PAID (FR-25) và PENDING → CANCELLED (FR-23, BR-09). PAID và CANCELLED là trạng thái cuối, không sửa, không hủy, không hoàn tiền. PENDING không tự hết hạn, không giữ lịch, không tính doanh thu. Mọi chuyển trạng thái dùng `UPDATE ... WHERE id = ? AND status = 'PENDING'`; 0 dòng ảnh hưởng thì báo "Đăng ký không còn chờ thanh toán". |
| BR-03 | Snapshot | Khi tạo đăng ký, lưu tên gói, số tháng và giá hiện hành vào đăng ký. Mọi màn hình, xác nhận thanh toán và báo cáo dùng snapshot; sửa loại gói về sau không làm đổi đăng ký đã tạo. |
| BR-04 | Ngừng bán | Loại gói ngừng bán không xuất hiện khi tạo đăng ký và backend từ chối nếu bị gửi trực tiếp. Đăng ký PENDING đã có của loại đó vẫn xác nhận được. |
| BR-05 | Xếp lịch gói khi PAID | Ngày do hệ thống tính, không nhận từ người dùng; công thức và ví dụ ở mục 4.2. Gói mới nối tiếp chuỗi PAID còn hạn, không chồng lấn, không khoảng trống; nếu mọi PAID đã hết hạn thì bắt đầu hôm nay. Thứ tự xác nhận PAID quyết định thứ tự lịch, không phải thứ tự tạo PENDING. |
| BR-06 | Đồng thời và nguyên tử | Tạo đăng ký (FR-22), xác nhận thanh toán (FR-25) và xóa mềm hội viên (FR-15) chạy trong một DB transaction, bắt đầu bằng `SELECT ... FROM members WITH (UPDLOCK, HOLDLOCK) WHERE id = ?`, sau đó mới kiểm tra lại trạng thái và ghi. Lỗi bất kỳ bước nào thì rollback toàn bộ; không để lại PAID thiếu lịch. |
| BR-07 | Tình trạng gói | Suy ra từ các đăng ký PAID, không lưu cột riêng. Theo thứ tự ưu tiên: **Đang hiệu lực** (có PAID với start\_date ≤ hôm nay ≤ end\_date) → **Chờ hiệu lực** (chỉ có PAID start\_date > hôm nay) → **Hết hạn** (mọi PAID có end\_date < hôm nay) → **Chưa có gói**. "Ngày hết hạn gói" hiển thị = end\_date lớn nhất trong các PAID. |
| BR-08 | Sắp hết hạn | Hội viên chưa xóa mềm, Đang hiệu lực, và end\_date lớn nhất trong các PAID cách hôm nay 0–6 ngày (còn 1–7 ngày sử dụng, tính cả hôm nay). Vì dùng end\_date lớn nhất, hội viên đã có gói nối tiếp không bị cảnh báo. PENDING không được tính là gia hạn. |
| BR-09 | Xóa mềm hội viên | Trong transaction của BR-06: nếu còn bất kỳ PAID nào có end\_date ≥ hôm nay (đang hoặc chờ hiệu lực) thì từ chối, không đổi gì. Ngược lại: đặt is\_deleted = 1, chuyển mọi PENDING của hội viên sang CANCELLED với lý do "Xóa hồ sơ hội viên"; PAID giữ nguyên. |
| BR-10 | Khôi phục hội viên | Đặt is\_deleted = 0, giữ nguyên mã, created\_at và lịch sử. Đăng ký đã CANCELLED không tự khôi phục. Khách cũ quay lại phải khôi phục hồ sơ cũ, không tạo hồ sơ mới cùng SĐT. |
| BR-11 | Hồ sơ đã xóa mềm | Không sửa hồ sơ, không tạo đăng ký, không xác nhận thanh toán cho đến khi khôi phục. Chỉ MANAGER xem được. |

### 4.2. Công thức xếp lịch (BR-05)

```
start_date = max( hôm nay,  max(end_date của mọi PAID của hội viên) + 1 ngày )
end_date   = start_date.plusMonths(duration_months_snapshot).minusDays(1)
```

Nếu hội viên chưa có PAID, vế thứ hai bị bỏ qua. `LocalDate.plusMonths` tự lùi về ngày cuối tháng khi tháng đích thiếu ngày. Mỗi gói cộng tháng từ ngày bắt đầu của chính nó.

| Tình huống (hôm nay 24/10/2026) | Gói | start\_date | end\_date |
| --- | --- | --- | --- |
| Chưa từng có PAID | 1 tháng | 24/10/2026 | 23/11/2026 |
| PAID cuối cùng hết hạn 10/09/2026 | 3 tháng | 24/10/2026 | 23/01/2027 |
| PAID đang hiệu lực đến 31/10/2026 | 1 tháng | 01/11/2026 | 30/11/2026 |
| PAID có end\_date 30/01/2027, gói mới bắt đầu 31/01/2027 | 1 tháng | 31/01/2027 | 27/02/2027 |
| Nối tiếp dòng trên | 1 tháng | 28/02/2027 | 27/03/2027 |

### 4.3. Báo cáo, tài khoản và hệ thống

| ID | Quy tắc | Nội dung |
| --- | --- | --- |
| BR-12 | Doanh thu | Doanh thu = tổng price\_snapshot của đăng ký PAID có paid\_at trong kỳ. Loại PENDING và CANCELLED. Doanh thu theo loại gói nhóm theo package\_id (không theo tên snapshot), hiển thị tên hiện hành. |
| BR-13 | Khoảng ngày lọc | "Từ ngày A đến ngày B" nghĩa là `[A 00:00, B+1 00:00)`, gồm trọn cả hai ngày. A ≤ B. Mặc định là tháng hiện tại. Trục Ngày thanh toán tự loại đăng ký chưa PAID. |
| BR-14 | Bảo vệ tài khoản | ADMIN không tự khóa, không tự đổi vai trò, không tự đặt lại mật khẩu (dùng FR-09). Nhờ vậy hệ thống luôn còn ít nhất một ADMIN ACTIVE. |
| BR-15 | Đồng hồ nghiệp vụ | Một lớp `AppClock` duy nhất trả về ngày giờ theo `Asia/Ho_Chi_Minh` cho created\_at, paid\_at, "hôm nay" và kỳ báo cáo. Không dùng `GETDATE()` của SQL Server hay múi giờ mặc định của hệ điều hành cho tính toán nghiệp vụ. |
| BR-16 | Không xóa vật lý | Không xóa vật lý tài khoản, hội viên, loại gói, đăng ký. "Xóa" chỉ là xóa mềm hội viên (FR-15); tài khoản dùng khóa, loại gói dùng ngừng bán, đăng ký dùng hủy. |
| BR-17 | Cookie ghi nhớ username | Đăng nhập thành công có chọn ô: đặt cookie `gym_username` (giá trị username, 30 ngày, HttpOnly, Path của ứng dụng). Thành công không chọn ô: xóa cookie (Max-Age = 0). Thất bại: không tạo hay làm mới cookie. Mở trang đăng nhập: điền sẵn username đã escape, chọn sẵn ô; giá trị sai định dạng VR-08 thì bỏ qua. Đăng xuất không xóa cookie. Cookie không chứa mật khẩu, vai trò hay dữ liệu xác thực. |

## 5. Yêu cầu dữ liệu

Cơ sở dữ liệu `GymDB` trên SQL Server 2022 gồm đúng 4 bảng, tạo bằng `database/GymDB_Setup.sql`. Tên bảng và tên cột trong tài liệu này là tên thật trong schema.

| ID | Yêu cầu | Nội dung |
| --- | --- | --- |
| DR-01 | Danh mục giá trị cố định | Lưu dạng chuỗi kèm ràng buộc `CHECK`, không có bảng danh mục: vai trò (`ADMIN`, `MANAGER`, `RECEPTIONIST`); trạng thái tài khoản (`ACTIVE`, `LOCKED`); trạng thái đăng ký (`PENDING`, `PAID`, `CANCELLED`); phương thức thanh toán (`CASH`, `TRANSFER`); giới tính (`Nam`, `Nữ`, `Khác`); thời hạn gói (1, 3, 6, 12 tháng). |
| DR-02 | Các bảng và quan hệ | `accounts` (nhân viên); `members` (hồ sơ hội viên, `created_by` → `accounts`); `packages` (loại gói); `registrations` (đăng ký/giao dịch, tham chiếu `members`, `packages` và `accounts` qua `created_by`, `confirmed_by`, `cancelled_by`). Khóa chính `id INT IDENTITY(1,1)` ở cả 4 bảng. |
| DR-03 | Ràng buộc toàn vẹn | Duy nhất: `accounts.username` (không phân biệt hoa/thường), `accounts.phone`, `members.phone`, `packages.name` (không phân biệt hoa/thường). Giá gói và `price_snapshot` trong khoảng 1–100.000.000. `registrations` có `CHECK` bảo đảm các cột thanh toán và hủy khớp với `status`: PENDING không có cột thanh toán/hủy; PAID có đủ `payment_method`, `paid_at`, `confirmed_by`, `start_date`, `end_date` (`end_date` ≥ `start_date`); CANCELLED có đủ `cancelled_at`, `cancelled_by`, `cancel_reason` (3–255 ký tự sau trim). Không có thao tác xóa vật lý (BR-16). |
| DR-04 | Kiểu dữ liệu và quy ước | Tiền là số nguyên VND (`INT`). Thời điểm dùng `DATETIME2(0)` theo giờ Việt Nam do `AppClock` cung cấp (BR-15); ngày hiệu lực gói (`start_date`, `end_date`) và ngày sinh dùng `DATE`. Hội viên không có cột mã riêng: mã hiển thị là `HV` + `members.id` đủ 6 chữ số (ví dụ id 123 → `HV000123`). Cờ xóa mềm `members.is_deleted` và cờ đang bán `packages.is_active` là `BIT`. Tình trạng gói không lưu cột (BR-07). Họ tên, tên gói, lý do hủy lưu `NVARCHAR` để có dấu tiếng Việt. |
| DR-05 | Dữ liệu mẫu | Seed cố định theo mốc 27/10/2026, chỉ gồm lệnh `INSERT` trực tiếp: 5 tài khoản (1 ADMIN, 2 MANAGER, 2 RECEPTIONIST, trong đó 1 RECEPTIONIST bị LOCKED); 5 loại gói (4 đang bán, 1 ngừng bán); 100 hội viên (8 hồ sơ đã xóa mềm); 150 đăng ký (100 PAID, 30 PENDING, 20 CANCELLED). Mật khẩu mẫu chỉ lưu dưới dạng hash PBKDF2 (NFR-03); danh sách tài khoản mẫu ở `database/demo_account.md`. |

## 6. Kiểm tra hợp lệ

Mọi quy tắc dưới đây bắt buộc kiểm tra ở backend cho cả form tạo và form sửa. Client có thể thêm thuộc tính HTML5 (`required`, `maxlength`, `pattern`) nhưng backend không tin client. Khi lỗi: hiển thị lại form, giữ giá trị đã nhập (trừ mật khẩu), báo lỗi cạnh từng trường, không lưu gì.

Họ tên, email, tên gói, lý do hủy được trim trước khi kiểm tra và lưu. Username và mật khẩu không trim.

| ID | Trường | Bắt buộc | Quy tắc |
| --- | --- | --- | --- |
| VR-01 | Họ tên (hội viên, nhân viên) | Có | 2–100 ký tự sau trim; không chứa chữ số |
| VR-02 | SĐT hội viên | Có | Regex `^0[0-9]{9}$`; duy nhất trong bảng members kể cả hồ sơ đã xóa mềm |
| VR-03 | SĐT nhân viên | Có | Regex `^0[0-9]{9}$`; duy nhất trong bảng accounts; được trùng SĐT hội viên |
| VR-04 | Email | Không | Nếu nhập: tối đa 254 ký tự, đúng dạng `^[^@\s]+@[^@\s]+\.[^@\s]+$`; được trùng |
| VR-05 | Ngày sinh | Không | Ngày hợp lệ, từ 01/01/1900 đến hôm nay |
| VR-06 | Giới tính | Không | Nam, Nữ hoặc Khác |
| VR-07 | Tìm kiếm | Không | Tối đa 100 ký tự; trang (page) là số nguyên ≥ 1, sai thì về trang 1 |
| VR-08 | Username | Có | Regex `^[a-zA-Z][a-zA-Z0-9_]{3,49}$` (4–50 ký tự, không dấu, không khoảng trắng); duy nhất không phân biệt hoa/thường |
| VR-09 | Mật khẩu | Có | 8–64 ký tự; giữ nguyên chuỗi nhập; ô nhập lại phải khớp chính xác |
| VR-10 | Vai trò, trạng thái tài khoản | Có | Thuộc danh sách cố định tại DR-01 |
| VR-11 | Tên gói | Có | 2–100 ký tự sau trim; không trùng sau khi gộp khoảng trắng liên tiếp và bỏ phân biệt hoa/thường |
| VR-12 | Giá gói | Có | Số nguyên 1–100.000.000 VND; chỉ chữ số, không âm |
| VR-13 | Thời hạn gói | Có (khi tạo) | 1, 3, 6 hoặc 12 tháng |
| VR-14 | Phương thức thanh toán | Có (khi xác nhận) | CASH hoặc TRANSFER |
| VR-15 | Lý do hủy đăng ký | Có | 3–255 ký tự sau trim |
| VR-16 | Khoảng ngày lọc/báo cáo | Không | Định dạng ngày hợp lệ; từ ngày ≤ đến ngày; trống thì dùng mặc định BR-13 |
| VR-17 | ID trong URL/form | Có | Số nguyên dương và bản ghi tồn tại; sai thì trả 404, không lộ lỗi hệ thống |

## 7. Yêu cầu phi chức năng

| ID | Yêu cầu | Căn cứ kiểm tra |
| --- | --- | --- |
| NFR-01 | Kiến trúc MVC ba lớp | Servlet chỉ điều phối, Service chứa quy tắc BR, DAO chứa SQL. JSP không chứa SQL hay scriptlet nghiệp vụ; dùng JSTL/EL. Mỗi phân hệ có package riêng (`controller/`, `service/`, `dao/`, `model/` theo phân hệ) để thêm chức năng mà không sửa phân hệ khác. |
| NFR-02 | Truy vấn an toàn | 100% câu SQL dùng PreparedStatement có tham số; không nối chuỗi giá trị người dùng vào SQL (kể cả cột sắp xếp: chỉ nhận từ danh sách cố định). |
| NFR-03 | Mật khẩu băm | Lưu bằng BCrypt cost ≥ 10 hoặc PBKDF2-HMAC-SHA256 có salt riêng. Không lưu plaintext, không dùng MD5/SHA không salt. Seed cũng chỉ chứa hash. (Tránh mức phạt −50%.) |
| NFR-04 | Chống XSS | Mọi dữ liệu người dùng hiển thị qua `<c:out>` hoặc `fn:escapeXml`, kể cả giá trị cookie. |
| NFR-05 | Phiên và cookie | Cookie JSESSIONID HttpOnly; timeout 30 phút cấu hình trong `web.xml`. Ngoài JSESSIONID chỉ có `gym_username` (BR-17). |
| NFR-06 | Transaction | Mọi thao tác ghi nhiều bảng hoặc nhiều dòng (FR-15, FR-22, FR-25) chạy trong một JDBC transaction (`setAutoCommit(false)`), rollback khi lỗi. |
| NFR-07 | Chống gửi lặp | Thao tác ghi dùng POST rồi chuyển hướng (Post/Redirect/Get) kèm thông báo flash trong session; F5 không tạo bản ghi trùng. |
| NFR-08 | Xử lý lỗi | Trang lỗi chung cho 403, 404, 500 khai báo trong `web.xml`; không hiển thị stack trace hay lỗi SQL cho người dùng. |
| NFR-09 | Dữ liệu | Hoạt động đúng với tối thiểu 100 hội viên và 150 đăng ký từ seed (DR-05); danh sách lớn dùng phân trang phía SQL (`OFFSET ... FETCH`). Dùng SQL Server 2022 (tránh mức phạt −40%). |
| NFR-10 | Cấu hình | Chuỗi kết nối DB đọc từ một file cấu hình (`db.properties`) không commit lên repository; có file mẫu `db.properties.example`. |
| NFR-11 | Giao diện | Một layout chung (header, menu theo vai trò, vùng thông báo) dùng `<jsp:include>`; Bootstrap 5 qua file tĩnh trong dự án để chạy offline. |

Không đặt SLA, ngưỡng thời gian phản hồi hay số phiên đồng thời bắt buộc.

## 8. Điều kiện chấp nhận

### 8.1. Phân hệ A

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| AC-01 | Đăng nhập đúng; sai mật khẩu; username viết hoa khác; tài khoản LOCKED | Đúng và khác hoa/thường thì vào trang chủ theo vai trò, session ID đổi; sai và LOCKED bị từ chối với cùng một thông báo chung | FR-01 |
| AC-02 | Đăng xuất rồi bấm Back hoặc mở lại URL cũ; để yên 31 phút | Bị chuyển về trang đăng nhập ở cả hai trường hợp | FR-02, FR-03 |
| AC-03 | ADMIN khóa tài khoản X trong khi X đang đăng nhập ở trình duyệt khác; đổi vai trò của Y đang đăng nhập | Request kế tiếp của X và Y bị đăng xuất; X không đăng nhập lại được cho tới khi mở khóa | FR-03, FR-07, FR-08 |
| AC-04 | Đăng nhập có và không chọn ô ghi nhớ; đăng nhập sai; sửa cookie thành `<script>alert(1)</script>` | Có cookie `gym_username` 30 ngày, HttpOnly, chỉ chứa username; mở lại trang thì điền sẵn nhưng vẫn phải nhập mật khẩu; không chọn ô thì cookie bị xóa; đăng nhập sai không đổi cookie; cookie độc hại bị bỏ qua, không chạy script | FR-04, BR-17 |
| AC-05 | Mỗi vai trò gọi trực tiếp một URL GET và một POST thuộc ô K trong mục 3 | Tất cả nhận 403, dữ liệu không đổi | Mục 3 |
| AC-06 | Tạo tài khoản với username `abc`, `Khanh_20`, `khanh.nd`, `2khanh`, `KHANH_20` (đã có `Khanh_20`); SĐT 9 số, trùng tài khoản khác, trùng một hội viên | Chỉ `Khanh_20` được nhận; `KHANH_20` báo trùng; SĐT 9 số và trùng tài khoản bị từ chối; trùng hội viên được nhận; form giữ giá trị đã nhập | FR-06, VR-03, VR-08 |
| AC-07 | ADMIN thử tự khóa, tự đổi vai trò, tự đặt lại mật khẩu | Cả ba bị từ chối, kể cả khi gửi POST trực tiếp | BR-14 |
| AC-08 | Đổi mật khẩu với mật khẩu hiện tại sai; mật khẩu mới 7 và 65 ký tự; có khoảng trắng đầu/cuối; ADMIN đặt lại mật khẩu tài khoản LOCKED | Sai mật khẩu cũ và sai độ dài bị từ chối; khoảng trắng được giữ nguyên và đăng nhập lại đúng; tài khoản LOCKED vẫn LOCKED sau khi đặt lại; DB chỉ có hash | FR-09, FR-10, VR-09, NFR-03 |

### 8.2. Phân hệ B

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| AC-09 | Tìm theo `HV000005`, `hv000005`, `5`, `HV5`, một phần SĐT, một phần tên viết thường; chuyển trang 1 → 5 | `HV000005` và `hv000005` ra hội viên số 5; `5` và `HV5` không được coi là mã nên không ra kết quả; SĐT đủ 10 số đúng dạng VR-02 khớp chính xác, một phần SĐT không ra kết quả; tên khớp một phần không phân biệt hoa/thường; phân trang giữ điều kiện tìm | FR-11 |
| AC-10 | Tạo/sửa hội viên với họ tên toàn khoảng trắng, email sai, ngày sinh tương lai, SĐT trùng hồ sơ đã xóa mềm; gửi POST trực tiếp bỏ qua HTML5 | Tất cả bị từ chối ở backend, không lưu; SĐT trùng hiển thị mã hồ sơ đang giữ số | FR-12, FR-14, VR-01 – VR-06 |
| AC-11 | Hội viên có PAID đang hiệu lực (hoặc chỉ có PAID chờ hiệu lực) và 2 PENDING: MANAGER xóa mềm | Bị từ chối kèm lý do; hai PENDING không đổi | FR-15, BR-09 |
| AC-12 | Hội viên có mọi PAID đã hết hạn và 2 PENDING: MANAGER xóa mềm; RECEPTIONIST thử xóa | MANAGER thành công: is\_deleted = 1, hai PENDING thành CANCELLED trong cùng transaction, PAID không đổi; RECEPTIONIST nhận 403 | FR-15, BR-06, BR-09 |
| AC-13 | Khôi phục hội viên ở AC-12; sau đó tạo đăng ký mới; thử sửa hoặc tạo đăng ký khi hồ sơ còn xóa mềm | Khôi phục giữ mã, created\_at, lịch sử; CANCELLED giữ nguyên; sau khôi phục tạo đăng ký được; khi còn xóa mềm thì sửa và tạo đăng ký bị chặn | FR-16, BR-10, BR-11 |
| AC-14 | Ba hội viên có gói hết hạn 30/10 (còn 7 ngày), 31/10 (còn 8 ngày) và 30/10 nhưng có PAID nối tiếp | Chỉ hội viên đầu tiên có trong danh sách sắp hết hạn và có cảnh báo ở trang chi tiết | FR-13, FR-17, BR-07, BR-08 |

### 8.3. Phân hệ C

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| AC-15 | Tạo loại gói trùng tên khác hoa/thường, giá 0, giá âm, thời hạn 2 tháng (sửa HTML); sửa thời hạn bằng POST trực tiếp | Tất cả bị từ chối; thời hạn không đổi | FR-19, FR-20, VR-11 – VR-13 |
| AC-16 | Tạo PENDING giá 500.000; đổi tên và giá loại gói thành 600.000; tạo PENDING mới; xác nhận cả hai | Đăng ký cũ giữ tên cũ và 500.000 ở mọi màn hình và báo cáo; đăng ký mới dùng 600.000 | FR-20, FR-22, BR-03 |
| AC-17 | Ngừng bán loại gói có PENDING; tạo đăng ký mới qua form và POST trực tiếp; xác nhận PENDING cũ; mở bán lại | Tạo mới bị từ chối; PENDING cũ xác nhận được; mở bán lại thì tạo được | FR-21, FR-22, BR-04 |
| AC-18 | Hủy PENDING không nhập lý do; hủy có lý do; gửi POST hủy cho đăng ký PAID và CANCELLED | Thiếu lý do bị từ chối; hủy hợp lệ lưu người, thời điểm, lý do; PAID và CANCELLED không đổi | FR-23, BR-02, VR-15 |
| AC-19 | Có PENDING tạo tháng 9 và thanh toán tháng 10; lọc tháng 10 theo Ngày tạo và theo Ngày thanh toán; lọc 01/10 – 31/10 với giao dịch lúc 31/10 23:30 và 01/11 00:00 | Theo Ngày tạo không thấy, theo Ngày thanh toán thấy; 31/10 23:30 được tính, 01/11 00:00 không | FR-24, BR-13 |

### 8.4. Phân hệ D

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| AC-20 | Xác nhận thanh toán: dừng ở bước rà soát rồi quay lại; không chọn phương thức; sửa HTML gửi thêm số tiền và ngày bắt đầu; xác nhận hợp lệ | Quay lại không đổi dữ liệu; thiếu phương thức bị từ chối; số tiền và ngày từ client bị bỏ qua; hợp lệ thì PAID với đủ paid\_at, confirmed\_by, lịch | FR-25, BR-02 |
| AC-21 | Xếp lịch cho bốn trường hợp: chưa có PAID; mọi PAID hết hạn; có PAID đến 31/10/2026; gói 1 tháng bắt đầu 31/01/2027 rồi gói nối tiếp | Kết quả đúng từng dòng bảng ví dụ tại mục 4.2 | FR-25, BR-05 |
| AC-22 | Hai PENDING A (3 tháng, tạo trước) và B (1 tháng) của hội viên có PAID đến 31/10; xác nhận B trước A | B: 01/11 – 30/11/2026; A: 01/12/2026 – 28/02/2027; không chồng lấn | FR-25, BR-05 |
| AC-23 | Hai tab cùng bấm xác nhận một PENDING; hai tab xác nhận hai PENDING khác nhau của cùng hội viên gần như đồng thời; một tab xóa mềm hội viên trong khi tab kia xác nhận | Một PENDING chỉ thành PAID một lần, tab còn lại báo không còn chờ; hai PAID nối tiếp không chồng lấn; không có PAID mới cho hội viên đã xóa mềm, hoặc xóa mềm bị chặn vì PAID mới | FR-25, BR-06 |
| AC-24 | Trong kỳ có PAID của 3 loại gói (một loại đổi tên giữa kỳ), có PENDING và CANCELLED; 100 hồ sơ với 8 đã xóa mềm, 3 tạo trong kỳ (1 trong số đó đã xóa) | Doanh thu chỉ gồm PAID; loại đổi tên vẫn là một dòng; tổng FR-27 = tổng FR-26; tổng quan: 92 đang quản lý, 8 đã xóa, 3 hội viên mới | FR-26 – FR-28, BR-12 |

## 9. Phân công và quy định

### 9.1. Phân công và giao diện dùng chung

Mỗi phân hệ do một thành viên phụ trách và sở hữu toàn bộ chức năng, quy tắc liên quan của phân hệ đó.

| Thành viên | Họ tên | Phân hệ | Chức năng |
| --- | --- | --- | --- |
| TV1 | Nguyen Duy Khanh | A — Truy cập và tài khoản | FR-01 – FR-10 |
| TV2 | TBD | B — Hội viên | FR-11 – FR-17 |
| TV3 | Nguyen Sy Nguyen | C — Gói tập và đăng ký | FR-18 – FR-24 |
| TV4 | Nguyen Bao Toan | D — Thanh toán và báo cáo | FR-25 – FR-28 |

Các thành phần dùng chung giữa phân hệ là giao diện đã thống nhất; thay đổi chữ ký phải báo các thành viên dùng nó.

| Giao diện | Chủ sở hữu | Nơi dùng |
| --- | --- | --- |
| `DBContext` (kết nối, transaction) | Cả nhóm | Mọi DAO |
| `SessionUser`, khóa session | TV1 | Mọi controller (FR-01, FR-03) |
| `MemberDAO`: tìm theo id, khóa dòng hội viên (BR-06), tìm hội viên chưa xóa mềm | TV2 | FR-15, FR-22, FR-25 |
| `MembershipStatusService` (BR-07, BR-08) | TV2 | FR-28 (TV4) |
| `RegistrationDAO`: lịch sử đăng ký của hội viên, hủy mọi PENDING của hội viên | TV3 | FR-13, FR-15 (TV2) |
| `PaymentService` (xác nhận thanh toán) | TV4 | Nút Xác nhận của FR-24 (TV3) |
| `AppClock` (BR-15) | TV4 | Mọi tính toán ngày giờ |

### 9.2. Quy định học thuật

Mỗi thành viên phải giải thích được mã nguồn của phân hệ mình phụ trách và các giao diện dùng chung mà phân hệ đó gọi tới. Mã sao chép từ nguồn ngoài mà không hiểu, hoặc mã của người khác nộp như của mình, bị xử lý theo mức phạt đạo văn / không hiểu code (−90%) và được đánh giá khi bảo vệ.

## 10. Truy vết tiêu chí chấm điểm (PRJ301)

Cả 11 tiêu chí chính (100%) đều có chức năng và AC đáp ứng; các mức phạt được chặn bằng NFR tương ứng. Bonus không nằm trong phạm vi (mục 11).

| Tiêu chí | Tỷ trọng | Chức năng / quy tắc đáp ứng | AC | Phụ trách |
| --- | --- | --- | --- | --- |
| Business rules/logic | 30% | BR-01 – BR-13; trọng tâm FR-15, FR-22, FR-25 | AC-11 – AC-14, AC-16 – AC-24 | TV2, TV3, TV4 |
| Authentication: Login/Logout | 5% | FR-01, FR-02 | AC-01, AC-02 | TV1 |
| Authorization: User roles | 5% | Mục 3, FR-03 | AC-03, AC-05, AC-07 | TV1 |
| List | 15% | FR-05, FR-11, FR-17, FR-18, FR-24, FR-26 – FR-28 (tìm kiếm, lọc, phân trang) | AC-09, AC-14, AC-19, AC-24 | Cả nhóm |
| Create | 10% | FR-06, FR-12, FR-19, FR-22 | AC-06, AC-10, AC-15, AC-16 | TV1, TV2, TV3 |
| Create - validation | 5% | Mục 6 (VR-01 – VR-13) | AC-06, AC-10, AC-15 | TV1, TV2, TV3 |
| Update | 10% | FR-07 – FR-10, FR-14, FR-16, FR-20, FR-21, FR-23, FR-25 | AC-08, AC-13, AC-17, AC-18, AC-20 | Cả nhóm |
| Update - validation | 5% | Mục 6 (VR-01 – VR-17) | AC-08, AC-10, AC-15, AC-18, AC-20 | Cả nhóm |
| Delete | 5% | FR-15 (xóa mềm hội viên kèm hủy PENDING) | AC-11, AC-12 | TV2 |
| Cookies | 5% | FR-04, BR-17 | AC-04 | TV1 |
| Session | 5% | FR-03, NFR-05 | AC-02, AC-03 | TV1 |
| Phạt: mật khẩu không hash (−50%) | — | NFR-03 | AC-08 | TV1 |
| Phạt: không dùng DB (−40%) | — | NFR-09, mục 5 | Mọi AC | Cả nhóm |
| Phạt: đạo văn / không hiểu code (−90%) | — | Mục 9.2 | Bảo vệ | Cả nhóm |

## 11. Ngoài phạm vi baseline

Các nội dung sau không thuộc baseline và không được tính vào điều kiện chấp nhận:

- Quản lý lượt ra/vào phòng tập; hiệu lực gói chỉ suy ra từ đăng ký PAID (mục 1.2).
- Tích hợp ngân hàng hoặc cổng thanh toán; thanh toán chỉ là xác nhận thủ công.
- Sửa, hủy hoặc hoàn tiền đăng ký đã PAID (BR-02).
- Gửi thông báo cho hội viên; danh sách sắp hết hạn chỉ hiển thị (FR-17).
- Tài khoản đăng nhập cho hội viên; bảng phân quyền động (mục 3).
- Tự động đăng nhập bằng cookie; cookie chỉ ghi nhớ username (BR-17).
