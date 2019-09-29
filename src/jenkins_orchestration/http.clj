(ns jenkins-orchestration.http
  (:require [clj-http.client :as client])
  (:require [cheshire.core :as json])
  (:require [jenkins-orchestration.filter :as f]))

(defn- get-job-body
  "Get info about the job from the server"
  [base-url username token]
  (json/parse-string
    (:body (client/get
             (str base-url "/api/json")
             {:basic-auth [username token]}))
    true))

(defn- get-parameters-from-body
  "Collect a list of parameters from the body of a job"
  [body]
  (map #({:name (:name %1)
          :value (:value (:defaultParameterValue %1))
          :type (case (:type %1)
                  "StringParameterDefinition" "string")})
       (:parameterDefinitions (first (:property body)))))

(defn- trigger-build
  "Trigger a build on the server with parameters"
  [base-url username token parameters]
  (client/post (str base-url "/build")
               {:basic-auth [username token]
                :form-params {:json (json/generate-string {:parameter (map #({:name (:name %1)
                                                                              :value (:value %1)})
                                                                           parameters)})}}))

(defn- get-children
  "Get the urls of all the child jobs in a job"
  [base-url username token]
  (map #({:url (:url %1)})
       (:jobs (json/parse-string
                (:body (client/get
                         (str base-url "/api/json")
                         {:basic-auth [username token]}))))))

(defn get-children
  "Get the urls of all the children jobs in a job"
  [base-url servers]
  (let [server (f/get-server-for-url base-url servers)]
    get-children base-url (:username server) (:token server)))

(defn create-job
  "Create a job from the url, name, and tags"
  [base-url servers tags]
  (let [server (f/get-server-for-url base-url servers)
        body (get-job-body base-url (:username server) (:token server))]
    {:title (:name body)
     :url base-url
     :tags tags
     :parameters (get-parameters-from-body body)}))
