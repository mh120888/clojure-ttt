(ns clojure-ttt.board-spec
  (:require [speclj.core :refer :all]
    [clojure-ttt.board :refer :all]))

(describe "Modeling a tic tac toe board"

  (before-all
    (def new-board (generate-new-board 3))

    (def board-with-first-space-marked-with-x (mark-space new-board 0 "x"))

    (def board-with-cats-game {0 {:marked "x"}, 1 {:marked "o"}, 2 {:marked "o"},
                               3 {:marked "o"}, 4 {:marked "x"}, 5 {:marked "x"},
                               6 {:marked "x"}, 7 {:marked "o"}, 8 {:marked "o"}})

    (def board-with-horizonal-win {3 {:marked "x"}, 4 {:marked "x"}, 5 {:marked "x"}, 0 {:marked "o"}, 1 {}, 2 {}, 6 {}, 7 {}, 8 {}})

    (def board-with-vertical-win {3 {:marked "x"}, 4 {:marked "x"}, 5 {:marked "x"}, 0 {:marked "o"}, 1 {}, 2 {}, 6 {}, 7 {}, 8 {}})

    (def board-with-diagonal-win-top-left-to-bottom-right {0 {:marked "x"}, 1 {:marked "o"}, 2 {:marked "x"},
                                                           3 {:marked "o"}, 4 {:marked "x"}, 5 {:marked "o"},
                                                           6 {:marked "x"}, 7 {:marked "o"}, 8 {:marked "x"}})

    (def board-with-diagonal-win-top-right-to-bottom-left {0 {:marked "o"}, 1 {:marked "o"}, 2 {:marked "x"},
                                                           3 {:marked "o"}, 4 {:marked "x"}, 5 {:marked "o"},
                                                           6 {:marked "x"}, 7 {:marked "o"}, 8 {:marked "x"}}))

  (describe "generate-new-board"
    (it "creates a new board with the correct number of spaces"
      (should= 9 (count new-board))))

  (describe "look-up-space"
    (it "returns the marker that is at a given space"
      (should= nil (look-up-space new-board 0))))

  (describe "mark-space"
    (it "does not mark the board if the given space is already taken"
      (should-throw Error "That space is not empty" (mark-space board-with-first-space-marked-with-x 0 "o")))

    (it "marks a space with the given mark"
      (should= board-with-first-space-marked-with-x (mark-space new-board 0 "x"))))

  (describe "find-free-spaces"
    (it "returns the board with only the free spaces left"
      (should= 8 (count (find-free-spaces (mark-space new-board 0 "x"))))))

  (describe "find-taken-spaces"
    (it "finds no empty spaces when called with a new board"
      (should= 0 (count (find-taken-spaces new-board))))

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
      (should= true (board-full? board-with-cats-game))))

  (describe "valid-move?"
    (it "returns true if input corresponds to a space on the board and that space is empty"
      (should= true (valid-move? new-board "8")))

    (it "returns false if the given space is not free"
      (should= false (valid-move? board-with-first-space-marked-with-x "0")))

    (it "returns false if the input does not correspond to a space on the board"
      (should= false (valid-move? new-board "20")))

    (it "returns false if the input is not a board space at all"
      (should= false (valid-move? new-board "not a space"))))

  (describe "is-integer-as-string?"
    (it "returns true if given a string representation of an integer"
      (should= true (is-integer-as-string? "2")))

    (it "returns false if given any other string"
      (should= false (is-integer-as-string? "2.0"))))

  (describe "space-free?"
    (it "returns true if a space is empty"
      (should= true (space-free? new-board 8)))

    (it "returns false if a space is taken"
      (should= false (space-free? board-with-first-space-marked-with-x 0))))

  (describe "get-winner"
    (it "returns nil for an empty board"
      (should= nil (get-winner new-board)))

    (it "returns nil for a board with a cat's game"
      (should= nil (get-winner board-with-cats-game)))

    (it "returns the winning marker for a board with a horizontal win"
      (should= "x" (get-winner board-with-horizonal-win)))

    (it "returns the winning marker for a board with a vertical win"
      (should= "x" (get-winner board-with-vertical-win)))

    (it "returns the winning marker for a board with a diagonal win, top left to bottom right"
      (should= "x" (get-winner board-with-diagonal-win-top-left-to-bottom-right)))

    (it "returns the winning marker for a board with a diagonal win, top right to bottom left"
      (should= "x" (get-winner board-with-diagonal-win-top-right-to-bottom-left))))

  (describe "get-number-of-rows"
    (it "returns the number of rows given a board"
      (should= 3 (get-number-of-rows new-board))))

  (describe "game-over?"
    (it "returns true if there is a winner"
      (should= true (game-over? board-with-horizonal-win)))

    (it "returns true if the board is full"
      (should= true (game-over? board-with-cats-game)))

    (it "returns false for an empty board"
      (should= false (game-over? new-board)))

    (it "returns false for a game in progress that has no winner yet"
      (should= false (game-over? board-with-first-space-marked-with-x))))

  (describe "get-other-marker"
    (it "returns \"x\" if given \"o\""
      (should= "x" (get-other-marker "o")))

    (it "returns \"o\" if given \"x\""
      (should= "o" (get-other-marker "x")))))

(run-specs)
