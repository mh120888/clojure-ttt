(ns clojure-ttt.game-spec
  (:require [speclj.core :refer :all]
            [clojure-ttt.game :as game]))

(describe "Game"
  (describe "game/get-starting-marker"
    (it "returns the human marker if the human player chose to play first"
      (let [human-marker "x"
            human-goes-first true]
        (should= "x" (game/get-starting-marker human-marker human-goes-first))))

    (it "returns the other marker if the human player chose not to play first"
      (let [human-marker "x"
            human-goes-first false]
        (should= "o" (game/get-starting-marker human-marker human-goes-first))))))
