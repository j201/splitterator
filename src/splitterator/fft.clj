(ns splitterator.fft
  (:import Fft.Fft)
  (:use [splitterator.complex :as complex]
        splitterator.utils))

; data is a seq of cplx
(defn fft [data]
  (let [re-arr (double-array (map :re data))
        im-arr (double-array (map :im data))]
    (do
      (Fft/transform re-arr im-arr)
      (map complex/cplx re-arr im-arr))))

(defn ifft [data]
  (let [N (count data)]
    (map complex/swap
         (fft (map (fn [cplx]
                     (complex/swap (map-vals #(/ % N) cplx)))
                   data)))))
