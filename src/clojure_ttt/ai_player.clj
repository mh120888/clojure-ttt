(ns clojure-ttt.ai-player
  (:require [clojure-ttt.board :as board]))

(declare negamax)

(defn get-min-value
  [map-input]
  (second (last (reverse (sort-by val map-input)))))

(defn get-max-value
  [map-input]
  (second (last (sort-by val map-input))))

(defn all-map-values-are-integers?
  [map-input]
  (every? integer? (vals map-input)))

(defn find-best-score
  [starting-map max-and-min-functions]
    (reduce (fn [new-map [key value]]
      (cond
        (integer? value) (assoc new-map key value)
        (and (map? value) (= 1 (count value))) (assoc new-map key ((comp second first) value))
        (all-map-values-are-integers? value) (assoc new-map key ((first max-and-min-functions) value))
        :else (assoc new-map key (find-best-score value (reverse max-and-min-functions)))))
    {} starting-map))

(defn flatten-score-map
  [scores]
  (if (all-map-values-are-integers? scores)
    scores
    (let [new-map (find-best-score scores [get-min-value get-max-value])]
      (recur new-map))))

(defn score-board
  [board marker depth]
  (let [winner (board/get-winner board)]
    (condp = winner
      nil 0
      marker (- 10 depth)
      (+ depth -10))))


(defn play-next-round-of-moves
  [board depth marker color free-spaces]
    (into {} (map #(assoc % 1 (negamax (board/mark-space board (first %) marker) (inc depth) (board/get-other-marker marker) color)) free-spaces)))

(def memoize-play-next-round-of-moves (memoize play-next-round-of-moves))

(defn negamax
  [board depth marker color]
  (if (board/game-over? board)
    (* color (score-board board marker depth))
    (let [free-spaces (board/find-free-spaces board)]
      (memoize-play-next-round-of-moves board depth marker (* -1 color) free-spaces))))
