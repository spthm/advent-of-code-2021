(ns run
  (:require [aoc
             d01 d02 d03 d04 d05
             d06 d07 d08 d09 d10
             d11 d12 d13 d14 d15
             d16 d17])
  (:require [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :as io])
  (:gen-class))

(defn parse-int-range [s]
  ;; hacky!
  (let [[start end] (->> (re-seq #"\d+" s) (map #(Integer/parseInt %)))
        end (or end start)] ; end is optional
    (range start (inc end))))

(def cli-options
  [["-d" "--days RANGE" "Day(s) to run, e.g. '1', '4-5'"
    :default (range 1 18)
    :default-desc "1-17"
    :parse-fn parse-int-range]
   ["-p" "--parts RANGE" "Part(s) to run, e.g. '1', '1-2'"
    :default (range 1 3)
    :default-desc "1-2"
    :parse-fn parse-int-range]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["AoC 2021 solutions in Clojure (incomplete!)"
        ""
        "Usage: clj -M -m run [options]"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn validate-args
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}

      errors
      {:exit-message (error-msg errors)}

      (pos? (count arguments))
      {:exit-message (format "No arguments are accepted (you provided %s)" arguments)
       :options options}

      :else
      {:options options})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn find-input [day]
  (let [fp (format "inputs/d%02d/input" day)]
    (if (.exists (io/file fp))
      fp
      (println (format "  cannot find input file: %s" fp)))))

(defn find-parse [day]
  (if-let [parse (resolve (symbol (format "aoc.d%02d/parse" day)))]
    parse
    (println "  cannot find (parse)")))

(defn find-part-* [day part]
  (if-let [part (resolve (symbol (format "aoc.d%02d/part-%d" day part)))]
    part
    (println (format "  cannot find (part-%d)" part))))

(defn run
  [day n]
  (when-let [input (find-input day)]
    (when-let [parse (find-parse day)]
      (when-let [part-* (find-part-* day n)]
        (let [answer (part-* (parse input))]
          (println (format "  %d: %-30s" n answer)))))))

(defn -main [& args]
  (let [{:keys [options exit-message ok?]} (validate-args args)]
    (cond
      (some? exit-message)
      (exit (if ok? 0 1) exit-message)

      :else
      (doseq [day (:days options)]
        (println (format "Day %02d" day))
        (doseq [part (:parts options)]
          (run day part))
        (println)))))
