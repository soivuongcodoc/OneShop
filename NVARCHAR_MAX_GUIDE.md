# 📝 HƯỚNG DẪN: ĐỔI COLUMN NAME THÀNH NVARCHAR(MAX)

## ✅ ĐÃ THỰC HIỆN

### 1. **Sửa Entity Product.java**

```java
// TRƯỚC:
@Column(nullable = false, length = 200)
private String name;

// SAU:
@Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
private String name;
```

---

## 🚀 BƯỚC TIẾP THEO

### **Bước 1: Chạy SQL Script để Alter Database**

Execute file này trong SSMS:

```
alter_product_name_to_max.sql
```

**Script sẽ:**

- ✅ Hiển thị thông tin column `name` TRƯỚC khi alter (NVARCHAR(200))
- ✅ Alter column thành `NVARCHAR(MAX)`
- ✅ Hiển thị thông tin column SAU khi alter (NVARCHAR(MAX))
- ✅ Test insert tên dài >200 ký tự

---

### **Bước 2: Restart Spring Boot App**

```bash
# Dừng app hiện tại (Ctrl+C trong terminal đang chạy)
# Sau đó chạy lại:
mvn spring-boot:run
```

⚠️ **Lưu ý:** Hibernate sẽ kiểm tra schema và validate column definition.

---

## 📋 LỢI ÍCH

### **Trước đây (NVARCHAR(200)):**

```
Serum Vitamin C Klairs  ✅ (OK - 24 ký tự)
Kem Chống Nắng Biore UV Aqua Rich Watery Essence SPF50+ PA++++ 85g - Chống Nắng Dạng Gel Siêu Nhẹ Không Gây Nhờn Rít Cho Da Dầu Mụn - Hàng Chính Hãng  ❌ (>200 ký tự - BỊ CẮT)
```

### **Sau này (NVARCHAR(MAX)):**

```
Serum Vitamin C Klairs  ✅
Kem Chống Nắng Biore UV Aqua Rich Watery Essence SPF50+ PA++++ 85g - Chống Nắng Dạng Gel Siêu Nhẹ Không Gây Nhờn Rít Cho Da Dầu Mụn - Hàng Chính Hãng - Công Thức Mới 2024 với Acid Hyaluronic và Vitamin E  ✅ (Dài bao nhiêu cũng OK!)
```

---

## ✅ KIỂM TRA KẾT QUẢ

### **Sau khi chạy SQL script:**

```sql
-- Kiểm tra column info
SELECT
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'products'
  AND COLUMN_NAME = 'name';
```

**Kết quả mong đợi:**

```
COLUMN_NAME | DATA_TYPE | CHARACTER_MAXIMUM_LENGTH
name        | nvarchar  | -1 (nghĩa là MAX)
```

---

## 🎯 TÓM TẮT

| Thay đổi       | Trước           | Sau                                  |
| -------------- | --------------- | ------------------------------------ |
| **Entity**     | `length = 200`  | `columnDefinition = "NVARCHAR(MAX)"` |
| **Database**   | `NVARCHAR(200)` | `NVARCHAR(MAX)`                      |
| **Giới hạn**   | 200 ký tự       | Không giới hạn (2GB)                 |
| **Tiếng Việt** | ✅ OK           | ✅ OK (tốt hơn)                      |

---

## ⚠️ LƯU Ý

1. **Sau khi alter column**, tên sản phẩm cũ vẫn giữ nguyên (không mất data)
2. **Từ giờ** có thể nhập tên sản phẩm dài tùy ý
3. **Không cần** chạy lại `fix_product_names_vietnamese.sql` (trừ khi muốn update lại tên)
4. **Script test** trong `alter_product_name_to_max.sql` sẽ tự động test với tên >200 ký tự

---

**Chúc bạn thành công!** 🎉
