;; Copyright 2013 the original author or authors.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;      http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns example.pubsub.server
  (:require [vertx.net :as net]
            [vertx.shareddata :as shared]
            [clojure.string :as string]
            [vertx.stream :as stream]
            [vertx.eventbus :as bus]
            [vertx.buffer :as buf]))


(defn connect-handler [sock]
<<<<<<< HEAD
  (let [parser
        (buf/parse-delimited
         "/n" (fn [line]
                (let [line-vec (string/split line #",")]
                  (condp = (first line-vec)
                      "subscribe" #(let [topic-name (second line-vec)]
                                     (println (str "subscribing to " topic-name))
                                     (shared/add! (shared/get-set topic-name) (.writeHandlerID sock)))

                      "unsubscribe" #(let [topic-name (second line-vec)]
                                       (println (str "unsubscribing from " topic-name))
                                       (shared/remove! (shared/get-set topic-name) (.writeHandlerID sock)))

                      "publish" #(let [topic-name (second line-vec)
                                       content (last line-vec)]
                                   (println (str "publishing to " topic-name "with " content))
                                   (doseq [target (shared/get-set topic-name)]
                                     (bus/send target content)))
                      (str "unexpected value " (first line-vec))))))]
    (stream/on-data sock parser)))
=======
  (stream/on-data
   sock
   (fn [buf!]
     (buf/parse-delimited
      buf! "\n"
      (fn [line]
        (let [[cmd topic msg] (-> line str string/trim (string/split #","))
              topic-set! (and topic (shared/get-set topic))
              sock-id (.writeHandlerID sock)]
          (condp = cmd
            "subscribe" (do
                          (println "subscribing to" topic)
                          (shared/add! topic-set! sock-id))

            "unsubscribe" (do
                            (println "unsubscribing from" topic)
                            (shared/remove! topic-set! sock-id))

            "publish" (do
                        (println "publishing to" topic "with" msg)
                        (doseq [target topic-set!]
                          (bus/send target (buf/buffer (str msg "\n")))))
            
            (stream/write sock (format "unknown command: %s\n" cmd)))))))))
>>>>>>> tobias-master

(-> (net/server)
    (net/on-connect connect-handler)
    (net/listen 1234))
<<<<<<< HEAD
=======

(println "Starting TCP pub-sub server on localhost:1234")
>>>>>>> tobias-master
