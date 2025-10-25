package com.oneshop.controller.vendor;

import com.oneshop.dto.vendor.PromotionForm;
import com.oneshop.entity.Promotion;
import com.oneshop.repository.PromotionRepository;
import com.oneshop.security.AuthFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vendor/promotions")
@RequiredArgsConstructor
public class VendorPromotionController {
    private final PromotionRepository promoRepo;
    private final AuthFacade auth;

    @GetMapping
    public String list(Model model) {
        Long shopId = auth.requireUserId();
        var promos = promoRepo.findByShopIdOrderByStartTimeDesc(shopId);
        model.addAttribute("pageTitle", "Chương trình khuyến mãi - OneShop Vendor");
        model.addAttribute("activePage", "promotions");
        model.addAttribute("promotions", promos);
        return "vendor/promotion";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Tạo khuyến mãi - OneShop Vendor");
        model.addAttribute("activePage", "promotions");
        model.addAttribute("form", new PromotionForm());
        model.addAttribute("mode", "create");
        return "vendor/promotion-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") PromotionForm form, BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("mode", "create");
            return "vendor/promotion-form";
        }
        Long shopId = auth.requireUserId();
        Promotion p = Promotion.builder()
                .shopId(shopId)
                .name(form.getName())
                .description(form.getDescription())
                .discountType(form.getDiscountType())
                .discountValue(form.getDiscountValue())
                .startTime(form.getStartTime())
                .endTime(form.getEndTime())
                .active(Boolean.TRUE.equals(form.getActive()))
                .build();
        promoRepo.save(p);
        return "redirect:/vendor/promotions";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Long shopId = auth.requireUserId();
        var p = promoRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi"));
        if (!p.getShopId().equals(shopId)) throw new RuntimeException("Không có quyền");
        PromotionForm form = new PromotionForm();
        form.setName(p.getName());
        form.setDescription(p.getDescription());
        form.setDiscountType(p.getDiscountType());
        form.setDiscountValue(p.getDiscountValue());
        form.setStartTime(p.getStartTime());
        form.setEndTime(p.getEndTime());
        form.setActive(p.isActive());

        model.addAttribute("pageTitle", "Sửa khuyến mãi - OneShop Vendor");
        model.addAttribute("activePage", "promotions");
        model.addAttribute("form", form);
        model.addAttribute("mode", "edit");
        model.addAttribute("promotionId", id);
        return "vendor/promotion-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") PromotionForm form,
                         BindingResult binding, Model model) {
        Long shopId = auth.requireUserId();
        var p = promoRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi"));
        if (!p.getShopId().equals(shopId)) throw new RuntimeException("Không có quyền");
        if (binding.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("promotionId", id);
            return "vendor/promotion-form";
        }
        p.setName(form.getName());
        p.setDescription(form.getDescription());
        p.setDiscountType(form.getDiscountType());
        p.setDiscountValue(form.getDiscountValue());
        p.setStartTime(form.getStartTime());
        p.setEndTime(form.getEndTime());
        p.setActive(form.getActive() != null && form.getActive());
        promoRepo.save(p);
        return "redirect:/vendor/promotions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        Long shopId = auth.requireUserId();
        var p = promoRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi"));
        if (!p.getShopId().equals(shopId)) throw new RuntimeException("Không có quyền");
        promoRepo.delete(p);
        return "redirect:/vendor/promotions";
    }
}
