(defproject particle-chamber "0.1.0-SNAPSHOT"
  :description "Clojure Workshop: Particle Chamber"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.9.0"]
                 [proto-repl "0.3.1"]]
  :main ^:skip-aot particle-chamber.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
