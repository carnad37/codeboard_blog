package com.hhs.codeboard.blog.jpa.repository;

import com.hhs.codeboard.blog.jpa.entity.menu.entity.MenuEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuDAO extends JpaRepository<MenuEntity, Long> {

//    Optional<MenuEntity> findByUuidAndRegUserSeqAndDelDateIsNull(String uuid, Integer regUserSeq);

    Optional<MenuEntity> findBySeqAndRegUserSeqAndDelDateIsNull(Integer seq, Integer regUserSeq);

//    Optional<MenuEntity> findByUuidAndRegUserSeqAndMenuTypeAndDelDateIsNull(String uuid, Integer regUserSeq, String menuType);

    List<MenuEntity> findAllByRegUserSeqAndDelDateIsNull(Integer regUserSeq, Sort sort);

    List<MenuEntity> findAllByRegUserSeqAndDelDateIsNull(Integer regUserSeq);

    List<MenuEntity> findAllByRegUserSeqAndMenuTypeAndDelDateIsNull(Integer regUserSeq, String menuType);

    List<MenuEntity> findAllByParentSeqAndDelDateIsNull(Integer parentSeq);

}
