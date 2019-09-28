(ns jenkins-orchestration.http
  (:require [clj-http.client :as client])
  (:require [cheshire.core :as json :refer :all]))


(defn get-job-info
  "Get info about the job from the server"
  [server job]
  (json/parse-string
    (:body (client/get
             (str (:url job) "/api/json")
             {:basic-auth [(:username server) (:token server)]}))
    true))
