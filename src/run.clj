(ns run
  (:require [aoc
             d01 d02 d03 d04 d05
             d06 d07 d08 d09 d10
             d11 d12 d13 d14 d15
             d16 d17])
  (:require [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(defn parse-days [s]
  ;; hacky!
  (let [[start end] (->> (re-seq #"\d+" s) (map #(Integer/parseInt %)))
        end (or end start)] ; end is optional
    (range start (inc end))))

(def cli-options
  [["-d" "--days RANGE" "Day(s) to run, e.g. '1', '4-5'"
    :default (range 1 18)
    :default-desc "1-17"
    :parse-fn parse-days]
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

      (< 0 (count arguments))
      {:exit-message (format "No arguments are accepted (you provided %s)" arguments)
       :options options}

      :else
      {:options options})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn run
  ([day]
   (run day 1)
   (run day 2))
  ([day part]
   (let [input (format "inputs/d%02d/input" day)
         parse-sym (symbol (format "aoc.d%02d/parse" day))
         soln-sym (symbol (format "aoc.d%02d/part-%d" day part))]
     (if-let [parse (resolve parse-sym)]
       (if-let [soln (resolve soln-sym)]
         (let [answer (soln (parse input))]
           (println (format "  %d: %-30s" part answer)))
         (println (format "  Can't find %s" soln-sym)))
       (println (format "  Can't find %s" parse-sym))
     )
   )
  )
)

(defn -main [& args]
  (let [{:keys [options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (doseq [day (:days options)]
        (println (format "Day %02d" day))
        (run day)
        (println)))))
