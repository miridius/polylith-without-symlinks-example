(ns com.example.mycomponent.interface
  (:require [com.example.mycomponent.core :as core]))

;; delegate to the implementations...
(defn add-two [x]
  (core/add-two x))
