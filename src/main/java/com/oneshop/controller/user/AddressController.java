package com.oneshop.controller.user;

import com.oneshop.entity.Address;
import com.oneshop.service.user.UserService;
import com.oneshop.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/addresses")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public String list(Model model) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("addresses", addressRepository.findByUserId(current.getId()));
        return "user/addresses";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         @RequestParam String address,
                         @RequestParam(required = false, defaultValue = "false") Boolean isDefault) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }

        // Nếu địa chỉ mới là mặc định, bỏ default của địa chỉ cũ
        if (isDefault) {
            addressRepository.findByUserId(current.getId()).forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        Address a = Address.builder()
                .userId(current.getId())
                .name(name)
                .address(address)
                .active(true)
                .isDefault(isDefault)
                .build();
        addressRepository.save(a);
        return "redirect:/user/addresses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }
        addressRepository.findById(id).ifPresent(a -> {
            if (a.getUserId().equals(current.getId())) {
                addressRepository.delete(a);
            }
        });
        return "redirect:/user/addresses";
    }

    @PostMapping("/{id}/set-default")
    public String setDefault(@PathVariable Long id) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }

        // Bỏ default của tất cả địa chỉ
        addressRepository.findByUserId(current.getId()).forEach(addr -> {
            addr.setIsDefault(false);
            addressRepository.save(addr);
        });

        // Set địa chỉ này làm mặc định
        addressRepository.findById(id).ifPresent(a -> {
            if (a.getUserId().equals(current.getId())) {
                a.setIsDefault(true);
                addressRepository.save(a);
            }
        });

        return "redirect:/user/addresses";
    }
}
