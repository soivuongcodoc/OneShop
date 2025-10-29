document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("loginForm");
  const msgEl = document.getElementById("message");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const body = {
      usernameOrEmail: document.getElementById("username").value.trim(),
      password: document.getElementById("password").value
    };

    msgEl.classList.remove("text-danger", "text-success");
    msgEl.textContent = "⏳ Đang đăng nhập...";

    try {
      const res = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "Sai tên đăng nhập hoặc mật khẩu!");
      }

      const data = await res.json();
      console.log("LOGIN RESPONSE:", data);

      // lưu
      localStorage.setItem("jwtToken", data.token);
      localStorage.setItem("username", data.username);
      localStorage.setItem("role", data.role);

      // cookie cho Spring Security đọc
      document.cookie = `jwtToken=${data.token}; Path=/; SameSite=Lax`;

      msgEl.classList.add("text-success");
      msgEl.textContent = "✅ Đăng nhập thành công! Đang chuyển hướng...";

	  // ✅ Đợi 500ms cho cookie được ghi
	  setTimeout(() => {
	    const role = localStorage.getItem("role");
	    let targetUrl = "/home";
	    if (role === "ROLE_ADMIN") targetUrl = "/admin/dashboard";
	    else if (role === "ROLE_VENDOR") targetUrl = "/vendor/home";

	    window.location.href = targetUrl;
	  }, 1000);

    } catch (err) {
      console.error("LOGIN ERROR:", err);
      msgEl.classList.add("text-danger");
      msgEl.textContent = "❌ " + err.message;
    }
  });
});