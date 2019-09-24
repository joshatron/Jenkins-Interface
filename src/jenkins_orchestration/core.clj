(ns jenkins-orchestration.core
  (:require [clj-http.client :as client])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;; Test structure
(def jobs [
           {:title      "Calc Mac"
            :url        "http://joshatron.io/jenkins/Calc_Mac"
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
            :url        "http://joshatron.io/jenkins/Calc_Win"
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
            :url        "http://joshatron.io/jenkins/Calc_Linux"
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
            :url        "http://joshatron.io/jenkins/UI_Mac"
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
            :url        "http://joshatron.io/jenkins/UI_Win"
            :tags       ["win" "ui"]
            :parameters [{
                          :name  "env"
                          :type  "string"
                          :value "e2e"}]}
           {:title      "UI Linux"
            :url        "http://joshatron.io/jenkins/UI_Linux"
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

(defn filter-tag [jobs tag]
  (filter (fn [job] (some #(= tag %1) (:tags job))) jobs))

(defn filter-tags [jobs tags]
  (reduce filter-tag jobs tags))

(defn replace-parameters [job new-params]
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

(defn get-job-info [job]
  (client/get (:url job)))