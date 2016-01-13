# Embedding executable, editable code examples into your blog

Let's say you're blogging about your favourite up-and-coming programming
language or the newest greatest web framework that will finally make everything
simple and elegant. Wouldn't it be cool if your readers could try out all the
good stuff you're writing about? If they could run your example code right from
the browser, make their own changes, and run it again? 

Sadly, unless you're writing about client side Javascript, that is quite
difficult to do. You need some kind of execution backend that runs the code for
you. It should execute the code in a controlled, isolated environment, yet it
should be flexible and extensible enough to run whatever you fancy. Luckily,
that already exists in the form of Jupyter notebooks. Jupyter runs in
[Docker](https://www.docker.com/) containers and even has a multiuser solution
called [tmpnb](https://github.com/jupyter/tmpnb), which spins up a new
container for each user of your site. Jupyter also provides an API so that the
execution backend can be used with a new frontend. This is where
[Thebe](https://github.com/oreillymedia/thebe/tree/master/static) comes in.

Thebe is an alternative frontend for Jupyter notebooks that you can easily
embed into your website. All it needs is the URL of a tmpnb backend to execute
code on and the css selector for your code examples. Let's get all of that
set up!

First, you'll need to install the Docker daemon. That will allow us to both
run individual notebook servers as well as tmpnb. Once you've installed Docker,
spinning up a notebook server is easy, as the Jupyter project already provides
a lot of Docker images for us to use. 

```javascript
var Cloudant = require("cloudant");
var examples = Cloudant({account: 'examples'});
var animaldb = examples.db.use('animaldb');
var res = animaldb.get('zebra', function(err, data){console.log(data);});
```

