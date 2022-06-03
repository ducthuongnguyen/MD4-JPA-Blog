package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    IBlogService blogService;

    @GetMapping()
    public String showList(Model model) {
        List<Blog> blogs = blogService.findAll();
        model.addAttribute("blogs", blogs);
        return "/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("blog",new Blog());
        return "/create";
    }

    @PostMapping("/save")
    public String save(Blog blog, Model model) {
        blogService.save(blog);
        model.addAttribute("blog", blog);
        return "redirect:/blog";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model){
        Blog blog =  blogService.findById(id);
        model.addAttribute("blog",blog);
        return "/edit";
    }

    @PostMapping("/edit")
    public String edit(Blog blog){
        blogService.save(blog);
        return "redirect:/blog";
    }

    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model){
        Blog blog =  blogService.findById(id);
        model.addAttribute("blog",blog);
        return "/delete";
    }

    @PostMapping("/delete")
    public String remove(Long id){
        blogService.remove(id);
        return "redirect:/blog";
    }
}
