# Deploying a pedestal application to Bluemix

## Create the pedestal app

```
lein new pedestal-service the-next-big-server-side-thing
```

## Make it use `VCAP_APP_PORT` as the server port

Bluemix apps have to bind to the port provided in the `VCAP_APP_PORT` environment variable. Otherwise, Bluemix won't know how to talk to the app and will report that the app failed to start.

Edit `service.clj` and replace 

```
::bootstrap/port 8080})
```

with

```
::bootstrap/port (Integer. 
                                 (let [port (System/getenv "VCAP_APP_PORT")]
                                   (or port "8000") ))})
```

### Add a manifest

Add a `manifest.yml` file to your project directory. It should look like this:

```
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

This is all fairly standard, except that we have to tell Bluemix to use a custom buildpack and the `cflinuxfs2` stack, which gives us Ubuntu 14.04 as the base instead of the default 10.04. You can list the available stacks with `cf stacks`.

### Add a `Procfile`

The `Procfile` tells Bluemix what to run. In our case, it's very simple:

```
web: lein run
```

### Login to Bluemix and push the app

Login and push...

```
cf login
cf push
```

... and go to `host-name.ng.mybluemix.net` to see the hello world message. 



