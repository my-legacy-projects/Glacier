package me.marcsteiner.glacier.events.impl.web.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.marcsteiner.glacier.events.impl.web.WebEvent;

import java.sql.SQLException;

@RequiredArgsConstructor
public class SQLExceptionEvent extends WebEvent {

    @Getter
    private final SQLException exception;

    @Getter @Setter
    private boolean closingConnection = true;

}
