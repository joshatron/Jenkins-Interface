(ns jenkins-orchestration.http
  (:require [clj-http.client :as client])
  (:require [cheshire.core :as json :refer :all]))


(defn- get-job-info
  "Get info about the job from the server"
  [base-url username token]
  (json/parse-string
    (:body (client/get
             (str base-url "/api/json")
             {:basic-auth [username token]}))
    true))

(defn- trigger-build
  "Trigger a build on the server with parameters"
  [base-url username token parameters]
  (client/post (str base-url "/build")
               {:basic-auth [username token]
                :form-params {:json (json/generate-string {:parameter (map #({:name (:name %1)
                                                                              :value (:value %1)})
                                                                           parameters)})}}))
