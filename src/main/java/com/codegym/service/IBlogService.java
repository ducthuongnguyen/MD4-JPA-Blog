package com.codegym.service;

import com.codegym.model.Blog;
import com.codegym.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IBlogService extends IGeneralBlog<Blog> {
    Page<Blog> findAll(Pageable pageable);
    Page<Blog> findAllByNameContaining(String name, Pageable pageable);
    Iterable<Blog> findAllByCategory(Category category);
    Page<Blog> findAllByOrderByCreatedDate( Pageable pageable);
    Iterable<Blog> findAllByNameContaining(String name);
}
