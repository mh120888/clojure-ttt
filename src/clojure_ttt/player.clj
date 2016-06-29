(ns clojure-ttt.player
  (:require [clojure-ttt.ai-player :as ai-player]
            [clojure-ttt.console-ui :as console-ui]
            [clojure-ttt.board :as board]))

(defprotocol Player
  (get-move [type board marker] [type board marker message]))

(deftype ComputerPlayer []
  Player
  (get-move [type board marker]
    (let [all-game-possibilities (ai-player/negamax board 0 marker 1)
          all-possible-next-moves-and-scores (ai-player/flatten-score-map all-game-possibilities)]
      ((comp first last) (sort-by val all-possible-next-moves-and-scores)))))

(deftype HumanPlayer [io-channel]
  Player
  (get-move [type board _]
            (get-move type board _ (str "Where would you like to play? Please enter a number between 0 and " ((comp int dec count) board))))
  (get-move [type board _ message]
            (loop [type type
                   board board
                   _ _
                   message message]
            (let [response (console-ui/get-user-input io-channel message)]
              (if (board/valid-move? board response)
                (Integer/parseInt response)
                (recur type board _ (str "Sorry that's not a valid move. Please enter a number between 0 and " ((comp int dec count) board) " that isn't already taken.")))))))
