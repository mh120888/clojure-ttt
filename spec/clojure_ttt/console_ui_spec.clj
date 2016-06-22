(ns clojure-ttt.console-ui-spec
  (:require [speclj.core :refer :all]
    [clojure-ttt.core :as core]
    [clojure-ttt.console-ui :as console-ui]
    [clojure-ttt.player :as player])
  (:import [clojure_ttt.console_ui ConsoleIO]
           [clojure_ttt.player HumanPlayer]))

(deftype TestConsoleIO [input]
  console-ui/IOProtocol
  (io-print-line [type message]
    message)
  (io-print [type message]
    message)
  (io-read [type]
    input))

(describe "Console output for tic tac toe board"
  (around [it]
    (with-out-str (it)))

  (before-all
    (def new-board (core/generate-new-board 3)))

  (describe "console-ui/print-board"
    (it "prints out a new board in a nice format"
      (should= "_ _ _\n_ _ _\n_ _ _\n" (console-ui/print-board (TestConsoleIO. "") new-board)))

    (before
      (def board-in-progress (core/mark-space new-board 0 "x")))

    (it "prints out a board in progress in a nice format"
      (should= "x _ _\n_ _ _\n_ _ _\n" (console-ui/print-board (TestConsoleIO. "") board-in-progress))))

  (describe "console-ui/get-current-marker-for-console-display"
    (it "returns an understore if the given space is empty"
      (should= "_" (console-ui/get-current-marker-for-console-display new-board 0)))

    (it "returns the marker if the given space is taken"
      (should= "x" (console-ui/get-current-marker-for-console-display board-in-progress 0))))

  (describe "console-ui/get-user-input"
    (before-all
      (def message "Where would you like to play?"))

    (it "returns the move specified by the user"
      (should= "3" (console-ui/get-user-input (TestConsoleIO. "3") message))))

  (describe "console-ui/do-you-want-to-go-first"
    (it "returns y if a user inputs y"
      (should= "y" (console-ui/do-you-want-to-go-first (TestConsoleIO. "y")))))

  (describe "console-ui/get-board-size"
    (it "returns the board size the user chose, if it is valid"
      (should= 3 (console-ui/get-board-size (TestConsoleIO. "3")))))

  (describe "console-ui/get-human-marker"
    (it "returns the marker the user chose, if it is valid"
      (should= "x" (console-ui/get-human-marker (TestConsoleIO. "x")))))

  (describe "HumanPlayer player/get-move"
    (it "returns the move the user chose, if it is valid"
      (should= 2 (player/get-move (HumanPlayer. (TestConsoleIO. "2")) board-in-progress "x")))))

(run-specs)
