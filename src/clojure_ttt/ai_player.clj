(ns clojure-ttt.ai-player
  (:require [clojure-ttt.core :refer :all]))

(declare negamax)


(defn get-min-value
  [map]
  (second (last (reverse (sort-by val map)))))

(defn get-max-value
  [map]
  (second (last (sort-by val map))))

(defn alternate-max-and-min
  [fn]
  (if
    (= fn get-max-value) get-min-value
    get-max-value))

(defn fsm
  [starting-map function]
    (reduce (fn [new-map [key value]]
      (cond
        (integer? value) (assoc new-map key value)
        (and (map? value) (= 1 (count value))) (assoc new-map key (second (first value)))
        (every? integer? (vals value)) (assoc new-map key (function value))
        :else (assoc new-map key (fsm value (alternate-max-and-min function))))
    ) {} starting-map))

(defn flatten-score-map
  [map]
  (if
    (every? integer? (vals map)) map
    (let [new-map (fsm map get-min-value)]
      (recur new-map))))

(defn score-board
  "Gives a numeric score for a board"
  [board marker depth]
  (let [winner (has-won? board)]
    (cond
      (= false winner) 0
      (= winner marker) (- 10 depth)
      :else (+ depth -10))))

(defn play-next-round-of-moves
  [board depth marker color free-spaces]
    (into {} (map #(assoc % 1 (negamax (mark-space board (first %) marker) (inc depth) (get-other-marker marker) color)) free-spaces)))

(defn negamax
  "Negamax implementation for ranking moves"
  [board depth marker color]
  (if (stop-game? board)
    (* color (score-board board marker depth))
    (let [free-spaces (find-free-spaces board)]
      (play-next-round-of-moves board depth marker (* -1 color) free-spaces))))

(defn get-next-move
  "Calculates which is the best move for a given player and a given board"
  [board marker]
  (let [moves-and-scores (negamax board 0 marker 1)
        potential-moves (flatten-score-map moves-and-scores)]
        (println potential-moves)
    (first (last (sort-by val potential-moves)))))

; 1. map over map
;     1. if value is an integer, return the integer
;     2. if value is a map with a single value, return the value of the first key-value pair
;     3. if value is a map with more than one value
;         1. if all values are integers
;             1. rank moves by value (score) (either by highest or lowest score, depending on depth) and take the value (score) that is highest/lowest
;         2. else
;                 1. recur

; Given a map representing the potential moves on a board, we check if it contains any sub-maps
; IF it doesn't return the map of moves and scores
; ELSE (it does)
;   map over the map, branching for each element
;     IF the submap is just a key and value (if count is 1, if the value is an int), return the value
;     ELSE (if the submap is multiple key-value pairs (if count > 1)), recur