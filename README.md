# Blog

## Getting Started

1. Start the application: `lein run-dev`
2. Go to [localhost:8080](http://localhost:8080/)
3. Read the source code at src/blog/service.clj. Explore the docs of functions
   that define routes and responses.
4. Run the tests with `lein test`. Read the tests at test/blog/service_test.clj.

`lein run-dev` automatically detects code changes. Alternatively, you can run in production mode with `lein run`.

## Configuration

To configure logging see config/logback.xml. By default, the app logs to stdout and logs/.
To learn more about configuring Logback, read its [documentation](http://logback.qos.ch/documentation.html).


## REPL

To start the server from the repl:

```
(def dev-serv (run-dev))
```

To reload (parts of) the server after code changes:

```
(require 'blog.service :reload)
```
