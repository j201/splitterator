(ns splitterator.processing
  (:use (splitterator fft
                      utils
                      wav
                      [complex :as complex])))

; returns a pair of ffts
(defn attempt [student source-fft]
  nil)

(defn root-square-diff [a b]
  (Math/sqrt (- (* a a)
                (* b b))))

; lower score is better
(defn score [attempt-ffts actual-ffts]
  (prn "scoring...")
  (reduce + (mapcat (fn [vec1 vec2]
                      (map root-square-diff
                           (map complex/magnitude vec1)
                           (map complex/magnitude vec2)))
                    attempt-ffts actual-ffts)))

(defn train [student combined-path split-paths effect]
  nil)

(defn apply-filter [flt data]
  (let [width (/ (count data) 2)
        filter-length (count flt)
        filter-vec (vec flt)]
    (map-indexed
      (fn [index point]
        (let [filter-index (min (dec filter-length) (int (* filter-length (/ (Math/abs (- index width))
                                                                            width))))]
          (complex/scale (filter-vec filter-index)
                         point)))
      data)))

(defn random-filter [n variance]
  (map +
       (repeatedly #(* (dec (* 2 (rand)))))
       (repeat n 1)))

(defn monte-carlo [iters source-fft actual-ffts]
  (let [n (count source-fft)]
    (max-by #(- (score [(apply-filter % source-fft)
                     (apply-filter (map / %) source-fft)]
                    actual-ffts))
            (take iters
                  (repeatedly #(random-filter 20 0.5))))))

(defn run-monte-carlo [iters source-path actual-paths output-path]
  (let [source-fft (fft (map #(complex/cplx % 0) (read-mono-wav source-path)))
        actual-ffts (map (fn [path]
                           (fft (map #(complex/cplx % 0) (read-mono-wav path))))
                         actual-paths)
        fft-filter (monte-carlo iters source-fft actual-ffts)]
    (write-mono-wav output-path (normalize 0.9 (map :re (ifft (apply-filter fft-filter source-fft)))))))
