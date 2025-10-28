# 🌸 Hướng Dẫn Sử Dụng Dữ Liệu Mẫu Mỹ Phẩm

## 📋 Tổng Quan

File `cosmetics_sample_data.sql` chứa dữ liệu mẫu cho **20 sản phẩm mỹ phẩm** được phân chia theo 4 danh mục:

- ✨ **5 Sản phẩm mới nhất** (Newest)
- 🔥 **5 Sản phẩm bán chạy** (Best Selling)
- ⭐ **5 Sản phẩm đánh giá cao** (Top Rated)
- ❤️ **5 Sản phẩm yêu thích nhất** (Most Favorited)

## 🎯 Mục Đích

Dữ liệu này được tạo để phục vụ chức năng **Top 20** trong User Dashboard, giúp kiểm tra và demo các tính năng:

- Hiển thị sản phẩm theo danh mục
- Sắp xếp và phân trang
- Lazy loading
- Wishlist (yêu thích)
- Reviews (đánh giá)

## 📦 Dữ Liệu Được Tạo

### 1. **Category (Danh mục)**

- ID: 100
- Tên: "Mỹ Phẩm"

### 2. **Vendor User & Shop**

- **Username**: `cosmetic_vendor`
- **Email**: `cosmetic@oneshop.vn`
- **Password**: `vendor123` (đã mã hóa BCrypt)
- **Shop**: Beauty Paradise (ID: 200)

### 3. **Products (Sản phẩm)**

20 sản phẩm mỹ phẩm thật với:

- Tên sản phẩm chính hãng (Klairs, La Roche-Posay, Bioderma, CeraVe, The Ordinary, Dior, Laneige, v.v.)
- Mô tả chi tiết về công dụng
- Giá từ 35,000đ - 1,285,000đ
- Hình ảnh từ Unsplash (URLs công khai)
- Số lượng đã bán (sold) và tồn kho (stock) phù hợp

### 4. **Customer Users**

- 10 tài khoản khách hàng mẫu: `customer1` đến `customer10`
- Email: `customer1@test.com` đến `customer10@test.com`
- Password: `vendor123` (giống vendor)

### 5. **Wishlists**

- Sản phẩm 1011 (Toner AHA BHA): 45 lượt thích
- Sản phẩm 1012 (Water Sleeping Mask): 52 lượt thích
- Sản phẩm 1013 (Moroccanoil): 38 lượt thích
- Sản phẩm 1014 (Avène Water): 60 lượt thích
- Sản phẩm 1015 (Aloe Vera Gel): 72 lượt thích

### 6. **Reviews**

25 reviews cho 5 sản phẩm đánh giá cao (1006-1010):

- Mỗi sản phẩm: 5 reviews
- Rating: 4-5 sao
- Bình luận chi tiết bằng tiếng Việt
- Một số có hình ảnh minh họa

## 🚀 Cách Sử Dụng

### Bước 1: Chuẩn bị Database

Đảm bảo database `OneShop` đã được tạo và các bảng cơ bản đã tồn tại.

### Bước 2: Chạy Script SQL

#### **Sử dụng SQL Server Management Studio (SSMS):**

```sql
1. Mở SSMS
2. Connect tới SQL Server
3. Mở file cosmetics_sample_data.sql
4. Nhấn F5 hoặc Execute để chạy script
```

#### **Sử dụng Command Line:**

```bash
sqlcmd -S localhost\SQLEXPRESS -d OneShop -i cosmetics_sample_data.sql
```

#### **Sử dụng Azure Data Studio:**

```sql
1. Mở Azure Data Studio
2. Connect tới database OneShop
3. Mở file cosmetics_sample_data.sql
4. Run (Ctrl+Shift+E)
```

### Bước 3: Kiểm Tra Kết Quả

Script sẽ tự động in ra thống kê:

- Tổng số sản phẩm
- Top 5 bán chạy
- Top 5 đánh giá cao
- Top 5 yêu thích
- Top 5 mới nhất

## 📊 Chi Tiết Sản Phẩm Theo Danh Mục

### 🔥 Top 5 Bán Chạy (Best Selling)

| ID   | Tên Sản Phẩm            | Đã Bán | Giá      |
| ---- | ----------------------- | ------ | -------- |
| 1004 | Mặt Nạ Some By Mi       | 1,520  | 35,000đ  |
| 1001 | Serum Vitamin C Klairs  | 1,250  | 385,000đ |
| 1002 | Kem Chống Nắng La Roche | 980    | 445,000đ |
| 1003 | Nước Tẩy Trang Bioderma | 875    | 295,000đ |
| 1005 | Sữa Rửa Mặt CeraVe      | 760    | 285,000đ |

### ⭐ Top 5 Đánh Giá Cao (Top Rated)

| ID   | Tên Sản Phẩm             | Avg Rating | Reviews |
| ---- | ------------------------ | ---------- | ------- |
| 1007 | The Ordinary Niacinamide | 4.9⭐      | 5       |
| 1006 | Neutrogena Hydro Boost   | 4.8⭐      | 5       |
| 1008 | Dior Lip Glow            | 4.7⭐      | 5       |
| 1009 | Laneige Cushion          | 4.6⭐      | 5       |
| 1010 | Innisfree Eye Cream      | 4.5⭐      | 5       |

### ❤️ Top 5 Yêu Thích (Most Favorited)

| ID   | Tên Sản Phẩm              | Lượt Thích |
| ---- | ------------------------- | ---------- |
| 1015 | Aloe Vera Nature Republic | 72 ❤️      |
| 1014 | Xịt Khoáng Avène          | 60 ❤️      |
| 1012 | Water Sleeping Mask       | 52 ❤️      |
| 1011 | Toner AHA BHA Some By Mi  | 45 ❤️      |
| 1013 | Moroccanoil Treatment     | 38 ❤️      |

### ✨ Top 5 Mới Nhất (Newest)

| ID   | Tên Sản Phẩm            | Ngày Thêm    |
| ---- | ----------------------- | ------------ |
| 1020 | Laura Mercier Powder    | Hôm qua      |
| 1016 | Fenty Beauty Foundation | 2 ngày trước |
| 1017 | Innisfree Clay Mask     | 3 ngày trước |
| 1019 | Maybelline Mascara      | 4 ngày trước |
| 1018 | The Ordinary Retinol    | 5 ngày trước |

## 🔗 Các Endpoint Liên Quan

Sau khi import dữ liệu, truy cập các URL sau để xem kết quả:

### User Dashboard & Top 20

- **Dashboard**: `http://localhost:8080/user/dashboard`
- **Top 20 Hub**: `http://localhost:8080/user/top20`

### Danh Mục Sản Phẩm

- **Mới nhất**: `http://localhost:8080/newest`
- **Bán chạy**: `http://localhost:8080/best-selling`
- **Đánh giá cao**: `http://localhost:8080/top-rated`
- **Yêu thích nhất**: `http://localhost:8080/most-favorited`

## 🖼️ Nguồn Hình Ảnh

Tất cả hình ảnh sản phẩm được lấy từ **Unsplash.com** - nền tảng ảnh miễn phí chất lượng cao:

- URL format: `https://images.unsplash.com/photo-[id]?w=500`
- Các ảnh liên quan đến mỹ phẩm, skincare, makeup
- Có thể thay đổi `w=500` thành `w=800`, `w=1000` để tải ảnh lớn hơn

## ⚠️ Lưu Ý

### Trước Khi Chạy Script

1. **Backup Database**:

   ```sql
   BACKUP DATABASE OneShop TO DISK = 'E:\Backup\OneShop_backup.bak'
   ```

2. **Kiểm tra Role**:

   - Đảm bảo bảng `roles` đã có "VENDOR" và "USER"
   - Script sẽ tự động tạo nếu chưa có

3. **ID Conflict**:
   - Script sử dụng ID cố định (1001-1020 cho products)
   - Nếu đã có products với ID này, comment dòng `DELETE FROM products WHERE shop_id = 200;`

### Sau Khi Chạy Script

1. **Login Vendor**:

   - Username: `cosmetic_vendor`
   - Password: `vendor123`
   - Có thể quản lý 20 sản phẩm mỹ phẩm

2. **Login Customer**:
   - Username: `customer1` đến `customer10`
   - Password: `vendor123`
   - Test chức năng wishlist, review

## 🧪 Kiểm Tra Dữ Liệu

### Truy vấn kiểm tra nhanh:

```sql
-- Đếm số sản phẩm
SELECT COUNT(*) AS total_products
FROM products
WHERE shop_id = 200;

-- Top bán chạy
SELECT TOP 5 name, sold
FROM products
WHERE shop_id = 200
ORDER BY sold DESC;

-- Sản phẩm có nhiều wishlist nhất
SELECT p.name, COUNT(w.id) AS wishlist_count
FROM products p
LEFT JOIN wishlists w ON p.id = w.product_id
WHERE p.shop_id = 200
GROUP BY p.name
ORDER BY wishlist_count DESC;

-- Sản phẩm có rating cao nhất
SELECT p.name, AVG(CAST(r.rating AS FLOAT)) AS avg_rating, COUNT(r.id) AS review_count
FROM products p
LEFT JOIN reviews r ON p.id = r.product_id
WHERE p.shop_id = 200
GROUP BY p.name
HAVING COUNT(r.id) > 0
ORDER BY avg_rating DESC, review_count DESC;

-- Sản phẩm mới nhất
SELECT TOP 5 name, created_at
FROM products
WHERE shop_id = 200
ORDER BY created_at DESC;
```

## 🛠️ Xử Lý Sự Cố

### Lỗi: "Violation of PRIMARY KEY constraint"

**Nguyên nhân**: ID đã tồn tại
**Giải pháp**: Thay đổi ID trong script hoặc xóa dữ liệu cũ

### Lỗi: "Cannot insert duplicate key"

**Nguyên nhân**: Username hoặc email đã tồn tại
**Giải pháp**: Đổi username/email hoặc xóa user cũ

### Lỗi: "Foreign key constraint"

**Nguyên nhân**: Thiếu bảng liên quan
**Giải pháp**: Chạy migration JPA trước, hoặc tạo bảng thủ công

### Hình ảnh không hiển thị

**Nguyên nhân**: URL Unsplash bị chặn hoặc thay đổi
**Giải pháp**:

1. Kiểm tra kết nối internet
2. Thay đổi URL sang nguồn khác
3. Download ảnh về local và cập nhật path

## 📝 Tùy Chỉnh

### Thay đổi số lượng wishlist:

```sql
-- Trong phần 5, sửa số TOP X
INSERT INTO wishlists (user_id, product_id)
SELECT ...
FROM (SELECT TOP 100 1 AS n FROM sys.objects) AS numbers;  -- Thay 100
```

### Thay đổi rating:

```sql
-- Trong phần 6, sửa giá trị rating (1-5)
INSERT INTO reviews (id, product_id, user_id, rating, ...)
VALUES (..., 5, ...);  -- Thay 5 thành 1-5
```

### Thay đổi giá sản phẩm:

```sql
-- Cập nhật giá theo tỷ lệ
UPDATE products
SET price = price * 1.1  -- Tăng 10%
WHERE shop_id = 200;
```

## 📚 Tài Liệu Tham Khảo

- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Unsplash API](https://unsplash.com/developers)
- [Bootstrap Icons](https://icons.getbootstrap.com/)

## 🤝 Đóng Góp

Nếu bạn muốn thêm/sửa dữ liệu mẫu:

1. Fork repository
2. Tạo branch mới
3. Commit changes
4. Tạo Pull Request

## 📧 Liên Hệ & Hỗ Trợ

Nếu gặp vấn đề khi sử dụng script này, vui lòng:

- Tạo issue trên GitHub
- Hoặc liên hệ qua email: support@oneshop.vn

---

**Chúc bạn thành công! 🎉**

_Last updated: October 28, 2025_
