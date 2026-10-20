# ĐẶC TẢ YÊU CẦU PHẦN MỀM (SRS)

# HỆ THỐNG QUẢN LÝ PHÒNG GYM

Baseline 1.0 — cập nhật ngày 15/10/2026.

## 1. Giới thiệu

### 1.1. Mục đích

Tài liệu xác định chức năng, quyền truy cập, dữ liệu, quy tắc nghiệp vụ và điều kiện chấp nhận của Gym Management System. Đây là căn cứ triển khai và lập kế hoạch kiểm thử; các tiêu chí trong tài liệu không phải kết quả kiểm thử phần mềm.

### 1.2. Phạm vi và ưu tiên

Website nội bộ quản lý hội viên, gói tập, thanh toán thủ công, lượt tập và báo cáo. Không yêu cầu tích hợp Excel, Notion hoặc Zalo đang được sử dụng trong hoạt động hiện tại.

- Must: bắt buộc trong phiên bản đầu.
- Should: triển khai sau các chức năng nghiệp vụ chính.
- Could: triển khai khi còn nguồn lực.
- OUT OF SCOPE: ngoài phạm vi; các ID đã loại bỏ không được tái sử dụng.

| Nhóm chức năng | Phạm vi | Ưu tiên |
| --- | --- | --- |
| Truy cập và phân quyền | Đăng nhập, đăng xuất, phiên đăng nhập, năm vai trò cố định, khóa/mở tài khoản | Must |
| Nhân viên | Tài khoản và thông tin nhân viên cơ bản | Must |
| Hội viên | Tạo, tìm kiếm, cập nhật, xóa mềm hồ sơ; trạng thái hồ sơ độc lập tình trạng gói | Must |
| Gói tập | Quản lý gói, đăng ký, gia hạn/mua tiếp, kiểm tra hiệu lực | Must |
| Thanh toán | Xác nhận thủ công tiền mặt/chuyển khoản, lưu và tra cứu giao dịch | Must |
| Lượt tập | Check-in trước 20:00, check-out, tự đóng lúc 20:00 giờ Việt Nam, sửa/hủy có lịch sử | Must |
| Báo cáo | Doanh thu, số hội viên, hội viên mới, đăng ký mới và gia hạn/mua tiếp | Must |
| Biên nhận | Xem/in HTML, lưu PDF bằng chức năng Print của trình duyệt | Should |
| Nhật ký hoạt động tổng quát | Ghi nhận và tra cứu Audit Log | Should |
| Xuất Excel | Xuất hội viên, đăng ký gói, giao dịch và báo cáo doanh thu thành `.xlsx` | Could |

Ngoài phạm vi:

- Sản phẩm, tồn kho, đơn bán hàng, doanh thu bán sản phẩm.
- Nhập dữ liệu Excel.
- Thanh toán trực tuyến, API ngân hàng, tự sinh QR, tự xác minh chuyển khoản.
- Hóa đơn VAT/điện tử, tính thuế, kế toán chuyên sâu, đối soát ngân hàng tự động.
- Quản lý nhân sự chuyên sâu, vai trò tùy chỉnh, cấu hình quyền động.
- Đổi/nâng cấp gói, hủy/hoàn tiền/điều chỉnh giao dịch PAID, phê duyệt nhiều cấp.
- Ứng dụng hội viên, website giới thiệu, đặt lịch PT, BMI, bài tập cá nhân hóa, thương mại điện tử.
- Khôi phục hội viên đã xóa mềm, chuyển số điện thoại; quên mật khẩu qua email/OTP hoặc khôi phục tự động.

### 1.3. Thuật ngữ và quy ước

| Thuật ngữ | Ý nghĩa |
| --- | --- |
| Hội viên / Member | Người tập có hồ sơ; không phải tài khoản đăng nhập nội bộ |
| Người dùng | Nhân sự có tài khoản truy cập hệ thống |
| Loại gói | Cấu hình tên, thời hạn và giá của quyền sử dụng phòng gym |
| Đăng ký gói / Membership | Một lần đăng ký hoặc mua tiếp gói gắn với hội viên, thời hạn và giao dịch |
| Gia hạn/mua tiếp | Lần mua PAID sau lần PAID đầu tiên của hội viên, kể cả gói trước đã hết hạn; không có nghiệp vụ đổi/nâng cấp gói |
| Daily Pass | Gói 1 ngày: quyền tập trong một ngày, nhiều lần ra/vào |
| PAID | Nhân viên đã xác nhận nhận đủ tiền; là trạng thái giao dịch hoàn thành, không đồng nghĩa gói đã đến ngày bắt đầu |
| start_at / end_exclusive | Mốc bắt đầu / mốc kết thúc độc quyền của gói; hiệu lực theo khoảng `[start_at, end_exclusive)` |
| Rolling / paid-order | Cộng lịch từ ngày bắt đầu thực tế từng gói / xếp chuỗi gói theo thứ tự xác nhận PAID |
| created_at / paid_at | Thời điểm tạo hồ sơ/bản ghi / thời điểm xác nhận đã nhận đủ tiền |
| OPEN | Lượt tập đã vào nhưng chưa kết thúc |
| RBAC | Phân quyền theo vai trò |
| CN / DL / QT / CL / GT / KT / XN | Mã chức năng / dữ liệu / quy tắc / chất lượng / giao tiếp / kiểm tra / nội dung xác nhận |

Mọi ngày giờ nghiệp vụ dùng giờ Việt Nam. Transaction là giao dịch thanh toán gắn với một Membership. Chuỗi PAID dùng để xếp lịch mới là chuỗi còn gói đang hiệu lực hoặc chờ hiệu lực tại thời điểm xác nhận; lịch sử đã hết hạn không phải điểm nối lịch mới.

## 2. Mô tả tổng thể

### 2.1. Tổng quan hệ thống và môi trường

| Nội dung | Yêu cầu |
| --- | --- |
| Hình thức | Website nội bộ cho quản trị viên và nhân viên |
| Hồ sơ hội viên | Hỗ trợ tối thiểu 100 hồ sơ. |
| Đồng thời | 10 phiên hoạt động đồng thời là quy mô dự kiến, không phải tiêu chí nghiệm thu bắt buộc |
| Số tài khoản nhân viên | Theo nhu cầu nhân sự, không quy định tối thiểu 100 tài khoản |
| Môi trường kiểm thử | Java Servlet/JSP, JDK 21, Apache Tomcat 10.1, Microsoft SQL Server 2022; Chrome và Brave bản hiện hành tại thời điểm kiểm thử; một máy chạy application server và database trong môi trường local |
| Sao lưu và lưu trữ | Sao lưu dữ liệu thủ công trước demo; không tự động xóa dữ liệu hoặc log |
| Kiểm soát truy cập | Kiểm tra quyền tại Servlet/backend |
| Xác thực | HttpSession phía server và cookie JSESSIONID |
| Giờ hoạt động phòng gym | 07:00-20:00 hằng ngày; cấm check-in từ 20:00 và tự đóng OPEN lúc 20:00 theo giờ Việt Nam; không đặt giờ cấm đăng nhập |

### 2.2. Vai trò người dùng

Mỗi tài khoản thuộc đúng một vai trò cố định. Hội viên không có tài khoản đăng nhập nội bộ.

| Vai trò | Phạm vi chính |
| --- | --- |
| Administrator | Quản trị tài khoản, phân quyền cố định; xem Audit Log và xuất dữ liệu theo ma trận. |
| Owner | Theo dõi hoạt động, quản lý loại gói và các thao tác được cấp riêng. |
| Manager | Quản lý nghiệp vụ tại quầy và tài khoản Receptionist/Accountant trong phạm vi được cấp. |
| Receptionist | Tiếp nhận hội viên, đăng ký gói, thanh toán, lượt tập và biên nhận theo quyền. |
| Accountant | Tra cứu giao dịch, doanh thu; biên nhận và xuất dữ liệu tài chính theo quyền. |

### 2.3. Ma trận phân quyền (RBAC)

C: được phép trong phạm vi ghi rõ. K: không được phép. —: chưa được cấp quyền, hệ thống từ chối; không suy ra lệnh cấm vĩnh viễn. Quyền Should/Could chỉ áp dụng khi chức năng được triển khai.

| Hành động | Administrator | Owner | Manager | Receptionist | Accountant | Phạm vi |
| --- | --- | --- | --- | --- | --- | --- |
| Đăng nhập/đăng xuất | C | C | C | C | C | CN-01–CN-03; tài khoản hợp lệ |
| Tự đổi mật khẩu | C | C | C | C | C | CN-36; sau xác thực hợp lệ, tài khoản không khóa/xóa mềm |
| Đặt lại mật khẩu tài khoản khác | C | K | K | K | K | CN-36; Administrator chỉ trong phạm vi quản lý đã xác nhận |
| Tạo/sửa/khóa tài khoản Receptionist, Accountant | C | — | C | — | K | Manager không đổi vai trò |
| Mở khóa Receptionist, Accountant | C | — | C | — | K | chỉ hai vai trò này |
| Quản lý tài khoản Administrator, Owner | C | — | K | — | K | Theo phạm vi hành động. |
| Quản lý tài khoản Manager | C | — | K | — | K | Manager không quản lý Manager khác; tự đổi mật khẩu theo CN-36 |
| Gán/thay đổi vai trò | C | — | K | — | K | Năm vai trò cố định; cấm tự nâng quyền |
| Xóa mềm tài khoản | C | — | — | — | K | CN-04; cấm tự xóa, giữ lịch sử |
| Xem/tìm hội viên, tình trạng gói | — | C | C | C | K | Theo phạm vi hành động. |
| Tạo/cập nhật hồ sơ hội viên | — | — | C | C | K | Quyền quản lý hội viên đã có; không gồm xóa mềm |
| Xóa mềm hội viên | — | C | C | — | K | Giữ lịch sử; xử lý PENDING theo CN-08 |
| Tạo loại gói trong danh mục đã chốt | — | C | — | — | K | Chỉ Owner được tạo; quyền sửa tên/giá được cấp riêng |
| Sửa tên, giá loại gói | — | C | C | — | K | không sửa thời hạn/ngừng dùng; giữ snapshot cũ |
| Đăng ký/gia hạn (tạo PENDING) | — | — | C | C | K | hội viên chưa xóa mềm |
| Xác nhận thanh toán PAID | — | — | C | C | K | không vượt điều kiện hội viên chưa xóa mềm |
| Hủy giao dịch PENDING và đăng ký liên quan | — | — | C | C | K | không hủy PAID |
| Xem/lọc danh sách giao dịch | — | C | C | C | C | được tra cứu toàn bộ danh sách |
| Check-in/check-out thông thường | — | — | C | C | K | tuân thủ hiệu lực gói, giờ hoạt động và OPEN |
| Xem lịch sử lượt tập | — | C | C | C | K | tra cứu toàn bộ lịch sử |
| Sửa/hủy lượt tập sai | — | C | C | C | K | bắt buộc lịch sử thay đổi |
| Xem báo cáo/thống kê | — | C | C | — | C | Accountant chỉ dữ liệu giao dịch/doanh thu |
| Xem/in biên nhận PAID (Should) | — | — | — | C | C | không nâng thành Must |
| Xuất Excel hội viên/đăng ký (Could) | C | C | C | — | K | không kéo theo quyền sửa nghiệp vụ |
| Xuất Excel giao dịch/doanh thu (Could) | C | C | C | — | C | không nâng thành Must |
| Xem Audit Log tổng quát (Should) | C | K | K | K | K | Chỉ Administrator |
| Sửa/xóa Audit Log qua giao diện thường | K | K | K | K | K | Theo phạm vi hành động. |
| Hủy/hoàn tiền/điều chỉnh PAID | K | K | K | K | K | OUT OF SCOPE |

Backend kiểm tra vai trò và trạng thái tài khoản tại mọi request được bảo vệ, kể cả URL trực tiếp. Administrator và Owner không có toàn quyền nghiệp vụ. Quyền xuất dữ liệu không kéo theo quyền xem màn hình hoặc sửa nghiệp vụ. Quyền quản lý tài khoản không tự bao gồm đặt lại mật khẩu. Thông tin cần đọc trong luồng đã cấp quyền chỉ phục vụ luồng đó, không cấp quyền tra cứu toàn bộ dữ liệu. Không có vai trò tùy chỉnh, cấu hình quyền động hoặc phê duyệt nhiều cấp.

### 2.4. Luồng nghiệp vụ

1. Tìm hội viên hiện có hoặc tạo hồ sơ; không tạo lại hồ sơ khách cũ.
2. Chọn gói, tạo Membership và Transaction PENDING; lưu snapshot, chưa cấp lịch.
3. Nhận đủ tiền mặt hoặc kiểm tra chuyển khoản qua QR ngân hàng có sẵn.
4. Manager/Receptionist xác nhận PAID. Hệ thống kiểm tra điều kiện, lưu paid_at và xếp lịch nguyên tử theo mục 3.1.3.
5. Manager/Receptionist check-in khi đạt CN-13; check-out theo CN-15. Hệ thống tự đóng lượt lúc 20:00 và xử lý bù sau gián đoạn.
6. Owner/Manager/Receptionist tra cứu, sửa/hủy lượt sai theo CN-16.
7. Người có quyền tra cứu và thống kê; in biên nhận hoặc xuất Excel nếu chức năng tương ứng được triển khai.

## 3. Yêu cầu cụ thể

### 3.1. Yêu cầu chức năng

Mọi điều kiện chấp nhận phải đồng thời tuân thủ ma trận tại mục 2.3, dữ liệu tại mục 3.3 và kiểm tra hợp lệ tại mục 3.4.

#### 3.1.1. Truy cập và tài khoản nhân viên

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-01 | Đăng nhập | Đăng nhập bằng tài khoản hợp lệ | Tài khoản không khóa/không bị xóa đăng nhập theo vai trò; tạo phiên và thay session ID khi đăng nhập thành công | Must |
| CN-02 | Đăng xuất | Đăng xuất | Hủy phiên hiện tại; phiên đã hủy không thể truy cập chức năng bảo vệ. | Must |
| CN-03 | Quản lý phiên | Quản lý phiên HttpSession/JSESSIONID | Hết hạn sau 60 phút không hoạt động; request hợp lệ tính lại thời gian không hoạt động; hết hạn phải đăng nhập lại | Must |
| CN-04 | Quản lý tài khoản | Tạo, sửa, xóa mềm, khóa/mở tài khoản theo RBAC | Thao tác đúng phạm vi tại mục 2.3; từ chối tự xóa/tự nâng quyền. Khóa/xóa mềm chặn đăng nhập và hủy phiên tại request bảo vệ tiếp theo; mở khóa không phục hồi phiên. Giữ liên kết lịch sử. | Must |
| CN-05 | Phân quyền | Phân quyền theo năm vai trò cố định, mỗi tài khoản một vai trò | Kiểm tra ở Servlet/backend; từ chối thao tác vượt quyền cả khi truy cập URL trực tiếp; không có giao diện tạo vai trò/quyền động | Must |
| CN-34 | Kiểm soát quyền nghiệp vụ | Kiểm soát nghiệp vụ theo ma trận RBAC | Mọi ô C tại mục 2.3 chỉ được thực hiện trong phạm vi ghi rõ; K/— bị từ chối cả qua request trực tiếp và không làm thay đổi dữ liệu. | Must |
| CN-35 | Quản lý nhân viên | Quản lý nhân viên cơ bản | Người có quyền lưu và xem lại các trường DL-07 theo CN-04. Từ chối thiếu trường bắt buộc hoặc vi phạm mục 3.4. Mật khẩu không hiển thị lại; không có quản lý HR chuyên sâu. | Must |
| CN-36 | Đổi và đặt lại mật khẩu | Tự đổi và đặt lại mật khẩu theo quyền | Tự đổi và đặt lại mật khẩu tuân thủ quy tắc bên dưới; yêu cầu sai quyền/xác thực bị từ chối. Mật khẩu cũ không đăng nhập được; mật khẩu mới dùng được nếu tài khoản hợp lệ. | Must |

Quy tắc mật khẩu (CN-36):

- Mọi nhân viên được tự đổi mật khẩu sau xác thực hợp lệ; từ chối nếu tài khoản khóa/xóa mềm hoặc phiên không hợp lệ.
- Chỉ Administrator đặt lại mật khẩu người khác trong phạm vi quản lý tại mục 2.3; các vai trò khác, kể cả Manager, bị từ chối tại backend.
- Mật khẩu mới lưu dưới dạng hash an toàn, không plaintext. Không có quên mật khẩu qua email/OTP/khôi phục tự động.

| Quy tắc phiên và cookie | Kết quả bắt buộc |
| --- | --- |
| Kiểm tra tài khoản | Backend kiểm tra ACTIVE/LOCKED và dấu xóa mềm tại mỗi request cần xác thực; tài khoản xóa mềm không được tiếp tục truy cập |
| Khóa hoặc xóa mềm khi đang đăng nhập | Request bảo vệ tiếp theo bị chặn, phiên bị hủy; không thể tiếp tục thao tác chức năng bảo vệ |
| Mở khóa | Không tự khôi phục phiên đã bị hủy |
| Phiên hết hạn | Không cho tiếp tục thao tác yêu cầu đăng nhập |
| Dữ liệu cookie | Không lưu mật khẩu hoặc thông tin xác thực nhạy cảm |
| Cookie theo môi trường | Cookie phiên dùng HttpOnly và SameSite phù hợp; khi dùng HTTPS phải có Secure. Demo local không bắt buộc HTTPS public; cấu hình cookie phải cho phép phiên hoạt động đúng giao thức kiểm thử |

#### 3.1.2. Hội viên và tình trạng gói

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-06 | Tạo hội viên | Tạo hồ sơ hội viên, kể cả trước khi mua gói | Tạo được hồ sơ chưa mua gói với ID tự sinh; từ chối dữ liệu không hợp lệ hoặc điện thoại trùng theo mục 3.4. | Must |
| CN-07 | Tìm hội viên | Tìm hội viên theo mã, tên hoặc điện thoại | Mã và điện thoại khớp chính xác; tên khớp một phần, không phân biệt hoa/thường. | Must |
| CN-08 | Cập nhật và xóa mềm hội viên | Cập nhật và xóa mềm hồ sơ hội viên theo quyền | Cập nhật hợp lệ được lưu theo quyền. Xóa mềm và xử lý các đăng ký liên quan tuân thủ quy tắc bên dưới; không xóa vật lý hoặc mất lịch sử. | Must |
| CN-12 | Xem tình trạng gói | Xem riêng trạng thái hồ sơ và tình trạng từng đăng ký gói | Hiển thị đúng các tình trạng và cảnh báo tại mục 3.1.2; PAID tương lai không được hiển thị là đang có quyền tập. | Must |

Quy tắc xóa mềm hội viên (CN-08):

- Chỉ Owner/Manager được xóa mềm; giữ liên kết gói, giao dịch và lượt tập.
- Hồ sơ đã xóa mềm không được đăng ký mới, check-in hoặc xác nhận PAID cho PENDING còn tồn tại.
- PENDING không tự hủy; giữ nguyên đến khi Manager/Receptionist hủy thủ công cùng đăng ký liên quan. PAID cũ không bị ảnh hưởng.

| Đối tượng/trạng thái nghiệp vụ | Quy tắc |
| --- | --- |
| Hồ sơ hội viên | Phân biệt chưa xóa mềm/đã xóa mềm; độc lập với gói; được tạo trước khi mua; không phải tài khoản đăng nhập |
| Đăng ký chờ thanh toán | Giao dịch PENDING: chưa có quyền tập, chưa có lịch chính thức |
| Đăng ký đã hủy | Giao dịch PENDING bị hủy: đăng ký liên quan bị hủy, không có hiệu lực |
| Gói đã trả tiền chờ hiệu lực | PAID nhưng thời điểm hiện tại trước start_at; phải phân biệt với đang hiệu lực |
| Gói đang hiệu lực | PAID và `start_at <= hiện tại < end_exclusive`; hội viên xóa mềm vẫn bị chặn check-in |
| Gói hết hạn | PAID và hiện tại từ end_exclusive trở đi; không dùng gói này để check-in |
| Cảnh báo sắp hết hạn | Gói tháng/6 tháng/năm đang hiệu lực cảnh báo khi còn tối đa 7 ngày lịch đến end_exclusive (theo ngày Việt Nam), không cảnh báo nếu đã có gói PAID nối tiếp; không thay đổi kiểm tra hiệu lực; không phải trạng thái hồ sơ |
| Daily Pass | Chỉ hiển thị thời hạn trong ngày, không áp dụng cảnh báo trước 7 ngày |
| Nhắc hội viên | Lễ tân chủ động báo; không yêu cầu tự gửi thông báo |

ACTIVE/LOCKED chỉ là trạng thái tài khoản; OPEN/COMPLETED/AUTO_CLOSED/CANCELLED là trạng thái lượt; PENDING/PAID/CANCELLED là trạng thái thanh toán. Các tình trạng Membership trong bảng trên là nhãn nghiệp vụ, không yêu cầu thêm trạng thái kỹ thuật mới.

#### 3.1.3. Gói tập, đăng ký và gia hạn

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-09 | Quản lý loại gói | Quản lý loại gói, tên và giá | Owner tạo loại gói trong bốn thời hạn tại mục 3.1.3; Owner/Manager chỉ sửa tên, giá hợp lệ theo mục 3.4. Không sửa thời hạn/ngừng dùng. Đăng ký mới dùng cấu hình hiện hành; snapshot cũ được giữ theo CN-25. | Must |
| CN-10 | Tạo đăng ký gói | Tạo đăng ký và giao dịch cho hội viên chưa xóa mềm | Mỗi lần mua có một Membership và giao dịch PENDING tương ứng; lưu snapshot tên gói, thời hạn đăng ký và số tiền lúc tạo, không giữ lịch chính thức; PENDING không cấp quyền tập; từ chối đăng ký mới cho hội viên xóa mềm | Must |
| CN-11 | Gia hạn/mua tiếp | Tạo lần mua tiếp trên hồ sơ hội viên hiện có; xếp lịch khi xác nhận PAID theo paid-order và rolling. | Áp dụng bảng chọn ngày tại mục 3.1.3 cho cả lần PAID đầu và lần mua sau khi hết hạn. Chuỗi còn hiệu lực/chờ hiệu lực được nối đúng cuối chuỗi; không dùng cuối chuỗi đã hết hạn để xếp lịch quá khứ. Lịch không chồng lấn kể cả khi xác nhận đồng thời. | Must |

| Loại gói | Thời hạn | Cách cộng từ ngày bắt đầu thực tế |
| --- | --- | --- |
| Daily Pass | 1 ngày | Ngày lịch kế tiếp |
| Monthly Pass | 1 tháng | Cộng 1 tháng theo lịch |
| 6-Month Pass | 6 tháng | Cộng 6 tháng theo lịch |
| Yearly Pass | 1 năm | Cộng 1 năm theo lịch |

Quy tắc chọn ngày bắt đầu khi xác nhận PAID (QT-18, QT-25):

| Lịch sử PAID tại thời điểm xác nhận | Ngày bắt đầu của gói mới |
| --- | --- |
| Chưa từng có PAID | Mặc định ngày xác nhận thanh toán; cho phép chọn ngày tương lai, từ chối ngày quá khứ. |
| Tất cả gói PAID trước đó đã hết hạn | Bắt đầu chuỗi mới như lần chưa có PAID; không dùng end_exclusive của chuỗi đã hết hạn để tạo lịch trong quá khứ. |
| Có gói PAID đang hiệu lực hoặc chờ hiệu lực | Bắt buộc nối start_at vào end_exclusive của gói cuối chuỗi; không cho chọn ngày khác tạo khoảng trống. |

Ngày bắt đầu được xác lập lúc 00:00 giờ Việt Nam. paid_at là thời điểm xác nhận tiền thực tế, không đồng nhất với start_at. PENDING không giữ lịch chính thức; ngày yêu cầu nếu có không cấp quyền hoặc giữ chỗ. Thứ tự xác nhận PAID quyết định thứ tự xếp lịch, không phải thứ tự tạo PENDING. Quy tắc nguyên tử và đồng thời tại QT-26 áp dụng cho cả ba trường hợp trên.

| Quy tắc thời hạn | Yêu cầu |
| --- | --- |
| Khoảng hiệu lực | `[start_at, end_exclusive)`; tại end_exclusive gói cũ không còn hiệu lực. Ngày cuối được tập là ngày trước end_exclusive. Giao diện có thể hiển thị ngày cuối đến 23:59:59, nhưng check-in vẫn giới hạn trước 20:00. |
| Cộng lịch | Tháng/năm tính theo lịch, không đổi thành 30/365 ngày; nếu tháng đích không có ngày tương ứng, lấy ngày cuối tháng đích. |
| Rolling | Mỗi gói cộng thời hạn từ ngày bắt đầu thực tế của chính gói đó, không neo theo ngày mua đầu tiên. |
| Không chồng lấn | Có thể lưu nhiều Membership lịch sử/tương lai; tại một thời điểm tối đa một gói có hiệu lực. Hai gói tiếp giáp tại end_exclusive không chồng lấn. |
| Quyền sử dụng | Chỉ được tập sau PAID và trong khoảng hiệu lực, kể cả start_at cùng ngày nhưng trước paid_at. |
| Phạm vi loại gói | Chỉ khác thời hạn/giá; không có quyền lợi đặc biệt, đổi/nâng cấp gói hoặc hoàn tiền. |

#### 3.1.4. Lượt tập

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-13 | Kiểm tra check-in | Kiểm tra trước check-in | Hội viên tồn tại, chưa xóa mềm; có gói PAID với start_at <= thời điểm check-in < end_exclusive và thời điểm check-in không trước paid_at; chưa có OPEN; giờ Việt Nam từ 07:00 đến trước 20:00. Thiếu điều kiện nào thì từ chối. | Must |
| CN-14 | Ghi nhận check-in | Ghi nhận check-in | Lưu hội viên, gói tập, thời gian, người tạo lượt và trạng thái OPEN; mỗi hội viên tối đa một lượt OPEN | Must |
| CN-15 | Kết thúc lượt tập | Kết thúc lượt thủ công và tự động | Chỉ check-out thủ công lượt OPEN trước 20:00 cùng ngày check-in, sau giờ check-in; thành công chuyển COMPLETED. Không có OPEN thì từ chối. Từ 20:00, OPEN phải thành AUTO_CLOSED theo quy tắc bên dưới; không thay đổi hạn gói. | Must |
| CN-16 | Tra cứu, sửa và hủy lượt tập | Cho phép tra cứu toàn bộ lịch sử và sửa/hủy lượt ghi nhận sai theo quyền tại mục 2.3. | Sửa giờ trong ngày ghi nhận ban đầu, hủy và lưu lịch sử theo mục 3.1.4. Cập nhật lượt và lịch sử phải nguyên tử; lỗi rollback toàn bộ. Thao tác đồng thời phải kiểm tra lại dữ liệu/trạng thái hiện hành, từ chối xung đột. Lịch sử sửa/hủy là Must, độc lập CN-33 Should. | Must |

Quy tắc kết thúc lượt (CN-15):

- Đúng 20:00 giờ Việt Nam mỗi ngày, hệ thống chuyển các lượt OPEN sang AUTO_CLOSED. Từ mốc này không chấp nhận check-out thủ công; OPEN còn tồn tại phải được xử lý tự đóng, không chuyển COMPLETED.
- Khi server khởi động lại, xử lý OPEN đã quá 20:00 ngày check-in; lưu giờ kết thúc là 20:00 ngày check-in, không dùng giờ khởi động lại. Không đóng sớm lượt chưa tới hạn.
- Tác vụ tự đóng và xử lý bù kiểm tra trạng thái/dữ liệu hiện hành, chỉ tác động OPEN; không thay đổi lượt COMPLETED, AUTO_CLOSED hoặc CANCELLED, không xử lý lặp hoặc ghi đè thay đổi hợp lệ. Lượt OPEN đã sửa giờ vẫn chịu quy tắc tự đóng. Giới hạn này áp dụng cho tác vụ tự động; sửa/hủy thủ công tuân theo CN-16.

| Trạng thái lượt | Ý nghĩa | Thao tác sửa/hủy được phép |
| --- | --- | --- |
| OPEN | Đã check-in, chưa có thời gian check-out; chặn tạo lượt mới. | Sửa giờ check-in trong ngày ban đầu hoặc hủy. |
| COMPLETED | Đã check-out sau check-in và trước 20:00 cùng ngày. | Sửa giờ check-in/check-out trong ngày ban đầu hoặc hủy; check-out sau sửa vẫn phải sau check-in và trước 20:00. |
| AUTO_CLOSED | Hệ thống tự đóng; giờ kết thúc quy ước là 20:00 ngày check-in, không phải giờ rời phòng thực tế. | Chỉ sửa giờ check-in trong ngày ban đầu hoặc hủy; giữ nguyên check-out tự động lúc 20:00. |
| CANCELLED | Lượt đã hủy, không tính là lượt tập hợp lệ. | Chỉ đọc; không sửa, hủy lại hoặc khôi phục. |

Quy tắc sửa/hủy lượt (CN-16, QT-14):

- Chỉ sửa giờ check-in/check-out trong cùng ngày ghi nhận ban đầu (ngày check-in lần đầu theo giờ Việt Nam); không đổi ngày của lượt, kể cả đổi đồng thời cả hai mốc.
- Check-in từ 07:00 đến trước 20:00, nằm trong thời hạn gói PAID đã gắn với lượt và không trước paid_at của giao dịch tương ứng.
- COMPLETED: check-out phải sau check-in và trước 20:00 cùng ngày ban đầu, kể cả khi sửa lịch sử. AUTO_CLOSED: check-out cố định đúng 20:00 ngày check-in, không được sửa. OPEN chưa có check-out; không được thêm check-out qua thao tác sửa.
- Không sửa thời gian sang tương lai hoặc tạo chồng lấn với lượt hợp lệ khác của cùng hội viên, kể cả OPEN. CANCELLED không tính là lượt hợp lệ.
- Thao tác sửa không thay đổi hội viên, gói tập, người tạo lượt hoặc trạng thái; không vi phạm tối đa một OPEN/hội viên.
- Hủy lượt được phép chuyển trạng thái thành CANCELLED, không xóa vật lý. CANCELLED chỉ đọc, không sửa, hủy lại hoặc khôi phục.
- Mọi sửa/hủy bắt buộc lưu lý do, người thực hiện, thời điểm, dữ liệu trước/sau. Cập nhật lượt và ghi lịch sử phải nguyên tử; thiếu dữ liệu lịch sử hoặc có lỗi ở bất kỳ bước nào phải rollback toàn bộ thao tác.
- Khi sửa/hủy đồng thời, phải kiểm tra lại trạng thái và dữ liệu hiện hành, từ chối thao tác xung đột; không ghi đè dữ liệu mới bằng dữ liệu cũ. Thao tác bị từ chối không thay đổi lượt hoặc lịch sử đã lưu; các bất biến về thời gian, chồng lấn và OPEN vẫn phải được giữ.

Cho phép ra/vào nhiều lần trong hạn gói khi lượt trước đã kết thúc. Check-out không đổi thời hạn gói.

#### 3.1.5. Thanh toán và giao dịch

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-21 | Quản lý thanh toán | Ghi nhận Cash/Bank Transfer thủ công; xác nhận đủ tiền hoặc hủy giao dịch PENDING theo quyền. | Chỉ hội viên chưa xóa mềm và giao dịch còn PENDING, đã nhận đủ số tiền snapshot được chuyển PAID. Lưu paid_at thực tế, người xác nhận và lịch theo mục 3.1.3 trong cùng thao tác nguyên tử; lỗi không lưu một phần. Xử lý trùng/đồng thời theo QT-26. Hủy PENDING đồng thời hủy đăng ký liên quan; không hủy PAID. | Must |
| CN-22 | Cấp quyền sử dụng gói | Xác lập lịch Membership và quyền sử dụng sau thanh toán. | PENDING không giữ lịch/cấp quyền. PAID dùng bảng chọn ngày tại mục 3.1.3, kể cả khi tất cả gói cũ hết hạn; chỉ được tập sau PAID và trong khoảng hiệu lực. Hai xác nhận đồng thời không tạo lịch chồng lấn. Từ chối PAID cho hội viên xóa mềm; ảnh chuyển khoản không thay xác nhận tiền thực nhận. | Must |
| CN-23 | Tra cứu giao dịch | Xem giao dịch và thông tin thanh toán theo quyền | Owner/Manager/Receptionist/Accountant được tra cứu toàn bộ giao dịch đã lưu và liên kết hội viên/đăng ký; không sửa trực tiếp số tiền hoặc trạng thái của giao dịch hoàn thành | Must |
| CN-24 | Lọc giao dịch | Lọc giao dịch theo thời gian và trạng thái | PENDING/CANCELLED dùng created_at; PAID và báo cáo doanh thu dùng paid_at; khoảng ngày bao gồm trọn ngày đầu/cuối; khi chọn nhiều trạng thái áp dụng mốc tương ứng từng giao dịch và thể hiện rõ trên giao diện | Must |
| CN-25 | Bảo toàn giao dịch | Bảo toàn giao dịch và số tiền đã lưu | Không xóa lịch sử, hủy, hoàn tiền hoặc điều chỉnh PAID. Giữ snapshot tên gói/thời hạn khi tạo đăng ký và số tiền khi tạo giao dịch cho cả PENDING/PAID; không tính lại từ cấu hình hiện hành khi thanh toán, xem, báo cáo, in hoặc xuất. Request xác nhận bị từ chối không đổi dữ liệu đã xác lập. | Must |

| Trạng thái thanh toán | Ý nghĩa | Ảnh hưởng |
| --- | --- | --- |
| PENDING | Chờ xác nhận thanh toán đủ | Không kích hoạt gói, không giữ lịch chính thức, không tính doanh thu hoặc số lượt mua thành công |
| PAID | Nhân viên xác nhận đã nhận đủ số tiền phải thanh toán | Lưu paid_at, xếp lịch theo paid-order; quyền tập chỉ hiệu lực trong khoảng gói; tính báo cáo và cho xem/in biên nhận nếu CN-31 được triển khai |
| CANCELLED | Giao dịch đã hủy | Chỉ chuyển từ PENDING; đăng ký liên quan bị hủy, không hiệu lực, không tính doanh thu/lần mua; Manager/Receptionist được hủy theo CN-21 |

| Phương thức | Quy trình |
| --- | --- |
| Tiền mặt / Cash | Nhân viên nhận đủ tiền tại quầy rồi xác nhận trên hệ thống |
| Chuyển khoản / Bank Transfer | Cung cấp QR có sẵn, khách chuyển tiền, nhân viên kiểm tra tiền đã nhận rồi xác nhận; ảnh minh chứng tùy chọn |

#### 3.1.6. Báo cáo và kiểm tra dữ liệu

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-26 | Doanh thu ngày/tháng | Tổng hợp doanh thu ngày/tháng | Tổng số tiền thực nhận của giao dịch PAID, phân kỳ theo paid_at; loại PENDING/CANCELLED | Must |
| CN-27 | Doanh thu gói tập | Doanh thu gói tập | Doanh thu chỉ từ đăng ký/gia hạn gói theo công thức bên dưới | Must |
| CN-28 | Thống kê hội viên | Thống kê tổng hội viên và hội viên mới | Tổng bao gồm cả hồ sơ xóa mềm, hỗ trợ lọc trạng thái; hội viên mới theo Members.created_at trong kỳ, không phụ thuộc đã mua gói hoặc xóa mềm | Must |
| CN-29 | Thống kê lần mua | Thống kê đăng ký mới và gia hạn/mua tiếp | Lần PAID đầu tiên trong lịch sử hội viên là đăng ký mới; các PAID sau là gia hạn; phân loại theo toàn bộ lịch sử và thứ tự PAID hệ thống xác lập (kể cả đồng thời) rồi đếm paid_at trong kỳ; bỏ PENDING/CANCELLED khỏi lịch sử mua thành công | Must |
| CN-30 | Kiểm tra dữ liệu | Kiểm tra dữ liệu trước lưu | Áp dụng mục 3.4 ở cả giao diện và backend; dữ liệu sai bị từ chối, không lưu. | Must |

| Nội dung thống kê | Công thức hoặc quy ước |
| --- | --- |
| Khoảng ngày | Từ 00:00:00 ngày bắt đầu đến trước 00:00:00 ngày kế tiếp ngày kết thúc |
| Doanh thu | Tổng số tiền các giao dịch PAID có paid_at trong khoảng lọc |
| Hội viên mới | Số hồ sơ có created_at trong khoảng lọc |
| Đăng ký/gia hạn thành công | Đếm lần đăng ký/mua tiếp PAID trong kỳ, không đếm PENDING/CANCELLED |
| Lần mua thành công | Lần PAID đầu tiên là đăng ký mới; PAID tiếp theo là gia hạn/mua tiếp, bất kể các PENDING/CANCELLED trước đó; không có nghiệp vụ đổi/nâng cấp gói |
| Tổng hội viên | Gồm cả hồ sơ xóa mềm; bộ lọc trạng thái chỉ thu hẹp kết quả, không xóa lịch sử |
| Danh sách giao dịch | PENDING/CANCELLED theo created_at, PAID theo paid_at; kỳ doanh thu chỉ theo paid_at |

#### 3.1.7. Biên nhận, xuất dữ liệu và Audit Log

| ID | Chức năng | Mô tả yêu cầu | Điều kiện chấp nhận | Ưu tiên |
| --- | --- | --- | --- | --- |
| CN-31 | Biên nhận thanh toán | Xem/in biên nhận thanh toán | Receptionist/Accountant được xem/in; chỉ PAID có biên nhận; mỗi giao dịch một biên nhận, được in lại không tạo giao dịch mới; HTML hỗ trợ Print/lưu PDF | Should |
| CN-32 | Xuất Excel | Xuất Excel | Administrator/Owner/Manager được xuất cả bốn loại; Accountant chỉ giao dịch/doanh thu; xuất bốn loại dữ liệu tại bảng dưới, `.xlsx`, theo bộ lọc; không có dữ liệu thì thông báo; không nhập Excel | Could |
| CN-33 | Audit Log tổng quát | Audit Log tổng quát | Chỉ Administrator xem; không sửa/xóa qua giao diện quản lý thông thường; không chứa mật khẩu, session ID hay dữ liệu xác thực nhạy cảm | Should |

| Dữ liệu biên nhận CN-31 | Nội dung |
| --- | --- |
| Đơn vị và giao dịch | Tên phòng gym, mã giao dịch |
| Hội viên và gói | Mã hội viên, họ tên, tên gói snapshot, thời hạn sử dụng đã lưu |
| Thanh toán | Số tiền, phương thức, thời gian thanh toán, nhân viên xác nhận |
| Loại chứng từ | Biên nhận thanh toán, không phải hóa đơn VAT/điện tử |
| Nguồn dữ liệu | Dữ liệu giao dịch đã lưu; xem/in lại không phát sinh giao dịch mới |

| Loại xuất CN-32 | Cột/nội dung cần xuất |
| --- | --- |
| Hội viên | Mã hội viên, họ tên, điện thoại, email, ngày đăng ký |
| Đăng ký gói | Mã đăng ký, mã hội viên, tên gói, ngày bắt đầu, ngày cuối được tập (ghi rõ nhãn), tình trạng đăng ký; PENDING/CANCELLED chưa có lịch thì để trống cột thời hạn |
| Giao dịch | Mã giao dịch, hội viên, gói, số tiền, phương thức, trạng thái, thời gian thanh toán |
| Báo cáo doanh thu | Tổng hợp giao dịch PAID theo paid_at và khoảng lọc |

| Quy tắc xuất Excel | Yêu cầu |
| --- | --- |
| Tệp | Mỗi loại một tệp `.xlsx`; hàng đầu là tiêu đề cột rõ ràng |
| Bộ lọc | Tuân theo điều kiện trên giao diện, bao gồm thời gian và trạng thái nếu có |
| Giá trị | Phản ánh dữ liệu lưu tại thời điểm xuất, tên/giá gói dùng snapshot của đăng ký/giao dịch; ngày giờ thống nhất, dễ đọc; tiền lưu dưới dạng số |
| Quyền | Chỉ ô C cho loại xuất tương ứng tại 2.3; Administrator/Owner/Manager xuất cả bốn loại; Accountant chỉ xuất giao dịch/doanh thu |
| Bảo mật | Không xuất mật khẩu, thông tin phiên hoặc dữ liệu xác thực nhạy cảm |
| Giới hạn | Tệp xuất không được dùng để cập nhật dữ liệu hệ thống; chức năng chính vẫn hoạt động nếu chưa có xuất Excel |

| Nhóm Audit Log CN-33 | Sự kiện ghi nhận khi nghiệp vụ tương ứng có triển khai |
| --- | --- |
| Tài khoản | Đăng nhập thành công/thất bại, đăng xuất, tạo/sửa/xóa/khóa tài khoản, thay đổi vai trò/quyền |
| Hội viên | Tạo, sửa hoặc xóa hồ sơ |
| Gói | Tạo/sửa gói; đăng ký, gia hạn, hủy đăng ký |
| Thanh toán | Xác nhận PAID hoặc hủy PENDING; không có điều chỉnh PAID |
| Lượt tập | Điều chỉnh, hủy và kết thúc thủ công |
| Dữ liệu log | Mã log, người thực hiện, loại hành động, đối tượng và mã đối tượng, thời gian, mô tả; trước/sau với sửa đổi quan trọng |
| Hoạt động tự động | Phân biệt hành động hệ thống với hành động nhân viên |
| Không bắt buộc | Ghi log thao tác xem danh sách, tìm kiếm, lọc |
| Quan hệ với nghiệp vụ chính | Chưa có Audit Log tổng quát không được làm ngừng nghiệp vụ chính; lịch sử sửa/hủy lượt theo CN-16 vẫn bắt buộc |

Audit Log chỉ ghi nghiệp vụ được cấp quyền; không mở thêm quyền xóa vật lý hội viên hoặc điều chỉnh PAID.

### 3.2. Quy tắc nghiệp vụ

Các mã QT trỏ tới đặc tả đầy đủ ở nhóm chức năng tương ứng; không tạo bộ quy tắc thay thế. Quy tắc giao dịch dùng chung được định nghĩa tại QT-26.

| ID | Quy tắc | Đặc tả áp dụng |
| --- | --- | --- |
| QT-01 | Điều kiện check-in | CN-13, gồm điều kiện check-in không trước paid_at. |
| QT-02 | Gói hết hạn/đã hủy | CN-12, CN-13 |
| QT-03 | Một lượt OPEN | CN-14 và mục 3.1.4; áp dụng cả check-in và sửa/hủy đồng thời. |
| QT-06 | Thanh toán và quyền tập | CN-22 và mục 3.1.3 |
| QT-07 | Khóa/xóa mềm tài khoản | CN-04 và bảng phiên tại mục 3.1.1 |
| QT-08 | Kiểm soát quyền | CN-05, CN-34 và mục 2.3 |
| QT-09 | Bảo toàn PAID | CN-25 |
| QT-10 | Kiểm tra đầu vào | CN-30 và mục 3.4 |
| QT-11 | Vai trò cố định | CN-04, CN-05 |
| QT-12 | Dùng lại hồ sơ hội viên | CN-06, CN-10, CN-11 |
| QT-13 | Kỳ báo cáo | CN-24 và công thức tại mục 3.1.6 |
| QT-14 | Sửa/hủy lượt và lịch sử Must | CN-16 và quy tắc sửa/hủy tại mục 3.1.4: giữ ngày ban đầu, lưu lịch sử nguyên tử và từ chối xung đột. |
| QT-15 | Tự đóng và xử lý bù | CN-15 và quy tắc kết thúc lượt tại mục 3.1.4: trước 20:00 check-out thủ công; từ 20:00 tự đóng, xử lý bù không ghi đè. |
| QT-16 | In lại biên nhận | CN-31 |
| QT-17 | Xuất dữ liệu | CN-32 và bảng xuất dữ liệu tại mục 3.1.7 |
| QT-18 | Paid-order và chọn chuỗi | Bảng chọn ngày tại mục 3.1.3; chưa có PAID hoặc mọi PAID đã hết hạn thì tạo chuỗi mới; chỉ nối chuỗi còn hiệu lực/chờ hiệu lực |
| QT-19 | Rolling calendar | Bảng thời hạn tại mục 3.1.3 |
| QT-20 | Snapshot và sửa loại gói | CN-09, CN-25 |
| QT-21 | Hủy PENDING | CN-21 và trạng thái thanh toán tại mục 3.1.5 |
| QT-22 | Xóa mềm hội viên | CN-08, CN-28 |
| QT-23 | Phân loại lần mua | CN-29 và công thức tại mục 3.1.6 |
| QT-24 | Tình trạng và cảnh báo | Bảng tình trạng tại mục 3.1.2 |
| QT-25 | Ngày bắt đầu và thời điểm trả tiền | Bảng chọn ngày và phân biệt start_at/paid_at tại mục 3.1.3 |
| QT-26 | Tính nguyên tử và xử lý đồng thời | Các điều kiện giao dịch bên dưới; áp dụng CN-21, CN-22, CN-25, CN-29. |
| QT-27 | Thông tin tài khoản | DL-07 và mục 3.4 |
| QT-28 | Đổi/đặt lại mật khẩu | CN-36 và mục 2.3 |

Điều kiện giao dịch của QT-26:

- Chỉ một request chuyển cùng PENDING sang PAID thành công. Request trùng hoặc không còn PENDING bị từ chối, không đổi paid_at, tiền, lịch, doanh thu hoặc lịch sử.
- Hai giao dịch khác nhau cùng hội viên được xếp nối tiếp, không chồng lấn theo thứ tự hệ thống xác lập. Dùng cùng thứ tự đó để phân loại lần mua theo CN-29.
- Chuyển PAID và cập nhật lịch phải nguyên tử: lỗi không để lại PAID thiếu lịch hoặc lịch của giao dịch còn PENDING; không ghi doanh thu/lần mua một phần.

### 3.3. Yêu cầu dữ liệu

Các bảng mô tả dữ liệu nghiệp vụ, không bắt buộc thiết kế bảng/cột cơ sở dữ liệu cụ thể. paid_at/người xác nhận phát sinh khi PAID, ảnh minh chứng tùy chọn. Không xóa vật lý Member/tài khoản có lịch sử; giữ các liên kết để tránh phá khóa ngoại.

| Mã | Nhóm | Nội dung cần quản lý | Ràng buộc |
| --- | --- | --- | --- |
| DL-01 | Hội viên | ID, họ tên, điện thoại, email, ngày sinh, giới tính, created_at, dấu xóa mềm; các đăng ký gói liên quan | Member không phải tài khoản; trạng thái hồ sơ độc lập Membership; điện thoại duy nhất; không xóa vật lý, giữ liên kết/lịch sử |
| DL-02 | Loại gói | ID, tên gói, thời hạn, đơn vị, giá | Danh mục 1 ngày/1 tháng/6 tháng/1 năm; giá nguyên dương; chỉ sửa tên/giá, không sửa thời hạn hoặc ngừng dùng |
| DL-03 | Đăng ký gói | ID, hội viên, loại gói, tên gói snapshot và thời hạn tại lúc tạo PENDING, thời điểm tạo, liên kết giao dịch; start_at/end_exclusive khi xác nhận PAID; tình trạng nghiệp vụ theo 3.1.2 | Một lần mua một bản ghi; PENDING không có lịch chính thức; PAID xếp lịch theo bảng chọn ngày tại mục 3.1.3, rolling, không chồng lấn; snapshot tên/thời hạn cũ không tính lại từ cấu hình; start_at là 00:00 giờ Việt Nam, PAID và lịch cập nhật nguyên tử; có lịch sử/tương lai; không bắt buộc mã enum mới |
| DL-04 | Lượt tập và lịch sử thay đổi | ID, hội viên, gói tập, người tạo lượt, ngày ghi nhận ban đầu, thời gian vào/ra, nhân viên hoặc hệ thống kết thúc lượt, trạng thái; lý do, người thực hiện, thời điểm, dữ liệu trước/sau khi sửa/hủy | Ngày ban đầu và liên kết hội viên/gói/người tạo không đổi; hủy không xóa vật lý. Cập nhật lượt và lịch sử Must phải nguyên tử, từ chối xung đột theo CN-16. Tự đóng theo CN-15; giữ check-out AUTO_CLOSED lúc 20:00 ngày check-in. |
| DL-06 | Giao dịch | Mã giao dịch, hội viên, đăng ký gói, tên gói snapshot tại lúc tạo PENDING, số tiền tại lúc tạo, Cash/Bank Transfer, created_at, người thực hiện, trạng thái PENDING/PAID/CANCELLED; paid_at, người xác nhận và thứ tự xác nhận do hệ thống xác lập khi PAID; ảnh tùy chọn | Giữ tên/tiền đã tạo kể cả PENDING và PAID; chỉ một request xác nhận thành công, từ chối request trùng mà không đổi dữ liệu đã xác lập; chỉ PENDING được hủy; PAID không điều chỉnh/xóa lịch sử; paid_at và created_at có ý nghĩa riêng, có thể trùng thời điểm; liên kết không bị mất khi xóa mềm hội viên/tài khoản |
| DL-07 | Tài khoản và nhân viên | ID, username, password hash, họ tên, điện thoại, một vai trò, ACTIVE/LOCKED, dấu xóa mềm; email tùy chọn | Username duy nhất không phân biệt hoa/thường; điện thoại đúng 10 chữ số, duy nhất trong tập nhân viên kể cả khóa/xóa mềm, được trùng hội viên; email không yêu cầu duy nhất; không lưu mật khẩu plaintext; không gộp khóa với xóa mềm; giữ liên kết giao dịch/lượt/log, chặn phiên đang tồn tại khi khóa/xóa mềm |
| DL-09 | Audit Log | Mã log, người/hệ thống thực hiện, hành động, đối tượng, mã đối tượng, thời gian, mô tả, trước/sau khi cần | Should; không sửa/xóa qua giao diện thường, không chứa bí mật xác thực |

### 3.4. Kiểm tra hợp lệ

Các dòng họ tên/điện thoại/email/ngày sinh/giới tính dưới đây mô tả hồ sơ hội viên; ràng buộc nhân viên được ghi riêng, không suy ra điện thoại nhân viên phải bắt đầu bằng 0.

| Trường | Bắt buộc | Cho phép trùng | Quy tắc |
| --- | --- | --- | --- |
| ID | Hệ thống sinh | Không | Không cho sửa |
| Họ tên | Có | Có | 2-100 ký tự, không chỉ chứa khoảng trắng |
| Điện thoại | Có | Không | Đúng 10 chữ số, bắt đầu bằng 0; không kiểm tra đầu số nhà mạng; duy nhất, kể cả hồ sơ xóa mềm; trùng báo kiểm tra hồ sơ hiện có, không tạo hồ sơ mới |
| Email | Không | Có | Nếu nhập phải đúng định dạng, tối đa 254 ký tự |
| Ngày sinh | Không | Có | Không sau ngày hiện tại |
| Giới tính | Không | Có | Nam, Nữ, Khác; có thể không cung cấp |
| Tên gói | Có | Không | Không trùng sau chuẩn hóa khoảng trắng và bỏ phân biệt hoa/thường |
| Giá gói | Có | Có | Số nguyên lớn hơn 0 |
| Thời hạn gói | Có | Có | Số nguyên dương; đơn vị theo loại gói được chốt, không suy ra có thể tùy ý mở thêm loại gói |
| Username nhân viên | Có | Không | Duy nhất không phân biệt hoa/thường, kể cả tài khoản khóa/xóa mềm |
| Mật khẩu nhân viên | Có | Không đặt ràng buộc duy nhất | Lưu hash an toàn, không plaintext; thay đổi theo CN-36 |
| Họ tên, vai trò, trạng thái nhân viên | Có | Theo trường | Một trong năm vai trò; ACTIVE/LOCKED và dấu xóa mềm độc lập |
| Điện thoại nhân viên | Có | Không trong tập nhân viên | Đúng 10 chữ số; duy nhất kể cả khóa/xóa mềm; được trùng số hội viên |
| Email nhân viên | Không | Có | Không đặt yêu cầu duy nhất |
| start_at | Có khi PAID; không giữ chính thức khi PENDING | Có | Áp dụng bảng chọn ngày tại mục 3.1.3, bao gồm trường hợp mọi PAID cũ đã hết hạn. |
| end_exclusive | Tự động khi xác lập lịch PAID | Có | Lớn hơn start_at; cộng lịch từ start_at, chốt ngày cuối tháng khi cần; mốc độc quyền, không phải ngày cuối được tập |
| Trường số | Theo nghiệp vụ | Theo nghiệp vụ | Không nhận số âm hoặc ký tự không hợp lệ |
| Vị trí kiểm tra | Áp dụng toàn hệ thống | Không áp dụng | Kiểm tra cả giao diện và backend, không chỉ HTML validation |

Thời gian và dữ liệu sửa/hủy lượt tập phải qua toàn bộ kiểm tra tại mục 3.1.4.

### 3.5. Yêu cầu phi chức năng

| ID | Yêu cầu | Căn cứ kiểm tra | Ưu tiên |
| --- | --- | --- | --- |
| CL-01 | Quản lý tối thiểu 100 hồ sơ hội viên | Chuẩn bị ít nhất 100 hồ sơ và kiểm tra thao tác nghiệp vụ trên dữ liệu này | Must |
| CL-02 | 10 phiên hoạt động đồng thời chỉ là quy mô dự kiến, không là tiêu chí nghiệm thu bắt buộc | Không đặt SLA, ngưỡng phản hồi hoặc tỷ lệ lỗi bắt buộc | Dự kiến, không bắt buộc nghiệm thu |
| CL-03 | Local một máy chạy application server và database; Java Servlet/JSP, JDK 21, Tomcat 10.1, SQL Server 2022; Chrome/Brave bản hiện hành; giờ gym 07:00–20:00 | Check-in và tự đóng theo giờ Việt Nam như CN-13/CN-15; không có cam kết uptime hoặc cấm đăng nhập ngoài giờ | Must |
| CL-04 | Tuân thủ quyền, trạng thái tài khoản và thời hạn phiên | Kiểm tra mọi quyền tại backend kể cả URL trực tiếp; khóa giữa phiên, hết hạn 60 phút, đăng xuất/đổi session ID; tự đổi/reset mật khẩu không vượt RBAC hoặc trạng thái khóa/xóa mềm | Must |
| CL-05 | Bảo toàn dữ liệu giao dịch, Membership và lượt tập | Thanh toán/lịch tuân thủ QT-26; snapshot theo CN-25; không chồng lấn PAID hoặc có hai OPEN. Sửa/hủy lượt và lịch sử Must phải nguyên tử, rollback khi lỗi và từ chối xung đột theo CN-16. Tự đóng theo CN-15 không xử lý lặp hoặc ghi đè thay đổi hợp lệ; xóa mềm không phá liên kết. | Must |
| CL-06 | Nghiệp vụ Must hoạt động độc lập các chức năng Should/Could | Các chức năng chính vẫn hoạt động khi chưa có biên nhận, Audit Log tổng quát hoặc Excel; lịch sử CN-16 vẫn bắt buộc. | Must |
| CL-07 | Mật khẩu lưu hash an toàn, không plaintext; không lộ dữ liệu xác thực qua cookie, log, Excel | Kiểm tra các ràng buộc CN-03, CN-32, CN-33; kiểm tra lưu mật khẩu và cookie theo giao thức local; khi HTTPS phải có Secure | Must |

Không đặt SLA, uptime hoặc thời gian phản hồi tối đa. Môi trường, sao lưu thủ công và lưu giữ dữ liệu/log theo mục 2.1.

#### 3.5.1. Giao tiếp và tích hợp

| Mã | Yêu cầu | Giới hạn |
| --- | --- | --- |
| GT-01 | Giao diện web nội bộ theo vai trò; kiểm tra quyền tại backend kể cả khi gọi URL trực tiếp | Kiểm thử trên Chrome và Brave bản hiện hành, môi trường local tại 2.1; không yêu cầu HTTPS public cho demo local |
| GT-02 | Thông báo rõ khi từ chối thao tác: hết hạn gói, hội viên xóa mềm, check-in từ 20:00, check-in trùng, check-out không có lượt mở, dữ liệu không hợp lệ | Không quy định nguyên văn thông báo |
| GT-03 | Ghi nhận thanh toán thủ công bằng tiền mặt hoặc chuyển khoản; cho phép lưu ảnh minh chứng chuyển khoản tùy chọn | Không tích hợp ngân hàng; ảnh không thay thế kiểm tra tiền đã nhận |
| GT-04 | Xuất `.xlsx` theo bộ lọc hiện tại, mỗi loại dữ liệu một tệp | Could; không nhập Excel |
| GT-05 | Biên nhận HTML tối ưu in ấn; in/lưu PDF qua trình duyệt | Should; không yêu cầu thư viện sinh PDF riêng |

## 4. Điều kiện chấp nhận

Các tình huống dưới đây là tiêu chí kiểm thử, chưa phải kết quả chạy phần mềm. Must phải đáp ứng cả điều kiện ở mục 3 và các tình huống liên quan. KT-24–KT-26 cùng các nhánh Should/Could chỉ áp dụng khi chức năng tương ứng được triển khai. CL-02 không là điều kiện nghiệm thu bắt buộc.

### 4.1. Truy cập, phân quyền và nhân viên

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-01 | Tài khoản khóa/xóa đăng nhập mới | Bị từ chối | CN-01, CN-04 |
| KT-02 | Truy cập URL ngoài quyền; Manager thay quyền quản trị; tự nâng quyền/tự xóa | Bị từ chối ở backend | CN-04, CN-05 |
| KT-17 | Phiên không hoạt động đủ 60 phút; request hợp lệ trước hạn; đăng xuất | Hết hạn phải đăng nhập lại; request hợp lệ tính lại timeout; đăng xuất hủy phiên | CN-02, CN-03 |
| KT-18 | Khóa hoặc xóa mềm tài khoản đang có phiên; sau đó mở khóa tài khoản khóa | Request bảo vệ tiếp theo bị chặn/hủy phiên; mở khóa không khôi phục phiên | CN-03, CN-04 |
| KT-19 | Đăng nhập và kiểm tra cookie trên môi trường local; kiểm tra thêm nếu dùng HTTPS | Session ID được thay; HttpOnly/SameSite phù hợp, Secure khi HTTPS; phiên hoạt động đúng giao thức local, không chứa mật khẩu; không bắt buộc HTTPS public | CN-01, CN-03, CL-07 |
| KT-29 | Xóa mềm tài khoản có giao dịch/lượt/log và phiên đang tồn tại | Giữ bản ghi/liên kết lịch sử, không phá khóa ngoại; không đăng nhập mới, request bảo vệ tiếp theo bị chặn/hủy phiên | CN-04, CN-25 |
| KT-37 | Với từng ô ma trận 2.3, thử qua giao diện và request trực tiếp, gồm tự đổi/reset mật khẩu; đặc biệt Manager quản lý Administrator/Owner/Manager khác, đổi vai trò/reset, tạo loại gói | Quyền C được phép trong phạm vi và điều kiện nghiệp vụ; K/— bị chặn không đổi dữ liệu; Mở khóa không khôi phục phiên; Manager chỉ sửa tên/giá, không cấp Manager tạo loại gói; không cấp ngầm Administrator/Owner; Biên nhận/Excel chỉ kiểm tra khi Should/Could triển khai, không nâng ưu tiên | CN-04, CN-05, CN-09, CN-31, CN-32, CN-34–CN-36 |
| KT-42 | Tạo/sửa nhân viên thiếu trường bắt buộc; username An/an; điện thoại 9/11 số hoặc ký tự; số trùng nhân viên đang hoạt động/khóa/xóa mềm; số chỉ trùng hội viên; email trống/trùng | Thiếu trường, username trùng khác hoa/thường, điện thoại sai hoặc trùng trong tập nhân viên bị từ chối; số đúng 10 chữ số chỉ trùng hội viên được nhận; email tùy chọn và không yêu cầu duy nhất; mật khẩu lưu hash an toàn, không plaintext | CN-01, CN-04, CN-35, DL-07, QT-27 |
| KT-45 | Mỗi vai trò tự đổi mật khẩu sau xác thực; Administrator reset tài khoản khác trong phạm vi; Manager/Owner/Receptionist/Accountant thử reset; thử tự đổi khi khóa/xóa mềm/phiên không hợp lệ | Tự đổi hợp lệ và Administrator reset được phép; các vai trò khác reset bị chặn; khóa/xóa mềm hoặc xác thực không hợp lệ bị chặn; mật khẩu mới lưu hash, mật khẩu cũ không đăng nhập được, mới đăng nhập được nếu tài khoản hợp lệ; không có email/OTP/khôi phục tự động | CN-01, CN-04, CN-05, CN-34, CN-36, DL-07, QT-27, QT-28 |

### 4.2. Hội viên và dữ liệu đầu vào

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-03 | Tạo, tìm theo mã/tên/điện thoại, cập nhật hồ sơ | Dữ liệu lưu đúng; mã/điện thoại chỉ khớp chính xác, nhập một phần không trả kết quả khớp; tên tìm một phần không phân biệt hoa/thường; điện thoại trùng không tạo mới | CN-06 đến CN-08 |
| KT-16 | Dữ liệu sai; số đúng 10 chữ số bắt đầu 0 nhưng không thuộc đầu số nhà mạng; giới tính để trống/Nam/Nữ/Khác | Dữ liệu sai bị chặn cả hai phía; số đúng cấu trúc, duy nhất không bị từ chối vì đầu số; giới tính tùy chọn đúng danh sách; email trống/trùng hợp lệ được nhận | CN-06, CN-30 |
| KT-35 | Owner/Manager xóa mềm hội viên có PAID và PENDING; thử tạo đăng ký, check-in, xác nhận PENDING bằng Manager/Receptionist; sau đó hủy PENDING thủ công | Giữ lịch sử/liên kết/PAID cũ; chặn đăng ký/check-in/PAID; PENDING không tự hủy, không phát sinh lịch/doanh thu; Manager/Receptionist hủy được cùng đăng ký; tổng hội viên gồm xóa mềm và lọc được trạng thái | CN-08, CN-10, CN-13, CN-21, CN-22, CN-28 |
| KT-38 | Có hồ sơ chưa mua gói, hồ sơ có PAID tương lai, hồ sơ hết gói; xóa mềm một hồ sơ | Tình trạng hồ sơ độc lập tình trạng Membership; created_at giữ nguyên; hội viên mới theo ngày tạo, tổng gồm hồ sơ xóa mềm | CN-06, CN-08, CN-12, CN-28 |

### 4.3. Gói tập, thanh toán và xếp lịch

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-04 | Tạo nhiều đăng ký PENDING, chưa xác nhận đủ tiền | Không cấp quyền tập hoặc giữ lịch chính thức; không tính doanh thu/lượt mua thành công | CN-10, CN-11, CN-22 |
| KT-12 | Xác nhận tiền mặt và chuyển khoản cho đăng ký/gia hạn | Đủ dữ liệu, PAID và paid_at; ảnh tùy chọn không thay việc kiểm tra tiền | CN-21 đến CN-23 |
| KT-13 | Thử xóa lịch sử, hủy/hoàn tiền/sửa tiền hoặc trạng thái PAID | Không cho thao tác; giữ nguyên lịch sử và số tiền | CN-23, CN-25 |
| KT-28 | Monthly/6-Month/Yearly đang hiệu lực có end_exclusive 15/11/2026 00:00; kiểm tra ngày 07/11, 08/11, 14/11, 15/11; lặp với PAID nối tiếp, Daily, PAID tương lai | Không cảnh báo ngày 07/11 (8 ngày); cảnh báo 08/11 (7 ngày) và 14/11 (1 ngày) nếu không PAID nối tiếp; có PAID nối tiếp thì không cảnh báo; Daily không cảnh báo 7 ngày; PAID tương lai khác đang hiệu lực, tại end_exclusive gói cũ hết hiệu lực | CN-12, QT-24 |
| KT-30 | Chuỗi PAID còn hiệu lực kết thúc 01/11/2026 00:00; tạo A Monthly PENDING trước B Daily PENDING, xác nhận B trước A | Trước PAID cả A/B không giữ lịch; B được xếp `[01/11, 02/11)`; A được xếp `[02/11, 02/12)`; gói cũ không mất thời gian; lịch theo thứ tự PAID, không thứ tự tạo | CN-10, CN-11, CN-22 |
| KT-31 | Monthly từ 31/01/2027 00:00 rồi Monthly nối tiếp; Yearly từ 29/02/2028 | Các mốc lần lượt 28/02/2027, 28/03/2027; Yearly kết thúc độc quyền 28/02/2029; không khôi phục mốc 31; đối chiếu mục 3.1.3 | CN-11, CN-12 |
| KT-32 | Hai PENDING khác nhau cùng hội viên được xác nhận đồng thời; gửi lại xác nhận PAID | Hai gói nối đúng end_exclusive không chồng lấn theo thứ tự hệ thống xác lập, giữ rolling; doanh thu/lần mua ghi một lần mỗi giao dịch; thứ tự lần PAID dùng nhất quán để phân loại mới/gia hạn; gửi lại bị từ chối, không đổi paid_at/tiền/lịch/doanh thu/lịch sử | CN-11, CN-21, CN-22, CN-25–CN-29, QT-26 |
| KT-33 | Tạo giao dịch PENDING giá 500.000, đổi giá loại gói thành 600.000, tạo giao dịch mới rồi thanh toán cả hai | Giao dịch cũ vẫn 500.000, mới 600.000; xem/biên nhận khi có/báo cáo không tính lại tiền cũ | CN-09, CN-21, CN-25 |
| KT-34 | Manager và Receptionist lần lượt hủy PENDING; thử hủy PAID và thử hủy bằng Administrator/Owner/Accountant | Hai vai trò được phép hủy đồng thời giao dịch PENDING và đăng ký liên quan, không lịch/hiệu lực/doanh thu; mọi thao tác hủy PAID hoặc từ vai trò không được cấp bị từ chối | CN-21, CN-22, CN-34 |
| KT-39 | Monthly `[15/10/2026 00:00, 15/11/2026 00:00)`; Daily 15/10; 6-Month/Yearly cùng ngày bắt đầu | Ngày cuối lần lượt 14/11, 15/10, 14/04/2027, 14/10/2027; tại end_exclusive gói cũ không cấp quyền; check-in vẫn chịu giờ 20:00 | CN-11, CN-12, CN-13 |
| KT-40 | Chưa từng có PAID; tạo PENDING trước ngày thanh toán; xác nhận lúc 10:30 ngày 15/10/2026 với mặc định, chọn 17/10 hoặc chọn 14/10 | PENDING không giữ lịch; mặc định start_at 15/10 00:00, tương lai 17/10 00:00, paid_at thực tế 15/10 10:30; ngày quá khứ bị từ chối, giao dịch còn PENDING không có lịch/doanh thu; gói tương lai chưa được check-in, gói hôm nay chỉ được check-in sau PAID và thỏa CN-13 | CN-11, CN-22, QT-25 |
| KT-41 | Có chuỗi PAID còn hiệu lực hoặc chỉ đang chờ hiệu lực, cuối chuỗi 01/11/2026 00:00; xác nhận gói mới và thử chọn 05/11 | Lịch hợp lệ bắt buộc bắt đầu 01/11 00:00; không chấp nhận lịch 05/11 tạo khoảng trống; end_exclusive mới cộng rolling, thứ tự theo PAID không theo tạo PENDING | CN-11, CN-22, QT-18, QT-19, QT-25 |
| KT-43 | Tạo một PENDING và một PAID tên gói A/giá 500.000; Owner/Manager đổi tên B/giá 600.000; Manager/Receptionist tạo đăng ký mới, thanh toán PENDING cũ; thử sửa thời hạn/ngừng dùng | PENDING/PAID cũ giữ tên A, giá 500.000 và thời hạn đã lưu; thanh toán không tính lại từ cấu hình; mới dùng B/600.000; không cho sửa thời hạn hoặc ngừng dùng; biên nhận/Excel khi triển khai dùng snapshot | CN-09, CN-10, CN-25, CN-31, CN-32, QT-20 |
| KT-44 | Hai request cùng xác nhận một PENDING; xác nhận lại PAID hoặc CANCELLED; gián đoạn/lỗi trong thao tác xác nhận và cập nhật lịch | Đúng một request chuyển PAID thành công; request trùng/không còn PENDING bị từ chối, không đổi paid_at/số tiền/lịch/doanh thu/lịch sử; lỗi không để lại PAID thiếu lịch hoặc lịch của giao dịch còn PENDING, không ghi doanh thu/lần mua một phần | CN-21, CN-22, CN-25, CL-05, QT-26 |
| KT-48 | Tất cả PAID đã hết hạn (kể cả đúng end_exclusive cuối cùng); xác nhận ngày 15/10/2026, mặc định hoặc chọn 17/10; thử ngày quá khứ | Tạo chuỗi mới bắt đầu 15/10 hoặc 17/10 lúc 00:00; không nối vào cuối chuỗi cũ trong quá khứ. Ngày quá khứ bị từ chối. paid_at là lúc xác nhận; lần mua vẫn là gia hạn theo toàn bộ lịch sử. | CN-11, CN-21, CN-22, CN-29, QT-18, QT-25 |
| KT-52 | Hai PENDING cùng hội viên xác nhận đồng thời khi chưa từng PAID, khi tất cả PAID hết hạn, và khi chỉ có PAID tương lai | Trường hợp tạo chuỗi mới: giao dịch được hệ thống xác nhận trước áp dụng ngày mặc định/tương lai; giao dịch sau nối cuối chuỗi vừa tạo. Trường hợp có PAID tương lai: cả hai nối lần lượt cuối chuỗi. Không chồng lấn, không ghi một phần hoặc đếm trùng; thứ tự phân loại lần mua thống nhất. | CN-11, CN-21, CN-22, CN-29, QT-26 |

### 4.4. Lượt tập

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-05 | Check-in không có hồ sơ, hồ sơ xóa mềm, PENDING, PAID tương lai hoặc hết hạn; check-in trước 07:00 | Từ chối từng trường hợp, không tạo lượt | CN-08, CN-12, CN-13 |
| KT-06 | Gói đã hết hạn trước thời điểm check-in | Từ chối, thông báo cần gia hạn | CN-12, CN-13 |
| KT-07 | Manager/Receptionist check-in hợp lệ không trước paid_at, rồi check-out sau check-in và trước 20:00 cùng ngày; Owner/Manager/Receptionist tra cứu toàn bộ lịch sử | Lưu thời gian/người thực hiện và gói gắn với lượt; chuyển OPEN sang COMPLETED, xem được lịch sử. Check-in trước paid_at bị từ chối. | CN-14 đến CN-16 |
| KT-08 | Check-in khi đang có OPEN, kể cả hai yêu cầu đồng thời | Không tạo thêm lượt; tối đa một OPEN | CN-13, CN-14 |
| KT-20 | Check-out khi không có OPEN | Từ chối, thông báo phù hợp | CN-15 |
| KT-21 | Check-in lúc 19:59:59, 20:00 và sau 20:00; check-out OPEN tại các mốc tương ứng; check-out cạnh tranh với tự đóng lúc 20:00 | Check-in đủ điều kiện và check-out hợp lệ trước 20:00 được chấp nhận. Từ 20:00 chặn check-in/check-out thủ công; OPEN thành AUTO_CLOSED với kết thúc 20:00 ngày check-in, không đổi hạn gói. Không tạo COMPLETED từ 20:00, không đóng lặp hoặc ghi đè lượt đã kết thúc hợp lệ. Xử lý gián đoạn theo KT-46. | CN-13, CN-15 |
| KT-22 | Owner/Manager/Receptionist sửa/hủy lượt sai; thiếu lý do; thử bằng Administrator/Accountant | Ba vai trò được cấp chỉ thực hiện thao tác hợp lệ theo CN-16; thiếu lý do không hoàn tất. Vai trò không có quyền bị chặn; trước/sau được giữ kể cả chưa có CN-33. | CN-16, CN-34 |
| KT-23 | Hội viên ra/vào nhiều lần trong hạn, đã kết thúc lượt trước | Chấp nhận lượt mới; kiểm tra cả gói 1 ngày, 1 tháng, 6 tháng và 1 năm | CN-13 đến CN-15 |
| KT-46 | OPEN ngày 15/10/2026, server tắt trước 20:00 rồi khởi động lúc 21:00 hoặc ngày sau; có COMPLETED, AUTO_CLOSED, CANCELLED và OPEN đã sửa giờ hợp lệ; chạy lại xử lý bù, kể cả cạnh tranh sửa/hủy | Chỉ OPEN quá hạn thành AUTO_CLOSED, kết thúc 15/10/2026 20:00, ghi hệ thống thực hiện; không dùng giờ khởi động hoặc đổi hạn Membership. Giữ nguyên COMPLETED/AUTO_CLOSED/CANCELLED, không xử lý lặp hoặc ghi đè thay đổi hợp lệ. Kiểm tra lại dữ liệu/trạng thái hiện hành; OPEN chưa tới giờ không đóng sớm. | CN-15, DL-04, QT-15, CL-05 |
| KT-49 | Sửa/hủy từng trạng thái OPEN, COMPLETED, AUTO_CLOSED, CANCELLED; thử đổi trạng thái qua sửa | Chỉ sửa giờ trong ngày ban đầu: OPEN sửa check-in; COMPLETED sửa check-in/check-out nhưng check-out phải sau check-in và trước 20:00; AUTO_CLOSED sửa check-in và giữ check-out 20:00. Sửa không đổi trạng thái. Ba trạng thái này được hủy sang CANCELLED, không xóa vật lý; lưu đủ lịch sử. CANCELLED chỉ đọc, không sửa/hủy lại/khôi phục và không tính lượt hợp lệ. | CN-16, DL-04, QT-14 |
| KT-50 | Sửa check-in tại 06:59:59, 07:00, 19:59:59, 20:00; ngoài hạn gói, trước/bằng paid_at, gói PENDING; check-out bằng/trước check-in, tại 19:59:59, 20:00 và sau 20:00; đổi ngày của một hoặc cả hai mốc, sửa sang tương lai | Chấp nhận chỉ khi thỏa toàn bộ mục 3.1.4; check-in bằng paid_at hợp lệ nếu đủ điều kiện khác. COMPLETED chỉ chấp nhận check-out sau check-in, trước 20:00 cùng ngày ban đầu và không trong tương lai; từ chối check-out bằng hoặc sau 20:00. AUTO_CLOSED giữ nguyên đúng 20:00, từ chối mọi sửa check-out. Từ chối đổi ngày, kể cả cả hai mốc cùng chuyển sang ngày khác, và mọi vi phạm khác; giữ nguyên lượt/gói đã gắn. | CN-16, DL-04, QT-14 |
| KT-51 | Sửa gây chồng lấn (kể cả OPEN), đổi hội viên/gói/người tạo/trạng thái; thiếu dữ liệu lịch sử; gây lỗi khi cập nhật lượt hoặc ghi lịch sử; hai thao tác sửa/sửa, sửa/hủy, hủy/hủy cùng lượt hoặc sửa các lượt gây chồng lấn | Từ chối thay đổi vi phạm. Kiểm tra lại trạng thái/dữ liệu hiện hành; thao tác xung đột bị từ chối, không ghi đè thay đổi đã lưu. Lỗi hoặc thiếu lý do/người/thời điểm/trước-sau phải rollback toàn bộ cập nhật lượt và lịch sử của thao tác. Thành công giữ đúng ngày, không chồng lấn, tối đa một OPEN và có lịch sử Must dù chưa có CN-33; CANCELLED không tính lượt hợp lệ. | CN-16, DL-04, QT-03, QT-14, CL-05 |

### 4.5. Báo cáo, biên nhận và xuất dữ liệu

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-14 | Tạo giao dịch ở kỳ A, thanh toán kỳ B; lọc ngày đầu/cuối | Chỉ tính doanh thu kỳ B; gồm trọn ngày cuối, loại thời điểm bắt đầu ngày kế tiếp; loại PENDING/CANCELLED | CN-24, CN-26, CN-27 |
| KT-15 | Một hồ sơ mới, ba lần mua PAID; thêm giao dịch PENDING/CANCELLED | Hội viên mới 1, đăng ký mới 1, gia hạn 2; doanh thu chỉ tổng ba PAID | CN-28, CN-29 |
| KT-24 | Receptionist/Accountant xem/in lại PAID; thử PENDING và ba vai trò còn lại | Hai vai trò được phép có biên nhận đúng snapshot tên/giá và lịch đã lưu; in lại không tạo giao dịch; PENDING và vai trò không có quyền bị từ chối | CN-31, Should |
| KT-25 | Administrator/Owner/Manager xuất từng loại Excel; Accountant thử cả bốn; Receptionist thử xuất; dữ liệu rỗng | Ba vai trò đầu xuất cả bốn loại; Accountant chỉ giao dịch/doanh thu, Receptionist bị chặn; đúng bộ lọc/cột và snapshot tên/giá, tiền dạng số; rỗng thì thông báo, không xuất bí mật | CN-32, Could |
| KT-36 | Có PENDING/CANCELLED trước lần PAID đầu tiên, lần PAID thứ hai ở kỳ sau; lọc giao dịch mỗi trạng thái | PAID đầu vẫn đăng ký mới, PAID sau là gia hạn kể cả lọc riêng kỳ sau; PENDING/CANCELLED lọc created_at, PAID/doanh thu theo paid_at | CN-24, CN-26, CN-29 |

### 4.6. Nhật ký và môi trường

| ID | Tình huống | Kết quả cần đạt | Yêu cầu |
| --- | --- | --- | --- |
| KT-26 | Thao tác quan trọng và thử truy cập/sửa/xóa Audit Log | Log đủ dữ liệu, phân biệt hệ thống/nhân viên; chỉ Administrator xem; không sửa/xóa qua giao diện thường; không ghi bí mật | CN-33, Should |
| KT-27 | Quản lý ít nhất 100 hồ sơ trên local JDK 21/Tomcat 10.1/SQL Server 2022, Chrome và Brave hiện hành | Kiểm tra nghiệp vụ trên tối thiểu 100 hồ sơ; không yêu cầu chạy/đạt 10 phiên đồng thời để nghiệm thu | CL-01, CL-02 |
| KT-47 | Kiểm tra cấu hình môi trường và thao tác sao lưu trước demo, chính sách lưu dữ liệu/log | JDK 21/Tomcat 10.1/SQL Server 2022 cùng máy local; kiểm tra trên Chrome/Brave và ghi phiên bản thực tế; có bản sao lưu thủ công trước demo; không cấu hình tự động xóa dữ liệu/log; không yêu cầu load test 10 phiên | GT-01, CL-02, CL-03 |

## 5. Phụ lục

### 5.1. Tham chiếu quyết định

Các mã XN, P, TBD và EDGE chỉ phục vụ truy vết các quyết định đã tích hợp. TBD là mã lịch sử, không biểu thị yêu cầu còn chờ xác nhận. Toàn bộ nội dung cần triển khai được quy định trong tài liệu này; không cần đọc lại tài liệu trao đổi trước đó.

- XN-01–XN-13: giải đáp nghiệp vụ từ `XN.pdf`; phạm vi tương ứng được thể hiện trong các nhóm yêu cầu.
- Xác nhận danh mục ngày 15/10/2026: bốn thời hạn tại mục 3.1.3.
- Case 01: quy tắc sửa/hủy lượt tại CN-16, DL-04, QT-14; kiểm tra KT-22, KT-49–KT-51.
- Case 02: chọn chuỗi và xác nhận PAID tại CN-11, CN-21, CN-22, QT-18, QT-25, QT-26; kiểm tra KT-30–KT-32, KT-40, KT-41, KT-44, KT-48, KT-52.

| Mã quyết định | Yêu cầu áp dụng | Kiểm tra |
| --- | --- | --- |
| P-01 | CN-04, CN-34, DL-07, QT-07, QT-08 | KT-18, KT-37 |
| P-02 | CN-08, DL-01, QT-22 | KT-35, KT-37 |
| P-03 | CN-09, DL-02, DL-03, DL-06, QT-20 | KT-33, KT-37, KT-43 |
| P-04 | CN-10, CN-11, DL-03, DL-06, QT-06 | KT-04, KT-37 |
| P-05 | CN-21, CN-22, DL-03, DL-06, QT-22, QT-26 | KT-12, KT-35, KT-37, KT-44 |
| P-06 | CN-21, CN-22, DL-03, DL-06, QT-21 | KT-34, KT-35, KT-37 |
| P-07 | CN-23, CN-24, DL-06, QT-08, QT-13 | KT-14, KT-36, KT-37 |
| P-08 | CN-13–CN-15, DL-04, QT-01, QT-03 | KT-07, KT-21, KT-37 |
| P-09 | CN-31, DL-03, DL-06, QT-16 | KT-24, KT-37 |
| P-10 | CN-32, DL-01, DL-03, DL-06, QT-17 | KT-25, KT-37 |
| P-11 | CN-16, DL-04, QT-08, QT-14 | KT-07, KT-22, KT-37 |
| TBD-01 | CN-11, CN-22, DL-03, DL-06, QT-18, QT-19, QT-25 | KT-30, KT-31, KT-39, KT-40, KT-41 |
| TBD-02 | CN-01, CN-04, CN-35, DL-07, QT-27 | KT-42 |
| TBD-03 | CN-07, DL-01, QT-10 | KT-03 |
| TBD-04 | CN-09, CN-10, CN-25, DL-02, DL-03, DL-06, QT-20 | KT-33, KT-43 |
| TBD-05 | CN-12, DL-03, QT-24 | KT-28 |
| TBD-06 | CN-15, DL-04, QT-15 | KT-21, KT-46 |
| TBD-07 | GT-01, CL-02, CL-03, DL-01–DL-09 còn trong phạm vi | KT-19, KT-27, KT-47 |
| TBD-08 | CN-08, CN-21, CN-22, DL-01, DL-03, DL-06, QT-22 | KT-35 |
| EDGE-01 | CN-10–CN-13, CN-22, DL-03, DL-06, QT-06, QT-25 | KT-04, KT-05, KT-40 |
| EDGE-02 | CN-11, CN-22, DL-03, QT-18, QT-19, QT-25 | KT-30, KT-31, KT-41 |
| EDGE-03 | CN-01, CN-04, CN-35, CN-36, DL-07, QT-27 | KT-42, KT-45 |
| EDGE-04 | CN-09, CN-10, CN-25, CN-31, CN-32, DL-03, DL-06, QT-20 | KT-24, KT-25, KT-33, KT-43 |
| EDGE-05 | CN-11, CN-21, CN-22, CN-25–CN-29, DL-03, DL-06, QT-18, QT-23, QT-26, CL-05 | KT-32, KT-44 |
| EDGE-06 | CN-04, CN-05, CN-34, CN-36, DL-07, QT-07, QT-08, QT-28, CL-04, CL-07 | KT-37, KT-45 |

### 5.2. ID đã loại bỏ

Các mã sau giữ nguyên để truy vết, không triển khai hoặc tái sử dụng.

| ID | Nội dung đã loại bỏ | Trạng thái |
| --- | --- | --- |
| CN-17 | Quản lý sản phẩm, giá và tồn | OUT OF SCOPE |
| CN-18 | Tạo đơn bán sản phẩm | OUT OF SCOPE |
| CN-19 | Kiểm tra tồn trước khi bán | OUT OF SCOPE |
| CN-20 | Giảm tồn khi bán thành công | OUT OF SCOPE |
| DL-05 | Sản phẩm và kho | OUT OF SCOPE |
| DL-08 | Đơn bán sản phẩm | OUT OF SCOPE |
| QT-04 | Quy tắc không bán vượt tồn đã loại bỏ theo XN-07 | OUT OF SCOPE |
| QT-05 | Quy tắc giảm tồn/không âm đã loại bỏ theo XN-07 | OUT OF SCOPE |
| KT-09 | Kiểm tra bán vượt tồn | OUT OF SCOPE |
| KT-10 | Kiểm tra giảm tồn | OUT OF SCOPE |
| KT-11 | Kiểm tra bán đồng thời | OUT OF SCOPE |