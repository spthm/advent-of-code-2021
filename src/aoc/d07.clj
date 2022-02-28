(ns aoc.d07
  (:require [aoc.util :as u]))

;; This problem can be simplified significantly with a bit of analysis.
;;
;; For part one it's trivial: the geometric median of a set of points
;; on one dimension is their median.

;; For part two, it's possible to (very) tightly bound the search space.
;; Take the crab positions for N crabs to be 'ci', and the target
;; horizontal position as 'h'. Note that the fuel cost for each crab is
;; the |ci - h|-th triangle number. The cost function over all crabs is
;; therefore,
;;   C = (1/2) * SUM(|ci - h|^2 + |ci - h|)
;; and we want to minimize this cost, i.e.
;;   dC/dh ~ SUM(2(ci - h) + |ci - h| / (ci - h)) = 0
;; Rearranging,
;;   h = (1/N) * SUM(ci) +  (1/2) * (1/N) * SUM(|ci - h| / (ci - h))
;; The absolute value here means we can't analytically solve for h, but
;; noting |ci - h| / (ci - h) = +/-1 we can compute some bounds:
;;   -N <= SUM(|ci - h| / (ci - h)) <= N,
;; (Since the derivative of |x| is not defined at x = 0, it's not
;; immediately clear that this applies for ci = h. But, the derivative
;; is -1 one side of the discontinuity and +1 the other, so we can
;; "reasonably" say that, while undefined, the derivative is in [-1, 1],
;; and the bounds above still hold. It seems to work, anyway...)
;; This can be rewritten as,
;;   -(1/2) <= (1/2) * (1/N) * SUM(|ci - h| / (ci - h)) <= (1/2)
;; Plugging this into our expression above for h at dC/dh = 0, we find
;;   h = (1/N) * SUM(ci) + [-(1/2), (1/2)]
;; That is, h is within +/-(1/2) of the mean of the ci. The challenge
;; limits us to integer values of h. Since the value of C always grows
;; as h moves away from its minimal value, we further constrain h to
;; being either the floor or ceiling of the mean.

(defn parse [s]
  (u/parse-ints (slurp s)))

(defn cost-1 [crabs x]
  (reduce + (map
             #(Math/abs (- % x))
             crabs)))

(defn cost-2 [crabs x]
  (reduce + (map
             #(u/triangle-number (Math/abs (- % x)))
             crabs)))

(defn part-1 [crabs]
  (cost-1 crabs (u/median crabs)))

(defn part-2 [crabs]
  (let [mu (u/mean crabs)]
    (min
     (cost-2 crabs (u/floor mu))
     (cost-2 crabs (u/ceil mu)))))

(comment
  (part-1 (parse "inputs/d07/input"))

  (part-2 (parse "inputs/d07/input")))
