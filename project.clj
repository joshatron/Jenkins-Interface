(defproject jenkins_orchestration "0.1.0-SNAPSHOT"
  :description "Tool for orchestrating jenkins builds"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.10.0"]
                 [cheshire "5.9.0"]]
  :main ^:skip-aot jenkins-orchestration.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
