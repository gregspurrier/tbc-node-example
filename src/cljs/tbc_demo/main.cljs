(ns tbc-demo.main
  (:require [tbc-demo.github :as gh]
            [to-be-continued.fns :as tbc-fns]
            [clojure.string :as str])
  (:require-macros [to-be-continued.macros :as tbc]))

;; # Formatters
(defn format-user-profile
  [{:keys [login name]}]
  (str name " (" login ")\n"))

(defn format-project-summary
  [{:keys [full_name description collaborators]}]
  (str full_name " (by " (str/join "," collaborators) ")\n"
       description "\n"))

(defn format-project-summaries
  [projects]
  (str/join "\n" (map format-project-summary projects)))

(defn format-user-info
  [{:keys [profile projects]}]
  (str (format-user-profile profile)
       "---------\n"
       (format-project-summaries projects)))

;; # Data fetching and transformation
(defn add-collaborators
  [project k]
  ;; The -+->> macro is analogous to the core ->> macro, but
  ;; transparently bridges asynchronous calls. If a form ends in ...,
  ;; the ... will be replaced with a function that acts as a
  ;; continuation for the remaining forms. Thus, synchronous and
  ;; asynchronous forms can be intermingled, making the intention of
  ;; the code more evident.
  ;;
  ;; Here we asynchronously fetch the projects collaborators and then
  ;; synchronously extract their :login fields and append the
  ;; resulting seq to the project map before passing the final result
  ;; to the function's callback.
  (tbc/-+->> project 
             :full_name
             (gh/project-collaborators ...)
             (map :login)
             (assoc project :collaborators)
             k))

(defn user-project-summaries
  [username k]
  ;; The TBC variants of the threading macros enable serial
  ;; asynchronous processing. TBC also supports two mechanisms for
  ;; performing asynchronous processing in parallel. The first of
  ;; these is map-par, which maps an asynchronous function of one
  ;; argument (plus a callback) over a collection. The results are
  ;; collected into a vector as they are received. When the result
  ;; vector has been fully realized, it is passed to continuation
  ;; provided as the last argument to map-par.
  ;;
  ;; Below we use map-par to add, in parallel, a :collaborators field
  ;; to each project that belongs to the user.
  (tbc/-+->> username
             (gh/user-projects ...)
             (tbc-fns/map-par add-collaborators ...)
             k))

(defn user-summary
  [username k]
  ;; The other parallelism mechanism provided by To Be Continued is
  ;; let-par, a parallel binding form. When a bound expression ends in
  ;; ..., the ... is replaced with a callback that binds the result to
  ;; the bound var. Once all asynchronous calls are completed, the
  ;; body of the let-par expression is executed.
  ;;
  ;; Here we use let-par to fetch the user's profile and project
  ;; summaries in parallel and then perform some processing on the
  ;; result before passing to the function's callback.
  (tbc/let-par [profile (gh/user-profile username ...)
                projects (user-project-summaries username ...)]
               (tbc/-+-> {:profile profile, :projects projects}
                         format-user-info
                         k)))

(defn start [username]
  (if username
    (user-summary username println)
    (println "Please provide a GitHub username")))

(set! *main-cli-fn* start)
