package me.marcsteiner.glacier.events.impl.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.marcsteiner.glacier.events.Event;

@RequiredArgsConstructor
public abstract class WebEvent implements Event {

    @Getter @Setter
    private int responseCode = 500;

    @Getter @Setter
    private String template = "error/internal";

}
