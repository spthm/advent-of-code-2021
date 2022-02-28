(ns aoc.d13
  (:require [clojure.string :as str])
  (:require [aoc.util :as u]))

(defn parse-dots [s]
  (->> (u/parse-ints s)
       (partition 2)
       ;; All the utils code is for [y x] coords.
       (map (fn [[x y]] [y x]))))

(defn parse-folds [s]
  (->> (re-seq #"([y|x])=(\d+)" s)
       (map
        (fn [[_ yx value]] {:axis (case yx "y" 0 "x" 1)
                            :value (u/parse-int value)}))))

(defn parse [s]
  (let [s (slurp s)
        [dots folds] (str/split s #"\n\n")]
    {:dots (parse-dots dots)
     :folds (parse-folds folds)}))

(defn fold [dots {:keys [axis value]}]
  (->> dots
       (map
        (fn [yx]
          ;; Folds never appear in the left-hand or upper half of the paper,
          ;; so we don't need to worry about negative results here.
          (update yx axis #(- value (Math/abs (- % value))))))
       set))

(defn part-1 [{:keys [dots folds]}]
  (count (fold dots (first folds))))

(defn part-2 [{:keys [dots folds]}]
  (u/plot-points (reduce fold dots folds)))

(comment
  (part-1 (parse "inputs/d13/input"))

  (part-2 (parse "inputs/d13/input")))
