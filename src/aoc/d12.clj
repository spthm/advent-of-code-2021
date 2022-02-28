(ns aoc.d12
  (:require [clojure.string :as str])
  (:require [aoc.util :as u]))

(defn parse [s]
  (->> (slurp s)
       str/split-lines
       (map #(re-seq #"\w+" %))
       (reduce u/add-bi-edge {})
       ;; The problem doesn't allow us to return to the starting cave.
       ;; It's also slightly faster if we convert the adjacent nodes
       ;; from sets to vecs.
       (u/map-val
        (fn [adjs] (vec (disj adjs "start"))))))

(defn big? [s]
  (= s (str/upper-case s)))

(def count-paths
  (memoize
   (fn [graph cave visited patience]
     (reduce
      (fn [n cave']
        (+ n
           (cond
             (= "end" cave') 1
             (big? cave') (count-paths graph cave' visited patience)
             (not (visited cave')) (count-paths graph cave' (conj visited cave') patience)
             ;; cave is small and has been visited before - should we visit it again?
             (pos? patience) (count-paths graph cave' visited (dec patience))
             :else 0)))
      0
      (graph cave)))))

(defn part-1 [graph]
  (count-paths graph "start" #{} 0))

(defn part-2 [graph]
  (count-paths graph "start" #{} 1))

(comment
  (part-1 (parse "inputs/d12/input"))

  (part-2 (parse "inputs/d12/input")))
