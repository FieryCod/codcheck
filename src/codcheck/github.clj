(ns codcheck.github
  (:require
   [cheshire.core :as cheshire]
   [clj-http.client :as http]))

(def api-url "https://api.github.com/")

(defn installation-token-url
  [installation-id]
  (str api-url "app/installations/" installation-id "/access_tokens"))

(defn comment-pr-url
  [repo-owner repo-name pr-number]
  (str api-url "repos/" repo-owner "/" repo-name "/issues/" pr-number "/comments"))

(defn comment-on-pr
  ([installation-token repo-owner repo-name pr-number comment-message]
   (comment-on-pr installation-token repo-owner repo-name pr-number comment-message false))

  ([installation-token repo-owner repo-name pr-number comment-message async?]
   (let [comment-url (comment-pr-url repo-owner repo-name pr-number)
         headers {:authorization (str "token " installation-token)}
         opts {:headers headers
               :async? async?
               :accept "application/vnd.github.machine-man-preview+json"
               :body (cheshire/encode {:body comment-message})}]
     (http/post comment-url opts))))

;; (comment (comment-on-pr "some-token" "FieryCod" "codcheck-lint-demo" 1 "a-message"))
