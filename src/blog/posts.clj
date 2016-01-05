(ns blog.posts
  (:require [markdown.core :as md]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as html]))

(def posts
  [{
    :title "Deploying a Pedestal application to Bluemix"
    :id "deploying-a-pedestal-application-to-bluemix"
    :body (-> "deploying-to-bluemix.md" io/resource io/file slurp md/md-to-html-string)}])