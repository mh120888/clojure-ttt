(defproject matts-clojure-ttt "0.1.0-SNAPSHOT"
  :description "A tic tac toe game in Clojure"
  :url "https://github.com/mh120888/matts-clojure-ttt"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-RC2"]]
  :profiles {:dev {:dependencies [[speclj "3.3.1"]]}}
  :plugins [[speclj "3.3.1"]]
  :repl-options {:init (load-file "src/matts_clojure_ttt/board.clj" ) }
  :test-paths ["spec"]
  :main matts-clojure-ttt.game)
