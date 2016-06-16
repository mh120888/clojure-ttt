(ns clojure-ttt.ai-player-spec
  (:require [speclj.core :refer :all]
    [clojure-ttt.core :refer :all]
    [clojure-ttt.ai-player :refer :all]))

(describe "get-next-move"
  (before-all
    (def board-with-only-one-space-open {0 {:marked "x"}, 1 {:marked "o"}, 2 {},
                                         3 {:marked "o"}, 4 {:marked "x"}, 5 {:marked "x"},
                                         6 {:marked "x"}, 7 {:marked "o"}, 8 {:marked "o"}})

    (def board-with-imminent-win {0 {:marked "x"}, 1 {:marked "o"}, 2 {:marked "o"},
                                  3 {:marked "o"}, 4 {:marked "x"}, 5 {},
                                  6 {:marked "x"}, 7 {:marked "o"}, 8 {}}))

  (it "returns the only possible move on a board that has only one available space"
    (should= 2 (get-next-move board-with-only-one-space-open "x"))))

(describe "score-board"
  (before-all
    (def board-where-x-wins {0 {:marked "o"}, 1 {:marked "o"}, 2 {:marked "x"},
                             3 {:marked "o"}, 4 {:marked "x"}, 5 {:marked "o"},
                             6 {:marked "x"}, 7 {:marked "o"}, 8 {:marked "x"}})

    (def board-with-cats-game {0 {:marked "o"}, 1 {:marked "o"}, 2 {:marked "x"},
                               3 {:marked "x"}, 4 {:marked "x"}, 5 {:marked "o"},
                               6 {:marked "o"}, 7 {:marked "o"}, 8 {:marked "x"}}))

  (it "gives a positive score if the given player has won"
    (should= true (< 0 (score-board board-where-x-wins "x"))))

  (it "gives a negative score if the given player has lost"
    (should= false (< 0 (score-board board-where-x-wins "o"))))

  (it "returns 0 if it's a cat's game"
    (should= 0 (score-board board-with-cats-game "x")))

  )
