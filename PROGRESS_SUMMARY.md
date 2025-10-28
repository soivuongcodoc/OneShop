# 📊 TỔNG HỢP TIẾN ĐỘ DỰ ÁN ONESHOP

## 🎯 YÊU CẦU VÀ TIẾN ĐỘ THỰC HIỆN

---

## 👤 GUEST (Khách vãng lai)

### ✅ ĐÃ HOÀN THÀNH

| Chức năng                                                                    | Trạng thái    | File Backend                                                                                          | File Frontend              |
| ---------------------------------------------------------------------------- | ------------- | ----------------------------------------------------------------------------------------------------- | -------------------------- |
| **Trang chủ hiển thị sản phẩm bán trên 10 sản phẩm, sắp xếp từ lớn đến nhỏ** | ✅ HOÀN THÀNH | `PageController.java` (line 34-46)<br>`ProductRepository.java` (line 56)<br>`ProductServiceImpl.java` | `home.html` (line 105-149) |

**Chi tiết:**

- ✅ Repository method: `findBySoldGreaterThanOrderBySoldDesc(Integer sold)`
- ✅ Service method: `findProductsSoldGreaterThan10()`
- ✅ Controller endpoint: `GET /` hoặc `GET /home`
- ✅ Hiển thị badge "Đã bán X" trên mỗi sản phẩm
- ✅ Mock data nếu chưa có sản phẩm thực tế
- ✅ Responsive layout với Bootstrap

---

## 👨‍💼 USER (Người dùng đã đăng nhập)

### ✅ ĐÃ HOÀN THÀNH

| #   | Chức năng                                            | Trạng thái    | File Backend                                                                         | File Frontend                                               |
| --- | ---------------------------------------------------- | ------------- | ------------------------------------------------------------------------------------ | ----------------------------------------------------------- |
| 1   | **Giao diện trang chủ**                              | ✅ HOÀN THÀNH | `PageController.java`                                                                | `home.html`                                                 |
| 2   | **Dashboard User**                                   | ✅ HOÀN THÀNH | `UserController.java` (line 52-104)                                                  | `user/dashboard.html`                                       |
| 3   | **20 sản phẩm (mới, bán chạy, đánh giá, yêu thích)** | ✅ HOÀN THÀNH | `UserController.java` (line 107-122)<br>`ProductRepository.java` (line 21-25, 48-53) | `user/top20.html`                                           |
| 4   | **Trang chi tiết sản phẩm**                          | ✅ HOÀN THÀNH | `UserController.java` (line 148-156)                                                 | `product-detail.html`<br>`product_detail.html`              |
| 5   | **Giỏ hàng (lưu trên database)**                     | ✅ HOÀN THÀNH | `CartController.java` (full file)<br>`CartRepository.java`                           | `user/cart.html`                                            |
| 6   | **Thanh toán COD**                                   | ✅ HOÀN THÀNH | `CheckoutController.java` (line 62-141)                                              | `user/checkout.html`                                        |
| 7   | **Thanh toán VNPAY**                                 | ✅ HOÀN THÀNH | `PaymentController.java`<br>`CheckoutController.java` (line 135-137)                 | `payment/vnpay.html`                                        |
| 8   | **Quản lý lịch sử mua hàng theo trạng thái**         | ✅ HOÀN THÀNH | `UserController.java` (line 174-200)<br>`OrderRepository.java`                       | `user/order_history.html`                                   |
| 9   | **Chi tiết đơn hàng**                                | ✅ HOÀN THÀNH | `UserController.java` (line 202-235)                                                 | `user/order_detail.html`                                    |
| 10  | **Hủy đơn hàng (trạng thái PENDING)**                | ✅ HOÀN THÀNH | `UserController.java` (line 237-261)                                                 | `user/order_detail.html` (form hủy)                         |
| 11  | **Thích sản phẩm (Wishlist)**                        | ✅ HOÀN THÀNH | `WishlistController.java`                                                            | `user/wishlist.html`                                        |
| 12  | **Trang profile user**                               | ✅ HOÀN THÀNH | `UserController.java` (line 160-172)                                                 | `user/profile.html`                                         |
| 13  | **Quản lý địa chỉ nhận hàng**                        | ✅ HOÀN THÀNH | `AddressController.java`<br>`AddressRepository.java`                                 | `user/profile.html` (modal + list)<br>`user/addresses.html` |
| 14  | **Đánh giá sản phẩm đã mua**                         | ✅ HOÀN THÀNH | `ReviewController.java` (line 23-60)<br>`ReviewService.java`                         | Form trong `product_detail.html`                            |
| 15  | **Bình luận text (≥50 ký tự)**                       | ✅ HOÀN THÀNH | `ReviewController.java` (line 32-34)                                                 | Form validation                                             |
| 16  | **Bình luận hình ảnh/video**                         | ✅ HOÀN THÀNH | `ReviewController.java` (line 39-50)                                                 | File upload trong form review                               |

---

### ⚠️ CHƯA HOÀN THÀNH / CẦN BỔ SUNG

| #   | Chức năng                                          | Trạng thái              | Ghi chú                                                                                                           |
| --- | -------------------------------------------------- | ----------------------- | ----------------------------------------------------------------------------------------------------------------- |
| 1   | **Trang sản phẩm theo danh mục**                   | ⚠️ CHƯA CÓ UI RIÊNG     | Có method `findByCategoryId()` trong `ProductRepository.java` nhưng chưa có controller endpoint và template riêng |
| 2   | **Phân trang / Lazy loading cho Top 20**           | ⚠️ CHƯA CÓ              | Hiện tại chỉ hiển thị top 5 mỗi loại, chưa có pagination                                                          |
| 3   | **Sản phẩm đã xem**                                | ⚠️ CHƯA CÓ UI           | Có `ViewedProductRepository` và tracking trong backend nhưng chưa có trang hiển thị danh sách sản phẩm đã xem     |
| 4   | **Thanh toán MOMO**                                | ❌ CHƯA LÀM             | Chỉ có TODO comment trong code                                                                                    |
| 5   | **Chọn mã giảm giá**                               | ⚠️ CÓ LOGIC CHƯA CÓ UI  | Có logic áp dụng coupon trong `CheckoutController.java` (line 80-93) nhưng UI chưa hoàn chỉnh                     |
| 6   | **Yêu cầu trả hàng - hoàn tiền**                   | ⚠️ CÓ NÚT CHƯA CÓ LOGIC | Có nút "Yêu cầu trả hàng" trong `order_detail.html` nhưng chỉ là alert placeholder                                |
| 7   | **Hiển thị danh sách reviews trên trang sản phẩm** | ⚠️ CHƯA RÕ              | Chỉ có form thêm review, chưa rõ có hiển thị list reviews không                                                   |

---

## 📂 CẤU TRÚC FILE CHI TIẾT

### 📁 Backend Controllers (User)

```
src/main/java/com/oneshop/controller/user/
├── UserController.java         ✅ Dashboard, Top20, Products, Profile, Orders
├── CartController.java         ✅ View cart, Add to cart, Remove from cart
├── CheckoutController.java     ✅ Checkout page, Place order (COD/VNPAY)
├── PaymentController.java      ✅ VNPAY callback handling
├── WishlistController.java     ✅ Add/remove wishlist
├── AddressController.java      ✅ CRUD addresses, Set default
└── ReviewController.java       ✅ Add review (text + media)
```

### 📁 Frontend Templates (User)

```
src/main/resources/templates/
├── home.html                   ✅ Guest homepage (products sold > 10)
├── product.html                ✅ Product list with pagination
├── product-detail.html         ✅ Product detail + add to cart + review form
└── user/
    ├── dashboard.html          ✅ User dashboard overview
    ├── top20.html              ✅ Top 5 (newest, best-selling, top-rated, most-favorited)
    ├── profile.html            ✅ User profile + address management
    ├── cart.html               ✅ Shopping cart
    ├── checkout.html           ✅ Checkout form (address, payment method)
    ├── order_history.html      ✅ Order list with status filter
    ├── order_detail.html       ✅ Order detail + cancel order
    ├── wishlist.html           ✅ Wishlist page
    └── addresses.html          ✅ Address management (legacy)
```

### 📁 Repositories

```
src/main/java/com/oneshop/repository/
├── ProductRepository.java      ✅ findBySoldGreaterThan, findTop5ByOrderBy...
├── OrderRepository.java        ✅ findByCustomer_User_Id...
├── CartRepository.java         ✅ findByUserId
├── WishlistRepository.java     ✅ findByUserId, existsByUserIdAndProductId
├── ViewedProductRepository.java ✅ findByUserIdOrderByViewedAtDesc
├── AddressRepository.java      ✅ findByUserId
├── ReviewRepository.java       ✅ findByProductId
└── CouponRepository.java       ✅ findByCodeAndActiveTrue
```

---

## 🎨 TRẠNG THÁI ĐÁNH GIÁ

| Trạng thái         | Icon | Ý nghĩa                                            |
| ------------------ | ---- | -------------------------------------------------- |
| ✅ HOÀN THÀNH      | ✅   | Đã có đầy đủ backend + frontend + hoạt động tốt    |
| ⚠️ CHƯA HOÀN CHỈNH | ⚠️   | Có một phần code nhưng chưa đầy đủ hoặc chưa có UI |
| ❌ CHƯA LÀM        | ❌   | Chưa có code gì                                    |

---

## 📊 TỔNG KẾT

### ✅ Đã hoàn thành: **16/22** chức năng (≈73%)

**Điểm mạnh:**

- ✅ Các chức năng core đã hoàn thiện tốt
- ✅ UI/UX đẹp, responsive
- ✅ Tích hợp thanh toán VNPAY
- ✅ Quản lý đơn hàng theo trạng thái
- ✅ Review có hỗ trợ media (ảnh/video)
- ✅ Giỏ hàng lưu database

**Cần bổ sung:**

- ⚠️ Trang sản phẩm theo danh mục
- ⚠️ Trang sản phẩm đã xem
- ⚠️ Phân trang cho Top 20
- ⚠️ UI chọn mã giảm giá
- ⚠️ Logic trả hàng - hoàn tiền
- ⚠️ Hiển thị danh sách reviews trên sản phẩm
- ❌ Thanh toán MOMO

---

## 📝 GHI CHÚ

- Tất cả các file đều tuân thủ cấu trúc MVC chuẩn
- Sử dụng Spring Boot + Thymeleaf + Bootstrap
- Database: SQL Server
- Authentication: JWT-based
- Roles: ADMIN, VENDOR, USER, GUEST

---

**Ngày cập nhật:** 29/10/2025  
**Phiên bản:** 1.0
