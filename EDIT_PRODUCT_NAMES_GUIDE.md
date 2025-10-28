# 📝 HƯỚNG DẪN SỬA TÊN SẢN PHẨM TIẾNG VIỆT

## 🎯 2 Cách Sửa Tên Sản Phẩm

---

## ✅ CÁCH 1: CHẠY SQL (NHANH - KHUYẾN NGHỊ)

### **Execute file SQL:**

```sql
fix_product_names_vietnamese.sql
```

Script sẽ tự động update tên cho **20 sản phẩm** với tiếng Việt có dấu đầy đủ.

**Ưu điểm:**

- ✅ Nhanh (1 click)
- ✅ Update hàng loạt
- ✅ Đảm bảo encoding đúng (có prefix `N`)

---

## ✅ CÁCH 2: EDIT TRỰC TIẾP TRONG SSMS

### **Bước 1: Enable Edit Mode**

1. Right-click vào bảng `dbo.products`
2. Chọn **"Edit Top 200 Rows"**

### **Bước 2: Edit Từng Cell**

1. Click vào cell trong cột `name` mà bạn muốn sửa
2. Nhấn **F2** hoặc double-click để edit
3. Gõ tên mới với tiếng Việt (VD: `Kem Chống Nắng Biore`)
4. Nhấn **Enter** hoặc click ra ngoài → Tự động save

### **Lưu Ý Quan Trọng:**

⚠️ Khi edit trực tiếp, SSMS **TỰ ĐỘNG** thêm prefix `N` nếu detect Unicode!

### **Ví Dụ:**

```
ID: 1001
Tên cũ: Serum du?...  ❌
Tên mới: Serum Vitamin C Klairs Freshly Juiced  ✅
```

---

## 🔧 CÁCH 3: UPDATE BẰNG QUERY

### **Update 1 sản phẩm:**

```sql
UPDATE products
SET name = N'Kem Chống Nắng Biore UV Aqua Rich'
WHERE id = 1002;
```

### **Update nhiều sản phẩm:**

```sql
UPDATE products SET name = N'Serum Vitamin C Klairs' WHERE id = 1001;
UPDATE products SET name = N'Kem Chống Nắng Biore' WHERE id = 1002;
UPDATE products SET name = N'Nước Tẩy Trang Bioderma' WHERE id = 1003;
-- ... etc
```

**⚠️ QUAN TRỌNG:**

- Luôn thêm **`N`** trước chuỗi tiếng Việt: `N'text'`
- Nếu thiếu `N`, tiếng Việt sẽ bị lỗi!

---

## 📋 DANH SÁCH TÊN MẪU (Copy & Paste)

```
1001 | Serum Vitamin C Klairs Freshly Juiced
1002 | Kem Chống Nắng Biore UV Aqua Rich
1003 | Nước Tẩy Trang Bioderma Sensibio H2O
1004 | Mặt Nạ Giấy Some By Mi Yuja Niacin
1005 | Sữa Rửa Mặt CeraVe Foaming Cleanser
1006 | Kem Dưỡng Ẩm Neutrogena Hydro Boost
1007 | Tinh Chất Serum The Ordinary Niacinamide
1008 | Son Dưỡng Môi Vaseline Lip Therapy
1009 | Phấn Nước Cushion Laneige Neo
1010 | Kem Dưỡng Mắt Innisfree Green Tea
1011 | Toner Làm Sạch AHA BHA COSRX
1012 | Mặt Nạ Ngủ Water Sleeping Mask Laneige
1013 | Tinh Dầu Argan Moroccanoil Treatment
1014 | Nước Khoáng Xịt Avène Thermal Water
1015 | Gel Lô Hội 99% Nature Republic Aloe Vera
1016 | Kem Nền Fenty Beauty Pro Filt'r Soft Matte
1017 | Mặt Nạ Đất Sét Innisfree Super Volcanic
1018 | Tinh Chất Retinol The Ordinary Retinol 0.5%
1019 | Phấn Phủ Bột Laura Mercier Translucent
1020 | Mascara Cong Mi Maybelline Sky High
```

---

## 🚀 KHUYẾN NGHỊ

### **Dùng CÁCH 1 (chạy SQL file) nếu:**

- ✅ Muốn update hàng loạt
- ✅ Muốn nhanh chóng
- ✅ Không muốn edit từng cái

### **Dùng CÁCH 2 (edit grid) nếu:**

- ✅ Chỉ sửa vài sản phẩm
- ✅ Muốn xem ngay kết quả
- ✅ Thích edit trực quan

---

## ✅ KIỂM TRA KẾT QUẢ

Sau khi update, chạy query:

```sql
SELECT
    id,
    name AS [Tên Sản Phẩm],
    description AS [Mô Tả]
FROM products
WHERE id BETWEEN 1001 AND 1020
ORDER BY id;
```

Nếu cột `name` giờ hiển thị tiếng Việt đúng như cột `description` → **THÀNH CÔNG!** ✅

---

## 🎯 LƯU Ý

1. **Prefix N là BẮT BUỘC** khi dùng UPDATE query: `N'text'`
2. **Edit grid trong SSMS** tự động thêm `N` (an toàn hơn)
3. Sau khi sửa, **refresh trang web** để xem kết quả
4. Không cần restart app, chỉ cần **Ctrl + F5** trên browser

---

**Chúc bạn thành công!** 🎉
