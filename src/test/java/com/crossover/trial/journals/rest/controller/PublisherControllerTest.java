package com.crossover.trial.journals.rest.controller;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.controller.PublisherController;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.service.JournalService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by nguni52 on 2017/07/07.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class PublisherControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Mock
    JournalService journalService;
    @Mock
    PublisherRepository publisherRepository;
    @InjectMocks
    PublisherController publisherController = new PublisherController();

    private Log log = LogFactory.getLog(this.getClass().getName());

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @WithUserDetails("publisher1")
    public void testProvideUploadInfo() throws Exception {
        mockMvc.perform(get("/publisher/publish"))
                .andExpect(status().isOk())
                .andExpect(view().name("publisher/publish"))
                .andDo(print());
    }

    @Test
    @WithUserDetails("publisher1")
    public void testHandleFileUpload() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "DATADATADATDATADATA".getBytes());

        mockMvc.perform(fileUpload("/publisher/publish")
                .file(mockFile)
                .param("name", "filename.test.pdf")
                .param("category", "1")
                .param("description", "New Journal Description")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/publisher/browse"))
                .andDo(print());
    }

    @Test
    @WithUserDetails("publisher1")
    public void testHandleEmptyFileUpload() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "".getBytes());

        mockMvc.perform(fileUpload("/publisher/publish")
                .file(mockFile)
                .param("name", "filename.test.pdf")
                .param("category", "1")
                .param("description", "New Journal Description")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isFound())

                .andExpect(redirectedUrl("/publisher/publish"))
                .andDo(print());
    }

    @Test
    @WithUserDetails("publisher1")
    public void testHandleNoCategoryFileUpload() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "DATADATADATDATADATA".getBytes());

        mockMvc.perform(fileUpload("/publisher/publish")
                .file(mockFile)
                .param("name", "filename.test.pdf")
                .param("category", "1000")
                .param("description", "New Journal Description")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isFound())

                .andExpect(redirectedUrl("/publisher/publish"))
                .andDo(print());
    }
}
