@echo off
chcp 65001 >nul
color 0A
cls

echo ========================================
echo   TẢI ẢNH SẢN PHẨM VỀ LOCAL
echo ========================================
echo.
echo Script này sẽ tải 20 ảnh sản phẩm về folder uploads\user
echo.
pause

echo.
echo Đang chạy PowerShell script...
echo.

powershell.exe -ExecutionPolicy Bypass -File "%~dp0download_product_images.ps1"

echo.
echo ========================================
echo   HOÀN TẤT!
echo ========================================
echo.
echo Tiếp theo: Chạy file update_image_paths.sql trong SQL Server
echo để cập nhật database.
echo.
pause

