package me.marcsteiner.glacier.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface CommandExecutor {

    Logger logger = LoggerFactory.getLogger("Command");

    boolean execute(String label, String[] args) throws CommandExecuteException;

}
