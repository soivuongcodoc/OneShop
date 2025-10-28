-- ============================================================
-- ALTER COLUMN NAME TO NVARCHAR(MAX)
-- ============================================================
-- Script này sẽ thay đổi độ dài column 'name' trong bảng 'products'
-- từ NVARCHAR(200) thành NVARCHAR(MAX)
-- ============================================================

USE [oneshop];
GO

PRINT '========================================';
PRINT '  ALTER COLUMN NAME TO NVARCHAR(MAX)';
PRINT '========================================';
PRINT '';

-- Kiểm tra xem column hiện tại có độ dài bao nhiêu
PRINT 'Thông tin column TRƯỚC khi alter:';
SELECT 
    COLUMN_NAME AS [Column],
    DATA_TYPE AS [Type],
    CHARACTER_MAXIMUM_LENGTH AS [Max Length]
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'products' 
  AND COLUMN_NAME = 'name';
PRINT '';

-- Alter column
PRINT 'Đang thay đổi column name thành NVARCHAR(MAX)...';
ALTER TABLE products 
ALTER COLUMN name NVARCHAR(MAX) NOT NULL;
PRINT '✓ Thay đổi thành công!';
PRINT '';

-- Kiểm tra lại sau khi alter
PRINT 'Thông tin column SAU khi alter:';
SELECT 
    COLUMN_NAME AS [Column],
    DATA_TYPE AS [Type],
    CHARACTER_MAXIMUM_LENGTH AS [Max Length]
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'products' 
  AND COLUMN_NAME = 'name';
PRINT '';

-- Test update với tên dài
PRINT 'Test: Update tên sản phẩm dài (>200 ký tự)...';
DECLARE @longName NVARCHAR(MAX) = N'Serum Vitamin C Klairs Freshly Juiced Drop - Tinh Chất Vitamin C Nguyên Chất 5% Giúp Làm Sáng Da, Mờ Thâm Nám, Chống Lão Hóa, Phù Hợp Mọi Loại Da Kể Cả Da Nhạy Cảm - Hàng Chính Hãng Nhập Khẩu Từ Hàn Quốc - Dung Tích 35ml';

UPDATE products 
SET name = @longName 
WHERE id = 1001;

SELECT 
    id,
    LEFT(name, 50) + '...' AS [Tên Sản Phẩm (50 ký tự đầu)],
    LEN(name) AS [Độ Dài Thực Tế]
FROM products
WHERE id = 1001;

PRINT '✓ Test thành công!';
PRINT '';

PRINT '========================================';
PRINT '  HOÀN TẤT!';
PRINT '========================================';
PRINT 'Column "name" giờ có thể chứa tên sản phẩm dài tùy ý.';
PRINT '';

GO

