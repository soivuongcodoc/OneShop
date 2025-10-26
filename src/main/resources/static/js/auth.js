// /static/js/auth.js

// Ghi đè window.fetch để tự động gắn Authorization header cho mọi request sau khi đăng nhập
const originalFetch = window.fetch;
window.fetch = async (url, options = {}) => {
  const token = localStorage.getItem("jwtToken");

  options.headers = options.headers || {};

  // nếu chưa có header Authorization thì mình gắn vào
  if (token && !options.headers["Authorization"]) {
    options.headers["Authorization"] = `Bearer ${token}`;
  }

  return originalFetch(url, options);
};

// tiện ích
function logout() {
  localStorage.removeItem("jwtToken");
  localStorage.removeItem("username");
  localStorage.removeItem("role");
  window.location.href = "/login";
}

// ai đăng nhập chưa
function getCurrentUser() {
  return {
    username: localStorage.getItem("username"),
    role: localStorage.getItem("role"),
    token: localStorage.getItem("jwtToken")
  };
}