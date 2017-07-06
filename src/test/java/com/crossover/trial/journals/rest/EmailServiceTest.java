package com.crossover.trial.journals.rest;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.CategoryRepository;
import com.crossover.trial.journals.service.EmailService;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.SubscriptionService;
import com.crossover.trial.journals.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by nguni52 on 2017/07/06.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmailServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private JournalService journalService;
    @Autowired
    EmailService emailService;
    @Autowired
    private CategoryRepository categoryRepository;
    private Log log = LogFactory.getLog(this.getClass().getName());
    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    public void sendEmailsForPublishedJournals() {
        Date today = new Date();
        List<Journal> journals = journalService.findByDate(today);
        assertNotNull(journals);
        assertEquals(3, journals.size());

        log.info("Number of journals:: " + journals.size() + "\n\n");

        // find all users
        List<User> users = userService.findAll();

        // send test emails for these users
        emailService.sendDailyDigest(journals, users);
    }

    @Test
    public void sendNewJournalEmailNotification() {
        long categoryId = 3;
        Category category = categoryRepository.findOne(categoryId);

        assertNotNull(category);
        assertEquals(category.getName(), "endocrinology");

        Journal journal = journalService.findByCategory(category).get(0);

        assertNotNull(journal);
        assertEquals("Medicine", journal.getName());

        // find subscriptions for this category
        List<Subscription> subscriptions = subscriptionService.findByCategory(category);
        assertNotNull(subscriptions);
        assertEquals(1, subscriptions.size());


        // send email to users who have subscribed to that category
        emailService.sendNewJournalNotification(journal, subscriptions);
    }
}
