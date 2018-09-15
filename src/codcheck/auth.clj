(ns codcheck.auth
  (:require
   [codcheck.envs :refer [envs]]
   [codcheck.github :as github]
   [buddy.core.keys :as buddy-keys]
   [clj-time.core :as clj-time]
   [buddy.sign.jwt :as jwt]
   [clj-http.client :as client])
  (:import
   [org.apache.commons.codec.digest HmacUtils HmacAlgorithms]))

(def gh-sha1-generator
  (HmacUtils. HmacAlgorithms/HMAC_SHA_1 (:github-webhook-secret envs)))

(def jwt-algorithm
  {:alg :rs256})

(def gh-private-key
  (buddy-keys/str->private-key (:github-private-key envs)))

(defn gh-sign-token
  []
  (let [claims {:exp (-> 10 clj-time/minutes clj-time/from-now)
                :iat (clj-time/now)
                :iss (:github-app-identifier envs)}]
    (jwt/sign claims gh-private-key jwt-algorithm)))

(defn gh-installation-token
  ([installation-id sign-token]
   (gh-installation-token installation-id sign-token false))

  ([installation-id sign-token async?]
   (let [installation-url (github/installation-token-url installation-id)
         headers {:authorization (str "Bearer " sign-token)}
         opts {:headers headers
               :async? async?
               :accept "application/vnd.github.machine-man-preview+json"
               :content-type :json}]
     (client/post installation-url opts))))
