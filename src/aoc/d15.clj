(ns aoc.d15
  (:require [clojure.data.priority-map :refer [priority-map]])
  (:require [aoc.util :as u]))

(defn parse [s]
  (let [rows (u/parse-intgrid (slurp s))
        h (u/n-row rows)
        w (u/n-col rows)
        risks (u/flattenv rows)]
    [risks h w]))

(defn adjacents [h w [y x]]
  (->> (u/adjacents-4 [y x])
       (filter
        (fn [[y x]] (and (<= 0 y (dec h))
                         (<= 0 x (dec w)))))))

;; The dijkstra search is a bit faster if nodes in the graph are indices
;; rather than [y x] vectors.
(defn make-graph [h w]
  (let [i->yx (u/i->yx w)
        yx->i (u/yx->i w)]
    (->> (range (* w h))
         (map #(i->yx %))
         (map #(adjacents h w %))
         (mapv (fn [adjs] (mapv #(yx->i %) adjs))))))

(defn tile-risk [risk ty tx]
  (inc (mod (dec (+ risk ty tx)) 9)))

(defn tile [risks factor w h]
  (let [h' (* factor h)
        w' (* factor w)
        yx->i (u/yx->i w)]
    (->> (u/yx-coords h' w')
         (mapv
          (fn [[y' x']]
            (tile-risk (risks (yx->i [(mod y' h) (mod x' w)]))
                       (quot y' h)
                       (quot x' w)))))))

(defn update-search [frontier explored graph cost current dist]
  ;; current is always the tip of the shortest path so far, and the
  ;; cost to traverse into a node from any adjacent is the same.
  ;; Therefore, as soon as we set the frontier value for an adjacent
  ;; of current, we are certain a lower value will not be found, and
  ;; can consider that adjacent as fully explored.
  (reduce
   (fn [[frontier' explored'] adj]
     [(assoc frontier' adj (+ dist (cost adj)))
      (assoc explored' adj 1)])
   [frontier explored]
   (->> (graph current)
        (filter #(zero? (explored %))))))

;; Essentially dijkstra with a priority-queue, but short-circuits
;; the normal "if new distance less than previous distance" per
;; update-search above.
(defn search [graph cost source target]
  (loop [frontier (priority-map source 0)
         explored (assoc (u/zeros (count graph)) source 1)]
    (let [[current dist] (peek frontier)
          frontier (pop frontier)]
      (if (= current target)
        dist
        (let [[frontier explored] (update-search frontier explored graph cost current dist)]
          (recur frontier explored))))))

(defn part-1 [[risks h w]]
  (let [graph (make-graph h w)
        target (dec (count risks))]
    (search graph risks 0 target)))

(defn part-2 [[risks h w]]
  (let [risks' (tile risks 5 w h)
        h' (* 5 h)
        w' (* 5 w)
        graph' (make-graph h' w')
        target' (dec (count risks'))]
    (search graph' risks' 0 target')))

(comment
  (part-1 (parse "inputs/d15/input"))

  (part-2 (parse "inputs/d15/input")))
