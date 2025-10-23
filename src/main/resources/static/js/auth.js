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
  window.location.href = "/login";
}
