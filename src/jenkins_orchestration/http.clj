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

(defn- get-job-build-body
  "Get info about the job from the server"
  [base-url build username token]
  (json/parse-string
    (:body (client/get
             (str base-url "/" build "/api/json")
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
                :form-params {:json (json/generate-string {:parameter (map #({:name (:name %)
                                                                              :value (:value %)})
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

(defn- get-children
  "Get the urls of all the children jobs in a job"
  [base-url config]
  (let [server (jfilter/get-server-for-url (:servers config) base-url)]
    (get-children-for-url base-url (:username server) (:token server))))

(defn add-job
  "Create a job from the url, name, and tags"
  [base-url config tags]
  (assoc config :jobs (concat (:jobs config)
                              (let [server (jfilter/get-server-for-url (:servers config) base-url)
                                    body (get-job-body base-url (:username server) (:token server))]
                                {:title (:name body)
                                 :url base-url
                                 :tags tags
                                 :parameters (get-parameters-from-body body)}))))

(defn add-jobs-from-children
  "Creates jobs from the children of a folder"
  [base-url config tags]
  (map #(add-job % (:servers config) tags) (get-children base-url (:servers config))))

(defn trigger-build
  "Trigger a build on the specified job"
  [job config new-params]
  (let [server (jfilter/get-server-for-job (:servers config) job)]
    (trigger-build-for-url (:url job) (:username server) (:token server) (:parameters (jfilter/inject-parameters job new-params)))))

(defn trigger-builds
  "Trigger a build on all of the specified jobs"
  [config new-params]
  (doseq [job (:jobs config)] (trigger-build job config new-params)))

(defn get-last-build
  "Get the last build number for a job. For build type, use from-
  :lastBuild, :lastCompletedBuild, :lastFailedBuild, :lastStableBuild, :lastSuccessfulBuild, :lastUnstableBuild, or :lastUnsuccessfulBuild"
  [job config build-type]
  (let [server (jfilter/get-server-for-url (:servers config) (:url job))]
    (:number (get (get-job-body (:url job) (:username server) (:token server)) build-type))))
