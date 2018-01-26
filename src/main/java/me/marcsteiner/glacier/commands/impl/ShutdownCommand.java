package me.marcsteiner.glacier.commands.impl;

import me.marcsteiner.glacier.Glacier;
import me.marcsteiner.glacier.commands.Command;
import me.marcsteiner.glacier.commands.CommandExecuteException;
import me.marcsteiner.glacier.commands.CommandExecutor;
import me.marcsteiner.glacier.utils.ApplicationUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@Command(value = "shutdown", aliases = { "exit", "stop", "bye" }, usage = "shutdown [-r]",
         description = "Shuts Glacier down or restarts it")
public class ShutdownCommand implements CommandExecutor {

    @Override
    public boolean execute(String label, String[] args) throws CommandExecuteException {
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("-r")) {
                logger.info("Thank you and have a nice day. See you soon.");

                ApplicationUtil.restart();
                return true;
            }
        } else {
            logger.info("Thank you and have a nice day.");
            Glacier.getInstance().getPippo().stop();

            System.exit(0);
            return true;
        }
        return false;
    }

}
