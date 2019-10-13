(ns jenkins-orchestration.filter
  (:require [clojure.string :as str]))

(defn filter-tag
  "Filter out all the jobs that don't contain the specified tag"
  [config tag]
  (assoc config :jobs (filter (fn [job] (some #(= tag %) (:tags job))) (:jobs config))))

(defn filter-tags
  "Filter out all the jobs that don't contain all the specified tags"
  [config tags]
  (reduce filter-tag config tags))

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
  (first (filter #(= (:server job) (:name %)) servers)))

(defn get-server-for-url
  "Get the proper server from the url of a job"
  [servers job-url]
  (first (filter #(str/starts-with? job-url (:url %)) servers)))
