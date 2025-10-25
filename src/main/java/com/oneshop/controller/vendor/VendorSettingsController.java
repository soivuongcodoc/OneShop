package com.oneshop.controller.vendor;

import com.oneshop.config.WebMvcConfig;
import com.oneshop.dto.vendor.ShopForm;
import com.oneshop.entity.Shop;
import com.oneshop.repository.ShopRepository;
import com.oneshop.repository.UserRepository;
import com.oneshop.security.AuthFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class VendorSettingsController {
    private final ShopRepository shopRepo;
    private final UserRepository userRepo;
    private final AuthFacade auth;

    @GetMapping("/vendor/settings")
    public String settings(Model model) {
        Long shopId = auth.requireUserId();
        var shop = shopRepo.findById(shopId).orElse(null);
        ShopForm form = new ShopForm();
        if (shop != null) {
            form.setName(shop.getName());
            form.setDescription(shop.getDescription());
            form.setAddress(shop.getAddress());
            form.setPhone(shop.getPhone());
            form.setCurrentLogo(shop.getLogoUrl());
        }
        model.addAttribute("pageTitle", "Cài đặt cửa hàng - OneShop Vendor");
        model.addAttribute("activePage", "settings");
        model.addAttribute("form", form);
        return "vendor/settings";
    }

    @PostMapping("/vendor/settings")
    public String update(@Valid @ModelAttribute("form") ShopForm form, BindingResult binding, Model model) throws Exception {
        if (binding.hasErrors()) {
            model.addAttribute("pageTitle", "Cài đặt cửa hàng - OneShop Vendor");
            model.addAttribute("activePage", "settings");
            return "vendor/settings";
        }
        Long shopId = auth.requireUserId();
        var user = userRepo.findById(shopId).orElseThrow();
        var shop = shopRepo.findById(shopId).orElse(null);
        
        if (shop == null) {
            // Create new shop with proper @MapsId setup
            shop = new Shop();
            shop.setVendor(user);
            shop.setCreatedAt(LocalDateTime.now());
        }
        
        shop.setName(form.getName());
        shop.setDescription(form.getDescription());
        shop.setAddress(form.getAddress());
        shop.setPhone(form.getPhone());
        shop.setUpdatedAt(LocalDateTime.now());

        MultipartFile logo = form.getLogo();
        if (logo != null && !logo.isEmpty()) {
            Files.createDirectories(WebMvcConfig.SHOP_UPLOAD_DIR);
            String original = logo.getOriginalFilename();
            String ext = (original != null && original.contains(".")) ? original.substring(original.lastIndexOf('.')) : "";
            String filename = UUID.randomUUID() + ext;
            Path target = WebMvcConfig.SHOP_UPLOAD_DIR.resolve(filename);
            logo.transferTo(target.toFile());
            // delete old
            if (shop.getLogoUrl() != null && shop.getLogoUrl().startsWith("/images/shops/")) {
                String old = shop.getLogoUrl().substring("/images/shops/".length());
                try { Files.deleteIfExists(WebMvcConfig.SHOP_UPLOAD_DIR.resolve(old)); } catch (Exception ignored) {}
            }
            shop.setLogoUrl("/images/shops/" + filename);
        }

        shopRepo.save(shop);
        return "redirect:/vendor/settings";
    }
}
