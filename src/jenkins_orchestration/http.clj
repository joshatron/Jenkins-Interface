(ns jenkins-orchestration.http
  (:require [clj-http.client :as client])
  (:require [cheshire.core :as json])
  (:require [jenkins-orchestration.filter :as jfilter]))

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
  (map (fn [param] {:name (:name param)
          :value (:value (:defaultParameterValue param))
          :type (case (:type param)
                  "StringParameterDefinition" "string")})
       (:parameterDefinitions (first (:property body)))))

(defn- trigger-build-for-url
  "Trigger a build on the server with parameters"
  [base-url username token parameters]
  (client/post (str base-url "/build")
               {:basic-auth [username token]
                :form-params {:json (json/generate-string {:parameter (map #({:name (:name %1)
                                                                              :value (:value %1)})
                                                                           parameters)})}}))

(defn- get-children-for-url
  "Get the urls of all the child jobs in a job"
  [base-url username token]
  (map :url
       (:jobs (json/parse-string
                (:body (client/get
                         (str base-url "/api/json")
                         {:basic-auth [username token]}))
                true))))

(defn get-children
  "Get the urls of all the children jobs in a job"
  [base-url servers]
  (let [server (jfilter/get-server-for-url servers base-url)]
    (get-children-for-url base-url (:username server) (:token server))))

(defn create-job
  "Create a job from the url, name, and tags"
  [base-url servers tags]
  (let [server (jfilter/get-server-for-url servers base-url)
        body (get-job-body base-url (:username server) (:token server))]
    {:title (:name body)
     :url base-url
     :tags tags
     :parameters (get-parameters-from-body body)}))

(defn create-jobs-from-children
  "Creates jobs from the children of a folder"
  [base-url servers tags]
  (map #(create-job %1 servers tags) (get-children base-url servers)))

(defn trigger-build
  "Trigger a build on the specified job"
  [job servers new-params]
  (let [server (jfilter/get-server-for-job servers job)]
    (trigger-build-for-url (:url job) (:username server) (:token server) (:parameters (jfilter/inject-parameters job new-params)))))

(defn trigger-builds
  "Trigger a build on all of the specified jobs"
  [jobs servers new-params]
  (doseq [job jobs] (trigger-build job servers new-params)))

(defn get-last-finished-durations
  "Get last duration of jobs specified"
  [jobs servers]
  (map #(:duration (let [server (jfilter/get-server-for-url servers (:url %1))]
                     (get-job-body (str (:url %1) "/" (:number (:lastCompletedBuild (get-job-body (:url %1) (:username server) (:token server))))) (:username server) (:token server)))) jobs)) 
