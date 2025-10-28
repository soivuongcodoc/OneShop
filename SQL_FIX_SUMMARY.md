# 🔧 Tóm Tắt Sửa Lỗi SQL Script

## ❌ Lỗi Ban Đầu

Khi execute file `cosmetics_sample_data.sql`, gặp các lỗi sau:

```
Msg 102, Level 15, State 1, Line 191
Incorrect syntax near 'r'.

Msg 132, Level 15, State 1, Line 200
The label 'https' has already been declared. Label names must be unique within a query batch or stored procedure.
```

## 🔍 Nguyên Nhân

### Lỗi 1: Incorrect syntax near 'r'

- **Vị trí**: Dòng 191 - `N'Kem Nền Fenty Beauty Pro Filt\'r Soft Matte'`
- **Nguyên nhân**: Trong SQL Server, **không thể dùng `\'` để escape dấu ngoặc đơn**
- **Giải pháp**: Phải dùng **hai dấu ngoặc đơn `''`** thay vì `\'`

### Lỗi 2: The label 'https' has already been declared

- **Vị trí**: Tất cả các dòng có URL `https://...`
- **Nguyên nhân**:
  - Trong SQL Server T-SQL, dấu `:` được dùng để khai báo **label** (nhãn)
  - Khi viết `'https://...'`, SQL Server hiểu nhầm `https` là một label thay vì string
  - URL không có prefix `N` nên SQL Server parser bị confused
- **Giải pháp**: Thêm prefix `N` trước tất cả các URL → `N'https://...'`

## ✅ Các Thay Đổi Đã Thực Hiện

### 1. Sửa escape character cho dấu ngoặc đơn

**Trước:**

```sql
N'Kem Nền Fenty Beauty Pro Filt\'r Soft Matte'
```

**Sau:**

```sql
N'Kem Nền Fenty Beauty Pro Filt''r Soft Matte'
```

### 2. Sửa ký tự lỗi trong mô tả

**Trước:**

```sql
N'Mặt nạ đất sét núi lửa Jeju làm sạch sâu, hút dầu thизлишка và se khít lỗ chân lông...'
```

**Sau:**

```sql
N'Mặt nạ đất sét núi lửa Jeju làm sạch sâu, hút dầu thừa và se khít lỗ chân lông...'
```

_(Sửa "thизлишка" (ký tự tiếng Nga lỗi) thành "thừa")_

### 3. Thêm prefix N cho tất cả URLs

Đã sửa **23 URLs** trong toàn bộ file:

- 1 URL cho shop logo
- 15 URLs cho product images (w=500)
- 7 URLs cho review images (w=300)

**Trước:**

```sql
'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=500',
```

**Sau:**

```sql
N'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=500',
```

## 📊 Thống Kê Sửa Lỗi

| Loại Sửa                       | Số Lượng | Mô Tả                     |
| ------------------------------ | -------- | ------------------------- |
| Escape character (`\'` → `''`) | 1        | Tên sản phẩm Fenty Beauty |
| Ký tự lỗi                      | 1        | "thизлишка" → "thừa"      |
| Thêm prefix N cho URLs         | 23       | Tất cả URLs trong file    |
| **Tổng cộng**                  | **25**   | **25 sửa đổi**            |

## 🚀 Cách Chạy Lại Script

### Bước 1: Xóa dữ liệu cũ (nếu đã chạy trước đó)

**🎯 KHUYẾN NGHỊ: Sử dụng file `cleanup_cosmetics_data.sql`**

File này sẽ:

- ✅ Kiểm tra dữ liệu hiện có
- ✅ Xóa theo thứ tự đúng (tránh lỗi FK)
- ✅ Tự động tắt/bật lại constraints
- ✅ Báo cáo chi tiết kết quả

**Cách chạy:**

```sql
-- Trong SSMS hoặc Azure Data Studio
1. Mở file cleanup_cosmetics_data.sql
2. Nhấn F5 hoặc Execute
3. Kiểm tra kết quả (phải thấy "✅ THÀNH CÔNG!")
```

**Hoặc dùng query thủ công:**

```sql
USE [OneShop]
GO

-- Xóa theo thứ tự (tránh lỗi FK)
DELETE FROM wishlists WHERE product_id IN (SELECT id FROM products WHERE shop_id = 200);
DELETE FROM reviews WHERE product_id IN (SELECT id FROM products WHERE shop_id = 200);
DELETE FROM cart_items WHERE product_id IN (SELECT id FROM products WHERE shop_id = 200);
DELETE FROM products WHERE shop_id = 200;
DELETE FROM shops WHERE id = 200;
DELETE FROM user_roles WHERE user_id BETWEEN 200 AND 310;
DELETE FROM users WHERE id BETWEEN 200 AND 310;
GO
```

### Bước 2: Chạy script đã sửa

**Trong SQL Server Management Studio (SSMS):**

1. Mở file `cosmetics_sample_data.sql` (đã sửa)
2. Nhấn **F5** hoặc **Execute**
3. Chờ script chạy xong

**Trong Azure Data Studio:**

1. Mở file `cosmetics_sample_data.sql`
2. Nhấn **Ctrl+Shift+E** hoặc click **Run**
3. Chờ kết quả

**Command Line:**

```bash
sqlcmd -S localhost\SQLEXPRESS -d OneShop -i cosmetics_sample_data.sql
```

### Bước 3: Kiểm tra kết quả

Script sẽ tự động in ra thống kê:

```sql
============================================================
KẾT QUẢ THÊM DỮ LIỆU MẪU MỸ PHẨM
============================================================

Tổng số sản phẩm mỹ phẩm: 20

--- TOP 5 SẢN PHẨM BÁN CHẠY ---
(5 rows)

--- TOP 5 SẢN PHẨM ĐÁNH GIÁ CAO ---
(5 rows)

--- TOP 5 SẢN PHẨM YÊU THÍCH ---
(5 rows)

--- TOP 5 SẢN PHẨM MỚI NHẤT ---
(5 rows)

============================================================
HOÀN TẤT! Dữ liệu mẫu đã được thêm thành công.
============================================================
```

## ✅ Kết Quả Mong Đợi

Nếu script chạy thành công, bạn sẽ thấy:

- ✅ Không có lỗi syntax
- ✅ Không có lỗi về label 'https'
- ✅ 20 sản phẩm được thêm vào (ID: 1001-1020)
- ✅ 1 vendor user: `cosmetic_vendor`
- ✅ 1 shop: Beauty Paradise (ID: 200)
- ✅ 10 customer users: `customer1` - `customer10`
- ✅ 267 wishlists
- ✅ 25 reviews

## 🧪 Test Trên Giao Diện

Sau khi chạy script thành công:

1. **Khởi động ứng dụng**:

   ```bash
   mvn spring-boot:run
   ```

2. **Login với user**:

   - Username: `customer1`
   - Password: `vendor123`

3. **Truy cập Top 20**:

   - Dashboard: `http://localhost:8080/user/dashboard`
   - Click nút **"Top 20"** 🏆
   - Chọn từng danh mục để xem sản phẩm

4. **Các URL để test**:
   - `/newest` - 5 sản phẩm mới nhất
   - `/best-selling` - 5 sản phẩm bán chạy nhất
   - `/top-rated` - 5 sản phẩm đánh giá cao nhất
   - `/most-favorited` - 5 sản phẩm yêu thích nhất

## 📝 Ghi Chú Quan Trọng

### Về SQL Server String Literals

1. **Unicode String**: Dùng prefix `N` trước string có ký tự Unicode

   ```sql
   N'Xin chào'  -- Đúng
   'Xin chào'   -- Có thể bị lỗi encoding
   ```

2. **Escape Single Quote**: Dùng hai dấu ngoặc đơn `''`

   ```sql
   N'It''s a beautiful day'  -- Đúng
   N'It\'s a beautiful day'  -- SAI - không work trong SQL Server
   ```

3. **URLs trong SQL**: Luôn dùng prefix `N` để tránh nhầm lẫn với labels
   ```sql
   N'https://example.com'  -- Đúng
   'https://example.com'   -- Có thể gây lỗi label
   ```

### Về Encoding

- Tất cả strings có **tiếng Việt** phải dùng prefix `N`
- URLs nên dùng prefix `N` để đảm bảo tương thích
- NVARCHAR(MAX) tự động hỗ trợ Unicode

## 🔗 Tài Liệu Tham Khảo

- [SQL Server String Literals](https://learn.microsoft.com/en-us/sql/t-sql/data-types/nchar-and-nvarchar-transact-sql)
- [T-SQL DECLARE (Labels)](https://learn.microsoft.com/en-us/sql/t-sql/language-elements/goto-transact-sql)
- [Unicode and Collation](https://learn.microsoft.com/en-us/sql/relational-databases/collations/collation-and-unicode-support)

---

**✨ File SQL đã được sửa và sẵn sàng sử dụng!**

_Last updated: October 28, 2025_
