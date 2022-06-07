package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.Category;
import com.codegym.service.IBlogService;
import com.codegym.service.ICategoryService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;


@Controller
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    IBlogService blogService;
    @Autowired
    ICategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping()
    public String showList(Model model, @PageableDefault(value = 10) Pageable pageable, @RequestParam Optional<String> search) {
        Page<Blog> blogs;
        if (search.isPresent()) {
            blogs = blogService.findAllByNameContaining(search.get(), pageable);
        } else {
            blogs = blogService.findAll(pageable);
        }
        model.addAttribute("blogs", blogs);
        return "/blog/list";
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/blog/create");
        modelAndView.addObject("blog", new Blog());
        return modelAndView;
    }


    @PostMapping("/save")
    public ModelAndView save(@Valid Blog blog, BindingResult bindingResult) {
        new Blog().validate(blog,bindingResult);
        ModelAndView modelAndView;
        if (bindingResult.hasErrors()) {
            modelAndView = new ModelAndView("/blog/create");
            return modelAndView;
        }
        modelAndView = new ModelAndView("/blog/create");
        blog.setCreatedDate(LocalDateTime.now());
        blogService.save(blog);
        modelAndView.addObject("blog", new Blog());
        modelAndView.addObject("mess", "New blog was created!!!");
        return modelAndView;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Optional<Blog> blog = blogService.findById(id);
        if (blog.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/blog/edit");
            modelAndView.addObject("blog", blog.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }

    }

    @PostMapping("/edit")
    public ModelAndView updateBlog(Blog blog) {
        ModelAndView modelAndView = new ModelAndView("/blog/edit");
        blogService.save(blog);
        modelAndView.addObject("blog", blog);
        modelAndView.addObject("mess", "Update blog successfully!!!");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<Blog> blog = blogService.findById(id);
        if (blog.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/blog/delete");
            modelAndView.addObject("blog", blog.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }

    }

    @PostMapping("/delete")
    public ModelAndView remove(Blog blog) {
        ModelAndView modelAndView = new ModelAndView("redirect:/blogs");
        blogService.remove(blog.getId());
        return modelAndView;
    }

    @GetMapping("/sortByDate")
    public ModelAndView sortByCreatedDate(@PageableDefault(value = 4) Pageable pageable, Blog blog) {
        Page<Blog> sortList = blogService.findAllByOrderByCreatedDate(pageable);
        ModelAndView modelAndView = new ModelAndView("/blog/list");
        modelAndView.addObject("blogs", sortList);
        return modelAndView;
    }
}
