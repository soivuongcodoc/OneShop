# 💳 HƯỚNG DẪN THÊM PHƯƠNG THỨC THANH TOÁN

## 📋 DANH SÁCH PHƯƠNG THỨC

Script sẽ thêm **5 phương thức thanh toán** phổ biến:

| ID  | Tên               | Tên hiển thị                   | Trạng thái   |
| --- | ----------------- | ------------------------------ | ------------ |
| 1   | **COD**           | Thanh toán khi nhận hàng (COD) | ✅ Hoạt động |
| 2   | **VNPAY**         | Thanh toán qua VNPAY           | ✅ Hoạt động |
| 3   | **MOMO**          | Thanh toán qua Ví MoMo         | ✅ Hoạt động |
| 4   | **ZALOPAY**       | Thanh toán qua ZaloPay         | ✅ Hoạt động |
| 5   | **BANK_TRANSFER** | Chuyển khoản ngân hàng         | ✅ Hoạt động |

---

## 🚀 CÁCH SỬ DỤNG

### **Bước 1: Mở SQL Server Management Studio (SSMS)**

1. Kết nối đến database `oneshop`
2. Mở file: `insert_payment_methods.sql`

### **Bước 2: Execute Script**

```sql
-- Chạy file này trong SSMS
insert_payment_methods.sql
```

### **Bước 3: Kiểm tra kết quả**

Script sẽ hiển thị:

```
========================================
  THÊM PHƯƠNG THỨC THANH TOÁN
========================================

✓ Đã thêm: COD - Thanh toán khi nhận hàng
✓ Đã thêm: VNPAY - Thanh toán qua VNPAY
✓ Đã thêm: MOMO - Thanh toán qua Ví MoMo
✓ Đã thêm: ZALOPAY - Thanh toán qua ZaloPay
✓ Đã thêm: BANK_TRANSFER - Chuyển khoản ngân hàng

========================================
  KẾT QUẢ
========================================
(Hiển thị bảng payment_methods)

========================================
  HOÀN TẤT!
========================================
Tổng số phương thức thanh toán: 5
```

---

## 🔧 CHỈNH SỬA

### **Tắt một phương thức thanh toán:**

```sql
UPDATE payment_methods
SET active = 0
WHERE name = 'ZALOPAY';
```

### **Bật lại phương thức thanh toán:**

```sql
UPDATE payment_methods
SET active = 1
WHERE name = 'ZALOPAY';
```

### **Thêm phương thức thanh toán mới:**

```sql
INSERT INTO payment_methods (name, display_name, active)
VALUES (N'SHOPEE_PAY', N'Thanh toán qua ShopeePay', 1);
```

### **Xóa phương thức thanh toán:**

```sql
-- ⚠️ CHÚ Ý: Chỉ xóa nếu chưa có đơn hàng nào dùng
DELETE FROM payment_methods
WHERE name = 'BANK_TRANSFER';
```

---

## 📊 KIỂM TRA TRONG APP

### **1. Kiểm tra trong Checkout:**

```
http://localhost:8080/user/checkout
```

→ Dropdown "Phương thức thanh toán" sẽ hiển thị:

- ✅ Thanh toán khi nhận hàng (COD)
- ✅ Thanh toán qua VNPAY
- ✅ Thanh toán qua Ví MoMo
- ✅ Thanh toán qua ZaloPay
- ✅ Chuyển khoản ngân hàng

### **2. Test thanh toán:**

1. Thêm sản phẩm vào giỏ hàng
2. Click "Thanh toán"
3. Chọn phương thức thanh toán
4. Điền địa chỉ giao hàng
5. Nhấn "Đặt hàng"

---

## 🎯 TÍCH HỢP VNPAY/MOMO

### **Cấu hình VNPAY:**

```properties
# application.properties
vnpay.merchant.id=YOUR_MERCHANT_ID
vnpay.hash.secret=YOUR_HASH_SECRET
vnpay.api.url=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.return.url=http://localhost:8080/payment/vnpay-return
```

### **Cấu hình MOMO:**

```properties
# application.properties
momo.partner.code=YOUR_PARTNER_CODE
momo.access.key=YOUR_ACCESS_KEY
momo.secret.key=YOUR_SECRET_KEY
momo.endpoint=https://test-payment.momo.vn/v2/gateway/api/create
momo.return.url=http://localhost:8080/payment/momo-return
```

---

## 📝 CẤU TRÚC BẢNG

### **payment_methods:**

```sql
CREATE TABLE payment_methods (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) UNIQUE NOT NULL,      -- Tên kỹ thuật (COD, VNPAY, MOMO)
    display_name NVARCHAR(100) NOT NULL,    -- Tên hiển thị cho user
    active BIT NOT NULL DEFAULT 1           -- Trạng thái hoạt động
);
```

---

## 🔥 QUICK COMMANDS

### **Xem tất cả phương thức thanh toán:**

```sql
SELECT * FROM payment_methods ORDER BY id;
```

### **Xem phương thức thanh toán đang hoạt động:**

```sql
SELECT * FROM payment_methods WHERE active = 1 ORDER BY id;
```

### **Reset toàn bộ:**

```sql
-- ⚠️ Chỉ dùng khi test, sẽ xóa tất cả!
DELETE FROM payment_methods;
-- Sau đó chạy lại insert_payment_methods.sql
```

---

## ✅ CHECKLIST

- [ ] Đã execute `insert_payment_methods.sql` trong SSMS
- [ ] Kiểm tra có 5 phương thức thanh toán trong database
- [ ] Tất cả phương thức đều `active = 1`
- [ ] Restart Spring Boot app
- [ ] Kiểm tra dropdown trong trang checkout
- [ ] Test đặt hàng với từng phương thức thanh toán

---

**Chúc bạn thành công!** 🎉
