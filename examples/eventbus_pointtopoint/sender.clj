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

(ns example.sender
  (:require [vertx.core :as vertx]
            [vertx.eventbus :as eb]))

(def address "example.address")
(def msg-count (atom 0))

(vertx/periodic
 1000
 (let [msg (str "some-message-" (swap! msg-count inc))]
   (eb/send address msg
            (fn [reply]
              (println "received:" (eb/message-body reply))))
   (println "sent message" msg)))
