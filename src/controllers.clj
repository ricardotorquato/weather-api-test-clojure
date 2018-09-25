(ns controllers
  (:require [clojure.data.json :as json]
            libs,
            http))

(defn to-json [response] 
  (json/write-str response))

(def cities 
  {:getAll (fn [context] (assoc context :response (http/ok (to-json (libs/cities)))))
   :getOne (fn [context]
             (let [city-id (read-string (:city-id (:path-params (:request context))))]
              (assoc context :response (http/ok (to-json (libs/cities city-id))))))})