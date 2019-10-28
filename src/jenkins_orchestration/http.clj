(ns jenkins-orchestration.http
  (:require [clj-http.client :as client])
  (:require [cheshire.core :as json])
  (:require [jenkins-orchestration.filter :as jfilter]))

(defn get-job-info-for-url
  "Gets general info for a job"
  [base-url username token]
  (let [body (json/parse-string (:body (client/get (str base-url "/api/json") {:basic-auth [username token]})) true)]
    {:name (:name body)
     :fullName (:fullName body)
     :url (:url body)
     :color (:color body)
     :health (:score (first (:healthReport body)))
     :in-queue (:inQueue body)
     :oldest (:number (:firstBuild body))
     :latest (:number (:lastBuild body))
     :latest-completed (:number (:lastCompletedBuild body))
     :latest-failed (:number (:lastFailedBuild body))
     :latest-stable (:number (:lastStableBuild body))
     :latest-successful (:number (:lastSuccessfulBuild body))
     :latest-unstable (:number (:lastUnstableBuild body))
     :latest-unsuccessful (:number (:lastUnsuccessfulBuild body))
     :next (:nextBuildNumber body)
     :parameters (map (fn [param] {:name (:name param)
                                   :value (:value (:defaultParameterValue param))
                                   :type (case (:type param)
                                           "StringParameterDefinition" "string")})
                      (:parameterDefinitions (first (:property body))))}))

(defn get-job-build-info-for-url
  "Gets information on a job build"
  [job-url build username token]
  (let [body (json/parse-string (:body (client/get (str job-url "/" build "/api/json") {:basic-auth [username token]})) true)]
    {:parameters (map (fn [param] {:name (:name param)
                                   :value (:value (:defaultParameterValue param))
                                   :type (case (:type param)
                                           "StringParameterDefinition" "string")})
                      (:parameters (first (:actions body))))
     :artifacts (:artifacts body)
     :running (:building body)
     :duration (:duration body)
     :estimated-duration (:estimatedDuration body)
     :build (:number body)
     :result (:result body)
     :timestamp (:timestamp body)}))

(defn trigger-build-for-url-and-parameters
  "Trigger a build on the server with parameters"
  [base-url username token parameters]
  (client/post (str base-url "/build")
               {:basic-auth [username token]
                :form-params {:json (json/generate-string {:parameter (map #({:name (:name %)
                                                                              :value (:value %)})
                                                                           parameters)})}}))

(defn get-children-for-url
  "Get the urls of all the child jobs in a job"
  [base-url username token]
  (map :url
       (:jobs (json/parse-string
                (:body (client/get
                         (str base-url "/api/json")
                         {:basic-auth [username token]}))
                true))))

(defn add-job
  "Create a job from the url, name, and tags (tag can be vector or function which is given the job body)"
  [base-url config tags]
  (assoc config :jobs (concat (:jobs config)
                              (let [server (jfilter/get-server-for-url (:servers config) base-url)
                                    info (get-job-info-for-url base-url (:username server) (:token server))]
                                {:title (:name info)
                                 :url base-url
                                 :tags (cond
                                         (fn? tags) (tags info)
                                         :else tags)
                                 :parameters (:parameters info)}))))

(defn get-children
  "Get the urls of all the children jobs in a job"
  [base-url config]
  (let [server (jfilter/get-server-for-url (:servers config) base-url)]
    (get-children-for-url base-url (:username server) (:token server))))

(defn add-jobs-from-children
  "Creates jobs from the children of a folder"
  [base-url config tags]
  (map #(add-job % (:servers config) tags) (get-children base-url (:servers config))))

(defn trigger-build
  "Trigger a build on the specified job"
  [job config new-params]
  (let [server (jfilter/get-server-for-job (:servers config) job)]
    (trigger-build-for-url-and-parameters (:url job) (:username server) (:token server) (:parameters (jfilter/inject-parameters job new-params)))))

(defn trigger-builds
  "Trigger a build on all of the specified jobs"
  [config new-params]
  (doseq [job (:jobs config)] (trigger-build job config new-params)))

(defn get-job-infos
  "Grabs the info for the specified jobs"
  [config]
  (map #((let [server (jfilter/get-server-for-job (:servers config) %)]
           (get-job-info-for-url (:url %) (:username server) (:token server))))
       (:jobs config)))
