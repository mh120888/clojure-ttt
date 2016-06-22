(ns clojure-ttt.ai-player
  (:require [clojure-ttt.core :as core]))

(declare negamax)

(defn get-min-value
  [map-input]
  (second (last (reverse (sort-by val map-input)))))

(defn get-max-value
  [map-input]
  (second (last (sort-by val map-input))))

(defn alternate-max-and-min
  [function]
  (if (= function get-max-value)
    get-min-value
    get-max-value))

(defn all-map-values-are-integers?
  [map-input]
  (every? integer? (vals map-input)))

(defn find-best-score
  [starting-map max-or-min-function]
    (reduce (fn [new-map [key value]]
      (cond
        (integer? value) (assoc new-map key value)
        (and (map? value) (= 1 (count value))) (assoc new-map key ((comp second first) value))
        (all-map-values-are-integers? value) (assoc new-map key (max-or-min-function value))
        :else (assoc new-map key (find-best-score value (alternate-max-and-min max-or-min-function)))))
    {} starting-map))

(defn flatten-score-map
  [scores]
  (if (all-map-values-are-integers? scores)
    scores
    (let [new-map (find-best-score scores get-min-value)]
      (recur new-map))))

(defn score-board
  [board marker depth]
  (let [winner (core/has-won? board)]
    (condp = winner
      false 0
      marker (- 10 depth)
      (+ depth -10))))

(defn play-next-round-of-moves
  [board depth marker color free-spaces]
    (into {} (map #(assoc % 1 (negamax (core/mark-space board (first %) marker) (inc depth) (core/get-other-marker marker) color)) free-spaces)))

(defn negamax
  [board depth marker color]
  (if (core/stop-game? board)
    (* color (score-board board marker depth))
    (let [free-spaces (core/find-free-spaces board)]
      (play-next-round-of-moves board depth marker (* -1 color) free-spaces))))
