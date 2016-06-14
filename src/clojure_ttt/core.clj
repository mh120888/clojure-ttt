(ns clojure-ttt.core)

(defn generate-new-board
  [num-of-rows]
  (let [num-of-spaces (* num-of-rows num-of-rows)]
    (reduce #(assoc %1 %2 {}) {} (take num-of-spaces (range)))))

(defn look-up-space
  [board space]
  (get-in board [space :marked]))

(defn mark-space
  [board space mark]
  (assoc board space {:marked mark}))

(defn find-free-spaces
  [board]
  (into {} (filter #(nil? (look-up-space board (first %))) board)))
