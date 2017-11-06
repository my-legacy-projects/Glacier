package me.marcsteiner.glacier.commands.impl;

import me.marcsteiner.glacier.Glacier;
import me.marcsteiner.glacier.commands.Command;
import me.marcsteiner.glacier.commands.CommandExecuteException;
import me.marcsteiner.glacier.commands.CommandExecutor;

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
                try {
                    restart();
                } catch (URISyntaxException | IOException ex) {
                    throw new CommandExecuteException(ex);
                }
                Glacier.getInstance().getPippo().stop();

                return true;
            }
        } else {
            logger.info("Thank you and have a nice day.");
            Glacier.getInstance().getPippo().stop();

            return true;
        }
        return false;
    }

    // https://stackoverflow.com/questions/4159802/how-can-i-restart-a-java-application
    private void restart() throws URISyntaxException, IOException {
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        File jar = new File(Glacier.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        if(!jar.getName().endsWith(".jar"))
            return;

        ProcessBuilder builder = new ProcessBuilder(Arrays.asList(java, "-jar", jar.getPath()));
        builder.start();
    }

}
