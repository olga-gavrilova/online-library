package com.gmail.olyagavrilova.onlinelibrary.dao;

import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository  extends CrudRepository<Subscription, Long> {
}
