# File Tree: OneShop-vendor
```markdown
# File Tree: OneShop-vendor

**Generated:** 10/28/2025, 1:05:39 PM
**Root Path:** `e:\OneShop-vendor\OneShop-vendor`

```
├── 📁 .mvn
│   └── 📁 wrapper
│       └── 📄 maven-wrapper.properties — cấu hình wrapper của Maven
├── 📁 lib
│   └── 📄 sitemesh-3.0.1-jakarta.jar — thư viện SiteMesh (decorator trang)
├── 📁 src
# File Tree: OneShop-vendor

Generated: 10/28/2025, 1:05:39 PM
Root Path: e:\OneShop-vendor\OneShop-vendor

├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties — cấu hình wrapper của Maven
├── lib/
│   └── sitemesh-3.0.1-jakarta.jar — thư viện SiteMesh (decorator trang)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/oneshop/
│   │   │       ├── config/
│   │   │       │   ├── SecurityConfig.java — cấu hình Spring Security (quyền & auth)
│   │   │       │   ├── SiteMeshConfig.java — cấu hình SiteMesh (decorator templates)
│   │   │       │   └── WebMvcConfig.java — cấu hình MVC (resolver, resource handlers)
│   │   │       ├── controller/
│   │   │       │   ├── admin/
│   │   │       │   │   ├── AdminCategoryController.java — quản lý danh mục (admin)
│   │   │       │   │   └── AdminController.java — các hành động quản trị chính
│   │   │       │   ├── auth/
│   │   │       │   │   ├── AuthController.java — xử lý đăng nhập/đăng ký/đăng xuất
│   │   │       │   │   └── VerifyController.java — xử lý xác thực email/OTP
│   │   │       │   ├── user/
│   │   │       │   │   ├── CartController.java — quản lý giỏ hàng cho người dùng
│   │   │       │   │   └── UserController.java — trang/profile/người dùng
│   │   │       │   ├── vendor/
│   │   │       │   │   ├── VendorController.java — dashboard và hành động vendor
│   │   │       │   │   ├── VendorPromotionController.java — quản lý khuyến mãi của vendor
│   │   │       │   │   └── VendorSettingsController.java — cài đặt cửa hàng vendor
│   │   │       │   ├── DecoratorController.java — controller để xử lý decorators/layouts
│   │   │       │   ├── GlobalExceptionHandler.java — bắt & xử lý ngoại lệ toàn cục
│   │   │       │   └── PageController.java — các route trang công cộng (home, index...)
│   │   │       ├── dto/
│   │   │       │   ├── auth/
│   │   │       │   │   └── AuthDtos.java — DTO cho auth (login/register requests/responses)
│   │   │       │   ├── vendor/
│   │   │       │   │   ├── ProductForm.java — DTO form sản phẩm (create/update)
│   │   │       │   │   ├── PromotionForm.java — DTO form khuyến mãi
│   │   │       │   │   └── ShopForm.java — DTO form cửa hàng
│   │   │       │   └── CartItem.java — DTO phần tử giỏ hàng
│   │   │       ├── entity/
│   │   │       │   ├── Category.java — entity danh mục sản phẩm
│   │   │       │   ├── Customer.java — entity khách hàng
│   │   │       │   ├── Order.java — entity đơn hàng
│   │   │       │   ├── OrderDetail.java — entity chi tiết đơn hàng
│   │   │       │   ├── OrderStatus.java — enum/trạng thái đơn hàng
│   │   │       │   ├── OtpCode.java — entity mã OTP cho xác thực
│   │   │       │   ├── PaymentMethod.java — entity phương thức thanh toán
│   │   │       │   ├── Product.java — entity sản phẩm
│   │   │       │   ├── Promotion.java — entity khuyến mãi
│   │   │       │   ├── PromotionDiscountType.java — enum loại giảm giá
│   │   │       │   ├── Review.java — entity đánh giá sản phẩm
│   │   │       │   ├── Role.java — entity vai trò người dùng (ROLE_USER/ADMIN...)
│   │   │       │   ├── Shop.java — entity cửa hàng/vendor
│   │   │       │   └── User.java — entity người dùng/chủ tài khoản
│   │   │       ├── repository/
│   │   │       │   ├── CategoryRepository.java — CRUD danh mục
│   │   │       │   ├── CustomerRepository.java — CRUD khách hàng
│   │   │       │   ├── OrderDetailRepository.java — CRUD chi tiết đơn
│   │   │       │   ├── OrderRepository.java — CRUD đơn hàng
│   │   │       │   ├── OtpCodeRepository.java — quản lý OTP
│   │   │       │   ├── PaymentMethodRepository.java — CRUD phương thức thanh toán
│   │   │       │   ├── PaymentRepository.java — ghi nhận thanh toán
│   │   │       │   ├── ProductRepository.java — CRUD sản phẩm / tìm kiếm
│   │   │       │   ├── PromotionRepository.java — CRUD khuyến mãi
│   │   │       │   ├── ReviewRepository.java — CRUD đánh giá
│   │   │       │   ├── RoleRepository.java — CRUD vai trò
│   │   │       │   ├── ShopRepository.java — CRUD cửa hàng
│   │   │       │   └── UserRepository.java — CRUD người dùng
│   │   │       ├── security/
│   │   │       │   ├── AuthFacade.java — tiện ích lấy thông tin auth hiện tại
│   │   │       │   ├── JwtAuthenticationFilter.java — filter xử lý JWT trong request
│   │   │       │   └── JwtTokenProvider.java — sinh & xác thực JWT token
│   │   │       ├── service/
│   │   │       │   ├── admin/
│   │   │       │   │   ├── CategoryService.java — interface dịch vụ danh mục (admin)
│   │   │       │   │   └── UserService.java — interface dịch vụ quản trị người dùng
│   │   │       │   ├── auth/
│   │   │       │   │   └── AuthService.java — logic đăng nhập/đăng ký/OTP
│   │   │       │   ├── impl/
│   │   │       │   │   ├── AdminUserServiceImpl.java — triển khai UserService cho admin
│   │   │       │   │   ├── CategoryServiceImpl.java — triển khai CategoryService
│   │   │       │   │   └── UserServiceImpl.java — triển khai UserService (người dùng)
│   │   │       │   ├── user/
│   │   │       │   │   └── UserService.java — interface dịch vụ người dùng (public)
│   │   │       │   └── MailService.java — gửi email (OTP, verify, thông báo)
│   │   │       └── OneshopApplication.java — lớp boot Spring Boot (main)
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css — stylesheet chính cho giao diện
│   │       │   └── js/
│   │       │       └── auth.js — JS xử lý giao diện xác thực/OTP trên client
│   │       ├── templates/
│   │       │   ├── admin/
│   │       │   │   ├── category/
│   │       │   │   │   ├── form.html — form tạo/sửa danh mục (admin)
│   │       │   │   │   └── list.html — danh sách danh mục (admin)
│   │       │   │   ├── users/
│   │       │   │   │   └── user-list.html — danh sách người dùng (admin)
│   │       │   │   └── dashboard.html — dashboard trang admin
│   │       │   ├── auth/
│   │       │   │   ├── forgot-password.html — giao diện quên mật khẩu
│   │       │   │   ├── login.html — giao diện đăng nhập
│   │       │   │   ├── register.html — giao diện đăng ký
│   │       │   │   ├── reset-password.html — giao diện đặt lại mật khẩu
│   │       │   │   └── verify.html — giao diện xác thực/nhập OTP
│   │       │   ├── decorators/
│   │       │   │   ├── admin-layout.html — layout chung cho admin
│   │       │   │   ├── main.html — layout trang chính
│   │       │   │   └── vendor-layout.html — layout cho vendor
│   │       │   ├── fragments/
│   │       │   │   ├── admin-footer.html — fragment footer admin
│   │       │   │   ├── admin-header.html — fragment header admin
│   │       │   │   ├── footer.html — fragment footer chung
│   │       │   │   └── header.html — fragment header chung
│   │       │   ├── user/
│   │       │   │   ├── cart.html — giao diện giỏ hàng người dùng
│   │       │   │   ├── order_history.html — lịch sử đơn của user
│   │       │   │   └── profile.html — trang profile người dùng
│   │       │   ├── vendor/
│   │       │   │   ├── dashboard.html — dashboard vendor
│   │       │   │   ├── home.html — trang chủ vendor
│   │       │   │   ├── order-detail.html — chi tiết đơn hàng (vendor)
│   │       │   │   ├── orders.html — danh sách đơn (vendor)
│   │       │   │   ├── product-detail.html — chi tiết sản phẩm
│   │       │   │   ├── product-form.html — form tạo/sửa sản phẩm
│   │       │   │   ├── product.html — danh sách sản phẩm vendor
│   │       │   │   ├── profile.html — trang profile vendor
│   │       │   │   ├── promotion-form.html — form khuyến mãi
│   │       │   │   ├── promotion.html — danh sách khuyến mãi
│   │       │   │   ├── revenue.html — báo cáo doanh thu
│   │       │   │   └── settings.html — cài đặt cửa hàng vendor
│   │       │   ├── error.html — trang lỗi chung
│   │       │   ├── home.html — trang chủ public
│   │       │   ├── index.html — landing/index
│   │       │   └── test.html — trang thử nghiệm
│   │       └── application.properties — cấu hình ứng dụng
│   └── test/
│       └── java/
│           └── com/oneshop/
│               └── OneshopApplicationTests.java — test khởi động ứng dụng (Spring Boot test)
├── uploads/
│   ├── products/
│   │   ├── 4310bbc3-cc49-427d-b442-3164197e2dc5.png
│   │   └── c32e0fe0-0fde-44e6-b5c5-6477a92b7d3e.png
│   └── shops/
│       ├── 4133998c-4095-4200-a876-9a79c0791630.jpg
│       ├── 92937c74-41a7-41bd-a9d9-d1bbd0e96da2.jpg
│       ├── b836e4bb-814d-468d-8ab6-b79ae76a9870.jpg
│       └── bdd98a66-f2ad-4dff-8179-6bf1a22b60e6.jpg
├── .gitattributes
├── .gitignore
├── mvnw — script wrapper cho Maven (Unix)
├── mvnw.cmd — script wrapper cho Maven (Windows)
├── oneshop.sql — file SQL (cấu trúc/dữ liệu mẫu)
└── pom.xml — cấu hình Maven / dependency dự án

│   │   │           │   ├── ☕ SecurityConfig.java — cấu hình Spring Security (quyền & auth)
│   │   │           │   ├── ☕ SiteMeshConfig.java — cấu hình SiteMesh (decorator templates)
│   │   │           │   └── ☕ WebMvcConfig.java — cấu hình MVC (resolver, resource handlers)
│   │   │           ├── 📁 controller
│   │   │           │   ├── 📁 admin
│   │   │           │   │   ├── ☕ AdminCategoryController.java — quản lý danh mục (admin)
│   │   │           │   │   └── ☕ AdminController.java — các hành động quản trị chính
│   │   │           │   ├── 📁 auth
│   │   │           │   │   ├── ☕ AuthController.java — xử lý đăng nhập/đăng ký/đăng xuất
│   │   │           │   │   └── ☕ VerifyController.java — xử lý xác thực email/OTP
│   │   │           │   ├── 📁 user
│   │   │           │   │   ├── ☕ CartController.java — quản lý giỏ hàng cho người dùng
│   │   │           │   │   └── ☕ UserController.java — trang/profile/người dùng
│   │   │           │   ├── 📁 vendor
│   │   │           │   │   ├── ☕ VendorController.java — dashboard và hành động vendor
│   │   │           │   │   ├── ☕ VendorPromotionController.java — quản lý khuyến mãi của vendor
│   │   │           │   │   └── ☕ VendorSettingsController.java — cài đặt cửa hàng vendor
│   │   │           │   ├── ☕ DecoratorController.java — controller để xử lý decorators/layouts
│   │   │           │   ├── ☕ GlobalExceptionHandler.java — bắt & xử lý ngoại lệ toàn cục
│   │   │           │   └── ☕ PageController.java — các route trang công cộng (home, index...)
│   │   │           ├── 📁 dto
│   │   │           │   ├── 📁 auth
│   │   │           │   │   └── ☕ AuthDtos.java — DTO cho auth (login/register requests/responses)
│   │   │           │   ├── 📁 user
│   │   │           │   ├── 📁 vendor
│   │   │           │   │   ├── ☕ ProductForm.java — DTO form sản phẩm (create/update)
│   │   │           │   │   ├── ☕ PromotionForm.java — DTO form khuyến mãi
│   │   │           │   │   └── ☕ ShopForm.java — DTO form cửa hàng
│   │   │           │   └── ☕ CartItem.java — DTO phần tử giỏ hàng
│   │   │           ├── 📁 entity
│   │   │           │   ├── ☕ Category.java — entity danh mục sản phẩm
│   │   │           │   ├── ☕ Customer.java — entity khách hàng
│   │   │           │   ├── ☕ Order.java — entity đơn hàng
│   │   │           │   ├── ☕ OrderDetail.java — entity chi tiết đơn hàng
│   │   │           │   ├── ☕ OrderStatus.java — enum/trạng thái đơn hàng
│   │   │           │   ├── ☕ OtpCode.java — entity mã OTP cho xác thực
│   │   │           │   ├── ☕ PaymentMethod.java — entity phương thức thanh toán
│   │   │           │   ├── ☕ Product.java — entity sản phẩm
│   │   │           │   ├── ☕ Promotion.java — entity khuyến mãi
│   │   │           │   ├── ☕ PromotionDiscountType.java — enum loại giảm giá
│   │   │           │   ├── ☕ Review.java — entity đánh giá sản phẩm
│   │   │           │   ├── ☕ Role.java — entity vai trò người dùng (ROLE_USER/ADMIN...)
│   │   │           │   ├── ☕ Shop.java — entity cửa hàng/vendor
│   │   │           │   └── ☕ User.java — entity người dùng/chủ tài khoản
│   │   │           ├── 📁 repository
│   │   │           │   ├── ☕ CategoryRepository.java — CRUD danh mục
│   │   │           │   ├── ☕ CustomerRepository.java — CRUD khách hàng
│   │   │           │   ├── ☕ OrderDetailRepository.java — CRUD chi tiết đơn
│   │   │           │   ├── ☕ OrderRepository.java — CRUD đơn hàng
│   │   │           │   ├── ☕ OtpCodeRepository.java — quản lý OTP
│   │   │           │   ├── ☕ PaymentMethodRepository.java — CRUD phương thức thanh toán
│   │   │           │   ├── ☕ PaymentRepository.java — ghi nhận thanh toán
│   │   │           │   ├── ☕ ProductRepository.java — CRUD sản phẩm / tìm kiếm
│   │   │           │   ├── ☕ PromotionRepository.java — CRUD khuyến mãi
│   │   │           │   ├── ☕ ReviewRepository.java — CRUD đánh giá
│   │   │           │   ├── ☕ RoleRepository.java — CRUD vai trò
│   │   │           │   ├── ☕ ShopRepository.java — CRUD cửa hàng
│   │   │           │   └── ☕ UserRepository.java — CRUD người dùng
│   │   │           ├── 📁 security
│   │   │           │   ├── ☕ AuthFacade.java — tiện ích lấy thông tin auth hiện tại
│   │   │           │   ├── ☕ JwtAuthenticationFilter.java — filter xử lý JWT trong request
│   │   │           │   └── ☕ JwtTokenProvider.java — sinh & xác thực JWT token
│   │   │           ├── 📁 service
│   │   │           │   ├── 📁 admin
│   │   │           │   │   ├── ☕ CategoryService.java — interface dịch vụ danh mục (admin)
│   │   │           │   │   └── ☕ UserService.java — interface dịch vụ quản trị người dùng
│   │   │           │   ├── 📁 auth
│   │   │           │   │   └── ☕ AuthService.java — logic đăng nhập/đăng ký/OTP
│   │   │           │   ├── 📁 impl
│   │   │           │   │   ├── ☕ AdminUserServiceImpl.java — triển khai UserService cho admin
│   │   │           │   │   ├── ☕ CategoryServiceImpl.java — triển khai CategoryService
│   │   │           │   │   └── ☕ UserServiceImpl.java — triển khai UserService (người dùng)
│   │   │           │   ├── 📁 user
│   │   │           │   │   └── ☕ UserService.java — interface dịch vụ người dùng (public)
│   │   │           │   └── ☕ MailService.java — gửi email (OTP, verify, thông báo)
│   │   │           └── ☕ OneshopApplication.java — lớp boot Spring Boot (main)
│   │   └── 📁 resources
│   │       ├── 📁 static
│   │       │   ├── 📁 css
│   │       │   │   └── 🎨 style.css — stylesheet chính cho giao diện
│   │       │   └── 📁 js
│   │       │       └── 📄 auth.js — JS xử lý giao diện xác thực/OTP trên client
│   │       ├── 📁 templates
│   │       │   ├── 📁 admin
│   │       │   │   ├── 📁 category
│   │       │   │   │   ├── 🌐 form.html — form tạo/sửa danh mục (admin)
│   │       │   │   │   └── 🌐 list.html — danh sách danh mục (admin)
│   │       │   │   ├── 📁 users
│   │       │   │   │   └── 🌐 user-list.html — danh sách người dùng (admin)
│   │       │   │   └── 🌐 dashboard.html — dashboard trang admin
│   │       │   ├── 📁 auth
│   │       │   │   ├── 🌐 forgot-password.html — giao diện quên mật khẩu
│   │       │   │   ├── 🌐 login.html — giao diện đăng nhập
│   │       │   │   ├── 🌐 register.html — giao diện đăng ký
│   │       │   │   ├── 🌐 reset-password.html — giao diện đặt lại mật khẩu
│   │       │   │   └── 🌐 verify.html — giao diện xác thực/nhập OTP
│   │       │   ├── 📁 decorators
│   │       │   │   ├── 🌐 admin-layout.html — layout chung cho admin
│   │       │   │   ├── 🌐 main.html — layout trang chính
│   │       │   │   └── 🌐 vendor-layout.html — layout cho vendor
│   │       │   ├── 📁 fragments
│   │       │   │   ├── 🌐 admin-footer.html — fragment footer admin
│   │       │   │   ├── 🌐 admin-header.html — fragment header admin
│   │       │   │   ├── 🌐 footer.html — fragment footer chung
│   │       │   │   └── 🌐 header.html — fragment header chung
│   │       │   ├── 📁 user
│   │       │   │   ├── 🌐 cart.html — giao diện giỏ hàng người dùng
│   │       │   │   ├── 🌐 order_history.html — lịch sử đơn của user
│   │       │   │   └── 🌐 profile.html — trang profile người dùng
│   │       │   ├── 📁 vendor
│   │       │   │   ├── 🌐 dashboard.html — dashboard vendor
│   │       │   │   ├── 🌐 home.html — trang chủ vendor
│   │       │   │   ├── 🌐 order-detail.html — chi tiết đơn hàng (vendor)
│   │       │   │   ├── 🌐 orders.html — danh sách đơn (vendor)
│   │       │   │   ├── 🌐 product-detail.html — chi tiết sản phẩm
│   │       │   │   ├── 🌐 product-form.html — form tạo/sửa sản phẩm
│   │       │   │   ├── 🌐 product.html — danh sách sản phẩm vendor
│   │       │   │   ├── 🌐 profile.html — trang profile vendor
│   │       │   │   ├── 🌐 promotion-form.html — form khuyến mãi
│   │       │   │   ├── 🌐 promotion.html — danh sách khuyến mãi
│   │       │   │   ├── 🌐 revenue.html — báo cáo doanh thu
│   │       │   │   └── 🌐 settings.html — cài đặt cửa hàng vendor
│   │       │   ├── 🌐 error.html — trang lỗi chung
│   │       │   ├── 🌐 home.html — trang chủ public
│   │       │   ├── 🌐 index.html — landing/index
│   │       │   └── 🌐 test.html — trang thử nghiệm
│   │       └── 📄 application.properties — cấu hình ứng dụng
│   └── 📁 test
│       └── 📁 java
│           └── 📁 com
│               └── 📁 oneshop
│                   └── ☕ OneshopApplicationTests.java — test khởi động ứng dụng (Spring Boot test)
├── 📁 uploads
│   ├── 📁 products
│   │   ├── 🖼️ 4310bbc3-cc49-427d-b442-3164197e2dc5.png
│   │   └── 🖼️ c32e0fe0-0fde-44e6-b5c5-6477a92b7d3e.png
│   └── 📁 shops
│       ├── 🖼️ 4133998c-4095-4200-a876-9a79c0791630.jpg
│       ├── 🖼️ 92937c74-41a7-41bd-a9d9-d1bbd0e96da2.jpg
│       ├── 🖼️ b836e4bb-814d-468d-8ab6-b79ae76a9870.jpg
│       └── 🖼️ bdd98a66-f2ad-4dff-8179-6bf1a22b60e6.jpg
├── ⚙️ .gitattributes
├── ⚙️ .gitignore
├── 📄 mvnw — script wrapper cho Maven (Unix)
├── 📄 mvnw.cmd — script wrapper cho Maven (Windows)
├── 📄 oneshop.sql — file SQL (cấu trúc/dữ liệu mẫu)
└── ⚙️ pom.xml — cấu hình Maven / dependency dự án
```

---

_Generated by FileTree Pro Extension (annotated)_

````
