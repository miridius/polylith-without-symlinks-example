(ns com.example.mycomponent.core-test
  (:require [clojure.test :refer :all]
            [com.example.mycomponent.interface :as mycomponent]))

;; add your tests here...
(deftest test-add-two
  (is (= 42 (mycomponent/add-two 40))))
