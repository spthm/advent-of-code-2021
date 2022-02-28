(ns aoc.d03
  (:require [clojure.string :as str])
  (:require [aoc.util :as u]))

(defn parse [s]
  (->> (slurp s)
       str/split-lines
       (map seq)))

(defn most-common-bit [numbers, i]
  (let [counts (frequencies (u/get-col numbers i))]
    (if (= (get counts \1) (get counts \0))
      \1
      (u/argmax counts))))

(defn least-common-bit [numbers, i]
  (let [counts (frequencies (u/get-col numbers i))]
    (if (= (get counts \1) (get counts \0))
      \0
      (u/argmin counts))))

(defn gamma [numbers]
  (->> (range (u/n-col numbers))
       (map #(most-common-bit numbers %))
       str/join))

(defn invert [s]
  (str/replace s #"1|0" {"0" "1", "1" "0"}))

(defn rate [bit-selector numbers]
  (->> (range (u/n-col numbers))
       (reduce
        (fn [numbers', i]
          (let [bit (bit-selector numbers' i)]
            (filter #(= (nth % i) bit) numbers')))
        numbers)
       first ; only one element left after reduction
       str/join
       u/parse-binary))

(defn part-1 [numbers]
  (let [g (gamma numbers)
        ig (invert g)]
    (* (u/parse-binary g)
       (u/parse-binary ig))))

(defn part-2 [numbers]
  (* (rate most-common-bit numbers)
     (rate least-common-bit numbers)))

(comment
  (part-1 (parse "inputs/d03/input"))

  (part-2 (parse "inputs/d03/input")))
