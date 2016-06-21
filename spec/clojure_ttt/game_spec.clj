(ns clojure-ttt.game-spec
  (:require [speclj.core :refer :all]
    [clojure-ttt.core :refer :all]
    [clojure-ttt.game :refer :all])

(describe "Game"
  (describe "get-starting-marker"
    (it "returns the human marker if the human player chose to play first"
      (let [human-marker "x"
            human-goes-first "y"]
      (should= "x" (get-starting-marker human-marker human-goes-first))))

    (it "returns the other marker if the human player chose not to play first"
      (let [human-marker "x"
            human-goes-first "n"]
        (should= "o" (get-starting-marker human-marker human-goes-first))))))
