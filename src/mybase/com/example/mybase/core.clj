(ns com.example.mybase.core
  (:require [com.example.mycomponent.interface :as mycomponent]
            [taoensso.timbre :as log])
  (:gen-class))

;; A stand alone base example. Change to the right type of base.
(defn -main [& args]
  (log/info "1 + 2 is" (mycomponent/add-two 1))
  (println "Hello world!"))
