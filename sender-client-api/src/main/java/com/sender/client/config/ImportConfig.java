package com.sender.client.config;

import com.sender.store.EnableSenderStore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        EnableSenderStore.class
})
@Configuration
public class ImportConfig {
}
