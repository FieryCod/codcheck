(defproject codcheck "0.0.1"

  :description "Core/Common code of the codcheck platform"
  :url "https://github.com/FieryCod/codcheck"

  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}

  :global-vars {*warn-on-reflection* true}

  :dependencies [[org.clojure/clojure "1.10.0-beta8"]
                 [com.taoensso/timbre "4.10.0"]
                 [org.clojure/core.async "0.4.474"]
                 [clj-time "0.14.4"]
                 [clj-http "3.9.1"]
                 [buddy/buddy-core "1.5.0"]
                 [buddy/buddy-auth "2.1.0"]
                 [commons-codec/commons-codec "1.11"]
                 [ring/ring-defaults "0.3.2"]
                 [org.slf4j/slf4j-simple "1.8.0-beta2"]
                 [com.novemberain/langohr "5.0.0"]
                 [jonase/eastwood "0.3.3"]
                 [irresponsible/tentacles "0.6.2"]]

  :source-paths ["src"]

  :plugins [[lein-ring "0.12.4"]
            [lein-bikeshed "0.5.1"]
            [lein-kibit "0.1.6"]
            [lein-shell "0.5.0"]]

  :aliases {"ci-check" ["do" ["kibit"] ["eastwood"] ["bikeshed"]]})
