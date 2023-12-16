(ns blackjack.game
  (:require [card-ascii-art.core :as card]))

;Cartas dispioníveis
; A, 2, 3, 4, 5, 6, 7, 8, 9, 10,  J,  Q,  K <=>
;[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]

;Gerando cartas randomicamente -> (rand-int 20) o 20 não está incluso!

(defn new-card
  []
  "Generates a card number between 1 and 13"
  (inc (rand-int 13)))
;(println (new-card))

; player's map
;
; (def jogador
;  {
;   :player "Marcos Brito"
;   :cards [4, 10]
;   })

;let -> escopo local, portanto não podera ser acessado fora do contexto ()
(defn player [player-name]
  (let [card1 (new-card)
        card2 (new-card)]
    {
     :player-name player-name
     :cards       [card1, card2]}))

(card/print-player (player "Marcos Brito"))
(card/print-player (player "Dealer"))