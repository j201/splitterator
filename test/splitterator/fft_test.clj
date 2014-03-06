(ns splitterator.fft-test
  (:require [clojure.test :refer :all]
            [splitterator.fft :as fft :refer :all]))

(deftest fft-fn
  (testing "fft exists"
   (is (not (nil? fft/fft))))
  (testing "fft takes vectors and returns vectors"
    (let [retval (fft/fft [[] []])]
      (is (vector? retval))
      (is (every? vector? retval)))))
