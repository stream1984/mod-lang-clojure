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

(ns example.upload.server
  (:require [vertx.http :as http]
            [vertx.stream :as stream]
            [vertx.utils :as u]
            [vertx.filesystem :as fs]))

(defn handle-file [async-file req]
  (let [pump (stream/pump req async-file)]
<<<<<<< HEAD
    (stream/on-end req #(fs/close async-file
                                    (fn [err]
                                      (if (nil? err)
                                        (do
                                          (http/end (http/server-response req))
                                          (println "Uploaded " (.bytesPumped pump) " bytes"))
                                        ((.printStackTrace (.cause err)))))))
    (.start pump)))

(defn req-handler [req]
  (let [filename (str "upload/file-" (u/uuid) ".upload")]
    (stream/pause req)
    (fs/open filename (fn [err res]
                          (if (nil? err)
                            (handle-file res req)
                            (.printStackTrace (.cause err)))))
    (stream/resume req)))
=======
    (stream/on-end
     req
     (fn []
       (fs/close async-file
                 (fn [err]
                   (if err
                     (throw err)
                     (println "Uploaded" (.bytesPumped pump) "bytes"))))
       (http/end (http/server-response req)))))
  (.resume req))

(defn req-handler [req]
  (.pause req)
  (fs/open
   (str "upload/file-" (u/uuid) ".upload")
   (fn [err f]
     (if err
       (throw err)
       (handle-file f req)))))
>>>>>>> tobias-master

(-> (http/server)
    (http/on-request req-handler)
    (http/listen 8080 "localhost"))
