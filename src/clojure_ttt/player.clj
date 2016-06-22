(ns clojure-ttt.player
  (:require [clojure-ttt.ai-player :refer :all]
            [clojure-ttt.console-ui :refer :all]
            [clojure-ttt.core :refer :all]))

(defprotocol Player
  (get-move [type board marker] [type board marker message]))

(deftype ComputerPlayer []
  Player
  (get-move [type board marker]
    (let [moves-and-scores (negamax board 0 marker 1)
          potential-moves (flatten-score-map moves-and-scores)]
      (first (last (sort-by val potential-moves))))))

(deftype HumanPlayer [io-channel]
  Player
  (get-move [type board marker] (get-move type board marker (str "Where would you like to play? Please enter a number between 0 and " (int (dec (count board))))))
  (get-move [type board marker message]
    (loop [type type
           board board
           marker marker
           message message]
    (let [response (get-user-input io-channel message)]
      (if (valid-move? board response)
        (Integer/parseInt response)
        (recur type board marker (str "Sorry that's not a valid move. Please enter a number between 0 and " (int (dec (count board)))" that isn't already taken.")))))))
