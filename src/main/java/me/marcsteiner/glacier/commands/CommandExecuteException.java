package me.marcsteiner.glacier.commands;

public class CommandExecuteException extends Exception {

    public CommandExecuteException(String s) {
        super(s);
    }

    public CommandExecuteException(Throwable throwable) {
        super(throwable);
    }

}
