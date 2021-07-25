(ns simple-wallet.events
  (:require
   ["ethers" :refer [ethers]]
   [re-frame.core :refer [reg-event-db dispatch]]
   [oops.core :refer [ocall oget]]
   [cljs.core.async :refer [go]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [simple-wallet.dai :as dai]
   [simple-wallet.db :as app-db]
   [simple-wallet.utils :as u]))

(reg-event-db
 :initialise-db
 (fn [_ _]
   app-db/db))

(reg-event-db
 :write-to
 (fn [db [_ [path value]]]
   (assoc-in db path value)))

(reg-event-db
 :merge
 (fn [db [_ data]]
   (merge db data)))

(reg-event-db
 :init-wallet
 (fn [db _]
   (go
     (let [accounts (<p! (js/window.ethereum.request #js {:method "eth_requestAccounts"}))
           account (first accounts)
           Web3Provider (oget ethers "providers.Web3Provider")
           provider (Web3Provider. js/window.ethereum)
           Contract (oget ethers "Contract")
           contract (Contract. dai/address dai/abi provider)
           balance (<p! (ocall contract :balanceOf account))
           signer (ocall provider :getSigner)
           ;; filtering events with our account
           filter-to (-> (oget contract :filters)
                         (ocall :Transfer nil account))
           filter-from (-> (oget contract :filters)
                           (ocall :Transfer account nil))]
       (ocall contract :on filter-to #(dispatch [:update-balance %]))
       (ocall contract :on filter-from #(dispatch [:update-balance %]))
       ;; saving initial data to the state
       (dispatch [:merge {:account account
                          :contract contract
                          :signer signer
                          :balance (u/format balance)}])))
   db))


;; updates balance on 'Transfer' event
(reg-event-db
 :update-balance
 (fn [{:keys [contract account] :as db} _]
   (go
     (let [balance (<p! (ocall contract :balanceOf account))]
       (dispatch [:merge {:balance (u/format balance)}])))
   db))


;; transfer DAI to another account
(reg-event-db
 :transfer
 (fn [{:keys [contract signer target amount] :as db} _]
   (let [contract-with-signer (ocall contract :connect signer)
         amount (u/parse amount)]
     (ocall contract-with-signer :transfer target amount))
   db))

;; mints 10 DAI
(reg-event-db
 :mint
 (fn [{:keys [contract signer account] :as db} _]
   (let [contract-with-signer (ocall contract :connect signer)
         amount (u/parse "10")]
     (ocall contract-with-signer :mint account amount))
   db))
