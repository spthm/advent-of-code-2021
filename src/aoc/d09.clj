(ns aoc.d09
  (:require [aoc.util :as u]))

(defn parse [s]
  (->> (slurp s) u/parse-intgrid))

(defn get-height [heights yx]
  (get-in heights yx 9)) ; border of 9s for grid

(defn adjacents [heights yx]
  (let [h (get-height heights yx)]
    (->> (u/adjacents-4 yx)
         (filterv
          (fn [yx]
            (let [h' (get-height heights yx)]
              ;; Since we always start from a low-point, (> h' h) stops us from
              ;; going back down hill. It's just for efficiency.
              (and (not= h' 9) (> h' h))))))))

(defn low-points [heights]
  (->> (u/yx-coords heights)
       (filter
        (fn [yx]
          (let[h (get-height heights yx)]
           (every? #(> (get-height heights %) h) (u/adjacents-4 yx)))))))

(defn part-1 [heights]
  (->> heights
       low-points
       (map #(get-height heights %))
       (map inc)
       (reduce +)))

(defn part-2 [heights]
  (->> heights
       low-points
       (map #(u/dfs (partial adjacents heights) %))
       (map count)
       (sort >)
       (take 3)
       (reduce *)))

(comment
  (part-1 (parse "inputs/d09/input"))

  (part-2 (parse "inputs/d09/input")))

