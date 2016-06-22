(ns clojure-ttt.ai-player-spec
  (:require [speclj.core :refer :all]
    [clojure-ttt.core :refer :all]
    [clojure-ttt.ai-player :refer :all]
    [clojure-ttt.game :refer :all]
    [clojure-ttt.player :refer :all])
  (:import [clojure_ttt.player ComputerPlayer]))

(describe "ComputerPlayer.get-move"
  (before-all
    (def computer-player (ComputerPlayer.))

    (def board-with-only-one-space-open {0 {:marked "x"}, 1 {:marked "o"}, 2 {},
                                         3 {:marked "o"}, 4 {:marked "x"}, 5 {:marked "x"},
                                         6 {:marked "x"}, 7 {:marked "o"}, 8 {:marked "o"}})

    (def board-with-imminent-win {0 {:marked "x"}, 1 {:marked "o"}, 2 {:marked "o"},
                                  3 {:marked "o"}, 4 {:marked "x"}, 5 {},
                                  6 {:marked "x"}, 7 {:marked "o"}, 8 {}})

    (def board-with-imminent-loss {0 {:marked "x"}, 1 {:marked "x"}, 2 {:marked "o"},
                                   3 {:marked "o"}, 4 {:marked "o"}, 5 {},
                                   6 {:marked "x"}, 7 {:marked "o"}, 8 {}})

    (def another-board-with-imminent-loss {0 {           }, 1 {           }, 2 {           },
                                           3 {:marked "x"}, 4 {:marked "o"}, 5 {           },
                                           6 {:marked "o"}, 7 {           }, 8 {:marked "x"}})

    (def new-board (generate-new-board 3)))

  (it "returns the only possible move on a board that has only one available space"
    (should= 2 (get-move computer-player board-with-only-one-space-open "x")))

  (it "returns the move that will allow the given player to win"
    (should= 8 (get-move computer-player board-with-imminent-win "x")))

  (it "returns the move that will prevent the opponent from winning"
    (should= 5 (get-move computer-player board-with-imminent-loss "x")))

  (it "returns the move that will prevent the opponent from winning"
    (should= 2 (get-move computer-player another-board-with-imminent-loss "x"))))

(describe "score-board"
  (before-all
    (def board-where-x-wins {0 {:marked "o"}, 1 {:marked "o"}, 2 {:marked "x"},
                             3 {:marked "o"}, 4 {:marked "x"}, 5 {:marked "o"},
                             6 {:marked "x"}, 7 {:marked "o"}, 8 {:marked "x"}})

    (def board-with-cats-game {0 {:marked "o"}, 1 {:marked "o"}, 2 {:marked "x"},
                               3 {:marked "x"}, 4 {:marked "x"}, 5 {:marked "o"},
                               6 {:marked "o"}, 7 {:marked "o"}, 8 {:marked "x"}}))

  (it "gives a positive score if the given player has won"
    (should= true (< 0 (score-board board-where-x-wins "x" 1))))

  (it "gives a negative score if the given player has lost"
    (should= false (< 0 (score-board board-where-x-wins "o" 1))))

  (it "returns 0 if it's a cat's game"
    (should= 0 (score-board board-with-cats-game "x" 1))))

(describe "flatten-score-map"
  (it "returns the a map as-is if it is not nested"
    (should= {5 0, 8 9} (flatten-score-map {5 0, 8 9})))

  (it "returns a map flattened by replacing a nested map with its value"
    (should= {5 0, 8 9} (flatten-score-map {5 {8 {8 0}}, 8 9}))))

(describe "all-map-values-are-integers?"
  (it "returns true if all values are integers"
    (should= true (all-map-values-are-integers? {1 2, 2 2, 3 3, 4 5})))

  (it "returns false if not all values are integers"
    (should= false (all-map-values-are-integers? {1 2, 2 2, 3 3, 4 {2 1}}))))

(describe "get-max-value"
  (it "returns the highest value from a map"
    (should= 5 (get-max-value {1 2, 2 2, 3 3, 4 5}))))

(describe "get-min-value"
  (it "returns the lowest value from a map"
    (should= 1 (get-min-value {4 6, 3 4, 6 6, 10 1}))))

(describe "alternate-max-and-min"
  (it "returns get-max-value when given get-min-value"
    (should= get-max-value (alternate-max-and-min get-min-value)))

  (it "returns get-min-value when given get-max-value"
    (should= get-min-value (alternate-max-and-min get-max-value))))
