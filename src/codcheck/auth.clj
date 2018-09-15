(ns codcheck.auth
  (:require
   [codcheck.envs :refer [envs]]
   [buddy.core.keys :as buddy-keys]
   [clj-time.core :as clj-time]
   [buddy.sign.jwt :as jwt])
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

(gh-sign-token)

(gh-installation-token (gh-sign-token))

(defn gh-installation-token
  [sign-token]
  ())
