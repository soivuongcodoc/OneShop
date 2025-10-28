# ============================================================
# Script tải 3 ảnh còn thiếu (ID: 1003, 1018, 1019)
# OneShop Project - Download Missing Images
# ============================================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  TẢI 3 ẢNH CÒN THIẾU" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$uploadDir = "uploads\user"

# 3 ảnh bị lỗi 404 - dùng URL mới
$missingImages = @(
    @{Id=1003; Url="https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=500"; Name="kem-chong-nang-biore.jpg"},
    @{Id=1018; Url="https://images.unsplash.com/photo-1512496015851-a90fb38ba796?w=500"; Name="phan-mat-urban-decay.jpg"},
    @{Id=1019; Url="https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=500"; Name="bot-phu-laura-mercier.jpg"}
)

Write-Host "Đang tải 3 ảnh còn thiếu..." -ForegroundColor Yellow
Write-Host ""

$success = 0
$failed = 0

foreach ($img in $missingImages) {
    $fileName = $img.Name
    $filePath = Join-Path $uploadDir $fileName
    
    try {
        Write-Host "[ID: $($img.Id)] Đang tải: $fileName..." -NoNewline
        
        Invoke-WebRequest -Uri $img.Url -OutFile $filePath -ErrorAction Stop
        
        if (Test-Path $filePath) {
            $fileSize = (Get-Item $filePath).Length
            $fileSizeKB = [math]::Round($fileSize / 1KB, 2)
            Write-Host " ✓ OK ($fileSizeKB KB)" -ForegroundColor Green
            $success++
        } else {
            Write-Host " ✗ FAILED" -ForegroundColor Red
            $failed++
        }
        
        Start-Sleep -Milliseconds 500
        
    } catch {
        Write-Host " ✗ ERROR: $($_.Exception.Message)" -ForegroundColor Red
        $failed++
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  KẾT QUẢ" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✓ Thành công: $success/3 ảnh" -ForegroundColor Green
Write-Host "✗ Thất bại: $failed/3 ảnh" -ForegroundColor Red

if ($success -eq 3) {
    Write-Host ""
    Write-Host "🎉 HOÀN HẢO! Giờ có đủ 20/20 ảnh!" -ForegroundColor Green
    Write-Host "📁 Vị trí: $(Resolve-Path $uploadDir)" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "👉 Tiếp theo: Chạy file 'update_image_paths.sql' để cập nhật database." -ForegroundColor Cyan
}

Write-Host ""
Write-Host "Nhấn Enter để thoát..." -ForegroundColor Gray
Read-Host

