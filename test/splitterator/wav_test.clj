(ns splitterator.wav-test
  (:require [clojure.test :refer :all]
            [splitterator.wav :as wav :refer :all])
  (:import java.io.File))

(def wav-data (wav/read-mono-wav "resources/test.wav"))

(deftest reading
  (is (seq? wav-data))
  (is (= 5 (count wav-data)))
  (is (> 0.2 (apply max (map #(Math/abs (- %1 %2))
                             [1 0.5 0 -0.5 -1]
                             wav-data)))))

(wav/write-mono-wav wav-data "output.wav")

(deftest writing
  (is (.exists (File. "output.wav")))
  (is (= wav-data (read-mono-wav "output.wav"))))
