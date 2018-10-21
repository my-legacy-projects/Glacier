<!doctype html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700,800">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/tonsky/FiraCode@1.206/distr/fira_code.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/github-markdown-css/2.10.0/github-markdown.css">
    <link rel="stylesheet" href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@9.13.1/build/styles/default.min.css">

    <link rel="stylesheet" type="text/css" media="screen" href="${webjarsAt('normalize.css/normalize.css')}">
    <link rel="stylesheet" type="text/css" media="screen" href="/static/app.css">

    <title>MarcSteiner.me</title>

    <meta property="og:title" content="MarcSteiner.me">
    <meta property="og:type" content="website">
    <meta property="og:description" content="Be interested in things that interest you.">
    <meta property="og:site_name" content="MarcSteiner.me">
</head>

<body>
<nav class="navigation">
    <div class="content">
            <span class="brand">
                <a href="/" class="link">marc steiner</a>
            </span>
        <span class="links">
                <a href="/blog" class="link">blog</a>
                <a href="mailto:contact@marcsteiner.me" class="link">contact</a>
            </span>
    </div>
</nav>
<main class="container">
    <#list articleList as keyMap, valueMap>
        <div class="blog-post">
            <h1><a href="${valueMap["url"]}">${keyMap}</a></h1>
            <h3>${valueMap["date"]} — by ${valueMap["author"]}</h3>
            <div class="preview markdown-body">
                ${valueMap["preview"]}
                <a class="read-more" href="${valueMap["url"]}">Read more...</a>
            </div>
        </div>
    </#list>
</main>
</body>

<script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@9.13.1/build/highlight.min.js"></script>

<script>
    hljs.initHighlightingOnLoad();

    const quotes = document.getElementsByTagName("blockquote");
    const codes = document.getElementsByTagName("code");

    for (let i = 0; i < quotes.length; i++) {
        let quote = quotes[i];
        quote.innerHTML = quote.innerHTML.trim();
    }
    for (let i = 0; i < codes.length; i++) {
        let code = codes[i];
        code.innerHTML = code.innerHTML.trim();
    }
</script>

</html>
