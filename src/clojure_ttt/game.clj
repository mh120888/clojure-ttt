(ns clojure-ttt.game
  (:require [clojure-ttt.core :refer :all]
    [clojure-ttt.console-ui :refer :all]
    [clojure-ttt.ai-player :refer :all]))

(defn get-starting-marker
  [human-marker human-goes-first]
  (if (= "y" human-goes-first)
    human-marker
    (get-other-marker human-marker)))