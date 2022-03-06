(ns aoc.d17
  (:require [aoc.util :as u]))

;; The equations of motion for this problem are similar to the usual
;; solution given a constant acceleration (here, ax = ay = -1) and
;; initial velocity v0,
;;
;;   y(t) = vy0*t + (1/2)*t - (1/2)*t^2
;;   x(t) = vx0*t + (1/2)*t - (1/2)*t^2   for t <= vx0
;;   x(t) = (vx0/2) * (vx0 + 1)           for t >= vx0
;;
;; If there as a vx0 such that vx = 0 for xl <= x <= xh, we can always
;; hit the target zone's x position. That's the case for my input, and
;; appears to be the case for all inputs. Assume also that the target
;; is entirely below the y = 0 line. Again, this hold for my input, and
;; seems to hold for all inputs.
;;
;; Thus, after launching with some positive velocity vy0, vy proceeds at
;; each step as,
;;
;;   vy0, vy0 - 1, vy0 - 2, ..., 0, -1, -2, ..., -vy0, -vy0 - 1, -vy0 - 2, ...
;;
;; We must of course return to y = 0 with a velocity vy = -vy0 at some
;; future step n. To ensure we do not overshoot the target zone on the
;; next step, we require
;;
;;   vy(n + 1) = (-vy0 - 1) >= yl  (remember, yl is negative!)
;; or,
;;
;;   vy0 <= -(yl + 1).
;;
;; To compute y_max (part one), we therefore need to calculate the
;; maximum height attained given vy0 = (-yl + 1). This is reached
;; when vy = 0. From the above sequence vy0, vy0 - 1, vy0 - 2 ...
;; we then infer that y_max is reached at t = vy0. Using our above
;; equation for y(t) and taking vy0 = -(yl + 1), we find,
;;
;;   y_max = y(t=vy0) = (1/2) * vy0 * (vy0 + 1)
;;                    = (1/2) * yl * (yl + 1)
;;
;; To narrow the search space for part two, we start by again noting
;; that vy0 = -(yl + 1) is the maximum vy0 for which a valid solution
;; can exist. If we invert y(t) = yl for this value of vy0, we find
;; t_max, the maximum time within which a solution can exist.
;;
;; We can further narrow the search space of vy0 values for a given t.
;; We must have yl <= y(t) <= yh, so vy0 bounds are found by solving
;; for vy0 in y(t) = yl and y(t) = yh.
;;
;; We can similarly narrow the search space of vx0 values for a given t.
;; To solve for vx0 in x(t) = xl and x(t) = xh, we need to know which x(t)
;; equation applies. Now, x(t) reaches its maximal (and final!) value at
;; t = vx0. So, we first solve for vx0 _assuming_ t >= vx0. If t is greater
;; than or equal to this value, then indeed we are in the t >= vx0 regime,
;; and have found vx0. Otherwise, we are in the t < vx0 regime, and must
;; solve x(t) for vx0 given x and t.

(defn parse [s]
  (->> (slurp s)
       (re-seq #"-?\d+")
       (mapv u/parse-int)))

(defn t
  "Find t given a distance and initial velocity."
  [v0 s]
  ;; Invert y(t) (or x(t)) and take the positive-valued solution.
  (+ 1/2 v0 (Math/sqrt (+ 1/4
                          (* v0 (+ v0 1))
                          (- (* 2 s))))))

(defn v0
  "Return v0 given x or y and t, or given only x and assuming the x(t) >= vx0 regime."
  ([x]
   (+ -1/2 (Math/sqrt (+ 1/4 (* 2 x)))))
  ([s t]
   (/ (+ (* t (- t 1))
         (* 2 s))
      (* 2 t))))

(defn t-bounds
  "Return [low, high) bounds on t given yl."
  [yl]
  (let [vy0 (- (+ yl 1))]
    [1
     (inc (int (Math/floor (t vy0 yl))))]))

;; Since we need to land within the target and are limited to
;; integer values, we can round-up the lower bound and round-down
;; the upper bound.
(defn vy0-bounds
  "Return [low, high) bounds on vy0 given [minimum, maximum] y values and a time."
  [yl yh t]
  [(int (Math/ceil (v0 yl t)))
   (inc (int (Math/floor (v0 yh t))))])

(defn vx0-bounds
  "Return [low, high) bounds on vx0 given [minimum, maximum] x values and a time."
  [xl xh t]
  (let [vx0l (v0 xl)
        vx0h (v0 xh)]
    [(int (Math/ceil (if (< t vx0l) (v0 xl t) vx0l)))
     (inc (int (Math/floor (if (< t vx0h) (v0 xh t) vx0h))))]))

(defn part-1 [[_ _ yl _]]
  (/ (* yl (+ yl 1))
     2))

(defn part-2 [[xl xh yl yh]]
  (->> (for [t (apply range (t-bounds yl))
             vx (apply range (vx0-bounds xl xh t))
             vy (apply range (vy0-bounds yl yh t))]
         [vx vy])
       set
       count))

(comment
  (part-1 (parse "inputs/d17/input"))

  (part-2 (parse "inputs/d17/input")))
