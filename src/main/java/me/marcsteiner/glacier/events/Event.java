package me.marcsteiner.glacier.events;

import java.util.concurrent.atomic.AtomicInteger;

public interface Event {

    AtomicInteger PRIORITY = new AtomicInteger(5); // 1 = lowest, 5 = standard, 10 = highest

    default void setPriority(int priority) {
        PRIORITY.set(priority);
    }

}
