package com.hhs.codeboard.blog.data.repository;

import com.hhs.codeboard.blog.data.entity.menu.entity.MenuEntity;
import com.hhs.codeboard.blog.enumeration.YN;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenuDAO extends JpaRepository<MenuEntity, Long> {

//    Optional<MenuEntity> findByUuidAndRegUserSeqAndDelDateIsNull(String uuid, Integer regUserSeq);

    List<MenuEntity> findAllByRegUserSeqAndDelDateIsNull(Long regUserSeq, Sort sort);

    List<MenuEntity> findAllByRegUserSeqAndDelDateIsNull(Long regUserSeq);

    List<MenuEntity> findAllByRegUserSeqAndMenuTypeAndDelDateIsNull(Long regUserSeq, String menuType);

    List<MenuEntity> findAllByParentSeqAndDelDateIsNull(Long parentSeq);

}
