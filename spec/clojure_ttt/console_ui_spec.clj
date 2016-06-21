(ns clojure-ttt.console-ui-spec
  (:require [speclj.core :refer :all]
    [clojure-ttt.core :refer :all]
    [clojure-ttt.console-ui :refer :all]))

(describe "Console output for tic tac toe board"
  (around [it]
    (with-out-str (it)))

  (before-all
    (def new-board (generate-new-board 3)))

  (describe "print-board"
    (it "prints out a new board in a nice format"
      (should= "_ _ _\n_ _ _\n_ _ _\n\n\n" (with-out-str (print-board new-board))))

    (before
      (def board-in-progress (mark-space new-board 0 "x")))

    (it "prints out a board in progress in a nice format"
      (should= "x _ _\n_ _ _\n_ _ _\n\n\n" (with-out-str (print-board board-in-progress)))))

  (describe "get-current-marker-for-console-display"
    (it "returns an understore if the given space is empty"
      (should= "_" (get-current-marker-for-console-display new-board 0)))

    (it "returns the marker if the given space is taken"
      (should= "x" (get-current-marker-for-console-display board-in-progress 0))))

  (describe "get-user-input"
    (before-all
      (def message "Where would you like to play?"))

    (it "asks the user the specified question"
      (should="Where would you like to play?\n" (with-out-str (with-in-str "3" (get-user-input message)))))

    (it "returns the move specified by the user"
      (should= "3" (with-in-str "3" (get-user-input message)))))

  (describe "do-you-want-to-go-first"
    (it "returns y if a user inputs y"
      (should= "y" (with-in-str "y" (do-you-want-to-go-first)))))

  (describe "get-board-size"
    (it "returns the board size the user chose, if it is valid"
      (should= 3 (with-in-str "3" (get-board-size)))))

  (describe "get-human-marker"
    (it "returns the marker the user chose, if it is valid"
      (should= "x" (with-in-str "x" (get-human-marker)))))

  (describe "get-next-human-move"
    (it "returns the move the user chose, if it is valid"
      (should= 2 (with-in-str "2" (get-next-human-move new-board))))))

(run-specs)
