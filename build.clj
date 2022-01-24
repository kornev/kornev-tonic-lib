(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.string :as str]
            [clojure.tools.build.api :as b]
            [org.corfield.build :as bb]))

(def lib 'com.ensime/tonic)
(def version (format "0.%s" (b/git-count-revs nil)))

(defn test "Run the tests." [opts]
   (bb/run-tests opts))

(defn- compile-java [{:keys [basis class-dir] :as opts}] ; rewrite it
  (b/javac {:src-dirs ["native"]
            :class-dir class-dir
            :basis basis
            :javac-opts ["-source" "8" "-target" "8"]}))

(defn jar [{:keys [lib version] :as opts}]
  (let [{:keys [basis class-dir jar-file src-dirs src+dirs ns-compile filter-nses]} opts
        current-dir (System/getProperty "user.dir")
        current-rel #(str/replace % (str current-dir "/") "")]
    (println "\nWriting pom.xml...")
    (b/write-pom {:basis basis
                  :lib lib
                  :version version
                  :class-dir class-dir
                  :src-dirs src-dirs})
    (println "Copying" (str (str/join ", " (map current-rel src+dirs)) "..."))
    (b/copy-dir {:src-dirs src+dirs
                 :target-dir class-dir})
    (println "Compiling" (str (str/join ", " (or ns-compile src-dirs)) "..."))
    (b/compile-clj {:basis basis
                    :src-dirs src-dirs
                    :class-dir class-dir
                    :filter-nses filter-nses
                    :ns-compile ns-compile})
    (println "Compiling native...")
    (compile-java opts)
    (println "Building jar" (str jar-file "..."))
    (b/jar {:class-dir class-dir
            :jar-file jar-file})))

(defn ci "Run the CI pipeline of tests (and build the JAR)." [opts]
  (-> opts
      (assoc :lib lib
             :version version
             :basis (bb/default-basis)
             :class-dir (bb/default-class-dir)
             :jar-file (bb/default-jar-file lib version)
             :src-dirs ["src"]
             :src+dirs ["src" "resources"]
             :filter-nses '[tonic.java]
             :ns-compile '[tonic.java])
      (bb/run-tests)
      (bb/clean)
      (jar)))

(defn deploy "Deploy the JAR to Clojars." [opts]
  (-> opts
      (assoc :lib lib :version version)
      (bb/deploy)))
