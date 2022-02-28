(ns aoc.d08
  (:require [clojure.string :as str])
  (:require [aoc.util :as u]))

;; More pen-and-paper to simplify this one. The strings for digits
;; 1, 4, 7 and 8 can be immediately identified, because they have a
;; unique number of segments (2, 4, 3 and 7, respectively).
;; With the digits 4, 7 and 8 known, we can identify all other digits,
;; because their segments intersect with those of 4, 7 and 8 in unique
;; ways. For example, digit 3 intersects with 4 three times, with 7
;; three times, and with 8 five times (3 3 5). Digit 9 intersects with
;; 4 four times, with 7 three times, and with 8 six times (4 3 6).

(defn parse [s]
  (->> (slurp s)
       (#(re-seq #"[a-g]+" %))
       (partition 14) ; 10 digit patterns + 4 displayed digits
       (mapv #(split-at 10 %))))

(defn invariants [patterns]
  (let [{[one] 2 [four] 4 [seven] 3 [eight] 7} (group-by count patterns)]
    {:one one :four four :seven seven :eight eight}))

(defn decode-segments [keys segments]
  (let [four (u/n-intersections segments (:four keys))
        seven (u/n-intersections segments (:seven keys))
        eight (u/n-intersections segments (:eight keys))]
    (case (list four seven eight)
      ((3 3 6)) "0"
      ((2 2 2)) "1"
      ((2 2 5)) "2"
      ((3 3 5)) "3"
      ((4 2 4)) "4"
      ((3 2 5)) "5"
      ((3 2 6)) "6"
      ((2 3 3)) "7"
      ((4 3 7)) "8"
      ((4 3 6)) "9")))

(defn decode-display [[patterns display]]
  (let [keys (invariants patterns)]
    (->> display
         (map #(decode-segments keys %))
         str/join
         u/parse-int)))

(defn part-1 [entries]
  (->> (map second entries)
       flatten
       (map count)
       (filter #{2 3 4 7})
       count))

(defn part-2 [entries]
  (reduce + (map decode-display entries)))

(comment
  (part-1 (parse "inputs/d08/input"))

  (part-2 (parse "inputs/d08/input")))
