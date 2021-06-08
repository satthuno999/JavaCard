# PROJECT9 - Xây dựng thẻ cán bộ nhân viên sử dụng Smart card
Author: [Vu Xuan Binh](http://facebook.com/xuanbinh.vu.6464) :scorpius:	
> Version 0.1 08/06/2021 :+1:
1. [ Mô tả chức năng dự kiến](#mô-tả-chức-năng-dự-kiến)
2. [ Mô tả chi tiết](#mô-tả-chi-tiết-các-chức-năng)
3. [ Demo Code](#demo-code)
***
***
### Mô tả chức năng dự kiến: 
* **Có cơ chế xác thực thẻ bằng mã PIN, mã PIN có thể thay đổi, có giới hạn số lần nhập sai cho phép**
* **Lưu thông tin cá nhân của chủ thẻ (mã số, họ tên, ngày sinh, tên cơ quan, ảnh cá nhân, số điện thoại…) Các thông tin trước khi lưu phải được mã hóa (sử dụng AES với khóa được tạo ra từ mã PIN). Có thể thay đổi thông tin người dùng.**
* **Có chức năng điểm danh khi đến và khi về. Khi điểm danh xác thực bằng mật mã khóa công khai (RSA hoặc ECC). Có chức năng mở cửa cơ quan sử dụng mật mã khóa công khai (RSA hoặc ECC)**
* **Xây dựng phần mềm giao tiếp với thẻ trên máy tính để demo các chức năng**
---
### Mô tả chi tiết các chức năng:
1. **Cơ chế xác thực thẻ bằng mã pin - VERIFY_PIN**
  * Lưu trạng thái đăng nhập
  * <sau khi kết nối thẻ, yêu cầu VERIFY_PIN nếu nhập sai quá số lần cho phép sẽ khoá thẻ>
2. **Thay đổi mã pin - CHANGE_PIN**
  * Sau khi đăng nhập thẻ thành công có thể chọn chức năng thay đổi mã PIN
  * Yêu cầu nhập mã pin cũ và mới (khoá thẻ nếu số lần nhập mã pin cũ vượt quá số lần cho phép)
3. **Giới hạn số lần nhập sai đối với từng mã pin**
  * Khi *CREATE_PIN* sẽ truyền thêm tham số là số lần nhập sai cho phép
  * Các pin khi bị khoá sẽ lưu lại phục vụ cho mở khoá thẻ và thay đổi pin (không trùng với mã pin cũ)
4. **Lưu thông tin cá nhân của chủ thẻ**
  * Tạo thông tin cá nhân cho lần sử dụng thẻ đầu tiên
5. **Cho phép thay đổi thông tin cá nhân**
  * Sau khi đăng nhập thẻ thành công cho phép chọn chức năng thay đổi thông tin cá nhân
  * Hiển thị thông tin cũ và cho phép cập nhật thông tin mới
6. **Mã hoá thông tin trước khi lưu xuống thẻ -  sử dụng khoá AES**
7. **Chức năng điểm danh**
 * Có chức năng điểm danh khi đến và về
 * Xác thực bằng mã khoá công khai ECC
9. **Dựng phần mềm giao tiếp trên máy tính**
---
### Demo code

