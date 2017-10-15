package me.marcsteiner.glacier;

import com.virtlink.commons.configuration2.jackson.JsonConfiguration;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.marcsteiner.glacier.bootstrap.BootstrapOptions;
import org.apache.commons.cli.*;
import org.apache.commons.configuration2.builder.DefaultReloadingDetectorFactory;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@MetaInfServices
public class Glacier extends Application {

    @Getter(lazy = true)
    private static final Glacier instance = new Glacier();

    @Getter(lazy = true)
    private final Logger logger = LoggerFactory.getLogger(Glacier.class);

    @Getter @Setter(onParam = @__(@NonNull))
    private CommandLine cmdArgs;

    @Getter @Setter(onParam = @__(@NonNull))
    private JsonConfiguration config;

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

        String confPath = getInstance().getCmdArgs().getOptionValue("config");
        if(confPath == null) {
            confPath = "config.json";
        }
        if(!confPath.endsWith(".json")) {
            confPath += ".json";
        }

        File confFile = new File(Paths.get(".").toAbsolutePath().normalize().toFile(), confPath);
        if(!confFile.exists()) {
            try {
                Files.copy(Glacier.class.getResourceAsStream("/config.json"), confFile.toPath());
            } catch (IOException ex) {
                getInstance().getLogger().error("Could not create default config.json file.", ex);
                System.exit(-1);
            }
        }

        try {
            getInstance().setConfig(new FileBasedConfigurationBuilder<>(JsonConfiguration.class)
                    .configure(new Parameters().properties()
                            .setFileName(confPath)
                            .setThrowExceptionOnMissing(true)
                            .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
                            .setReloadingDetectorFactory(new DefaultReloadingDetectorFactory())
                            .setEncoding("UTF-8"))
            .getConfiguration());
        } catch (ConfigurationException ex) {
            getInstance().getLogger().error("Could not load config.json file. " +
                    "Please run it the config.json file thought a JSON validator and check for missing keys.", ex);
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
