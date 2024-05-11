package com.sender.client.worker.service;

import org.springframework.stereotype.Component;


@Component
public class EmailClientApi {
    /**
     * return true if email delivered destination email
     */
    public boolean sendEmail(String destinationEmail, String message) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }
}
