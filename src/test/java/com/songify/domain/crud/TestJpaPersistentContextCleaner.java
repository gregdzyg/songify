package com.songify.domain.crud;

class TestJpaPersistentContextCleaner implements PersistentContextCleaner{

    private boolean cleared;

    @Override
    public void clear() {
        cleared = true;
    }

    boolean wasCleared() {
        return cleared;
    }
}
