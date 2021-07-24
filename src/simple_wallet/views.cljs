(ns simple-wallet.views
  (:require
   [re-frame.core :refer [subscribe dispatch]]))

(defn mint-button []
  [:button {:style {:margin 25}
            :on-click #(dispatch [:mint])}
   "Mint 10 DAI"])

(defn wallet []
  (dispatch [:init-wallet])
  (fn []
    [:div.flex-col.align-center
     [:div.container.flex-col
      [:h3 "Testnet DAI dashboard"]
      [:p "Your address " @(subscribe [:account])]
      [:p "Your Balance " @(subscribe [:balance]) " DAI"]
      [:p "Make a payment"]
      [:p.flex-row
       [:div {:style {:margin-left 15
                      :margin-right 15}}
        "to"]
       [:input {:style {:width "100%"}
                :on-change #(dispatch [:write-to [[:target] (.. % -target -value)]])}]]
      [:p.flex-row
       [:div {:style {:margin-right 15}}
        "amount"]
       [:input {:style {:width 100}
                :default-value "1"
                :type "number"
                :min "0"
                :on-change #(dispatch [:write-to [[:amount] (.. % -target -value)]])}]
       [:div {:style {:margin-left 15}}
        "DAI"]]
      [:div.flex-row.justify-end
       [:button {:on-click #(dispatch [:transfer])}
        "Send"]]]
     [mint-button]]))
