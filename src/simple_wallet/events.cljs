(ns simple-wallet.events
  (:require
   ["ethers" :refer [ethers]]
   [re-frame.core :refer [reg-event-db dispatch]]
   [oops.core :refer [ocall oget]]
   [cljs.core.async :refer [go]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [simple-wallet.dai :as dai]))

(reg-event-db
 :write-to
 (fn [db [_ path value]]
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
           signer (ocall provider :getSigner)]
       (dispatch [:merge {:account account
                          :contract contract
                          :signer signer
                          :balance (-> (oget ethers :utils)
                                       (ocall :formatUnits balance 18))}])))
   db))

(reg-event-db
 :transfer
 (fn [{:keys [contract signer target amount] :as db} _]
   (let [contract-with-signer (ocall contract :connect signer)
         amount (-> (oget ethers :utils)
                    (ocall :parseUnits amount 18))]
     (ocall contract-with-signer :transfer target amount))
   db))

(reg-event-db
 :mint
 (fn [{:keys [contract signer account] :as db} _]
   (let [contract-with-signer (ocall contract :connect signer)
         amount (-> (oget ethers :utils)
                    (ocall :parseUnits "10.0" 18))]
     (ocall contract-with-signer :mint account amount))
   db))
