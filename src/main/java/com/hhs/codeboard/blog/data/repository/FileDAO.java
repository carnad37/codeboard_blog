package com.hhs.codeboard.blog.data.repository;

import com.hhs.codeboard.blog.data.entity.common.entity.FileEntity;
import com.hhs.codeboard.blog.data.entity.menu.entity.MenuEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileDAO extends JpaRepository<FileEntity, Long> {

}
