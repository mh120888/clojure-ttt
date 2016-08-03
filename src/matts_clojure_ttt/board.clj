(ns matts-clojure-ttt.board)

(defn get-other-marker
  [marker]
  (if (= "x" marker)
    "o"
    "x"))

(defn look-up-space
  [board space]
  (get-in board [space :marked]))

(defn find-spaces
  ([f board] (into {} (filter #(f (look-up-space board (first %))) board)))
  ([f value board] (into {} (filter #(f value (look-up-space board (first %))) board))))

(defn find-spaces-taken-by
  [board marker]
  (find-spaces = marker board))

(defn find-taken-spaces
  [board]
  (find-spaces identity board))

(defn find-free-spaces
  [board]
  (find-spaces nil? board))

(defn is-space-on-board?
  [board space]
  (< -1 space (count board)))

(defn space-free?
  [board space]
  ((comp nil? look-up-space) board space))

(defn is-integer-as-string?
  [space]
  (integer? (read-string space)))

(defn valid-move?
  [board space]
  (and (is-integer-as-string? space)
       (space-free? board (Integer/parseInt space))
       (is-space-on-board? board (Integer/parseInt space))))

(defn get-number-of-rows
  [board]
  (int (java.lang.Math/sqrt (count board))))

(def memoize-get-number-of-rows (memoize get-number-of-rows))

(defn build-diagonal-lazy-seq
  [current incrementer]
  (let [next-num (+ current incrementer)]
      (cons current (lazy-seq (build-diagonal-lazy-seq next-num incrementer)))))

(defn top-right-to-bottom-left-coords
  ([num-of-rows]
    (take num-of-rows (build-diagonal-lazy-seq (dec num-of-rows) (dec num-of-rows)))))

(defn top-left-to-bottom-right-coords
  ([num-of-rows]
    (take num-of-rows (build-diagonal-lazy-seq 0 (inc num-of-rows)))))

(defn generate-diagonal-coords
  [num-of-rows]
  (concat (list (top-left-to-bottom-right-coords num-of-rows))
          (list (top-right-to-bottom-left-coords num-of-rows))))

(defn generate-horizontal-coords
  [num-of-rows]
  (partition num-of-rows (range (* num-of-rows num-of-rows))))

(defn generate-vertical-coords
  [num-of-rows]
  (apply map list (generate-horizontal-coords num-of-rows)))

(defn generate-all-winning-coords
  [num-of-rows]
  (concat (generate-horizontal-coords num-of-rows) (generate-vertical-coords num-of-rows) (generate-diagonal-coords num-of-rows)))

(def memoize-generate-all-winning-coords (memoize generate-all-winning-coords))

(defn get-winner
  [board]
  (let [num-of-rows (memoize-get-number-of-rows board)
        coords-to-check (memoize-generate-all-winning-coords num-of-rows)]
    (loop [coords-to-check coords-to-check
           row-to-check (first coords-to-check)
           winner nil]
      (if (or winner (empty? coords-to-check))
        winner
        (let [this-row-as-set (into #{} (vals (select-keys board (into [] (first coords-to-check)))))
              is-this-a-win (and (seq (first this-row-as-set)) (= 1 (count this-row-as-set)))
              winning-marker (or (and is-this-a-win (:marked (first this-row-as-set))) nil)]
          (recur (rest coords-to-check) (first (rest coords-to-check)) winning-marker))))))

(defn board-full?
  [board]
  (empty? (find-free-spaces board)))

(defn game-over?
  [board]
  (or (and (get-winner board) true) (board-full? board)))

(defn mark-space
  [board space mark]
  (if (space-free? board space)
    (merge board {space {:marked mark}})
    (throw (Error. "That space is not empty"))))

(defn generate-new-board
  [num-of-rows]
    (reduce #(assoc %1 %2 {}) {} (range (* num-of-rows num-of-rows))))
