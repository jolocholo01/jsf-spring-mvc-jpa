
[https://github.com/jolocholo01/spring-mvc-jetty-jsf](https://github.com/jolocholo01/spring-mvc-jetty-jsf)

Vou deixar aqui as ações que foram executadas na aula e os comandos também.

### Instalação do Ruby

Na aula foi utilizado o comando:

```shell
$ sudo apt-get install ruby-full
```

Consulte o link:

[https://www.ruby-lang.org/pt/documentation/installation/](https://www.ruby-lang.org/pt/documentation/installation/)

... para diferentes sistemas operacionais.

### Instalação do Heroku CLI

Na aula foi utilizado o comando:

```shell
$ wget -O- https://toolbelt.heroku.com/install-ubuntu.sh | sh
```
Consulte o link:

[https://devcenter.heroku.com/articles/heroku-cli](https://devcenter.heroku.com/articles/heroku-cli)

... para diferentes sistemas operacionais.

## Comandos utilizados na aula

```shell
$ git clone https://github.com/jolocholo01/spring-mvc-jetty-jsf.git

$ cd /videoaula-jsf-hospedagem

$ mvn clean package

$ heroku local

$ heroku login

$ heroku create videoaula-jsf-hospedagem

$ heroku addons:create heroku-postgresql:hobby-dev

$ heroku config

$ heroku config:set JDBC_URL="...";

$ heroku config:set JDBC_USER="...";

$ heroku config:set JDBC_PASSWORD="..."

$ git push heroku master

$ heroku open

$ heroku logs --tail

$ heroku ps

$ heroku ps:scale web=1

$ heroku dyno:type web=hobby # Somente se já tiver cartão configurado.

$ heroku destroy --app videoaula-jsf-hospedagem
```
