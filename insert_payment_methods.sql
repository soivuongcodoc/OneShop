-- ============================================================
-- INSERT PAYMENT METHODS (VNPAY, COD, MOMO)
-- ============================================================
-- Script này thêm các phương thức thanh toán vào bảng payment_methods
-- ============================================================

USE [oneshop];
GO

PRINT '========================================';
PRINT '  THÊM PHƯƠNG THỨC THANH TOÁN';
PRINT '========================================';
PRINT '';

-- Kiểm tra và thêm COD (Cash On Delivery)
IF NOT EXISTS (SELECT 1 FROM payment_methods WHERE name = 'COD')
BEGIN
    INSERT INTO payment_methods (name, display_name, active)
    VALUES (N'COD', N'Thanh toán khi nhận hàng (COD)', 1);
    PRINT '✓ Đã thêm: COD - Thanh toán khi nhận hàng';
END
ELSE
BEGIN
    PRINT '⊗ COD đã tồn tại - Bỏ qua';
END

-- Kiểm tra và thêm VNPAY
IF NOT EXISTS (SELECT 1 FROM payment_methods WHERE name = 'VNPAY')
BEGIN
    INSERT INTO payment_methods (name, display_name, active)
    VALUES (N'VNPAY', N'Thanh toán qua VNPAY', 1);
    PRINT '✓ Đã thêm: VNPAY - Thanh toán qua VNPAY';
END
ELSE
BEGIN
    PRINT '⊗ VNPAY đã tồn tại - Bỏ qua';
END

-- Kiểm tra và thêm MOMO
IF NOT EXISTS (SELECT 1 FROM payment_methods WHERE name = 'MOMO')
BEGIN
    INSERT INTO payment_methods (name, display_name, active)
    VALUES (N'MOMO', N'Thanh toán qua Ví MoMo', 1);
    PRINT '✓ Đã thêm: MOMO - Thanh toán qua Ví MoMo';
END
ELSE
BEGIN
    PRINT '⊗ MOMO đã tồn tại - Bỏ qua';
END

-- Kiểm tra và thêm ZALOPAY (Optional)
IF NOT EXISTS (SELECT 1 FROM payment_methods WHERE name = 'ZALOPAY')
BEGIN
    INSERT INTO payment_methods (name, display_name, active)
    VALUES (N'ZALOPAY', N'Thanh toán qua ZaloPay', 1);
    PRINT '✓ Đã thêm: ZALOPAY - Thanh toán qua ZaloPay';
END
ELSE
BEGIN
    PRINT '⊗ ZALOPAY đã tồn tại - Bỏ qua';
END

-- Kiểm tra và thêm BANK_TRANSFER (Optional)
IF NOT EXISTS (SELECT 1 FROM payment_methods WHERE name = 'BANK_TRANSFER')
BEGIN
    INSERT INTO payment_methods (name, display_name, active)
    VALUES (N'BANK_TRANSFER', N'Chuyển khoản ngân hàng', 1);
    PRINT '✓ Đã thêm: BANK_TRANSFER - Chuyển khoản ngân hàng';
END
ELSE
BEGIN
    PRINT '⊗ BANK_TRANSFER đã tồn tại - Bỏ qua';
END

PRINT '';
PRINT '========================================';
PRINT '  KẾT QUẢ';
PRINT '========================================';

-- Hiển thị tất cả phương thức thanh toán
SELECT 
    id AS [ID],
    name AS [Tên],
    display_name AS [Tên hiển thị],
    CASE 
        WHEN active = 1 THEN N'Hoạt động'
        ELSE N'Không hoạt động'
    END AS [Trạng thái]
FROM payment_methods
ORDER BY id;

PRINT '';
PRINT '========================================';
PRINT '  HOÀN TẤT!';
PRINT '========================================';
PRINT 'Tổng số phương thức thanh toán: ' + CAST((SELECT COUNT(*) FROM payment_methods) AS NVARCHAR(10));
PRINT '';

GO

