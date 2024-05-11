package com.sender.client.worker.scheduled;

import com.sender.client.worker.service.EmailClientApi;
import com.sender.client.worker.service.RedisLock;
import com.sender.client.worker.service.RedisLockWrapper;
import com.sender.store.dao.SendEmailTaskDao;
import com.sender.store.entities.SendEmailTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
public class SendEmailTaskScheduler {

    private final SendEmailTaskDao sendEmailTaskDao;

    private final EmailClientApi emailClientApi;

    private final RedisLock redisLock;

    private final RedisLockWrapper redisLockWrapper;

    @Autowired
    SendEmailTaskScheduler(
            SendEmailTaskDao sendEmailTaskDao,
            EmailClientApi emailClientApi,
            RedisLock redisLock,
            RedisLockWrapper redisLockWrapper
    ) {
        this.sendEmailTaskDao = sendEmailTaskDao;
        this.emailClientApi = emailClientApi;
        this.redisLock = redisLock;
        this.redisLockWrapper = redisLockWrapper;
    }


    private static final String SEND_EMAIL_TASK_KEY_FORMAT = "sender:send:email:task:&s";

    @Scheduled(cron = "*/5 * * * * *")
    public void executeSendEmailTask() {

        List<Long> sendEmailTaskIds = sendEmailTaskDao.findNotProcessedIds();

        for (Long sendEmailTaskId : sendEmailTaskIds) {
            String sendEmailTaskKey = getSendEmailTaskKey(sendEmailTaskId);

            redisLockWrapper.lockAndExecuteTask(
                    sendEmailTaskKey,
                    Duration.ofSeconds(5),
                    () -> sendEmail(sendEmailTaskId)
            );
        }
    }

    private void sendEmail(Long sendEmailTaskId) {
        Optional<SendEmailTaskEntity> optionalSendEmailTask = sendEmailTaskDao.findNotProcessedById(sendEmailTaskId);

        if (optionalSendEmailTask.isEmpty()) {
            return;
        }

        SendEmailTaskEntity sendEmailTask = optionalSendEmailTask.get();

        String destinationEmail = sendEmailTask.getDestinationEmail();
        String message = sendEmailTask.getMessage();


        boolean delivered = emailClientApi.sendEmail(destinationEmail, message);

        if (delivered) {
            System.out.println("Task already processed");
            sendEmailTaskDao.markAsProcessed(sendEmailTask);
            return;
        }

        System.out.println("Task returned to process");
        sendEmailTaskDao.updateLatestTryAt(sendEmailTask);
    }

    private static String getSendEmailTaskKey(Long taskId) {
        return SEND_EMAIL_TASK_KEY_FORMAT.formatted(taskId);
    }
}
