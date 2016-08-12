(ns matts-clojure-ttt.board-spec
  (:require [speclj.core :refer :all]
            [matts-clojure-ttt.board :refer :all]))

(describe "Modeling a tic tac toe board"

  (before-all
    (def new-board (generate-new-board 3))

    (def board-with-first-space-marked-with-x (mark-space new-board 0 "x"))

    (def board-with-two-xs-in-a-row
      (-> board-with-first-space-marked-with-x
          (mark-space 4 "o")
          (mark-space 1 "x")))

    (def board-with-cats-game
      (-> new-board
          (mark-space 0 "o")
          (mark-space 1 "o")
          (mark-space 2 "x")
          (mark-space 3 "x")
          (mark-space 4 "x")
          (mark-space 5 "o")
          (mark-space 6 "o")
          (mark-space 7 "o")
          (mark-space 8 "x")))

    (def board-with-horizonal-win
      (-> new-board
          (mark-space 3 "x")
          (mark-space 4 "x")
          (mark-space 5 "x")))

    (def board-with-vertical-win
      (-> new-board
          (mark-space 0 "x")
          (mark-space 3 "x")
          (mark-space 6 "x")))

    (def board-with-diagonal-win-top-left-to-bottom-right
      (-> new-board
          (mark-space 0 "o")
          (mark-space 1 "o")
          (mark-space 2 "x")
          (mark-space 3 "o")
          (mark-space 4 "x")
          (mark-space 5 "o")
          (mark-space 6 "x")
          (mark-space 7 "o")
          (mark-space 8 "x")))

    (def board-with-diagonal-win-top-right-to-bottom-left
      (-> new-board
          (mark-space 0 "o")
          (mark-space 1 "o")
          (mark-space 2 "x")
          (mark-space 3 "o")
          (mark-space 4 "x")
          (mark-space 5 "o")
          (mark-space 6 "x")
          (mark-space 7 "o")
          (mark-space 8 "x"))))

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
      (should= 1 (count (find-taken-spaces (mark-space new-board 0 "x"))))))

  (describe "find-spaces-taken-by"
    (it "returns an empty collection if there are no spaces taken by the given marker"
      (should= 0 (count (find-spaces-taken-by new-board "x"))))

    (it "returns the board with only the spaces taken by the given marker"
      (should= 1 (count (find-spaces-taken-by board-with-first-space-marked-with-x "x")))))

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
      (should= "o" (get-other-marker "x"))))

  (describe "generate-horizontal-coords"
    (it "returns a list of lists, each containing a horizontal row of coordinates"
      (should= `((0 1 2) (3 4 5) (6 7 8)) (generate-horizontal-coords 3))))

  (describe "generate-vertical-coords"
    (it "returns a list of lists, each containing a vertical column of coordinates"
      (should= `((0 3 6) (1 4 7) (2 5 8)) (generate-vertical-coords 3))))

  (describe "top-right-to-bottom-left-coords"
    (it "returns a list containing 2, 4, 6 for a board with 3 rows"
      (should= `(2 4 6) (top-right-to-bottom-left-coords 3)))

    (it "returns a list containing 3, 6, 9, 12 for a board with 4 rows"
      (should= `(3 6 9 12) (top-right-to-bottom-left-coords 4))))

  (describe "top-left-to-bottom-right-coords"
    (it "returns a list containing 0, 4, 8 for a board with 3 rows"
      (should= `(0 4 8) (top-left-to-bottom-right-coords 3)))

    (it "returns a list containing 0, 5, 10, 15 for a board with 4 rows"
      (should= `(0 5 10 15) (top-left-to-bottom-right-coords 4)))))

(run-specs)
