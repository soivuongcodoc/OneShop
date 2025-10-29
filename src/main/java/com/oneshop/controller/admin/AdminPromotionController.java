package com.oneshop.controller.admin;

import com.oneshop.dto.admin.AdminPromotionForm;
import com.oneshop.entity.AdminPromotion;
import com.oneshop.entity.AdminPromotionDiscountType;
import com.oneshop.repository.AdminPromotionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/promotions")
@RequiredArgsConstructor
public class AdminPromotionController {

    private final AdminPromotionRepository repo;

    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<AdminPromotion> promotions = (keyword == null || keyword.isBlank())
                ? repo.findAllByOrderByIdDesc()
                : repo.searchByName(keyword.trim());
        model.addAttribute("promotions", promotions);
        model.addAttribute("keyword", keyword);
        return "admin/promotion/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("mode", "create");
        model.addAttribute("form", new AdminPromotionForm());
        model.addAttribute("types", AdminPromotionDiscountType.values());
        return "admin/promotion/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") AdminPromotionForm form,
                         BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("mode", "create");
            model.addAttribute("types", AdminPromotionDiscountType.values());
            return "admin/promotion/form";
        }
        AdminPromotion p = AdminPromotion.builder()
                .name(form.getName())
                .description(form.getDescription())
                .discountType(form.getDiscountType())
                .discountValue(form.getDiscountValue())
                .startTime(form.getStartTime())
                .endTime(form.getEndTime())
                .active(Boolean.TRUE.equals(form.getActive()))
                .build();
        repo.save(p);
        return "redirect:/admin/promotions";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        AdminPromotion p = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi"));
        AdminPromotionForm form = new AdminPromotionForm();
        form.setName(p.getName());
        form.setDescription(p.getDescription());
        form.setDiscountType(p.getDiscountType());
        form.setDiscountValue(p.getDiscountValue());
        form.setStartTime(p.getStartTime());
        form.setEndTime(p.getEndTime());
        form.setActive(Boolean.TRUE.equals(p.getActive()));

        model.addAttribute("mode", "edit");
        model.addAttribute("promotionId", id);
        model.addAttribute("form", form);
        model.addAttribute("types", AdminPromotionDiscountType.values());
        return "admin/promotion/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") AdminPromotionForm form,
                         BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("promotionId", id);
            model.addAttribute("types", AdminPromotionDiscountType.values());
            return "admin/promotion/form";
        }
        AdminPromotion p = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi"));
        p.setName(form.getName());
        p.setDescription(form.getDescription());
        p.setDiscountType(form.getDiscountType());
        p.setDiscountValue(form.getDiscountValue());
        p.setStartTime(form.getStartTime());
        p.setEndTime(form.getEndTime());
        p.setActive(Boolean.TRUE.equals(form.getActive()));
        repo.save(p);
        return "redirect:/admin/promotions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        AdminPromotion p = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi"));
        repo.delete(p);
        return "redirect:/admin/promotions";
    }
}
