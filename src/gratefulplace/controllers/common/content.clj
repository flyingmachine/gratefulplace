(ns gratefulplace.controllers.common.content
  (:use gratefulplace.models.permissions))

(defn updated
  [params]
  (markdown/md-to-html-string (:content params)))

(defn update-fn
  [finder-fn record-update-fn]
  (fn [params]
    (let [id     (:id params)
          record (finder-fn id)]
      (protect
       (modify-content? (:user_id record))
       (record-update-fn {:id id} params)
       (updated params)))))