(ns blog.feed
  (:require [markdown.core :as md]
            [blog.posts :as posts]
            [clj-rss.core :as rss]
            [ring.util.response :as ring-resp]
            [blog.common :as common]))

(defn feed-handler [req]
  (-> 
    (ring-resp/response
      (let [items (map (fn [post] {:title (:title post)
                                   :guid (posts/post-url post)
                                   :link (posts/post-url post)
                                   :description (str "<![CDATA[ " (posts/content-html post) " ]]>")})
                       posts/posts)]
        (rss/channel-xml {:title "Kim Stebel's Blog"
                          :link (str common/base-url "/feed")
                          :description "A blog about functional programming, cloud stuff and other things"}
                     items)))
  (ring-resp/content-type "application/rss+xml; charset=UTF-8")))
