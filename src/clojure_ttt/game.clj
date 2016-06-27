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
  [io-channel players current-marker human-marker board]
  (console-ui/print-board io-channel board)
  (let [next-board (core/mark-space board (player/get-move (first players) board current-marker) current-marker)]
    (if (core/stop-game? next-board)
      (console-ui/show-final-result io-channel next-board human-marker)
      (recur io-channel (reverse players) (core/get-other-marker current-marker) human-marker next-board))))

(defn game-setup
  [io-channel]
  (console-ui/io-print-line io-channel "Let's play a game of tic tac toe")
  (let [human-marker (console-ui/get-human-marker io-channel)
        human-goes-first (= "y" (console-ui/do-you-want-to-go-first io-channel))
        players (if human-goes-first
                  [(HumanPlayer. io-channel) (ComputerPlayer.)]
                  [(ComputerPlayer.) (HumanPlayer. io-channel)])
        starting-marker (get-starting-marker human-marker human-goes-first)
        new-board (core/generate-new-board ((comp read-string console-ui/get-board-size) io-channel))]
    (play-game io-channel players starting-marker human-marker new-board)))

(defn -main
  []
  (game-setup (ConsoleIO.)))
