(ns simple-wallet.core
  (:require
   [reagent.dom :as dom]
   [simple-wallet.subs]
   [simple-wallet.events]
   [simple-wallet.views :as views]))

(defn ^:export render []
  (dom/render [views/wallet]
              (js/document.getElementById "app")))

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (js/console.log "start")
  (render))

(defn init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))
