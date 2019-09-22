(ns jenkins-orchestration.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;; Test structure
(def jobs [
  {:title "Calc Mac"
   :url "http://joshatron.io/jenkins/Calc_Mac"
   :tags ["mac" "calc"]}
  {:title "Calc Win"
   :url "http://joshatron.io/jenkins/Calc_Win"
   :tags ["win" "calc"]}
  {:title "Calc Linux"
   :url "http://joshatron.io/jenkins/Calc_Linux"
   :tags ["linux" "calc"]}
  {:title "UI Mac"
   :url "http://joshatron.io/jenkins/UI_Mac"
   :tags ["mac" "ui"]}
  {:title "UI Win"
   :url "http://joshatron.io/jenkins/UI_Win"
   :tags ["win" "ui"]}
  {:title "UI Linux"
   :url "http://joshatron.io/jenkins/UI_Linux"
   :tags ["linux" "ui"]}])

(defn filter-tag [jobs tag]
  (filter (fn [job] (some #(= tag %1) (:tags job))) jobs))

(defn filter-tags [jobs tags]
  (reduce filter-tag jobs tags))
