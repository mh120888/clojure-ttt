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
      (should= "_ _ _\n_ _ _\n_ _ _\n" (with-out-str (print-board new-board))))

    (before
      (def board-in-progress (do
        (mark-space new-board 0 "x"))))

    (it "prints out a board in progress in a nice format"
      (should= "x _ _\n_ _ _\n_ _ _\n" (with-out-str (print-board board-in-progress)))))

  (describe "get-current-marker-for-console-display"
    (it "returns an understore if the given space is empty"
      (should= "_" (get-current-marker-for-console-display new-board 0)))

    (it "returns the marker if the given space is taken"
      (should= "x" (get-current-marker-for-console-display board-in-progress 0)))))

(run-specs)
