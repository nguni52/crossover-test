package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.Journal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by nguni52 on 2017/07/06.
 */
@Component
public class ScheduledTask {
    @Autowired
    JournalService journalService;


    @Scheduled(cron = "${cron.journal.daily.digest}")
    public void dailyDigest() {
        List<Journal> journals = journalService.findByDate(new Date());

    }
}
