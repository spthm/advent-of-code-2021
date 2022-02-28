(ns aoc.d11
  (:require [aoc.util :as u]))

(defn parse [s]
  (->> (slurp s)
       (re-seq #"\d")
       (mapv u/parse-int)))

;; todo: memoize?
(def i->yx (u/i->yx 10))
(def yx->i (u/yx->i 10))

(def adjacents
  (memoize
   (fn [i]
     (let [yx (i->yx i)]
       (->> (u/adjacents-8 yx)
            (filter
             (fn [[y x]] (and (<= 0 y 9) (<= 0 x 9))))
            (mapv             
             (fn [yx] (yx->i yx))))))))

(defn flashing? [v] (> v 9))

(defn flash-at [octos i]
  (reduce
   (fn [octos' adj]
     (update octos' adj #(if (zero? %) 0 (inc %))))
   (assoc octos i 0)
   (adjacents i)))

(defn flash [octos]
  ;; might be faster to maintain a set of unflashed octopi and loop
  ;; over only those?
  (if-let [flashing (seq (u/indices flashing? octos))]
    (recur (reduce flash-at octos flashing))
    octos))

(defn step [octos]
  (->> octos (mapv inc) flash))

(defn part-1 [octos]
  (reduce
   (fn [n octos'] (+ n
                     (u/filter-count zero? octos')))
   0
   (take 101 (iterate step octos))))

(defn part-2 [octos]
  (ffirst
   (drop-while
    (fn [[_ octos']] (some #(not= 0 %) octos'))
    (map-indexed vector (iterate step octos)))))

(comment
  (part-1 (parse "inputs/d11/input"))

  (part-2 (parse "inputs/d11/input")))
