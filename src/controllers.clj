(ns controllers
  (:require [clojure.data.json :as json]
            libs,
            http))

(def cities 
  {:getAll (fn [context] (assoc context :response (http/ok (json/write-str (libs/cities)))))
   :getOne (fn [context]
             (let [city-id (read-string (http/path :city-id context))]
              (assoc context :response (http/ok (json/write-str (libs/cities city-id))))))})
