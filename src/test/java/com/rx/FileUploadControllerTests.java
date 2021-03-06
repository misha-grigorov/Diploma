package com.rx;

import com.rx.controllers.DocumentUploadController;
import com.rx.controllers.exceptions.FileUploadIOException;
import com.rx.controllers.exceptions.FileUploadInvalidPathException;
import com.rx.services.DocumentStorageService;
import com.rx.validators.DocumentUploadFormDtoValidator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUploadControllerTests {

    @Value("${app.storage.allowed.filename}")
    private String allowedFilenamePattern;

    @MockBean
    private DocumentStorageService mockDocumentStorageService;

    private MockMvc mvc;
    private MockMultipartFile file;

    @Before
    public void init() {
        file = new MockMultipartFile("multipartFile", "text.txt", "text/plain", "This is a testHandleUploadWhenFileUploaded".getBytes());
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix("/WEB-INF/templates");
        viewResolver.setSuffix(".html");

        DocumentUploadFormDtoValidator validator = new DocumentUploadFormDtoValidator(allowedFilenamePattern);
        //DocumentUploadController controller = new DocumentUploadController(mockDocumentStorageService, validator);

        //mvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver).build();
    }

    @Test
    public void testGetUploadForm() throws Exception {
        mvc.perform(get("/upload"))
                .andExpect(status().isOk())
                .andExpect(view().name("upload"))
                .andExpect(model().hasNoErrors());

        //then(mockFileStorageService).should(times(0)).saveDocumentInStorage(file);
    }

    @Test
    public void testHandleUploadWhenFileUploaded() throws Exception {
        UUID uuid = UUID.randomUUID();
        //DocumentUploadResultDto result = new DocumentUploadResultDtoBuilder().withFileId(uuid).build();

        //given(mockFileStorageService.saveDocumentInStorage(file)).willReturn(result);

        mvc.perform(fileUpload("/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("upload-result"))
                .andExpect(model().attribute("uploadedFileUUID", uuid))
                .andExpect(model().hasNoErrors());

        //then(mockFileStorageService).should().saveDocumentInStorage(file);
    }

    @Test
    public void testHandleUploadIOException() throws Exception {
        FileUploadIOException fileUploadIOException = new FileUploadIOException();

        //given(mockFileStorageService.saveDocumentInStorage(file)).willThrow(fileUploadIOException);

        mvc.perform(fileUpload("/upload").file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("upload"))
                .andExpect(model().attribute("exception", fileUploadIOException))
                .andExpect(model().hasNoErrors());

        //then(mockFileStorageService).should().saveDocumentInStorage(file);
    }

    @Test
    public void testHandleUploadInvalidPathException() throws Exception {
        FileUploadInvalidPathException fileUploadInvalidPathException = new FileUploadInvalidPathException();

        //given(mockFileStorageService.saveDocumentInStorage(file)).willThrow(fileUploadInvalidPathException);

        mvc.perform(fileUpload("/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("upload"))
                .andExpect(model().attribute("exception", fileUploadInvalidPathException))
                .andExpect(model().hasNoErrors());

        //then(mockFileStorageService).should().saveDocumentInStorage(file);
    }

    @Test
    public void testHandleUploadWhenFilenameIsInvalid() throws Exception {
        MockMultipartFile multipartFileWithInvalidName = new MockMultipartFile("multipartFile", "test.sh", "text/plain", "echo \"Hello!\"".getBytes());

        mvc.perform(fileUpload("/upload").file(multipartFileWithInvalidName))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("fileUploadFormDto"))
                .andExpect(model().attributeHasFieldErrorCode("fileUploadFormDto", "multipartFile", "upload.file.invalid.name"))
                .andExpect(view().name("upload"));

        //then(mockFileStorageService).should(times(0)).saveDocumentInStorage(file);
    }

    @Test
    public void testHandleUploadWhenFileIsEmpty() throws Exception {
        MockMultipartFile emptyMultipartFile = new MockMultipartFile("multipartFile", "test.sh", "text/plain", (byte[]) null);

        mvc.perform(fileUpload("/upload").file(emptyMultipartFile))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("fileUploadFormDto"))
                .andExpect(model().attributeHasFieldErrorCode("fileUploadFormDto", "multipartFile", "upload.file.empty"))
                .andExpect(view().name("upload"));

        //then(mockFileStorageService).should(times(0)).saveDocumentInStorage(file);
    }
}
