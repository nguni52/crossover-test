package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nguni52 on 2017/07/06.
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailService;
    @Autowired
    private Configuration freemarkerConfiguration;
    Log log = LogFactory.getLog(this.getClass().getName());
    private static final String DEFAULT_ENCODING = "utf-8";

    @Override
    public void sendDailyDigest(List<Journal> journals, List<User> users) {

        for (User user : users) {
            try {
                MimeMessage msg = javaMailService.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

                helper.setFrom("nguni52@gmail.com");
                helper.setTo(user.getEmail());
                helper.setSubject("Journals - Daily Digest");

                String content = generateContent(journals, user);
                helper.setText(content, true);
                javaMailService.send(msg);
            } catch (MessagingException e) {
                log.error("build email failed", e);
            } catch (Exception e) {
                log.error("send email failed", e);
            }
        }
    }

    @Override
    public void sendNewJournalNotification(Journal journal, List<Subscription> subscriptions) {
        for (Subscription subscription : subscriptions) {
            try {
                MimeMessage msg = javaMailService.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

                helper.setFrom("nguni52@gmail.com");
                helper.setTo(subscription.getUser().getEmail());
                helper.setSubject("Category - " + subscription.getCategory().getName() + " Journal");

                String content = generateNewJournalContent(journal, subscription);

                helper.setText(content, true);
                javaMailService.send(msg);
            } catch (MessagingException e) {
                log.error("build email failed", e);
            } catch (Exception e) {
                log.error("send email failed", e);
            }
        }
    }

    private String generateContent(List<Journal> journals, User user) throws MessagingException {
        try {
            Map context = new HashMap<>();
            context.put("loginName", user.getLoginName());
            context.put("journals", journals);
            Template template = freemarkerConfiguration.getTemplate("mailTemplate.ftl", DEFAULT_ENCODING);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
        } catch (IOException e) {
            log.error("FreeMarker template not exist", e);
            throw new MessagingException("FreeMarker template not exist", e);
        } catch (TemplateException e) {
            log.error("FreeMarker process failed", e);
            throw new MessagingException("FreeMarker process failed", e);
        }
    }

    private String generateNewJournalContent(Journal journal, Subscription subscription) throws MessagingException {
        try {
            Map context = new HashMap<>();
            context.put("loginName", subscription.getUser().getLoginName());
            context.put("journal", journal);
            context.put("category", subscription.getCategory());
            Template template = freemarkerConfiguration.getTemplate("notificationMailTemplate.ftl", DEFAULT_ENCODING);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
        } catch (IOException e) {
            log.error("FreeMarker template not exist", e);
            throw new MessagingException("FreeMarker template not exist", e);
        } catch (TemplateException e) {
            log.error("FreeMarker process failed", e);
            throw new MessagingException("FreeMarker process failed", e);
        }
    }
}
