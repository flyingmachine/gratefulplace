(ns gratefulplace.lib.twitter
  (:require twitter
            [oauth.client :as oauth]))

;; TODO find a happier home for this stuff
(def ^:private oauth-consumer
  (oauth/make-consumer (System/getenv "TWITTER_CONSUMER_KEY")
                       (System/getenv "TWITTER_CONSUMER_SECRET")
                       "https://api.twitter.com/oauth/request_token"
                       "https://api.twitter.com/oauth/access_token"
                       "https://api.twitter.com/oauth/authorize"
                       :hmac-sha1))
(def ^:private oauth-access-token (System/getenv "TWITTER_ACCESS_TOKEN"))
(def ^:private oauth-access-token-secret (System/getenv "TWITTER_ACCESS_TOKEN_SECRET"))

(defn send-tweets!
  [content path]
  (twitter/with-oauth
    oauth-consumer
    oauth-access-token
    oauth-access-token-secret
    (doseq [twitter-name (re-seq #"@\S+" content)]
      (twitter/update-status (str "Hi, " twitter-name ". Someone's expressed their gratitude for you: http://gratefulplace.com" path)))))