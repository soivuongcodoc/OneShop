-- ============================================================
-- SQL SCRIPT: Dữ liệu mẫu cho các sản phẩm mỹ phẩm
-- Mục đích: Phục vụ chức năng Top 20 trong User Dashboard
-- - 5 Sản phẩm mới nhất
-- - 5 Sản phẩm bán chạy nhất
-- - 5 Sản phẩm đánh giá cao nhất  
-- - 5 Sản phẩm yêu thích nhất
-- ============================================================

USE [OneShop]
GO

-- ============================================================
-- 1. TẠO DANH MỤC MỸ PHẨM
-- ============================================================

-- Kiểm tra và thêm danh mục Mỹ Phẩm nếu chưa có
IF NOT EXISTS (SELECT 1 FROM categories WHERE id = 100)
BEGIN
    SET IDENTITY_INSERT categories ON;
    INSERT INTO categories (id, name, description) 
    VALUES (100, N'Mỹ Phẩm', N'Các sản phẩm chăm sóc da, trang điểm và làm đẹp');
    SET IDENTITY_INSERT categories OFF;
    PRINT 'Đã thêm category Mỹ Phẩm (ID: 100)';
END
ELSE
BEGIN
    PRINT 'Category Mỹ Phẩm (ID: 100) đã tồn tại - BỎ QUA';
END
GO

-- ============================================================
-- 2. TẠO VENDOR USER & SHOP CHO MỸ PHẨM
-- ============================================================

-- Tạo vendor user (password: vendor123)
IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'cosmetic_vendor')
BEGIN
    SET IDENTITY_INSERT users ON;
    INSERT INTO users (id, username, email, password, enabled)
    VALUES (200, 'cosmetic_vendor', 'cosmetic@oneshop.vn', 
            '$2a$10$8YNdJ9Qy5Xqz3kH4LpqnL.xJZvqKv5wF2H3YmZxH8jH9nH7KpH5qK', 1);
    SET IDENTITY_INSERT users OFF;
    
    -- Gán role VENDOR
    IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'VENDOR')
    BEGIN
        INSERT INTO roles (name) VALUES ('VENDOR');
    END
    
    DECLARE @vendorRoleId BIGINT = (SELECT id FROM roles WHERE name = 'VENDOR');
    INSERT INTO user_roles (user_id, role_id) VALUES (200, @vendorRoleId);
END
GO

-- Tạo shop cho vendor
IF NOT EXISTS (SELECT 1 FROM shops WHERE id = 200)
BEGIN
    INSERT INTO shops (id, name, description, address, phone, logo_url, created_at)
    VALUES (200, N'Beauty Paradise', 
            N'Cửa hàng mỹ phẩm chính hãng uy tín hàng đầu Việt Nam',
            N'123 Nguyễn Huệ, Quận 1, TP.HCM',
            '0901234567',
            N'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400',
            GETDATE());
END
GO

-- ============================================================
-- 3. TẠO CÁC SẢN PHẨM MỸ PHẨM
-- ============================================================

PRINT 'Xóa sản phẩm cũ (nếu có)...';
-- Xóa dữ liệu cũ để tránh duplicate key
DELETE FROM wishlists WHERE product_id BETWEEN 1001 AND 1020;
DELETE FROM reviews WHERE product_id BETWEEN 1001 AND 1020;
DELETE FROM cart_items WHERE product_id BETWEEN 1001 AND 1020;
DELETE FROM order_details WHERE product_id BETWEEN 1001 AND 1020;
DELETE FROM products WHERE shop_id = 200 AND id BETWEEN 1001 AND 1020;
PRINT '  ✓ Đã xóa sản phẩm cũ';
PRINT '';

SET IDENTITY_INSERT products ON;
GO

-- ============================================================
-- 3.1. SẢN PHẨM BÁN CHẠY (BEST SELLING) - 5 sản phẩm
-- ============================================================

INSERT INTO products (id, shop_id, category_id, name, description, price, stock, image_url, active, sold, featured, created_at)
VALUES 
(1001, 200, 100, N'Serum Vitamin C Klairs Freshly Juiced', 
 N'Serum dưỡng trắng da với chiết xuất vitamin C tươi nguyên chất, giúp làm sáng da, mờ thâm nám và chống lão hóa hiệu quả. Phù hợp cho mọi loại da, đặc biệt da nhạy cảm.',
 385000, 150, 
 N'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=500',
 1, 1250, 1, DATEADD(DAY, -45, GETDATE())),

(1002, 200, 100, N'Kem Chống Nắng La Roche-Posay Anthelios SPF50+',
 N'Kem chống nắng phổ rộng với công nghệ bảo vệ da tối ưu, chống tia UVA/UVB, không gây nhờn rít, phù hợp cho da nhạy cảm và da dầu mụn.',
 445000, 200,
 N'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500',
 1, 980, 1, DATEADD(DAY, -60, GETDATE())),

(1003, 200, 100, N'Nước Tẩy Trang Bioderma Sensibio H2O',
 N'Nước tẩy trang dịu nhẹ cho da nhạy cảm, làm sạch sâu mà không gây kích ứng, loại bỏ makeup lâu trôi và bụi bẩn hiệu quả. Không cần rửa lại với nước.',
 295000, 300,
 N'https://images.unsplash.com/photo-1631730486572-226d1f595b68?w=500',
 1, 875, 1, DATEADD(DAY, -50, GETDATE())),

(1004, 200, 100, N'Mặt Nạ Giấy Some By Mi Yuja Niacin',
 N'Mặt nạ dưỡng trắng với chiết xuất từ chanh Yuja và Niacinamide, giúp cấp ẩm, làm sáng da và cải thiện độ đàn hồi chỉ sau 20 phút sử dụng.',
 35000, 500,
 N'https://images.unsplash.com/photo-1608248543803-ba4f8c70ae0b?w=500',
 1, 1520, 1, DATEADD(DAY, -55, GETDATE())),

(1005, 200, 100, N'Sữa Rửa Mặt CeraVe Foaming Facial Cleanser',
 N'Sữa rửa mặt tạo bọt nhẹ với 3 Ceramides thiết yếu và Hyaluronic Acid, làm sạch sâu mà không làm khô da, duy trì hàng rào bảo vệ da tự nhiên.',
 285000, 180,
 N'https://images.unsplash.com/photo-1556228852-80f09cfe7c65?w=500',
 1, 760, 1, DATEADD(DAY, -65, GETDATE()));
GO

-- ============================================================
-- 3.2. SẢN PHẨM ĐÁNH GIÁ CAO (TOP RATED) - 5 sản phẩm
-- ============================================================

INSERT INTO products (id, shop_id, category_id, name, description, price, stock, image_url, active, sold, featured, created_at)
VALUES 
(1006, 200, 100, N'Kem Dưỡng Ẩm Neutrogena Hydro Boost',
 N'Kem dưỡng ẩm với công nghệ Hydro Boost cấp ẩm sâu 72 giờ, chứa Hyaluronic Acid giúp da mềm mại, căng mọng và tươi trẻ. Kết cấu gel mỏng nhẹ thấm nhanh.',
 365000, 120,
 N'https://images.unsplash.com/photo-1570194065650-d99fb4a2b1b8?w=500',
 1, 420, 1, DATEADD(DAY, -70, GETDATE())),

(1007, 200, 100, N'Tinh Chất The Ordinary Niacinamide 10% + Zinc 1%',
 N'Tinh chất se khít lỗ chân lông, kiểm soát dầu và cải thiện kết cấu da với 10% Niacinamide và 1% Zinc. Giảm mụn, thâm và nám hiệu quả.',
 245000, 90,
 N'https://images.unsplash.com/photo-1620916298870-b0fa7e70f83a?w=500',
 1, 550, 1, DATEADD(DAY, -40, GETDATE())),

(1008, 200, 100, N'Son Dưỡng Môi Dior Addict Lip Glow',
 N'Son dưỡng cao cấp với công nghệ Color Reviver, tự động thích ứng với độ pH của môi tạo màu hồng tự nhiên, dưỡng ẩm 24h với Cherry Oil.',
 895000, 60,
 N'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=500',
 1, 180, 1, DATEADD(DAY, -30, GETDATE())),

(1009, 200, 100, N'Phấn Nước Cushion Laneige Neo Cushion Matte',
 N'Phấn nước kiềm dầu lâu trôi với độ che phủ hoàn hảo, finish mịn lì tự nhiên. Chứa thành phần dưỡng da, kiểm soát dầu 24h không bết dính.',
 685000, 75,
 N'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=500',
 1, 320, 1, DATEADD(DAY, -35, GETDATE())),

(1010, 200, 100, N'Kem Mắt Innisfree Green Tea Seed Eye Cream',
 N'Kem dưỡng mắt với chiết xuất trà xanh Jeju, giảm quầng thâm, bọng mắt và nếp nhăn vùng mắt. Cấp ẩm sâu giúp vùng da quanh mắt săn chắc, tươi trẻ.',
 425000, 100,
 N'https://images.unsplash.com/photo-1571875257727-256c39da42af?w=500',
 1, 280, 1, DATEADD(DAY, -42, GETDATE()));
GO

-- ============================================================
-- 3.3. SẢN PHẨM YÊU THÍCH NHẤT (MOST FAVORITED) - 5 sản phẩm
-- ============================================================

INSERT INTO products (id, shop_id, category_id, name, description, price, stock, image_url, active, sold, featured, created_at)
VALUES 
(1011, 200, 100, N'Toner AHA BHA PHA 30 Days Miracle Some By Mi',
 N'Toner làm sạch sâu và tái tạo da với AHA, BHA, PHA. Loại bỏ tế bào chết, se khít lỗ chân lông, cải thiện mụn và làm sáng da chỉ sau 30 ngày.',
 345000, 140,
 N'https://images.unsplash.com/photo-1590439471364-192aa70c88b6?w=500',
 1, 640, 1, DATEADD(DAY, -48, GETDATE())),

(1012, 200, 100, N'Mặt Nạ Ngủ Laneige Water Sleeping Mask',
 N'Mặt nạ ngủ dưỡng ẩm cấp nước tức thì, giúp da mềm mại, căng mọng và tươi sáng vào buổi sáng. Công nghệ Moisture Wrap™ khóa ẩm suốt đêm.',
 535000, 85,
 N'https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?w=500',
 1, 380, 1, DATEADD(DAY, -52, GETDATE())),

(1013, 200, 100, N'Tinh Dầu Dưỡng Tóc Moroccanoil Treatment',
 N'Tinh dầu Argan Morocco phục hồi tóc hư tổn, mang lại độ mềm mượt, bóng khỏe tự nhiên. Thấm nhanh không gây bết dính, bảo vệ tóc khỏi nhiệt và tia UV.',
 685000, 70,
 N'https://images.unsplash.com/photo-1535585209827-a15fcdbc4c2d?w=500',
 1, 250, 1, DATEADD(DAY, -38, GETDATE())),

(1014, 200, 100, N'Xịt Khoáng Avène Thermal Spring Water',
 N'Nước khoáng thiên nhiên từ suối khoáng Avène, làm dịu da nhạy cảm, kích ứng. Cân bằng độ pH, cấp ẩm tức thì, có thể dùng cho cả em bé.',
 265000, 200,
 N'https://images.unsplash.com/photo-1556228724-a4b5b4bfb0c2?w=500',
 1, 520, 1, DATEADD(DAY, -44, GETDATE())),

(1015, 200, 100, N'Gel Dưỡng Ẩm Aloe Vera Nature Republic',
 N'Gel lô hội 92% chiết xuất từ California, dưỡng ẩm đa năng cho da mặt, body và tóc. Làm dịu da sau khi phơi nắng, cấp ẩm tức thì không nhờn rít.',
 185000, 250,
 N'https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?w=500',
 1, 890, 1, DATEADD(DAY, -58, GETDATE()));
GO

-- ============================================================
-- 3.4. SẢN PHẨM MỚI NHẤT (NEWEST) - 5 sản phẩm
-- ============================================================

INSERT INTO products (id, shop_id, category_id, name, description, price, stock, image_url, active, sold, featured, created_at)
VALUES 
(1016, 200, 100, N'Kem Nền Fenty Beauty Pro Filt''r Soft Matte',
 N'Kem nền lì mịn với 50 tông màu đa dạng, độ che phủ cao, lâu trôi 12h. Kiềm dầu tuyệt vời cho da dầu mà không làm khô da. Finish tự nhiên như da thật.',
 945000, 80,
 N'https://images.unsplash.com/photo-1457972729786-0411a3b2b626?w=500',
 1, 45, 1, DATEADD(DAY, -2, GETDATE())),

(1017, 200, 100, N'Mặt Nạ Đất Sét Innisfree Super Volcanic Pore Clay Mask',
 N'Mặt nạ đất sét núi lửa Jeju làm sạch sâu, hút dầu thừa và se khít lỗ chân lông. Loại bỏ bụi bẩn, mụn đầu đen hiệu quả chỉ sau 10 phút.',
 225000, 160,
 N'https://images.unsplash.com/photo-1556228724-c1b6115d2c95?w=500',
 1, 28, 1, DATEADD(DAY, -3, GETDATE())),

(1018, 200, 100, N'Tinh Chất Retinol The Ordinary Retinol 0.5% in Squalane',
 N'Tinh chất chống lão hóa với 0.5% Retinol tinh khiết, giảm nếp nhăn, thâm nám và cải thiện kết cấu da. Hòa trong Squalane dịu nhẹ, phù hợp người mới dùng Retinol.',
 325000, 65,
 N'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=500',
 1, 15, 1, DATEADD(DAY, -5, GETDATE())),

(1019, 200, 100, N'Mascara Maybelline Sky High Cosmic Black',
 N'Mascara công thức độc quyền giúp mi dài vút, cong vờn tự nhiên. Chải mi từng sợi rõ nét, không vón cục, không lem, lâu trôi 24h.',
 285000, 120,
 N'https://images.unsplash.com/photo-1631214524020-7e18db9a8f92?w=500',
 1, 22, 1, DATEADD(DAY, -4, GETDATE())),

(1020, 200, 100, N'Phấn Phủ Bột Laura Mercier Translucent Loose Setting Powder',
 N'Phấn phủ bột mịn lì huyền thoại, cố định lớp makeup lâu trôi, kiểm soát dầu và làm mờ lỗ chân lông. Finish tự nhiên không bóng, không làm xám da.',
 1285000, 45,
 N'https://images.unsplash.com/photo-1512496015851-a90fb38ba796?w=500',
 1, 8, 1, DATEADD(DAY, -1, GETDATE()));
GO

SET IDENTITY_INSERT products OFF;
GO

-- ============================================================
-- 4. TẠO USER MẪU ĐỂ TEST WISHLIST VÀ REVIEW
-- ============================================================

-- Tạo 10 user khách hàng mẫu
SET IDENTITY_INSERT users ON;
GO

DECLARE @i INT = 1;
WHILE @i <= 10
BEGIN
    IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'customer' + CAST(@i AS VARCHAR(2)))
    BEGIN
        INSERT INTO users (id, username, email, password, enabled)
        VALUES (300 + @i, 
                'customer' + CAST(@i AS VARCHAR(2)), 
                'customer' + CAST(@i AS VARCHAR(2)) + '@test.com',
                '$2a$10$8YNdJ9Qy5Xqz3kH4LpqnL.xJZvqKv5wF2H3YmZxH8jH9nH7KpH5qK', 
                1);
        
        -- Gán role USER
        DECLARE @userRoleId BIGINT = (SELECT id FROM roles WHERE name = 'USER');
        IF @userRoleId IS NULL
        BEGIN
            INSERT INTO roles (name) VALUES ('USER');
            SET @userRoleId = SCOPE_IDENTITY();
        END
        INSERT INTO user_roles (user_id, role_id) VALUES (300 + @i, @userRoleId);
    END
    SET @i = @i + 1;
END
GO

SET IDENTITY_INSERT users OFF;
GO

-- ============================================================
-- 5. TẠO WISHLIST CHO CÁC SẢN PHẨM YÊU THÍCH
-- ============================================================

PRINT 'Tạo wishlists cho sản phẩm yêu thích...';

-- Xóa wishlist cũ cho các sản phẩm này trước (đảm bảo không duplicate)
IF EXISTS (SELECT 1 FROM wishlists WHERE product_id BETWEEN 1011 AND 1015)
BEGIN
    DELETE FROM wishlists WHERE product_id BETWEEN 1011 AND 1015;
    PRINT '  ✓ Đã xóa wishlists cũ';
END;

-- Thêm wishlist cho sản phẩm 1011 (Toner AHA BHA) - khoảng 45 người thích
WITH NumberedUsers AS (
    SELECT DISTINCT (ROW_NUMBER() OVER (ORDER BY object_id) % 10) + 1 AS user_offset, 1011 AS prod_id
    FROM (SELECT TOP 45 object_id FROM sys.objects ORDER BY object_id) AS t
)
INSERT INTO wishlists (user_id, product_id)
SELECT DISTINCT 300 + user_offset, prod_id
FROM NumberedUsers;
PRINT '  ✓ Đã thêm wishlists cho sản phẩm 1011';

-- Thêm wishlist cho sản phẩm 1012 (Water Sleeping Mask) - khoảng 52 người thích
;WITH NumberedUsers AS (
    SELECT DISTINCT (ROW_NUMBER() OVER (ORDER BY object_id) % 10) + 1 AS user_offset, 1012 AS prod_id
    FROM (SELECT TOP 52 object_id FROM sys.objects ORDER BY object_id) AS t
)
INSERT INTO wishlists (user_id, product_id)
SELECT DISTINCT 300 + user_offset, prod_id
FROM NumberedUsers;
PRINT '  ✓ Đã thêm wishlists cho sản phẩm 1012';

-- Thêm wishlist cho sản phẩm 1013 (Moroccanoil) - khoảng 38 người thích
;WITH NumberedUsers AS (
    SELECT DISTINCT (ROW_NUMBER() OVER (ORDER BY object_id) % 10) + 1 AS user_offset, 1013 AS prod_id
    FROM (SELECT TOP 38 object_id FROM sys.objects ORDER BY object_id) AS t
)
INSERT INTO wishlists (user_id, product_id)
SELECT DISTINCT 300 + user_offset, prod_id
FROM NumberedUsers;
PRINT '  ✓ Đã thêm wishlists cho sản phẩm 1013';

-- Thêm wishlist cho sản phẩm 1014 (Avène Water) - khoảng 60 người thích
;WITH NumberedUsers AS (
    SELECT DISTINCT (ROW_NUMBER() OVER (ORDER BY object_id) % 10) + 1 AS user_offset, 1014 AS prod_id
    FROM (SELECT TOP 60 object_id FROM sys.objects ORDER BY object_id) AS t
)
INSERT INTO wishlists (user_id, product_id)
SELECT DISTINCT 300 + user_offset, prod_id
FROM NumberedUsers;
PRINT '  ✓ Đã thêm wishlists cho sản phẩm 1014';

-- Thêm wishlist cho sản phẩm 1015 (Aloe Vera Gel) - khoảng 72 người thích
;WITH NumberedUsers AS (
    SELECT DISTINCT (ROW_NUMBER() OVER (ORDER BY object_id) % 10) + 1 AS user_offset, 1015 AS prod_id
    FROM (SELECT TOP 72 object_id FROM sys.objects ORDER BY object_id) AS t
)
INSERT INTO wishlists (user_id, product_id)
SELECT DISTINCT 300 + user_offset, prod_id
FROM NumberedUsers;
PRINT '  ✓ Đã thêm wishlists cho sản phẩm 1015';
PRINT '';

GO

-- ============================================================
-- 6. TẠO REVIEWS CHO CÁC SẢN PHẨM ĐÁNH GIÁ CAO
-- ============================================================

-- Xóa reviews cũ cho các sản phẩm này
DELETE FROM reviews WHERE product_id BETWEEN 1006 AND 1010;

SET IDENTITY_INSERT reviews ON;
GO

-- Reviews cho sản phẩm 1006 (Neutrogena Hydro Boost) - Trung bình 4.8 sao
INSERT INTO reviews (id, product_id, user_id, rating, comment, media_url, created_at)
VALUES 
(2001, 1006, 301, 5, N'Kem dưỡng ẩm tuyệt vời! Da mình khô nhưng dùng em này thấy mềm mịn hẳn. Thấm nhanh không nhờn rít, rất thích!', NULL, DATEADD(DAY, -15, GETDATE())),
(2002, 1006, 302, 5, N'Chất kem gel mát lạnh, thấm vào da rất nhanh. Dùng được 2 tuần da đã thấy khác biệt rõ rệt. Must have!', N'https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=300', DATEADD(DAY, -12, GETDATE())),
(2003, 1006, 303, 5, N'Sản phẩm xứng đáng 5 sao. Giá hơi cao nhưng chất lượng không chê vào đâu được. Sẽ repurchase!', NULL, DATEADD(DAY, -10, GETDATE())),
(2004, 1006, 304, 4, N'Tốt nhưng hơi đắt. Nếu có sale thì sẽ mua tiếp. Chất kem dễ chịu, phù hợp da khô.', NULL, DATEADD(DAY, -8, GETDATE())),
(2005, 1006, 305, 5, N'Đã dùng 3 hũ rồi. Không thể thiếu trong routine dưỡng da của mình. Highly recommend!', N'https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=300', DATEADD(DAY, -5, GETDATE()));

-- Reviews cho sản phẩm 1007 (The Ordinary Niacinamide) - Trung bình 4.9 sao
INSERT INTO reviews (id, product_id, user_id, rating, comment, media_url, created_at)
VALUES 
(2006, 1007, 306, 5, N'Holy grail của mình! Giảm mụn và se khít lỗ chân lông rõ rệt sau 1 tháng. Giá rẻ mà chất lượng không thua kém gì hàng đắt tiền.', NULL, DATEADD(DAY, -20, GETDATE())),
(2007, 1007, 307, 5, N'Sản phẩm tốt nhất trong tầm giá. Mụn giảm hẳn, da bớt nhờn. Đã giới thiệu cho nhiều bạn bè.', N'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=300', DATEADD(DAY, -18, GETDATE())),
(2008, 1007, 308, 5, N'The Ordinary never disappoints! Chai này dùng hết rất nhanh vì hiệu quả quá. Da sáng hơn, lỗ chân lông nhỏ lại.', NULL, DATEADD(DAY, -14, GETDATE())),
(2009, 1007, 309, 5, N'Mình có da dầu mụn, dùng em này kết hợp với AHA/BHA thấy da cải thiện nhanh lắm. 10/10!', NULL, DATEADD(DAY, -11, GETDATE())),
(2010, 1007, 310, 4, N'Tốt nhưng ban đầu hơi bong tróc. Sau 1 tuần da quen thì ổn. Nhìn chung rất hài lòng.', NULL, DATEADD(DAY, -7, GETDATE()));

-- Reviews cho sản phẩm 1008 (Dior Lip Glow) - Trung bình 4.7 sao
INSERT INTO reviews (id, product_id, user_id, rating, comment, media_url, created_at)
VALUES 
(2011, 1008, 301, 5, N'Son dưỡng sang chảnh nhất từng dùng! Màu tự nhiên cực kỳ đẹp, dưỡng môi mềm mại cả ngày.', N'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=300', DATEADD(DAY, -16, GETDATE())),
(2012, 1008, 302, 5, N'Xứng đáng với giá tiền. Môi mình khô nhưng dùng em này thấy cải thiện rõ. Màu hồng tự nhiên rất xinh.', NULL, DATEADD(DAY, -13, GETDATE())),
(2013, 1008, 303, 5, N'Luxury son dưỡng! Mùi thơm nhẹ nhàng, chất son mịn lì. Tô son lên trông môi căng mọng hơn hẳn.', NULL, DATEADD(DAY, -9, GETDATE())),
(2014, 1008, 304, 4, N'Đẹp thật nhưng hơi đắt. Nếu có điều kiện thì nên thử. Chất lượng Dior không bàn cãi.', NULL, DATEADD(DAY, -6, GETDATE())),
(2015, 1008, 305, 5, N'Best lip balm ever! Đi làm không cần tô son, chỉ cần em này là đủ tự tin rồi.', N'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=300', DATEADD(DAY, -3, GETDATE()));

-- Reviews cho sản phẩm 1009 (Laneige Cushion) - Trung bình 4.6 sao
INSERT INTO reviews (id, product_id, user_id, rating, comment, media_url, created_at)
VALUES 
(2016, 1009, 306, 5, N'Phấn nước kiềm dầu tốt nhất mình từng dùng! Finish mịn lì, không bết dính, makeup lâu trôi cả ngày.', NULL, DATEADD(DAY, -17, GETDATE())),
(2017, 1009, 307, 5, N'Da dầu mà dùng em này thì an tâm makeup cả ngày. Không cần blotting paper nữa. Love it!', N'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=300', DATEADD(DAY, -14, GETDATE())),
(2018, 1009, 308, 4, N'Tốt nhưng hơi khô với da mình. Có thể do da mình hỗn hợp thiên khô. Nhưng nhìn chung ok.', NULL, DATEADD(DAY, -10, GETDATE())),
(2019, 1009, 309, 5, N'Che phủ tốt, finish đẹp tự nhiên. Đi làm 8 tiếng vẫn giữ được độ mịn lì. Highly recommend!', NULL, DATEADD(DAY, -7, GETDATE())),
(2020, 1009, 310, 5, N'Sản phẩm cushion yêu thích nhất! Mua đi mua lại nhiều lần rồi. Laneige quality as always!', NULL, DATEADD(DAY, -4, GETDATE()));

-- Reviews cho sản phẩm 1010 (Innisfree Eye Cream) - Trung bình 4.5 sao
INSERT INTO reviews (id, product_id, user_id, rating, comment, media_url, created_at)
VALUES 
(2021, 1010, 301, 5, N'Kem mắt giá tốt hiệu quả cao. Quầng thâm giảm rõ rệt sau 2 tuần. Texture nhẹ không gây milia.', NULL, DATEADD(DAY, -19, GETDATE())),
(2022, 1010, 302, 4, N'Tốt với mức giá này. Chưa thấy giảm nếp nhăn nhưng vùng mắt thấy ẩm hơn, không khô căng.', NULL, DATEADD(DAY, -15, GETDATE())),
(2023, 1010, 303, 5, N'Innisfree luôn là lựa chọn an toàn. Kem mắt này dùng dễ chịu, không cay mắt, thấm nhanh.', N'https://images.unsplash.com/photo-1571875257727-256c39da42af?w=300', DATEADD(DAY, -11, GETDATE())),
(2024, 1010, 304, 5, N'Mình 30 tuổi dùng thấy ổn. Bọng mắt giảm đi phần nào. Giá rẻ nên repurchase thoải mái.', NULL, DATEADD(DAY, -8, GETDATE())),
(2025, 1010, 305, 4, N'Ổn với tầm giá. Không kỳ diệu gì nhưng maintain vùng mắt khỏe mạnh là được. Will buy again.', NULL, DATEADD(DAY, -5, GETDATE()));

SET IDENTITY_INSERT reviews OFF;
GO

-- ============================================================
-- 7. CẬP NHẬT AVERAGE RATING CHO PRODUCTS (nếu có trường rating)
-- ============================================================
-- Note: Nếu bảng products có trường average_rating thì uncomment phần này

/*
UPDATE p
SET p.average_rating = r.avg_rating
FROM products p
INNER JOIN (
    SELECT product_id, AVG(CAST(rating AS FLOAT)) AS avg_rating
    FROM reviews
    WHERE product_id BETWEEN 1006 AND 1010
    GROUP BY product_id
) r ON p.id = r.product_id;
GO
*/

-- ============================================================
-- 8. KIỂM TRA DỮ LIỆU ĐÃ THÊM
-- ============================================================

PRINT '============================================================';
PRINT 'KẾT QUẢ THÊM DỮ LIỆU MẪU MỸ PHẨM';
PRINT '============================================================';
PRINT '';

-- Kiểm tra products
DECLARE @totalProducts INT;
SELECT @totalProducts = COUNT(*) FROM products WHERE shop_id = 200;
PRINT 'Tổng số sản phẩm mỹ phẩm: ' + CAST(@totalProducts AS VARCHAR(10));
PRINT '';

-- Top 5 sản phẩm bán chạy
PRINT '--- TOP 5 SẢN PHẨM BÁN CHẠY ---';
SELECT TOP 5 id, name, sold, price
FROM products 
WHERE shop_id = 200
ORDER BY sold DESC;
PRINT '';

-- Top 5 sản phẩm đánh giá cao
PRINT '--- TOP 5 SẢN PHẨM ĐÁNH GIÁ CAO (theo số lượng reviews) ---';
SELECT TOP 5 p.id, p.name, COUNT(r.id) AS review_count, AVG(CAST(r.rating AS FLOAT)) AS avg_rating
FROM products p
LEFT JOIN reviews r ON p.id = r.product_id
WHERE p.shop_id = 200
GROUP BY p.id, p.name
ORDER BY review_count DESC, avg_rating DESC;
PRINT '';

-- Top 5 sản phẩm yêu thích
PRINT '--- TOP 5 SẢN PHẨM YÊU THÍCH (theo số lượng wishlists) ---';
SELECT TOP 5 p.id, p.name, COUNT(w.id) AS wishlist_count
FROM products p
LEFT JOIN wishlists w ON p.id = w.product_id
WHERE p.shop_id = 200
GROUP BY p.id, p.name
ORDER BY wishlist_count DESC;
PRINT '';

-- Top 5 sản phẩm mới nhất
PRINT '--- TOP 5 SẢN PHẨM MỚI NHẤT ---';
SELECT TOP 5 id, name, created_at, sold
FROM products 
WHERE shop_id = 200
ORDER BY created_at DESC;
PRINT '';

PRINT '============================================================';
PRINT 'HOÀN TẤT! Dữ liệu mẫu đã được thêm thành công.';
PRINT 'Có thể truy cập /user/top20 để xem các danh mục sản phẩm.';
PRINT '============================================================';
GO

