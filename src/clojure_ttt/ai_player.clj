(ns clojure-ttt.ai-player
  (:require [clojure-ttt.core :refer :all]))

(declare negamax score-board bubble-up-score flatten-score-map play-next-round-of-moves)

(defn get-next-move
  "Calculates which is the best move for a given player and a given board"
  [board marker]
  (let [moves-and-scores (negamax board 0 marker 1)
        potential-moves (flatten-score-map moves-and-scores)]
        (println moves-and-scores)
    (first (last (sort-by val potential-moves)))))

(defn negamax
  "Negamax implementation for ranking moves"
  [board depth marker color]
  (if (stop-game? board)
    (* color (score-board board marker depth))
    (let [free-spaces (find-free-spaces board)]
      (play-next-round-of-moves board depth marker color free-spaces))))

(defn play-next-round-of-moves
  [board depth marker color free-spaces]
    (into {} (map #(assoc % 1 (negamax (mark-space board (first %) marker) (inc depth) (get-other-marker marker) (* -1 color))) free-spaces)))

(defn score-board
  "Gives a numeric score for a board"
  [board marker depth]
  (let [winner (has-won? board)]
    (cond
      (= false winner) 0
      (= winner marker) (- 10 depth)
      :else (+ depth -10))))

(defn bubble-up-score
  [element]
  (cond
    (integer? element) element
    :else (second (first element))))

(defn flatten-score-map
  [starting-map]
  (if (every? integer? (vals starting-map))
    starting-map
    (let [new-map (into {} (map #(assoc % 1 (bubble-up-score (second %))) starting-map))]
      (recur new-map))))
