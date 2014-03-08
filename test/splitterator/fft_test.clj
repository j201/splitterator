(ns splitterator.fft-test
  (:require [clojure.test :refer :all]
            [splitterator.fft :as fft :refer :all]
            [splitterator.complex :as cplx :refer :all]))

(deftest fft-fn
  (is (not (nil? fft/fft)))
  (let [retval (fft/fft [(cplx/cplx 0.5 -0.4) (cplx/cplx 0 -1)])]
    (is (seq? retval))
    (is (= (count retval) 2))
    (is (every? #(= (keys %) [:re :im]) retval))))
