(ns splitterator.complex)

(defn cplx [re im]
  {:re re, :im im})

(defn swap [cplx]
  {:re (:im cplx), :im (:re cplx)})

(defn- sqr [x] (* x x))

(defn magnitude [cplx]
  (Math/sqrt (+ (sqr (:re cplx))
                (sqr (:im cplx)))))

(defn to-polar [cplx]
  {:r (magnitude cplx)
   :theta (Math/atan2 (:im cplx) (:re cplx))})

(defn from-polar [polar]
  {:re (* (:r polar) (Math/cos (:theta polar)))
   :im (* (:r polar) (Math/sin (:theta polar)))})

(defn scale [factor cplx]
  {:re (* factor (:re cplx))
   :im (* factor (:im cplx))})
