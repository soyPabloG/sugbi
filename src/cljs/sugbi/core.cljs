(ns sugbi.core
  (:require
    [day8.re-frame.http-fx]
    [reagent.dom :as rdom]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [goog.events :as events]
    [goog.history.EventType :as HistoryEventType]
    [markdown.core :refer [md->html]]
    [sugbi.ajax :as ajax]
    [sugbi.events]
    [reitit.core :as reitit]
    [reitit.frontend.easy :as rfe]
    [clojure.string :as string])
  (:import goog.History))

(defn nav-link [uri title page]
  [:a.navbar-item
   {:href   uri
    :class (when (= page @(rf/subscribe [:common/page-id])) :is-active)}
   title])

(defn navbar [] 
  (r/with-let [expanded? (r/atom false)]
              [:nav.navbar.is-info>div.container
               [:div.navbar-brand
                [:a.navbar-item {:href "/" :style {:font-weight :bold}} "sugbi"]
                [:span.navbar-burger.burger
                 {:data-target :nav-menu
                  :on-click #(swap! expanded? not)
                  :class (when @expanded? :is-active)}
                 [:span][:span][:span]]]
               [:div#nav-menu.navbar-menu
                {:class (when @expanded? :is-active)}
                [:div.navbar-start
                 [nav-link "#/" "Home" :home]
                 [nav-link "#/about" "About" :about]]]]))

(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]])

(defn home-page []
  [:section.section>div.container>div.content
   (when-let [docs @(rf/subscribe [:docs])]
     [:div {:dangerouslySetInnerHTML {:__html (md->html docs)}}])])

(defn calc-bmi
  [{:keys [height weight bmi] :as data}]
  (let [h (/ height 100)]
    (if (nil? bmi)
      (assoc data :bmi (/ weight (* h h)))
      (assoc data :weight (* bmi h h)))))

(defn slider
  [atom param value min max invalidates]
  [:input {:type      :range
           :value     value
           :min       min
           :max       max
           :on-change (fn [e]
                        (let [new-value (js/parseInt (-> e .-target .-value))]
                          (swap! atom (fn [data]
                                        (-> data
                                            (assoc param new-value)
                                            (dissoc invalidates)
                                            calc-bmi)))))}])

(defn color-diagnose
  [bmi]
  (cond
    (< bmi 18.5) [:orange "underweight"]
    (< bmi 25)   [:inherit "normal"]
    (< bmi 30)   [:orange "overweight"]
    :else        [:red "obese"]))

(defn bmi-component []
  (let [bmi-data (r/atom (calc-bmi {:height 180 :weight 80}))]
    (fn []
      (let [{:keys [height weight bmi]} @bmi-data
            [color diagnose]            (color-diagnose bmi)]
        [:div
         [:h1 "BMI calculator"]
         [:div
          "Height: " (int height) "cm " [:br]
          [slider bmi-data :height height 100 220 :bmi]]
         [:div
          "Weight: " (int weight) "kg " [:br]
          [slider bmi-data :weight weight 30 150 :bmi]]
         [:div
          "BMI " (int bmi) " " [:br]
          [:span {:style {:color color}} diagnose] [:br]
          [slider bmi-data :bmi bmi 10 50 :weight]]]))))


(defn page []
  (if-let [page @(rf/subscribe [:common/page])]
    [:div
     [navbar]
     [bmi-component]]))

(defn navigate! [match _]
  (rf/dispatch [:common/navigate match]))

(def router
  (reitit/router
    [["/" {:name        :home
           :view        #'home-page
           :controllers [{:start (fn [_] (rf/dispatch [:page/init-home]))}]}]
     ["/about" {:name :about
                :view #'about-page}]]))

(defn start-router! []
  (rfe/start!
    router
    navigate!
    {}))

;; -------------------------
;; Initialize app
(defn ^:dev/after-load mount-components []
  (rf/clear-subscription-cache!)
  (rdom/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (start-router!)
  (ajax/load-interceptors!)
  (mount-components))
