(ns splitterator.fft
  (:import Fft.Fft)
  (:use [splitterator.complex :as complex]))

; data is a seq of cplx
(defn fft [data]
  (let [re-arr (double-array (map :re data))
        im-arr (double-array (map :im data))]
    (do
      (Fft/transform re-arr im-arr)
      (map complex/cplx re-arr im-arr))))

(defn ifft [data]
  (fft (map complex/swap data)))
