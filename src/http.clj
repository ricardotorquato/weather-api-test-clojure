(ns http)

;; Defining function response that sent status, body and headers
(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

;; Defining ok function as as partial of response
(def ok (partial response 200))

;; return a path key from context
(defn path [key context]
  (key (:path-params (:request context))))

(defn query [key context]
  (key (:query-params (:request context))))