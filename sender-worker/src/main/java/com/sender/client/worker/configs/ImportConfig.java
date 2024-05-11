package com.sender.client.worker.configs;

import com.sender.store.EnableSenderStore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import({
        EnableSenderStore.class
})
@Configuration
@EnableScheduling
public class ImportConfig {
}
