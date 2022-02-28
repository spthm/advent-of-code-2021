(ns aoc.d04
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [aoc.util :as u]))

(defn board [xs]
  {:rows (map #(set %) xs)
   :cols (map #(set (u/get-col xs %)) (range (u/n-col xs)))})

(defn board-uncalled [{:keys [rows cols]}]
  (reduce set/union (concat rows cols)))

(defn board-score [n board]
  (* n (reduce + (board-uncalled board))))

(defn parse-board [s]
  (->> s
       (str/split-lines)
       (map u/parse-ints)
       board))

(defn parse [s]
  (let [[calls & boards] (str/split (slurp s) #"\n\n")]
    {:calls (u/parse-ints calls)
     :boards (map parse-board boards)}))

(defn call [n {:keys [rows cols]}]
  {:rows (map #(disj % n) rows)
   :cols (map #(disj % n) cols)})

(defn bingo? [{:keys [rows cols]}]
  (boolean (some empty? (concat rows cols))))

(defn play
  [boards [n & ns]]
  (when n
    (let [{winners true rest false} (group-by bingo? (map #(call n %) boards))]
      (if (seq winners)
        (lazy-cat (map #(board-score n %) winners) (play rest ns))
        (play rest ns)))))

(defn part-1 [{:keys [boards calls]}]
  (first (play boards calls)))

(defn part-2 [{:keys [boards calls]}]
  (last (play boards calls)))

(comment
  (part-1 (parse "inputs/d04/input"))

  (part-2 (parse "inputs/d04/input")))
