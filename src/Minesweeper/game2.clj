(ns Minesweeper.game2
  (:require [clojure.string :as str]))

; This namespace defines the Minesweeper game in Clojure.
; It requires the 'clojure.string' library and aliases it as 'str1 for convenience

(defn board-creator
  "Create the Minesweeper board."
  [lines columns]
  (vec (repeat lines (vec (repeat columns :0)))))

(defn place-bombs
  "Place bombs randomly on the board."
  [board bombs]
  (loop [board board bombs bombs]
    (if (zero? bombs)
      board
      (let [line (rand-int (count board))
            column (rand-int (count (first board)))]
        (recur (assoc-in board [line column] :bomb)
               (dec bombs))))))

(defn show-board
  "Render the entire Minesweeper board on the terminal."
  [board]
  (let [lines (count board)
        columns (count (first board))]
    (println "   " (str/join "   " (map #(format "%2s" %) (range columns))))
    (doseq [line (map vector (range lines) board)]
      (println (format "%2d |" (first line))
               (str/join " | "
                         (map #(if (= % :bomb) "xx" (if (= % :bomb-exploded) " *" (format "%2s" %))) (second line)))))))



;fn -> Anonymous function to count neighboring bombs around a cell.
(defn counting-neighboring-bombs
  "Check how many bombs there are around the player's choice."
  [board line column]
  (let [lines (count board)
        columns (count (first board))
        position [[-1 -1] [-1 0] [-1 1]
                  [0 -1]         [0 1]
                  [1 -1]  [1 0]  [1 1]]]
    (->> position
         (map (fn [[dx dy]] [(+ line dx) (+ column dy)]))
         (filter (fn [[x y]]
                   (and (>= x 0) (< x lines)
                        (>= y 0) (< y columns))))
         (map (fn [[x y]] (get-in board [x y])))
         (filter #(= :bomb %))
         count)))

(defn update-board
  "Update the board after the player's choice [line column]."
  [board line column]
  (if (= :0 (get-in board [line column]))
    (update-in board [line column] (constantly (counting-neighboring-bombs board line column)))
    board))

(defn player-move
  "Function representing a player's move on the board"
  [board line column]
  (if (= :bomb (get-in board [line column]))
    (assoc-in board [line column] :bomb-exploded)
    (update-board board line column)))

(defn lost?
  "Check if the player lost the game."
  [board]
  (some #(= :bomb-exploded %) (flatten board)))

(defn start-game
  "Manage the Minesweeper game."
  []
  (println "Bem-vindo ao Campo Minado!")
  (println "Informe o número de linhas: ")
  (let [lines (read)]
    (println "Informe o número de colunas: ")
    (let [columns (read)]
      (println "Informe o número de bombas: ")
      (let [bombs (read)
            board (-> (board-creator lines columns)
                      (place-bombs bombs))]
        (println "Comecando o jogo!")
        (let [start-time (System/currentTimeMillis)]
          (loop [board board]
            (show-board board)
            (if (lost? board)
              (do (println "Voce perdeu!")
                  (let [end-time (System/currentTimeMillis)
                        elapsed-time (/ (- end-time start-time) 1000)]
                    (println (str "Tempo decorrido: " (int elapsed-time) " segundos."))))
              (do
                (println "Informe a linha: ")
                (let [line (read)]
                  (println "Informe a coluna: ")
                  (let [column (read)]
                    (recur (player-move board line column))))))))))))

(start-game)
