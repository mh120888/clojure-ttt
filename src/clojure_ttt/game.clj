(ns clojure-ttt.game
  (:gen-class)
  (:require [clojure-ttt.core :as core]
    [clojure-ttt.console-ui :as console-ui]
    [clojure-ttt.player :as player])
  (:import [clojure_ttt.console_ui ConsoleIO]
           [clojure_ttt.player HumanPlayer ComputerPlayer]))

(defn get-starting-marker
  [human-marker human-goes-first]
  (if human-goes-first
    human-marker
    (core/get-other-marker human-marker)))

(defn play-game
  [io-channel players]
    (console-ui/io-print-line io-channel "Let's play a game of tic tac toe")
    (let [human-marker (console-ui/get-human-marker io-channel)
          human-goes-first (= "y" (console-ui/do-you-want-to-go-first io-channel))
          players (if human-goes-first
                    players
                    (reverse players))
          starting-marker (get-starting-marker human-marker human-goes-first)
          new-board (core/generate-new-board (console-ui/get-board-size io-channel))]
          (console-ui/print-board io-channel new-board)
          (loop [current-marker starting-marker
                 players players
                 board new-board
                 human-marker human-marker]
            (let [next-board (core/mark-space board (player/get-move (first players) board current-marker) current-marker)]
              (console-ui/print-board io-channel next-board)
              (if (core/stop-game? next-board)
                (console-ui/io-print-line io-channel "Game over")
                (recur (core/get-other-marker current-marker) (reverse players) next-board human-marker))))))

(defn -main
  []
  (play-game (ConsoleIO.) [(HumanPlayer. (ConsoleIO.)) (ComputerPlayer.)]))
