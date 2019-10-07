(ns jenkins-orchestration.core
  (:require [cheshire.core :as json])
  (:require [jenkins-orchestration.filter :as jfilter])
  (:require [jenkins-orchestration.http :as jhttp])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn save-config
  "Save config back to file"
  [config]
  (json/generate-stream config (clojure.java.io/writer "./config.json") {:pretty true}))

(defn add-jobs-to-config
  "Add jobs to config"
  [config jobs]
  (assoc config :jobs
                (concat (:jobs config) jobs)))
;; Test structure
(def config (json/parse-stream (clojure.java.io/reader "./config.json") true))

(def params [
             {:name  "env"
              :value "dev"}
             {:name  "submodule"
              :value "first"}])

