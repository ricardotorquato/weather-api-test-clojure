(ns libs
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]))

(defn cities 
  ([]
   (json/read-str
    (slurp (io/resource "data/city_list.json")) :key-fn keyword))
  ([key value]
   (first (filter #(= (key %) value) (cities)))))

;; reading weather list file
(defn weather 
  ([]
    (json/read-str
      (slurp (io/resource "data/weather_list.json")) :key-fn keyword))
  ([key value]
    (first (filter #(= (key %) value) (weather)))))