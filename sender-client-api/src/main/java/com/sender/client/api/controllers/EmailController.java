package com.sender.client.api.controllers;


import com.sender.store.dao.SendEmailTaskDao;
import com.sender.store.entities.SendEmailTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    SendEmailTaskDao sendEmailTaskDao;

    @Autowired
    EmailController(SendEmailTaskDao sendEmailTaskDao) {
        this.sendEmailTaskDao = sendEmailTaskDao;
    }

    public static final String SEND_EMAIL = "/api/email/send";

    @PostMapping(SEND_EMAIL)
    public void sendEmail(
            @RequestParam("destination_email") String destinationEmail,
            @RequestParam String message) {

        SendEmailTaskEntity sendEmailTaskEntity = new SendEmailTaskEntity();
        sendEmailTaskEntity.setDestinationEmail(destinationEmail);
        sendEmailTaskEntity.setMessage(message);

        sendEmailTaskDao.save(sendEmailTaskEntity);
    }
}
