# 📊 PHÂN TÍCH CÁC FILE SQL TRONG DỰ ÁN

## 🎯 CÂU HỎI: CÓ THỂ XÓA TẤT CẢ FILE SQL KHÔNG?

### ✅ **TRƯỚC LÀI: CÓ, BẠN CÓ THỂ XÓA**

**LÝ DO:**

- ✅ Các file SQL chỉ là **scripts hỗ trợ**, không phải code chính của ứng dụng
- ✅ Ứng dụng Spring Boot **không phụ thuộc** vào các file SQL này khi chạy
- ✅ Database schema được quản lý bởi **JPA/Hibernate** (auto-generate từ Entity classes)
- ✅ Backup từ SSMS sẽ **đầy đủ hơn** các file SQL riêng lẻ

---

## 📁 DANH SÁCH 10 FILE SQL HIỆN CÓ

### 1️⃣ **NHÓM SCHEMA (Thay đổi cấu trúc database)**

| File                                 | Mục đích                                   | Có thể xóa? | Ghi chú                              |
| ------------------------------------ | ------------------------------------------ | ----------- | ------------------------------------ |
| `alter_addresses_add_is_default.sql` | Thêm cột `is_default` vào bảng `addresses` | ✅ **CÓ**   | Đã chạy rồi, cột đã tồn tại trong DB |
| `alter_product_name_to_max.sql`      | Đổi `name` column thành `NVARCHAR(MAX)`    | ✅ **CÓ**   | Đã chạy rồi, schema đã update        |

### 2️⃣ **NHÓM DATA (Dữ liệu mẫu)**

| File                         | Mục đích                       | Có thể xóa?    | Ghi chú                                      |
| ---------------------------- | ------------------------------ | -------------- | -------------------------------------------- |
| `cosmetics_sample_data.sql`  | Insert 20 sản phẩm mỹ phẩm mẫu | ⚠️ **NÊN GIỮ** | Dữ liệu demo cho Top 20, có thể cần chạy lại |
| `cleanup_cosmetics_data.sql` | Xóa dữ liệu mẫu mỹ phẩm        | ⚠️ **NÊN GIỮ** | Dùng để clean up trước khi re-insert         |
| `insert_payment_methods.sql` | Insert COD, VNPAY, MOMO        | ⚠️ **NÊN GIỮ** | Dữ liệu bắt buộc cho thanh toán              |

### 3️⃣ **NHÓM FIX (Sửa lỗi encoding/data)**

| File                               | Mục đích                     | Có thể xóa? | Ghi chú                           |
| ---------------------------------- | ---------------------------- | ----------- | --------------------------------- |
| `fix_product_names_vietnamese.sql` | Sửa tên sản phẩm có font lỗi | ✅ **CÓ**   | Đã fix rồi, không cần nữa         |
| `fix_vietnamese_encoding.sql`      | Fix collation cho Vietnamese | ✅ **CÓ**   | Chỉ dùng 1 lần                    |
| `check_collation.sql`              | Kiểm tra collation của DB    | ✅ **CÓ**   | Script kiểm tra, không quan trọng |

### 4️⃣ **NHÓM UTILITY (Tiện ích)**

| File                     | Mục đích                             | Có thể xóa? | Ghi chú       |
| ------------------------ | ------------------------------------ | ----------- | ------------- |
| `update_image_paths.sql` | Update đường dẫn ảnh từ web về local | ✅ **CÓ**   | Đã update rồi |

### 5️⃣ **NHÓM INITIAL SCHEMA (Schema ban đầu)**

| File          | Mục đích                | Có thể xóa?    | Ghi chú                            |
| ------------- | ----------------------- | -------------- | ---------------------------------- |
| `oneshop.sql` | Schema database ban đầu | ⚠️ **XEM LẠI** | Có thể là schema gốc, nên kiểm tra |

---

## 🎯 KHUYẾN NGHỊ

### ✅ **CÓ THỂ XÓA AN TOÀN (7 files):**

```
✅ alter_addresses_add_is_default.sql
✅ alter_product_name_to_max.sql
✅ fix_product_names_vietnamese.sql
✅ fix_vietnamese_encoding.sql
✅ check_collation.sql
✅ update_image_paths.sql
✅ oneshop.sql (nếu đã backup DB từ SSMS)
```

**LÝ DO:** Đã chạy xong, schema/data đã update trong database thực tế.

---

### ⚠️ **NÊN GIỮ LẠI (3 files):**

```
⚠️ cosmetics_sample_data.sql
⚠️ cleanup_cosmetics_data.sql
⚠️ insert_payment_methods.sql
```

**LÝ DO:**

- Có thể cần **chạy lại** khi:
  - Reset database về trạng thái ban đầu
  - Demo cho giáo viên/đánh giá đồ án
  - Deploy lên server mới
  - Test lại chức năng Top 20

**HOẶC:** Nếu đã backup DB đầy đủ từ SSMS rồi thì **CÓ THỂ XÓA HẾT**.

---

## 📦 HƯỚNG DẪN BACKUP DATABASE TỪ SSMS

### **BƯỚC 1: BACKUP DATABASE**

```sql
-- Trong SSMS, click phải vào database "OneShop" → Tasks → Back Up...

-- Hoặc dùng T-SQL:
BACKUP DATABASE [OneShop]
TO DISK = 'E:\OneShop-vendor\backups\OneShop_FULL_20251029.bak'
WITH FORMAT, INIT, COMPRESSION,
     NAME = 'OneShop-Full Database Backup',
     STATS = 10;
GO
```

**Lưu file backup vào thư mục riêng, KHÔNG trong project Git.**

---

### **BƯỚC 2: BACKUP CHỈ SCHEMA (Không có data)**

```sql
-- Nếu chỉ muốn backup cấu trúc database:
-- Right-click DB → Tasks → Generate Scripts...
-- Chọn "Schema only" → Save to file
```

---

### **BƯỚC 3: BACKUP CHỈ DATA (Không có schema)**

```sql
-- Generate Scripts → Chọn "Data only"
```

---

### **BƯỚC 4: XÁC NHẬN BACKUP THÀNH CÔNG**

```sql
-- Kiểm tra backup file
RESTORE HEADERONLY
FROM DISK = 'E:\OneShop-vendor\backups\OneShop_FULL_20251029.bak';
GO
```

---

## 🗑️ CÁC BƯỚC XÓA FILE SQL

### **SAU KHI BACKUP XONG:**

1. **Xóa tất cả 10 file .sql** trong thư mục root của project:

```
E:\OneShop-vendor\OneShop-vendor\*.sql
```

2. **Commit Git:**

```bash
git add .
git commit -m "Removed SQL scripts after backing up database to SSMS"
```

3. **Lưu file backup (.bak) vào thư mục riêng:**

```
E:\OneShop-vendor\backups\
hoặc
E:\Database-Backups\OneShop\
```

**⚠️ QUAN TRỌNG:** Thêm `*.bak` vào `.gitignore` để không push file backup lên Git (file backup rất nặng).

---

## 📋 CHECKLIST TRƯỚC KHI XÓA

- [ ] ✅ Database đang chạy tốt, không có lỗi
- [ ] ✅ Đã backup database từ SSMS thành công
- [ ] ✅ Đã kiểm tra file backup (.bak) có mở được
- [ ] ✅ Đã test restore database từ backup (optional nhưng nên làm)
- [ ] ✅ Lưu file backup ở nơi an toàn, KHÔNG trong project
- [ ] ✅ Thêm `*.bak` vào `.gitignore`

---

## 💡 LỢI ÍCH KHI XÓA FILE SQL

| Lợi ích                         | Giải thích                         |
| ------------------------------- | ---------------------------------- |
| ✅ **Project gọn gàng hơn**     | Bớt 10 files rác trong root folder |
| ✅ **Giảm kích thước Git repo** | File SQL đôi khi rất nặng          |
| ✅ **Tránh nhầm lẫn**           | Không lo chạy nhầm script cũ       |
| ✅ **Chuẩn hóa backup**         | Dùng backup SSMS là best practice  |

---

## ⚠️ RỦI RO (Nhỏ)

| Rủi ro                                 | Giải pháp                                                               |
| -------------------------------------- | ----------------------------------------------------------------------- |
| ⚠️ Mất script demo data                | Backup file SQL trước khi xóa, hoặc giữ lại `cosmetics_sample_data.sql` |
| ⚠️ Không tái tạo được DB từ đầu        | Backup SSMS đầy đủ schema + data là đủ                                  |
| ⚠️ Cần migration script cho production | Không áp dụng, đây là đồ án học tập                                     |

---

## 🎯 KẾT LUẬN

### ✅ **CÓ THỂ XÓA HẾT 10 FILE SQL**

**ĐIỀU KIỆN:**

1. ✅ Đã backup database từ SSMS thành công
2. ✅ File backup (.bak) đã kiểm tra và hoạt động tốt
3. ✅ Lưu file backup ở nơi an toàn

**KHUYẾN NGHỊ:**

- Nếu lo lắng: Giữ lại `cosmetics_sample_data.sql` và `insert_payment_methods.sql`
- Nếu tự tin: Xóa hết 10 file, chỉ dùng backup SSMS

---

## 📞 HƯỚNG DẪN RESTORE DATABASE (Nếu cần)

```sql
-- Trong SSMS:
-- 1. Right-click "Databases" → Restore Database...
-- 2. Chọn "Device" → Browse → Chọn file .bak
-- 3. Click OK

-- Hoặc dùng T-SQL:
USE master;
GO

RESTORE DATABASE [OneShop_Restored]
FROM DISK = 'E:\OneShop-vendor\backups\OneShop_FULL_20251029.bak'
WITH
    MOVE 'OneShop' TO 'C:\SQLData\OneShop_Restored.mdf',
    MOVE 'OneShop_log' TO 'C:\SQLData\OneShop_Restored_log.ldf',
    REPLACE,
    STATS = 10;
GO
```

---

**Ngày tạo:** 29/10/2025  
**Phiên bản:** 1.0

**👉 QUYẾT ĐỊNH CỦA BẠN: Backup xong thì xóa hết được, nhưng nên giữ lại 3 files data nếu muốn demo lại sau này.**
