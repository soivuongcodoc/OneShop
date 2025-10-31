-- ============================================
-- ONESHOP - DỮ LIỆU MẪU CHO SHOP MỸ PHẨM
-- ============================================

-- XÓA DỮ LIỆU CŨ (nếu có) - theo thứ tự foreign key (con → cha)
DELETE FROM notifications;
DELETE FROM order_details;
DELETE FROM orders;
DELETE FROM reviews;
DELETE FROM cart_items;
DELETE FROM carts;
DELETE FROM wishlists;
DELETE FROM viewed_products;
DELETE FROM addresses;
DELETE FROM customers;
DELETE FROM user_roles;
DELETE FROM products;
DELETE FROM promotions;
DELETE FROM shop_requests;
DELETE FROM shops;
DELETE FROM categories;
DELETE FROM coupons;
DELETE FROM payment_methods;
DELETE FROM users;
DELETE FROM roles;

-- 1. ROLES
SET IDENTITY_INSERT roles ON;
INSERT INTO roles (id, name) VALUES
(1, 'USER'),
(2, 'VENDOR'),
(3, 'ADMIN');
SET IDENTITY_INSERT roles OFF;

-- 2. USERS (Password: 123456)
SET IDENTITY_INSERT users ON;
INSERT INTO users (id, username, email, password, enabled) VALUES
-- Admin
(1, 'admin', 'admin@oneshop.vn', '$2a$10$CvfkJ/NZ/jTg3au1tVTh4ea3wZthFinF5tU62Ji48.qL4EgQeJLny', 1),
-- Vendors (Shop owners)
(2, 'beautyworld', 'beautyworld@gmail.com', '$2a$10$CvfkJ/NZ/jTg3au1tVTh4ea3wZthFinF5tU62Ji48.qL4EgQeJLny', 1),
(3, 'natureglow', 'natureglow@gmail.com', '$2a$10$CvfkJ/NZ/jTg3au1tVTh4ea3wZthFinF5tU62Ji48.qL4EgQeJLny', 1),
(4, 'skincarevn', 'skincare@gmail.com', '$2a$10$CvfkJ/NZ/jTg3au1tVTh4ea3wZthFinF5tU62Ji48.qL4EgQeJLny', 1),
-- Regular users
(5, 'nguyenvana', 'nguyenvana@gmail.com', '$2a$10$CvfkJ/NZ/jTg3au1tVTh4ea3wZthFinF5tU62Ji48.qL4EgQeJLny', 1),
(6, 'tranthib', 'tranthib@gmail.com', '$2a$10$CvfkJ/NZ/jTg3au1tVTh4ea3wZthFinF5tU62Ji48.qL4EgQeJLny', 1),
(7, 'lethic', 'lethic@gmail.com', '$2a$10$CvfkJ/NZ/jTg3au1tVTh4ea3wZthFinF5tU62Ji48.qL4EgQeJLny', 1),
(8, 'phamvand', 'phamvand@gmail.com', '$2a$10$CvfkJ/NZ/jTg3au1tVTh4ea3wZthFinF5tU62Ji48.qL4EgQeJLny', 1);
SET IDENTITY_INSERT users OFF;

-- 3. USER_ROLES
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 3), -- admin
(2, 2), -- vendor
(3, 2), -- vendor
(4, 2), -- vendor
(5, 1), -- user
(6, 1), -- user
(7, 1), -- user
(8, 1); -- user

-- 4. SHOPS
INSERT INTO shops (id, name, description, address, phone, logo_url, created_at, updated_at) VALUES
(2, N'Beauty World', N'Chuyên cung cấp mỹ phẩm cao cấp từ các thương hiệu nổi tiếng thế giới', N'123 Nguyễn Huệ, Q.1, TP.HCM', '0901234567', '/uploads/shops/beautyworld.jpg', GETDATE(), GETDATE()),
(3, N'Nature Glow', N'Mỹ phẩm thiên nhiên organic an toàn cho mọi làn da', N'456 Lê Lợi, Q.1, TP.HCM', '0912345678', '/uploads/shops/natureglow.jpg', GETDATE(), GETDATE()),
(4, N'Skincare VN', N'Chuyên về chăm sóc da, điều trị mụn và nám chuyên sâu', N'789 Trần Hưng Đạo, Q.5, TP.HCM', '0923456789', '/uploads/shops/skincarevn.jpg', GETDATE(), GETDATE());

-- 5. CATEGORIES
SET IDENTITY_INSERT categories ON;
INSERT INTO categories (id, name, description) VALUES
(1, N'Chăm sóc da mặt', N'Sữa rửa mặt, toner, serum, kem dưỡng'),
(2, N'Trang điểm', N'Son môi, phấn nền, mascara, phấn mắt'),
(3, N'Chăm sóc cơ thể', N'Sữa tắm, kem body, tẩy tế bào chết'),
(4, N'Chăm sóc tóc', N'Dầu gội, dầu xả, mặt nạ tóc'),
(5, N'Nước hoa', N'Nước hoa nam, nữ, unisex'),
(6, N'Mặt nạ', N'Mặt nạ giấy, mặt nạ ngủ, mặt nạ đất sét');
SET IDENTITY_INSERT categories OFF;

-- 6. PRODUCTS (Sản phẩm mỹ phẩm với sold > 10)
SET IDENTITY_INSERT products ON;
INSERT INTO products (id, shop_id, category_id, name, description, price, stock, image_url, active, featured, sold, created_at) VALUES
-- Beauty World Shop
(1, 2, 1, N'Sữa Rửa Mặt CeraVe Foaming Cleanser 236ml', N'Sữa rửa mặt dạng gel tạo bọt dịu nhẹ dành cho da thường đến da dầu. Chứa 3 ceramides thiết yếu giúp làm sạch sâu, loại bỏ dầu thừa mà không làm khô da. Công nghệ MVE giúp duy trì độ ẩm suốt 24 giờ.', 285000, 150, '/uploads/products/cerave-cleanser.jpg', 1, 1, 156, DATEADD(day, -60, GETDATE())),
(2, 2, 1, N'Kem Dưỡng La Roche-Posay Effaclar Duo+ 40ml', N'Kem dưỡng trị mụn từ thương hiệu dược mỹ phẩm Pháp. Giúp giảm mụn, mờ thâm, kiểm soát dầu hiệu quả. Phù hợp cho da nhạy cảm, da mụn.', 450000, 120, '/uploads/products/lrp-effaclar.jpg', 1, 1, 234, DATEADD(day, -55, GETDATE())),
(3, 2, 2, N'Son Kem Lì 3CE Velvet Lip Tint', N'Son kem lì lâu trôi với màu sắc trendy, kết cấu mềm mượt. Bám màu tốt, không gây khô môi. Thiết kế đầu cọ nhỏ giúp tô môi dễ dàng.', 320000, 200, '/uploads/products/3ce-liptint.jpg', 1, 1, 189, DATEADD(day, -50, GETDATE())),
(4, 2, 1, N'Serum Vitamin C The Ordinary 30ml', N'Serum Vitamin C 23% kết hợp HA Spheres 2% giúp làm sáng da, mờ thâm nám, chống lão hóa. Thích hợp cho da thiếu sức sống, da bị xỉn màu.', 180000, 180, '/uploads/products/to-vitaminc.jpg', 1, 0, 298, DATEADD(day, -45, GETDATE())),
(5, 2, 2, N'Phấn Nền Maybelline Fit Me Matte 30ml', N'Phấn nền dạng lỏng với độ che phủ vừa phải, lớp finish mịn lì tự nhiên. Kiểm soát dầu tốt, không gây mụn. 12 tông màu đa dạng.', 199000, 250, '/uploads/products/maybelline-fitme.jpg', 1, 1, 312, DATEADD(day, -40, GETDATE())),

-- Nature Glow Shop
(6, 3, 1, N'Sữa Rửa Mặt Innisfree Green Tea 150ml', N'Sữa rửa mặt chiết xuất từ trà xanh hữu cơ Jeju. Làm sạch nhẹ nhàng, cấp ẩm và cân bằng độ pH cho da. An toàn cho da nhạy cảm.', 165000, 300, '/uploads/products/innisfree-cleanser.jpg', 1, 1, 267, DATEADD(day, -58, GETDATE())),
(7, 3, 6, N'Mặt Nạ Giấy Some By Mi Tea Tree 10 Miếng', N'Mặt nạ giấy chiết xuất tràm trà giúp làm dịu da, kháng khuẩn, giảm mụn hiệu quả. Phù hợp cho da dầu mụn.', 89000, 500, '/uploads/products/somebymi-mask.jpg', 1, 1, 445, DATEADD(day, -52, GETDATE())),
(8, 3, 1, N'Nước Hoa Hồng Klairs Supple Preparation 180ml', N'Toner không mùi, không cồn với 10 loại thảo mộc thiên nhiên. Cân bằng độ pH, cấp ẩm sâu, chuẩn bị da cho các bước dưỡng tiếp theo.', 380000, 150, '/uploads/products/klairs-toner.jpg', 1, 0, 178, DATEADD(day, -48, GETDATE())),
(9, 3, 3, N'Sữa Tắm L''Occitane Almond Shower Oil 250ml', N'Dầu tắm chiết xuất hạnh nhân ngọt, dưỡng ẩm vượt trội. Hương thơm sang trọng, da mềm mịn sau khi tắm.', 520000, 80, '/uploads/products/loccitane-shower.jpg', 1, 1, 92, DATEADD(day, -44, GETDATE())),
(10, 3, 4, N'Dầu Gội Aromatica Rosemary 400ml', N'Dầu gội hữu cơ với tinh dầu hương thảo giúp kích thích mọc tóc, giảm rụng tóc. Làm sạch da đầu, tóc khỏe mạnh từ gốc.', 420000, 100, '/uploads/products/aromatica-shampoo.jpg', 1, 0, 134, DATEADD(day, -42, GETDATE())),

-- Skincare VN Shop
(11, 4, 1, N'Kem Trị Mụn Acnes Spot Care 9g', N'Kem bôi trị mụn tại chỗ với BHA và Sulfur. Giúp giảm sưng, kháng khuẩn, làm khô mụn nhanh chóng trong 3-5 ngày.', 65000, 400, '/uploads/products/acnes-spot.jpg', 1, 0, 523, DATEADD(day, -65, GETDATE())),
(12, 4, 1, N'Serum Trị Nám Vichy Mineral 89 50ml', N'Serum khoáng cô đặc 89% với Hyaluronic Acid giúp phục hồi, tăng cường hàng rào bảo vệ da. Làm mờ nám, sáng da an toàn.', 680000, 90, '/uploads/products/vichy-serum.jpg', 1, 1, 167, DATEADD(day, -38, GETDATE())),
(13, 4, 1, N'Kem Chống Nắng Bioré UV Aqua Rich 50g', N'Kem chống nắng dạng gel nước nhẹ bóng, thấm nhanh. SPF 50+ PA++++ bảo vệ da khỏi tia UV. Kháng nước, mồ hôi.', 180000, 350, '/uploads/products/biore-uv.jpg', 1, 1, 421, DATEADD(day, -35, GETDATE())),
(14, 4, 6, N'Mặt Nạ Ngủ Laneige Water Sleeping Mask 70ml', N'Mặt nạ ngủ cấp ẩm chuyên sâu với công nghệ Hydro Ionized Mineral Water. Da sáng mịn, căng bóng sau một đêm.', 520000, 110, '/uploads/products/laneige-mask.jpg', 1, 1, 203, DATEADD(day, -32, GETDATE())),
(15, 4, 1, N'Tinh Chất AHA/BHA Cosrx 100ml', N'Tinh chất tẩy da chết hóa học với AHA, BHA và 10% Willow Bark Water. Làm sạch lỗ chân lông, giảm mụn đầu đen, mịn da.', 320000, 140, '/uploads/products/cosrx-aha.jpg', 1, 0, 256, DATEADD(day, -30, GETDATE())),

-- Thêm sản phẩm nước hoa
(16, 2, 5, N'Nước Hoa Chanel Coco Mademoiselle 50ml', N'Hương thơm quyến rũ, sang trọng với note hoa hồng, vani và patchouli. Lưu hương 8-10 giờ. Phù hợp cho phái nữ thanh lịch.', 2850000, 45, '/uploads/products/chanel-coco.jpg', 1, 1, 78, DATEADD(day, -28, GETDATE())),
(17, 2, 5, N'Nước Hoa Dior Sauvage EDT 60ml', N'Hương thơm nam tính mạnh mẽ với note ớt hồng, bergamot và hổ phách. Lưu hương lâu, phù hợp cho nam giới năng động.', 2650000, 60, '/uploads/products/dior-sauvage.jpg', 1, 1, 95, DATEADD(day, -26, GETDATE())),

-- Sản phẩm mới ra
(18, 3, 1, N'Kem Dưỡng Ẩm Neutrogena Hydro Boost 50ml', N'Kem dưỡng ẩm dạng gel với Hyaluronic Acid giúp cấp ẩm tức thì. Kết cấu nhẹ, thấm nhanh, không gây nhờn.', 299000, 200, '/uploads/products/neutrogena-hydro.jpg', 1, 1, 15, DATEADD(day, -5, GETDATE())),
(19, 4, 2, N'Mascara Maybelline Lash Sensational Sky High', N'Mascara làm dài và cong mi với công thức Bamboo Extract. Không lem, không vón cục. Giữ nếp tốt cả ngày.', 249000, 180, '/uploads/products/maybelline-mascara.jpg', 1, 0, 32, DATEADD(day, -3, GETDATE())),
(20, 3, 1, N'Kem Dưỡng Trắng Da Innisfree Jeju Cherry Blossom 50ml', N'Kem dưỡng trắng da chiết xuất hoa anh đào Jeju. Làm sáng, mờ thâm, cải thiện tông màu da đều màu tự nhiên.', 385000, 150, '/uploads/products/innisfree-cherry.jpg', 1, 0, 23, DATEADD(day, -2, GETDATE()));
SET IDENTITY_INSERT products OFF;

-- 7. PAYMENT METHODS
SET IDENTITY_INSERT payment_methods ON;
INSERT INTO payment_methods (id, name, display_name, active) VALUES
(1, 'COD', N'Thanh toán khi nhận hàng', 1),
(2, 'VNPAY', N'Thanh toán qua VNPay', 1),
(3, 'MOMO', N'Thanh toán qua MoMo', 1);
SET IDENTITY_INSERT payment_methods OFF;

-- 8. COUPONS (Mã giảm giá)
SET IDENTITY_INSERT coupons ON;
INSERT INTO coupons (id, code, discount_type, discount_value, start_time, end_time, active) VALUES
(1, 'WELCOME10', 'PERCENTAGE', 10.00, DATEADD(day, -10, GETDATE()), DATEADD(day, 30, GETDATE()), 1),
(2, 'FREESHIP', 'AMOUNT', 30000.00, DATEADD(day, -5, GETDATE()), DATEADD(day, 25, GETDATE()), 1),
(3, 'BEAUTY20', 'PERCENTAGE', 20.00, DATEADD(day, -3, GETDATE()), DATEADD(day, 15, GETDATE()), 1),
(4, 'SUMMER50K', 'AMOUNT', 50000.00, GETDATE(), DATEADD(day, 60, GETDATE()), 1);
SET IDENTITY_INSERT coupons OFF;

-- 9. CUSTOMERS
SET IDENTITY_INSERT customers ON;
INSERT INTO customers (id, user_id, full_name, phone) VALUES
(1, 5, N'Nguyễn Văn A', '0901111111'),
(2, 6, N'Trần Thị B', '0902222222'),
(3, 7, N'Lê Thị C', '0903333333'),
(4, 8, N'Phạm Văn D', '0904444444');
SET IDENTITY_INSERT customers OFF;

-- 10. ADDRESSES (Địa chỉ giao hàng)
SET IDENTITY_INSERT addresses ON;
INSERT INTO addresses (id, user_id, name, address, active, is_default) VALUES
(1, 5, N'Nhà riêng', N'123 Lê Văn Việt, P.Hiệp Phú, Q.9, TP.HCM', 1, 1),
(2, 5, N'Văn phòng', N'999 Quang Trung, P.14, Gò Vấp, TP.HCM', 1, 0),
(3, 6, N'Nhà riêng', N'456 Võ Văn Ngân, P.Linh Chiểu, Thủ Đức, TP.HCM', 1, 1),
(4, 7, N'Nhà riêng', N'789 Nguyễn Thái Sơn, P.4, Gò Vấp, TP.HCM', 1, 1),
(5, 8, N'Nhà riêng', N'321 Cách Mạng Tháng 8, P.7, Q.3, TP.HCM', 1, 1);
SET IDENTITY_INSERT addresses OFF;

-- 11. ORDERS (Đơn hàng với đủ trạng thái)
SET IDENTITY_INSERT orders ON;
INSERT INTO orders (id, customer_id, shop_id, payment_method_id, total_amount, shipping_address, status, order_date) VALUES
-- CONFIRMED (Đã xác nhận - đơn cũ)
(1, 1, 2, 1, 735000, N'123 Lê Văn Việt, P.Hiệp Phú, Q.9, TP.HCM', 'CONFIRMED', DATEADD(day, -15, GETDATE())),
(2, 2, 3, 2, 545000, N'456 Võ Văn Ngân, P.Linh Chiểu, Thủ Đức, TP.HCM', 'CONFIRMED', DATEADD(day, -12, GETDATE())),
(3, 3, 4, 1, 860000, N'789 Nguyễn Thái Sơn, P.4, Gò Vấp, TP.HCM', 'CONFIRMED', DATEADD(day, -10, GETDATE())),
-- CONFIRMED (Đã xác nhận)
(4, 1, 3, 1, 469000, N'123 Lê Văn Việt, P.Hiệp Phú, Q.9, TP.HCM', 'CONFIRMED', DATEADD(day, -3, GETDATE())),
(5, 4, 2, 3, 2850000, N'321 Cách Mạng Tháng 8, P.7, Q.3, TP.HCM', 'CONFIRMED', DATEADD(day, -2, GETDATE())),
(6, 2, 4, 1, 500000, N'456 Võ Văn Ngân, P.Linh Chiểu, Thủ Đức, TP.HCM', 'CONFIRMED', DATEADD(day, -1, GETDATE())),
(7, 3, 2, 2, 770000, N'789 Nguyễn Thái Sơn, P.4, Gò Vấp, TP.HCM', 'CONFIRMED', GETDATE()),
-- PENDING (Chờ xác nhận)
(8, 1, 4, 1, 385000, N'999 Quang Trung, P.14, Gò Vấp, TP.HCM', 'PENDING', GETDATE()),
(9, 4, 3, 1, 254000, N'321 Cách Mạng Tháng 8, P.7, Q.3, TP.HCM', 'PENDING', GETDATE()),
-- CANCELLED (Đã hủy)
(10, 2, 2, 1, 320000, N'456 Võ Văn Ngân, P.Linh Chiểu, Thủ Đức, TP.HCM', 'CANCELLED', DATEADD(day, -5, GETDATE()));
SET IDENTITY_INSERT orders OFF;

-- 12. ORDER DETAILS
SET IDENTITY_INSERT order_details ON;
INSERT INTO order_details (id, order_id, product_id, quantity, price) VALUES
-- Order 1 (DELIVERED)
(1, 1, 1, 2, 285000),
(2, 1, 3, 1, 320000),
-- Order 2 (DELIVERED)
(3, 2, 6, 2, 165000),
(4, 2, 7, 1, 89000),
-- Order 3 (DELIVERED)
(5, 3, 12, 1, 680000),
(6, 3, 13, 1, 180000),
-- Order 4 (SHIPPING)
(7, 4, 8, 1, 380000),
(8, 4, 7, 1, 89000),
-- Order 5 (SHIPPING)
(9, 5, 16, 1, 2850000),
-- Order 6 (CONFIRMED)
(10, 6, 14, 1, 520000),
-- Order 7 (CONFIRMED)
(11, 7, 2, 1, 450000),
(12, 7, 5, 1, 199000),
-- Order 8 (PENDING)
(13, 8, 20, 1, 385000),
-- Order 9 (PENDING)
(14, 9, 6, 1, 165000),
(15, 9, 7, 1, 89000),
-- Order 10 (CANCELLED)
(16, 10, 3, 1, 320000);
SET IDENTITY_INSERT order_details OFF;

-- 13. REVIEWS (Đánh giá với comment >= 50 ký tự và có media)
SET IDENTITY_INSERT reviews ON;
INSERT INTO reviews (id, user_id, product_id, rating, comment, media_url, created_at) VALUES
(1, 5, 1, 5, N'Sản phẩm rất tuyệt vời, làm sạch sâu nhưng không hề khô da. Sau khi dùng da mềm mịn, sạch sẽ. Mình đã dùng được 2 tuần và thấy da cải thiện rõ rệt. Rất đáng để mua và sử dụng lâu dài.', '/uploads/reviews/review1.jpg', DATEADD(day, -10, GETDATE())),
(2, 5, 3, 4, N'Màu son đẹp lắm, bám màu tốt. Nhưng hơi khô môi một chút nếu không dưỡng môi trước. Về tổng thể thì mình vẫn rất hài lòng với sản phẩm này, sẽ mua thêm màu khác.', '/uploads/reviews/review2.jpg', DATEADD(day, -9, GETDATE())),
(3, 2, 6, 5, N'Sữa rửa mặt organic rất nhẹ nhàng, mùi trà xanh thơm mát. Da mình nhạy cảm mà dùng vẫn ổn, không bị kích ứng gì cả. Giá cả hợp lý, chất lượng tốt. Highly recommended cho các bạn da nhạy cảm nhé!', NULL, DATEADD(day, -8, GETDATE())),
(4, 2, 7, 5, N'Mặt nạ rất tốt cho da mụn, sau khi đắp thấy da dịu hẳn xuống. Mụn cũng xẹp nhanh hơn. Một hộp 10 miếng dùng được lâu lắm. Mình sẽ ủng hộ thương hiệu này tiếp.', '/uploads/reviews/review3.jpg', DATEADD(day, -7, GETDATE())),
(5, 3, 12, 5, N'Serum này giúp da sáng lên rõ rệt sau 1 tháng sử dụng. Nám mờ đi nhiều, da đều màu hơn. Kết cấu thấm nhanh, không nhờn. Đáng đồng tiền bát gạo. Chắc chắn sẽ repurchase khi hết lọ này.', '/uploads/reviews/review4.jpg', DATEADD(day, -6, GETDATE())),
(6, 3, 13, 4, N'Kem chống nắng thấm nhanh, không gây nhờn dính. Bảo vệ da tốt trong ngày nắng. Mình dùng đi làm hàng ngày, da không bị sạm đen. Giá hơi cao nhưng chất lượng xứng đáng.', NULL, DATEADD(day, -5, GETDATE())),
(7, 1, 8, 5, N'Toner này cấp ẩm rất tốt, da căng mịn sau khi dùng. Không có mùi hương gì cả nên rất an toàn cho da nhạy cảm. Chai to dùng được lâu, giá cả hợp lý. Mình đã repurchase lần 2 rồi đó.', '/uploads/reviews/review5.jpg', DATEADD(day, -4, GETDATE()));
SET IDENTITY_INSERT reviews OFF;

-- 14. WISHLISTS (Sản phẩm yêu thích)
SET IDENTITY_INSERT wishlists ON;
INSERT INTO wishlists (id, user_id, product_id) VALUES
(1, 5, 2),
(2, 5, 16),
(3, 5, 14),
(4, 6, 12),
(5, 6, 15),
(6, 6, 17),
(7, 7, 1),
(8, 7, 9),
(9, 8, 16),
(10, 8, 17);
SET IDENTITY_INSERT wishlists OFF;

-- 15. VIEWED PRODUCTS (Sản phẩm đã xem)
SET IDENTITY_INSERT viewed_products ON;
INSERT INTO viewed_products (id, user_id, product_id, viewed_at) VALUES
(1, 5, 1, DATEADD(hour, -2, GETDATE())),
(2, 5, 2, DATEADD(hour, -1, GETDATE())),
(3, 5, 16, DATEADD(minute, -30, GETDATE())),
(4, 6, 6, DATEADD(hour, -5, GETDATE())),
(5, 6, 7, DATEADD(hour, -4, GETDATE())),
(6, 6, 12, DATEADD(hour, -3, GETDATE())),
(7, 7, 13, DATEADD(hour, -6, GETDATE())),
(8, 7, 14, DATEADD(minute, -45, GETDATE())),
(9, 8, 16, DATEADD(hour, -1, GETDATE())),
(10, 8, 17, DATEADD(minute, -15, GETDATE()));
SET IDENTITY_INSERT viewed_products OFF;
