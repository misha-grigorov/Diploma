package com.rx;

import com.rx.controllers.DocumentDownloadController;
import com.rx.services.DocumentStorageService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(DocumentDownloadController.class)
public class FileDownloadControllerTests {

    @MockBean
    private FileSystemResource mockFileSystemResource;

    @MockBean
    private DocumentStorageService mockDocumentStorageService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldOkWhenDownloading() throws Exception {
        UUID randomUUID = UUID.randomUUID();
        String fileContent = "Тестовый текст !()_не";
        long contentLength = fileContent.length();
        byte[] fileContentBytes = fileContent.getBytes();

        given(mockFileSystemResource.getInputStream())
                .willReturn(new ByteArrayInputStream(fileContentBytes));

        given(mockFileSystemResource.getFilename()).willReturn("тест файл.docx");
        given(mockFileSystemResource.contentLength()).willReturn(contentLength);

        /*given(mockFileStorageService.getDocumentFromStorageById(randomUUID))
                .willReturn(new FileDownloadResultDtoBuilder()
                        .withFileResource(mockFileSystemResource)
                        .build());*/

        this.mvc.perform(get("/download?fileUUID=" + randomUUID))
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileContentBytes))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%D1%82%D0%B5%D1%81%D1%82%20%D1%84%D0%B0%D0%B9%D0%BB.docx\"; filename*=UTF-8''%D1%82%D0%B5%D1%81%D1%82%20%D1%84%D0%B0%D0%B9%D0%BB.docx"))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength)));

        /*then(mockFileStorageService).should().getDocumentFromStorageById(randomUUID);*/
    }

    @Test
    public void should404WhenFileNotFound() throws Exception {
        UUID randomUUID = UUID.randomUUID();

        /*given(mockFileStorageService.getDocumentFromStorageById(randomUUID))
                .willThrow(new FileDownloadNotFoundException());

        this.mvc.perform(get("/download?fileUUID=" + randomUUID))
                .andExpect(status().isNotFound());

        then(mockFileStorageService).should().getDocumentFromStorageById(randomUUID);*/
    }
}
