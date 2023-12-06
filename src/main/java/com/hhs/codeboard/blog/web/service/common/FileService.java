package com.hhs.codeboard.blog.web.service.common;

import com.hhs.codeboard.blog.config.except.CodeboardException;
import com.hhs.codeboard.blog.data.entity.common.dto.ProcessResultDto;
import com.hhs.codeboard.blog.data.entity.common.entity.FileEntity;
import com.hhs.codeboard.blog.data.repository.FileDAO;
import com.hhs.codeboard.blog.enumeration.ErrorType;
import com.hhs.codeboard.blog.enumeration.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${codeboard.file.path}")
    private String fileUploadDest;

    private final FileDAO fileDAO;

    /**
     * 현재로서는 파일 중복저장을 막을 방법은 클라에서 처리 하는것뿐.
     * 제대로된 처리를 하려면 메세지큐를 구성해보아야 할듯.
     *
     * @param type
     * @param userSeq
     * @param file
     * @return
     */
    @Transactional
    public ProcessResultDto upload(FileType type, Long userSeq, MultipartFile file) throws IOException, CodeboardException {
        /**
         * 기본적으로 tomcat에서 파일 저장방식은 다음과같다.
         * 요청으로 들어온 파일정보는 일정 용량이상일경우 일단 임시 저장되게된다(메모리에 둘시 서버 부하가 심함).
         * 저장된 파일의 생명주기는 해당 요청이 끝날때까지이다.(만약 다른 쓰레드로 요청파일 저장이전에 넘겨주게되면, 해당 쓰레드에서는 그 파일을 참조할 수가 없다)
         *
         * 그렇기에 파일처리 과정은 DB에 정보저장 -> 파일정보분석 -> 파일저장 순으로 진행한다.
         */
        FileEntity saveFile = new FileEntity();

        saveFile.setFileType(type.getCode());
        // file ext
        /**
         * 외부에서 들어온 파일 처리시 Normalizer를 고려.
         */
        if (!StringUtils.hasText(file.getOriginalFilename())) CodeboardException.error(ErrorType.FILE_INVALID_NAME);
        String orgFileName = Normalizer.normalize(file.getOriginalFilename(),  Normalizer.Form.NFC);

        String[] extArray = orgFileName.split("\\.");
        if (extArray.length != 2) CodeboardException.error(ErrorType.FILE_INVALID_EXT);
        saveFile.setExt(extArray[1]);
        saveFile.setFileSize(file.getSize());

        String savFileName = String.format("%s_%s_%d.%s", type.getAddName(), UUID.randomUUID().toString().replaceAll("-", ""), userSeq, saveFile.getExt());

        try (InputStream inputStream = file.getInputStream()){
            String mime = new Tika().detect(inputStream);
            // 이미지 타입이 아닐경우
            if (!StringUtils.hasText(mime) || !mime.startsWith("image/")) CodeboardException.error(ErrorType.FILE_INVALID_TYPE);
            saveFile.setMime(mime);
        } catch (IOException ioe) {
            CodeboardException.error(ErrorType.FAIL_ANALYZE_FILE);
        }
        saveFile.setSavFileName(savFileName);
        saveFile.setOrgFileName(orgFileName);
        saveFile.setRegUserSeq(userSeq);
        saveFile.setRegDate(LocalDateTime.now());

        fileDAO.save(saveFile);

        // 파일 저장
        file.transferTo(new File(fileUploadDest, savFileName));

        ProcessResultDto result = new ProcessResultDto();
        result.setSuccess(true);
        result.setSeq(saveFile.getSeq());
        result.setResult(saveFile.getSavFileName());
        return result;

    }


}
