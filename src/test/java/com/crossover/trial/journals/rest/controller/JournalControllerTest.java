package com.crossover.trial.journals.rest.controller;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.controller.JournalController;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.repository.UserRepository;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by nguni52 on 2017/07/07.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class JournalControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Mock
    UserRepository userRepository;
    @Mock
    PublisherRepository publisherRepository;
    @InjectMocks
    JournalController journalController = new JournalController();

    private Log log = LogFactory.getLog(this.getClass().getName());

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @WithUserDetails("publisher1")
    public void testRenderDocument() throws Exception {
        mockMvc.perform(get("/view/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"));
//                .andDo(print());
    }

    @Test(expected = NestedServletException.class)
    @WithUserDetails("publisher1")
    public void testRenderDocumentNotFound() throws Exception {
        mockMvc.perform(get("/view/1000"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/pdf"))
                .andDo(print());
    }
}
