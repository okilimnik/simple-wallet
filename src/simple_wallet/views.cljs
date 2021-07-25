(ns simple-wallet.views
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [subscribe dispatch]]))

(defn container []
  [:div.flex-col.align-center
   (into [:<>] (r/children (r/current-component)))])

(defn wallet-container []
  [:div.container.flex-col
   (into [:<>] (r/children (r/current-component)))])

(defn title []
  [:h3 "Testnet DAI dashboard"])

(defn user-address []
  [:p "Your address " @(subscribe [:account])])

(defn user-balance []
  [:p "Your Balance " @(subscribe [:balance]) " DAI"])

(defn payment-form-title []
  [:p "Make a payment"])

(defn recipient-address []
  (let [on-change #(dispatch [:write-to [[:target] (.. % -target -value)]])]
    [:div.flex-row.with-padding
     [:div {:style {:margin-left 15
                    :margin-right 15}}
      "to"]
     [:input {:style {:width "100%"}
              :value @(subscribe [:target])
              :on-change on-change}]]))

(defn sending-amount []
  (let [on-change #(dispatch [:write-to [[:amount] (.. % -target -value)]])]
    [:div.flex-row.with-padding
     [:div {:style {:margin-right 15}}
      "amount"]
     [:input {:style {:width 100}
              :value @(subscribe [:amount])
              :type "number"
              :min "0"
              :on-change on-change}]
     [:div {:style {:margin-left 15}}
      "DAI"]]))

(defn send-button []
  (let [on-click #(dispatch [:transfer])]
    [:div.flex-row.justify-end
     [:button {:on-click on-click}
      "Send"]]))

(defn payment-form []
  [:<>
   [payment-form-title]
   [recipient-address]
   [sending-amount]
   [send-button]])

(defn mint-button []
  (let [on-click #(dispatch [:mint])]
    [:button {:style {:margin 25}
              :on-click on-click}
     "Mint 10 DAI"]))

(defn wallet []
  [container
   [wallet-container
    [title]
    [user-address]
    [user-balance]
    [payment-form]]
   [mint-button]])
