(ns aoc.d10
  (:require [clojure.string :as str])
  (:require [aoc.util :as u]))

(defn parse [s]
  (->> (slurp s) str/split-lines))

(def closer-of { \( \) \[ \] \{ \} \< \>})
(def opening? (set (keys closer-of)))

(def score-1 {\) 3 \] 57 \} 1197 \> 25137})

(defn score-2 [cs]
  (reduce
   (fn [total c] (+ (* 5 total)
                    ({\) 1 \] 2 \} 3 \> 4} c)))
   0
   cs))

(defn scan [s]
  (reduce
   (fn [stack c]
     (cond
       (opening? c) (conj stack (closer-of c))
       (= c (peek stack)) (pop stack)
       :else (reduced {:error c})))
   []
   s))

(defn part-1 [s]
  (->> s
       (map scan)
       (keep :error)
       (map score-1)
       (reduce +)))

(defn part-2 [s]
  (->> s
       (map scan)
       (remove :error)
       (map reverse)
       (map score-2)
       u/median))

(comment
  (part-1 (parse "inputs/d10/input"))

  (part-2 (parse "inputs/d10/input")))
