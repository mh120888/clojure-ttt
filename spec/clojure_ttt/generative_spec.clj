(ns clojure-ttt.generative-spec
  (:require [speclj.core :refer :all]
            [clojure-ttt.core :as core]
            [clojure-ttt.game :as game]
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
  ([] (simulate-possible-games human-marker [(core/generate-new-board 3)] []))
  ([current-marker
    in-progress-boards
    completed-boards]
  (if (empty? in-progress-boards)
    completed-boards
    (if (= human-marker current-marker)
      (let [new-in-progress-boards (simulate-possible-human-moves in-progress-boards human-marker)]
        (recur ai-marker (vec (remove #(core/stop-game? %) new-in-progress-boards)) ((comp vec concat) completed-boards (vec (filter #(core/stop-game? %) new-in-progress-boards)))))
      (let [new-in-progress-boards (simulate-computer-moves in-progress-boards ai-marker)]
        (recur human-marker (vec (remove #(core/stop-game? %) new-in-progress-boards)) ((comp vec concat) completed-boards (vec (filter #(core/stop-game? %) new-in-progress-boards)))))))))


(describe "get-move for ComputerPlayer"
  (before
    (def possible-games (simulate-possible-games)))

  (it "never allows the human player to win"
    (should= 0 (count (filter #(= human-marker (core/has-won? %)) possible-games)))))

(run-specs)
