// ⚙️ Tự động thêm JWT vào tất cả fetch requests
const originalFetch = window.fetch;
window.fetch = async (url, options = {}) => {
  const token = localStorage.getItem("jwtToken");
  options.headers = options.headers || {};
  if (token) {
    options.headers["Authorization"] = `Bearer ${token}`;
  }
  return originalFetch(url, options);
};

// ⚙️ Hàm kiểm tra đăng nhập
function getCurrentUser() {
  return localStorage.getItem("username");
}

function logout() {
  localStorage.removeItem("jwtToken");
  localStorage.removeItem("username");
  // Xóa cookie JWT để kết thúc phiên trên server cho các request SSR
  document.cookie = "JWT=; Max-Age=0; Path=/";
  window.location.href = "/login";
}
