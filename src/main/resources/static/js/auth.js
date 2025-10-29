// // ⚙️ Tự động thêm JWT vào tất cả fetch requests
// const originalFetch = window.fetch;
// window.fetch = async (url, options = {}) => {
//   const token = localStorage.getItem("jwtToken");
//   options.headers = options.headers || {};
//   if (token) {
//     options.headers["Authorization"] = `Bearer ${token}`;
//   }
//   return originalFetch(url, options);
// };

// // ⚙️ Hàm kiểm tra đăng nhập
// function getCurrentUser() {
//   return localStorage.getItem("username");
// }

// async function logout() {
//   try {
//     // Gọi server để xóa HttpOnly cookie JWT
//     await fetch("/api/auth/logout", { method: "POST" });
//   } catch (e) {
//     // ignore network error and proceed client-side cleanup
//   }
//   localStorage.removeItem("jwtToken");
//   localStorage.removeItem("username");
//   // Xóa cookie do client set (nếu còn)
//   document.cookie = "JWT=; Max-Age=0; Path=/";
//   window.location.href = "/login";
// }
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