# OneShop - Hệ thống Thương mại Điện tử

## 📋 Giới thiệu

OneShop là một nền tảng thương mại điện tử được xây dựng bằng **Spring Boot** và **Thymeleaf**, cho phép người dùng mua sắm trực tuyến với đầy đủ các tính năng của một hệ thống e-commerce hiện đại.

## 🛠 Công nghệ sử dụng

- **Backend**: Spring Boot 3.x, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript
- **Database**: SQL Server
- **Authentication**: JWT Token, Spring Security
- **Payment Integration**: VNPay, COD
- **Email Service**: Spring Mail (gửi OTP)
- **Build Tool**: Maven

---

## ✨ Tính năng chung (Guest & User)

### 🔐 Xác thực & Bảo mật

- ✅ **Đăng ký tài khoản**: Gửi mã OTP xác thực qua Email
- ✅ **Xác thực Email**: Nhập mã OTP để kích hoạt tài khoản
- ✅ **Đăng nhập**: Sử dụng Spring Security + JWT
- ✅ **Đăng xuất**: Clear JWT cookie
- ✅ **Quên mật khẩu**: Gửi mã OTP qua Email để đặt lại mật khẩu
- ✅ **Mã hóa mật khẩu**: Sử dụng BCrypt thông qua Spring Security

### 🔍 Tìm kiếm & Lọc

- ✅ **Tìm kiếm sản phẩm**: Theo tên hoặc mô tả
- ✅ **Lọc theo danh mục**: Hiển thị sản phẩm theo từng category
- ✅ **Phân trang**: Hỗ trợ phân trang cho danh sách sản phẩm

---

## 👤 Tính năng Guest (Khách vãng lai)

### 🏠 Trang chủ

- ✅ **Hiển thị Top 10 sản phẩm bán chạy**:
  - Sản phẩm có số lượng đã bán > 10
  - Sắp xếp từ cao xuống thấp theo số lượng đã bán
  - Layout mỗi hàng 1 sản phẩm với thông tin:
    - Số thứ tự (ranking badge)
    - Hình ảnh sản phẩm (250x250px)
    - Tên sản phẩm (in đậm)
    - Giá sản phẩm
    - Số lượng đã bán
    - Số lượng còn lại trong kho

### 🌐 Truy cập hạn chế

- ✅ Xem danh sách sản phẩm
- ✅ Xem chi tiết sản phẩm
- ❌ Không thể thêm vào giỏ hàng
- ❌ Không thể mua hàng
- ❌ Không thể đánh giá sản phẩm

---

## 👨‍💼 Tính năng User (Người dùng đã đăng nhập)

### 🏠 Trang chủ & Sản phẩm

- ✅ **Trang chủ User**: Giao diện tương tự Guest nhưng có thêm các tính năng tương tác
- ✅ **Danh sách sản phẩm theo danh mục**: Lọc và hiển thị sản phẩm theo category
- ✅ **Trang Top 20 sản phẩm**:
  - Top 20 sản phẩm bán chạy nhất
  - Phân trang hoặc lazy loading
- ⚠️ **Các trang đặc biệt** (chưa hoàn thiện hết):
  - 🔶 Sản phẩm mới nhất
  - 🔶 Sản phẩm được đánh giá cao nhất
  - 🔶 Sản phẩm yêu thích nhiều nhất

### 📄 Chi tiết sản phẩm

- ✅ Hiển thị thông tin đầy đủ của sản phẩm:
  - Hình ảnh, tên, giá, mô tả
  - Category, đánh giá trung bình
  - Số lượng đã bán, còn lại
- ✅ Xem danh sách đánh giá của sản phẩm
- ✅ Chọn số lượng và thêm vào giỏ hàng

### 👤 Quản lý Profile

- ✅ **Trang Profile User**:
  - Xem và cập nhật thông tin cá nhân
  - Thay đổi mật khẩu
- ✅ **Quản lý địa chỉ nhận hàng**:
  - Thêm địa chỉ mới
  - Sửa địa chỉ hiện có
  - Xóa địa chỉ
  - Đặt địa chỉ mặc định
  - Hỗ trợ nhiều địa chỉ khác nhau

### 🛒 Giỏ hàng & Thanh toán

- ✅ **Giỏ hàng**:
  - Lưu trên database (không phải session)
  - Thêm/Xóa/Cập nhật số lượng sản phẩm
  - Tính tổng tiền tự động
- ✅ **Thanh toán**:
  - ✅ COD (Thanh toán khi nhận hàng)
  - ✅ VNPay (Cổng thanh toán trực tuyến)
  - 🔶 MOMO (chưa tích hợp)
- ✅ **Checkout**: Chọn địa chỉ giao hàng và phương thức thanh toán

### 📦 Quản lý Đơn hàng

- ✅ **Lịch sử mua hàng**:
  - Xem tất cả đơn hàng đã đặt
  - Lọc theo trạng thái:
    - ✅ Đơn hàng mới (PENDING)
    - ✅ Đã xác nhận (CONFIRMED)
    - ✅ Đang giao hàng (SHIPPING)
    - ✅ Đã giao (DELIVERED)
    - ✅ Đã hủy (CANCELLED)
    - 🔶 Trả hàng - Hoàn tiền (RETURNED) - chưa xử lý logic đầy đủ
- ✅ **Chi tiết đơn hàng**:
  - Thông tin sản phẩm
  - Địa chỉ giao hàng
  - Tổng tiền, phương thức thanh toán
  - Trạng thái đơn hàng

### ❤️ Wishlist (Yêu thích)

- ✅ **Thêm sản phẩm vào danh sách yêu thích**
- ✅ **Xem danh sách sản phẩm yêu thích**
- ✅ **Xóa sản phẩm khỏi wishlist**

### 👁️ Sản phẩm đã xem

- ✅ **Tự động lưu lịch sử xem sản phẩm**
- ✅ **Xem lại danh sách sản phẩm đã xem**
- ✅ **Theo dõi thời gian xem**

### ⭐ Đánh giá sản phẩm

- ✅ **Viết đánh giá**:
  - Chọn số sao (1-5)
  - Viết nhận xét
  - Upload hình ảnh (media)
- ✅ **Xem đánh giá của người dùng khác**

---

## 📊 Tính năng đã hoàn thành

### ✅ Backend

- [x] Authentication & Authorization (Spring Security + JWT)
- [x] Email Service (OTP cho đăng ký và quên mật khẩu)
- [x] Password Encryption (BCrypt)
- [x] Product Management
- [x] Category Management
- [x] User Profile Management
- [x] Address Management (nhiều địa chỉ)
- [x] Cart Management (lưu database)
- [x] Order Management
- [x] Order Status Tracking
- [x] Payment Integration (COD + VNPay)
- [x] Wishlist Management
- [x] Viewed Products Tracking
- [x] Review System
- [x] Search & Filter Products
- [x] Pagination

### ✅ Frontend

- [x] Trang chủ Guest (Top 10 bán chạy)
- [x] Trang đăng ký/đăng nhập
- [x] Trang xác thực OTP
- [x] Trang quên mật khẩu
- [x] Trang danh sách sản phẩm
- [x] Trang chi tiết sản phẩm
- [x] Trang giỏ hàng
- [x] Trang checkout
- [x] Trang profile user
- [x] Trang quản lý địa chỉ
- [x] Trang lịch sử đơn hàng
- [x] Trang chi tiết đơn hàng
- [x] Trang wishlist
- [x] Trang Top 20 sản phẩm
- [x] Responsive design (Bootstrap 5)

---

## 🔧 Tính năng chưa hoàn thành / Cần cải thiện

### 🔶 Cần bổ sung

- [ ] **MOMO Payment Integration**: Tích hợp cổng thanh toán MOMO
- [ ] **Xử lý trả hàng - hoàn tiền**: Logic đầy đủ cho trạng thái RETURNED
- [ ] **Thông báo realtime**: Notification cho đơn hàng, thanh toán
- [ ] **Lazy Loading**: Thay thế pagination ở một số trang

### 🔶 Cải thiện UX/UI

- [ ] **Loading states**: Thêm spinner khi loading data
- [ ] **Error handling**: Hiển thị lỗi user-friendly hơn
- [ ] **Toast notifications**: Thông báo khi thêm giỏ hàng, wishlist
- [ ] **Image zoom**: Phóng to ảnh sản phẩm khi hover
- [ ] **Quick view**: Xem nhanh sản phẩm không cần vào trang chi tiết

### 🔶 Tối ưu hiệu năng

- [ ] **Caching**: Redis cache cho sản phẩm hot
- [ ] **Image optimization**: Compress và resize ảnh tự động
- [ ] **Database indexing**: Tối ưu query performance
- [ ] **API rate limiting**: Giới hạn request để tránh spam

---

## 🚀 Hướng dẫn cài đặt

### Yêu cầu hệ thống

- Java 17 hoặc cao hơn
- Maven 3.8+
- SQL Server 2019 hoặc cao hơn
- IDE: IntelliJ IDEA / Eclipse / VS Code

### Các bước cài đặt

1. **Clone repository**

```bash
git clone <repository-url>
cd OneShop-vendor
```

2. **Cấu hình database**
   - Tạo database trong SQL Server
   - Cập nhật thông tin trong `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=oneshop;encrypt=true;trustServerCertificate=true
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
```

3. **Cấu hình Email Service**
   - Cập nhật thông tin email trong `application.properties`:

```properties
spring.mail.username=<your-email>
spring.mail.password=<your-app-password>
```

4. **Import database** (nếu có file SQL)

```bash
# Import file oneshop.sql vào database
```

5. **Build và chạy project**

```bash
mvn clean install
mvn spring-boot:run
```

6. **Truy cập ứng dụng**
   - URL: `http://localhost:8080`
   - Guest: Truy cập trực tiếp trang chủ
   - User: Đăng ký tài khoản mới hoặc đăng nhập

---

## 📱 API Endpoints

### Authentication

- `POST /api/auth/register` - Đăng ký tài khoản
- `POST /api/auth/verify-email` - Xác thực OTP
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/logout` - Đăng xuất
- `POST /api/auth/forgot-password` - Quên mật khẩu
- `POST /api/auth/reset-password` - Đặt lại mật khẩu

### User Routes (cần authentication)

- `GET /user/dashboard` - Trang dashboard user
- `GET /user/profile` - Trang profile
- `GET /user/addresses` - Quản lý địa chỉ
- `GET /user/cart` - Giỏ hàng
- `GET /user/checkout` - Thanh toán
- `GET /user/orders` - Lịch sử đơn hàng
- `GET /user/wishlist` - Danh sách yêu thích
- `GET /user/top20` - Top 20 sản phẩm

### Guest Routes (public)

- `GET /` hoặc `/home` - Trang chủ
- `GET /products` - Danh sách sản phẩm
- `GET /product/{id}` - Chi tiết sản phẩm

---

## 👥 Vai trò & Quyền hạn

### Guest (Khách vãng lai)

- Xem sản phẩm, tìm kiếm, lọc
- Không thể mua hàng hoặc tương tác

### User (Người dùng)

- Tất cả quyền của Guest
- Thêm giỏ hàng, mua hàng
- Quản lý profile, địa chỉ
- Đánh giá, yêu thích sản phẩm
- Xem lịch sử mua hàng

---

## 📝 Ghi chú

- Project sử dụng **Sitemesh** để quản lý layout/template
- Upload files được lưu tại thư mục `uploads/`
- JWT token được lưu trong HttpOnly cookie
- Session timeout: 24 giờ
- OTP có hiệu lực trong 5 phút

---

## 📧 Liên hệ

Nếu có bất kỳ câu hỏi hoặc góp ý nào, vui lòng liên hệ qua email hoặc tạo issue trên repository.

---

## 📄 License

Project này được phát triển cho mục đích học tập và nghiên cứu.

---

**Phiên bản**: 1.0.0  
**Cập nhật lần cuối**: 29/10/2025
