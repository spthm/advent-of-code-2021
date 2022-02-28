(ns aoc.d01
  (:require [clojure.string :as str])
  (:require [aoc.util :as u]))

(defn parse [s]
  (->> (slurp s)
       str/split-lines
       (map u/parse-int)))

(defn part-1 [depths]
  (->> depths
       (partition 2 1)
       (filter (fn [[a b]] (> b a)))
       count))

(defn part-2 [depths]
  (part-1 (->> depths
               (partition 3 1)
               (map u/sum))))

(comment
  (part-1 (parse "inputs/d01/input"))

  (part-2 (parse "inputs/d01/input")))
