-- ============================================================
-- CẬP NHẬT ĐỔI URL ẢNH ONLINE SANG LOCAL PATH
-- OneShop Project - Update Image Paths to Local
-- ============================================================

USE oneshop;
GO

PRINT '========================================';
PRINT '  CẬP NHẬT ĐƯỜNG DẪN ẢNH LOCAL';
PRINT '========================================';
PRINT '';

-- Đếm số ảnh sẽ được cập nhật
DECLARE @totalCount INT;
SELECT @totalCount = COUNT(*) FROM products WHERE id BETWEEN 1001 AND 1020;
PRINT 'Tổng số sản phẩm cần cập nhật: ' + CAST(@totalCount AS VARCHAR(10));
PRINT '';

-- Cập nhật từng sản phẩm với đường dẫn local
PRINT 'Đang cập nhật đường dẫn ảnh...';
PRINT '';

-- Sản phẩm bán chạy (1001-1005)
UPDATE products SET image_url = '/images/user/serum-vitamin-c-klairs.jpg' WHERE id = 1001;
PRINT '✓ [1001] Serum Vitamin C Klairs';

UPDATE products SET image_url = '/images/user/xit-khoang-la-roche-posay.jpg' WHERE id = 1002;
PRINT '✓ [1002] Xịt Khoáng La Roche-Posay';

UPDATE products SET image_url = '/images/user/kem-chong-nang-biore.jpg' WHERE id = 1003;
PRINT '✓ [1003] Kem Chống Nắng Biore';

UPDATE products SET image_url = '/images/user/sua-rua-mat-cerave.jpg' WHERE id = 1004;
PRINT '✓ [1004] Sữa Rửa Mặt CeraVe';

UPDATE products SET image_url = '/images/user/kem-duong-am-neutrogena.jpg' WHERE id = 1005;
PRINT '✓ [1005] Kem Dưỡng Ẩm Neutrogena';

-- Sản phẩm mới nhất (1006-1010)
UPDATE products SET image_url = '/images/user/mat-na-innisfree.jpg' WHERE id = 1006;
PRINT '✓ [1006] Mặt Nạ Innisfree';

UPDATE products SET image_url = '/images/user/nuoc-hoa-hong-mamonde.jpg' WHERE id = 1007;
PRINT '✓ [1007] Nước Hoa Hồng Mamonde';

UPDATE products SET image_url = '/images/user/kem-mat-laneige.jpg' WHERE id = 1008;
PRINT '✓ [1008] Kem Mắt Laneige';

UPDATE products SET image_url = '/images/user/serum-retinol-roc.jpg' WHERE id = 1009;
PRINT '✓ [1009] Serum Retinol RoC';

UPDATE products SET image_url = '/images/user/tinh-dau-duong-toc-loreal.jpg' WHERE id = 1010;
PRINT '✓ [1010] Tinh Dầu Dưỡng Tóc L''Oréal';

-- Sản phẩm đánh giá cao (1011-1015)
UPDATE products SET image_url = '/images/user/toner-aha-bha-cosrx.jpg' WHERE id = 1011;
PRINT '✓ [1011] Toner AHA BHA COSRX';

UPDATE products SET image_url = '/images/user/mat-na-ngu-laneige.jpg' WHERE id = 1012;
PRINT '✓ [1012] Mặt Nạ Ngủ Laneige';

UPDATE products SET image_url = '/images/user/dau-duong-toc-moroccanoil.jpg' WHERE id = 1013;
PRINT '✓ [1013] Dầu Dưỡng Tóc Moroccanoil';

UPDATE products SET image_url = '/images/user/xit-khoang-avene.jpg' WHERE id = 1014;
PRINT '✓ [1014] Xịt Khoáng Avène';

UPDATE products SET image_url = '/images/user/gel-aloe-vera-nature-republic.jpg' WHERE id = 1015;
PRINT '✓ [1015] Gel Aloe Vera Nature Republic';

-- Sản phẩm yêu thích (1016-1020)
UPDATE products SET image_url = '/images/user/kem-nen-fenty-beauty.jpg' WHERE id = 1016;
PRINT '✓ [1016] Kem Nền Fenty Beauty';

UPDATE products SET image_url = '/images/user/son-moi-mac.jpg' WHERE id = 1017;
PRINT '✓ [1017] Son Môi MAC';

UPDATE products SET image_url = '/images/user/phan-mat-urban-decay.jpg' WHERE id = 1018;
PRINT '✓ [1018] Phấn Mắt Urban Decay';

UPDATE products SET image_url = '/images/user/bot-phu-laura-mercier.jpg' WHERE id = 1019;
PRINT '✓ [1019] Bột Phủ Laura Mercier';

UPDATE products SET image_url = '/images/user/mascara-maybelline.jpg' WHERE id = 1020;
PRINT '✓ [1020] Mascara Maybelline';

PRINT '';
PRINT '========================================';
PRINT '  HOÀN TẤT CẬP NHẬT!';
PRINT '========================================';
PRINT '';

-- Kiểm tra kết quả
DECLARE @updatedCount INT;
SELECT @updatedCount = COUNT(*) 
FROM products 
WHERE id BETWEEN 1001 AND 1020 
  AND image_url LIKE '/images/user/%';

PRINT 'Đã cập nhật: ' + CAST(@updatedCount AS VARCHAR(10)) + '/' + CAST(@totalCount AS VARCHAR(10)) + ' sản phẩm';
PRINT '';
PRINT '🎉 Bây giờ tất cả ảnh sẽ load từ LOCAL - NHANH & ỔN ĐỊNH!';
PRINT '';

-- Hiển thị một số ví dụ
PRINT 'VÍ DỤ MỘT SỐ SẢN PHẨM ĐÃ CẬP NHẬT:';
SELECT TOP 5 
    id AS [ID],
    name AS [Tên Sản Phẩm],
    image_url AS [Đường Dẫn Ảnh]
FROM products 
WHERE id BETWEEN 1001 AND 1020
ORDER BY id;

