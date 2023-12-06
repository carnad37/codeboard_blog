package com.hhs.codeboard.blog.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void uploadRequest() throws Exception {

        InputStream fileStream = new FileInputStream("/Users/hwanghyeonsu/Downloads/20210226_900824385703777_ko.jpg");
        MockMultipartFile file = new MockMultipartFile("test", "20210226_900824385703777_ko.jpg", MediaType.IMAGE_JPEG.getType(), fileStream);

        mockMvc.perform(
                multipart("/public/article/upload").file(file)
        ).andExpect(status().isOk());

    }

}
