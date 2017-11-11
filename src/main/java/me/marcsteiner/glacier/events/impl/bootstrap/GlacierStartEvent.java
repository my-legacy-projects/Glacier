package me.marcsteiner.glacier.events.impl.bootstrap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.marcsteiner.glacier.events.Cancellable;
import me.marcsteiner.glacier.events.Event;

@RequiredArgsConstructor
public class GlacierStartEvent extends Cancellable implements Event {

    @Getter
    private final long timestamp; // Timestamp of Glacier Start in milliseconds since epoch time

}
