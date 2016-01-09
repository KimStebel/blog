(ns blog.posts
  (:require [markdown.core :as md]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as html]
            [blog.common :as common]))

(def base-url (str common/base-url "/posts"))

(defn post-url [post] (str base-url "/" (:id post)))

(defn content-html [post]
  (str "<h1>" (:title post) "</h1><span class=\"date\">published " (:date post) "</span>"
       (-> (:filename post) io/resource io/file slurp md/md-to-html-string)))

(def posts
  [{
    :title "Deploying a Pedestal application to Bluemix"
    :id "deploying-a-pedestal-application-to-bluemix"
    :date "2015-12-20"
    :tags "pedestal clojure bluemix cf cloud foundry"
    :filename "deploying-to-bluemix.md"}])