(ns blog.service
  (:require [io.pedestal.http :as bootstrap]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [ring.util.response :as ring-resp]
            [markdown.core :as md]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as html]
            [blog.posts :as posts]
            [clj-rss.core :as rss]
            [blog.feed :as feed]
            [io.pedestal.http.jetty.util :as jetty-util])
   (:import (org.eclipse.jetty.server.handler.gzip GzipHandler)))

(defn disqus [post] (str "
    var disqus_config = function () {
      //this.page.url = 'https://kim-stebel.mybluemix.net/deploying-pedestal-applications-on-bluemix'; 
      this.page.identifier = '" (:id post) "';
    };
    (function() {
      var d = document, s = d.createElement('script');
      s.src = '//kim-stebel.disqus.com/embed.js';
      s.setAttribute('data-timestamp', +new Date());
      (d.head || d.body).appendChild(s);
    })();"))

(defn posts-list [] 
  (apply str (map (fn [post] (str "<li><a href=\"/posts/" (:id post) "\">" (:title post) "</a></li>")) posts/posts)))

(html/deftemplate template "public/template.html"
  [post]
  [:title] (html/content (:title post))
  [:div.content] (html/html-content (posts/content-html post))
  [:ul.recent] (html/html-content (posts-list))
  [:script.disqus] (html/html-content (disqus post))
  [:div.thebe] (html/html-content (if (:thebe post) "<script src=\"//code.jquery.com/jquery-2.1.4.min.js\" type=\"text/javascript\" charset=\"utf-8\"></script>
 <script src=\"//storage.googleapis.com/kimstebel-public/thebe/static/main-built.js\" type=\"text/javascript\" charset=\"utf-8\"></script>
  <script>
    $(function(){
      new Thebe({
        url: 'http://185.73.38.147:8000',
				selector: 'code.javascript',
        tmpnb_mode: true,
        kernel_name: 'javascript',
				debug: true,
				image_name: 'kimstebel/thebe-demo-notebook'
      });
    });
  </script>" "<script src=\"//storage.googleapis.com/kimstebel-public/highlightjs/highlight.pack.js\"></script>
    <script>hljs.initHighlightingOnLoad();</script>"))
  [(html/attr= :name "description")] (html/set-attr :content (str (:description post))))
  
;; Some sample data
(def home-page-content (first posts/posts))

(defn post-content [req]
  (let [id (get-in req [:path-params :id])]
    (first (filter (fn [post] (= id (:id post))) posts/posts))))

(defn post-template [post]
  (apply str
    (template post)))

(defn home-page [req]
  (ring-resp/response (post-template home-page-content)))

(defn post-handler [req]
  (ring-resp/response (post-template (post-content req))))

(defroutes routes
  ;; Defines "/" and "/about" routes with their associated :get handlers.
  ;; The interceptors defined after the verb map (e.g., {:get home-page}
  ;; apply to / and its children (/about).
  [[["/" {:get home-page} ^:interceptors [(body-params/body-params) bootstrap/html-body]]
    ["/posts/:id" {:get post-handler} ^:interceptors [(body-params/body-params) bootstrap/html-body]]
    ["/feed" {:get feed/feed-handler}]]])

;; Consumed by blog.server/create-server
;; See bootstrap/default-interceptors for additional options you can configure
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::bootstrap/interceptors []
              ::bootstrap/routes routes
              
              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::bootstrap/allowed-origins ["scheme://host:port"]
              
              ;; Root for resource interceptor that is available by default.
              ::bootstrap/resource-path "/public"
              
              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ::bootstrap/type :jetty
              
              ;; Add our filter-fn a the context configurator
              ::bootstrap/container-options
                {:context-configurator (fn [c]
                                         (let [gzip-handler (GzipHandler.)]
                                           (.setGzipHandler c gzip-handler)
                                           c))}
              
              
              ;;::bootstrap/host "localhost"
              ::bootstrap/port (Integer. 
                                 (let [port (System/getenv "VCAP_APP_PORT")]
                                   (or port "8080") ))});
