package me.marcsteiner.glacier.renderer;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import ro.pippo.core.route.RouteContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkdownToHtml {

    private static List<Extension> extensions = Arrays.asList(
            AutolinkExtension.create(),
            StrikethroughExtension.create(),
            TablesExtension.create(),
            InsExtension.create()
    );

    private static Parser parser = Parser.builder().extensions(extensions).build();
    private static HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();

    public static void render(Path path, RouteContext context) {
        String rawMarkdown;

        try {
            byte[] encoded = Files.readAllBytes(path);
            rawMarkdown = new String(encoded, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            context.html().send("A error occured: " + ex.getMessage());
            return;
        }

        String[] lines = rawMarkdown.split("\n");

        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if (startIndex == -1 && line.equals("---")) {
                startIndex = i;
                continue;
            }

            if (endIndex == -1 && line.equals("---"))
                endIndex = i;
        }

        for (Map.Entry<String, String> entry : parseVariables(lines).entrySet()) {
            context.setLocal(entry.getKey(), entry.getValue());
        }

        String article = String.join("\n", Arrays.copyOfRange(lines, endIndex + 2, lines.length));

        article = mdToHtml(article);

        context.setLocal("article", article);
        context.render("article");
    }

    public static Map<String, String> parseVariables(String[] lines) {
        Map<String, String> variables = new HashMap<>();
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if (startIndex == -1 && line.equals("---")) {
                startIndex = i;
                continue;
            }

            if (endIndex == -1 && line.equals("---"))
                endIndex = i;
        }

        for (int i = startIndex + 1; i < endIndex; i++) {
            String line = lines[i];
            String[] parsed = line.split(": ");

            String key = parsed[0];
            String value = parsed[1];

            variables.put(key, value);
        }

        return variables;
    }

    public static String mdToHtml(String markdown) {
        Node doc = parser.parse(markdown);
        return renderer.render(doc);
    }

}
