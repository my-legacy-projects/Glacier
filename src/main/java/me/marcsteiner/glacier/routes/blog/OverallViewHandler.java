package me.marcsteiner.glacier.routes.blog;

import me.marcsteiner.glacier.renderer.MarkdownToHtml;
import org.apache.commons.io.FileUtils;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteHandler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

public class OverallViewHandler implements RouteHandler {

    @Override
    public void handle(RouteContext routeContext) {
        File file = new File("articles/");
        Collection<File> files = FileUtils.listFiles(file, new String[]{ "md" }, true);

        Map<String, Map<String, String>> articleList = new LinkedHashMap<>();

        for (File f : files) {
            String rawMarkdown;

            try {
                byte[] encoded = Files.readAllBytes(f.toPath());
                rawMarkdown = new String(encoded, Charset.forName("UTF-8"));
            } catch (IOException ex) {
                continue;
            }

            String[] lines = rawMarkdown.split("\n");

            String path;
            int startIndex = -1;
            int endIndex = -1;

            try {
                path = f.toPath().toAbsolutePath().toUri().toURL().toString();
            } catch (MalformedURLException ex) {
                routeContext.render("pippo/500internalError");
                continue;
            }

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];

                if (startIndex == -1 && line.equals("---")) {
                    startIndex = i;
                    continue;
                }

                if (endIndex == -1 && line.equals("---"))
                    endIndex = i;
            }

            String article = String.join("\n", Arrays.copyOfRange(lines, endIndex + 2, lines.length));
            String html = MarkdownToHtml.mdToHtml(article);

            int index = html.indexOf("<h2>");

            Map<String, String> map = MarkdownToHtml.parseVariables(lines);
            map.put("url", "/blog/" + path.split(Pattern.quote("/articles/"))[1].replace(".md", ""));

            map.put("preview", html.substring(0, index));

            articleList.put(map.get("title"), map);
        }

        routeContext.setLocal("articleList", articleList);
        routeContext.render("articleList");
    }

}
