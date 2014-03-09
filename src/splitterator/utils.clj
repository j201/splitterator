(ns splitterator.utils)

(defn max-by [f sq]
  (when (seq sq)
    (loop [best (first sq)
           left (rest sq)
           best-score (f best)]
      (if (seq left)
        (let [score (f (first left))]
          (if (> score best-score)
            (recur (first left) (rest left) score)
            (recur best (rest left) best-score)))
        best))))

(defn normalize [max-val sq]
  (map (partial * (/ max-val (apply max (map #(Math/abs %) sq))))
       sq))

(defn map-vals [f m]
  (into {}
        (for [[k v] m]
          [k (f v)])))
