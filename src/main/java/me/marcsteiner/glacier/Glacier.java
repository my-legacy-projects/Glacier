package me.marcsteiner.glacier;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.marcsteiner.glacier.bootstrap.BootstrapOptions;
import org.apache.commons.cli.*;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;

@MetaInfServices
public class Glacier extends Application {

    @Getter(lazy = true)
    private static final Glacier instance = new Glacier();

    @Getter(lazy = true)
    private final Logger logger = LoggerFactory.getLogger(Glacier.class);

    @Getter @Setter(onParam = @__(@NonNull))
    private CommandLine cmdArgs;

    @Getter @Setter
    private boolean running = false;

    public static void main(String[] args) {
        getInstance().setRunning(true);
        getInstance().getLogger().info("Starting Glacier...");

        Options options = BootstrapOptions.getOptions();
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            getInstance().setCmdArgs(parser.parse(options, args));
        } catch (ParseException ex) {
            formatter.printHelp("argument", options);
            System.exit(-1);
        }

    }

    @Override
    protected void onInit() {
        // Called when Jetty is initialized
    }

    @Override
    protected void onDestroy() {
        // Called when Jetty is destroyed
    }

}
