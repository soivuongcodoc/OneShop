# 📊 HƯỚNG DẪN DASHBOARD USER - OneShop

## 🎯 TỔNG QUAN CHỨC NĂNG USER

### ✅ CÁC CHỨC NĂNG ĐÃ HOÀN THIỆN

| STT | Chức năng                    | URL                            | Controller              | Template                | Trạng thái    |
| --- | ---------------------------- | ------------------------------ | ----------------------- | ----------------------- | ------------- |
| 1   | **Dashboard User**           | `/user/dashboard`              | UserController          | user/dashboard.html     | ✅ Hoàn thành |
| 2   | **Trang chủ Guest**          | `/`, `/home`                   | PageController          | home.html               | ✅ Hoàn thành |
| 3   | **Sản phẩm theo danh mục**   | `/products?category={id}`      | PageController          | product_list.html       | ✅ Hoàn thành |
| 4   | **Sản phẩm mới nhất**        | `/products/new`                | PageController          | product_list.html       | ✅ Hoàn thành |
| 5   | **Sản phẩm bán chạy**        | `/products/best-selling`       | PageController          | product_list.html       | ✅ Hoàn thành |
| 6   | **Sản phẩm đánh giá cao**    | `/products/top-rated`          | PageController          | product_list.html       | ✅ Hoàn thành |
| 7   | **Sản phẩm yêu thích nhiều** | `/products/most-favorited`     | PageController          | product_list.html       | ✅ Hoàn thành |
| 8   | **Chi tiết sản phẩm**        | `/product/{id}`                | PageController          | product_detail.html     | ✅ Hoàn thành |
| 9   | **Giỏ hàng**                 | `/cart`                        | CartController          | user/cart.html          | ✅ Hoàn thành |
| 10  | **Thanh toán**               | `/user/checkout`               | CheckoutController      | user/checkout.html      | ✅ Hoàn thành |
| 11  | **Thanh toán VNPay**         | `/payment/vnpay`               | PaymentController       | payment/vnpay.html      | ✅ Hoàn thành |
| 12  | **Lịch sử đơn hàng**         | `/user/orders`                 | UserController          | user/order_history.html | ✅ Hoàn thành |
| 13  | **Lọc đơn theo trạng thái**  | `/user/orders?status={status}` | UserController          | user/order_history.html | ✅ Hoàn thành |
| 14  | **Sản phẩm yêu thích**       | `/user/wishlist`               | WishlistController      | user/wishlist.html      | ✅ Hoàn thành |
| 15  | **Thêm yêu thích**           | `/user/wishlist/add/{id}`      | WishlistController      | -                       | ✅ Hoàn thành |
| 16  | **Xóa yêu thích**            | `/user/wishlist/remove/{id}`   | WishlistController      | -                       | ✅ Hoàn thành |
| 17  | **Sản phẩm đã xem**          | Auto lưu                       | ViewedProductRepository | -                       | ✅ Hoàn thành |
| 18  | **Quản lý địa chỉ**          | `/user/addresses`              | AddressController       | user/addresses.html     | ✅ Hoàn thành |
| 19  | **Thêm địa chỉ**             | `/user/addresses` POST         | AddressController       | user/addresses.html     | ✅ Hoàn thành |
| 20  | **Xóa địa chỉ**              | `/user/addresses/{id}/delete`  | AddressController       | user/addresses.html     | ✅ Hoàn thành |
| 21  | **Profile User**             | `/user/profile`                | UserController          | user/profile.html       | ✅ Hoàn thành |
| 22  | **Đánh giá sản phẩm**        | `/user/reviews`                | ReviewController        | -                       | ✅ Hoàn thành |

---

## 📊 DASHBOARD USER - Chi tiết

### 1. **Thống kê tổng quan**

```
- Tổng đơn hàng
- Đơn chờ xác nhận (PENDING)
- Đơn đang giao (SHIPPING)
- Đơn đã giao (DELIVERED)
- Số sản phẩm yêu thích
- Số sản phẩm đã xem
- Số sản phẩm trong giỏ hàng
```

### 2. **Liên kết nhanh**

```
- Sản phẩm
- Giỏ hàng (có badge số lượng)
- Đơn hàng
- Yêu thích (có badge số lượng)
- Địa chỉ
- Hồ sơ
```

### 3. **Đơn hàng gần đây**

- Hiển thị 5 đơn hàng mới nhất
- Thông tin: Mã ĐH, Ngày đặt, Tổng tiền, Trạng thái
- Nút "Chi tiết" để xem chi tiết đơn hàng

### 4. **Sản phẩm yêu thích gần đây**

- Hiển thị 4 sản phẩm yêu thích mới nhất
- Thông tin: Hình ảnh, Tên, Giá

### 5. **Sản phẩm đã xem gần đây**

- Hiển thị 4 sản phẩm đã xem mới nhất
- Thông tin: Hình ảnh, Tên, Giá

---

## 🔄 TRẠNG THÁI ĐƠN HÀNG

```java
public enum OrderStatus {
    PENDING,           // Chờ xác nhận
    CONFIRMED,         // Đã xác nhận
    SHIPPING,          // Đang giao
    DELIVERED,         // Đã giao
    RETURN_REQUESTED,  // Yêu cầu trả hàng
    RETURNED,          // Đã trả hàng
    REFUNDING,         // Đang hoàn tiền
    REFUNDED,          // Đã hoàn tiền
    CANCELLED          // Đã hủy
}
```

---

## 🛍️ PHÂN TRANG & LAZY LOADING

### Đã có Pagination cho:

- ✅ Danh sách sản phẩm (20 items/page)
- ✅ Sản phẩm mới (20 items/page)
- ✅ Sản phẩm bán chạy (20 items/page)
- ✅ Sản phẩm đánh giá cao (20 items/page)
- ✅ Sản phẩm yêu thích nhiều (20 items/page)

### Lazy Loading:

- ✅ Đã có trong `product_list.html` (scroll to load more)

---

## 💳 THANH TOÁN

### Phương thức đã hỗ trợ:

1. **COD** (Cash on Delivery) - Thanh toán khi nhận hàng
2. **VNPay** - Thanh toán online qua VNPay

### Flow thanh toán:

```
1. User chọn sản phẩm → Thêm vào giỏ
2. Vào giỏ hàng → Checkout
3. Chọn địa chỉ nhận hàng
4. Chọn phương thức thanh toán (COD/VNPay)
5. Xác nhận đơn hàng
6. Đơn hàng được tạo với status = PENDING
```

---

## 📝 ĐÁNH GIÁ SẢN PHẨM

### Chức năng đã có:

- ✅ Đánh giá sản phẩm đã mua
- ✅ Rating (1-5 sao)
- ✅ Bình luận văn bản
- ⚠️ **Cần bổ sung:** Upload hình ảnh/video trong review

### Repository:

```java
ReviewRepository
- findByProductId(Long productId)
- findByUserId(Long userId)
```

---

## 🗂️ CẤU TRÚC THƯ MỤC

```
src/main/java/com/oneshop/
├── controller/
│   ├── user/
│   │   ├── UserController.java         ✅ (Dashboard + Orders + Profile)
│   │   ├── CartController.java         ✅ (Giỏ hàng)
│   │   ├── CheckoutController.java     ✅ (Thanh toán)
│   │   ├── PaymentController.java      ✅ (VNPay)
│   │   ├── WishlistController.java     ✅ (Yêu thích)
│   │   ├── AddressController.java      ✅ (Địa chỉ)
│   │   └── ReviewController.java       ✅ (Đánh giá)
│   └── PageController.java             ✅ (Trang chủ + Sản phẩm)
│
├── entity/
│   ├── Order.java
│   ├── OrderStatus.java
│   ├── Product.java
│   ├── Cart.java
│   ├── Wishlist.java
│   ├── Address.java
│   ├── Review.java
│   └── ViewedProduct.java
│
└── repository/
    ├── OrderRepository.java
    ├── ProductRepository.java
    ├── CartRepository.java
    ├── WishlistRepository.java
    ├── AddressRepository.java
    ├── ReviewRepository.java
    └── ViewedProductRepository.java

src/main/resources/templates/
├── user/
│   ├── dashboard.html          ✅ (MỚI - Dashboard User)
│   ├── cart.html               ✅
│   ├── checkout.html           ✅
│   ├── order_history.html      ✅
│   ├── wishlist.html           ✅
│   ├── addresses.html          ✅
│   └── profile.html            ✅
├── home.html                   ✅ (Trang chủ Guest)
├── product_list.html           ✅
├── product_detail.html         ✅
└── payment/
    └── vnpay.html              ✅
```

---

## 🚀 HƯỚNG DẪN SỬ DỤNG

### 1. **Truy cập Dashboard User**

```
URL: http://localhost:8080/user/dashboard
Yêu cầu: Đã đăng nhập
```

### 2. **Trang chủ Guest (Sản phẩm bán > 10)**

```
URL: http://localhost:8080/
Không yêu cầu đăng nhập
Hiển thị: Sản phẩm có sold > 10, sắp xếp từ lớn đến nhỏ
```

### 3. **Xem sản phẩm theo danh mục**

```
URL: http://localhost:8080/products?category=1
```

### 4. **Thêm vào giỏ hàng**

```
POST /cart/add/{productId}?qty=1
```

### 5. **Thêm vào yêu thích**

```
POST /user/wishlist/add/{productId}
```

### 6. **Lọc đơn hàng theo trạng thái**

```
/user/orders?status=PENDING      // Chờ xác nhận
/user/orders?status=CONFIRMED    // Đã xác nhận
/user/orders?status=SHIPPING     // Đang giao
/user/orders?status=DELIVERED    // Đã giao
/user/orders?status=CANCELLED    // Đã hủy
```

---

## ⚠️ CHỨC NĂNG CẦN BỔ SUNG (Tùy chọn)

### 1. **Upload hình ảnh/video trong đánh giá**

- Cần thêm field `images` và `videos` vào entity `Review`
- Cập nhật ReviewController để xử lý multipart file

### 2. **Mã giảm giá (Coupon)**

- ✅ Entity `Coupon` đã có
- ⚠️ Cần tích hợp vào checkout flow

### 3. **Chi tiết đơn hàng**

- Cần tạo endpoint `/user/orders/{id}` để xem chi tiết
- Hiển thị danh sách sản phẩm trong đơn

---

## 🎨 GIAO DIỆN DASHBOARD

### Đã có:

- ✅ Thống kê bằng card màu sắc đẹp mắt
- ✅ Icons Bootstrap
- ✅ Responsive design
- ✅ Hover effects
- ✅ Quick links với badge
- ✅ Bảng đơn hàng gần đây
- ✅ Grid sản phẩm yêu thích & đã xem

### Decorator:

Sử dụng `decorators/main.html` để layout chung với header & footer

---

## 📋 CHECKLIST HOÀN THIỆN

- [x] Dashboard User với thống kê tổng quan
- [x] Trang chủ hiển thị sản phẩm bán > 10
- [x] Sản phẩm theo danh mục + phân trang
- [x] Sản phẩm mới/bán chạy/đánh giá cao/yêu thích
- [x] Profile User
- [x] Quản lý địa chỉ nhận hàng
- [x] Chi tiết sản phẩm
- [x] Giỏ hàng lưu database
- [x] Thanh toán COD & VNPay
- [x] Lịch sử mua hàng theo trạng thái
- [x] Sản phẩm yêu thích
- [x] Sản phẩm đã xem (auto save)
- [x] Đánh giá sản phẩm đã mua
- [ ] Upload ảnh/video trong đánh giá (Optional)
- [ ] Chọn mã giảm giá (Optional)

---

## 🎯 KẾT LUẬN

**Tất cả chức năng chính đã được hoàn thiện theo yêu cầu đề tài!**

Để sử dụng:

1. Khởi động ứng dụng: `mvn spring-boot:run`
2. Truy cập: `http://localhost:8080/user/dashboard`
3. Đăng nhập với tài khoản User để xem đầy đủ chức năng

---

**Ngày cập nhật:** 28/10/2025
**Version:** 1.0
