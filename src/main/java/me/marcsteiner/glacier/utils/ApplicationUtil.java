package me.marcsteiner.glacier.utils;

import lombok.experimental.UtilityClass;
import me.marcsteiner.glacier.Glacier;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@UtilityClass
public class ApplicationUtil {

    // Source: https://stackoverflow.com/questions/4159802/how-can-i-restart-a-java-application
    public static void restart() {
        try {
            String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            File jar = new File(Glacier.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            if (!jar.getName().endsWith(".jar")) {
                return;
            }

            Glacier.getInstance().getPippo().stop();

            ProcessBuilder builder = new ProcessBuilder(Arrays.asList(java, "-jar", jar.getPath()));
            builder.start();

            System.exit(0);
        } catch (IOException | URISyntaxException ex) {
            Glacier.getInstance().getLogger().error("Could not restart Glacier.", ex);
            System.exit(-1);
        }
    }

}
