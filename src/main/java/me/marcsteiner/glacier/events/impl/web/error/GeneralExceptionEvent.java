package me.marcsteiner.glacier.events.impl.web.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.marcsteiner.glacier.events.impl.web.WebEvent;

@RequiredArgsConstructor
public class GeneralExceptionEvent extends WebEvent {

    @Getter
    private final Exception exception;

}
