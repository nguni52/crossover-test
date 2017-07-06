package com.crossover.trial.journals.repository;

import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by nguni52 on 2017/07/06.
 */
public interface SubscriptionRepository  extends CrudRepository<Subscription, Long> {
    List<Subscription> findByCategory(Category category);
}
