(ns eventbusbridge-cljs.client
  (:require [vertx.client.eventbus :as eb]
            [enfocus.core :as ef]
            [enfocus.events :as events])
  (:require-macros [enfocus.macros :as em]))

(def eb (atom nil))

(defn open-eventbus []
  (let [eventbus (eb/eventbus "http://localhost:8080/eventbus")]
      (eb/on-open eventbus (fn [bus]
                             (reset! eb bus)
                             (ef/at "#status_info" (ef/content "connected"))))
      (eb/on-close eventbus (fn []
                              (ef/at
                               "#status_info" (ef/content "Not connected"))
                              (reset! eb nil)))))

(defn close-eventbus []
  (when @eb
    (eb/close @eb)))

(defn append-text [id txt]
  (ef/at
       id (ef/append
           (ef/html [:div
                     [:code txt]]))))

(defn display-message [id addr m]
  (append-text id 
               (str "Address: " addr
                    " Message: " m)))

(defn publish [addr message]
  (when @eb
    (let [m {:text message}]
      (eb/publish @eb addr m)
      (display-message "#sent" addr m))))

(defn subscribe [addr]
  (when @eb
    (eb/on-message @eb addr (partial display-message "#received" addr))
    (append-text "#subscribed"
                 (str "Address:" addr))))

(defn get-value [id]
  (ef/from id (ef/get-prop :value)))

(defn on-click [f]
  (fn [node]
    ((events/listen :click f) node)))

(defn init []
  (ef/at
   "#sendButton"      (on-click
                       #(publish (get-value "#sendAddress")
                                 (get-value "#sendMessage")))
   "#subscribeButton" (on-click
                       #(subscribe (get-value "#subscribeAddress")))
   "#closeButton"     (on-click close-eventbus)
   "#connectButton"   (on-click open-eventbus)))

(set! (.-onload js/window) init)
