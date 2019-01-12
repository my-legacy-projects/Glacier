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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OverallViewHandler implements RouteHandler {

    @Override
    public void handle(RouteContext routeContext) {
        File file = new File("articles/");
        Collection<File> files = FileUtils.listFiles(file, new String[]{ "md" }, true);

        Map<String, Map<String, String>> unsortedArticleList = new LinkedHashMap<>();

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

            if (map.getOrDefault("hidden", "false").equalsIgnoreCase("true")) {
                continue;
            }

            unsortedArticleList.put(map.get("title"), map);
        }

        Comparator<Map.Entry<String, Map<String, String>>> comparator = (a, b) -> {
            SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy");

            String strDateFirst = a.getValue().get("date");
            String strDateSecond = b.getValue().get("date");

            Date dateFirst;
            Date dateSecond;

            try {
                dateFirst = format.parse(strDateFirst);
                dateSecond = format.parse(strDateSecond);
            } catch (ParseException ex) {
                routeContext.render("pippo/500internalError");
                return 0;
            }

            return dateFirst.compareTo(dateSecond);
        };

        Map<String, Map<String, String>> sortedArticleList =
                unsortedArticleList.entrySet().stream().sorted(comparator).
                        collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (a, b) -> a, LinkedHashMap::new));

        routeContext.setLocal("articleList", sortedArticleList);
        routeContext.render("articleList");
    }

}
