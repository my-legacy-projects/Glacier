package me.marcsteiner.glacier.routes.blog;

import ro.pippo.core.route.RouteGroup;

public class BlogRouter extends RouteGroup {

    public BlogRouter() {
        super("/blog");

        GET("/", new OverallViewHandler());
        GET("/{year}", routeContext -> routeContext.redirect("/blog"));
        GET("/{year}/{month}", routeContext -> routeContext.redirect("/blog"));
        GET("/{year}/{month}/{article}", new BlogHandler());
    }

}
