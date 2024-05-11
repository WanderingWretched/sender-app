package com.sender.store.dao;

import com.sender.store.entities.SendEmailTaskEntity;
import com.sender.store.repositories.SendEmailTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class SendEmailTaskDao {
    private final SendEmailTaskRepository sendEmailTaskRepository;
    private static final Duration TASK_EXECUTE_DURATION = Duration.ofSeconds(10);

    @Autowired
    SendEmailTaskDao(SendEmailTaskRepository sendEmailTaskRepository) {
        this.sendEmailTaskRepository = sendEmailTaskRepository;
    }

    @Transactional
    public SendEmailTaskEntity save(SendEmailTaskEntity entity) {
        return sendEmailTaskRepository.save(entity);

    }

    public List<Long> findNotProcessedIds() {
        Instant latestTryAtLte = Instant.now().minus(TASK_EXECUTE_DURATION);
        return sendEmailTaskRepository.findAllNotProcessed(latestTryAtLte);
    }

    public Optional<SendEmailTaskEntity> findNotProcessedById(Long id) {
        Instant latestTryAtLte = Instant.now().minus(TASK_EXECUTE_DURATION);
        return sendEmailTaskRepository.findNotProcessedById(id, latestTryAtLte);
    }

    @Transactional
    public void markAsProcessed(SendEmailTaskEntity entity) {
        sendEmailTaskRepository.markAsProcessed(entity.getId());
    }

    @Transactional
    public void updateLatestTryAt(SendEmailTaskEntity entity) {
        sendEmailTaskRepository.updateLatestTryAt(entity.getId());
    }
}
