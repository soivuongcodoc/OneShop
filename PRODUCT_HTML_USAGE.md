# 📄 FILE `product.html` - CHỨC NĂNG VÀ URL

## 🎯 MỤC ĐÍCH

File `product.html` dùng để **hiển thị danh sách sản phẩm** với:

- ✅ Tìm kiếm sản phẩm theo tên
- ✅ Phân trang (pagination)
- ✅ Lọc theo danh mục (category)
- ✅ Thêm vào giỏ hàng
- ✅ Xem chi tiết sản phẩm

---

## 🔗 CÁC URL TRUY CẬP

### 1️⃣ **GUEST (Khách vãng lai)** - `/products`

**Controller:** `PageController.java`  
**Line:** 85-111

```java
@GetMapping("/products")
public String products(
        @RequestParam(value = "category", required = false) Long categoryId,
        @RequestParam(value = "q", required = false) String q,
        @RequestParam(value = "page", defaultValue = "0") int page,
        Model model) {

    int size = 20;
    PageRequest pageable = PageRequest.of(page, size);
    Page<Product> result;

    if (q != null && !q.isBlank()) {
        // Tìm kiếm theo tên/mô tả
        result = productService.search(q, pageable);
    } else if (categoryId != null) {
        // Lọc theo danh mục
        result = productService.listByCategory(categoryId, pageable);
    } else {
        // Tất cả sản phẩm
        result = productService.list(pageable);
    }

    model.addAttribute("products", result.getContent());
    model.addAttribute("totalPages", result.getTotalPages());
    model.addAttribute("currentPage", page);
    model.addAttribute("categoryId", categoryId);

    return "product"; // ← Gọi product.html
}
```

**Ví dụ URL:**

- `/products` - Tất cả sản phẩm (trang 1)
- `/products?page=2` - Trang 2
- `/products?q=kem` - Tìm kiếm "kem"
- `/products?category=100` - Lọc theo danh mục ID 100
- `/products?q=son&page=1` - Tìm "son" trang 2

---

### 2️⃣ **USER (Người dùng đã đăng nhập)** - `/user/products`

**Controller:** `UserController.java`  
**Line:** 125-145

```java
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("products")
    public String products(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Page<Product> p;
        if (q != null && !q.isBlank()) {
            p = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    q, q, PageRequest.of(page, 20)
            );
            model.addAttribute("q", q);
        } else {
            p = productRepository.findAll(PageRequest.of(page, 20));
        }

        model.addAttribute("products", p.getContent());
        model.addAttribute("totalPages", p.getTotalPages());
        model.addAttribute("currentPage", page);

        return "product"; // ← Gọi product.html
    }
}
```

**Ví dụ URL:**

- `/user/products` - Tất cả sản phẩm cho user
- `/user/products?page=2` - Trang 2
- `/user/products?q=serum` - Tìm kiếm "serum"

---

## 📋 CẤU TRÚC FILE `product.html`

### **Line 12-20: Form tìm kiếm**

```html
<form method="get" th:action="@{/products}">
  <input
    type="text"
    name="q"
    th:value="${q}"
    placeholder="Tìm kiếm sản phẩm..."
  />
  <button type="submit">Tìm</button>
</form>
```

✅ Cho phép tìm kiếm sản phẩm theo tên

---

### **Line 22-42: Grid hiển thị sản phẩm**

```html
<div class="product-grid">
  <div class="product-card" th:each="p : ${products}">
    <!-- Hình ảnh sản phẩm -->
    <a th:href="@{'/product/' + ${p.id}}">
      <img th:src="${p.imageUrl}" alt="" />
      <div th:text="${p.name}"></div>
    </a>

    <!-- Giá -->
    <div
      th:text="${#numbers.formatDecimal(p.price, 0, 'COMMA', 0, 'POINT')} + ' đ'"
    ></div>

    <!-- Form thêm vào giỏ hàng -->
    <form th:action="@{'/user/cart/add/' + ${p.id}}" method="post">
      <input type="number" name="qty" value="1" min="1" />
      <button type="submit">Thêm vào giỏ</button>
    </form>
  </div>
</div>
```

✅ Hiển thị danh sách sản phẩm dạng grid  
✅ Link đến trang chi tiết: `/product/{id}`  
✅ Form thêm vào giỏ: `POST /user/cart/add/{id}`

---

### **Line 44-53: Pagination**

```html
<nav class="pagination" th:if="${totalPages} > 1">
  <a
    th:each="i : ${#numbers.sequence(0, totalPages - 1)}"
    th:href="@{/products(page=${i}, q=${q}, category=${categoryId})}"
    th:text="${i + 1}"
    th:classappend="${i} == ${currentPage} ? 'active' : ''"
  >
  </a>
</nav>
```

✅ Phân trang nếu có nhiều hơn 1 trang  
✅ Giữ nguyên query string (q, category) khi chuyển trang

---

## 📊 SO SÁNH GUEST vs USER

| Tiêu chí              | GUEST `/products`                         | USER `/user/products` |
| --------------------- | ----------------------------------------- | --------------------- |
| **Lọc theo danh mục** | ✅ Có (`?category=...`)                   | ❌ Không              |
| **Tìm kiếm**          | ✅ Có                                     | ✅ Có                 |
| **Phân trang**        | ✅ Có (20 sp/trang)                       | ✅ Có (20 sp/trang)   |
| **Thêm vào giỏ**      | ✅ Có (redirect login nếu chưa đăng nhập) | ✅ Có                 |
| **Controller**        | `PageController`                          | `UserController`      |

---

## 🔄 FLOW HOẠT ĐỘNG

### **GUEST:**

```
1. User truy cập /products
2. PageController xử lý
3. Load danh sách sản phẩm từ DB
4. Return product.html
5. Hiển thị sản phẩm + pagination
6. Click "Thêm vào giỏ" → Redirect /auth/login (nếu chưa đăng nhập)
```

### **USER:**

```
1. User (đã login) truy cập /user/products
2. UserController xử lý
3. Load danh sách sản phẩm từ DB
4. Return product.html
5. Hiển thị sản phẩm + pagination
6. Click "Thêm vào giỏ" → Thêm vào giỏ hàng trong DB
```

---

## 🎨 TÍNH NĂNG CHÍNH

| #   | Tính năng                   | Trạng thái     |
| --- | --------------------------- | -------------- |
| 1   | Hiển thị danh sách sản phẩm | ✅             |
| 2   | Tìm kiếm theo tên           | ✅             |
| 3   | Lọc theo danh mục           | ✅ (chỉ Guest) |
| 4   | Phân trang                  | ✅             |
| 5   | Thêm vào giỏ hàng           | ✅             |
| 6   | Xem chi tiết sản phẩm       | ✅             |
| 7   | Hiển thị giá định dạng VNĐ  | ✅             |
| 8   | Hiển thị hình ảnh sản phẩm  | ✅             |

---

## 📝 GHI CHÚ

- File `product.html` được **DÙNG CHUNG** cho cả Guest và User
- File này **KHÔNG DÙNG DECORATOR** (không có header/footer tự động)
- Comment ở cuối file: _"đơn giản để dùng chung cho cả guest (/products) và user (/user/products)"_
- Form "Thêm vào giỏ" luôn post đến `/user/cart/add/{id}` - nếu chưa đăng nhập sẽ redirect đến login

---

**Ngày cập nhật:** 29/10/2025
