package me.marcsteiner.glacier.commands.impl;

import me.marcsteiner.glacier.Glacier;
import me.marcsteiner.glacier.commands.Command;
import me.marcsteiner.glacier.commands.CommandExecuteException;
import me.marcsteiner.glacier.commands.CommandExecutor;

import java.util.Arrays;
import java.util.Map;

@Command(value = "help", aliases = { "?" }, usage = "help [command ...]",
         description = "Shows a list of all commands and their description")
public class HelpCommand implements CommandExecutor {

    @Override
    public boolean execute(String label, String[] args) throws CommandExecuteException {
        if(args.length == 0) {
            logger.info("-- List of commands in Glacier --");

            for(Map.Entry entry : Glacier.getInstance().getCommandManager().getCommands().entrySet()) {
                Command cmd = (Command) entry.getKey();

                logger.info("{}: {}", cmd.value(), cmd.description());
            }

            return true;
        } else if(args.length == 1) {
            for(Map.Entry entry : Glacier.getInstance().getCommandManager().getCommands().entrySet()) {
                Command cmd = (Command) entry.getKey();

                if(cmd.value().equalsIgnoreCase(args[0]) || Arrays.asList(cmd.aliases()).contains(args[0])) {
                    logger.info("-- Information about `{}\' --", args[0]);
                    logger.info("Command: {}", cmd.value());
                    logger.info("Aliases: {}", Arrays.toString(cmd.aliases()));
                    logger.info("Description: {}", cmd.description());
                    logger.info("Usage: {}", cmd.usage());
                    logger.info("Arguments: {}", cmd.args());

                    return true;
                }
            }

            logger.error("Command `{}\' not found.", args[0]);
            return true;
        }

        return false;
    }

}
