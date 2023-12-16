(ns blackjack.game
  (:require [card-ascii-art.core :as card]))

;Available cards
; A, 2, 3, 4, 5, 6, 7, 8, 9, 10,  J,  Q,  K <=>
;[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]

;Random cards -> (rand-int 20) less 20!

(defn new-card
  []
  "Generates a card number between 1 and 13"
  (inc (rand-int 13)))
;(println (new-card))

;Calculate points
;J, Q, K = 10 points
;[A 5 7] = 1+5+7 (13 points)  ou ;[A 5 7] = 11+5+5 (23 points)
;Ele deverá escolher a melhor mão para o usuário, portanto
; A = 11, porém, se passar de 21, ele vale A = 1

(defn JQK->10 [card]
  (if (> card 10) 10 card))

(defn A->11 [card]
  (if (= card 1) 11 card))
(defn points-cards [cards]
  (let [cards-without-JQK (map JQK->10 cards)
        cards-with-A11 (map A->11 cards-without-JQK)
        points-with-A-1 (reduce + cards-without-JQK)
        points-with-A-11 (reduce + cards-with-A11)]
    (if (> points-with-A-11 21) points-with-A-1 points-with-A-11)))

;(points-cards [1 10])      21
;(points-cards [1 6 7])     14
;(points-cards [1 8 5])     14

; player's map
;
; (def player
;  {
;   :player "Marcos Brito"
;   :cards [4, 10]
;   })

(defn player [player-name]
  (let [card1 (new-card)
        card2 (new-card)
        cards [card1 card2]
        points (points-cards cards)]
    {
     :player-name player-name
     :cards       cards
     :points      points
     }))

(card/print-player (player "Marcos Brito"))
(card/print-player (player "Dealer"))
