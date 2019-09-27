(ns jenkins-orchestration.core
  (:require [clj-http.client :as client])
  (:require [cheshire.core :as json :refer :all])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;; Test structure

(def servers [
              {:name "main"
               :url "http://localhost:8080"
               :username "joshatron"
               :token "116020aa468bc4510f74abf93399f19a8f"}])

(def jobs [
           {:title      "Calc Mac"
            :server "main"
            :url        "http://localhost:8080/job/UI/job/Calc_Mac"
            :tags       ["mac" "calc"]
            :parameters [{
                          :name  "env"
                          :type  "string"
                          :value "e2e"}
                         {
                          :name  "module"
                          :type  "string"
                          :value "all"}]}
           {:title      "Calc Win"
            :server "main"
            :url        "http://localhost:8080/job/UI/job/Calc_Win"
            :tags       ["win" "calc"]
            :parameters [{
                          :name  "env"
                          :type  "string"
                          :value "e2e"}
                         {
                          :name  "module"
                          :type  "string"
                          :value "all"}]}
           {:title      "Calc Linux"
            :server "main"
            :url        "http://localhost:8080/job/UI/job/Calc_Linux"
            :tags       ["linux" "calc"]
            :parameters [{
                          :name  "env"
                          :type  "string"
                          :value "e2e"}
                         {
                          :name  "module"
                          :type  "string"
                          :value "all"}]}
           {:title      "UI Mac"
            :server "main"
            :url        "http://localhost:8080/job/UI/job/UI_Mac"
            :tags       ["mac" "ui"]
            :parameters [{
                          :name  "env"
                          :type  "string"
                          :value "e2e"}
                         {
                          :name  "module"
                          :type  "string"
                          :value "all"}]}
           {:title      "UI Win"
            :server "main"
            :url        "http://localhost:8080/job/UI/job/UI_Win"
            :tags       ["win" "ui"]
            :parameters [{
                          :name  "env"
                          :type  "string"
                          :value "e2e"}]}
           {:title      "UI Linux"
            :server "main"
            :url        "http://localhost:8080/job/UI/job/UI_Linux"
            :tags       ["linux" "ui"]
            :parameters [{
                          :name  "env"
                          :type  "string"
                          :value "e2e"}
                         {
                          :name  "submodule"
                          :type  "string"
                          :value ""}]}])

(def params [
             {:name  "env"
              :value "dev"}
             {:name  "submodule"
              :value "first"}])

(defn filter-tag
  "Filter out all the jobs that don't contain the specified tag"
  [jobs tag]
  (filter (fn [job] (some #(= tag %1) (:tags job))) jobs))

(defn filter-tags
  "Filter out all the jobs that don't contain all the specified tags"
  [jobs tags]
  (reduce filter-tag jobs tags))

(defn inject-parameters
  "Inject parameters into the job, overriding default values if they exist.
  This will not add new parameters that aren't defined in the job"
  [job new-params]
  ;;apply for each replacing parameter
  (reduce (fn [job new-param]
            ;;override job parameters
            (assoc job :parameters
                       ;;replacing old values with new
                       (map (fn [old-param]
                              (if (= (:name old-param) (:name new-param))
                                (assoc old-param :value (:value new-param))
                                old-param))
                            (:parameters job))))
          job new-params))

(defn get-server-for-job
  "Get the corresponding server for a job"
  [servers job]
  (first (filter #(= (:server job) (:name %1)) servers)))

(defn get-job-info
  "Get info about the job from the server"
  [servers job]
  (let [server (get-server-for-job servers job)]
    (json/parse-string
      (:body (client/get
               (str (:url job) "/api/json")
               {:basic-auth [(:username server) (:token server)]})))))