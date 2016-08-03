(ns matts-clojure-ttt.generative-spec
  (:require [speclj.core :refer :all]
            [matts-clojure-ttt.board :as board]
            [matts-clojure-ttt.player :as player])
  (:import [matts_clojure_ttt.player ComputerPlayer]))

(def human-marker "x")
(def ai-marker "o")
(def computer-player (ComputerPlayer.))

(defn simulate-possible-human-moves
  [boards human-marker]
  ((comp vec flatten) (map (fn [board] (map #(board/mark-space board % human-marker) (keys (board/find-free-spaces board)))) boards)))

(defn simulate-computer-moves
  [boards ai-marker]
  ((comp vec flatten) (map (fn [board] (board/mark-space board (player/get-move computer-player board ai-marker) ai-marker)) boards)))

(defn simulate-possible-games
  ([] (simulate-possible-games [ai-marker human-marker] [simulate-computer-moves simulate-possible-human-moves] [(board/generate-new-board 3)] []))
  ([markers simulate-move-functions in-progress-boards completed-boards]
    (if (empty? in-progress-boards)
      completed-boards
      (let [new-in-progress-boards ((first simulate-move-functions) in-progress-boards (first markers))]
        (recur (reverse markers) (reverse simulate-move-functions) (vec (remove #(board/game-over? %) new-in-progress-boards)) ((comp vec concat) completed-boards (vec (filter #(board/game-over? %) new-in-progress-boards))))))))

(tags :slow
  (describe "get-move for ComputerPlayer"
    (before
      (def possible-games (simulate-possible-games)))

    (it "never allows the human player to win"
      (should= 0 (count (filter #(= human-marker (board/get-winner %)) possible-games))))))

(run-specs)
