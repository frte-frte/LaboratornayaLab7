package managers;

import data.Flat;

import java.time.LocalDateTime;
import java.util.ArrayDeque;

public class CollectionManager {
    private static ArrayDeque<Flat> collection;
    private final LocalDateTime initializationTime;

    public CollectionManager(DatabaseManager databaseManager) {
        this.initializationTime = LocalDateTime.now();
        collection = databaseManager.loadCollection();
    }

    public long getMaxId() {
        return collection.stream()
                .mapToLong(Flat::getId)
                .max()
                .orElse(Long.MIN_VALUE);
    }

    public Long generateId() {
        return getMaxId() + 1;
    }

    public static ArrayDeque<Flat> getCollection() {
        return collection;
    }

    public LocalDateTime getInitializationTime() {
        return initializationTime;
    }
}
