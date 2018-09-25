(ns main
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :as test]
            [clojure.data.json :as json]))

(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

(def ok       (partial response 200))

(def echo
  {:name :echo
   :enter
   (fn [context]
     (let [request (:request context)
           response (ok context)]
       (assoc context :response response)))})

(def routes
  (route/expand-routes
   #{["/cities"          :get echo :route-name :list-query-form]
     ["/cities/:city-id" :get echo :route-name :list-view]}))

(def service-map
  {::http/routes routes
   ::http/type   :jetty
   ::http/port   8890})

(defn start []
  (http/start (http/create-server service-map)))

;; For interactive development
(defonce server (atom nil))

(defn start-dev []
  (reset! server
          (http/start (http/create-server
                       (assoc service-map
                              ::http/join? false)))))

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))