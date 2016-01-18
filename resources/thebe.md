The year is 2016. The web is entirely occupied by interactive content. Everywhere,
users can create, edit, comment, share, interact. Everywhere? Well, not
entirely... One small village still holds out against the invasion. Technical
documentation, references, and tutorials still work like good old books. Often,
the only interactive element is a search box - hardly an improvement over printed
indexes.

It's not that documentation couldn't benefit from being more like an app than
a book. Let's say you're writing about your favourite web framework that will
finally make everything simple and elegant or you're documenting the library
you're developing. Wouldn't it be great if your readers could try out all the
good stuff you're writing about immediately? If they could run your example code
and - more importantly - tinker and experiment with it without even having to
leave your web<strike>site</strike>app?

Sadly, unless you're writing about client side Javascript, that is quite
difficult to do. You need some kind of execution backend that runs the code for
you. It should execute the code in a controlled, isolated environment, yet it
should be flexible and extensible enough to run whatever you fancy. Luckily,
that already exists in the form of Jupyter notebooks. Jupyter runs in
[Docker](https://www.docker.com/) containers and even has a multiuser solution
called [tmpnb](https://github.com/jupyter/tmpnb), which spins up a new
container for each user. Jupyter's frontend however is rather clunky and can't
be embedded into other pages very well. This is where
[Thebe](https://github.com/oreillymedia/thebe/tree/master/static) comes in.

Thebe is an alternative frontend for Jupyter notebooks that you can easily
embed into your website. All it needs is the URL of a tmpnb backend to execute
code on and the css selector for your code examples.

Once you have all of this set up, you'll get something like this:

```javascript
var Cloudant = require("cloudant");
var examples = Cloudant({account: 'examples'});
var animaldb = examples.db.use('animaldb');
var res = animaldb.get('zebra', function(err, data){console.log(data);});
```

When you click the run button, thebe talks to tmpnb to spin up a new docker
image. Tmpnb spins up the image and sends back a URL pointing to it. Thebe
then sends a request to execute the code, the Jupyter server forwards the
request to the javascript/node.js kernel for execution and sends the
output back to Thebe.

While all the bits of pieces of this solution are freely available, putting
them together is a bit of work.

1. You will need a Docker image that runs Jupyter.
   While there are images available from the Jupyter project, you will
   probably want to extend them to add other programming languages or
   libraries.
2. You need to configure and run tmpnb and 
   [configurable http proxy](https://github.com/jupyter/configurable-http-proxy).
3. You need to set up Thebe.

## 1. Tmpnb and configurable-http-proxy

## 2. Jupyter

## 3. Thebe 




