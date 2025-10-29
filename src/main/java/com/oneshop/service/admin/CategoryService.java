package com.oneshop.service.admin;

import com.oneshop.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    void delete(Long id);
    List<Category> searchCategories(String keyword);
}
