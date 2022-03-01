(ns aoc.d14
  (:require [clojure.string :as str]))

(defn parse-rules [s]
  (->> s
       (re-seq #"(\w+) -> (\w)")
       (map
        (fn [[_ pair product]]
          [pair [(str (first pair) product)
                 (str product (second pair))]]))
       (into {})))

(defn parse [s]
  (let [[template rules] (str/split (slurp s) #"\n\n")]
    [(parse-rules rules), template]))

(defn pairs [template]
  (->> template (partition 2 1) (map str/join)))

(defn step [rules pair-counts]
  (reduce-kv
   (fn [counts pair n]
     (let [[p q] (rules pair)] (merge-with + counts {p n, q n})))
   {}
   pair-counts))

(defn simulate [n rules pair-counts]
  (nth (iterate #(step rules %) pair-counts) n))

(defn element-counts [n rules template]
  (->> (frequencies (pairs template))
       (simulate n rules)
       ;; To compute the number of each element, simply take the first element
       ;; from each pair. This will under-count the last element of the input
       ;; by one, so we initialize with that.
       (reduce-kv
        (fn [counts pair n] (merge-with + counts {(first pair) n}))
        {(last template) 1})))

(defn part-* [n rules template]
  (->> (element-counts n rules template)
       vals
       (apply (juxt max min))
       (apply -)))

(defn part-1 [[rules template]]
  (part-* 10 rules template))

(defn part-2 [[rules template]]
  (part-* 40 rules template))

(comment
  (part-1 (parse "inputs/d14/input"))

  (part-2 (parse "inputs/d14/input")))
