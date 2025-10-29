package com.oneshop.controller.user;

import com.oneshop.entity.OrderStatus;
import com.oneshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/vnpay/create")
    public String vnpayCreate(@RequestParam Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "payment/vnpay";
    }

    @GetMapping("/vnpay/return")
    public String vnpayReturn(@RequestParam Long orderId, @RequestParam(required = false) String vnp_ResponseCode) {
        var orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            var order = orderOpt.get();
            if ("00".equals(vnp_ResponseCode)) {
                order.setStatus(OrderStatus.CONFIRMED);
            } else {
                order.setStatus(OrderStatus.CANCELLED);
            }
            orderRepository.save(order);
        }
        return "redirect:/user/orders";
    }

    @PostMapping("/vnpay/ipn")
    @ResponseBody
    public String vnpayIpn(@RequestParam Long orderId, @RequestParam(required = false) String vnp_ResponseCode) {
        var orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            var order = orderOpt.get();
            if ("00".equals(vnp_ResponseCode)) {
                order.setStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);
            }
        }
        return "OK";
    }
}
