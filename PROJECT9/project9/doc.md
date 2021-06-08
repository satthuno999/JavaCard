# PROJECT9 - Xây dựng thẻ cán bộ nhân viên sử dụng Smart card
Author: [Vu Xuan Binh](http://facebook.com/xuanbinh.vu.6464)
> Version 0.1 08/06/2021
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
  * 

  

