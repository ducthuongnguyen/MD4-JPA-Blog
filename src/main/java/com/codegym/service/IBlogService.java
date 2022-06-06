package com.codegym.service;

import com.codegym.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBlogService extends IGeneralBlog<Blog> {
    Page<Blog> findAll(Pageable pageable);
}
