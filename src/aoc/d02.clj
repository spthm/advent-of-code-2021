(ns aoc.d02
  (:require [clojure.string :as str])
  (:require [aoc.util :as u]))

(defn parse [s]
  (->> (slurp s)
       str/split-lines
       (map #(str/split % #" "))
       (map (fn [[cmd s]] [cmd (u/parse-int s)]))))

(defn dive [commands]
  ;; note that aim, h and d have different meanings here for part 1 vs part 2.
  (reduce (fn [[aim h d] [cmd x]]
            (case cmd
              "down" [(+ aim x) h d]
              "up" [(- aim x) h d]
              "forward" [aim (+ h x) (+ d (* aim x))]))
          [0 0 0]
          commands))

(defn part-1 [commands]
  (let [[d, h, _] (dive commands)]
    (* h d)))

(defn part-2 [commands]
  (let [[_, h, d] (dive commands)]
    (* h d)))

(comment
  (part-1 (parse "inputs/d02/input"))

  (part-2 (parse "inputs/d02/input")))
