package com.sender.store;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("com.sender.store.dao")
@EntityScan("com.sender.store.entities")
@EnableJpaRepositories("com.sender.store.repositories")
public class EnableSenderStore {

}
