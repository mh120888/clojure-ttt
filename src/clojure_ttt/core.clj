(ns clojure-ttt.core)

(defn get-other-marker
  [marker]
  (if (= "x" marker)
    "o"
    "x"))

(defn top-right-to-bottom-left-coords
  ([num-of-rows]
    (top-right-to-bottom-left-coords (dec num-of-rows) (dec num-of-rows)))
  ([current incrementer]
    (let [next-num (+ current incrementer)]
      (cons current (lazy-seq (top-right-to-bottom-left-coords next-num incrementer))))))

(defn top-left-to-bottom-right-coords
  ([num-of-rows]
    (top-left-to-bottom-right-coords 0 num-of-rows))
  ([current incrementer]
    (let [next-num (+ (inc current) incrementer)]
      (cons current (lazy-seq (top-left-to-bottom-right-coords next-num incrementer))))))

(defn generate-diagonal-coords
  [num-of-rows]
  (concat (list (take num-of-rows (top-left-to-bottom-right-coords num-of-rows)))
          (list (take num-of-rows (top-right-to-bottom-left-coords num-of-rows)))))

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

(defn is-integer?
  [space]
  (integer? (read-string space)))

(defn valid-move?
  [board space]
  (and (is-integer? space)
       (space-free? board (Integer/parseInt space))
       (is-space-on-board? board (Integer/parseInt space))))

(defn get-number-of-rows
  [board]
  (int (java.lang.Math/sqrt (count board))))

(defn get-winner
  [board]
  (let [num-of-rows (get-number-of-rows board)
        spaces-on-board (range (count board))
        horizontal-coords (partition num-of-rows spaces-on-board)
        vertical-coords (apply map list horizontal-coords)
        diagonal-coords (generate-diagonal-coords num-of-rows)
        coords-to-check (concat horizontal-coords vertical-coords diagonal-coords)]
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

(defn stop-game?
  [board]
  (or (and (get-winner board) true) (board-full? board)))

(defn mark-space
  [board space mark]
  (if (space-free? board space)
    (merge board {space {:marked mark}})
    (throw (Error. "That space is not empty"))))

(defn generate-new-board
  [num-of-rows]
  (let [num-of-spaces (* num-of-rows num-of-rows)]
    (reduce #(assoc %1 %2 {}) {} (range num-of-spaces))))
