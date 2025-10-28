-- ============================================================
-- CLEANUP SCRIPT: Xóa dữ liệu mẫu mỹ phẩm
-- Mục đích: Xóa sạch dữ liệu trước khi chạy lại cosmetics_sample_data.sql
-- Lưu ý: Script này CHỈ xóa dữ liệu mẫu mỹ phẩm (shop_id=200, user_id 200-310)
-- ============================================================

USE [OneShop]
GO

PRINT '============================================================';
PRINT 'BẮT ĐẦU XÓA DỮ LIỆU MẪU MỸ PHẨM';
PRINT '======================================================a======';
PRINT '';

-- ============================================================
-- BƯỚC 1: Kiểm tra dữ liệu hiện có
-- ============================================================

PRINT '--- BƯỚC 1: Kiểm tra dữ liệu hiện có ---';
PRINT '';

DECLARE @productCount INT;
DECLARE @userCount INT;
DECLARE @shopCount INT;
DECLARE @wishlistCount INT;
DECLARE @reviewCount INT;

SELECT @productCount = COUNT(*) FROM products WHERE shop_id = 200;
SELECT @userCount = COUNT(*) FROM users WHERE id BETWEEN 200 AND 310;
SELECT @shopCount = COUNT(*) FROM shops WHERE id = 200;
SELECT @wishlistCount = COUNT(*) FROM wishlists WHERE product_id BETWEEN 1001 AND 1020;
SELECT @reviewCount = COUNT(*) FROM reviews WHERE product_id BETWEEN 1001 AND 1020;

PRINT 'Số lượng dữ liệu hiện tại:';
PRINT '  - Products (shop_id=200): ' + CAST(@productCount AS VARCHAR(10));
PRINT '  - Users (id 200-310): ' + CAST(@userCount AS VARCHAR(10));
PRINT '  - Shops (id=200): ' + CAST(@shopCount AS VARCHAR(10));
PRINT '  - Wishlists: ' + CAST(@wishlistCount AS VARCHAR(10));
PRINT '  - Reviews: ' + CAST(@reviewCount AS VARCHAR(10));
PRINT '';

-- ============================================================
-- BƯỚC 2: Backup dữ liệu (optional - uncomment nếu cần)
-- ============================================================

/*
PRINT '--- BƯỚC 2: Backup dữ liệu ---';
BACKUP DATABASE [OneShop] 
TO DISK = 'E:\Backup\OneShop_BeforeCleanup_' + CONVERT(VARCHAR(20), GETDATE(), 112) + '.bak'
WITH FORMAT, INIT, NAME = N'OneShop-Full Database Backup Before Cleanup', SKIP, NOREWIND, NOUNLOAD, STATS = 10;
PRINT 'Backup completed!';
PRINT '';
*/

-- ============================================================
-- BƯỚC 3: Tắt Foreign Key Constraints (tạm thời)
-- ============================================================

PRINT '--- BƯỚC 3: Tắt Foreign Key Constraints ---';
ALTER TABLE [dbo].[wishlists] NOCHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[reviews] NOCHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[cart_items] NOCHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[carts] NOCHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[order_details] NOCHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[orders] NOCHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[products] NOCHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[shops] NOCHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[user_roles] NOCHECK CONSTRAINT ALL;
PRINT 'Foreign Key Constraints đã tắt!';
PRINT '';

-- ============================================================
-- BƯỚC 4: Xóa dữ liệu theo thứ tự
-- ============================================================

PRINT '--- BƯỚC 4: Xóa dữ liệu ---';
PRINT '';

-- 4.1. Xóa Wishlists
PRINT 'Xóa Wishlists...';
DELETE FROM [dbo].[wishlists] 
WHERE product_id IN (SELECT id FROM products WHERE shop_id = 200);
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' wishlists';

-- 4.2. Xóa Reviews
PRINT 'Xóa Reviews...';
DELETE FROM [dbo].[reviews] 
WHERE product_id IN (SELECT id FROM products WHERE shop_id = 200);
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' reviews';

-- 4.3. Xóa Cart Items (nếu có)
PRINT 'Xóa Cart Items...';
DELETE FROM [dbo].[cart_items] 
WHERE product_id IN (SELECT id FROM products WHERE shop_id = 200);
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' cart items';

-- 4.4. Xóa Order Details (nếu có)
PRINT 'Xóa Order Details...';
DELETE FROM [dbo].[order_details] 
WHERE product_id IN (SELECT id FROM products WHERE shop_id = 200);
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' order details';

-- 4.5. Xóa Carts của users mẫu
PRINT 'Xóa Carts của users mẫu...';
DELETE FROM [dbo].[carts] 
WHERE user_id BETWEEN 300 AND 310;
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' carts';

-- 4.6. Xóa Orders của users mẫu
PRINT 'Xóa Orders của users mẫu...';
DELETE FROM [dbo].[orders] 
WHERE customer_id IN (SELECT id FROM customers WHERE user_id BETWEEN 300 AND 310);
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' orders';

-- 4.7. Xóa Products
PRINT 'Xóa Products...';
DELETE FROM [dbo].[products] 
WHERE shop_id = 200;
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' products';

-- 4.8. Xóa Shops
PRINT 'Xóa Shops...';
DELETE FROM [dbo].[shops] 
WHERE id = 200;
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' shops';

-- 4.9. Xóa User Roles
PRINT 'Xóa User Roles...';
DELETE FROM [dbo].[user_roles] 
WHERE user_id BETWEEN 200 AND 310;
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' user roles';

-- 4.10. Xóa Customers
PRINT 'Xóa Customers...';
DELETE FROM [dbo].[customers] 
WHERE user_id BETWEEN 200 AND 310;
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' customers';

-- 4.11. Xóa Users
PRINT 'Xóa Users...';
DELETE FROM [dbo].[users] 
WHERE id BETWEEN 200 AND 310;
PRINT '  ✓ Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' users';

PRINT '';

-- ============================================================
-- BƯỚC 5: Bật lại Foreign Key Constraints
-- ============================================================

PRINT '--- BƯỚC 5: Bật lại Foreign Key Constraints ---';
ALTER TABLE [dbo].[wishlists] WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[reviews] WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[cart_items] WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[carts] WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[order_details] WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[orders] WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[products] WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[shops] WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE [dbo].[user_roles] WITH CHECK CHECK CONSTRAINT ALL;
PRINT 'Foreign Key Constraints đã bật lại!';
PRINT '';

-- ============================================================
-- BƯỚC 6: Reset Identity (nếu muốn bắt đầu lại từ ID cũ)
-- ============================================================

/*
-- Uncomment nếu muốn reset identity cho các bảng
PRINT '--- BƯỚC 6: Reset Identity ---';

-- Reset products identity to 1000
DBCC CHECKIDENT ('products', RESEED, 1000);
PRINT '  ✓ Reset products identity to 1000';

-- Reset users identity to 199
DBCC CHECKIDENT ('users', RESEED, 199);
PRINT '  ✓ Reset users identity to 199';

-- Reset reviews identity to 2000
DBCC CHECKIDENT ('reviews', RESEED, 2000);
PRINT '  ✓ Reset reviews identity to 2000';

PRINT '';
*/

-- ============================================================
-- BƯỚC 7: Kiểm tra kết quả
-- ============================================================

PRINT '--- BƯỚC 7: Kiểm tra kết quả sau khi xóa ---';
PRINT '';

SELECT @productCount = COUNT(*) FROM products WHERE shop_id = 200;
SELECT @userCount = COUNT(*) FROM users WHERE id BETWEEN 200 AND 310;
SELECT @shopCount = COUNT(*) FROM shops WHERE id = 200;
SELECT @wishlistCount = COUNT(*) FROM wishlists WHERE product_id BETWEEN 1001 AND 1020;
SELECT @reviewCount = COUNT(*) FROM reviews WHERE product_id BETWEEN 1001 AND 1020;

PRINT 'Số lượng dữ liệu còn lại:';
PRINT '  - Products (shop_id=200): ' + CAST(@productCount AS VARCHAR(10));
PRINT '  - Users (id 200-310): ' + CAST(@userCount AS VARCHAR(10));
PRINT '  - Shops (id=200): ' + CAST(@shopCount AS VARCHAR(10));
PRINT '  - Wishlists: ' + CAST(@wishlistCount AS VARCHAR(10));
PRINT '  - Reviews: ' + CAST(@reviewCount AS VARCHAR(10));
PRINT '';

-- ============================================================
-- KẾT QUẢ
-- ============================================================

IF @productCount = 0 AND @userCount = 0 AND @shopCount = 0 AND @wishlistCount = 0 AND @reviewCount = 0
BEGIN
    PRINT '============================================================';
    PRINT '✅ THÀNH CÔNG! Đã xóa sạch dữ liệu mẫu mỹ phẩm.';
    PRINT 'Bạn có thể chạy lại cosmetics_sample_data.sql ngay bây giờ.';
    PRINT '============================================================';
END
ELSE
BEGIN
    PRINT '============================================================';
    PRINT '⚠️ CẢNH BÁO! Vẫn còn dữ liệu chưa được xóa.';
    PRINT 'Kiểm tra lại foreign key constraints hoặc dependencies.';
    PRINT '============================================================';
END

PRINT '';
PRINT 'Hoàn tất cleanup script!';
PRINT '';
GO

