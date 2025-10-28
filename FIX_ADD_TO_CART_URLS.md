# ✅ ĐÃ SỬA LỖI "ADD TO CART" URLS

## 🐛 VẤN ĐỀ

Khi bấm "Thêm vào giỏ hàng", báo lỗi:

```
No static resource cart/add/1002
```

**Nguyên nhân:**

- **CartController** đã đổi mapping từ `/cart` → `/user/cart`
- **Nhưng các trang sản phẩm vẫn dùng URL cũ:** `/cart/add/{id}`
- **Cần đổi thành:** `/user/cart/add/{id}`

---

## ✅ ĐÃ SỬA 3 FILES:

### **1. `product_detail.html`** ✅

```html
<!-- TRƯỚC: ❌ -->
<form th:action="@{'/cart/add/' + ${product.id}}">
  <!-- SAU: ✅ -->
  <form th:action="@{'/user/cart/add/' + ${product.id}}"></form>
</form>
```

### **2. `product.html`** ✅

```html
<!-- TRƯỚC: ❌ -->
<form th:action="@{'/cart/add/' + ${p.id}}">
  <!-- SAU: ✅ -->
  <form th:action="@{'/user/cart/add/' + ${p.id}}"></form>
</form>
```

### **3. `product-detail.html`** ✅

```html
<!-- TRƯỚC: ❌ -->
<form th:action="@{'/cart/add/' + ${product.id}}">
  <!-- SAU: ✅ -->
  <form th:action="@{'/user/cart/add/' + ${product.id}}"></form>
</form>
```

---

## 🎯 KẾT QUẢ

### **Tất cả URLs giờ đã NHẤT QUÁN:**

| Chức năng            | URL Old (❌)        | URL New (✅)             |
| -------------------- | ------------------- | ------------------------ |
| **View cart**        | `/cart`             | `/user/cart`             |
| **Add to cart**      | `/cart/add/{id}`    | `/user/cart/add/{id}`    |
| **Remove from cart** | `/cart/remove/{id}` | `/user/cart/remove/{id}` |
| **Checkout**         | `/checkout`         | `/user/checkout`         |

---

## 🚀 RESTART APP NGAY:

**Trong terminal:**

```bash
# Ctrl+C để dừng app
# Sau đó chạy:
mvn spring-boot:run
```

---

## ✅ TEST SAU KHI RESTART:

### **Bước 1: Xem sản phẩm**

```
http://localhost:8080/products
hoặc
http://localhost:8080/user/top20
```

### **Bước 2: Click "Thêm vào giỏ"**

- ✅ Không còn lỗi "No static resource"
- ✅ Sản phẩm được thêm vào giỏ hàng
- ✅ Redirect về `/user/cart` thành công

### **Bước 3: Kiểm tra giỏ hàng**

```
http://localhost:8080/user/cart
```

- ✅ Hiển thị sản phẩm vừa thêm
- ✅ Số lượng, giá, tổng tiền hiển thị đúng
- ✅ Có thể xóa sản phẩm
- ✅ Có thể click "Thanh toán"

---

## 📊 TÓM TẮT TOÀN BỘ THAY ĐỔI CART:

| File                        | Thay đổi                                |
| --------------------------- | --------------------------------------- |
| **CartController.java**     | `@RequestMapping("/user/cart")`         |
| **CheckoutController.java** | `@RequestMapping("/user/checkout")`     |
| **product_detail.html**     | Form action: `/user/cart/add/{id}`      |
| **product.html**            | Form action: `/user/cart/add/{id}`      |
| **product-detail.html**     | Form action: `/user/cart/add/{id}`      |
| **cart.html**               | Remove action: `/user/cart/remove/{id}` |
| **cart.html**               | Checkout link: `/user/checkout`         |
| **checkout.html**           | Back to cart: `/user/cart`              |
| **wishlist.html**           | View cart: `/user/cart`                 |
| **dashboard.html**          | Cart link: `/user/cart`                 |

---

## 🔥 QUICK COMMAND

**Copy và chạy ngay:**

```bash
# Trong terminal đang chạy app, nhấn Ctrl+C
# Sau đó:
mvn spring-boot:run
```

---

**Chúc bạn thành công!** 🎉
