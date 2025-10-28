# 📍 HƯỚNG DẪN: QUẢN LÝ ĐỊA CHỈ TRONG PROFILE

## ✅ ĐÃ THỰC HIỆN

### **1. Cập nhật Entity: `Address.java`** ✅

Thêm cột `isDefault` để đánh dấu địa chỉ mặc định:

```java
@Builder.Default
@Column(name = "is_default", nullable = false)
private Boolean isDefault = false;
```

### **2. Cập nhật Controller: `UserController.java`** ✅

Thêm load addresses vào profile:

```java
@GetMapping("/profile")
public String profile(Model model) {
    User u = userService.getCurrentUser();
    var addresses = addressRepository.findByUserId(u.getId());
    model.addAttribute("user", u);
    model.addAttribute("addresses", addresses);
    return "user/profile";
}
```

### **3. Cập nhật Controller: `AddressController.java`** ✅

Thêm các chức năng:

- ✅ Thêm địa chỉ mới (có thể đặt làm mặc định)
- ✅ Xóa địa chỉ
- ✅ Đặt địa chỉ làm mặc định

### **4. Tạo giao diện mới: `user/profile.html`** ✅

Giao diện đẹp với:

- ✅ Thông tin tài khoản (username, email)
- ✅ Danh sách địa chỉ nhận hàng
- ✅ Badge "Mặc định" cho địa chỉ mặc định
- ✅ Modal thêm địa chỉ mới
- ✅ Nút "Đặt mặc định" và "Xóa" cho mỗi địa chỉ

---

## 🚀 CÁCH CÀI ĐẶT

### **Bước 1: Chạy SQL để thêm cột is_default**

Execute file này trong SSMS:

```sql
alter_addresses_add_is_default.sql
```

Script sẽ:

- ✅ Thêm cột `is_default` vào bảng `addresses`
- ✅ Tự động set địa chỉ đầu tiên của mỗi user làm mặc định

---

### **Bước 2: Restart Spring Boot App**

```bash
# Trong terminal, nhấn Ctrl+C
# Sau đó chạy lại:
mvn spring-boot:run
```

---

## 🎯 CHỨC NĂNG

### **1. Xem danh sách địa chỉ**

- Hiển thị tất cả địa chỉ của user
- Địa chỉ mặc định có badge "Mặc định" màu xanh
- Card của địa chỉ mặc định có viền trái màu xanh

### **2. Thêm địa chỉ mới**

- Click nút "Thêm địa chỉ mới"
- Điền form:
  - Họ tên người nhận (required)
  - Số điện thoại (required)
  - Địa chỉ chi tiết (required)
  - Checkbox "Đặt làm địa chỉ mặc định"
- Click "Lưu địa chỉ"

### **3. Đặt địa chỉ làm mặc định**

- Click nút "Đặt mặc định" trên địa chỉ muốn đặt
- Confirm
- Địa chỉ cũ sẽ tự động bỏ mặc định
- Địa chỉ mới được set làm mặc định

### **4. Xóa địa chỉ**

- Click nút "Xóa" trên địa chỉ muốn xóa
- Confirm
- Địa chỉ bị xóa khỏi hệ thống

---

## 📊 CẤU TRÚC BẢNG

### **Bảng `addresses`:**

```sql
CREATE TABLE addresses (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,               -- FK to users.id
    name NVARCHAR(255) NOT NULL,           -- Tên người nhận
    phone NVARCHAR(20) NOT NULL,           -- Số điện thoại
    address NVARCHAR(500) NOT NULL,        -- Địa chỉ chi tiết
    active BIT NOT NULL DEFAULT 1,         -- Trạng thái
    is_default BIT NOT NULL DEFAULT 0      -- Địa chỉ mặc định
);
```

---

## 🎨 GIAO DIỆN

### **Layout:**

```
┌─────────────────────────────────────────────────────────┐
│  Hồ sơ của tôi                [Quay lại Dashboard]      │
├──────────────┬──────────────────────────────────────────┤
│              │                                           │
│  THÔNG TIN   │         ĐỊA CHỈ NHẬN HÀNG                │
│  TÀI KHOẢN   │  [+ Thêm địa chỉ mới]                   │
│              │                                           │
│  Username    │  ┌────────────────────────────────┐      │
│  Email       │  │ 👤 Nguyễn Văn A  [Mặc định]   │      │
│  [Cập nhật]  │  │ 📞 0912345678                  │      │
│              │  │ 📍 123 ABC, XYZ, HCM          │      │
│              │  │         [⭐ Đặt MĐ] [🗑️ Xóa]  │      │
│              │  └────────────────────────────────┘      │
│              │                                           │
│              │  ┌────────────────────────────────┐      │
│              │  │ 👤 Trần Thị B                 │      │
│              │  │ 📞 0987654321                  │      │
│              │  │ 📍 456 DEF, UVW, HN           │      │
│              │  │         [⭐ Đặt MĐ] [🗑️ Xóa]  │      │
│              │  └────────────────────────────────┘      │
└──────────────┴──────────────────────────────────────────┘
```

---

## 🔄 LOGIC ĐỊA CHỈ MẶC ĐỊNH

### **Quy tắc:**

1. **Mỗi user chỉ có 1 địa chỉ mặc định**

   - Khi set địa chỉ A làm mặc định
     → Địa chỉ cũ tự động bỏ mặc định

2. **Khi thêm địa chỉ đầu tiên**

   - Tự động set làm mặc định

3. **Khi xóa địa chỉ mặc định**

   - Cần set địa chỉ khác làm mặc định thủ công

4. **Địa chỉ mặc định được dùng khi checkout**
   - Tự động điền vào form thanh toán

---

## 🔥 API ENDPOINTS

| Method | Endpoint                           | Chức năng             |
| ------ | ---------------------------------- | --------------------- |
| GET    | `/user/profile`                    | Xem profile + địa chỉ |
| POST   | `/user/profile/update`             | Cập nhật email        |
| POST   | `/user/addresses`                  | Thêm địa chỉ mới      |
| POST   | `/user/addresses/{id}/delete`      | Xóa địa chỉ           |
| POST   | `/user/addresses/{id}/set-default` | Đặt làm mặc định      |

---

## ✅ TEST CHECKLIST

- [ ] Chạy SQL `alter_addresses_add_is_default.sql`
- [ ] Restart Spring Boot app
- [ ] Truy cập `/user/profile`
- [ ] Xem thông tin user hiển thị đúng
- [ ] Thêm địa chỉ mới thành công
- [ ] Địa chỉ mới hiển thị trong danh sách
- [ ] Set địa chỉ làm mặc định hoạt động
- [ ] Badge "Mặc định" hiển thị đúng địa chỉ
- [ ] Xóa địa chỉ hoạt động
- [ ] Modal đóng sau khi thêm địa chỉ

---

## 🎯 NÂNG CAO (Optional)

### **Tính năng có thể thêm:**

1. **Edit địa chỉ:** Sửa thông tin địa chỉ hiện có
2. **Địa chỉ văn phòng/nhà:** Tag phân loại
3. **Tọa độ GPS:** Lưu lat/lng cho giao hàng chính xác
4. **Tỉnh/Thành phố dropdown:** Chọn từ danh sách thay vì nhập tự do
5. **Phí ship theo khu vực:** Tính phí ship dựa trên địa chỉ

---

**Chúc bạn thành công!** 🎉
