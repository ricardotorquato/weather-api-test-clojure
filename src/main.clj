;; Defining namespace
(ns main
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :as test]
            [clojure.data.json :as json]
            [clojure.java.io :as io]))

;; Defining function response that sent status, body and headers
(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

;; Defining ok function as as partial of response
(def ok       (partial response 200))

;; reading cities list file
(defn read-cities []
  (json/read-str 
    (slurp (io/resource "data/city_list.json")) :key-fn keyword))

;; reading weather list file
(defn read-weather []
  (json/read-str
   (slurp (io/resource "data/weather_list.json")) :key-fn keyword))

;; filter function
(defn filter-all [value field data]
  (filter #(= value (field %) data)))

;; defining our response of all cities
;; it uses a map that needs a name and an enter function
;; the enter function get the cities from read-cities
;; then parse that to json (again)
;; and then define the response to be sent with the ok function
;; finally insert the response to the context and that is the return
(def respond-cities
  {:name :respond-cities
   :enter
   (fn [context]
     (let [body (read-cities)
           json-response (json/write-str body)
           response (ok json-response)]
       (assoc context :response response)))})

;; defining our response of one city
;; it also uses the same structure of map with a name and an enter function
;; in this enter function we start verifying if we have a city-id from path-params from request from context
;; the if-let has two params: success and failure
;; in the success we have another if let
;; in the second if-let we verify if we have the city-id inside the cities from read-cities
;; to verify that we use a filter function
;; the filter function receive the function that will verify as first param and the collection as second param
;; the #() define an annonymous function that will receive a param (%) and will get :id key from this param %
;; and then it will verify if this :id key is equal to city-id
;; and of course we use this filter function as param of first function because the filter will return a collection
;; and we just want to get the first item
;; as the failure of the both if-let we return the context itself
;; by default it comes with 404 status code of not-found, so we don't need to worry about saying that
;;
;; It's not working yet because of the city-id inside the annonymous function
;; If we change to a value, it works :/
(def respond-city
  {:name :respond-city
   :enter
   (fn [context]
     (if-let [city-id (:city-id (:path-params (:request context)))]
       (if-let [city (first (filter #(= (:id %) city-id) (main/read-cities)))]
        (let [json-response (json/write-str city)
              response (ok json-response)]
          (assoc context :response response))
        context)
       context))})

;; defining our routes
(def routes
  (route/expand-routes
   #{["/cities"          :get respond-cities]
     ["/cities/:city-id" :get respond-city]}))

;; defining our service-map
(def service-map
  {::http/routes routes
   ::http/type   :jetty
   ::http/port   8890})

(defn start []
  (http/start (http/create-server service-map)))