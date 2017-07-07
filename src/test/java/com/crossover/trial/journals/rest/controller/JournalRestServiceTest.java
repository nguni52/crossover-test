package com.crossover.trial.journals.rest.controller;

import com.crossover.trial.journals.Application;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by nguni52 on 2017/07/06.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JournalRestServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    @WithUserDetails("user1")
    public void browseJournals() throws Exception {
        mockMvc.perform(get("/rest/journals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Medicine")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("publisher1")
    public void findPublished() throws Exception {
        mockMvc.perform(get("/rest/journals/published"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Test Journal")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("publisher1")
    public void unPublishedJournal() throws Exception {
        mockMvc.perform(delete("/rest/journals/unPublish/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(print());
    }

    @Test
    @WithUserDetails("user1")
    public void subscribeCategory() throws Exception {
        mockMvc.perform(post("/rest/journals/subscribe/3")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(print());
    }

    @Test
    @WithUserDetails("user1")
    public void findSubscriptions() throws Exception {
        mockMvc.perform(get("/rest/journals/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(print());
    }
}
