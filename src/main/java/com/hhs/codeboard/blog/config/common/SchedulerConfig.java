package com.hhs.codeboard.blog.config.common;

import com.hhs.codeboard.blog.data.entity.common.entity.FileEntity;
import com.hhs.codeboard.blog.data.entity.common.entity.QFileEntity;
import com.hhs.codeboard.blog.data.repository.FileDAO;
import com.hhs.codeboard.blog.web.service.common.FileService;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class SchedulerConfig {

    private QFileEntity file = QFileEntity.fileEntity;

    private final JPAQueryFactory jpaQueryFactory;
    private final FileService fileService;
    private final FileDAO fileDAO;


    /**
     * 한시간마다 사용되지 않은 이미지 삭제.
     */
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void notUseImageRemove() {
        // 현재 시간 기준으로 업로드된지 1시간이상 지난 이미지들 조회
        List<FileEntity> resultList = jpaQueryFactory.selectFrom(file)
                .where(
                        file.typeSeq.isNull(),
                        file.delDate.isNull(),
                        file.regDate.before(LocalDateTime.now().minusHours(1)))
                .fetch();
        for (FileEntity result : resultList) {
            fileService.delete(result.getSavFileName());
            result.setDelDate(LocalDateTime.now());
            fileDAO.save(result);
        }
    }

}
