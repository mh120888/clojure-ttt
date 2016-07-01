(ns clojure-ttt.ai-player
  (:require [clojure-ttt.board :as board]))

(declare minmax)

(defn score-board
  [board marker depth]
  (let [winner (board/get-winner board)]
    (condp = winner
      nil 0
      marker (- 10 depth)
      (+ depth -10))))

(def memo-score-board (memoize score-board))

(defn get-possible-game-states
  [board marker]
  (->> (board/find-free-spaces board)
       (keys)
       (vec)
       (reduce (fn [new-map space] (assoc new-map space (board/mark-space board space marker))) {})))

(def memo-get-possible-game-states (memoize get-possible-game-states))

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

(def memo-get-depth-limit (memoize get-depth-limit))

(defn max-or-min
  [depth]
  (if (even? depth)
    >
    <))

(defn alpha-beta-max
  [build-up-this-map depth marker color alpha beta possible-boards]
    (let [current-board (second (first possible-boards))
          value (minmax current-board (inc depth) (board/get-other-marker marker) (- color) alpha beta)
          space (first (first possible-boards))
          new-alpha (max alpha value)
          new-map (assoc build-up-this-map space value)]
            (if (or (>= new-alpha beta) (empty? (rest possible-boards)))
              new-map
              (recur new-map depth marker color new-alpha beta (rest possible-boards)))))

(defn alpha-beta-min
  [build-up-this-map depth marker color alpha beta possible-boards]
  (let [current-board (second (first possible-boards))
        value (minmax current-board (inc depth) (board/get-other-marker marker) (- color) alpha beta)
        space (first (first possible-boards))
        new-beta (min beta value)
        new-map (assoc build-up-this-map space value)]
          (if (or (>= alpha new-beta) (empty? (rest possible-boards)))
            new-map
            (recur new-map depth marker color alpha new-beta (rest possible-boards)))))

(defn alpha-beta
  ([depth marker color alpha beta possible-boards] (alpha-beta {} depth marker color alpha beta possible-boards))
  ([build-up-this-map depth marker color alpha beta possible-boards]
    (if (even? depth)
      (alpha-beta-max build-up-this-map depth marker color alpha beta possible-boards)
      (alpha-beta-min build-up-this-map depth marker color alpha beta possible-boards))))

(defn minmax
  ([board marker] (minmax board 0 marker 1 -100 100))
  ([board depth marker color alpha beta]
  (if (or (< (memo-get-depth-limit board) depth) (board/game-over? board))
      (* color (memo-score-board board marker depth))
      (->> (memo-get-possible-game-states board marker)
           (alpha-beta depth marker color alpha beta)
           (sort-by val (max-or-min depth))
           (first)
           ((get-value-or-move depth))))))

(def memo-minmax (memoize minmax))
