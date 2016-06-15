(ns clojure-ttt.core-spec
  (:require [speclj.core :refer :all]
    [clojure-ttt.core :refer :all]))
(describe "Modeling a tic tac toe board"

  (before-all
    (def new-board (generate-new-board 3))
    (def board-with-0-marked-with-x (mark-space new-board 0 "x")))

  (describe "generate-new-board"
    (it "creates a new board with the correct number of spaces"
      (should= 9 (count new-board))))

  (describe "look-up-space"
    (it "returns the marker that is at a given space"
      (should= nil (look-up-space new-board 0))))

  (describe "mark-space"
    (it "does not mark the board if the given space is already taken"
      (should= board-with-0-marked-with-x (mark-space board-with-0-marked-with-x 0 "o")))

    (it "marks a space with the given mark"
      (should= board-with-0-marked-with-x (mark-space new-board 0 "x"))))

  (describe "find-free-spaces"
    (it "returns the board with only the free spaces left"
      (should= {1 {}, 2 {}, 3 {}, 4 {}, 5 {}, 6 {}, 7 {}, 8 {}} (find-free-spaces (mark-space new-board 0 "x")))))

  (describe "find-taken-spaces"
    (it "returns an empty map when called with a new board"
      (should= {} (find-taken-spaces new-board)))

    (it "returns the board with only the taken spaces"
      (should= {0 {:marked "x"}} (find-taken-spaces (mark-space new-board 0 "x")))))

  (describe "find-spaces-taken-by"
    (it "returns an empty map if there are no spaces taken by the given marker"
      (should= {} (find-spaces-taken-by new-board "x")))

    (it "returns the board with only the spaces taken by the given marker"
      (should= {0 {:marked "x"}} (find-spaces-taken-by (mark-space new-board 0 "x") "x"))))

  (describe "board-full?"
    (it "returns a false for a new board"
      (should= false (board-full? new-board)))

    (it "returns true for a board with no free spaces"
      (should= true (board-full? {0 {:marked "x"}, 1 {:marked "x"}, 2 {:marked "x"}, 3 {:marked "x"}, 4 {:marked "x"}, 5 {:marked "x"}, 6 {:marked "x"}, 7 {:marked "x"}, 8 {:marked "x"}}))))

  (describe "space-free?"
    (it "returns true if a space is empty"
      (should= true (space-free? new-board 0)))

    (it "returns false if a space is taken"
      (should= false (space-free? board-with-0-marked-with-x 0))))

  (describe "has-won?"
    (it "returns false for an empty board"
      (should= false (has-won? new-board)))

    (it "returns false for a board with a cat's game"
      (should= false (has-won? {0 {:marked "x"}, 1 {:marked "o"}, 2 {:marked "x"},
                                3 {:marked "o"}, 4 {:marked "x"}, 5 {:marked "o"},
                                6 {:marked "x"}, 7 {:marked "o"}, 8 {:marked "x"}})))

    (it "returns true for a board with a horizontal win"
      (should= true (has-won? {3 {:marked "x"}, 4 {:marked "x"}, 5 {:marked "x"}, 0 {:marked "o"}, 1 {}, 2 {}, 6 {}, 7 {}, 8 {}})))

    (it "returns true for a board with a vertical win"
      (should= true (has-won? {1 {:marked "x"}, 4 {:marked "x"}, 7 {:marked "x"}, 0 {:marked "o"}, 3 {}, 2 {}, 6 {}, 5 {}, 8 {}})))))

(run-specs)
