(ns clojure-ttt.core-spec
  (:require [speclj.core :refer :all]
            [clojure-ttt.core :refer :all]))

(describe "generate-new-board"
  (it "creates a new board with the correct number of spaces"
    (should= 9 (count (generate-new-board 3)))))

(describe "look-up-space"
  (it "returns the marker that is at a given space"
    (should= nil (look-up-space (generate-new-board 3) 0))))

(describe "mark-space"
  (it "marks a space with the given mark"
    (should= {0 {:marked "x"}, 1 {}, 2 {}, 3 {}, 4 {}, 5 {}, 6 {}, 7 {}, 8 {}} (mark-space (generate-new-board 3) 0 "x"))))

(describe "find-free-spaces"
  (it "returns the board with only the free spaces left"
    (should= {1 {}, 2 {}, 3 {}, 4 {}, 5 {}, 6 {}, 7 {}, 8 {}} (find-free-spaces (mark-space (generate-new-board 3) 0 "x")))))
