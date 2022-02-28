(ns aoc.d06
  (:require [aoc.util :as u]))

(defn parse [s]
  (u/parse-ints (slurp s)))

(defn timer-counts [timers]
  (let [counts (frequencies timers)]
    (mapv #(get counts % 0) (range 9))))

;; Number of fish with a timer value transitions
;; as
;;  #@0       -> #@8
;;  #@1       -> #@0
;;  #@2       -> #@1
;;  ...
;;  #@6       -> #@5
;;  #@7 + #@0 -> #@6
;;  #@8       -> #@7
;; which is a simple roll of a vector representing the
;; number of counts, plus an update of the count @6.

(defn roll [v]
  (conj (subvec v 1) (v 0)))

(defn step [counts]
  (-> counts
      roll
      (update 6 + (counts 0))))

(defn part-* [counts days]
  (reduce + (nth (iterate step counts) days)))

(defn part-1 [timers]
  (part-* (timer-counts timers) 80))

(defn part-2 [timers]
  (part-* (timer-counts timers) 256))

(comment
  (part-1 (parse "inputs/d06/input"))

  (part-2 (parse "inputs/d06/input")))
