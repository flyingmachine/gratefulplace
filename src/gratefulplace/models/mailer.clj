(ns gratefulplace.models.mailer
  (:use gratefulplace.utils
        gratefulplace.paths
        environ.core)
  (:import org.apache.commons.mail.SimpleEmail))

(defn msg 
  "format the message"
  [username comment]
  (str "Hi " username ",

You've received a new comment on Grateful Place. You can reply to it by visiting http://gratefulplace.com/posts/" (:post_id comment) ". Here's the comment:

" (:content comment) "


=========
You can change your email preferences by going to http://gratefulplace.com" (user-edit-path username) "

If you'd like to stop receiving emails but don't want to log in, please email notifications@gratefulplace.com, and I apologize for the inconvenience - one-click unsubscribe is on my to-do list.

Take care!
Daniel Higginbotham of Grateful Place"))

(defn send-new-comment 
  "send an email for a new comment"
  [user comment]
  (doto (SimpleEmail.)
    (.setHostName "smtp.gmail.com")
    (.setSslSmtpPort "465")
    (.setSSL true)
    (.addTo (:email user))
    (.setFrom (env :gp-email-from-address) (env :gp-email-from-name))
    (.setSubject "You have a new comment on Grateful Place")
    (.setMsg (msg (:username user) comment))
    (.setAuthentication (env :gp-email-username) (env :gp-email-password))
    (.send)))