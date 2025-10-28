-- ============================================================
-- SỬA TÊN SẢN PHẨM TIẾNG VIỆT (CÓ DẤU)
-- OneShop Project - Fix Vietnamese Product Names
-- ============================================================

USE oneshop;
GO

PRINT '========================================';
PRINT '  SỬA TÊN SẢN PHẨM TIẾNG VIỆT';
PRINT '========================================';
PRINT '';

-- Sản phẩm bán chạy (1001-1005)
UPDATE products SET name = N'Serum Vitamin C Klairs Freshly Juiced' WHERE id = 1001;
PRINT '✓ [1001] Serum Vitamin C Klairs';

UPDATE products SET name = N'Kem Chống Nắng Biore UV Aqua Rich' WHERE id = 1002;
PRINT '✓ [1002] Kem Chống Nắng Biore';

UPDATE products SET name = N'Nước Tẩy Trang Bioderma Sensibio H2O' WHERE id = 1003;
PRINT '✓ [1003] Nước Tẩy Trang Bioderma';

UPDATE products SET name = N'Mặt Nạ Giấy Some By Mi Yuja Niacin' WHERE id = 1004;
PRINT '✓ [1004] Mặt Nạ Giấy Some By Mi';

UPDATE products SET name = N'Sữa Rửa Mặt CeraVe Foaming Cleanser' WHERE id = 1005;
PRINT '✓ [1005] Sữa Rửa Mặt CeraVe';

-- Sản phẩm mới nhất (1006-1010)
UPDATE products SET name = N'Kem Dưỡng Ẩm Neutrogena Hydro Boost' WHERE id = 1006;
PRINT '✓ [1006] Kem Dưỡng Ẩm Neutrogena';

UPDATE products SET name = N'Tinh Chất Serum The Ordinary Niacinamide' WHERE id = 1007;
PRINT '✓ [1007] Tinh Chất Serum The Ordinary';

UPDATE products SET name = N'Son Dưỡng Môi Vaseline Lip Therapy' WHERE id = 1008;
PRINT '✓ [1008] Son Dưỡng Môi Vaseline';

UPDATE products SET name = N'Phấn Nước Cushion Laneige Neo' WHERE id = 1009;
PRINT '✓ [1009] Phấn Nước Cushion Laneige';

UPDATE products SET name = N'Kem Dưỡng Mắt Innisfree Green Tea' WHERE id = 1010;
PRINT '✓ [1010] Kem Dưỡng Mắt Innisfree';

-- Sản phẩm đánh giá cao (1011-1015)
UPDATE products SET name = N'Toner Làm Sạch AHA BHA COSRX' WHERE id = 1011;
PRINT '✓ [1011] Toner AHA BHA COSRX';

UPDATE products SET name = N'Mặt Nạ Ngủ Water Sleeping Mask Laneige' WHERE id = 1012;
PRINT '✓ [1012] Mặt Nạ Ngủ Laneige';

UPDATE products SET name = N'Tinh Dầu Argan Moroccanoil Treatment' WHERE id = 1013;
PRINT '✓ [1013] Tinh Dầu Argan Moroccanoil';

UPDATE products SET name = N'Nước Khoáng Xịt Avène Thermal Water' WHERE id = 1014;
PRINT '✓ [1014] Nước Khoáng Avène';

UPDATE products SET name = N'Gel Lô Hội 99% Nature Republic Aloe Vera' WHERE id = 1015;
PRINT '✓ [1015] Gel Lô Hội Nature Republic';

-- Sản phẩm yêu thích (1016-1020)
UPDATE products SET name = N'Kem Nền Fenty Beauty Pro Filt''r Soft Matte' WHERE id = 1016;
PRINT '✓ [1016] Kem Nền Fenty Beauty';

UPDATE products SET name = N'Mặt Nạ Đất Sét Innisfree Super Volcanic' WHERE id = 1017;
PRINT '✓ [1017] Mặt Nạ Đất Sét Innisfree';

UPDATE products SET name = N'Tinh Chất Retinol The Ordinary Retinol 0.5%' WHERE id = 1018;
PRINT '✓ [1018] Tinh Chất Retinol The Ordinary';

UPDATE products SET name = N'Phấn Phủ Bột Laura Mercier Translucent' WHERE id = 1019;
PRINT '✓ [1019] Phấn Phủ Laura Mercier';

UPDATE products SET name = N'Mascara Cong Mi Maybelline Sky High' WHERE id = 1020;
PRINT '✓ [1020] Mascara Maybelline';

PRINT '';
PRINT '========================================';
PRINT '  HOÀN TẤT!';
PRINT '========================================';
PRINT '';
PRINT 'Đã cập nhật tên cho 20 sản phẩm với tiếng Việt có dấu đầy đủ.';
PRINT '';

-- Hiển thị kết quả
PRINT 'KẾT QUẢ (TOP 10):';
SELECT TOP 10
    id AS [ID],
    name AS [Tên Sản Phẩm],
    price AS [Giá],
    sold AS [Đã Bán]
FROM products
WHERE id BETWEEN 1001 AND 1020
ORDER BY id;

