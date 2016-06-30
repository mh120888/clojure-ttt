(ns clojure-ttt.ai-player-spec
  (:require [speclj.core :refer :all]
            [clojure-ttt.board :as board]
            [clojure-ttt.ai-player :as ai-player]
            [clojure-ttt.game :as game]
            [clojure-ttt.player :as player])
  (:import [clojure_ttt.player ComputerPlayer]))

(describe "ComputerPlayer.get-move"
  (before-all
    (def computer-player (ComputerPlayer.))

    (def new-board (board/generate-new-board 3))

    (def board-with-only-one-space-open
      (-> (board/generate-new-board 3)
          (board/mark-space 0 "x")
          (board/mark-space 1 "o")
          (board/mark-space 3 "o")
          (board/mark-space 4 "x")
          (board/mark-space 5 "x")
          (board/mark-space 6 "x")
          (board/mark-space 7 "o")
          (board/mark-space 8 "o")))

    (def board-with-imminent-win
      (-> (board/generate-new-board 3)
          (board/mark-space 0 "x")
          (board/mark-space 1 "o")
          (board/mark-space 2 "o")
          (board/mark-space 3 "o")
          (board/mark-space 4 "x")
          (board/mark-space 6 "x")
          (board/mark-space 7 "o")))

    (def board-with-imminent-loss
      (-> (board/generate-new-board 3)
          (board/mark-space 0 "x")
          (board/mark-space 1 "x")
          (board/mark-space 2 "o")
          (board/mark-space 3 "o")
          (board/mark-space 4 "o")
          (board/mark-space 6 "x")
          (board/mark-space 7 "o")))

    (def another-board-with-imminent-loss
      (-> (board/generate-new-board 3)
          (board/mark-space 3 "x")
          (board/mark-space 4 "o")
          (board/mark-space 6 "o")
          (board/mark-space 8 "x"))))

  (it "returns the only possible move on a board that has only one available space"
    (should= 2 (player/get-move computer-player board-with-only-one-space-open "x")))

  (it "returns the move that will allow the given player to win"
    (should= 8 (player/get-move computer-player board-with-imminent-win "x")))

  (it "returns the move that will prevent the opponent from winning"
    (should= 5 (player/get-move computer-player board-with-imminent-loss "x")))

  (it "returns the move that will prevent the opponent from winning"
    (should= 2 (player/get-move computer-player another-board-with-imminent-loss "x"))))

(describe "ai-player/score-board"
  (before-all
    (def board-where-x-wins
      (-> new-board
          (board/mark-space 0 "o")
          (board/mark-space 1 "o")
          (board/mark-space 2 "x")
          (board/mark-space 3 "o")
          (board/mark-space 4 "x")
          (board/mark-space 5 "o")
          (board/mark-space 6 "x")
          (board/mark-space 7 "o")
          (board/mark-space 8 "x")))

    (def board-with-cats-game
      (-> new-board
          (board/mark-space 0 "o")
          (board/mark-space 1 "o")
          (board/mark-space 2 "x")
          (board/mark-space 3 "x")
          (board/mark-space 4 "x")
          (board/mark-space 5 "o")
          (board/mark-space 6 "o")
          (board/mark-space 7 "o")
          (board/mark-space 8 "x"))))

  (it "gives a positive score if the given player has won"
    (should= true (< 0 (ai-player/score-board board-where-x-wins "x" 1))))

  (it "gives a negative score if the given player has lost"
    (should= false (< 0 (ai-player/score-board board-where-x-wins "o" 1))))

  (it "returns 0 if it's a cat's game"
    (should= 0 (ai-player/score-board board-with-cats-game "x" 1))))
