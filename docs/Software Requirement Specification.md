# SRS Hệ thống quản lý phòng gym — Baseline 2.0

## 1. Mô tả dự án

### 1.1. Mục đích

Tài liệu xác định chức năng, quyền truy cập, quy tắc nghiệp vụ, dữ liệu và điều kiện chấp nhận của Gym Management System. Đây là căn cứ duy nhất để thiết kế schema, viết code, seed dữ liệu và kiểm thử; các tiêu chí trong tài liệu không phải kết quả kiểm thử.

### 1.2. Phạm vi

Website nội bộ cho nhân viên phòng gym, gồm 4 phân hệ:

| Mã | Phân hệ | Nội dung | Chức năng |
| --- | --- | --- | --- |
| A | Truy cập và tài khoản | Đăng nhập/đăng xuất, phiên, cookie, quản lý tài khoản nhân viên, mật khẩu | CN-01 – CN-10 |
| B | Hội viên | CRUD hồ sơ, tìm kiếm, xóa mềm/khôi phục, tình trạng gói, cảnh báo sắp hết hạn | CN-11 – CN-17 |
| C | Gói tập và đăng ký | Loại gói, tạo/hủy đăng ký PENDING, tra cứu đăng ký | CN-18 – CN-24 |
| D | Thanh toán và báo cáo | Xác nhận thanh toán, xếp lịch gói, báo cáo doanh thu và hội viên | CN-25 – CN-28 |

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
| Hôm nay | Ngày hiện tại theo giờ Việt Nam, lấy từ lớp đồng hồ nghiệp vụ (QT-15) |

### 1.6. Quy ước ký hiệu

Mã gồm tiền tố và hai chữ số, đánh số liên tục từ 01 trong baseline này; không dùng lại số của baseline 1.3.

| Tiền tố | Loại yêu cầu | Ví dụ |
| --- | --- | --- |
| CN | Chức năng | CN-01 |
| QT | Quy tắc nghiệp vụ | QT-05 |
| DL | Yêu cầu dữ liệu | DL-04 |
| HL | Kiểm tra hợp lệ | HL-03 |
| CL | Yêu cầu phi chức năng (chất lượng) | CL-02 |
| KT | Điều kiện chấp nhận (kiểm thử) | KT-10 |
| FUT | Hạng mục mở rộng sau | FUT-01 |

Mọi CN, QT, DL, HL, CL trong tài liệu này có mức ưu tiên **Must** (bắt buộc). Mục 11 liệt kê các FUT, không thuộc phạm vi nghiệm thu.

## 2. Các chức năng

Hệ thống có 28 chức năng chia 4 phân hệ, mỗi phân hệ do một thành viên phụ trách (mục 9). Mọi chức năng phải đồng thời tuân thủ phân quyền (mục 3), quy tắc nghiệp vụ (mục 4) và kiểm tra hợp lệ (mục 6).

### 2.1. Phân hệ A — Truy cập và tài khoản

| ID | Chức năng | Mô tả và điều kiện chấp nhận | Vai trò |
| --- | --- | --- | --- |
| CN-01 | Đăng nhập | Nhập username và mật khẩu; username không phân biệt hoa/thường, mật khẩu so với hash (CL-03). Chỉ tài khoản ACTIVE đăng nhập được. Thành công: đổi session ID, lưu accountId, vai trò, họ tên vào session, chuyển tới trang chủ theo vai trò. Thất bại: một thông báo chung, không nói rõ sai username hay mật khẩu. | Tất cả |
| CN-02 | Đăng xuất | Hủy session hiện tại và về trang đăng nhập. Trang bảo vệ gửi header chống cache để nút Back không hiển thị lại dữ liệu. | Tất cả |
| CN-03 | Quản lý phiên | `AuthFilter` chặn mọi URL trừ trang đăng nhập và tài nguyên tĩnh. Chưa đăng nhập thì chuyển về trang đăng nhập. Mỗi request đọc lại trạng thái và vai trò tài khoản từ DB; LOCKED hoặc vai trò đã đổi thì hủy phiên. Lỗi đọc DB thì từ chối. Timeout 30 phút không hoạt động. Không lưu session vào DB. | Tất cả |
| CN-04 | Ghi nhớ tên đăng nhập | Ô "Ghi nhớ tên đăng nhập" trên trang đăng nhập, theo QT-17. Chỉ ghi nhớ username, không tự đăng nhập. | Tất cả |
| CN-05 | Danh sách tài khoản | Tìm theo username hoặc họ tên (khớp một phần, không phân biệt hoa/thường); lọc theo vai trò và trạng thái. Cột: username, họ tên, SĐT, vai trò, trạng thái, ngày tạo. Không hiển thị hash. | ADMIN |
| CN-06 | Tạo tài khoản | Nhập username, họ tên, SĐT, email (tùy chọn), vai trò, mật khẩu ban đầu và nhập lại. Trạng thái mặc định ACTIVE. | ADMIN |
| CN-07 | Cập nhật tài khoản | Sửa họ tên, SĐT, email, vai trò. Username không sửa được. Không đổi vai trò của chính mình (QT-14). | ADMIN |
| CN-08 | Khóa/mở khóa tài khoản | Chuyển ACTIVE ↔ LOCKED có hộp xác nhận. Không tự khóa (QT-14). Phiên của tài khoản bị khóa mất hiệu lực ở request kế tiếp (CN-03). Không xóa tài khoản. | ADMIN |
| CN-09 | Đổi mật khẩu cá nhân | Nhập mật khẩu hiện tại, mật khẩu mới và nhập lại. Sai mật khẩu hiện tại thì từ chối. Giữ phiên hiện tại. | Tất cả |
| CN-10 | Đặt lại mật khẩu | Đặt mật khẩu mới cho tài khoản khác (không cần mật khẩu cũ). Không áp dụng cho chính mình; không thay đổi trạng thái khóa. | ADMIN |

### 2.2. Phân hệ B — Hội viên

| ID | Chức năng | Mô tả và điều kiện chấp nhận | Vai trò |
| --- | --- | --- | --- |
| CN-11 | Danh sách và tìm kiếm hội viên | Tìm theo mã (khớp chính xác, nhận `HV000123` hoặc `123`), SĐT (khớp chính xác) hoặc họ tên (khớp một phần, không phân biệt hoa/thường). Lọc: Đang quản lý (mặc định), Đã xóa, Tất cả — hai lựa chọn sau chỉ MANAGER. Phân trang 20 dòng, mới tạo trước. Cột: mã, họ tên, SĐT, tình trạng gói (QT-07), ngày hết hạn gói hiện tại. | MANAGER, RECEPTIONIST |
| CN-12 | Tạo hội viên | Nhập họ tên, SĐT, email, ngày sinh, giới tính. Mã tự sinh; lưu created\_at, created\_by. SĐT trùng (kể cả hồ sơ đã xóa mềm) thì từ chối và hiển thị mã hồ sơ đang dùng số đó. Tạo được hồ sơ chưa mua gói. | MANAGER, RECEPTIONIST |
| CN-13 | Xem chi tiết hội viên | Hiển thị thông tin hồ sơ, nhãn đã xóa mềm (nếu có), tình trạng gói hôm nay (QT-07), cảnh báo sắp hết hạn (QT-08) và lịch sử đăng ký mọi trạng thái (mới nhất trước). Có nút chuyển sang tạo đăng ký (CN-22) nếu hồ sơ chưa xóa mềm. | MANAGER, RECEPTIONIST |
| CN-14 | Cập nhật hội viên | Sửa họ tên, SĐT, email, ngày sinh, giới tính. Mã và created\_at không sửa. Không sửa hồ sơ đã xóa mềm. | MANAGER, RECEPTIONIST |
| CN-15 | Xóa mềm hội viên | Có hộp xác nhận. Thực hiện theo QT-09; bị chặn thì nêu lý do (còn gói đã thanh toán chưa hết hạn). | MANAGER |
| CN-16 | Khôi phục hội viên | Khôi phục hồ sơ đã xóa mềm theo QT-10. | MANAGER |
| CN-17 | Danh sách sắp hết hạn | Liệt kê hội viên thỏa QT-08 với mã, họ tên, SĐT, tên gói, ngày hết hạn, số ngày còn lại; sắp xếp ngày hết hạn tăng dần. Chỉ hiển thị, không gửi thông báo. | MANAGER, RECEPTIONIST |

### 2.3. Phân hệ C — Gói tập và đăng ký

| ID | Chức năng | Mô tả và điều kiện chấp nhận | Vai trò |
| --- | --- | --- | --- |
| CN-18 | Danh sách loại gói | Cột: tên, thời hạn, giá, trạng thái bán. MANAGER xem tất cả và lọc đang bán/ngừng bán; RECEPTIONIST chỉ xem loại đang bán, không có nút thao tác. | MANAGER, RECEPTIONIST |
| CN-19 | Tạo loại gói | Nhập tên, chọn thời hạn 1/3/6/12 tháng, nhập giá. Mặc định đang bán. | MANAGER |
| CN-20 | Cập nhật loại gói | Sửa tên và giá; thời hạn chỉ đọc. Không ảnh hưởng đăng ký đã tạo (QT-03). | MANAGER |
| CN-21 | Ngừng bán/mở bán lại | Đổi trạng thái bán theo QT-04. Không xóa loại gói. | MANAGER |
| CN-22 | Tạo đăng ký | Chọn hội viên chưa xóa mềm và loại gói đang bán; tạo đăng ký PENDING kèm snapshot tên, thời hạn, giá (QT-03), created\_at, created\_by. Giá lấy từ DB, không nhận từ form. Thành công thì chuyển tới bước rà soát CN-25. | MANAGER, RECEPTIONIST |
| CN-23 | Hủy đăng ký PENDING | Nhập lý do hủy; chuyển PENDING → CANCELLED và lưu cancelled\_at, cancelled\_by, lý do. Chỉ áp dụng cho PENDING (QT-02). | MANAGER, RECEPTIONIST |
| CN-24 | Danh sách đăng ký/giao dịch | Lọc theo trạng thái, loại gói, mã hoặc SĐT hội viên, và khoảng ngày trên một trục thời gian do người dùng chọn: Ngày tạo (mọi trạng thái) hoặc Ngày thanh toán (chỉ PAID) theo QT-13. Phân trang 20 dòng. Cột: mã, hội viên, gói (snapshot), số tiền, trạng thái, phương thức, ngày tạo, ngày thanh toán, ngày bắt đầu–hết hạn. Dòng PENDING có nút Xác nhận và Hủy. | MANAGER, RECEPTIONIST |

### 2.4. Phân hệ D — Thanh toán và báo cáo

| ID | Chức năng | Mô tả và điều kiện chấp nhận | Vai trò |
| --- | --- | --- | --- |
| CN-25 | Xác nhận thanh toán | Hai bước, dùng JSP thuần. Bước 1 (rà soát): hiển thị hội viên, gói, số tiền snapshot, ngày bắt đầu và ngày hết hạn dự kiến (QT-05); người dùng chọn phương thức Tiền mặt hoặc Chuyển khoản. Bước 2 (xác nhận cuối): backend thực hiện QT-06 rồi lưu PAID, paid\_at, confirmed\_by, payment\_method, start\_date, end\_date trong một transaction. Quay lại từ bước 1 không đổi dữ liệu. Không nhận số tiền hay ngày từ client. Thành công thì hiển thị lịch gói thực tế đã lưu. | MANAGER, RECEPTIONIST |
| CN-26 | Doanh thu theo kỳ | Chọn khoảng ngày và mức gộp (ngày hoặc tháng). Bảng: kỳ, số giao dịch PAID, doanh thu; dòng tổng cuối bảng. Theo QT-12, QT-13. | MANAGER |
| CN-27 | Doanh thu theo loại gói | Cùng khoảng ngày với CN-26; nhóm theo loại gói (QT-12), hiển thị tên hiện hành, số giao dịch, doanh thu, tỷ trọng %. Tổng bằng tổng CN-26 cùng kỳ. | MANAGER |
| CN-28 | Tổng quan hội viên | Trang chủ của MANAGER. Chỉ số tại hôm nay: số hồ sơ đang quản lý (chưa xóa mềm), số hồ sơ đã xóa mềm, số hội viên đang có gói hiệu lực, số hội viên sắp hết hạn (QT-08). Chỉ số theo khoảng ngày: số hội viên mới (theo created\_at, kể cả hồ sơ sau đó bị xóa mềm). | MANAGER |

Trang chủ sau đăng nhập: ADMIN → CN-05; MANAGER → CN-28; RECEPTIONIST → CN-11.

## 3. Phân quyền

Quyền được kiểm tra ở backend cho mọi request (GET và POST), kể cả khi gọi URL trực tiếp; ẩn nút trên giao diện không thay thế kiểm tra này. **C** = được phép; **K** = bị từ chối bằng HTTP 403, dữ liệu không đổi.

| Hành động | ADMIN | MANAGER | RECEPTIONIST | Chức năng |
| --- | --- | --- | --- | --- |
| Đăng nhập, đăng xuất, đổi mật khẩu cá nhân | C | C | C | CN-01 – CN-04, CN-09 |
| Xem, tạo, sửa, khóa/mở khóa tài khoản; đặt lại mật khẩu | C | K | K | CN-05 – CN-08, CN-10 |
| Xem, tìm, tạo, sửa hội viên; danh sách sắp hết hạn | K | C | C | CN-11 – CN-14, CN-17 |
| Xem hội viên đã xóa mềm; xóa mềm; khôi phục | K | C | K | CN-11, CN-15, CN-16 |
| Xem loại gói đang bán | K | C | C | CN-18 |
| Xem loại gói ngừng bán; tạo, sửa, ngừng bán/mở bán lại | K | C | K | CN-18 – CN-21 |
| Tạo đăng ký, hủy PENDING, xem/lọc đăng ký | K | C | C | CN-22 – CN-24 |
| Xác nhận thanh toán | K | C | C | CN-25 |
| Báo cáo doanh thu, tổng quan hội viên | K | C | K | CN-26 – CN-28 |
| Sửa/hủy đăng ký PAID, xóa vật lý bất kỳ dữ liệu nào | K | K | K | QT-02, QT-16 |

Triển khai: mỗi URL thuộc một tiền tố theo nhóm quyền (`/admin/*`, `/manager/*`, `/staff/*` cho hành động chung MANAGER và RECEPTIONIST); `AuthFilter` (CN-03) so vai trò trong session với tiền tố. Vai trò cố định trong code, không có bảng quyền động.

## 4. Quy tắc nghiệp vụ

17 quy tắc dưới đây là phần chấm "Business rules/logic" (30%); mỗi quy tắc phải được kiểm tra tại tầng Service, không chỉ ở giao diện.

### 4.1. Hội viên và đăng ký

| ID | Quy tắc | Nội dung |
| --- | --- | --- |
| QT-01 | Hồ sơ độc lập với gói | Dấu xóa mềm của hội viên và tình trạng gói là hai thông tin riêng. Hồ sơ được tạo trước khi mua gói. |
| QT-02 | Vòng đời đăng ký | Chỉ có hai chuyển trạng thái: PENDING → PAID (CN-25) và PENDING → CANCELLED (CN-23, QT-09). PAID và CANCELLED là trạng thái cuối, không sửa, không hủy, không hoàn tiền. PENDING không tự hết hạn, không giữ lịch, không tính doanh thu. Mọi chuyển trạng thái dùng `UPDATE ... WHERE id = ? AND status = 'PENDING'`; 0 dòng ảnh hưởng thì báo "Đăng ký không còn chờ thanh toán". |
| QT-03 | Snapshot | Khi tạo đăng ký, lưu tên gói, số tháng và giá hiện hành vào đăng ký. Mọi màn hình, xác nhận thanh toán và báo cáo dùng snapshot; sửa loại gói về sau không làm đổi đăng ký đã tạo. |
| QT-04 | Ngừng bán | Loại gói ngừng bán không xuất hiện khi tạo đăng ký và backend từ chối nếu bị gửi trực tiếp. Đăng ký PENDING đã có của loại đó vẫn xác nhận được. |
| QT-05 | Xếp lịch gói khi PAID | Ngày do hệ thống tính, không nhận từ người dùng; công thức và ví dụ ở mục 4.2. Gói mới nối tiếp chuỗi PAID còn hạn, không chồng lấn, không khoảng trống; nếu mọi PAID đã hết hạn thì bắt đầu hôm nay. Thứ tự xác nhận PAID quyết định thứ tự lịch, không phải thứ tự tạo PENDING. |
| QT-06 | Đồng thời và nguyên tử | Tạo đăng ký (CN-22), xác nhận thanh toán (CN-25) và xóa mềm hội viên (CN-15) chạy trong một DB transaction, bắt đầu bằng `SELECT ... FROM members WITH (UPDLOCK, HOLDLOCK) WHERE id = ?`, sau đó mới kiểm tra lại trạng thái và ghi. Lỗi bất kỳ bước nào thì rollback toàn bộ; không để lại PAID thiếu lịch. |
| QT-07 | Tình trạng gói | Suy ra từ các đăng ký PAID, không lưu cột riêng. Theo thứ tự ưu tiên: **Đang hiệu lực** (có PAID với start\_date ≤ hôm nay ≤ end\_date) → **Chờ hiệu lực** (chỉ có PAID start\_date > hôm nay) → **Hết hạn** (mọi PAID có end\_date < hôm nay) → **Chưa có gói**. "Ngày hết hạn gói" hiển thị = end\_date lớn nhất trong các PAID. |
| QT-08 | Sắp hết hạn | Hội viên chưa xóa mềm, Đang hiệu lực, và end\_date lớn nhất trong các PAID cách hôm nay 0–6 ngày (còn 1–7 ngày sử dụng, tính cả hôm nay). Vì dùng end\_date lớn nhất, hội viên đã có gói nối tiếp không bị cảnh báo. PENDING không được tính là gia hạn. |
| QT-09 | Xóa mềm hội viên | Trong transaction của QT-06: nếu còn bất kỳ PAID nào có end\_date ≥ hôm nay (đang hoặc chờ hiệu lực) thì từ chối, không đổi gì. Ngược lại: đặt is\_deleted = 1, chuyển mọi PENDING của hội viên sang CANCELLED với lý do "Xóa hồ sơ hội viên"; PAID giữ nguyên. |
| QT-10 | Khôi phục hội viên | Đặt is\_deleted = 0, giữ nguyên mã, created\_at và lịch sử. Đăng ký đã CANCELLED không tự khôi phục. Khách cũ quay lại phải khôi phục hồ sơ cũ, không tạo hồ sơ mới cùng SĐT. |
| QT-11 | Hồ sơ đã xóa mềm | Không sửa hồ sơ, không tạo đăng ký, không xác nhận thanh toán cho đến khi khôi phục. Chỉ MANAGER xem được. |

### 4.2. Công thức xếp lịch (QT-05)

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
| QT-12 | Doanh thu | Doanh thu = tổng price\_snapshot của đăng ký PAID có paid\_at trong kỳ. Loại PENDING và CANCELLED. Doanh thu theo loại gói nhóm theo package\_id (không theo tên snapshot), hiển thị tên hiện hành. |
| QT-13 | Khoảng ngày lọc | "Từ ngày A đến ngày B" nghĩa là `[A 00:00, B+1 00:00)`, gồm trọn cả hai ngày. A ≤ B. Mặc định là tháng hiện tại. Trục Ngày thanh toán tự loại đăng ký chưa PAID. |
| QT-14 | Bảo vệ tài khoản | ADMIN không tự khóa, không tự đổi vai trò, không tự đặt lại mật khẩu (dùng CN-09). Nhờ vậy hệ thống luôn còn ít nhất một ADMIN ACTIVE. |
| QT-15 | Đồng hồ nghiệp vụ | Một lớp `AppClock` duy nhất trả về ngày giờ theo `Asia/Ho_Chi_Minh` cho created\_at, paid\_at, "hôm nay" và kỳ báo cáo. Không dùng `GETDATE()` của SQL Server hay múi giờ mặc định của hệ điều hành cho tính toán nghiệp vụ. |
| QT-16 | Không xóa vật lý | Không xóa vật lý tài khoản, hội viên, loại gói, đăng ký. "Xóa" chỉ là xóa mềm hội viên (CN-15); tài khoản dùng khóa, loại gói dùng ngừng bán, đăng ký dùng hủy. |
| QT-17 | Cookie ghi nhớ username | Đăng nhập thành công có chọn ô: đặt cookie `gym_username` (giá trị username, 30 ngày, HttpOnly, Path của ứng dụng). Thành công không chọn ô: xóa cookie (Max-Age = 0). Thất bại: không tạo hay làm mới cookie. Mở trang đăng nhập: điền sẵn username đã escape, chọn sẵn ô; giá trị sai định dạng HL-08 thì bỏ qua. Đăng xuất không xóa cookie. Cookie không chứa mật khẩu, vai trò hay dữ liệu xác thực. |

## 5. Yêu cầu dữ liệu

Cơ sở dữ liệu gồm 4 bảng: `accounts`, `members`, `packages`, `registrations`. Đăng ký và giao dịch thanh toán gộp chung một bảng vì mỗi lần mua có đúng một lần thanh toán. Tên cột dưới đây là đề xuất; ràng buộc (UNIQUE, FK, CHECK) là bắt buộc.

Quan hệ: `members 1–n registrations`, `packages 1–n registrations`, `accounts 1–n` (members.created\_by, registrations.created\_by / confirmed\_by / cancelled\_by).

### DL-01. accounts — tài khoản nhân viên

| Cột | Kiểu | Ràng buộc |
| --- | --- | --- |
| id | INT IDENTITY | PK |
| username | VARCHAR(50) | NOT NULL, UNIQUE (collation không phân biệt hoa/thường) |
| password\_hash | VARCHAR(100) | NOT NULL |
| full\_name | NVARCHAR(100) | NOT NULL |
| phone | CHAR(10) | NOT NULL, UNIQUE |
| email | VARCHAR(254) | NULL |
| role | VARCHAR(20) | NOT NULL, CHECK IN ('ADMIN','MANAGER','RECEPTIONIST') |
| status | VARCHAR(10) | NOT NULL, DEFAULT 'ACTIVE', CHECK IN ('ACTIVE','LOCKED') |
| created\_at | DATETIME2(0) | NOT NULL |

### DL-02. members — hồ sơ hội viên

| Cột | Kiểu | Ràng buộc |
| --- | --- | --- |
| id | INT IDENTITY | PK; hiển thị dạng mã `HV` + 6 chữ số (`HV000123`), không lưu cột mã riêng |
| full\_name | NVARCHAR(100) | NOT NULL |
| phone | CHAR(10) | NOT NULL, UNIQUE (kể cả hồ sơ đã xóa mềm) |
| email | VARCHAR(254) | NULL |
| date\_of\_birth | DATE | NULL |
| gender | NVARCHAR(10) | NULL, CHECK IN (N'Nam', N'Nữ', N'Khác') |
| is\_deleted | BIT | NOT NULL, DEFAULT 0 |
| created\_at | DATETIME2(0) | NOT NULL |
| created\_by | INT | FK → accounts.id |

### DL-03. packages — loại gói

| Cột | Kiểu | Ràng buộc |
| --- | --- | --- |
| id | INT IDENTITY | PK |
| name | NVARCHAR(100) | NOT NULL, UNIQUE |
| duration\_months | TINYINT | NOT NULL, CHECK IN (1, 3, 6, 12); không sửa sau khi tạo |
| price | INT | NOT NULL, CHECK BETWEEN 1 AND 100000000 (VND) |
| is\_active | BIT | NOT NULL, DEFAULT 1 (1 = đang bán) |
| created\_at | DATETIME2(0) | NOT NULL |

### DL-04. registrations — đăng ký gói kiêm giao dịch

| Cột | Kiểu | Ràng buộc |
| --- | --- | --- |
| id | INT IDENTITY | PK; hiển thị `DK` + 6 chữ số |
| member\_id | INT | NOT NULL, FK → members.id |
| package\_id | INT | NOT NULL, FK → packages.id |
| package\_name\_snapshot | NVARCHAR(100) | NOT NULL |
| duration\_months\_snapshot | TINYINT | NOT NULL |
| price\_snapshot | INT | NOT NULL |
| status | VARCHAR(10) | NOT NULL, CHECK IN ('PENDING','PAID','CANCELLED') |
| created\_at, created\_by | DATETIME2(0), INT | NOT NULL; FK → accounts.id |
| payment\_method | VARCHAR(10) | NULL, CHECK IN ('CASH','TRANSFER') |
| paid\_at, confirmed\_by | DATETIME2(0), INT | NULL; FK → accounts.id |
| start\_date, end\_date | DATE | NULL |
| cancelled\_at, cancelled\_by, cancel\_reason | DATETIME2(0), INT, NVARCHAR(255) | NULL; FK → accounts.id |

CHECK liên cột bắt buộc trên registrations:

- `status = 'PAID'` ⇔ payment\_method, paid\_at, confirmed\_by, start\_date, end\_date đều NOT NULL và end\_date ≥ start\_date.
- `status = 'CANCELLED'` ⇔ cancelled\_at, cancelled\_by NOT NULL.
- `status = 'PENDING'` ⇒ các cột thanh toán và hủy đều NULL.

Index đề xuất: `registrations(member_id, status)`, `registrations(paid_at)`, `registrations(created_at)`, `members(full_name)`.

### DL-05. Dữ liệu mẫu (seed)

Một script `seed.sql` chạy được nhiều lần trên DB trống, chèn dữ liệu đủ để demo mọi KT:

| Bảng | Tối thiểu | Yêu cầu nội dung |
| --- | --- | --- |
| accounts | 6 | 2 ADMIN, 2 MANAGER, 2 RECEPTIONIST; 1 tài khoản LOCKED; mật khẩu lưu dạng hash do chính ứng dụng sinh ra, ghi mật khẩu demo trong README |
| packages | 5 | Đủ bốn thời hạn 1/3/6/12 tháng; 1 loại ngừng bán |
| members | 100 | 8 hồ sơ đã xóa mềm; created\_at rải trong 6 tháng gần nhất |
| registrations | 150 | Rải trong 6 tháng; có đủ PENDING, PAID (tiền mặt và chuyển khoản), CANCELLED. Lịch PAID tính đúng QT-05, không chồng lấn. Phải có hội viên ở mỗi tình trạng: chưa có gói, đang hiệu lực, sắp hết hạn, chờ hiệu lực, hết hạn, có gói nối tiếp |

Seed được viết theo ngày tương đối (ví dụ `DATEADD(DAY, -20, CAST(SYSDATETIMEOFFSET() AT TIME ZONE 'SE Asia Standard Time' AS DATE))`) để các tình trạng vẫn đúng khi demo vào ngày khác.

## 6. Kiểm tra hợp lệ

Mọi quy tắc dưới đây bắt buộc kiểm tra ở backend cho cả form tạo và form sửa. Client có thể thêm thuộc tính HTML5 (`required`, `maxlength`, `pattern`) nhưng backend không tin client. Khi lỗi: hiển thị lại form, giữ giá trị đã nhập (trừ mật khẩu), báo lỗi cạnh từng trường, không lưu gì.

Họ tên, email, tên gói, lý do hủy được trim trước khi kiểm tra và lưu. Username và mật khẩu không trim.

| ID | Trường | Bắt buộc | Quy tắc |
| --- | --- | --- | --- |
| HL-01 | Họ tên (hội viên, nhân viên) | Có | 2–100 ký tự sau trim; không chứa chữ số |
| HL-02 | SĐT hội viên | Có | Regex `^0[0-9]{9}$`; duy nhất trong bảng members kể cả hồ sơ đã xóa mềm |
| HL-03 | SĐT nhân viên | Có | Regex `^0[0-9]{9}$`; duy nhất trong bảng accounts; được trùng SĐT hội viên |
| HL-04 | Email | Không | Nếu nhập: tối đa 254 ký tự, đúng dạng `^[^@\s]+@[^@\s]+\.[^@\s]+$`; được trùng |
| HL-05 | Ngày sinh | Không | Ngày hợp lệ, từ 01/01/1900 đến hôm nay |
| HL-06 | Giới tính | Không | Nam, Nữ hoặc Khác |
| HL-07 | Tìm kiếm | Không | Tối đa 100 ký tự; trang (page) là số nguyên ≥ 1, sai thì về trang 1 |
| HL-08 | Username | Có | Regex `^[a-zA-Z][a-zA-Z0-9_]{3,49}$` (4–50 ký tự, không dấu, không khoảng trắng); duy nhất không phân biệt hoa/thường |
| HL-09 | Mật khẩu | Có | 8–64 ký tự; giữ nguyên chuỗi nhập; ô nhập lại phải khớp chính xác |
| HL-10 | Vai trò, trạng thái tài khoản | Có | Thuộc danh sách cố định tại DL-01 |
| HL-11 | Tên gói | Có | 2–100 ký tự sau trim; không trùng sau khi gộp khoảng trắng liên tiếp và bỏ phân biệt hoa/thường |
| HL-12 | Giá gói | Có | Số nguyên 1–100.000.000 VND; chỉ chữ số, không âm |
| HL-13 | Thời hạn gói | Có (khi tạo) | 1, 3, 6 hoặc 12 tháng |
| HL-14 | Phương thức thanh toán | Có (khi xác nhận) | CASH hoặc TRANSFER |
| HL-15 | Lý do hủy đăng ký | Có | 3–255 ký tự sau trim |
| HL-16 | Khoảng ngày lọc/báo cáo | Không | Định dạng ngày hợp lệ; từ ngày ≤ đến ngày; trống thì dùng mặc định QT-13 |
| HL-17 | ID trong URL/form | Có | Số nguyên dương và bản ghi tồn tại; sai thì trả 404, không lộ lỗi hệ thống |

## 7. Yêu cầu phi chức năng

| ID | Yêu cầu | Căn cứ kiểm tra |
| --- | --- | --- |
| CL-01 | Kiến trúc MVC ba lớp | Servlet chỉ điều phối, Service chứa quy tắc QT, DAO chứa SQL. JSP không chứa SQL hay scriptlet nghiệp vụ; dùng JSTL/EL. Mỗi phân hệ có package riêng (`controller/`, `service/`, `dao/`, `model/` theo phân hệ) để thêm chức năng mà không sửa phân hệ khác. |
| CL-02 | Truy vấn an toàn | 100% câu SQL dùng PreparedStatement có tham số; không nối chuỗi giá trị người dùng vào SQL (kể cả cột sắp xếp: chỉ nhận từ danh sách cố định). |
| CL-03 | Mật khẩu băm | Lưu bằng BCrypt cost ≥ 10 hoặc PBKDF2-HMAC-SHA256 có salt riêng. Không lưu plaintext, không dùng MD5/SHA không salt. Seed cũng chỉ chứa hash. (Tránh mức phạt −50%.) |
| CL-04 | Chống XSS | Mọi dữ liệu người dùng hiển thị qua `<c:out>` hoặc `fn:escapeXml`, kể cả giá trị cookie. |
| CL-05 | Phiên và cookie | Cookie JSESSIONID HttpOnly; timeout 30 phút cấu hình trong `web.xml`. Ngoài JSESSIONID chỉ có `gym_username` (QT-17). |
| CL-06 | Transaction | Mọi thao tác ghi nhiều bảng hoặc nhiều dòng (CN-15, CN-22, CN-25) chạy trong một JDBC transaction (`setAutoCommit(false)`), rollback khi lỗi. |
| CL-07 | Chống gửi lặp | Thao tác ghi dùng POST rồi chuyển hướng (Post/Redirect/Get) kèm thông báo flash trong session; F5 không tạo bản ghi trùng. |
| CL-08 | Xử lý lỗi | Trang lỗi chung cho 403, 404, 500 khai báo trong `web.xml`; không hiển thị stack trace hay lỗi SQL cho người dùng. |
| CL-09 | Dữ liệu | Hoạt động đúng với tối thiểu 100 hội viên và 150 đăng ký từ seed (DL-05); danh sách lớn dùng phân trang phía SQL (`OFFSET ... FETCH`). Dùng SQL Server 2022 (tránh mức phạt −40%). |
| CL-10 | Cấu hình | Chuỗi kết nối DB đọc từ một file cấu hình (`db.properties`) không commit lên repository; có file mẫu `db.properties.example`. |
| CL-11 | Giao diện | Một layout chung (header, menu theo vai trò, vùng thông báo) dùng `<jsp:include>`; Bootstrap 5 qua file tĩnh trong dự án để chạy offline. |

Không đặt SLA, ngưỡng thời gian phản hồi hay số phiên đồng thời bắt buộc.

## 8. Điều kiện chấp nhận

24 tình huống kiểm thử dưới đây là tiêu chí nghiệm thu, chưa phải kết quả chạy phần mềm. Ngày trong ví dụ giả định hôm nay là 24/10/2026; khi demo dùng dữ liệu seed tương đối (DL-05). Mỗi thành viên tự kiểm các KT của phân hệ mình.

### 8.1. Phân hệ A

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-01 | Đăng nhập đúng; sai mật khẩu; username viết hoa khác; tài khoản LOCKED | Đúng và khác hoa/thường thì vào trang chủ theo vai trò, session ID đổi; sai và LOCKED bị từ chối với cùng một thông báo chung | CN-01 |
| KT-02 | Đăng xuất rồi bấm Back hoặc mở lại URL cũ; để yên 31 phút | Bị chuyển về trang đăng nhập ở cả hai trường hợp | CN-02, CN-03 |
| KT-03 | ADMIN khóa tài khoản X trong khi X đang đăng nhập ở trình duyệt khác; đổi vai trò của Y đang đăng nhập | Request kế tiếp của X và Y bị đăng xuất; X không đăng nhập lại được cho tới khi mở khóa | CN-03, CN-07, CN-08 |
| KT-04 | Đăng nhập có và không chọn ô ghi nhớ; đăng nhập sai; sửa cookie thành `<script>alert(1)</script>` | Có cookie `gym_username` 30 ngày, HttpOnly, chỉ chứa username; mở lại trang thì điền sẵn nhưng vẫn phải nhập mật khẩu; không chọn ô thì cookie bị xóa; đăng nhập sai không đổi cookie; cookie độc hại bị bỏ qua, không chạy script | CN-04, QT-17 |
| KT-05 | Mỗi vai trò gọi trực tiếp một URL GET và một POST thuộc ô K trong mục 3 | Tất cả nhận 403, dữ liệu không đổi | Mục 3 |
| KT-06 | Tạo tài khoản với username `abc`, `Khanh_20`, `khanh.nd`, `2khanh`, `KHANH_20` (đã có `Khanh_20`); SĐT 9 số, trùng tài khoản khác, trùng một hội viên | Chỉ `Khanh_20` được nhận; `KHANH_20` báo trùng; SĐT 9 số và trùng tài khoản bị từ chối; trùng hội viên được nhận; form giữ giá trị đã nhập | CN-06, HL-03, HL-08 |
| KT-07 | ADMIN thử tự khóa, tự đổi vai trò, tự đặt lại mật khẩu | Cả ba bị từ chối, kể cả khi gửi POST trực tiếp | QT-14 |
| KT-08 | Đổi mật khẩu với mật khẩu hiện tại sai; mật khẩu mới 7 và 65 ký tự; có khoảng trắng đầu/cuối; ADMIN đặt lại mật khẩu tài khoản LOCKED | Sai mật khẩu cũ và sai độ dài bị từ chối; khoảng trắng được giữ nguyên và đăng nhập lại đúng; tài khoản LOCKED vẫn LOCKED sau khi đặt lại; DB chỉ có hash | CN-09, CN-10, HL-09, CL-03 |

### 8.2. Phân hệ B

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-09 | Tìm theo `HV000005`, `5`, một phần SĐT, một phần tên viết thường; chuyển trang 1 → 5 | Mã và SĐT chỉ khớp chính xác (một phần SĐT không ra kết quả); tên khớp một phần không phân biệt hoa/thường; phân trang giữ điều kiện tìm | CN-11 |
| KT-10 | Tạo/sửa hội viên với họ tên toàn khoảng trắng, email sai, ngày sinh tương lai, SĐT trùng hồ sơ đã xóa mềm; gửi POST trực tiếp bỏ qua HTML5 | Tất cả bị từ chối ở backend, không lưu; SĐT trùng hiển thị mã hồ sơ đang giữ số | CN-12, CN-14, HL-01 – HL-06 |
| KT-11 | Hội viên có PAID đang hiệu lực (hoặc chỉ có PAID chờ hiệu lực) và 2 PENDING: MANAGER xóa mềm | Bị từ chối kèm lý do; hai PENDING không đổi | CN-15, QT-09 |
| KT-12 | Hội viên có mọi PAID đã hết hạn và 2 PENDING: MANAGER xóa mềm; RECEPTIONIST thử xóa | MANAGER thành công: is\_deleted = 1, hai PENDING thành CANCELLED trong cùng transaction, PAID không đổi; RECEPTIONIST nhận 403 | CN-15, QT-06, QT-09 |
| KT-13 | Khôi phục hội viên ở KT-12; sau đó tạo đăng ký mới; thử sửa hoặc tạo đăng ký khi hồ sơ còn xóa mềm | Khôi phục giữ mã, created\_at, lịch sử; CANCELLED giữ nguyên; sau khôi phục tạo đăng ký được; khi còn xóa mềm thì sửa và tạo đăng ký bị chặn | CN-16, QT-10, QT-11 |
| KT-14 | Ba hội viên có gói hết hạn 30/10 (còn 7 ngày), 31/10 (còn 8 ngày) và 30/10 nhưng có PAID nối tiếp | Chỉ hội viên đầu tiên có trong danh sách sắp hết hạn và có cảnh báo ở trang chi tiết | CN-13, CN-17, QT-07, QT-08 |

### 8.3. Phân hệ C

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-15 | Tạo loại gói trùng tên khác hoa/thường, giá 0, giá âm, thời hạn 2 tháng (sửa HTML); sửa thời hạn bằng POST trực tiếp | Tất cả bị từ chối; thời hạn không đổi | CN-19, CN-20, HL-11 – HL-13 |
| KT-16 | Tạo PENDING giá 500.000; đổi tên và giá loại gói thành 600.000; tạo PENDING mới; xác nhận cả hai | Đăng ký cũ giữ tên cũ và 500.000 ở mọi màn hình và báo cáo; đăng ký mới dùng 600.000 | CN-20, CN-22, QT-03 |
| KT-17 | Ngừng bán loại gói có PENDING; tạo đăng ký mới qua form và POST trực tiếp; xác nhận PENDING cũ; mở bán lại | Tạo mới bị từ chối; PENDING cũ xác nhận được; mở bán lại thì tạo được | CN-21, CN-22, QT-04 |
| KT-18 | Hủy PENDING không nhập lý do; hủy có lý do; gửi POST hủy cho đăng ký PAID và CANCELLED | Thiếu lý do bị từ chối; hủy hợp lệ lưu người, thời điểm, lý do; PAID và CANCELLED không đổi | CN-23, QT-02, HL-15 |
| KT-19 | Có PENDING tạo tháng 9 và thanh toán tháng 10; lọc tháng 10 theo Ngày tạo và theo Ngày thanh toán; lọc 01/10 – 31/10 với giao dịch lúc 31/10 23:30 và 01/11 00:00 | Theo Ngày tạo không thấy, theo Ngày thanh toán thấy; 31/10 23:30 được tính, 01/11 00:00 không | CN-24, QT-13 |

### 8.4. Phân hệ D

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-20 | Xác nhận thanh toán: dừng ở bước rà soát rồi quay lại; không chọn phương thức; sửa HTML gửi thêm số tiền và ngày bắt đầu; xác nhận hợp lệ | Quay lại không đổi dữ liệu; thiếu phương thức bị từ chối; số tiền và ngày từ client bị bỏ qua; hợp lệ thì PAID với đủ paid\_at, confirmed\_by, lịch | CN-25, QT-02 |
| KT-21 | Xếp lịch cho bốn trường hợp: chưa có PAID; mọi PAID hết hạn; có PAID đến 31/10/2026; gói 1 tháng bắt đầu 31/01/2027 rồi gói nối tiếp | Kết quả đúng từng dòng bảng ví dụ tại mục 4.2 | CN-25, QT-05 |
| KT-22 | Hai PENDING A (3 tháng, tạo trước) và B (1 tháng) của hội viên có PAID đến 31/10; xác nhận B trước A | B: 01/11 – 30/11/2026; A: 01/12/2026 – 28/02/2027; không chồng lấn | CN-25, QT-05 |
| KT-23 | Hai tab cùng bấm xác nhận một PENDING; hai tab xác nhận hai PENDING khác nhau của cùng hội viên gần như đồng thời; một tab xóa mềm hội viên trong khi tab kia xác nhận | Một PENDING chỉ thành PAID một lần, tab còn lại báo không còn chờ; hai PAID nối tiếp không chồng lấn; không có PAID mới cho hội viên đã xóa mềm, hoặc xóa mềm bị chặn vì PAID mới | CN-25, QT-06 |
| KT-24 | Trong kỳ có PAID của 3 loại gói (một loại đổi tên giữa kỳ), có PENDING và CANCELLED; 100 hồ sơ với 8 đã xóa mềm, 3 tạo trong kỳ (1 trong số đó đã xóa) | Doanh thu chỉ gồm PAID; loại đổi tên vẫn là một dòng; tổng CN-27 = tổng CN-26; tổng quan: 92 đang quản lý, 8 đã xóa, 3 hội viên mới | CN-26 – CN-28, QT-12 |

## 9. Phân công nhóm

Mỗi thành viên sở hữu trọn một phân hệ: schema các bảng của mình, seed, DAO, Service, Servlet, JSP và các KT tương ứng. Phân hệ A nhẹ nghiệp vụ nên gánh thêm phần nền chung; phân hệ D ít màn hình nhưng gánh logic khó nhất và seed phức tạp nhất.

|  | TV1 | TV2 | TV3 | TV4 |
| --- | --- | --- | --- | --- |
| Phân hệ | A — Truy cập, tài khoản | B — Hội viên | C — Gói tập, đăng ký | D — Thanh toán, báo cáo |
| Chức năng | CN-01 – CN-10 | CN-11 – CN-17 | CN-18 – CN-24 | CN-25 – CN-28 |
| Quy tắc chính | QT-14, QT-16, QT-17 | QT-01, QT-07 – QT-11 | QT-02 – QT-04, QT-13 | QT-05, QT-06, QT-12, QT-15 |
| Schema | `accounts` | `members` | `packages`, `registrations` | Index báo cáo; CHECK liên cột của `registrations` (cùng TV3) |
| Seed | 6 tài khoản (hash thật) | 100 hội viên | 5 loại gói | 150 đăng ký có lịch đúng QT-05 |
| Màn hình JSP (ước tính) | 5: đăng nhập, danh sách tài khoản, form tài khoản, đổi mật khẩu, đặt lại mật khẩu | 4: danh sách, form, chi tiết, sắp hết hạn | 4: danh sách gói, form gói, tạo đăng ký, danh sách đăng ký | 4: rà soát thanh toán, doanh thu theo kỳ, theo loại gói, tổng quan |
| Phần nền chung | Khung dự án, `DBContext`, `AuthFilter`, layout + menu theo vai trò, trang lỗi, `PasswordUtil` | Tiện ích phân trang dùng chung | Tiện ích validate dùng chung | `AppClock`, tiện ích khoảng ngày (QT-13) |
| KT phụ trách | KT-01 – KT-08 | KT-09 – KT-14 | KT-15 – KT-19 | KT-20 – KT-24 |
| Tiêu chí rubric | Login/Logout, Session, Cookies, Authorization, List, Create, Update, validation | List, Create, Update, Delete, validation, Business rules | List, Create, Update, validation, Business rules | Business rules, List (báo cáo), Update (chuyển trạng thái) |

### 9.1. Giao diện dùng chung giữa các phân hệ

Các phương thức sau phải thống nhất chữ ký trước khi code song song; phương thức ghi nhận `Connection` để chạy trong transaction của bên gọi.

| Phương thức | Chủ sở hữu | Bên dùng |
| --- | --- | --- |
| `DBContext.getConnection()` | TV1 | Tất cả |
| `SessionUser` (id, username, fullName, role) trong session | TV1 | Tất cả |
| `AppClock.now()`, `AppClock.today()` | TV4 | Tất cả |
| `MemberDAO.lockForUpdate(conn, memberId)` — SELECT với UPDLOCK, HOLDLOCK, trả về member | TV2 | TV3 (CN-22), TV4 (CN-25) |
| `MemberDAO.findById`, `MemberDAO.searchActive` | TV2 | TV3 |
| `RegistrationDAO.cancelAllPending(conn, memberId, byId, reason)` | TV3 | TV2 (CN-15) |
| `RegistrationDAO.hasUnexpiredPaid(conn, memberId, today)` | TV3 | TV2 (CN-15) |
| `RegistrationDAO.findByMember(memberId)` | TV3 | TV2 (CN-13) |
| `MembershipStatusService.statusOf(memberId)`, `.expiringSoon()` (QT-07, QT-08) | TV2 | TV4 (CN-28) |
| `PaymentService.previewSchedule(regId)`, `.confirm(regId, method, byId)` | TV4 | TV3 (nút Xác nhận ở CN-24) |

### 9.2. Thứ tự thực hiện

1. Cả nhóm: chốt SRS này, chữ ký mục 9.1 và quy ước đặt tên; mỗi người viết DDL bảng của mình, TV1 gộp thành `schema.sql` theo thứ tự accounts → members → packages → registrations.
2. TV1 đưa lên khung dự án, `DBContext`, layout và `AuthFilter`; TV4 đưa `AppClock`. Trong lúc chờ, các thành viên khác dùng tài khoản seed và viết DAO/Service.
3. Phát triển song song theo phân hệ; mỗi người làm việc trên nhánh riêng, merge khi KT của chức năng đó đạt.
4. TV4 viết seed đăng ký sau khi seed members và packages ổn định.
5. Kiểm thử chéo: mỗi người chạy KT của một phân hệ khác (TV1→B, TV2→C, TV3→D, TV4→A) và toàn bộ KT-05 (phân quyền).

Mỗi thành viên phải giải thích được toàn bộ code phân hệ của mình và luồng đăng nhập → tạo hội viên → đăng ký → thanh toán khi bảo vệ (mức phạt −90% cho code không hiểu).

## 10. Truy vết tiêu chí chấm điểm (PRJ301)

Cả 11 tiêu chí chính (100%) đều có chức năng và KT đáp ứng; các mức phạt được chặn bằng CL tương ứng. Bonus không nằm trong phạm vi (mục 11).

| Tiêu chí | Tỷ trọng | Chức năng / quy tắc đáp ứng | KT | Phụ trách |
| --- | --- | --- | --- | --- |
| Business rules/logic | 30% | QT-01 – QT-13; trọng tâm CN-15, CN-22, CN-25 | KT-11 – KT-14, KT-16 – KT-24 | TV2, TV3, TV4 |
| Authentication: Login/Logout | 5% | CN-01, CN-02 | KT-01, KT-02 | TV1 |
| Authorization: User roles | 5% | Mục 3, CN-03 | KT-03, KT-05, KT-07 | TV1 |
| List | 15% | CN-05, CN-11, CN-17, CN-18, CN-24, CN-26 – CN-28 (tìm kiếm, lọc, phân trang) | KT-09, KT-14, KT-19, KT-24 | Cả nhóm |
| Create | 10% | CN-06, CN-12, CN-19, CN-22 | KT-06, KT-10, KT-15, KT-16 | TV1, TV2, TV3 |
| Create - validation | 5% | Mục 6 (HL-01 – HL-13) | KT-06, KT-10, KT-15 | TV1, TV2, TV3 |
| Update | 10% | CN-07 – CN-10, CN-14, CN-16, CN-20, CN-21, CN-23, CN-25 | KT-08, KT-13, KT-17, KT-18, KT-20 | Cả nhóm |
| Update - validation | 5% | Mục 6 (HL-01 – HL-17) | KT-08, KT-10, KT-15, KT-18, KT-20 | Cả nhóm |
| Delete | 5% | CN-15 (xóa mềm hội viên kèm hủy PENDING) | KT-11, KT-12 | TV2 |
| Cookies | 5% | CN-04, QT-17 | KT-04 | TV1 |
| Session | 5% | CN-03, CL-05 | KT-02, KT-03 | TV1 |
| Phạt: mật khẩu không hash (−50%) | — | CL-03 | KT-08 | TV1 |
| Phạt: không dùng DB (−40%) | — | CL-09, mục 5 | Mọi KT | Cả nhóm |
| Phạt: đạo văn / không hiểu code (−90%) | — | Mục 9.2 | Bảo vệ | Cả nhóm |

## 11. Ngoài phạm vi và hướng mở rộng

### 11.1. Ngoài phạm vi

- Check-in/check-out và mọi dữ liệu lượt tập.
- Thanh toán trực tuyến, API ngân hàng, QR tự sinh, lưu ảnh minh chứng chuyển khoản.
- Sửa, hủy, hoàn tiền đăng ký PAID; đổi/nâng cấp gói; chọn ngày bắt đầu thủ công.
- Hóa đơn VAT, kế toán, sản phẩm và bán hàng; ứng dụng cho hội viên; gửi email/SMS.
- Vai trò động, phân quyền chi tiết, quên mật khẩu qua email/OTP, tự đăng nhập bằng cookie.

### 11.2. Hạng mục mở rộng sau (FUT)

Chỉ làm khi toàn bộ mục 2 đã đạt KT và nhóm thống nhất cập nhật SRS. Kiến trúc ở CL-01 và mục 9.1 cho phép thêm mà không sửa phân hệ hiện có.

| ID | Hạng mục | Loại rubric | Gắn vào phân hệ |
| --- | --- | --- | --- |
| FUT-01 | Tìm hội viên bằng AJAX, modal xác nhận, kiểm tra form bằng JavaScript | Bonus JavaScript | B, C |
| FUT-02 | Google reCAPTCHA v2 ở trang đăng nhập | Bonus reCAPTCHA | A |
| FUT-03 | Xuất `.xlsx` (hội viên, đăng ký, doanh thu), nhập hội viên từ `.xlsx` | Bonus Excel | B, D |
| FUT-04 | Biên nhận HTML in được cho đăng ký PAID | Không | D |
| FUT-05 | Nhật ký hoạt động (Audit Log) | Không | A |
| FUT-06 | Vai trò Chủ phòng gym chỉ xem báo cáo; MANAGER quản lý tài khoản lễ tân | Không | A |
| FUT-07 | Daily Pass (gói theo ngày) | Không | C, D |
| FUT-08 | Phân loại đăng ký mới / gia hạn trong báo cáo | Không | D |

### 11.3. Thay đổi so với Baseline 1.3

| Nội dung | Baseline 1.3 | Baseline 2.0 |
| --- | --- | --- |
| Vai trò | 4 (Administrator, Owner, Manager, Receptionist) | 3; Owner gộp vào MANAGER |
| Bảng dữ liệu | Accounts, Members, Packages, Memberships, Transactions, Audit Log | 4; Membership và Transaction gộp thành `registrations` |
| Thời hạn gói | 1 ngày, 1/6/12 tháng; mốc `end_exclusive` 00:00 | 1/3/6/12 tháng; ngày kiểu DATE, `end_date` tính cả ngày |
| Phiên | `security_version`, hủy phiên khác khi đổi mật khẩu, timeout 60 phút | Filter đọc trạng thái/vai trò từ DB mỗi request, timeout 30 phút |
| Tài khoản | Khóa, xóa mềm, khôi phục; Manager quản lý Receptionist | Chỉ khóa/mở khóa; chỉ ADMIN quản lý |
| Đồng hồ | `Clock` có cấu hình giờ giả | `AppClock` một múi giờ; demo bằng seed ngày tương đối |
| Báo cáo | Doanh thu, theo loại gói, hội viên, NEW/RENEWAL | Giữ ba loại đầu; bỏ NEW/RENEWAL (FUT-08) |
| Bonus và Should | AJAX, reCAPTCHA, Excel, biên nhận, Audit Log trong phạm vi | Chuyển thành FUT-01 – FUT-05 |
| Giữ nguyên |  | Snapshot giá, PENDING không tự hết hạn, PAID bất biến, xếp lịch nối tiếp theo thứ tự PAID, khóa hàng hội viên, chặn xóa mềm khi còn gói, cookie ghi nhớ username, xác nhận thanh toán hai bước |
