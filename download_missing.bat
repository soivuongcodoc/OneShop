@echo off
chcp 65001 >nul
color 0E
cls

echo ========================================
echo   TẢI 3 ẢNH CÒN THIẾU
echo ========================================
echo.
echo Tải lại 3 ảnh bị lỗi 404:
echo   - ID 1003: Kem chống nắng Biore
echo   - ID 1018: Phấn mắt Urban Decay
echo   - ID 1019: Bột phủ Laura Mercier
echo.
pause

echo.
echo Đang chạy PowerShell script...
echo.

powershell.exe -ExecutionPolicy Bypass -File "%~dp0download_missing_images.ps1"

echo.
pause

