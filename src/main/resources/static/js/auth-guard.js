// auth-guard.js - Bảo vệ các trang yêu cầu đăng nhập

(function() {
    'use strict';
    
    // Lấy path hiện tại
    const currentPath = window.location.pathname;
    
    // Các path yêu cầu authentication
    const protectedPaths = ['/admin', '/user', '/vendor'];
    
    // Kiểm tra xem path hiện tại có thuộc protected không
    const isProtectedPath = protectedPaths.some(path => currentPath.startsWith(path));
    
    if (isProtectedPath) {
        // Kiểm tra token trong localStorage
        const token = localStorage.getItem('jwtToken');
        const username = localStorage.getItem('username');
        const role = localStorage.getItem('role');
        
        if (!token || !username || !role) {
            // Không có token -> redirect về login
            console.warn('No authentication found, redirecting to login...');
            localStorage.clear();
            sessionStorage.clear();
            window.location.href = '/login';
            return;
        }
        
        // Kiểm tra role phù hợp với path
        if (currentPath.startsWith('/admin') && role !== 'ROLE_ADMIN') {
            console.warn('Unauthorized access to admin area');
            window.location.href = '/login';
            return;
        }
        
        if (currentPath.startsWith('/vendor') && role !== 'ROLE_VENDOR' && role !== 'ROLE_ADMIN') {
            console.warn('Unauthorized access to vendor area');
            window.location.href = '/login';
            return;
        }
        
        if (currentPath.startsWith('/user') && !['ROLE_USER', 'ROLE_VENDOR', 'ROLE_ADMIN'].includes(role)) {
            console.warn('Unauthorized access to user area');
            window.location.href = '/login';
            return;
        }
        
        // Verify token với server (async)
        verifyTokenWithServer(token);
    }
    
    async function verifyTokenWithServer(token) {
        try {
            const response = await fetch('/api/auth/verify-token', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            });
            
            if (!response.ok) {
                // Token không hợp lệ
                console.warn('Token verification failed, logging out...');
                await logout();
            }
        } catch (error) {
            console.error('Error verifying token:', error);
            // Nếu có lỗi network, không force logout ngay (để offline vẫn dùng được)
        }
    }
    
    async function logout() {
        try {
            await fetch('/api/auth/logout', { 
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                }
            });
        } catch (e) {
            console.error('Logout API error:', e);
        }
        
        localStorage.clear();
        sessionStorage.clear();
        
        // Xóa tất cả cookies
        document.cookie.split(";").forEach(function(c) { 
            document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/"); 
        });
        
        window.location.href = '/login';
    }
})();
