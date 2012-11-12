(ns tbc-demo.github
  (:require [tbc-demo.https :as https]
            [clojure.string :as str])
  (:require-macros [to-be-continued.macros :as tbc]))

(defn- github-url
  [& path-segments]
  (->> (str/join "/" path-segments)
      (str "https://api.github.com/")))


(defn user-profile
  [username k]
  (tbc/-+-> (github-url "users" username)
            (https/fetch-json-resource ...)
            k))

(defn user-followers
  [username k]
  (tbc/-+-> (github-url "users" username "followers")
            (https/fetch-json-resource ...)
            k))

(defn user-followees
  [username k]
  (tbc/-+-> (github-url "users" username "following")
            (https/fetch-json-resource ...)
            k))

(defn user-projects
  [username k]
  (tbc/-+-> (github-url "users" username "repos")
            (https/fetch-json-resource ...)
            k))

(defn project-collaborators
  [name k]
  (tbc/-+-> (github-url "repos" name "collaborators")
            (https/fetch-json-resource ...)
            k))