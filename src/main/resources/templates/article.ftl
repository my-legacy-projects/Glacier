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

    <title>${title} - MarcSteiner.me</title>

    <meta property="og:title" content="${title}">
    <meta property="og:type" content="article">
    <meta property="og:image" content="${image}">
    <meta property="og:description" content="${description}">
    <meta property="og:site_name" content="MarcSteiner.me">
</head>

<body>
<header class="background" style="background-image: url('${image}'); background-size: cover; background-position: center center;">
    <nav class="navigation">
        <div class="content">
                <span class="brand">
                    <a href="/" class="link">marc steiner</a>
                </span>
            <span class="links">
                    <a href="/blog" class="link">blog</a>
                    <a href="mailto:contact@marcsteiner.me" class="link">contact</a>
                </span>
            <div class="post-info">
                <h1>${title}</h1>
                <h3>${description}</h3>
                <h5>by ${author}</h5>
            </div>
        </div>
    </nav>
</header>
<main class="container markdown-body">${article}</main>
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
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-131249369-1"></script>
<script>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('js', new Date());

    gtag('config', 'UA-131249369-1');
</script>
</body>
</html>
