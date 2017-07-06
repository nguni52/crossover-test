package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;

import java.util.List;

/**
 * Created by nguni52 on 2017/07/06.
 */
public interface EmailService {
    void sendDailyDigest(List<Journal> journals, List<User> users);

    void sendNewJournalNotification(Journal journal, List<Subscription> subscriptions);
}
