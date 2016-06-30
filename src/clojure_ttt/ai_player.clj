(ns clojure-ttt.ai-player
  (:require [clojure-ttt.board :as board]))

(defn score-board
  [board marker depth]
  (let [winner (board/get-winner board)]
    (condp = winner
      nil 0
      marker (- 10 depth)
      (+ depth -10))))

(defn get-possible-game-states
  [board marker]
  (->> (board/find-free-spaces board)
       (keys)
       (vec)
       (reduce (fn [new-map space] (assoc new-map space (board/mark-space board space marker))) {})))

(def memoize-get-possible-game-states (memoize get-possible-game-states))

(defn get-value-or-move
  [depth]
  (if (= 0 depth)
    first
    second))

(defn get-depth-limit
  [board]
  (condp = (count board)
    9  5
    16 6
    5))

(defn max-or-min
  [depth]
  (if (even? depth)
    >
    <))

(defn minmax
  [board depth marker color]
  (if (or (board/game-over? board) (< (get-depth-limit board) depth))
    (* color (score-board board marker depth))
    (->> (memoize-get-possible-game-states board marker)
         (reduce (fn [new-map [space possible-board]] (assoc new-map space (minmax possible-board (inc depth) (board/get-other-marker marker) (- color)))) {})
         (sort-by val (max-or-min depth))
         (first)
         ((get-value-or-move depth)))))

(def memoize-minmax (memoize minmax))
