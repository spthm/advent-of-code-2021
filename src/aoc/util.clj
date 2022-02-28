(ns aoc.util
  (:require [clojure.set :as set])
  (:require [clojure.string :as str]))

;;
;; Parsing
;;

(defn parse-int
  "Parse s into an Int"
  [s]
  (Integer/parseInt s))

(defn parse-long
  "Parse s into a Long"
  [s]
  (Long/parseLong s))

(defn parse-binary
  "Parse a string of 0s and 1s as an Int."
  [s]
  (Integer/parseInt s 2))

(defn parse-ints
  "Return a vector of all ints in the string s."
  [s]
  (->> (re-seq #"\d+" s)
       (mapv parse-int)))

;;
;; Collection operations
;;

(defn argmax
  "Return the key in m for which (k m) is maximized."
  [m]
  (key (apply max-key val m)))

(defn argmin
  "Return the key in m for which (k m) is minimized,"
  [m]
  (key (apply min-key val m)))

(defn filter-count
  "Count the number of items in coll after filtering with pred."
  [pred coll]
  (count (filter pred coll)))

(defn indices
  "Returns a lazy sequence of the indices in coll for which pred is true."
  [pred coll]
  (keep-indexed (fn [i v] (when (pred v) i)) coll))

(defn map-max
  "Return the maximum value of f applied to each element of coll."
  [f coll]
  (apply max (map f coll)))

(defn map-val
  "Returns a new Map, with the same keys as m, and f applied to all its values."
  [f m]
  (into (empty m) (for [[k v] m] [k (f v)])))

;;
;; Maths
;;

(defn ceil
  "Return the ceiling of x, as an int."
  [x]
  (int (Math/ceil x)))

(defn floor
  "Return the floor of x, as an int."
  [x]
  (int (Math/floor x)))

(defn triangle-number
  "Return the nth triangle number."
  [n]
  (/ (* n (inc n)) 2))

(defn median
  "Return the median of values in xs."
  [xs]
  (let [xs' (sort xs)
        n (count xs)
        k (quot n 2)
        mid (nth xs' k)]
    (if (even? n)
      (/ (+ mid (nth xs' (dec k))) 2)
      mid)))

(defn mean
  "Return the mean of values in xs."
  [xs]
  (/ (reduce + xs) (count xs)))

(defn sum
  "Return sum of elements of coll."
  [coll]
  (reduce + coll))

;;
;; Matrix ops
;;

(defn n-col
  "Return the number of columns of xs.
   
   Specifically, return the number of items in the first item of xs."
  [xs]
  (count (first xs)))

(defn get-col
  "Return the ith column of xs.
   
   Specifically, a vector of the ith item of each item of xs."
  [xs, i]
  (mapv #(nth % i) xs))

;;
;; Misc.
;;

(defn n-intersections
  "Return the number of intersecting items of the collections"
  [c1 c2]
  (count (set/intersection (set c1) (set c2))))

(defn inclusive-range
  "Return a range from a to b, inclusive.
   
   If a > b, the range goes from a to b in steps of -1."
  [a b]
  (if (> a b)
    (range a (dec b) -1)
    (range a (inc b))))

;;
;; 2D grid operations
;;

(defn yx-coords
  "Yield a lazy sequence of [y x] vectors for each item in rows, or
   for the given height and width.
   
   Assumes rows represents a 2D grid, and that each item in rows
   has the same length."
  ([rows]
   (let [height (count rows) width (count (first rows))]
     (yx-coords height width)))
  ([height width]
   (for [y (range height) x (range width)]
     [y x])))

;; todo: makes sense to (optionally) pass the regex for re-seq into this
(defn parse-intgrid
  "Parse a string of unpspaced ints as a grid, returning a vector of vectors.
   
   E.g.,
     (parse-intgrid '123\n456\n789') -> [[1 2 3] [4 5 6] [7 8 9]]"
  [s]
  (->> (str/split-lines s)
       (mapv
        (fn [s']
          (->> s'
               (re-seq #"\d") ; different from parse-ints
               (mapv parse-int))))))

(defn adjacents-4
  "Return the cardinal (N, S, E, W) adjacents of a point [y x]."
  [[y x]]
  [[(inc y) x]
   [(dec y) x]
   [y (inc x)]
   [y (dec x)]])

(defn adjacents-8
  "Return the grid adjacents of a point [y x], including diagonals."
  [[y x]]
  [[(dec y) (dec x)]
   [(dec y) x]
   [(dec y) (inc x)]
   [y (dec x)]
   [y (inc x)]
   [(inc y) (dec x)]
   [(inc y) x]
   [(inc y) (inc x)]])

(defn i->yx
  "Return a function mapping an index i for a vector [y x] for a grid of width."
  [width]
  (fn [i] [(quot i width) (mod i width)]))

(defn yx->i
  "Return a function mapping a vector [y x] to an index i for a grid of width."
  [width]
  (fn [[y x]] (+ (* width y) x)))

;; todo: this shouldn't assume the top-left is (0, 0).
(defn plot-points
  "Plot a set of [y x] points as a printable string.
   
   Assumes [0 0] is the top-left of the plotting area"
  [points]
  (let [height (inc (map-max first points))
        width (inc (map-max second points))]
    (->> (yx-coords height width)
         (map #(if (contains? points %) "█" " "))
         (partition width)
         (map str/join)
         (str/join "\n")
         (str "\n")))) ; prepend a newline; looks nicer when running with CLI.

;;
;; Graph operations
;;

(defn add-bi-edge
  "Add two edges (v1->v2, v2->v1) to a bidirectional graph.
   
   Values of the graph are assumed to be Sets."
  [graph [v1 v2]]
  (merge-with set/union
              graph
              {v1 #{v2}}
              {v2 #{v1}}))

(defn dfs
  "Run a depth-first search of a graph, returning a set of all visited nodes.
   
   graph must return the adjacent nodes of 'node', (graph node)."
  [graph root]
  (loop [stack [root]
         visited #{}]
    (if (empty? stack)
      visited
      (let [node (peek stack)
            stack (pop stack)]
        (if (contains? visited node)
          (recur stack visited)
          (recur (into stack (graph node)) (conj visited node)))))))
