package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.Category;
import com.codegym.service.IBlogService;
import com.codegym.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @Autowired
    IBlogService blogService;

    @GetMapping()
    public String showListProvince(Model model) {
        Iterable<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "/category/list";
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/category/create");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }


    @PostMapping("/save")
    public ModelAndView save(Category category) {
        ModelAndView modelAndView = new ModelAndView("/category/create");
        categoryService.save(category);
        modelAndView.addObject("blog", new Category());
        modelAndView.addObject("mess", "New category was created!!!");
        return modelAndView;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/category/edit");
            modelAndView.addObject("category", category.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }

    }

    @PostMapping("/edit")
    public ModelAndView updateBlog(Category category) {
        ModelAndView modelAndView = new ModelAndView("/category/edit");
        categoryService.save(category);
        modelAndView.addObject("category", category);
        modelAndView.addObject("mess", "Update category successfully!!!");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/category/delete");
            modelAndView.addObject("category", category.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }

    }

    @PostMapping("/delete")
    public ModelAndView remove(Category category) {
        ModelAndView modelAndView = new ModelAndView("redirect:/categories");
        categoryService.remove(category.getId());
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewCategory(@PageableDefault(value = 3) Pageable pageable, @PathVariable Long id) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()) {
            return new ModelAndView("/error.404");
        }
        Page<Blog> blogs = blogService.findAllByCategory(categoryOptional.get(), pageable);
        ModelAndView modelAndView = new ModelAndView("/category/view");
        modelAndView.addObject("blogs", blogs);
        modelAndView.addObject("category", categoryOptional.get());
        return modelAndView;
    }
}
