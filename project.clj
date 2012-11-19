(defproject tbc-node-example "0.1.0-SNAPSHOT"
  :description "Example usage of the To Be Continued library with Node.js"
  :url "https://github.com/gregspurrier/tbc-node-example"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [to-be-continued "0.1.0"]]
  :plugins [[lein-cljsbuild "0.2.9"]]
  :cljsbuild {:builds [{:source-path "src/cljs"
                        :compiler {:target :nodejs
                                   :optimizations :simple
                                   :output-to "lib/tbc-node-example.js"}}]})
