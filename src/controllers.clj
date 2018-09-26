(ns controllers
  (:require [clojure.data.json :as json]
            [clojure.string :as string]
            libs
            http
            dates))

(defn get-city [city-id]
  (libs/cities :id city-id))

(defn get-city-with-weather [city-id date-start date-end]
  (let [city         (get-city city-id)
        weather-full (:data (libs/weather :cityId city-id))
        weather (if (not (and (nil? date-start) (nil? date-end)))
                  (filter #(and (>= (:dt %) date-start) (<= (:dt %) date-end)) weather-full)
                  weather-full)]
    (if (nil? city) nil (assoc city :weather weather))))

(defn get-cities-that-have-weather []
  (let [city-ids (map #(:cityId %) (libs/weather))]
    (filter #(contains? (set city-ids) (:id %)) (libs/cities))))

(def cities 
  {:get-all (fn [context] 
              (if (= (read-string (or (http/query :that-has-weather context) "0")) 1)
                (assoc context :response (http/ok (json/write-str (get-cities-that-have-weather))))
                (assoc context :response (http/ok (json/write-str (libs/cities))))))
   :get-one (fn [context]
              (if-let [city-id (read-string (http/path :city-id context))]
                (if (= (read-string (or (http/query :with-weather context) "0")) 1)
                  (let [date-start (dates/to-timestamp (http/query :date-start context))
                        date-end   (dates/to-timestamp (string/join (concat (http/query :date-end context) "-23-59-59")))]
                    (if-let [city-with-weather (get-city-with-weather city-id date-start date-end)]
                      (assoc context :response (http/ok (json/write-str city-with-weather)))
                      context))
                  (if-let [city (get-city city-id)]
                    (assoc context :response (http/ok (json/write-str city)))
                    context))
                context))})