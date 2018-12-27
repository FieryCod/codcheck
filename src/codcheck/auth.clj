(ns codcheck.auth
  (:require
   [cheshire.core :as cheshire]
   [buddy.core.keys :as buddy-keys]
   [buddy.sign.jwt :as jwt]
   [clj-time.core :as clj-time]
   [clojure.string :as string]
   [clj-http.client :as client]
   [codcheck.envs :refer [envs]]
   [codcheck.github :as github])
  (:import
   [org.apache.commons.codec.digest HmacUtils HmacAlgorithms]))

(defn private-key-from-pem
  []
  (try
    (buddy-keys/private-key "codcheck.pem")
    (catch Exception err
      (println "There is no codcheck.pem key")
      nil)))

(def ^HmacUtils gh-sha1-generator
  (HmacUtils.
   ^HmacAlgorithms HmacAlgorithms/HMAC_SHA_1
   ^String (:GITHUB_WEBHOOK_SECRET envs)))

(def jwt-algorithm
  {:alg :rs256})

(def gh-private-key
  (or (private-key-from-pem) (buddy-keys/str->private-key (:GITHUB_PRIVATE_KEY envs))))

(defn gh-sign-token
  []
  (let [claims {:exp (-> 10 clj-time/minutes clj-time/from-now)
                :iat (clj-time/now)
                :iss (:GITHUB_APP_IDENTIFIER envs)}]
    (jwt/sign claims gh-private-key jwt-algorithm)))

(defn gh-installation-token-request
  ([installation-id sign-token]
   (gh-installation-token-request installation-id sign-token false))

  ([installation-id sign-token async?]
   (let [installation-url (github/installation-token-url installation-id)
         headers {:authorization (str "Bearer " sign-token)}
         opts {:headers headers
               :async? async?
               :accept "application/vnd.github.machine-man-preview+json"
               :content-type :json}]
     (client/post installation-url opts))))

(defn installation-request->token
  [installation-request]
  (get (cheshire/parse-string (:body installation-request) true) :token ""))
