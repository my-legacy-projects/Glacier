<p align="center"><img width=15% src="https://openclipart.org/image/2400px/svg_to_png/201913/glacier.png"></p>
<p align="center"><img width=17.5% src="https://i.imgur.com/FuCpGpU.png"></p>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
[![Travis CI](https://img.shields.io/travis/Marc3842h/Glacier.svg)][0]
[![Dependencies](https://img.shields.io/librariesio/github/Marc3842h/Glacier.svg)][1]
[![](https://img.shields.io/website-up-down-green-red/http/marcsteiner.me.svg?label=MarcSteiner.me)][2]
[![License](https://img.shields.io/github/license/Marc3842h/Glacier.svg)][3]
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

Glacier `/ˈgleɪʃər/` is a lightweight [Pippo](http://www.pippo.ro/)-based web
backend and blog script currently running on [MarcSteiner.me][2]. It 
implements both a simple website as well as a blog scripting software with 
support for modern techniques like Markdown or SASS. Glacier itself is highly 
customizable and is running on modern web techniques like [Jetty](https://www.eclipse.org/jetty/).

## Features

* Both simple website as well as blog scripting software
* Support for advanced technologies like Markdown or SASS
* Developed with simplicity and security in mind
* Licensed under the MIT License and fully open-source
* Highly customizable 

## Installation

Install the required dependencies:
`jre-8 jdk-8 gradle mysql phpmyadmin`

After these have been successfully installed, run the following commands:

```bash
$ git clone git@github.com:Marc3842h/Glacier.git
$ ./gradlew shadowJar
# Deploy the Glacier-1.0.0-SNAPSHOT.jar file to a location where you want it to live
$ java -jar Glacier-1.0.0-SNAPSHOT.jar --ip 127.0.0.1 --port 80
```

## Usage

Glacier is running [MarcSteiner.me][2] behind a [nginx balancing proxy](https://www.nginx.com/resources/wiki/start/topics/examples/javaservers/). 
I suggest you do the same.

You may want to edit the default `config.json` file with your own specific
options before starting the application again. If you want to customize
the layout of the website, edit the corresponding `.html` file in the `public`
directory in `/src/main/java/resources`.

After the application has successfully connected to the database, Glacier
will start to listen on the specified IP address and port. Glacier is now
ready to be used.

## Contributing

All contributions are welcomed and appreciated.

#### Bug Reports

Please use the [issue tracker](https://github.com/Marc3842h/Glacier/issues) to
report any bugs or file feature requests.

#### Developing

Pull Requests are welcome. I suggest using IntelliJ IDEA as IDE for this
project, but you're free to choose whatever you want.

## License

Glacier is licensed under the [MIT License][3]. Please visit the `LICENSE.txt`
file in the root directory tree for more information.

External resources as well the logo listed at the top of this page have been
credited in the `CREDIT.txt` file in the `resources` directory. 

[0]: https://travis-ci.org/Marc3842h/Glacier
[1]: https://libraries.io/github/Marc3842h/Glacier
[2]: https://marcsteiner.me/
[3]: LICENSE.txt
