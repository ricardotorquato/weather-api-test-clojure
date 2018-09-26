(ns controllers
  (:require [clojure.data.json :as json]
            libs,
            http))

(defn get-city [city-id]
  (libs/cities :id city-id))

(defn get-city-with-weather [city-id]
  (let [city    (get-city city-id)
        weather (:data (libs/weather :cityId city-id))]
    (assoc city :weather weather)))

(def cities 
  {:get-all (fn [context] (assoc context :response (http/ok (json/write-str (libs/cities)))))
   :get-one (fn [context]
             (if-let [city-id (read-string (http/path :city-id context))]
               (if (= (read-string (http/query :with-weather context)) 1)
                 (if-let [city-with-weather (get-city-with-weather city-id)]
                   (assoc context :response (http/ok (json/write-str city-with-weather)))
                   context)
                 (if-let [city (get-city city-id)]
                   (assoc context :response (http/ok (json/write-str city)))
                   context))
               context))})