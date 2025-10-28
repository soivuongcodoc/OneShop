# 🚀 Quick Start: Dữ Liệu Mẫu Mỹ Phẩm

## 📝 Tóm Tắt Nhanh

Hướng dẫn nhanh để thêm 20 sản phẩm mỹ phẩm vào OneShop.

## 🎯 Quy Trình 2 Bước

### ✅ BƯỚC 1: Xóa dữ liệu cũ (nếu đã chạy trước đó)

```sql
-- Chạy file này trong SSMS hoặc Azure Data Studio
cleanup_cosmetics_data.sql
```

**Kết quả mong đợi:**

```
✅ THÀNH CÔNG! Đã xóa sạch dữ liệu mẫu mỹ phẩm.
Bạn có thể chạy lại cosmetics_sample_data.sql ngay bây giờ.
```

### ✅ BƯỚC 2: Thêm dữ liệu mới

```sql
-- Chạy file này trong SSMS hoặc Azure Data Studio
cosmetics_sample_data.sql
```

**Kết quả mong đợi:**

```
============================================================
HOÀN TẤT! Dữ liệu mẫu đã được thêm thành công.
Có thể truy cập /user/top20 để xem các danh mục sản phẩm.
============================================================
```

## 📦 Dữ Liệu Được Tạo

| Loại         | Số Lượng | Chi Tiết                                           |
| ------------ | -------- | -------------------------------------------------- |
| 🛍️ Products  | 20       | ID: 1001-1020, Shop ID: 200                        |
| 👤 Vendor    | 1        | Username: `cosmetic_vendor`, Password: `vendor123` |
| 🏪 Shop      | 1        | Beauty Paradise (ID: 200)                          |
| 👥 Customers | 10       | `customer1`-`customer10`, Password: `vendor123`    |
| ❤️ Wishlists | 267      | Phân bổ cho 5 sản phẩm yêu thích                   |
| ⭐ Reviews   | 25       | 5 reviews cho mỗi sản phẩm đánh giá cao            |

## 🧪 Test Ngay

1. **Khởi động ứng dụng:**

   ```bash
   mvn spring-boot:run
   ```

2. **Login:**

   - Username: `customer1`
   - Password: `vendor123`

3. **Truy cập:**

   - Dashboard: http://localhost:8080/user/dashboard
   - Click nút **"Top 20"** 🏆

4. **Xem các danh mục:**
   - http://localhost:8080/newest - 5 sản phẩm mới nhất
   - http://localhost:8080/best-selling - 5 sản phẩm bán chạy
   - http://localhost:8080/top-rated - 5 sản phẩm đánh giá cao
   - http://localhost:8080/most-favorited - 5 sản phẩm yêu thích

## 📚 Files Liên Quan

| File                         | Mô Tả                                  |
| ---------------------------- | -------------------------------------- |
| `cosmetics_sample_data.sql`  | ✨ File chính - Tạo dữ liệu mẫu        |
| `cleanup_cosmetics_data.sql` | 🧹 Xóa dữ liệu cũ trước khi chạy lại   |
| `COSMETICS_DATA_GUIDE.md`    | 📖 Hướng dẫn chi tiết đầy đủ           |
| `SQL_FIX_SUMMARY.md`         | 🔧 Tài liệu sửa lỗi và troubleshooting |

## ⚠️ Lưu Ý Quan Trọng

1. **Luôn chạy cleanup trước khi chạy lại script chính** để tránh lỗi duplicate key
2. **Backup database trước** nếu bạn có dữ liệu quan trọng
3. **Kiểm tra kết quả** sau mỗi bước bằng các query kiểm tra

## 🆘 Troubleshooting

### Lỗi: "Violation of PRIMARY KEY constraint"

➡️ **Giải pháp:** Chạy `cleanup_cosmetics_data.sql` trước

### Lỗi: "The label 'https' has already been declared"

➡️ **Giải pháp:** File đã được sửa, tải lại `cosmetics_sample_data.sql`

### Lỗi: Foreign key constraint

➡️ **Giải pháp:** Chạy `cleanup_cosmetics_data.sql` (script tự động xử lý FK)

## ✅ Checklist

- [ ] Đã chạy `cleanup_cosmetics_data.sql`
- [ ] Thấy message "✅ THÀNH CÔNG!"
- [ ] Chạy `cosmetics_sample_data.sql`
- [ ] Thấy message "HOÀN TẤT!"
- [ ] Test login với `customer1` / `vendor123`
- [ ] Truy cập `/user/top20` thành công
- [ ] Xem được 4 danh mục sản phẩm

## 📞 Hỗ Trợ

Nếu gặp vấn đề:

1. Đọc `SQL_FIX_SUMMARY.md` để biết cách sửa lỗi
2. Đọc `COSMETICS_DATA_GUIDE.md` để hiểu chi tiết
3. Kiểm tra console output để xem lỗi cụ thể

---

**Happy Coding! 🎉**

_Last updated: October 28, 2025_
