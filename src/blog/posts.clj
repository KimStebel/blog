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
    :description "Recently I've been dabbling in Clojure a bit and decided to use it to write my own blogging engine (which is running this blog now). I deployed it to Bluemix, IBM's cloud foundry PaaS, and since I couldn't find any useful documentation on how to deploy a Clojure/Pedestal app to Bluemix or cloud foundry, I thought I'd write it up."
    :filename "deploying-to-bluemix.md"
    :thebe false}
   ;{
   ; :title "Thebe"
   ; :id "thebe"
   ; :date "2016-1-13"
   ; :description ""
   ; :filename "thebe.md"
   ; :thebe true}
   ])
