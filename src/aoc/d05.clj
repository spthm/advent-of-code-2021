(ns aoc.d05
  (:require [clojure.string :as str])
  (:require [aoc.util :as u]))

(defn parse [s]
  (->> (slurp s)
       str/split-lines
       (mapv u/parse-ints)))

(defn cardinal? [[x1 y1 x2 y2]]
  (or (= x1 x2) (= y1 y2)))

(defn diagonal-range [a b]
  (cond
    (= a b) (repeat a)
    :else (u/inclusive-range a b)))

(defn line-points [[x1 y1 x2 y2]]
  (map vector (diagonal-range x1 x2) (diagonal-range y1 y2)))

(defn overlaps [lines]
  (->> lines
       (mapcat line-points)
       frequencies
       (filter (fn ([[_ v]] (>= v 2))))
       count))

(defn part-1 [lines]
  (->> lines
       (filter cardinal?)
       overlaps))

(defn part-2 [lines]
  (->> lines overlaps))

(comment
  (part-1 (parse "inputs/d05/input"))

  (part-2 (parse "inputs/d05/input")))
