# ============================================================
# Script tải ảnh sản phẩm từ URL về local
# OneShop Project - Download Product Images
# ============================================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  DOWNLOAD PRODUCT IMAGES" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Tạo folder nếu chưa có
$uploadDir = "uploads\user"
if (-not (Test-Path $uploadDir)) {
    New-Item -ItemType Directory -Path $uploadDir -Force | Out-Null
    Write-Host "✓ Đã tạo folder: $uploadDir" -ForegroundColor Green
} else {
    Write-Host "✓ Folder đã tồn tại: $uploadDir" -ForegroundColor Green
}
Write-Host ""

# Danh sách URL ảnh từ database (ID từ 1001-1020)
$images = @(
    # Sản phẩm bán chạy (1001-1005)
    @{Id=1001; Url="https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=500"; Name="serum-vitamin-c-klairs.jpg"},
    @{Id=1002; Url="https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500"; Name="xit-khoang-la-roche-posay.jpg"},
    @{Id=1003; Url="https://images.unsplash.com/photo-1585652757173-57de5e9f23de?w=500"; Name="kem-chong-nang-biore.jpg"},
    @{Id=1004; Url="https://images.unsplash.com/photo-1571875257727-256c39da42af?w=500"; Name="sua-rua-mat-cerave.jpg"},
    @{Id=1005; Url="https://images.unsplash.com/photo-1596755389378-c31d21fd1273?w=500"; Name="kem-duong-am-neutrogena.jpg"},
    
    # Sản phẩm mới nhất (1006-1010)
    @{Id=1006; Url="https://images.unsplash.com/photo-1457972729786-0411a3b2b626?w=500"; Name="mat-na-innisfree.jpg"},
    @{Id=1007; Url="https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?w=500"; Name="nuoc-hoa-hong-mamonde.jpg"},
    @{Id=1008; Url="https://images.unsplash.com/photo-1612817288484-6f916006741a?w=500"; Name="kem-mat-laneige.jpg"},
    @{Id=1009; Url="https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?w=500"; Name="serum-retinol-roc.jpg"},
    @{Id=1010; Url="https://images.unsplash.com/photo-1505944270255-72b8c68c6a70?w=500"; Name="tinh-dau-duong-toc-loreal.jpg"},
    
    # Sản phẩm đánh giá cao (1011-1015)
    @{Id=1011; Url="https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=500"; Name="toner-aha-bha-cosrx.jpg"},
    @{Id=1012; Url="https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?w=500"; Name="mat-na-ngu-laneige.jpg"},
    @{Id=1013; Url="https://images.unsplash.com/photo-1535585209827-a15fcdbc4c2d?w=500"; Name="dau-duong-toc-moroccanoil.jpg"},
    @{Id=1014; Url="https://images.unsplash.com/photo-1526047932273-341f2a7631f9?w=500"; Name="xit-khoang-avene.jpg"},
    @{Id=1015; Url="https://images.unsplash.com/photo-1608248543803-ba4f8c70ae0b?w=500"; Name="gel-aloe-vera-nature-republic.jpg"},
    
    # Sản phẩm yêu thích (1016-1020)
    @{Id=1016; Url="https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=500"; Name="kem-nen-fenty-beauty.jpg"},
    @{Id=1017; Url="https://images.unsplash.com/photo-1512496015851-a90fb38ba796?w=500"; Name="son-moi-mac.jpg"},
    @{Id=1018; Url="https://images.unsplash.com/photo-1583241800698-c014c1f50c68?w=500"; Name="phan-mat-urban-decay.jpg"},
    @{Id=1019; Url="https://images.unsplash.com/photo-1590393876643-e8b5f38cf1d7?w=500"; Name="bot-phu-laura-mercier.jpg"},
    @{Id=1020; Url="https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=500"; Name="mascara-maybelline.jpg"}
)

Write-Host "Bắt đầu tải $($images.Count) ảnh..." -ForegroundColor Yellow
Write-Host ""

$success = 0
$failed = 0

foreach ($img in $images) {
    $fileName = $img.Name
    $filePath = Join-Path $uploadDir $fileName
    
    try {
        Write-Host "[ID: $($img.Id)] Đang tải: $fileName..." -NoNewline
        
        # Tải ảnh
        Invoke-WebRequest -Uri $img.Url -OutFile $filePath -ErrorAction Stop
        
        # Kiểm tra file đã tải
        if (Test-Path $filePath) {
            $fileSize = (Get-Item $filePath).Length
            $fileSizeKB = [math]::Round($fileSize / 1KB, 2)
            Write-Host " ✓ OK ($fileSizeKB KB)" -ForegroundColor Green
            $success++
        } else {
            Write-Host " ✗ FAILED" -ForegroundColor Red
            $failed++
        }
        
        # Delay nhỏ để tránh bị block
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
Write-Host "✓ Thành công: $success ảnh" -ForegroundColor Green
Write-Host "✗ Thất bại: $failed ảnh" -ForegroundColor Red
Write-Host "📁 Vị trí: $(Resolve-Path $uploadDir)" -ForegroundColor Yellow
Write-Host ""

if ($success -gt 0) {
    Write-Host "🎉 Hoàn tất! Hãy chạy file 'update_image_paths.sql' để cập nhật database." -ForegroundColor Green
}

Write-Host ""
Write-Host "Nhấn Enter để thoát..." -ForegroundColor Gray
Read-Host

