(ns particle-chamber.core-test
  (:require [clojure.test :refer :all]
            [particle-chamber.core :refer :all]))

(deftest test-read-init-state
  (testing "Reading initial state from test case"
    (is (=
         (read-init-state [2 "R..L"])
         '([0 \R 4 2] [1 \. 4 2] [2 \. 4 2] [3 \L 4 2])))))
