(ns clojure-ttt.game
  (:gen-class)
  (:require [clojure-ttt.core :refer :all]
    [clojure-ttt.console-ui :refer :all]
    [clojure-ttt.ai-player :refer :all])
  (:import [clojure_ttt.console_ui ConsoleIO]))

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

(defn get-starting-marker
  [human-marker human-goes-first]
  (if human-goes-first
    human-marker
    (get-other-marker human-marker)))

(defn play-game
  [io-channel players]
    (io-print-line io-channel "Let's play a game of tic tac toe")
    (let [human-marker (get-human-marker io-channel)
          human-goes-first (= "y" (do-you-want-to-go-first io-channel))
          players (if human-goes-first
                    players
                    (reverse players))
          starting-marker (get-starting-marker human-marker human-goes-first)
          new-board (generate-new-board (get-board-size io-channel))]
          (print-board io-channel new-board)
          (loop [current-marker starting-marker
                 players players
                 board new-board
                 human-marker human-marker]
            (let [next-board (mark-space board (get-move (first players) board current-marker) current-marker)]
              (print-board io-channel next-board)
              (if (stop-game? next-board)
                (io-print-line io-channel "Game over")
                (recur (get-other-marker current-marker) (reverse players) next-board human-marker))))))

(defn -main
  []
  (play-game (ConsoleIO.) [(HumanPlayer. (ConsoleIO.)) (ComputerPlayer.)]))
