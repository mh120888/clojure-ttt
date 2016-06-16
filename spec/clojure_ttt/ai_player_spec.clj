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
