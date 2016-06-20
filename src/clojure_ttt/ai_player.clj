(ns clojure-ttt.ai-player
  (:require [clojure-ttt.core :refer :all]))

(declare negamax score-board bubble-up-score flatten-score-map play-next-round-of-moves return-value)

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
      (play-next-round-of-moves board depth marker (* -1 color) free-spaces))))

(defn play-next-round-of-moves
  [board depth marker color free-spaces]
    (into {} (map #(assoc % 1 (negamax (mark-space board (first %) marker) (inc depth) (get-other-marker marker) color)) free-spaces)))

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

; (defn flatten-score-map
;   [starting-map]
;   (if (every? integer? (vals starting-map))
;     starting-map
;     (into {} (map (fn [element]
;                     ; (if (integer? (second element))
;                     ;   (assoc element 1 (bubble-up-score (second element)))
;                     ;   (flatten-score-map (second element)))
;                   (cond
;                     (integer? (second element)) (assoc element 1 (bubble-up-score (second element)))
;                     (= 1 (count (second element))) (flatten-score-map (second element))))
;                     :else
;               starting-map))))

(defn fsm
  [starting-map]
  (into {} (map fsm-map starting-map)))

(defn fsm-map
  [element]
  (println element)
  (cond
      (integer? (second element)) element
      (and (map? (second element)) (= 1 (count (second element)))) (into () (second (first element)))))

(defn return-value
  [key-value-pair]
  (first (vals key-value-pair)))

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