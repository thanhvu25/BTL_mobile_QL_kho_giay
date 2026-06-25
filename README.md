Xây dựng ứng dụng QUẢN LÝ KHO HÀNG GIÀY THỂ THAO

1. Giới thiệu đề tài

Đề tài Xây dựng ứng dụng quản lý kho hàng giày thể thao được thực hiện nhằm hỗ trợ người dùng quản lý thông tin hàng hóa trong kho một cách nhanh chóng, chính xác và thuận tiện trên thiết bị Android.

Ứng dụng tập trung vào các nghiệp vụ chính như đăng ký, đăng nhập, quản lý sản phẩm, quản lý loại sản phẩm, quản lý nhà cung cấp, nhập kho, xuất kho và thống kê tồn kho. Dữ liệu được lưu trữ bằng cơ sở dữ liệu SQLite, giúp ứng dụng có thể hoạt động độc lập mà không cần kết nối Internet.

2. Mục tiêu đề tài

* Xây dựng ứng dụng quản lý kho giày thể thao trên nền tảng Android.
* Hỗ trợ quản lý thông tin sản phẩm, loại sản phẩm và nhà cung cấp.
* Hỗ trợ nghiệp vụ nhập kho và xuất kho.
* Tự động cập nhật số lượng tồn kho sau khi nhập hoặc xuất hàng.
* Hiển thị thống kê tổng số sản phẩm, tổng số lượng tồn kho và sản phẩm sắp hết hàng.
* Lưu trữ dữ liệu bằng SQLite.
* Thiết kế giao diện đơn giản, dễ sử dụng và phù hợp với thiết bị di động.

3. Dataset / Dữ liệu sử dụng

Ứng dụng không sử dụng dataset lớn từ bên ngoài. Dữ liệu được người dùng nhập trực tiếp trong quá trình sử dụng và được lưu vào cơ sở dữ liệu SQLite nội bộ.

Các bảng dữ liệu chính:

| Bảng       | Mô tả                                |
| ---------- | ------------------------------------ |
| TaiKhoan   | Lưu thông tin tài khoản đăng nhập    |
| LoaiSP     | Lưu thông tin loại sản phẩm          |
| NhaCungCap | Lưu thông tin nhà cung cấp           |
| SanPham    | Lưu thông tin sản phẩm giày thể thao |
| NhapKho    | Lưu thông tin phiếu nhập kho         |
| XuatKho    | Lưu thông tin phiếu xuất kho         |

Tài khoản mẫu:

* Tên đăng nhập: admin

* Mật khẩu: 123


4. Pipeline xử lý

Quy trình hoạt động chính của ứng dụng:


Người dùng đăng nhập

        ↓
Truy cập trang chủ

        ↓
Quản lý dữ liệu danh mục

        ↓
Thêm / sửa / xóa / tìm kiếm sản phẩm

        ↓
Thực hiện nhập kho hoặc xuất kho

        ↓
Cập nhật số lượng tồn kho trong SQLite

        ↓
Hiển thị thống kê tồn kho


====================================================================

Pipeline chi tiết:

a. Tiền xử lý dữ liệu nhập
   * Kiểm tra dữ liệu người dùng nhập.
   * Không cho phép bỏ trống các trường bắt buộc.
   * Kiểm tra số lượng và đơn giá phải hợp lệ.

b. Lưu trữ dữ liệu
   * Dữ liệu được lưu vào SQLite.
   * Các bảng có khóa chính, khóa ngoại và ràng buộc dữ liệu.

c. Xử lý nghiệp vụ
   * Khi nhập kho, hệ thống cộng thêm số lượng tồn.
   * Khi xuất kho, hệ thống trừ số lượng tồn.
   * Không cho phép xuất kho nếu số lượng tồn không đủ.

d. Thống kê
   * Tính tổng số sản phẩm.
   * Tính tổng số lượng tồn kho.
   * Liệt kê sản phẩm sắp hết hàng.

5. Mô hình / Kiến trúc sử dụng

Ứng dụng sử dụng mô hình xử lý nghiệp vụ kết hợp SQLite:

* Model: Các lớp dữ liệu như SanPham, LoaiSP, NhaCungCap, NhapKho, XuatKho.
* View: Các file giao diện XML trong Android.
* Controller / Activity: Các Activity xử lý thao tác người dùng và cập nhật dữ liệu.
* DatabaseHelper: Lớp tạo và quản lý cơ sở dữ liệu SQLite.

Lý do lựa chọn SQLite:

* Nhẹ, dễ tích hợp trong Android.
* Không cần máy chủ riêng.
* Phù hợp với ứng dụng quản lý dữ liệu cục bộ.
* Dễ triển khai trong phạm vi bài tập lớn.

6. Kết quả đạt được

Ứng dụng đã hoàn thành các chức năng chính:

* Đăng ký tài khoản.
* Đăng nhập hệ thống.
* Đăng xuất.
* Quản lý sản phẩm.
* Quản lý loại sản phẩm.
* Quản lý nhà cung cấp.
* Quản lý nhập kho.
* Quản lý xuất kho.
* Tìm kiếm dữ liệu.
* Thống kê tồn kho.
* Hiển thị sản phẩm sắp hết hàng.

7. Hướng dẫn cài đặt môi trường

Cần cài đặt:
* Android Studio.
* JDK 17 hoặc phiên bản tương thích.
* Android SDK.
* Máy ảo Android hoặc thiết bị Android thật.

8. Hướng dẫn chạy dự án

Bước 1: Clone project từ GitHub

```bash
git clone https://github.com/ten-tai-khoan/QLKhoGiayTheThao.git
```

Bước 2: Mở project

Mở Android Studio, chọn:

```text
File → Open → chọn thư mục QLKhoGiayTheThao
```

Bước 3: Đồng bộ Gradle

Chờ Android Studio tải và đồng bộ Gradle. Nếu có thông báo yêu cầu Sync, chọn:

```text
Sync Now
```

Bước 4: Chạy ứng dụng

Chọn máy ảo Android hoặc điện thoại thật, sau đó nhấn nút:

```text
Run
```

Bước 5: Đăng nhập

Sử dụng tài khoản mẫu:

```text
Tên đăng nhập: admin
Mật khẩu: 123
```

9. Chạy demo nhanh

Vào thư mục `demo/` để xem hướng dẫn demo. Quy trình demo đề xuất:

1. Đăng nhập vào hệ thống.
2. Thêm loại sản phẩm.
3. Thêm nhà cung cấp.
4. Thêm sản phẩm.
5. Thực hiện nhập kho.
6. Thực hiện xuất kho.
7. Kiểm tra thống kê tồn kho.

10. Cấu trúc thư mục dự án

QLKhoGiayTheThao/
│
├── app/                  # Source code chính của ứng dụng Android
├── demo/                 # Hướng dẫn demo hoặc ảnh minh họa chạy ứng dụng
├── data/                 # Mô tả dữ liệu mẫu và cơ sở dữ liệu SQLite
├── reports/              # Báo cáo Word/PDF
├── slides/               # Slide thuyết trình PPTX/PDF
├── requirements.txt      # Môi trường cần thiết để chạy dự án
├── README.md             # Hướng dẫn sử dụng dự án
└── .gitignore            # Các file/thư mục không đưa lên GitHub

11. Tác giả

| Họ tên              | Mã sinh viên | Lớp              |
| ------------------- | ------------ | ---------------- |
| Nguyễn Thị Thu Sang | 12523073     | 12523T.1         |
| Vũ Vy Thanh         | 10123295     | 12523T.1         |

12. Công nghệ sử dụng

* Ngôn ngữ lập trình: Java.
* IDE: Android Studio.
* Cơ sở dữ liệu: SQLite.
* Giao diện: XML Layout.
* Nền tảng: Android.
