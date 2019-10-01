(ns jenkins-orchestration.core
  (:require [cheshire.core :as json])
  (:require [jenkins-orchestration.filter :as jfilter])
  (:require [jenkins-orchestration.http :as jhttp])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;; Test structure
(def config (json/parse-stream (clojure.java.io/reader "./config.json") true))

(def params [
             {:name  "env"
              :value "dev"}
             {:name  "submodule"
              :value "first"}])
