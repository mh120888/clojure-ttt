(ns clojure-ttt.generative-spec
  (:require [speclj.core :refer :all]
            [clojure-ttt.core :as core]
            [clojure-ttt.player :as player])
  (:import [clojure_ttt.player ComputerPlayer]))

(def human-marker "x")
(def ai-marker "o")
(def computer-player (ComputerPlayer.))

(defn simulate-possible-human-moves
  [boards human-marker]
  ((comp vec flatten) (map (fn [board] (map #(core/mark-space board % human-marker) (keys (core/find-free-spaces board)))) boards)))

(defn simulate-computer-moves
  [boards ai-marker]
  ((comp vec flatten) (map (fn [board] (core/mark-space board (player/get-move computer-player board ai-marker) ai-marker)) boards)))

(defn simulate-possible-games
  ([] (simulate-possible-games [ai-marker human-marker] [simulate-computer-moves simulate-possible-human-moves] [(core/generate-new-board 3)] []))
  ([markers simulate-move-functions in-progress-boards completed-boards]
    (if (empty? in-progress-boards)
      completed-boards
      (let [new-in-progress-boards ((first simulate-move-functions) in-progress-boards (first markers))]
        (recur (reverse markers) (reverse simulate-move-functions) (vec (remove #(core/stop-game? %) new-in-progress-boards)) ((comp vec concat) completed-boards (vec (filter #(core/stop-game? %) new-in-progress-boards))))))))

(tags :slow
  (describe "get-move for ComputerPlayer"
    (before
      (def possible-games (simulate-possible-games)))

    (it "never allows the human player to win"
      (should= 0 (count (filter #(= human-marker (core/has-won? %)) possible-games))))))

(run-specs)
