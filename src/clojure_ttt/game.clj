(ns clojure-ttt.game
  (:gen-class)
  (:require [clojure-ttt.core :refer :all]
    [clojure-ttt.console-ui :refer :all]
    [clojure-ttt.ai-player :refer :all]
    [clojure-ttt.player :refer :all])
  (:import [clojure_ttt.console_ui ConsoleIO]
           [clojure_ttt.player HumanPlayer ComputerPlayer]))

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
