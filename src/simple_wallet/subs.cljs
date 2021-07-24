(ns simple-wallet.subs
  (:require
   [oops.core :refer [ocall oget]]
   [re-frame.core :refer [reg-sub]]))

(reg-sub
 :account
 (fn [db _]
   (:account db)))

(reg-sub
 :balance
 (fn [db _]
   (or (:balance db) 0)))

(reg-sub
 :amount
 (fn [db _]
   (:amount db)))

(reg-sub
 :target
 (fn [db _]
   (:target db)))