package com.hhs.codeboard.blog.web.service.category;

import com.hhs.codeboard.blog.data.entity.board.dto.BoardCategoryDto;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleEntity;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardCategoryEntity;
import com.hhs.codeboard.blog.data.entity.menu.entity.MenuEntity;
import com.hhs.codeboard.blog.data.repository.ArticleDAO;
import com.hhs.codeboard.blog.data.repository.CategoryDAO;
import com.hhs.codeboard.blog.data.repository.MemberDAO;
import com.hhs.codeboard.blog.data.repository.MenuDAO;
import com.hhs.codeboard.blog.web.service.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDAO categoryDAO;
    private final MemberDAO memberDAO;
    private final MenuDAO menuDAO;
    private final ArticleDAO articleDAO;

    public int insertCategory(BoardCategoryDto cateVO, MemberDto memberDto) {
        /*
            먼저 해당 게시판에대해 유저의 권한을 확인한다.
         */
        MenuEntity targetBoard = menuDAO.findBySeqAndRegUserSeqAndDelDateIsNull(cateVO.getBoardSeq(), memberDto.getSeq())
                .orElseThrow(()-> new ServiceException("잘못된 접근입니다."));

        BoardCategoryEntity insertVO = new BoardCategoryEntity();
        insertVO.setBoardSeq(targetBoard.getSeq());
        insertVO.setTitle(cateVO.getTitle());
        insertVO.setRegDate(LocalDateTime.now());
        insertVO.setRegUserSeq(memberDto.getSeq());

        categoryDAO.save(insertVO);
        return 1;
    }

    public int deleteCategory(BoardCategoryDto cateVO, MemberDto memberDto) {
        /*
            카테고리의 삭제. 해당 카테고리를 삭제하며, delDate를 업데이트한다.
            해당 카테고리 번호를 가진 boardArticle역시 모두 null로 초기화한다.
         */
        MenuEntity targetBoard = menuDAO.findBySeqAndRegUserSeqAndDelDateIsNull(cateVO.getBoardSeq(), memberDto.getSeq())
                .orElseThrow(()-> new ServiceException("잘못된 접근입니다."));

        Integer delCateSeq = cateVO.getSeq();

        BoardCategoryEntity deleteEntity = categoryDAO.findBySeqAndDelDateIsNotNull(delCateSeq)
                .orElseThrow(() -> new ServiceException("잘못된 접근입니다."));

        deleteEntity.setDelDate(LocalDateTime.now());
        categoryDAO.save(deleteEntity);

        //해당 카테고리를 가진 boardArticle을 일괄 null 업데이트할 필요가 있다.
        List<BoardArticleEntity> articleList = articleDAO.findAllByCategorySeq(delCateSeq);
        articleList.forEach(entity->entity.setCategorySeq(null));
        articleDAO.saveAll(articleList);

        return articleList.size();
    }

}
