(ns com.example.mybase.core-test
  (:require [clojure.test :refer :all]
            [com.example.mybase.core :as core]))

;; Add tests here...
(deftest hello-world-example-test
  (let [output (with-out-str (core/-main))]
    (is (re-seq #"INFO .* - 1 \+ 2 is 3" output))
    (is (re-seq #"Hello world!" output))))
