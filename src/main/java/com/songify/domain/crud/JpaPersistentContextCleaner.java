package com.songify.domain.crud;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
class JpaPersistentContextCleaner implements PersistentContextCleaner {

    private final EntityManager entityManager;
    @Override
    public void clear() {
        entityManager.clear();
    }
}
