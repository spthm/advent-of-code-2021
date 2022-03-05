(ns aoc.d16
  (:require [clojure.pprint :refer [cl-format]])
  (:require [clojure.string :as str])
  (:require [aoc.util :as u]))

(defn byte-str [n]
  (cl-format nil "~4,'0',B" n))

(defn hex->binary [s]
  (->> (str/trim s)
       (map (comp byte-str u/parse-hex str))
       str/join))

(defn parse [s]
  (hex->binary (slurp s)))

(defn eat-bits [n stream]
  [(subs stream 0 n) (subs stream n)])

(defn eat-int [n stream]
  (let [[bits stream] (eat-bits n stream)]
    [(u/parse-binary bits) stream]))

(declare eat-packet)

(defn eat-nbits-packets [n stream]
  (let [[bits stream] (eat-bits n stream)]
    (loop [packets []
           bits bits]
      (if (empty? bits)
        [packets stream]
        (let [[packet bits] (eat-packet bits)]
          (recur (conj packets packet) bits))))))

(defn eat-n-packets [n stream]
  (loop [packets []
         stream stream]
    (if (= n (count packets))
      [packets stream]
      (let [[packet stream] (eat-packet stream)]
        (recur (conj packets packet) stream)))))

(defn eat-literal [stream]
  (loop [literal ""
         stream stream]
    (let [[more stream] (eat-bits 1 stream)
          [bits stream] (eat-bits 4 stream)
          literal (str literal bits)]
      (case more
        "0" [(u/parse-binary literal) stream] ; note some literals > 32 bits!
        "1" (recur literal stream)))))

(defn eat-operator [stream]
  (let [[length-type-id stream] (eat-bits 1 stream)]
    (case length-type-id
      "0" (apply eat-nbits-packets (eat-int 15 stream))
      "1" (apply eat-n-packets     (eat-int 11 stream)))))

(defn eat-packet [stream]
  (let [[ver stream]     (eat-int 3 stream)
        [type-id stream] (eat-int 3 stream)
        [value stream]   (if (= 4 type-id)
                           (eat-literal stream)
                           (eat-operator stream))]
    [{:version ver :type-id type-id :value value} stream]))

(def type-id->op
  {0 +
   1 *
   2 min
   3 max
   5 (fn [a b] (if (> a b) 1 0))
   6 (fn [a b] (if (< a b) 1 0))
   7 (fn [a b] (if (= a b) 1 0))})

(defn sum-versions [packet]
  (let [{:keys [type-id value version]} packet]
    (if (= 4 type-id)
     version
     (reduce + version (map sum-versions value)))))

(defn evaluate [packet]
  (let [{:keys [type-id value]} packet]
    (if (= 4 type-id)
     value
     (reduce (type-id->op type-id) (map evaluate value)))))

(defn part-1 [s]
  (sum-versions (first (eat-packet s))))

(defn part-2 [s]
  (evaluate (first (eat-packet s))))

(comment
  (part-1 (parse "inputs/d16/input"))

  (part-2 (parse "inputs/d16/input")))
