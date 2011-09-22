(ns cljwordgraph.core)

(defn gather-words [s]
  (map #(.toLowerCase %) (re-seq #"\w+" s)))

(defn count-words [coll]
  (reduce #(assoc %1 %2 (inc (%1 %2 0))) {} coll))

(defn sort-counted-words [words]
  (sort-by second words))

(defn repeat-str [s count]
  (apply str (repeat count s)))

(defn histogram-entry [word-count width]
  (with-out-str
    (printf
      (str "%-" width "." width "s %s")
      (first word-count)
      (repeat-str "#" (second word-count)))))

(defn histogram [word-counts]
  (let [max (apply max (map (comp count first) word-counts))]
    (apply str 
      (map (fn [w] (str (histogram-entry w max) \newline))
        word-counts))))

(comment

  ;; the traditional LISP way
  (defn run [file]
    (println (histogram (sort-counted-words (count-words (gather-words (slurp file)))))))

  )

;; the Clojure way
(defn run [file]
  (->> file
    slurp
    gather-words
    count-words
    sort-counted-words
    histogram
    println))
