;; Defining namespace
(ns main
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :as test]
            controllers))

;; defining our routes
(def routes
  (route/expand-routes
   #{["/cities"          :get {:name :cities-getAll :enter (:get-all controllers/cities)}]
     ["/cities/:city-id" :get {:name :cities-getOne :enter (:get-one controllers/cities)}]}))

;; defining our service-map
(def service-map
  {::http/routes routes
   ::http/type   :jetty
   ::http/port   8890})

(defn start []
  (http/start (http/create-server service-map)))