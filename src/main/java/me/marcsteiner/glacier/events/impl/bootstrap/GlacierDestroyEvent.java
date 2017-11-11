package me.marcsteiner.glacier.events.impl.bootstrap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.marcsteiner.glacier.events.Event;

@RequiredArgsConstructor
public class GlacierDestroyEvent implements Event {

    @Getter
    private final long timestamp; // Timestamp of Glacier Destroy in milliseconds since epoch time

}
