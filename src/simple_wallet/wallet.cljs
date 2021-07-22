(ns simple-wallet.wallet
  (:require
   ["ethers" :refer [ethers]]
   [oops.core :refer [ocall oget]]))

(def Web3Provider (oget ethers "providers.Web3Provider"))
(def Contract (oget ethers "Contract"))
(def dai-address "0x8ad3aa5d5ff084307d28c8f514d7a193b2bfe725") ;;RinkebyDai Token
(def abi #js [
    ;; Read-Only Functions
    "function balanceOf(address owner) view returns (uint256)",
    "function decimals() view returns (uint8)",
    "function symbol() view returns (string)",

    ;; Authenticated Functions
    "function transfer(address to, uint amount) returns (boolean)",

    ;; Events
    "event Transfer(address indexed from, address indexed to, uint amount)"
])

(defn display-balance [erc20 account]
  (-> erc20
      (ocall :balanceOf account)
      (.then js/console.log)))

(defn connect-metamask [callback]
  (-> (js/window.ethereum.request #js {:method "eth_requestAccounts"})
      (.then callback)))

(defn init []
  (let [provider (Web3Provider. js/window.ethereum)
        erc20 (Contract. dai-address abi provider)]
    (connect-metamask (fn [accounts]
                        (let [account (first accounts)]
                          (display-balance erc20 account))))))

(defn transfer [contract target amount]
  (ocall contract :transfer target amount))