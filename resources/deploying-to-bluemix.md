Recently I've been dabbling in Clojure a bit and decided to use it to write my own blogging engine (which is running this blog now). I deployed it to [Bluemix](https://console.ng.bluemix.net/), IBM's cloud foundry PaaS, and since I couldn't find any useful documentation on how to deploy a Clojure/[Pedestal](https://github.com/pedestal/pedestal) app to Bluemix or cloud foundry, I thought I'd write it up.

## Create the pedestal app

If you haven't already, create a Pedestal application with Leiningen:

```nohighlight
lein new pedestal-service the-next-big-server-side-thing
```

## Listen on the right port

Cloud foundry apps have to bind to the port provided in the `VCAP_APP_PORT` environment variable. Otherwise, cloud foundry won't know how to talk to the app and will report that it failed to start.

Edit `service.clj` and replace 

```clojure
::bootstrap/port 8080})
```

with

```clojure
::bootstrap/port (Integer. 
                   (let [port (System/getenv "VCAP_APP_PORT")]
                     (or port "8080") ))})
```

## Add a manifest

Add a `manifest.yml` file to your project directory:

```nohighlight
---
applications:
- name: application-name
  memory: 512M
  host: host-name
  stack: cflinuxfs2
  instances: 1
  path: .
  buildpack: https://github.com/heroku/heroku-buildpack-clojure.git
```

This tells cloud foundry to use a custom buildpack for Clojure and the `cflinuxfs2` stack, which gives us Ubuntu 14.04 instead of the default 10.04. Can you believe Bluemix is still using 10.04 by default? [That's not even supported anymore!](https://wiki.ubuntu.com/Releases) If you're using a different cloud foundry installation, names for stacks will probably be different. You can get a list of available stacks with `cf stacks`.

## Login and push the app

```nohighlight
cf login
cf push
```

... and go to `host-name.ng.mybluemix.net` to see the hello world message. 



