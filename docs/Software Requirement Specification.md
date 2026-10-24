# SRS

# HỆ THỐNG QUẢN LÝ PHÒNG GYM

Baseline 1.2 — ngày 24/10/2026.

## 1. Giới thiệu và phạm vi

---

### 1.1. Mục đích

Tài liệu xác định chức năng, quyền truy cập, dữ liệu, quy tắc nghiệp vụ và điều kiện chấp nhận của Gym Management System. Đây là căn cứ triển khai và lập kế hoạch kiểm thử cho assignment; các tiêu chí trong tài liệu không phải kết quả kiểm thử phần mềm.

Phạm vi tập trung vào lõi nghiệp vụ và các tiêu chí chấm điểm của môn học (mục 6). Các hạng mục chưa làm trong baseline này được đặc tả ở mục 5.

### 1.2. Phạm vi

Website nội bộ quản lý hội viên, gói tập, thanh toán thủ công, lượt tập và báo cáo. Không yêu cầu tích hợp Excel, Notion hoặc Zalo đang được sử dụng trong hoạt động hiện tại.

#### 1.2.1. Cách phân loại

| Nhãn | Ý nghĩa |
| --- | --- |
| Must | Bắt buộc trong phiên bản đầu; nghiệm thu bằng điều kiện tại mục 3 và 4 |
| Should (B) | Chức năng cộng điểm (Bonus) theo rubric; làm sau khi toàn bộ Must hoàn tất và chạy ổn định |
| Should | Triển khai sau các chức năng nghiệp vụ chính |
| Could | Triển khai khi còn nguồn lực |
| HOÃN (FUT) | Thuộc phạm vi dự án nhưng chưa làm trong baseline này; có hành vi thay thế đơn giản hơn; đặc tả ở mục 5 |
| OUT OF SCOPE | Ngoài phạm vi |

#### 1.2.2. Trong phạm vi

| Nhóm chức năng | Phạm vi | Ưu tiên |
| --- | --- | --- |
| Truy cập và phân quyền | Đăng nhập, đăng xuất, phiên, cookie ghi nhớ tên đăng nhập, năm vai trò cố định, khóa/mở tài khoản | Must |
| Nhân viên | Tài khoản và thông tin nhân viên cơ bản; tự đổi/đặt lại mật khẩu | Must |
| Hội viên | Tạo, tìm kiếm, cập nhật, xóa mềm, khôi phục hồ sơ; trạng thái hồ sơ độc lập tình trạng gói | Must |
| Gói tập | Quản lý loại gói (gồm ngừng bán/mở bán lại), đăng ký, gia hạn/mua tiếp, kiểm tra hiệu lực | Must |
| Thanh toán | Xác nhận thủ công tiền mặt/chuyển khoản, lưu và tra cứu giao dịch | Must |
| Lượt tập | Check-in trước 20:00, check-out, tự đóng lúc 20:00 theo cơ chế đóng lười, tra cứu lịch sử | Must |
| Báo cáo | Doanh thu (tổng và theo loại gói), số hội viên, hội viên mới, đăng ký mới và gia hạn/mua tiếp | Must |
| Kiểm tra dữ liệu | Kiểm tra bằng JavaScript phía client và bắt buộc lại ở backend | Must |
| JavaScript tương tác | Tìm hội viên bằng AJAX, hộp thoại xác nhận, chống gửi lặp | Should (B) |
| reCAPTCHA | Xác minh người dùng thật ở trang đăng nhập | Should (B) |
| Excel | Xuất `.xlsx` (hội viên, đăng ký gói, giao dịch, doanh thu) và nhập hội viên từ `.xlsx` | Should (B) |
| Biên nhận | Xem/in HTML, lưu PDF bằng chức năng Print của trình duyệt | Should |
| Nhật ký hoạt động tổng quát | Ghi nhận và tra cứu Audit Log | Should |

#### 1.2.3. Hoãn phát triển sau (FUT)

| ID | Hạng mục hoãn | Hành vi trong baseline này | Chi tiết |
| --- | --- | --- | --- |
| FUT-01 | Sửa/hủy lượt tập sai và bảng lịch sử thay đổi | Chỉ tra cứu lịch sử lượt tập; chưa có sửa/hủy | Mục 5.1 |
| FUT-02 | Tác vụ nền tự đóng 20:00 và xử lý bù khi khởi động lại | Đóng lười: tự đóng trước mọi thao tác đọc/ghi lượt tập | Mục 5.2 |
| FUT-03 | Chọn ngày bắt đầu tương lai khi xác nhận PAID | Ngày bắt đầu do hệ thống xác định, không cho chọn | Mục 5.3 |

#### 1.2.4. Ngoài phạm vi

- Sản phẩm, tồn kho, đơn bán hàng, doanh thu bán sản phẩm.
- Nhập dữ liệu Excel ngoài danh sách hội viên (nhập gói, giao dịch, lượt tập, tài khoản).
- Thanh toán trực tuyến, API ngân hàng, tự sinh QR, tự xác minh chuyển khoản.
- Lưu ảnh minh chứng chuyển khoản hoặc tệp đính kèm giao dịch.
- Hóa đơn VAT/điện tử, tính thuế, kế toán chuyên sâu, đối soát ngân hàng tự động.
- Quản lý nhân sự chuyên sâu, vai trò tùy chỉnh, cấu hình quyền động.
- Đổi/nâng cấp gói, hủy/hoàn tiền/điều chỉnh giao dịch PAID, phê duyệt nhiều cấp.
- Ứng dụng hội viên, website giới thiệu, đặt lịch PT, BMI, bài tập cá nhân hóa, thương mại điện tử.
- Chuyển số điện thoại giữa các hồ sơ hội viên; quên mật khẩu qua email/OTP hoặc khôi phục tự động.
- Tự động đăng nhập bằng cookie ("remember me" lưu thông tin xác thực).

### 1.3. Thuật ngữ và quy ước

| Thuật ngữ | Ý nghĩa |
| --- | --- |
| Hội viên / Member | Người tập có hồ sơ; không phải tài khoản đăng nhập nội bộ |
| Người dùng | Nhân sự có tài khoản truy cập hệ thống |
| Loại gói | Cấu hình tên, thời hạn, giá và trạng thái bán (đang bán/ngừng bán) của quyền sử dụng phòng gym |
| Đăng ký gói / Membership | Một lần đăng ký hoặc mua tiếp gói gắn với hội viên, thời hạn và giao dịch |
| Gia hạn/mua tiếp | Lần mua PAID sau lần PAID đầu tiên của hội viên, kể cả gói trước đã hết hạn; không có nghiệp vụ đổi/nâng cấp gói |
| Daily Pass | Gói 1 ngày: quyền tập trong một ngày, nhiều lần ra/vào |
| PAID | Nhân viên đã xác nhận nhận đủ tiền; là trạng thái giao dịch hoàn thành, không đồng nghĩa gói đã đến ngày bắt đầu |
| start_at / end_exclusive | Mốc bắt đầu / mốc kết thúc độc quyền của gói; hiệu lực theo khoảng `[start_at, end_exclusive)`; cả hai luôn là 00:00 giờ Việt Nam |
| Rolling / paid-order | Cộng lịch từ ngày bắt đầu thực tế từng gói / xếp lịch theo thứ tự xác nhận PAID |
| created_at / paid_at | Thời điểm tạo hồ sơ/bản ghi / thời điểm xác nhận đã nhận đủ tiền |
| OPEN | Lượt tập đã vào nhưng chưa kết thúc |
| Đóng lười (lazy close) | Cơ chế tự đóng lượt OPEN quá 20:00 ngay trước mọi thao tác đọc/ghi lượt tập, không dùng tác vụ nền |
| Đồng hồ nghiệp vụ | Nguồn thời gian duy nhất của ứng dụng theo giờ Việt Nam (CL-09) |
| RBAC | Phân quyền theo vai trò |
| CN / DL / QT / CL / GT / KT / FUT | Mã chức năng / dữ liệu / quy tắc / chất lượng / giao tiếp / kiểm tra / hạng mục hoãn |

Mọi ngày giờ nghiệp vụ dùng giờ Việt Nam, lấy từ đồng hồ nghiệp vụ (CL-09). Transaction là giao dịch thanh toán gắn với đúng một Membership.

---

## 2. Mô tả tổng thể

### 2.1. Tổng quan hệ thống và môi trường

| Nội dung | Yêu cầu |
| --- | --- |
| Hình thức | Website nội bộ cho quản trị viên và nhân viên |
| Hồ sơ hội viên | Hỗ trợ tối thiểu 100 hồ sơ (có thể nạp nhanh bằng CN-41) |
| Đồng thời | 10 phiên hoạt động đồng thời là quy mô dự kiến, không phải tiêu chí nghiệm thu bắt buộc |
| Số tài khoản nhân viên | Theo nhu cầu nhân sự, không quy định tối thiểu |
| Môi trường kiểm thử | Java Servlet/JSP, JDK 21, Apache Tomcat 10.1, Microsoft SQL Server 2022; Chrome và Brave bản hiện hành; một máy chạy application server và database trong môi trường local |
| Kết nối Internet | Chỉ cần khi bật reCAPTCHA (CN-40); các chức năng còn lại chạy offline |
| Sao lưu và lưu trữ | Sao lưu dữ liệu thủ công trước demo; không tự động xóa dữ liệu hoặc log |
| Kiểm soát truy cập | Kiểm tra quyền tại Servlet/backend |
| Xác thực | HttpSession phía server và cookie JSESSIONID |
| Đồng hồ nghiệp vụ | Một nguồn thời gian của ứng dụng theo `Asia/Ho_Chi_Minh`; có cấu hình giờ giả chỉ dùng cho demo/kiểm thử (CL-09) |
| Giờ hoạt động phòng gym | 07:00-20:00 hằng ngày; cấm check-in từ 20:00 và lượt OPEN được tự đóng lúc 20:00 (đóng lười, CN-15); không đặt giờ cấm đăng nhập |

### 2.2. Vai trò người dùng

Mỗi tài khoản thuộc đúng một vai trò cố định. Hội viên không có tài khoản đăng nhập nội bộ.

| Vai trò | Phạm vi chính |
| --- | --- |
| Administrator | Quản trị tài khoản, phân quyền cố định, đặt lại mật khẩu; xem Audit Log. Không có quyền nghiệp vụ hội viên/gói/thanh toán. |
| Owner | Theo dõi hoạt động, quản lý loại gói, xóa mềm/khôi phục hội viên, xem báo cáo và xuất dữ liệu. |
| Manager | Quản lý nghiệp vụ tại quầy, nhập hội viên từ Excel và quản lý tài khoản Receptionist/Accountant. |
| Receptionist | Tiếp nhận hội viên, đăng ký gói, thanh toán, lượt tập và biên nhận. |
| Accountant | Tra cứu giao dịch, doanh thu, lần mua; biên nhận và xuất dữ liệu tài chính. |

### 2.3. Ma trận phân quyền (RBAC)

C: được phép trong phạm vi ghi rõ. K: không được phép trong baseline này; backend từ chối và không thay đổi dữ liệu. Quyền Should/Could chỉ áp dụng khi chức năng được triển khai.

| Hành động | Administrator | Owner | Manager | Receptionist | Accountant | Phạm vi |
| --- | --- | --- | --- | --- | --- | --- |
| Đăng nhập/đăng xuất | C | C | C | C | C | CN-01–CN-03, CN-37, CN-40; tài khoản hợp lệ |
| Tự đổi mật khẩu | C | C | C | C | C | CN-36; nhập đúng mật khẩu hiện tại; tài khoản không khóa/xóa mềm |
| Đặt lại mật khẩu tài khoản khác | C | K | K | K | K | CN-36; mọi tài khoản trừ chính mình |
| Tạo/sửa/khóa tài khoản Receptionist, Accountant | C | K | C | K | K | Manager không đổi vai trò |
| Mở khóa Receptionist, Accountant | C | K | C | K | K | Chỉ hai vai trò này với Manager |
| Tạo/sửa/khóa/mở khóa tài khoản Administrator, Owner, Manager | C | K | K | K | K | Không tự khóa (CN-04) |
| Gán/thay đổi vai trò | C | K | K | K | K | Năm vai trò cố định; không tự đổi vai trò của mình |
| Xóa mềm tài khoản | C | K | K | K | K | CN-04; không tự xóa; giữ lịch sử |
| Xem/tìm hội viên, tình trạng gói (kể cả AJAX CN-38) | K | C | C | C | K | |
| Tạo/cập nhật hồ sơ hội viên | K | K | C | C | K | Không gồm xóa mềm/khôi phục |
| Xóa mềm hội viên | K | C | C | K | K | CN-08; tự hủy PENDING liên quan |
| Khôi phục hội viên đã xóa mềm | K | C | C | K | K | CN-42 |
| Nhập hội viên từ Excel (Should (B)) | K | K | C | K | K | CN-41; chỉ tạo hồ sơ mới |
| Tạo loại gói | K | C | K | K | K | Chỉ trong bốn thời hạn đã chốt |
| Sửa tên, giá loại gói | K | C | C | K | K | Không sửa thời hạn; snapshot cũ giữ nguyên |
| Ngừng bán/mở bán lại loại gói | K | C | K | K | K | CN-09 |
| Đăng ký/gia hạn (tạo PENDING) | K | K | C | C | K | Hội viên chưa xóa mềm; loại gói đang bán |
| Xác nhận thanh toán PAID | K | K | C | C | K | Hội viên chưa xóa mềm |
| Hủy giao dịch PENDING và đăng ký liên quan | K | K | C | C | K | Không hủy PAID |
| Xem/lọc danh sách giao dịch | K | C | C | C | C | Tra cứu toàn bộ danh sách |
| Check-in/check-out | K | K | C | C | K | Tuân thủ hiệu lực gói, giờ hoạt động và OPEN |
| Xem lịch sử lượt tập | K | C | C | C | K | Tra cứu toàn bộ lịch sử |
| Xem báo cáo doanh thu và lần mua (CN-26, CN-27, CN-29) | K | C | C | K | C | |
| Xem thống kê hội viên (CN-28) | K | C | C | K | K | |
| Xem/in biên nhận PAID (Should) | K | K | C | C | C | CN-31 |
| Xuất Excel hội viên/đăng ký (Should (B)) | K | C | C | K | K | CN-32 |
| Xuất Excel giao dịch/doanh thu (Should (B)) | K | C | C | K | C | CN-32 |
| Xem Audit Log tổng quát (Should) | C | K | K | K | K | Chỉ Administrator |
| Sửa/xóa Audit Log qua giao diện thường | K | K | K | K | K | |
| Hủy/hoàn tiền/điều chỉnh PAID | K | K | K | K | K | Ngoài phạm vi |

Hành động hoãn (chưa có chức năng, mọi vai trò bị từ chối tại backend; quyền dự kiến ghi ở mục 5):

| Hành động | Trạng thái | Tham chiếu |
| --- | --- | --- |
| Sửa/hủy lượt tập sai | HOÃN | FUT-01 |
| Chọn ngày bắt đầu tương lai khi xác nhận PAID | HOÃN | FUT-03 |

Backend kiểm tra vai trò và trạng thái tài khoản tại mọi request được bảo vệ, kể cả URL trực tiếp và endpoint AJAX. Administrator và Owner không có toàn quyền nghiệp vụ. Quyền xuất dữ liệu chỉ cấp cho vai trò đã được xem loại dữ liệu đó. Quyền quản lý tài khoản không tự bao gồm đặt lại mật khẩu. Thông tin cần đọc trong luồng đã cấp quyền chỉ phục vụ luồng đó, không cấp quyền tra cứu toàn bộ dữ liệu. Không có vai trò tùy chỉnh, cấu hình quyền động hoặc phê duyệt nhiều cấp.

### 2.4. Luồng nghiệp vụ

1. Đăng nhập (reCAPTCHA nếu bật, tùy chọn ghi nhớ tên đăng nhập). Tìm hội viên hiện có hoặc tạo hồ sơ (hoặc nhập hàng loạt từ Excel); khách cũ đã bị xóa mềm thì khôi phục hồ sơ, không tạo lại.
2. Chọn loại gói đang bán, tạo Membership và Transaction PENDING; lưu snapshot, chưa cấp lịch.
3. Nhận đủ tiền mặt hoặc kiểm tra chuyển khoản qua QR ngân hàng có sẵn.
4. Manager/Receptionist chọn phương thức thanh toán và xác nhận PAID. Hệ thống kiểm tra điều kiện, lưu paid_at, loại lần mua và xếp lịch nguyên tử theo mục 3.1.3.
5. Manager/Receptionist check-in khi đạt CN-13; check-out theo CN-15. Lượt OPEN quá 20:00 được tự đóng theo cơ chế đóng lười.
6. Owner/Manager/Receptionist tra cứu lịch sử lượt tập.
7. Người có quyền tra cứu và thống kê; in biên nhận hoặc xuất/nhập Excel nếu chức năng tương ứng được triển khai.

---

## 3. Yêu cầu cụ thể

### 3.1. Yêu cầu chức năng

Mọi điều kiện chấp nhận phải đồng thời tuân thủ ma trận tại mục 2.3, dữ liệu tại mục 3.3 và kiểm tra hợp lệ tại mục 3.4.

#### 3.1.1. Truy cập và tài khoản nhân viên

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-01 | Đăng nhập | Đăng nhập bằng tài khoản hợp lệ | Tài khoản không khóa/không bị xóa mềm đăng nhập theo vai trò; tạo phiên và thay session ID khi đăng nhập thành công; khi CN-40 bật, phải vượt xác minh reCAPTCHA trước khi kiểm tra mật khẩu | Must |
| CN-02 | Đăng xuất | Đăng xuất | Hủy phiên hiện tại; phiên đã hủy không thể truy cập chức năng bảo vệ | Must |
| CN-03 | Quản lý phiên | Quản lý phiên HttpSession/JSESSIONID | Hết hạn sau 60 phút không hoạt động; request hợp lệ tính lại thời gian không hoạt động; hết hạn phải đăng nhập lại; phiên bị vô hiệu theo bảng phiên bên dưới | Must |
| CN-04 | Quản lý tài khoản | Tạo, sửa, xóa mềm, khóa/mở tài khoản theo RBAC | Thao tác đúng phạm vi tại mục 2.3. Từ chối tự khóa, tự xóa mềm, tự đổi vai trò; nhờ đó hệ thống luôn còn ít nhất một Administrator ACTIVE (người thực hiện). Giữ liên kết lịch sử. | Must |
| CN-05 | Phân quyền | Phân quyền theo năm vai trò cố định, mỗi tài khoản một vai trò | Kiểm tra ở Servlet/backend; từ chối thao tác vượt quyền cả khi truy cập URL trực tiếp; không có giao diện tạo vai trò/quyền động | Must |
| CN-34 | Kiểm soát quyền nghiệp vụ | Kiểm soát nghiệp vụ theo ma trận RBAC | Mọi ô C tại mục 2.3 chỉ được thực hiện trong phạm vi ghi rõ; ô K bị từ chối cả qua request trực tiếp và không làm thay đổi dữ liệu | Must |
| CN-35 | Quản lý nhân viên | Quản lý nhân viên cơ bản | Người có quyền lưu và xem lại các trường DL-07 theo CN-04. Từ chối thiếu trường bắt buộc hoặc vi phạm mục 3.4. Mật khẩu không hiển thị lại; không có quản lý HR chuyên sâu. | Must |
| CN-36 | Đổi và đặt lại mật khẩu | Tự đổi và đặt lại mật khẩu theo quyền | Tuân thủ quy tắc mật khẩu bên dưới; yêu cầu sai quyền/xác thực bị từ chối. Mật khẩu cũ không đăng nhập được; mật khẩu mới dùng được nếu tài khoản hợp lệ. | Must |
| CN-37 | Cookie ghi nhớ tên đăng nhập | Trang đăng nhập có ô "Ghi nhớ tên đăng nhập"; chỉ ghi nhớ username, không ghi nhớ mật khẩu hay phiên | Theo quy tắc cookie bên dưới | Must |
| CN-40 | reCAPTCHA đăng nhập | Xác minh Google reCAPTCHA v2 ở trang đăng nhập | Theo quy tắc reCAPTCHA bên dưới | Should (B) |

Quy tắc mật khẩu (CN-36):

- Mật khẩu dài 8–64 ký tự.
- Tự đổi: mọi nhân viên đã đăng nhập; phải nhập đúng mật khẩu hiện tại, mật khẩu mới và xác nhận mật khẩu mới. Từ chối nếu tài khoản khóa/xóa mềm hoặc phiên không hợp lệ.
- Đặt lại: chỉ Administrator, cho mọi tài khoản trừ chính mình (tài khoản của mình dùng tự đổi). Các vai trò khác, kể cả Manager, bị từ chối tại backend.
- Lưu bằng BCrypt (cost tối thiểu 10) hoặc PBKDF2-HMAC-SHA256 có salt riêng cho từng mật khẩu; không lưu plaintext, không dùng hash không salt (MD5/SHA đơn thuần).
- Không có quên mật khẩu qua email/OTP/khôi phục tự động.

Quy tắc cookie ghi nhớ tên đăng nhập (CN-37):

- Đăng nhập thành công và ô được chọn: server gửi cookie `gym_username` chứa username đã mã hóa URL, thời hạn 30 ngày, Path của ứng dụng, HttpOnly, SameSite=Lax; có Secure khi dùng HTTPS.
- Mở trang đăng nhập khi có cookie: điền sẵn username và chọn sẵn ô ghi nhớ; người dùng vẫn phải nhập mật khẩu. Cookie không tự đăng nhập.
- Giá trị cookie là dữ liệu từ client: phải được escape khi đưa vào trang; giá trị không đúng quy tắc username thì bỏ qua (không điền sẵn).
- Đăng nhập thành công khi bỏ chọn ô: cookie bị xóa (Max-Age=0). Đăng nhập thất bại không tạo hoặc làm mới cookie. Đăng xuất không xóa cookie này.
- Cookie không chứa mật khẩu, session ID, vai trò hay bất kỳ dữ liệu dùng để xác thực/phân quyền; backend không dùng cookie này để quyết định quyền.

Quy tắc reCAPTCHA (CN-40):

- Trang đăng nhập hiển thị widget reCAPTCHA v2. Servlet đăng nhập gửi token `g-recaptcha-response` tới dịch vụ xác minh của Google bằng secret key; chỉ khi xác minh thành công mới kiểm tra username/mật khẩu.
- Thiếu token, token sai/hết hạn, hoặc không liên lạc được dịch vụ xác minh: từ chối đăng nhập, không tạo phiên, hiển thị thông báo chung, không tiết lộ username đúng hay sai.
- Secret key và site key đọc từ cấu hình môi trường, không đưa vào mã nguồn công khai; secret key không gửi xuống trình duyệt.
- Cờ cấu hình triển khai `recaptcha.enabled` (mặc định bật) cho phép chạy demo/kiểm thử khi không có Internet; cờ chỉ đặt ở phía server, không nhận từ request. Cờ tắt thì đăng nhập hoạt động như CN-01 (CL-06). Khi phát triển có thể dùng cặp khóa kiểm thử do Google công bố.

| Quy tắc phiên và cookie | Kết quả bắt buộc |
| --- | --- |
| Kiểm tra tài khoản | Backend kiểm tra ACTIVE/LOCKED, dấu xóa mềm và vai trò hiện hành tại mỗi request cần xác thực |
| Khóa, xóa mềm, đổi vai trò, được đặt lại mật khẩu | Mọi phiên hiện có của tài khoản đó bị hủy tại request bảo vệ tiếp theo; người dùng phải đăng nhập lại |
| Tự đổi mật khẩu | Phiên hiện tại được giữ; các phiên khác của cùng tài khoản bị hủy tại request bảo vệ tiếp theo |
| Mở khóa | Không khôi phục phiên đã bị hủy |
| Phiên hết hạn | Không cho tiếp tục thao tác yêu cầu đăng nhập |
| Dữ liệu cookie | JSESSIONID không chứa dữ liệu nghiệp vụ; cookie duy nhất khác là `gym_username` (CN-37) chỉ chứa tên đăng nhập; không cookie nào lưu mật khẩu hoặc thông tin xác thực |
| Cookie theo môi trường | HttpOnly và SameSite phù hợp; khi dùng HTTPS phải có Secure. Demo local không bắt buộc HTTPS public; cấu hình cookie phải cho phép phiên hoạt động đúng giao thức kiểm thử |

Gợi ý triển khai việc hủy phiên: lưu một số phiên bản bảo mật trên tài khoản, tăng khi khóa/xóa mềm/đổi vai trò/đổi hoặc đặt lại mật khẩu; mỗi request so với giá trị đã lưu trong session, lệch thì hủy phiên.

#### 3.1.2. Hội viên và tình trạng gói

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-06 | Tạo hội viên | Tạo hồ sơ hội viên, kể cả trước khi mua gói | Tạo được hồ sơ chưa mua gói với ID tự sinh; từ chối dữ liệu không hợp lệ hoặc điện thoại trùng theo mục 3.4 | Must |
| CN-07 | Tìm hội viên | Tìm hội viên theo mã, tên hoặc điện thoại | Mã và điện thoại khớp chính xác; tên khớp một phần, không phân biệt hoa/thường | Must |
| CN-08 | Cập nhật và xóa mềm hội viên | Cập nhật và xóa mềm hồ sơ hội viên theo quyền | Cập nhật hợp lệ được lưu theo quyền. Xóa mềm tuân thủ quy tắc bên dưới; không xóa vật lý hoặc mất lịch sử. | Must |
| CN-42 | Khôi phục hội viên | Khôi phục hồ sơ đã xóa mềm | Owner/Manager bỏ dấu xóa mềm; hồ sơ giữ nguyên ID, created_at, gói, giao dịch, lượt tập. Sau khôi phục, hội viên được đăng ký, xác nhận PAID và check-in như hồ sơ thường; gói PAID còn hiệu lực dùng tiếp được; giao dịch đã hủy khi xóa mềm vẫn CANCELLED | Must |
| CN-12 | Xem tình trạng gói | Xem riêng trạng thái hồ sơ và tình trạng từng đăng ký gói | Hiển thị đúng các tình trạng và cảnh báo tại mục 3.1.2; PAID tương lai không được hiển thị là đang có quyền tập | Must |

Quy tắc xóa mềm hội viên (CN-08):

- Chỉ Owner/Manager được xóa mềm; giữ liên kết gói, giao dịch và lượt tập.
- Trong cùng giao dịch cơ sở dữ liệu với việc xóa mềm, mọi giao dịch PENDING của hội viên chuyển CANCELLED cùng đăng ký liên quan; người thực hiện là người xóa, lý do "hủy do xóa mềm hội viên". Lỗi thì không lưu một phần. PAID cũ không bị ảnh hưởng.
- Hồ sơ đã xóa mềm không được đăng ký mới, xác nhận PAID hoặc check-in cho đến khi được khôi phục (CN-42).

| Đối tượng/trạng thái nghiệp vụ | Quy tắc |
| --- | --- |
| Hồ sơ hội viên | Phân biệt chưa xóa mềm/đã xóa mềm; độc lập với gói; được tạo trước khi mua; không phải tài khoản đăng nhập |
| Đăng ký chờ thanh toán | Giao dịch PENDING: chưa có quyền tập, chưa có lịch chính thức |
| Đăng ký đã hủy | Giao dịch PENDING bị hủy (thủ công hoặc do xóa mềm hội viên): đăng ký liên quan bị hủy, không có hiệu lực |
| Gói đã trả tiền chờ hiệu lực | PAID nhưng thời điểm hiện tại trước start_at (xảy ra khi gói nối tiếp sau gói đang hiệu lực); phải phân biệt với đang hiệu lực |
| Gói đang hiệu lực | PAID và `start_at <= hiện tại < end_exclusive`; hội viên xóa mềm vẫn bị chặn check-in |
| Gói hết hạn | PAID và hiện tại từ end_exclusive trở đi; không dùng gói này để check-in |
| Cảnh báo sắp hết hạn | Gói tháng/6 tháng/năm đang hiệu lực cảnh báo khi còn tối đa 7 ngày lịch đến end_exclusive (theo ngày Việt Nam), không cảnh báo nếu đã có gói PAID nối tiếp; không thay đổi kiểm tra hiệu lực; không phải trạng thái hồ sơ |
| Daily Pass | Chỉ hiển thị thời hạn trong ngày, không áp dụng cảnh báo trước 7 ngày |
| Nhắc hội viên | Lễ tân chủ động báo; không yêu cầu tự gửi thông báo |

ACTIVE/LOCKED chỉ là trạng thái tài khoản; OPEN/COMPLETED/AUTO_CLOSED là trạng thái lượt (CANCELLED dành sẵn cho FUT-01, chưa dùng); PENDING/PAID/CANCELLED là trạng thái thanh toán. Các tình trạng Membership trong bảng trên là nhãn nghiệp vụ suy ra từ trạng thái thanh toán và lịch, không yêu cầu trạng thái kỹ thuật riêng.

#### 3.1.3. Gói tập, đăng ký và gia hạn

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-09 | Quản lý loại gói | Quản lý loại gói: tên, giá, trạng thái bán | Owner tạo loại gói, chọn thời hạn từ bốn giá trị dưới đây. Owner/Manager sửa tên, giá hợp lệ theo mục 3.4; không sửa thời hạn. Owner ngừng bán hoặc mở bán lại. Loại gói ngừng bán không chọn được khi tạo đăng ký mới (backend từ chối cả request trực tiếp); đăng ký PENDING/PAID đã có vẫn giữ snapshot và PENDING vẫn xác nhận được. Không xóa loại gói. | Must |
| CN-10 | Tạo đăng ký gói | Tạo đăng ký và giao dịch cho hội viên chưa xóa mềm | Mỗi lần mua có một Membership và một giao dịch PENDING; Membership lưu snapshot tên gói và thời hạn, giao dịch lưu snapshot số tiền; không giữ lịch chính thức; PENDING không cấp quyền tập; từ chối hội viên xóa mềm hoặc loại gói ngừng bán | Must |
| CN-11 | Gia hạn/mua tiếp | Tạo lần mua tiếp trên hồ sơ hội viên hiện có; xếp lịch khi xác nhận PAID theo paid-order và rolling | Ngày bắt đầu theo công thức bên dưới cho mọi lần PAID. Các xác nhận của cùng hội viên, kể cả đồng thời, không tạo lịch chồng lấn hoặc khoảng trống. | Must |

| Loại gói | Thời hạn | Cách cộng từ ngày bắt đầu thực tế |
| --- | --- | --- |
| Daily Pass | 1 ngày | Ngày lịch kế tiếp |
| Monthly Pass | 1 tháng | Cộng 1 tháng theo lịch |
| 6-Month Pass | 6 tháng | Cộng 6 tháng theo lịch |
| Yearly Pass | 1 năm | Cộng 1 năm theo lịch |

Quy tắc xác định ngày bắt đầu khi xác nhận PAID (QT-18, QT-25). Ngày bắt đầu do hệ thống xác định; màn hình xác nhận không có trường nhập ngày (chọn ngày tương lai: FUT-03):

```
start_at = max(00:00 hôm nay theo giờ Việt Nam,
               end_exclusive lớn nhất trong các gói PAID của hội viên)
```

| Lịch sử PAID tại thời điểm xác nhận | Kết quả của công thức |
| --- | --- |
| Chưa từng có PAID | 00:00 hôm nay |
| Tất cả gói PAID đã hết hạn (end_exclusive lớn nhất ≤ 00:00 hôm nay) | 00:00 hôm nay; không tạo lịch trong quá khứ |
| Có gói PAID đang hiệu lực hoặc chờ hiệu lực | end_exclusive của gói kết thúc muộn nhất; không có khoảng trống |

Công thức đúng vì end_exclusive luôn là 00:00, các gói PAID không có khoảng trống và PAID không bao giờ bị hủy. paid_at là thời điểm xác nhận tiền thực tế, không đồng nhất với start_at. PENDING không giữ lịch chính thức. Thứ tự xác nhận PAID quyết định thứ tự xếp lịch, không phải thứ tự tạo PENDING. Request có kèm tham số ngày bắt đầu từ client bị backend bỏ qua. Việc đọc end_exclusive lớn nhất và ghi lịch tuân thủ QT-26.

Lưu ý Daily Pass: nếu xác nhận PAID một Daily Pass có start_at là hôm nay vào hoặc sau 20:00, hệ thống hiển thị cảnh báo trước khi xác nhận (gói không còn check-in được trong ngày). Cảnh báo không chặn xác nhận.

| Quy tắc thời hạn | Yêu cầu |
| --- | --- |
| Khoảng hiệu lực | `[start_at, end_exclusive)`; tại end_exclusive gói cũ không còn hiệu lực. Ngày cuối được tập là ngày trước end_exclusive. Giao diện có thể hiển thị ngày cuối đến 23:59:59, nhưng check-in vẫn giới hạn trước 20:00. |
| Cộng lịch | Tháng/năm tính theo lịch, không đổi thành 30/365 ngày; nếu tháng đích không có ngày tương ứng, lấy ngày cuối tháng đích. |
| Rolling | Mỗi gói cộng thời hạn từ ngày bắt đầu thực tế của chính gói đó, không neo theo ngày mua đầu tiên. |
| Không chồng lấn | Có thể lưu nhiều Membership lịch sử/tương lai; tại một thời điểm tối đa một gói có hiệu lực. Hai gói tiếp giáp tại end_exclusive không chồng lấn. |
| Quyền sử dụng | Chỉ được tập khi giao dịch đã PAID và thời điểm hiện tại nằm trong khoảng hiệu lực. |
| Phạm vi loại gói | Chỉ khác thời hạn/giá; không có quyền lợi đặc biệt, đổi/nâng cấp gói hoặc hoàn tiền. |

#### 3.1.4. Lượt tập

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-13 | Kiểm tra check-in | Kiểm tra trước check-in | Hội viên tồn tại, chưa xóa mềm; có gói PAID với start_at <= thời điểm check-in < end_exclusive; chưa có OPEN (sau bước đóng bù của CN-15); giờ Việt Nam từ 07:00 đến trước 20:00. Thiếu điều kiện nào thì từ chối. | Must |
| CN-14 | Ghi nhận check-in | Ghi nhận check-in | Lưu hội viên, gói tập, thời gian, người tạo lượt và trạng thái OPEN; mỗi hội viên tối đa một lượt OPEN, bảo đảm bằng ràng buộc duy nhất ở cơ sở dữ liệu | Must |
| CN-15 | Kết thúc lượt tập | Kết thúc lượt thủ công và tự động (đóng lười) | Check-out thủ công chỉ áp dụng cho lượt OPEN trước 20:00; thành công chuyển COMPLETED. Không có OPEN thì từ chối. Lượt OPEN quá 20:00 ngày check-in thành AUTO_CLOSED theo quy tắc bên dưới; không thay đổi hạn gói. | Must |
| CN-16 | Tra cứu lịch sử lượt tập | Tra cứu toàn bộ lịch sử lượt tập theo quyền tại mục 2.3 | Owner/Manager/Receptionist xem danh sách lượt (hội viên, gói, giờ vào/ra, trạng thái, người tạo/kết thúc), có lọc theo hội viên, ngày và trạng thái. Sửa/hủy lượt: FUT-01. | Must |

Quy tắc kết thúc lượt bằng đóng lười (CN-15):

- Hệ thống không chạy tác vụ nền. Mọi thao tác đọc hoặc ghi dữ liệu lượt tập (check-in, check-out, tra cứu lịch sử, hiển thị trạng thái lượt ở bất kỳ màn hình nào) đều gọi trước một điểm xử lý dùng chung thực hiện bước **đóng bù**.
- Bước đóng bù chuyển mọi lượt OPEN có thời điểm hiện tại từ 20:00 ngày check-in trở đi sang AUTO_CLOSED; giờ kết thúc lưu là **20:00 ngày check-in**, không dùng giờ hiện tại; người thực hiện ghi là Hệ thống.
- Bước đóng bù là một cập nhật có điều kiện trạng thái hiện hành là OPEN: không thay đổi lượt COMPLETED hoặc AUTO_CLOSED, gọi lặp không xử lý lặp, lượt OPEN chưa tới 20:00 không bị đóng sớm.
- Hệ quả: từ 20:00 không còn lượt OPEN nên check-out thủ công bị từ chối và không tạo COMPLETED; lượt OPEN của ngày trước không chặn check-in mới.
- Tác vụ nền đúng 20:00 và xử lý bù khi khởi động lại: FUT-02.

| Trạng thái lượt | Ý nghĩa |
| --- | --- |
| OPEN | Đã check-in, chưa có thời gian check-out; chặn tạo lượt mới |
| COMPLETED | Đã check-out thủ công trước 20:00 |
| AUTO_CLOSED | Hệ thống tự đóng; giờ kết thúc quy ước là 20:00 ngày check-in, không phải giờ rời phòng thực tế |
| CANCELLED | Dành sẵn cho FUT-01; chưa phát sinh trong baseline này |

Cho phép ra/vào nhiều lần trong hạn gói khi lượt trước đã kết thúc. Check-out không đổi thời hạn gói. Lượt tập không bị xóa vật lý.

#### 3.1.5. Thanh toán và giao dịch

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-21 | Quản lý thanh toán | Xác nhận đủ tiền hoặc hủy giao dịch PENDING theo quyền | Khi xác nhận, nhân viên chọn phương thức Cash hoặc Bank Transfer; không có ô nhập số tiền, nút xác nhận đồng nghĩa đã nhận đủ số tiền snapshot. Chỉ hội viên chưa xóa mềm và giao dịch còn PENDING được chuyển PAID. Lưu phương thức, paid_at, người xác nhận, loại lần mua (CN-29) và lịch theo mục 3.1.3 trong cùng thao tác nguyên tử theo QT-26; lỗi không lưu một phần. Hủy PENDING đồng thời hủy đăng ký liên quan; không hủy PAID. | Must |
| CN-22 | Cấp quyền sử dụng gói | Xác lập lịch Membership và quyền sử dụng sau thanh toán | PENDING không giữ lịch/cấp quyền. PAID dùng công thức ngày bắt đầu tại mục 3.1.3; chỉ được tập sau PAID và trong khoảng hiệu lực. Từ chối PAID cho hội viên xóa mềm. Xác nhận dựa trên tiền thực nhận, không dựa trên ảnh hay lời khai của khách. | Must |
| CN-23 | Tra cứu giao dịch | Xem giao dịch và thông tin thanh toán theo quyền | Owner/Manager/Receptionist/Accountant tra cứu toàn bộ giao dịch đã lưu và liên kết hội viên/đăng ký; tên gói hiển thị lấy từ snapshot của đăng ký liên kết; không sửa trực tiếp số tiền hoặc trạng thái của giao dịch hoàn thành | Must |
| CN-24 | Lọc giao dịch | Lọc giao dịch theo thời gian và trạng thái | Người dùng chọn trục thời gian: **Ngày tạo** (created_at, áp dụng mọi trạng thái) hoặc **Ngày thanh toán** (paid_at, chỉ PAID); kết hợp lọc trạng thái. Khoảng ngày bao gồm trọn ngày đầu/cuối. Trục đang dùng hiển thị rõ trên giao diện. | Must |
| CN-25 | Bảo toàn giao dịch | Bảo toàn giao dịch và số tiền đã lưu | Không xóa lịch sử, hủy, hoàn tiền hoặc điều chỉnh PAID. Snapshot tên gói/thời hạn lưu ở đăng ký và số tiền lưu ở giao dịch từ lúc tạo, cho cả PENDING/PAID; không tính lại từ cấu hình hiện hành khi thanh toán, xem, báo cáo, in hoặc xuất. Request xác nhận bị từ chối không đổi dữ liệu đã xác lập. | Must |

| Trạng thái thanh toán | Ý nghĩa | Ảnh hưởng |
| --- | --- | --- |
| PENDING | Chờ xác nhận thanh toán đủ | Không kích hoạt gói, không giữ lịch chính thức, chưa có phương thức, không tính doanh thu hoặc lần mua |
| PAID | Nhân viên xác nhận đã nhận đủ số tiền phải thanh toán | Lưu phương thức, paid_at, người xác nhận, loại lần mua; xếp lịch theo paid-order; quyền tập chỉ hiệu lực trong khoảng gói; tính báo cáo và cho xem/in biên nhận nếu CN-31 được triển khai |
| CANCELLED | Giao dịch đã hủy | Chỉ chuyển từ PENDING (thủ công bởi Manager/Receptionist hoặc khi xóa mềm hội viên); đăng ký liên quan bị hủy, không hiệu lực, không tính doanh thu/lần mua |

| Phương thức | Quy trình |
| --- | --- |
| Tiền mặt / Cash | Nhân viên nhận đủ tiền tại quầy rồi xác nhận, chọn Cash |
| Chuyển khoản / Bank Transfer | Cung cấp QR có sẵn, khách chuyển tiền, nhân viên kiểm tra tiền đã về tài khoản rồi xác nhận, chọn Bank Transfer |

#### 3.1.6. Báo cáo và kiểm tra dữ liệu

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-26 | Doanh thu ngày/tháng | Tổng hợp doanh thu ngày/tháng | Tổng số tiền của giao dịch PAID, phân kỳ theo paid_at; loại PENDING/CANCELLED | Must |
| CN-27 | Doanh thu theo loại gói | Doanh thu trong kỳ tách theo từng loại gói | Nhóm theo ID loại gói (không theo tên snapshot), hiển thị tên hiện hành của loại gói; tổng các nhóm bằng doanh thu CN-26 cùng kỳ | Must |
| CN-28 | Thống kê hội viên | Thống kê tổng hội viên và hội viên mới | Tổng hội viên tính tại thời điểm xem, gồm cả hồ sơ xóa mềm, hỗ trợ lọc trạng thái hồ sơ; hội viên mới theo Members.created_at trong kỳ, không phụ thuộc đã mua gói hoặc xóa mềm | Must |
| CN-29 | Thống kê lần mua | Thống kê đăng ký mới và gia hạn/mua tiếp | Loại lần mua được xác lập một lần lúc PAID: NEW nếu hội viên chưa có PAID nào trước đó, ngược lại RENEWAL; không thay đổi sau đó. Báo cáo đếm theo loại lần mua với paid_at trong kỳ; PENDING/CANCELLED không được tính | Must |
| CN-30 | Kiểm tra dữ liệu | Kiểm tra dữ liệu trước lưu | Áp dụng mục 3.4 bằng JavaScript phía client **và** bắt buộc lại ở backend; dữ liệu sai bị từ chối, không lưu. Backend không tin kiểm tra của client. | Must |

| Nội dung thống kê | Công thức hoặc quy ước |
| --- | --- |
| Khoảng ngày | Từ 00:00:00 ngày bắt đầu đến trước 00:00:00 ngày kế tiếp ngày kết thúc |
| Doanh thu | Tổng số tiền các giao dịch PAID có paid_at trong khoảng lọc |
| Doanh thu theo loại gói | Doanh thu nhóm theo ID loại gói của đăng ký liên kết |
| Hội viên mới | Số hồ sơ có created_at trong khoảng lọc |
| Đăng ký mới / gia hạn | Số giao dịch PAID có paid_at trong kỳ, đếm theo loại lần mua NEW / RENEWAL đã lưu |
| Tổng hội viên | Tại thời điểm xem, gồm cả hồ sơ xóa mềm; bộ lọc trạng thái chỉ thu hẹp kết quả |
| Danh sách giao dịch | Theo trục thời gian người dùng chọn tại CN-24 |

#### 3.1.7. Biên nhận, Excel và Audit Log

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-31 | Biên nhận thanh toán | Xem/in biên nhận thanh toán | Manager/Receptionist/Accountant được xem/in; chỉ PAID có biên nhận; mỗi giao dịch một biên nhận, in lại không tạo giao dịch mới; HTML hỗ trợ Print/lưu PDF | Should |
| CN-32 | Xuất Excel | Xuất dữ liệu ra `.xlsx` | Owner/Manager được xuất cả bốn loại; Accountant chỉ giao dịch/doanh thu; xuất bốn loại dữ liệu tại bảng dưới, `.xlsx`, theo bộ lọc; không có dữ liệu thì thông báo | Should (B) |
| CN-41 | Nhập hội viên từ Excel | Tạo hàng loạt hồ sơ hội viên từ tệp `.xlsx` | Theo quy tắc nhập bên dưới; chỉ Manager; chỉ tạo hồ sơ hội viên mới | Should (B) |
| CN-33 | Audit Log tổng quát | Audit Log tổng quát | Chỉ Administrator xem; không sửa/xóa qua giao diện quản lý thông thường; không chứa mật khẩu, session ID hay dữ liệu xác thực nhạy cảm | Should |

| Dữ liệu biên nhận CN-31 | Nội dung |
| --- | --- |
| Đơn vị và giao dịch | Tên phòng gym (đọc từ cấu hình), mã giao dịch |
| Hội viên và gói | Mã hội viên, họ tên, tên gói snapshot, thời hạn sử dụng đã lưu |
| Thanh toán | Số tiền, phương thức, thời gian thanh toán, nhân viên xác nhận |
| Loại chứng từ | Biên nhận thanh toán, không phải hóa đơn VAT/điện tử |
| Nguồn dữ liệu | Dữ liệu giao dịch đã lưu; xem/in lại không phát sinh giao dịch mới |

| Loại xuất CN-32 | Cột/nội dung cần xuất |
| --- | --- |
| Hội viên | Mã hội viên, họ tên, điện thoại, email, ngày tạo hồ sơ |
| Đăng ký gói | Mã đăng ký, mã hội viên, tên gói snapshot, ngày bắt đầu, ngày cuối được tập (ghi rõ nhãn), tình trạng đăng ký; PENDING/CANCELLED chưa có lịch thì để trống cột thời hạn |
| Giao dịch | Mã giao dịch, hội viên, tên gói snapshot, số tiền, phương thức (trống nếu chưa PAID), trạng thái, thời gian tạo, thời gian thanh toán |
| Báo cáo doanh thu | Doanh thu PAID theo paid_at trong khoảng lọc, tách theo loại gói và tổng |

| Quy tắc xuất Excel | Yêu cầu |
| --- | --- |
| Tệp | Mỗi loại một tệp `.xlsx`; hàng đầu là tiêu đề cột rõ ràng |
| Bộ lọc | Tuân theo điều kiện trên giao diện, bao gồm thời gian và trạng thái nếu có |
| Giá trị | Phản ánh dữ liệu lưu tại thời điểm xuất; tên/giá gói dùng snapshot; ngày giờ thống nhất, dễ đọc; tiền lưu dưới dạng số |
| Quyền | Chỉ ô C cho loại xuất tương ứng tại 2.3 |
| Bảo mật | Không xuất mật khẩu, thông tin phiên hoặc dữ liệu xác thực nhạy cảm; ô văn bản ghi dưới dạng chuỗi, giá trị bắt đầu bằng `=`, `+`, `-`, `@` không bị hiểu là công thức |
| Giới hạn | Tệp xuất chỉ để đọc, không dùng để cập nhật dữ liệu hệ thống; chức năng chính vẫn hoạt động nếu chưa có Excel |

Quy tắc nhập hội viên từ Excel (CN-41):

| Nội dung | Yêu cầu |
| --- | --- |
| Tệp mẫu | Hệ thống cho tải tệp mẫu `.xlsx` có hàng tiêu đề: Họ tên, Điện thoại, Email, Ngày sinh, Giới tính |
| Tệp hợp lệ | Chỉ `.xlsx` (kiểm tra định dạng thực, không chỉ phần mở rộng), tối đa 2 MB và 1.000 dòng dữ liệu; chỉ đọc sheet đầu; dòng trống bị bỏ qua; thiếu hàng tiêu đề đúng bị từ chối |
| Kiểm tra từng dòng | Mỗi dòng qua toàn bộ kiểm tra hồ sơ hội viên tại mục 3.4. Điện thoại phải nhập kiểu văn bản (ô số bị từ chối với gợi ý đổi định dạng); điện thoại không được trùng trong tệp hoặc trong hệ thống, kể cả hồ sơ xóa mềm. Ngày sinh theo `dd/MM/yyyy` hoặc kiểu ngày của Excel. Ô công thức coi là không hợp lệ |
| Kết quả | Tất cả hoặc không: chỉ khi mọi dòng hợp lệ mới tạo toàn bộ hồ sơ trong một giao dịch cơ sở dữ liệu; có lỗi thì không lưu dòng nào và hiển thị danh sách lỗi (số dòng, cột, lý do; tối đa 50 lỗi) |
| Dữ liệu tạo | ID tự sinh, created_at là thời điểm nhập; không cập nhật hay khôi phục hồ sơ có sẵn, không nhập gói/giao dịch/lượt tập |
| Bảo mật | Nội dung ô không được thực thi, được escape khi hiển thị; tệp tải lên không lưu lâu dài sau khi xử lý |
| Ghi log | Khi CN-33 được triển khai: ghi người nhập, số hồ sơ tạo, kết quả |

| Nhóm Audit Log CN-33 | Sự kiện ghi nhận khi nghiệp vụ tương ứng có triển khai |
| --- | --- |
| Tài khoản | Đăng nhập thành công/thất bại (gồm thất bại do reCAPTCHA), đăng xuất, tạo/sửa/xóa mềm/khóa/mở khóa tài khoản, thay đổi vai trò, đổi/đặt lại mật khẩu (không ghi giá trị mật khẩu) |
| Hội viên | Tạo, sửa, xóa mềm, khôi phục hồ sơ; nhập hàng loạt từ Excel |
| Gói | Tạo/sửa loại gói, ngừng bán/mở bán lại; đăng ký, gia hạn, hủy đăng ký |
| Thanh toán | Xác nhận PAID; hủy PENDING (thủ công hoặc do xóa mềm hội viên) |
| Lượt tập | Kết thúc thủ công và tự đóng (ghi là hành động hệ thống) |
| Dữ liệu log | Mã log, người thực hiện, loại hành động, đối tượng và mã đối tượng, thời gian, mô tả; trước/sau với sửa đổi quan trọng |
| Hoạt động tự động | Phân biệt hành động hệ thống với hành động nhân viên |
| Không bắt buộc | Ghi log thao tác xem danh sách, tìm kiếm, lọc |
| Quan hệ với nghiệp vụ chính | Chưa có Audit Log tổng quát không được làm ngừng nghiệp vụ chính |

#### 3.1.8. Giao diện tương tác bằng JavaScript

Kiểm tra dữ liệu bằng JavaScript phía client thuộc CN-30 (Must). Các chức năng sau là Should (B). Mọi thao tác của JavaScript chỉ hỗ trợ giao diện; backend vẫn kiểm tra quyền, trạng thái và dữ liệu như request thường.

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-38 | Tìm hội viên bằng AJAX | Ô tìm kiếm hội viên (màn hình hội viên, đăng ký gói, check-in) gợi ý kết quả khi gõ | Gửi từ khóa tối thiểu 2 ký tự, trì hoãn khoảng 300 ms sau lần gõ cuối; trả JSON tối đa 10 kết quả (mã, họ tên, điện thoại, dấu xóa mềm); khớp theo quy tắc CN-07; chỉ Owner/Manager/Receptionist (vai trò khác nhận lỗi 403 không kèm dữ liệu); hội viên xóa mềm có nhãn và không chọn được ở đăng ký/check-in; dữ liệu được escape khi hiển thị; lỗi mạng hiển thị thông báo, tìm kiếm thường (không AJAX) vẫn dùng được | Should (B) |
| CN-39 | Tương tác giao diện | Nâng trải nghiệm thao tác bằng JavaScript | (a) Hộp thoại xác nhận trước xác nhận PAID, hủy PENDING, khóa/xóa mềm tài khoản, xóa mềm/khôi phục hội viên, ngừng bán loại gói, nhập Excel; (b) hiển thị tên gói, thời hạn và giá khi chọn loại gói (chỉ tham khảo, số tiền lưu do backend quyết định, bỏ qua giá do client gửi); (c) vô hiệu nút gửi sau lần bấm đầu để tránh gửi lặp (backend vẫn chống trùng theo QT-26); (d) nút hiện/ẩn mật khẩu ở form đăng nhập và đổi mật khẩu | Should (B) |

---

### 3.2. Quy tắc nghiệp vụ

Các mã QT trỏ tới đặc tả đầy đủ ở nhóm chức năng tương ứng; không tạo bộ quy tắc thay thế.

| ID | Quy tắc | Đặc tả áp dụng |
| --- | --- | --- |
| QT-01 | Điều kiện check-in | CN-13 |
| QT-02 | Gói hết hạn/đã hủy | CN-12, CN-13 |
| QT-03 | Một lượt OPEN | CN-14 và mục 3.1.4; áp dụng cả khi hai request check-in đồng thời; bảo đảm bằng ràng buộc duy nhất ở cơ sở dữ liệu |
| QT-06 | Thanh toán và quyền tập | CN-22 và mục 3.1.3 |
| QT-07 | Khóa/xóa mềm/tự thao tác tài khoản | CN-04 và bảng phiên tại mục 3.1.1 |
| QT-08 | Kiểm soát quyền | CN-05, CN-34 và mục 2.3 |
| QT-09 | Bảo toàn PAID | CN-25 |
| QT-10 | Kiểm tra đầu vào | CN-30 và mục 3.4 |
| QT-11 | Vai trò cố định | CN-04, CN-05 |
| QT-12 | Dùng lại hồ sơ hội viên | CN-06, CN-10, CN-11, CN-42 |
| QT-13 | Kỳ báo cáo và lọc | CN-24 và công thức tại mục 3.1.6 |
| QT-14 | Sửa/hủy lượt và lịch sử | HOÃN (FUT-01) |
| QT-15 | Tự đóng lượt (đóng lười) | CN-15 và quy tắc kết thúc lượt tại mục 3.1.4. Tác vụ nền: FUT-02 |
| QT-16 | In lại biên nhận | CN-31 |
| QT-17 | Xuất/nhập dữ liệu | CN-32, CN-41 và bảng tại mục 3.1.7 |
| QT-18 | Paid-order | Công thức start_at tại mục 3.1.3; thứ tự xác nhận theo QT-26 |
| QT-19 | Rolling calendar | Bảng thời hạn tại mục 3.1.3 |
| QT-20 | Snapshot, sửa và ngừng bán loại gói | CN-09, CN-10, CN-25 |
| QT-21 | Hủy PENDING | CN-21, CN-08 và trạng thái thanh toán tại mục 3.1.5 |
| QT-22 | Xóa mềm hội viên | CN-08, CN-28 |
| QT-23 | Phân loại lần mua | CN-29 |
| QT-24 | Tình trạng và cảnh báo | Bảng tình trạng tại mục 3.1.2 |
| QT-25 | Ngày bắt đầu và thời điểm trả tiền | Công thức và phân biệt start_at/paid_at tại mục 3.1.3 (chọn ngày tương lai: FUT-03) |
| QT-26 | Tính nguyên tử và tuần tự hóa xác nhận | Điều kiện giao dịch bên dưới; áp dụng CN-21, CN-22, CN-25, CN-29 |
| QT-27 | Thông tin tài khoản | DL-07 và mục 3.4 |
| QT-28 | Đổi/đặt lại mật khẩu | CN-36 và mục 2.3 |
| QT-29 | Cookie và xác minh đăng nhập | CN-37, CN-40 và bảng phiên/cookie tại mục 3.1.1 |
| QT-30 | Khôi phục hội viên | CN-42 |

Điều kiện giao dịch của QT-26:

- Mọi xác nhận PAID của cùng một hội viên được tuần tự hóa, kể cả khi đồng thời và kể cả với các PENDING khác nhau: thao tác xác nhận khóa hồ sơ hội viên trong giao dịch cơ sở dữ liệu trước khi đọc trạng thái giao dịch và end_exclusive lớn nhất.
- Sau khi khóa, kiểm tra lại "giao dịch vẫn PENDING" và "hội viên chưa xóa mềm". Request trùng hoặc không còn PENDING bị từ chối, không đổi paid_at, tiền, lịch, doanh thu hoặc lịch sử.
- Chuyển PAID, ghi phương thức, loại lần mua và lịch phải nguyên tử: lỗi không để lại PAID thiếu lịch hoặc lịch của giao dịch còn PENDING; không ghi doanh thu/lần mua một phần.
- Thứ tự hệ thống ghi nhận các xác nhận là thứ tự xếp lịch và phân loại lần mua (CN-29).
- Gợi ý triển khai trên SQL Server: `SELECT ... FROM Members WITH (UPDLOCK, HOLDLOCK) WHERE id = ?` ở đầu giao dịch xác nhận.

### 3.3. Yêu cầu dữ liệu

Các bảng mô tả dữ liệu nghiệp vụ, không bắt buộc thiết kế bảng/cột cơ sở dữ liệu cụ thể. Không xóa vật lý Member/tài khoản/loại gói có lịch sử; giữ các liên kết để tránh phá khóa ngoại.

| Mã | Nhóm | Nội dung cần quản lý | Ràng buộc |
| --- | --- | --- | --- |
| DL-01 | Hội viên | ID, họ tên, điện thoại, email, ngày sinh, giới tính, created_at, dấu xóa mềm; các đăng ký gói liên quan | Member không phải tài khoản; trạng thái hồ sơ độc lập Membership; điện thoại duy nhất kể cả hồ sơ xóa mềm; không xóa vật lý; xóa mềm và khôi phục giữ liên kết/lịch sử |
| DL-02 | Loại gói | ID, tên gói, thời hạn, giá, trạng thái bán (đang bán/ngừng bán) | Thời hạn thuộc 1 ngày/1 tháng/6 tháng/1 năm, không sửa sau khi tạo; giá theo mục 3.4; tên duy nhất kể cả loại ngừng bán; không xóa |
| DL-03 | Đăng ký gói | ID, hội viên, loại gói, tên gói và thời hạn snapshot tại lúc tạo PENDING, thời điểm tạo, liên kết giao dịch; start_at/end_exclusive khi PAID | Một lần mua một bản ghi, liên kết đúng một giao dịch; PENDING không có lịch chính thức; PAID xếp lịch theo mục 3.1.3, rolling, không chồng lấn; start_at/end_exclusive là 00:00 giờ Việt Nam; snapshot không tính lại từ cấu hình; là nơi duy nhất lưu snapshot tên/thời hạn |
| DL-04 | Lượt tập | ID, hội viên, gói tập, người tạo lượt, thời gian vào/ra, nhân viên hoặc hệ thống kết thúc lượt, trạng thái OPEN/COMPLETED/AUTO_CLOSED | Liên kết hội viên/gói/người tạo không đổi; không xóa vật lý; tối đa một OPEN mỗi hội viên (ràng buộc duy nhất ở cơ sở dữ liệu); AUTO_CLOSED có giờ ra là 20:00 ngày check-in |
| DL-06 | Giao dịch | Mã giao dịch, hội viên, đăng ký gói, số tiền snapshot tại lúc tạo, created_at, người tạo, trạng thái PENDING/PAID/CANCELLED; khi PAID: phương thức Cash/Bank Transfer, paid_at, người xác nhận, loại lần mua NEW/RENEWAL; khi CANCELLED: người hủy, thời điểm, lý do | Là nơi duy nhất lưu snapshot số tiền; chỉ một request xác nhận thành công; chỉ PENDING được hủy; PAID không điều chỉnh/xóa; loại lần mua không đổi sau khi lưu; paid_at và created_at có ý nghĩa riêng; liên kết không mất khi xóa mềm hội viên/tài khoản |
| DL-07 | Tài khoản và nhân viên | ID, username, password hash, họ tên, điện thoại, một vai trò, ACTIVE/LOCKED, dấu xóa mềm; email tùy chọn | Username duy nhất không phân biệt hoa/thường; điện thoại theo mục 3.4, duy nhất trong tập nhân viên kể cả khóa/xóa mềm, được trùng hội viên; email không yêu cầu duy nhất; mật khẩu theo CN-36; không gộp khóa với xóa mềm; giữ liên kết giao dịch/lượt/log |
| DL-09 | Audit Log | Mã log, người/hệ thống thực hiện, hành động, đối tượng, mã đối tượng, thời gian, mô tả, trước/sau khi cần | Should; không sửa/xóa qua giao diện thường, không chứa bí mật xác thực |

### 3.4. Kiểm tra hợp lệ

Chuỗi văn bản được loại khoảng trắng đầu/cuối trước khi kiểm tra và lưu. Các quy tắc hồ sơ hội viên dùng cho cả nhập Excel CN-41.

| Trường | Bắt buộc | Cho phép trùng | Quy tắc |
| --- | --- | --- | --- |
| ID | Hệ thống sinh | Không | Không cho sửa |
| Họ tên (hội viên, nhân viên) | Có | Có | 2-100 ký tự sau khi loại khoảng trắng đầu/cuối |
| Điện thoại (hội viên, nhân viên) | Có | Không | Đúng 10 chữ số, bắt đầu bằng 0; không kiểm tra đầu số nhà mạng. Hội viên: duy nhất trong tập hội viên kể cả hồ sơ xóa mềm; trùng thì báo kiểm tra hồ sơ hiện có (hoặc khôi phục), không tạo mới. Nhân viên: duy nhất trong tập nhân viên kể cả khóa/xóa mềm; được trùng số hội viên |
| Email (hội viên, nhân viên) | Không | Có | Nếu nhập phải đúng định dạng, tối đa 254 ký tự |
| Ngày sinh | Không | Có | Từ 01/01/1900 đến ngày hiện tại |
| Giới tính | Không | Có | Nam, Nữ, Khác; có thể không cung cấp |
| Tên gói | Có | Không | Không trùng sau chuẩn hóa khoảng trắng và bỏ phân biệt hoa/thường |
| Giá gói | Có | Có | Số nguyên từ 1 đến 100.000.000 (VND) |
| Thời hạn gói | Có | Có | Chọn một trong bốn giá trị: 1 ngày, 1 tháng, 6 tháng, 1 năm; chỉ khi tạo |
| Username nhân viên | Có | Không | Duy nhất không phân biệt hoa/thường, kể cả tài khoản khóa/xóa mềm |
| Mật khẩu nhân viên | Có | Không đặt ràng buộc duy nhất | 8-64 ký tự; lưu theo CN-36 |
| Vai trò, trạng thái nhân viên | Có | Có | Một trong năm vai trò; ACTIVE/LOCKED và dấu xóa mềm độc lập |
| Phương thức thanh toán | Có khi xác nhận PAID | Có | Cash hoặc Bank Transfer |
| start_at | Hệ thống xác lập khi PAID | Có | Theo công thức tại mục 3.1.3; không nhận giá trị từ người dùng |
| end_exclusive | Tự động khi xác lập lịch PAID | Có | Lớn hơn start_at; cộng lịch từ start_at, chốt ngày cuối tháng khi cần; mốc độc quyền, không phải ngày cuối được tập |
| Trường số | Theo nghiệp vụ | Theo nghiệp vụ | Không nhận số âm hoặc ký tự không hợp lệ |
| Vị trí kiểm tra | Áp dụng toàn hệ thống | Không áp dụng | Kiểm tra bằng JavaScript phía client và bắt buộc lại ở backend, không chỉ HTML validation |

### 3.5. Yêu cầu phi chức năng

| ID | Yêu cầu | Căn cứ kiểm tra | Ưu tiên |
| --- | --- | --- | --- |
| CL-01 | Quản lý tối thiểu 100 hồ sơ hội viên | Chuẩn bị ít nhất 100 hồ sơ và kiểm tra thao tác nghiệp vụ trên dữ liệu này | Must |
| CL-02 | 10 phiên hoạt động đồng thời chỉ là quy mô dự kiến | Không đặt SLA, ngưỡng phản hồi hoặc tỷ lệ lỗi bắt buộc | Dự kiến, không bắt buộc nghiệm thu |
| CL-03 | Local một máy chạy application server và database; Java Servlet/JSP, JDK 21, Tomcat 10.1, SQL Server 2022; Chrome/Brave bản hiện hành; giờ gym 07:00–20:00 | Check-in và tự đóng theo giờ Việt Nam như CN-13/CN-15; không có cam kết uptime hoặc cấm đăng nhập ngoài giờ | Must |
| CL-04 | Tuân thủ quyền, trạng thái tài khoản và thời hạn phiên | Kiểm tra mọi quyền tại backend kể cả URL trực tiếp và endpoint AJAX; hủy phiên theo bảng phiên mục 3.1.1; hết hạn 60 phút; đăng xuất/đổi session ID; cấm tự khóa/tự xóa mềm/tự đổi vai trò theo CN-04 | Must |
| CL-05 | Bảo toàn dữ liệu giao dịch, Membership và lượt tập | Thanh toán/lịch tuân thủ QT-26; snapshot theo CN-25; không chồng lấn PAID kể cả khi xác nhận đồng thời; không có hai OPEN; đóng lười chỉ tác động OPEN, không xử lý lặp; xóa mềm/khôi phục không phá liên kết | Must |
| CL-06 | Nghiệp vụ Must hoạt động độc lập các chức năng Should/Could | Các chức năng chính vẫn hoạt động khi chưa có biên nhận, Audit Log, Excel, AJAX; đăng nhập vẫn hoạt động khi reCAPTCHA chưa triển khai hoặc `recaptcha.enabled` tắt | Must |
| CL-07 | Bảo vệ dữ liệu xác thực | Mật khẩu lưu theo CN-36; không lộ dữ liệu xác thực qua cookie, log, Excel; giá trị cookie được escape khi hiển thị; khi HTTPS phải có Secure | Must |
| CL-08 | An toàn cho chức năng bonus | Secret key reCAPTCHA không nằm trong mã gửi client; tệp nhập bị giới hạn kích thước/định dạng; công thức Excel không được thực thi cả khi nhập lẫn xuất; kết quả AJAX và nội dung nhập được escape khi hiển thị; truy vấn cơ sở dữ liệu dùng tham số hóa | Should (B) |
| CL-09 | Đồng hồ nghiệp vụ | Mọi thời điểm nghiệp vụ (created_at, paid_at, start_at, giờ check-in/out, so sánh 20:00, cảnh báo hết hạn, kỳ báo cáo) lấy từ một nguồn thời gian dùng chung của ứng dụng theo `Asia/Ho_Chi_Minh`, không dùng múi giờ mặc định của JVM/hệ điều hành hay hàm thời gian của cơ sở dữ liệu. Cấu hình triển khai `app.clock.override` cho phép đặt thời điểm giả để demo/kiểm thử: mặc định tắt, chỉ đặt phía server, không nhận từ request | Must |

Không đặt SLA, uptime hoặc thời gian phản hồi tối đa. Môi trường, sao lưu thủ công và lưu giữ dữ liệu/log theo mục 2.1.

#### 3.5.1. Giao tiếp và tích hợp

| Mã | Yêu cầu | Giới hạn |
| --- | --- | --- |
| GT-01 | Giao diện web nội bộ theo vai trò; kiểm tra quyền tại backend kể cả khi gọi URL trực tiếp | Kiểm thử trên Chrome và Brave bản hiện hành, môi trường local tại 2.1; không yêu cầu HTTPS public cho demo local |
| GT-02 | Thông báo rõ khi từ chối thao tác: hết hạn gói, hội viên xóa mềm, loại gói ngừng bán, check-in từ 20:00, check-in trùng, check-out không có lượt mở, dữ liệu không hợp lệ | Không quy định nguyên văn thông báo |
| GT-03 | Ghi nhận thanh toán thủ công bằng tiền mặt hoặc chuyển khoản | Không tích hợp ngân hàng; không lưu ảnh minh chứng |
| GT-04 | Xuất `.xlsx` theo bộ lọc hiện tại, mỗi loại dữ liệu một tệp; nhập `.xlsx` hội viên theo tệp mẫu (CN-41) | Should (B); chỉ nhập hội viên |
| GT-05 | Biên nhận HTML tối ưu in ấn; in/lưu PDF qua trình duyệt | Should; không yêu cầu thư viện sinh PDF riêng |
| GT-06 | Tích hợp dịch vụ xác minh Google reCAPTCHA v2 ở đăng nhập (CN-40) | Should (B); cần Internet khi bật; không tích hợp dịch vụ bên thứ ba nào khác |

---

## 4. Điều kiện chấp nhận

Các tình huống dưới đây là tiêu chí kiểm thử, chưa phải kết quả chạy phần mềm. Must phải đáp ứng cả điều kiện ở mục 3 và các tình huống liên quan. KT thuộc Should/Should (B)/Could chỉ áp dụng khi chức năng tương ứng được triển khai. CL-02 không là điều kiện nghiệm thu bắt buộc. Các tình huống phụ thuộc giờ được thực hiện bằng cấu hình `app.clock.override` (CL-09).

### 4.1. Truy cập, phân quyền và nhân viên

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-01 | Tài khoản khóa/xóa mềm đăng nhập mới | Bị từ chối | CN-01, CN-04 |
| KT-02 | Truy cập URL ngoài quyền; Manager thay quyền quản trị; tự nâng quyền/tự xóa | Bị từ chối ở backend | CN-04, CN-05 |
| KT-17 | Phiên không hoạt động đủ 60 phút; request hợp lệ trước hạn; đăng xuất | Hết hạn phải đăng nhập lại; request hợp lệ tính lại timeout; đăng xuất hủy phiên | CN-02, CN-03 |
| KT-18 | Tài khoản đang có phiên bị: khóa, xóa mềm, đổi vai trò, Administrator đặt lại mật khẩu; sau đó mở khóa tài khoản đã khóa | Mỗi trường hợp: request bảo vệ tiếp theo bị chặn và phiên bị hủy; mở khóa không khôi phục phiên | CN-03, CN-04, CN-36 |
| KT-19 | Đăng nhập và kiểm tra cookie trên môi trường local; kiểm tra thêm nếu dùng HTTPS | Session ID được thay; HttpOnly/SameSite phù hợp, Secure khi HTTPS; phiên hoạt động đúng giao thức local, không chứa mật khẩu | CN-01, CN-03, CL-07 |
| KT-29 | Xóa mềm tài khoản có giao dịch/lượt/log và phiên đang tồn tại | Giữ bản ghi/liên kết lịch sử, không phá khóa ngoại; không đăng nhập mới, request bảo vệ tiếp theo bị chặn/hủy phiên | CN-04, CN-25 |
| KT-37 | Với từng ô ma trận 2.3, thử qua giao diện và request trực tiếp; đặc biệt: Manager quản lý Administrator/Owner/Manager khác, đổi vai trò, reset mật khẩu, tạo loại gói, ngừng bán; Administrator xuất Excel; Manager in biên nhận; Owner/Manager khôi phục hội viên; thử hành động hoãn (sửa/hủy lượt) | Ô C được phép trong phạm vi và điều kiện nghiệp vụ; ô K bị chặn, không đổi dữ liệu; hành động hoãn bị từ chối; biên nhận/Excel/nhập Excel chỉ kiểm tra khi triển khai | CN-04, CN-05, CN-09, CN-31, CN-32, CN-34–CN-36, CN-41, CN-42 |
| KT-42 | Tạo/sửa nhân viên thiếu trường bắt buộc; họ tên toàn khoảng trắng; username An/an; điện thoại 9/11 số, có ký tự, không bắt đầu bằng 0; số trùng nhân viên đang hoạt động/khóa/xóa mềm; số chỉ trùng hội viên; email trống/trùng | Thiếu trường, họ tên rỗng sau trim, username trùng khác hoa/thường, điện thoại sai hoặc trùng trong tập nhân viên bị từ chối; số hợp lệ chỉ trùng hội viên được nhận; email tùy chọn và không yêu cầu duy nhất | CN-04, CN-35, DL-07, QT-27 |
| KT-45 | Mỗi vai trò tự đổi mật khẩu: nhập đúng/sai mật khẩu hiện tại, mật khẩu mới 7/8/64/65 ký tự; tự đổi khi có phiên thứ hai đang mở; Administrator reset tài khoản khác và thử reset chính mình; Manager/Owner/Receptionist/Accountant thử reset; tự đổi khi khóa/xóa mềm/phiên không hợp lệ | Tự đổi chỉ thành công khi đúng mật khẩu hiện tại và độ dài 8-64; phiên hiện tại giữ, phiên thứ hai bị hủy; Administrator reset được tài khoản khác, không reset chính mình; các vai trò khác bị chặn; mật khẩu lưu bằng BCrypt/PBKDF2 có salt; mật khẩu cũ không đăng nhập được, mật khẩu mới đăng nhập được nếu tài khoản hợp lệ | CN-01, CN-04, CN-34, CN-36, DL-07, QT-28 |
| KT-53 | Đăng nhập chọn "Ghi nhớ tên đăng nhập"; mở lại trang đăng nhập; đăng nhập không chọn ô; đăng nhập sai mật khẩu; kiểm tra nội dung cookie; sửa cookie thành chuỗi chứa thẻ HTML/script | Có cookie `gym_username` (30 ngày, HttpOnly, SameSite) chỉ chứa username; mở lại trang thì điền sẵn username, vẫn phải nhập mật khẩu; đăng nhập thành công không chọn ô thì cookie bị xóa; đăng nhập sai không tạo/làm mới cookie; không có mật khẩu, session ID, vai trò trong cookie; sửa cookie không thay đổi quyền; giá trị chứa HTML/script không được thực thi (bị escape hoặc bỏ qua) | CN-37, QT-29, CL-07 |
| KT-54 | Đăng nhập khi bật reCAPTCHA: vượt xác minh; không tích; token sai/dùng lại; không có Internet; gửi request đăng nhập trực tiếp không kèm token; đặt cờ `recaptcha.enabled` tắt phía server | Chỉ khi xác minh thành công mới kiểm tra mật khẩu và tạo phiên; các trường hợp còn lại bị từ chối, không tạo phiên, không tiết lộ username đúng/sai; không thể tắt xác minh bằng tham số request; secret key không xuất hiện trong trang trả về; cờ tắt thì đăng nhập như thường | CN-40, QT-29, CL-06, CL-08, Should (B) |
| KT-60 | Administrator duy nhất thử tự khóa, tự xóa mềm, tự đổi vai trò qua giao diện và request trực tiếp; có hai Administrator ACTIVE: một người khóa người kia | Tự khóa, tự xóa mềm, tự đổi vai trò bị từ chối, hệ thống vẫn còn Administrator ACTIVE; khóa Administrator khác được chấp nhận | CN-04, QT-07, CL-04 |

### 4.2. Hội viên và dữ liệu đầu vào

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-03 | Tạo, tìm theo mã/tên/điện thoại, cập nhật hồ sơ | Dữ liệu lưu đúng; mã/điện thoại chỉ khớp chính xác, nhập một phần không trả kết quả khớp; tên tìm một phần không phân biệt hoa/thường; điện thoại trùng không tạo mới | CN-06 đến CN-08 |
| KT-16 | Dữ liệu sai (họ tên toàn khoảng trắng, ngày sinh tương lai hoặc trước 1900, email sai); số đúng 10 chữ số bắt đầu 0 nhưng không thuộc đầu số nhà mạng; giới tính để trống/Nam/Nữ/Khác; tắt JavaScript hoặc gửi request trực tiếp bỏ qua kiểm tra client | Dữ liệu sai bị chặn ở client và ở backend (khi bỏ qua client vẫn bị từ chối, không lưu); số đúng cấu trúc, duy nhất không bị từ chối vì đầu số; giới tính tùy chọn đúng danh sách; email trống/trùng hợp lệ được nhận | CN-06, CN-30 |
| KT-35 | Owner/Manager xóa mềm hội viên có PAID và hai PENDING; sau đó thử tạo đăng ký, check-in, xác nhận PENDING cũ bằng Manager/Receptionist | Trong cùng thao tác xóa mềm, cả hai PENDING thành CANCELLED cùng đăng ký liên quan, ghi người xóa và lý do; PAID cũ và lịch sử giữ nguyên; đăng ký/check-in/xác nhận đều bị chặn; không phát sinh lịch/doanh thu; tổng hội viên gồm hồ sơ xóa mềm và lọc được trạng thái | CN-08, CN-10, CN-13, CN-21, CN-22, CN-28 |
| KT-38 | Có hồ sơ chưa mua gói, hồ sơ có PAID tương lai, hồ sơ hết gói; xóa mềm một hồ sơ | Tình trạng hồ sơ độc lập tình trạng Membership; created_at giữ nguyên; hội viên mới theo ngày tạo, tổng gồm hồ sơ xóa mềm | CN-06, CN-08, CN-12, CN-28 |
| KT-55 | Gõ từ khóa vào ô tìm hội viên (1 ký tự, 2 ký tự, tên một phần, mã, điện thoại); gọi trực tiếp endpoint AJAX bằng từng vai trò và khi hết phiên; chọn hội viên xóa mềm; ngắt máy chủ giữa chừng; bấm hai lần nút xác nhận PAID; thao tác có hộp thoại xác nhận chọn Hủy | 1 ký tự không gửi tìm kiếm; kết quả tối đa 10 theo quy tắc CN-07; Owner/Manager/Receptionist nhận dữ liệu, vai trò khác và phiên hết hạn nhận lỗi không kèm dữ liệu; hội viên xóa mềm có nhãn và không chọn được ở đăng ký/check-in; lỗi mạng có thông báo, tìm kiếm thường vẫn dùng được; bấm lặp chỉ tạo một lần xác nhận; chọn Hủy thì không thay đổi dữ liệu; nội dung bị escape (thử tên chứa thẻ HTML) | CN-38, CN-39, CL-06, CL-08, Should (B) |
| KT-59 | Hội viên xóa mềm có một PAID còn hiệu lực, một PAID đã hết hạn và một giao dịch đã bị hủy khi xóa mềm; Owner rồi Manager khôi phục; Receptionist/Accountant/Administrator thử khôi phục; thử tạo hồ sơ mới cùng số điện thoại trước khi khôi phục | Owner/Manager khôi phục được; hồ sơ giữ nguyên ID, created_at và toàn bộ lịch sử; check-in bằng PAID còn hiệu lực được chấp nhận nếu đủ CN-13; giao dịch đã hủy vẫn CANCELLED; đăng ký và xác nhận PAID mới hoạt động bình thường; vai trò khác bị từ chối; tạo hồ sơ trùng số bị từ chối với gợi ý khôi phục; hội viên mới trong kỳ không tăng | CN-42, CN-06, CN-13, CN-28 |

### 4.3. Gói tập, thanh toán và xếp lịch

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-04 | Tạo nhiều đăng ký PENDING, chưa xác nhận đủ tiền | Không cấp quyền tập hoặc giữ lịch chính thức; không tính doanh thu/lần mua | CN-10, CN-11, CN-22 |
| KT-12 | Xác nhận PAID cho đăng ký và gia hạn, lần lượt chọn Cash và Bank Transfer; gửi request xác nhận thiếu phương thức hoặc kèm số tiền khác snapshot | Lưu phương thức đã chọn, paid_at, người xác nhận; màn hình không có ô nhập số tiền; thiếu phương thức bị từ chối; số tiền từ client bị bỏ qua, giữ số tiền snapshot | CN-21 đến CN-23 |
| KT-13 | Thử xóa lịch sử, hủy/hoàn tiền/sửa tiền hoặc trạng thái PAID | Không cho thao tác; giữ nguyên lịch sử và số tiền | CN-23, CN-25 |
| KT-28 | Monthly/6-Month/Yearly đang hiệu lực có end_exclusive 15/11/2026 00:00; kiểm tra ngày 07/11, 08/11, 14/11, 15/11; lặp với PAID nối tiếp, Daily, PAID tương lai | Không cảnh báo ngày 07/11 (8 ngày); cảnh báo 08/11 (7 ngày) và 14/11 (1 ngày) nếu không PAID nối tiếp; có PAID nối tiếp thì không cảnh báo; Daily không cảnh báo 7 ngày; PAID tương lai khác đang hiệu lực, tại end_exclusive gói cũ hết hiệu lực | CN-12, QT-24 |
| KT-30 | Gói PAID đang hiệu lực có end_exclusive 01/11/2026 00:00; tạo A Monthly PENDING trước B Daily PENDING, xác nhận B trước A | Trước PAID cả A/B không giữ lịch; B được xếp `[01/11, 02/11)`; A được xếp `[02/11, 02/12)`; gói cũ không mất thời gian; lịch theo thứ tự PAID, không thứ tự tạo | CN-10, CN-11, CN-22 |
| KT-31 | Monthly từ 31/01/2027 00:00 rồi Monthly nối tiếp; Yearly từ 29/02/2028 | Các mốc lần lượt 28/02/2027, 28/03/2027; Yearly kết thúc độc quyền 28/02/2029; không khôi phục mốc 31 | CN-11, CN-12 |
| KT-32 | Hai PENDING khác nhau của cùng hội viên được xác nhận đồng thời trong ba trường hợp: chưa từng có PAID; mọi PAID đã hết hạn; chỉ có PAID chờ hiệu lực | Cả hai thành PAID; lịch nối tiếp, không chồng lấn, không khoảng trống, giữ rolling: giao dịch được ghi nhận trước dùng start_at theo công thức, giao dịch sau nối vào end_exclusive của giao dịch trước; loại lần mua theo cùng thứ tự (trường hợp đầu: một NEW, một RENEWAL); doanh thu ghi đúng một lần mỗi giao dịch | CN-11, CN-21, CN-22, CN-29, QT-26, CL-05 |
| KT-33 | Tạo giao dịch PENDING giá 500.000, đổi giá loại gói thành 600.000, tạo giao dịch mới rồi thanh toán cả hai | Giao dịch cũ vẫn 500.000, mới 600.000; xem/biên nhận khi có/báo cáo không tính lại tiền cũ | CN-09, CN-21, CN-25 |
| KT-34 | Manager và Receptionist lần lượt hủy PENDING; thử hủy PAID và thử hủy bằng Administrator/Owner/Accountant | Hai vai trò được phép hủy đồng thời giao dịch PENDING và đăng ký liên quan, không lịch/hiệu lực/doanh thu; mọi thao tác hủy PAID hoặc từ vai trò không được cấp bị từ chối | CN-21, CN-22, CN-34 |
| KT-39 | Monthly `[15/10/2026 00:00, 15/11/2026 00:00)`; Daily 15/10; 6-Month/Yearly cùng ngày bắt đầu | Ngày cuối lần lượt 14/11, 15/10, 14/04/2027, 14/10/2027; tại end_exclusive gói cũ không cấp quyền; check-in vẫn chịu giờ 20:00 | CN-11, CN-12, CN-13 |
| KT-40 | Chưa từng có PAID; tạo PENDING trước ngày thanh toán; xác nhận lúc 10:30 ngày 15/10/2026; gửi kèm tham số ngày bắt đầu 17/10 hoặc 14/10; xác nhận một Daily Pass lúc 20:30 | PENDING không giữ lịch; màn hình xác nhận không có ô chọn ngày; start_at 15/10 00:00 bất kể tham số client, paid_at 15/10 10:30; giao dịch còn PENDING không có lịch/doanh thu; gói chỉ được check-in sau PAID và thỏa CN-13; Daily Pass lúc 20:30 có cảnh báo nhưng vẫn xác nhận được | CN-11, CN-22, QT-25 |
| KT-41 | Hội viên có gói PAID đang hiệu lực hoặc chỉ đang chờ hiệu lực, end_exclusive lớn nhất 01/11/2026 00:00; xác nhận gói mới; gửi kèm tham số ngày 05/11 | Lịch bắt đầu 01/11 00:00, tham số ngày bị bỏ qua, không tạo khoảng trống; end_exclusive mới cộng rolling; thứ tự theo PAID không theo tạo PENDING | CN-11, CN-22, QT-18, QT-19, QT-25 |
| KT-43 | Tạo một PENDING và một PAID loại gói tên A/giá 500.000; Owner/Manager đổi tên B/giá 600.000; tạo đăng ký mới, thanh toán PENDING cũ; thử sửa thời hạn; Owner ngừng bán loại gói rồi thử tạo đăng ký mới qua giao diện và request trực tiếp, xác nhận một PENDING có sẵn của loại đó; Manager thử ngừng bán; Owner mở bán lại | PENDING/PAID cũ giữ tên A, giá 500.000 và thời hạn đã lưu; thanh toán không tính lại từ cấu hình; đăng ký mới dùng B/600.000; không sửa được thời hạn; loại ngừng bán không tạo được đăng ký mới nhưng PENDING có sẵn vẫn xác nhận được; Manager bị chặn ngừng bán; mở bán lại thì tạo đăng ký được; biên nhận/Excel khi triển khai dùng snapshot | CN-09, CN-10, CN-25, CN-31, CN-32, QT-20 |
| KT-44 | Hai request cùng xác nhận một PENDING; xác nhận lại PAID hoặc CANCELLED; gián đoạn/lỗi trong thao tác xác nhận và cập nhật lịch | Đúng một request chuyển PAID thành công; request trùng/không còn PENDING bị từ chối, không đổi paid_at/số tiền/lịch/doanh thu/lịch sử; lỗi không để lại PAID thiếu lịch hoặc lịch của giao dịch còn PENDING, không ghi doanh thu/lần mua một phần | CN-21, CN-22, CN-25, CL-05, QT-26 |
| KT-48 | Tất cả PAID đã hết hạn (kể cả đúng end_exclusive cuối cùng); xác nhận ngày 15/10/2026; gửi kèm tham số ngày quá khứ hoặc tương lai | start_at 15/10 00:00 bất kể tham số client; không nối vào cuối chuỗi cũ trong quá khứ; paid_at là lúc xác nhận; loại lần mua là RENEWAL | CN-11, CN-21, CN-22, CN-29, QT-18, QT-25 |

### 4.4. Lượt tập

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-05 | Check-in không có hồ sơ, hồ sơ xóa mềm, PENDING, PAID tương lai hoặc hết hạn; check-in trước 07:00 | Từ chối từng trường hợp, không tạo lượt | CN-08, CN-12, CN-13 |
| KT-06 | Gói đã hết hạn trước thời điểm check-in | Từ chối, thông báo cần gia hạn | CN-12, CN-13 |
| KT-07 | Manager/Receptionist check-in hợp lệ, rồi check-out trước 20:00; Owner/Manager/Receptionist tra cứu và lọc toàn bộ lịch sử | Lưu thời gian/người thực hiện và gói gắn với lượt; chuyển OPEN sang COMPLETED, xem được lịch sử; Administrator/Accountant không xem được lịch sử | CN-14 đến CN-16 |
| KT-08 | Check-in khi đang có OPEN, kể cả hai yêu cầu đồng thời | Không tạo thêm lượt; tối đa một OPEN | CN-13, CN-14, QT-03 |
| KT-20 | Check-out khi không có OPEN | Từ chối, thông báo phù hợp | CN-15 |
| KT-21 | Check-in lúc 19:59:59, 20:00 và sau 20:00; check-out OPEN trước và từ 20:00 | Check-in đủ điều kiện và check-out trước 20:00 được chấp nhận. Từ 20:00 chặn check-in; check-out gặp lượt đã AUTO_CLOSED (kết thúc 20:00 ngày check-in) nên bị từ chối, không đổi hạn gói. Không tạo COMPLETED từ 20:00, không đóng lặp hoặc ghi đè lượt đã kết thúc | CN-13, CN-15 |
| KT-23 | Hội viên ra/vào nhiều lần trong hạn, đã kết thúc lượt trước | Chấp nhận lượt mới; kiểm tra cả gói 1 ngày, 1 tháng, 6 tháng và 1 năm | CN-13 đến CN-15 |
| KT-58 | OPEN ngày 15/10/2026 không check-out; vào 21:00 cùng ngày hoặc ngày 16/10 lần lượt: mở lịch sử lượt tập, mở màn hình hội viên, check-out, check-in lại của chính hội viên đó; có thêm lượt COMPLETED, AUTO_CLOSED và OPEN của hôm nay chưa tới 20:00; gọi lại nhiều lần | Thao tác đầu tiên đã đóng bù: OPEN quá hạn thành AUTO_CLOSED, kết thúc 15/10/2026 20:00, người thực hiện là Hệ thống, không đổi hạn gói; màn hình nào cũng không còn hiển thị OPEN quá hạn; check-in lại được chấp nhận nếu đủ điều kiện; check-out lúc này bị từ chối; COMPLETED/AUTO_CLOSED giữ nguyên, OPEN chưa tới 20:00 không bị đóng sớm; gọi lặp không xử lý lặp | CN-15, CN-16, QT-15, CL-05 |

### 4.5. Báo cáo, biên nhận và xuất/nhập dữ liệu

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-14 | Tạo giao dịch ở kỳ A, thanh toán kỳ B; lọc ngày đầu/cuối; có PAID của ba loại gói, một loại bị đổi tên giữa kỳ | Chỉ tính doanh thu kỳ B; gồm trọn ngày cuối, loại thời điểm bắt đầu ngày kế tiếp; loại PENDING/CANCELLED; doanh thu theo loại gói nhóm theo ID (loại đổi tên vẫn một dòng) và tổng các dòng bằng doanh thu kỳ | CN-24, CN-26, CN-27 |
| KT-15 | Một hồ sơ mới, ba lần mua PAID; thêm giao dịch PENDING/CANCELLED | Hội viên mới 1, đăng ký mới 1, gia hạn 2; doanh thu chỉ tổng ba PAID | CN-28, CN-29 |
| KT-24 | Manager/Receptionist/Accountant xem/in lại PAID; thử PENDING; Administrator/Owner thử xem | Ba vai trò được phép có biên nhận đúng snapshot tên/giá, phương thức và lịch đã lưu; in lại không tạo giao dịch; PENDING và vai trò không có quyền bị từ chối | CN-31, Should |
| KT-25 | Owner/Manager xuất từng loại Excel; Accountant thử cả bốn; Administrator và Receptionist thử xuất; dữ liệu rỗng; ô văn bản bắt đầu bằng `=` | Owner/Manager xuất cả bốn loại; Accountant chỉ giao dịch/doanh thu; Administrator và Receptionist bị chặn; đúng bộ lọc/cột và snapshot tên/giá, tiền dạng số; rỗng thì thông báo, không xuất bí mật; giá trị bắt đầu bằng `=` hiển thị như văn bản | CN-32, CL-08, Should (B) |
| KT-36 | Có PENDING/CANCELLED trước lần PAID đầu tiên, lần PAID thứ hai ở kỳ sau; lọc giao dịch theo trục Ngày tạo và Ngày thanh toán; Accountant xem báo cáo lần mua và thử xem thống kê hội viên | PAID đầu là đăng ký mới, PAID sau là gia hạn kể cả lọc riêng kỳ sau; trục Ngày tạo lọc mọi trạng thái theo created_at; trục Ngày thanh toán chỉ có PAID theo paid_at; giao diện ghi rõ trục đang dùng; Accountant xem được lần mua, bị chặn thống kê hội viên | CN-24, CN-26, CN-28, CN-29 |
| KT-56 | Manager nhập tệp mẫu 100 hồ sơ hợp lệ; nhập tệp có một dòng lỗi (thiếu họ tên, điện thoại 9 số, ô điện thoại kiểu số, trùng trong tệp, trùng hệ thống kể cả hồ sơ xóa mềm, ngày sinh tương lai, giới tính sai); tệp `.xls`/`.xlsm`/đổi đuôi từ tệp khác/quá 2 MB/quá 1.000 dòng/thiếu tiêu đề/ô công thức; Receptionist, Owner, Accountant, Administrator thử nhập | Tệp hợp lệ tạo đủ 100 hồ sơ với created_at là thời điểm nhập (hỗ trợ CL-01); tệp có lỗi không lưu dòng nào và báo đúng số dòng/cột/lý do (tối đa 50); các tệp sai định dạng/giới hạn bị từ chối; chỉ Manager nhập được; không tạo/sửa gói, giao dịch, lượt tập hay cập nhật/khôi phục hồ sơ có sẵn; nội dung ô độc hại được escape khi hiển thị | CN-41, CN-30, CL-01, CL-08, Should (B) |

### 4.6. Nhật ký và môi trường

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-26 | Thao tác quan trọng (gồm đăng nhập thất bại, đặt lại mật khẩu, xóa mềm hội viên kèm hủy PENDING, khôi phục hội viên, ngừng bán loại gói, nhập Excel, tự đóng lượt) và thử truy cập/sửa/xóa Audit Log | Log đủ dữ liệu, phân biệt hệ thống/nhân viên; chỉ Administrator xem; không sửa/xóa qua giao diện thường; không ghi bí mật | CN-33, Should |
| KT-27 | Quản lý ít nhất 100 hồ sơ trên local JDK 21/Tomcat 10.1/SQL Server 2022, Chrome và Brave hiện hành | Kiểm tra nghiệp vụ trên tối thiểu 100 hồ sơ; không yêu cầu chạy/đạt 10 phiên đồng thời để nghiệm thu | CL-01, CL-02 |
| KT-47 | Kiểm tra cấu hình môi trường và thao tác sao lưu trước demo, chính sách lưu dữ liệu/log; kiểm tra khóa reCAPTCHA và kết nối Internet nếu bật CN-40; đặt `app.clock.override` thành 15/10/2026 19:59:59 rồi 20:00:00, sau đó tắt; đổi múi giờ hệ điều hành của máy chủ; gửi request kèm tham số thời gian | JDK 21/Tomcat 10.1/SQL Server 2022 cùng máy local; ghi phiên bản Chrome/Brave thực tế; có bản sao lưu thủ công trước demo; không tự động xóa dữ liệu/log; có phương án demo khi không có Internet; mọi thời điểm nghiệp vụ theo giờ giả khi bật và theo giờ thực khi tắt; đổi múi giờ hệ điều hành không làm lệch giờ nghiệp vụ; không thể đặt giờ qua request | GT-01, GT-06, CL-02, CL-03, CL-09 |

---

## 5. Phạm vi hoãn

Các hạng mục dưới đây thuộc phạm vi dự án nhưng chưa là yêu cầu nghiệm thu của baseline này. Khi triển khai, cập nhật lại các mục liên quan ở mục 2.3, 3.1, 3.2, 3.3 và 4.

### 5.1. FUT-01: Sửa/hủy lượt tập và lịch sử thay đổi

| Nội dung | Mô tả |
| --- | --- |
| Hành vi hiện tại | Lượt tập chỉ được tạo bởi check-in, kết thúc bởi check-out hoặc đóng lười; không sửa/hủy; CN-16 chỉ tra cứu |
| Quyền dự kiến | Owner, Manager, Receptionist; Administrator/Accountant bị từ chối |
| Quy tắc sửa | Chỉ sửa giờ trong ngày ghi nhận ban đầu; check-in từ 07:00 đến trước 20:00, trong hạn gói đã gắn và không trước paid_at của gói; COMPLETED: check-out sau check-in và trước 20:00; AUTO_CLOSED: chỉ sửa check-in, giữ check-out 20:00; OPEN: chỉ sửa check-in; không đổi ngày, hội viên, gói, người tạo, trạng thái; không sang tương lai; không chồng lấn lượt hợp lệ khác kể cả OPEN |
| Quy tắc hủy | OPEN/COMPLETED/AUTO_CLOSED chuyển sang CANCELLED, không xóa vật lý; CANCELLED chỉ đọc, không sửa/hủy lại/khôi phục, không tính lượt hợp lệ |
| Lịch sử | Mọi sửa/hủy bắt buộc lưu lý do, người thực hiện, thời điểm, dữ liệu trước/sau; cập nhật lượt và ghi lịch sử nguyên tử, lỗi rollback toàn bộ; độc lập CN-33 |
| Đồng thời | Kiểm tra lại trạng thái/dữ liệu hiện hành, từ chối thao tác xung đột, không ghi đè thay đổi mới bằng dữ liệu cũ |
| Điều kiện triển khai | Hoàn tất Must và CN-16 tra cứu; thêm bảng lịch sử vào DL-04 |

### 5.2. FUT-02: Tác vụ nền tự đóng lúc 20:00 và xử lý bù khi khởi động lại

| Nội dung | Mô tả |
| --- | --- |
| Hành vi hiện tại | Đóng lười tại CN-15; giá trị kết thúc là 20:00 ngày check-in |
| Đặc tả mục tiêu | Tác vụ nền (ví dụ ServletContextListener với bộ lập lịch) chạy đúng 20:00 giờ Việt Nam mỗi ngày chuyển OPEN sang AUTO_CLOSED; khi server khởi động lại, xử lý OPEN đã quá 20:00 ngày check-in với giờ kết thúc 20:00 ngày check-in (không dùng giờ khởi động); không đóng sớm; chỉ tác động OPEN; không xử lý lặp hoặc ghi đè thay đổi hợp lệ, kể cả khi cạnh tranh với sửa/hủy |
| Điều kiện triển khai | Giữ đóng lười như lớp bảo vệ thứ hai |

### 5.3. FUT-03: Chọn ngày bắt đầu tương lai khi xác nhận PAID

| Nội dung | Mô tả |
| --- | --- |
| Hành vi hiện tại | start_at theo công thức tại mục 3.1.3; không có trường nhập ngày; cảnh báo Daily Pass sau 20:00 |
| Đặc tả mục tiêu | Khi công thức cho ra 00:00 hôm nay (chưa có PAID hoặc mọi PAID đã hết hạn): mặc định hôm nay, cho chọn ngày tương lai (00:00 giờ Việt Nam), từ chối ngày quá khứ. Khi có PAID đang hiệu lực/chờ hiệu lực: bắt buộc nối end_exclusive, không cho chọn ngày tạo khoảng trống. start_at khác paid_at; gói tương lai chưa được check-in |
| Điều kiện triển khai | Thêm trường ngày vào màn hình xác nhận và kiểm tra ở backend. Vì có thể tạo khoảng trống giữa chuỗi cũ đã hết hạn và gói mới, cần kiểm tra lại giả định "không có khoảng trống" của công thức start_at và cảnh báo sắp hết hạn |

---

## 6. Phụ lục: Truy vết tiêu chí chấm điểm môn học (PRJ301)

| Tiêu chí | Tỷ trọng | Yêu cầu đáp ứng |
| --- | --- | --- |
| Business rules/logic | 30% | CN-10–CN-15, CN-21, CN-22, CN-25, CN-29, QT-01–QT-03, QT-06, QT-18, QT-19, QT-25, QT-26 |
| Authentication: Login/Logout | 5% | CN-01, CN-02 |
| Authorization: User roles | 5% | CN-05, CN-34, mục 2.3 |
| List | 15% | CN-07, CN-16, CN-23, CN-24 |
| Create | 10% | CN-04, CN-06, CN-09, CN-10 |
| Create - validation | 5% | CN-30, mục 3.4 |
| Update | 10% | CN-08, CN-09, CN-35, CN-36, CN-42 |
| Update - validation | 5% | CN-30, mục 3.4 |
| Delete | 5% | CN-04 (xóa mềm tài khoản), CN-08 (xóa mềm hội viên) |
| Cookies | 5% | CN-37 (và JSESSIONID, CN-03) |
| Session | 5% | CN-03, bảng phiên tại mục 3.1.1 |
| Bonus: JavaScript | 5% | CN-30 (kiểm tra client), CN-38, CN-39 |
| Bonus: reCAPTCHA | 5% | CN-40 |
| Bonus: Excel export/import | 10% | CN-32, CN-41 |
| Phạt: mật khẩu không hash | -50% | CN-36, DL-07, CL-07 |
| Phạt: không dùng cơ sở dữ liệu | -40% | CL-03 (SQL Server 2022) |
| Phạt: đạo văn / không hiểu mã mình viết | -90% | Ngoài SRS; người thực hiện phải giải thích được mọi luồng đã triển khai khi bảo vệ |

Phần Bonus chỉ có điểm khi các chức năng chính hoàn thành; vì vậy các hạng mục Should (B) được xếp sau toàn bộ Must.