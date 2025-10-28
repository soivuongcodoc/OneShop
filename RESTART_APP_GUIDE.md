# 🔄 HƯỚNG DẪN RESTART APP

## 🐛 VẤN ĐỀ

Sau khi sửa code, app **vẫn chạy code cũ** vì:

- File trong `target/classes/` chưa được rebuild
- Spring Boot đang load template từ `target/classes/templates/user/cart.html` (code cũ)
- Cần restart để Maven compile lại code mới

---

## ✅ CÁCH SỬA (Chọn 1 trong 3)

### **CÁCH 1: RESTART NHANH** ⚡ (KHUYẾN NGHỊ)

**Trong terminal đang chạy app:**

1. Nhấn **`Ctrl + C`** để dừng app
2. Chạy lại:
   ```bash
   mvn spring-boot:run
   ```

→ Maven sẽ tự động compile lại code mới!

---

### **CÁCH 2: CLEAN BUILD** 🧹 (Nếu cách 1 không work)

```bash
# Dừng app (Ctrl+C)
# Sau đó chạy:
mvn clean compile spring-boot:run
```

→ Xóa toàn bộ `target/` và build lại từ đầu!

---

### **CÁCH 3: BUILD RIÊNG** 🔧 (Debug chi tiết)

```bash
# Bước 1: Dừng app (Ctrl+C)

# Bước 2: Clean
mvn clean

# Bước 3: Compile
mvn compile

# Bước 4: Kiểm tra target/classes/templates/user/cart.html
# Xem file đã được update chưa

# Bước 5: Chạy lại
mvn spring-boot:run
```

---

## 🚀 KIỂM TRA SAU KHI RESTART

1. **Xem log khi app khởi động:**

   ```
   Started OneShopApplication in X.XXX seconds
   ```

2. **Truy cập giỏ hàng:**

   ```
   http://localhost:8080/user/cart
   ```

3. **Kiểm tra kết quả:**
   - ✅ Không còn lỗi template parsing
   - ✅ Trang giỏ hàng hiển thị bình thường
   - ✅ Tổng tiền hiển thị đúng

---

## ⚠️ LƯU Ý

### **Khi nào cần restart?**

✅ **BẮT BUỘC RESTART** khi thay đổi:

- Java Controller files (`.java`)
- Entity classes
- Template files (`.html`) trong `src/main/resources/templates/`
- Configuration files (`application.properties`)

❌ **KHÔNG CẦN RESTART** khi thay đổi:

- Static files (CSS, JS) trong `src/main/resources/static/` (nếu có Spring DevTools)
- README, document files

---

## 🎯 TÓM TẮT

| File thay đổi               | Restart cần?            |
| --------------------------- | ----------------------- |
| **CartController.java**     | ✅ CẦN                  |
| **cart.html**               | ✅ CẦN                  |
| **CheckoutController.java** | ✅ CẦN                  |
| **application.properties**  | ✅ CẦN                  |
| **style.css** (static)      | ❌ Không (với DevTools) |

---

## 🔥 QUICK COMMAND

**Copy và chạy ngay:**

```bash
# Nhấn Ctrl+C trước, sau đó:
mvn spring-boot:run
```

---

**Chúc bạn thành công!** 🎉
