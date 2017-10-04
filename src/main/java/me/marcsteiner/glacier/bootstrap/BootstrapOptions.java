package me.marcsteiner.glacier.bootstrap;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class BootstrapOptions {

    public static Options getOptions() {
        Options options = new Options();

        options.addOption(new Option("c", "config", true, "Path to Glacier config.json file"));
        options.addOption(new Option("a", "address", true, "IP Address to which Glacier should bind to"));
        options.addOption(new Option("p", "port", true, "Port on which Glacier should listen on"));
        options.addOption(new Option("m", "mode", true, "Mode in which Glacier should run (Dev/Prod/Test)"));

        return options;
    }

}
