package me.marcsteiner.glacier.commands.impl;

import me.marcsteiner.glacier.Glacier;
import me.marcsteiner.glacier.commands.Command;
import me.marcsteiner.glacier.commands.CommandExecutor;
import me.marcsteiner.glacier.utils.NetUtil;

@Command(value = "status", aliases = { "s" }, usage = "status",
         description = "Shows status information about Glacier")
public class StatusCommand implements CommandExecutor {

    @Override
    public boolean execute(String label, String[] args) {
        logger.info("-- Glacier Status --");
        logger.info("Pippo: 127.0.0.1:{} ({})", Glacier.getInstance().getPippo().getServer().getPort(), ping());
        logger.info("Database: {} ({} in idle)", Glacier.getInstance().getDatabase() != null ? "UP" : "DOWN",
                                                 Glacier.getInstance().getDatabase().getAvailableConnections());

        return true;
    }

    private String ping() {
        return NetUtil.isPortAvailable(Glacier.getInstance().getPippo().getServer().getPort()) ? "DOWN" : "UP";
    }

}
