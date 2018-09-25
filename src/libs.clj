(ns libs
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]))

(defn read-cities 
  ([]
   (json/read-str
    (slurp (io/resource "data/city_list.json")) :key-fn keyword))
  ([id]
   (first (filter #(= (:id %) id) (read-cities)))))

;; reading weather list file
(defn read-weather []
  (json/read-str
   (slurp (io/resource "data/weather_list.json")) :key-fn keyword))