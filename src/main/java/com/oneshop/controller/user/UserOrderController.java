package com.oneshop.controller.user;

import com.oneshop.entity.Order;
import com.oneshop.entity.OrderDetail;
import com.oneshop.entity.OrderStatus;
import com.oneshop.entity.User;
import com.oneshop.repository.OrderDetailRepository;
import com.oneshop.repository.OrderRepository;
import com.oneshop.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user/orders")
@RequiredArgsConstructor
public class UserOrderController {
    
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserService userService;

    @GetMapping
    public String listOrders(Model model) {
        User currentUser = userService.getCurrentUser();
        List<Order> orders = orderRepository.findByCustomer_User_IdOrderByOrderDateDesc(currentUser.getId());
        
        model.addAttribute("orders", orders);
        return "user/order_history";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        
        Order order = orderRepository.findById(id).orElse(null);
        
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại!");
            return "redirect:/user/orders";
        }
        
        // Check if order belongs to current user
        if (!order.getCustomer().getUser().getId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền xem đơn hàng này!");
            return "redirect:/user/orders";
        }
        
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(id);
        
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        
        return "user/order_detail";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        
        Order order = orderRepository.findById(id).orElse(null);
        
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại!");
            return "redirect:/user/orders";
        }
        
        // Check if order belongs to current user
        if (!order.getCustomer().getUser().getId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền hủy đơn hàng này!");
            return "redirect:/user/orders";
        }
        
        // Only allow cancel if order is PENDING
        if (order.getStatus() != OrderStatus.PENDING) {
            redirectAttributes.addFlashAttribute("error", "Chỉ có thể hủy đơn hàng đang chờ xác nhận!");
            return "redirect:/user/orders/" + id;
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        
        redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng thành công!");
        return "redirect:/user/orders/" + id;
    }
}
