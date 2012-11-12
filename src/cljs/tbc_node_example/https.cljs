(ns tbc-node-example.https
  (:require [cljs.nodejs :as node])
  (:require-macros [to-be-continued.macros :as tbc]))

(def node-https
  (node/require "https"))

(defn issue-https-get
  "Initiate an HTTPS GET request for the provided url. The resulting
response stream will be passed to k when it is ready."
  [url k]
  (.get node-https url k))

(defn read-response
  "Read to the end of the response stream. When complete, a map
containing its :status, :headers, and :body will be passed to k."
  [stream k]
  (let [body (atom [])]
    (doto stream
      (.setEncoding "utf8")
      (.on "data" (fn [data] (swap! body conj data)))
      (.on "end"  (fn [] (k {:status (.-statusCode stream)
                             :headers (js->clj (.-headers stream))
                             :body (apply str @body)}))))))

(defn parse-json
  "Parse a JSON string and return it as a Clojure data structure with
any keys converted to keywords."
  [s]
  (-> s
      JSON/parse
      (js->clj :keywordize-keys true)))

(defn fetch-json-resource
  "Fetch the JSON resource from url, parse it, and pass the resulting
Clojure data structure to k."
  [url k]
  (tbc/-+-> url
            (issue-https-get ...)
            (read-response ...)
            :body
            parse-json
            k))
