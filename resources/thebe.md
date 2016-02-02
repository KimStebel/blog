The year is 2016. The web is entirely occupied by interactive content. Everywhere, users can create, edit, comment, share, interact. Everywhere? Well, not entirely... One small village still holds out against the invasion. With few exceptions, technical documentation, reference material, and tutorials still work like good old books. Often, the only interactive element is a search box.

It's not that documentation couldn't benefit from being more like an application than a book. Let's say you're writing about your favourite web framework that will finally make everything simple and elegant, you're documenting the library you're developing or your web service. Wouldn't it be great if your readers could try out all the good stuff you're writing about immediately? If they could run your example code and - more importantly - tinker and experiment with it without even having to leave your web<strike>site</strike>app?

In this post I'll talk about how to use Jupyter notebooks and Thebe, a new frontend for Jupyter, to make technical documentation more interactive. First, you need some kind of execution backend that runs the code for you. It should execute the code in a controlled, isolated environment, yet it should be flexible and extensible enough to run whatever you fancy. Jupyter notebooks do just that. Jupyter runs in [Docker](https://www.docker.com/) containers and even has a multiuser solution called [tmpnb](https://github.com/jupyter/tmpnb), which spins up a new container for each user. Jupyter's frontend however is rather clunky and can't be embedded into other pages very well. This is where [Thebe](https://github.com/oreillymedia/thebe/tree/master/static) comes in.

Thebe is an alternative frontend for Jupyter notebooks that you can easily embed into your website. All it needs is the URL of a Jupyter backend to execute code on and some code to execute.

Once you have it set up, you'll get something like this:

```javascript
var Cloudant = require("cloudant");
var examples = Cloudant({account: 'examples'});
var animaldb = examples.db.use('animaldb');
var res = animaldb.get('zebra', function(err, data){console.log(data);});
```

When you click the run button, Thebe talks to tmpnb to spin up a new Docker image. Tmpnb spins up the image and sends back a URL pointing to it. Thebe then sends a request to execute the code, the Jupyter server forwards the request to the node.js kernel for execution and replies with the output.

While all the pieces of this solution are available as open source, putting them together is still a bit of work. You will need a Docker image that runs Jupyter, and an instance of tmpnb and [configurable http proxy](https://github.com/jupyter/configurable-http-proxy).
   
## Jupyter

While there are images available from the Jupyter project, you will probably want to extend them to add other programming languages or libraries. You can use [jupyter/notebook](jupyter/notebook) directly to get started and then add to it by creating your own `Dockerfile` with `FROM jupyter/notebook`. For example, I'm using [this Dockerfile](https://github.com/KimStebel/thebe-demo/blob/master/notebook/Dockerfile).

User `docker run` to start the notebook server:

```shell
docker run \
  --net=host # share networking with the host
  jupyter/notebook \
  jupyter notebook \
    --no-browser \ # don't open a browser, since we're in a docker container
    --NotebookApp.base_url=/ \ # base URL can be / as nothing else is served from this server
    --ip=0.0.0.0 \ # allow access from any network interface
    --port 8000 \ # on port 8000
    --NotebookApp.allow_origin='*' #and enable CORS for any origin, you might want to change this later ;)
```



## Thebe

To install Thebe, currently the best option seems to be compiling it from source. You'll need a few tools...

```shell
sudo apt-get install docker.io nodejs-legacy npm #or whatever your OS of choice uses to install those
sudo npm install -g bower
sudo npm install -g requirejs
sudo npm install -g coffee-script
```

And then you can compile it as documented in the [readme](https://github.com/oreillymedia/thebe/blob/master/README.md).

```shell
bower install
coffee -cbm .
cd static
r.js -o build.js baseUrl=. name=almond include=main out=main-built.js
```

There is a CDN link in the readme too, but that doesn't seem to work at the moment.

Then you embed Thebe into your website. You will also need JQuery.

```html
<script src="//code.jquery.com/jquery-2.1.4.min.js" type="text/javascript" charset=\"utf-8"></script>
<script src="//example.com/thebe/static/main-built.js" type="text/javascript" charset="utf-8"></script>
```

To start Thebe, you pass it a configuration object. `url` is the URL of your Jupyter server, it will be replaced with the URL or your tmpnb server later. `selector` is the css selector for the code snippets you want to run. `tmpnb_mode` is set to false, since we're using a single Notebook server. It will be set to true later. The kernel name names the kernel used to run the code snippets. You can currently only have one instance of Thebe running one Kernel per page. Finally, `image_name` is the name of your docker image.

```html
<script>
  $(function() {
    new Thebe({
      url: 'https://localhost:8000',
      selector: 'code.javascript',
      tmpnb_mode: false,
      kernel_name: 'javascript',
      debug: true,
      image_name: 'jupyter/notebook'
    });
  });
</script>
```

With this configuration, Thebe will use the notebook server running locally. While that's fine for development and testing, to run Thebe on a website with more than one user, we'll need to use tmpnb to give every user their own notebook instance. I'll talk more about tmpnb, deployment options and what kind of load it can handle in my next blog post.






