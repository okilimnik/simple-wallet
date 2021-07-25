(ns simple-wallet.utils
  (:require
   ["ethers" :refer [ethers]]
   [oops.core :refer [ocall oget]]))

(defn format [n]
  (-> (oget ethers :utils)
      (ocall :formatUnits n 18)))

(defn parse [s]
  (-> (oget ethers :utils)
      (ocall :parseUnits s 18)))