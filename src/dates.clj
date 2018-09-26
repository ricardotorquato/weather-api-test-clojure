(ns dates 
  (:require [clj-time.core :as time]
            [clj-time.coerce :as tc]
            [clojure.string :as string]))

(defn to-date-time [str]
  (try (apply time/date-time (map #(Integer/parseInt %) (string/split str #"-")))
    (catch Exception e nil)))

(defn to-timestamp [str]
  (try (/ (tc/to-long (to-date-time str)) 1000)
    (catch Exception e nil)))