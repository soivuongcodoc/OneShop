package com.oneshop.controller.admin;

import com.oneshop.repository.OrderRepository;
import com.oneshop.repository.AdminProductRepository;
import com.oneshop.repository.UserRepository;
import com.oneshop.service.admin.UserService;
import com.oneshop.entity.User;
import com.oneshop.dto.admin.UserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    private final AdminProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;

    @GetMapping({"/dashboard", ""})
    public String dashboard(Model model) {
        long userCount = userRepository.count();
        long productCount = productRepository.count();
        long orderCount = orderRepository.count();
        BigDecimal revenue = orderRepository.findAll().stream()
                .map(o -> o.getTotalAmount() == null ? BigDecimal.ZERO : o.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("userCount", userCount);
        model.addAttribute("productCount", productCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("revenue", revenue);
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("users", userService.searchUsers(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/users/user-list";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        try {
            userService.deleteUser(id);
            ra.addFlashAttribute("success", "Đã xóa người dùng.");
        } catch (DataIntegrityViolationException ex) {
            // Nếu có ràng buộc khóa ngoại, chuyển sang vô hiệu hóa thay vì xóa cứng
            userService.deactivateUser(id);
            ra.addFlashAttribute("warning", "Không thể xóa do ràng buộc dữ liệu. Đã chuyển sang vô hiệu hóa tài khoản.");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Xóa thất bại: " + ex.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        model.addAttribute("form", new UserForm());
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("pageTitle", "Thêm người dùng - Admin");
        model.addAttribute("mode", "create");
        model.addAttribute("userId", null);
        return "admin/users/user-form";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("form") UserForm form) {
        userService.createUser(form);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        UserForm form = new UserForm();
        form.setId(user.getId());
        form.setUsername(user.getUsername());
        form.setEmail(user.getEmail());
        // map roles (assuming User.getRoles() returns collection of Role with getName())
        if (user.getRoles() != null) {
            form.setRoles(user.getRoles().stream().map(r -> r.getName()).toList());
        }
        model.addAttribute("form", form);
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("pageTitle", "Sửa người dùng - Admin");
        model.addAttribute("mode", "edit");
        model.addAttribute("userId", id);
        return "admin/users/user-form";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("form") UserForm form) {
        userService.updateUser(id, form);
        return "redirect:/admin/users";
    }
}
