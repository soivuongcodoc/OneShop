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

async function logout() {
  try {
    // Gọi server để xóa HttpOnly cookie JWT
    await fetch("/api/auth/logout", { method: "POST" });
  } catch (e) {
    // ignore network error and proceed client-side cleanup
  }
  localStorage.removeItem("jwtToken");
  localStorage.removeItem("username");
  // Xóa cookie do client set (nếu còn)
  document.cookie = "JWT=; Max-Age=0; Path=/";
  window.location.href = "/login";
}
