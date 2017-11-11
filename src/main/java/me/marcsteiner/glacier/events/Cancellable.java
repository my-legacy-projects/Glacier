package me.marcsteiner.glacier.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Cancellable {

    @Getter @Setter
    private boolean cancelled;

}
