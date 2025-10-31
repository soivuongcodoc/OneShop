-- Fix notification type constraint
USE [OneShop]
GO

-- 1. Drop old constraint
DECLARE @ConstraintName nvarchar(200)
SELECT @ConstraintName = Name 
FROM SYS.CHECK_CONSTRAINTS 
WHERE PARENT_OBJECT_ID = OBJECT_ID('notifications') 
  AND COL_NAME(PARENT_OBJECT_ID, parent_column_id) = 'type'

IF @ConstraintName IS NOT NULL
BEGIN
    EXEC('ALTER TABLE notifications DROP CONSTRAINT ' + @ConstraintName)
    PRINT 'Dropped old constraint: ' + @ConstraintName
END

-- 2. Add new constraint with all enum values
ALTER TABLE notifications
ADD CONSTRAINT CK_notifications_type 
CHECK ([type] IN (
    'NEW_ORDER',
    'ORDER_CONFIRMED', 
    'ORDER_CANCELLED',
    'PRODUCT_DELETED',
    'SHOP_REQUEST_APPROVED',
    'SHOP_REQUEST_REJECTED',
    'NEW_SHOP_REQUEST'
))
GO

PRINT 'Successfully updated notification type constraint!'
GO
