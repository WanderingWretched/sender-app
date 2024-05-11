package com.sender.store.repositories;

import com.sender.store.entities.SendEmailTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SendEmailTaskRepository extends JpaRepository<SendEmailTaskEntity, Long> {

    @Query("""
            SELECT task.id
            FROM SendEmailTaskEntity task
            WHERE task.processedAt IS NULL
                AND (task.latestTryAt IS NULL OR task.latestTryAt <= :latestTryAtLte)
            ORDER BY task.createdAt
            """)
    List<Long> findAllNotProcessed(Instant latestTryAtGte);


    @Modifying
    @Query("""
                UPDATE SendEmailTaskEntity task
                SET task.processedAt = CURRENT_TIMESTAMP
                WHERE task.id = :id
            """)
    void markAsProcessed(Long id);

    @Modifying
    @Query("""
                UPDATE SendEmailTaskEntity task
                SET task.latestTryAt = CURRENT_TIMESTAMP
                WHERE task.id = :id
            """)
    void updateLatestTryAt(Long id);

    @Query("""
            SELECT task
            FROM SendEmailTaskEntity task
            WHERE task.id = :id
            AND task.processedAt IS NULL
            AND (task.latestTryAt IS NULL OR task.latestTryAt <= :latestTryAtLte)
            """)
    Optional<SendEmailTaskEntity> findNotProcessedById(Long id, Instant latestTryAtGte);
}
