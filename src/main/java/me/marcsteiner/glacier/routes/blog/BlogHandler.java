package me.marcsteiner.glacier.routes.blog;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import me.marcsteiner.glacier.renderer.MarkdownToHtml;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BlogHandler implements RouteHandler {

    @Override
    public void handle(RouteContext context) {
        int year = context.getParameter("year").toInt();
        int month = context.getParameter("month").toInt();
        String article = context.getParameter("article").toString();

        article = article.replace(" ", "-");

        Path path = Paths.get("articles/" + year + "/" + month + "/" + article + ".md");

        if (!Files.exists(path)) {
            context.render("pippo/404notFound");
            return;
        }

        MarkdownToHtml.render(path, context);
    }

}
