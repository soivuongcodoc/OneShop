-- ============================================================
-- ALTER ADDRESSES TABLE - ADD IS_DEFAULT COLUMN
-- ============================================================
-- Script này thêm cột is_default vào bảng addresses
-- để đánh dấu địa chỉ mặc định cho mỗi user
-- ============================================================

USE [oneshop];
GO

PRINT '========================================';
PRINT '  ALTER ADDRESSES TABLE';
PRINT '========================================';
PRINT '';

-- Kiểm tra xem cột đã tồn tại chưa
IF NOT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'addresses' AND COLUMN_NAME = 'is_default'
)
BEGIN
    PRINT 'Đang thêm cột is_default vào bảng addresses...';
    
    ALTER TABLE addresses 
    ADD is_default BIT NOT NULL DEFAULT 0;
    
    PRINT '✓ Đã thêm cột is_default thành công!';
    PRINT '';
    
    -- Set địa chỉ đầu tiên của mỗi user làm mặc định
    PRINT 'Đang set địa chỉ đầu tiên của mỗi user làm mặc định...';
    
    WITH FirstAddress AS (
        SELECT 
            id,
            user_id,
            ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY id) as rn
        FROM addresses
    )
    UPDATE addresses
    SET is_default = 1
    WHERE id IN (SELECT id FROM FirstAddress WHERE rn = 1);
    
    DECLARE @updatedCount INT = @@ROWCOUNT;
    PRINT '✓ Đã set ' + CAST(@updatedCount AS NVARCHAR(10)) + ' địa chỉ làm mặc định';
END
ELSE
BEGIN
    PRINT '⊗ Cột is_default đã tồn tại - Bỏ qua';
END

PRINT '';
PRINT '========================================';
PRINT '  KẾT QUẢ';
PRINT '========================================';

-- Hiển thị thông tin bảng sau khi alter
SELECT 
    COLUMN_NAME AS [Tên cột],
    DATA_TYPE AS [Kiểu dữ liệu],
    IS_NULLABLE AS [Null?],
    COLUMN_DEFAULT AS [Giá trị mặc định]
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'addresses'
ORDER BY ORDINAL_POSITION;

PRINT '';
PRINT '========================================';
PRINT '  HOÀN TẤT!';
PRINT '========================================';
PRINT 'Bảng addresses đã được cập nhật với cột is_default.';
PRINT '';

GO

