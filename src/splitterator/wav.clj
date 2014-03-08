(ns splitterator.wav
  (:import (javax.sound.sampled AudioSystem
                                AudioInputStream
                                AudioFileFormat)
           (java.nio ByteBuffer
                     ByteOrder
                     ShortBuffer)
           (java.io ByteArrayOutputStream
                    File)))

(defn- wav-to-byte-array [path]
  (let [audio-stream (AudioSystem/getAudioInputStream (File. path))
        byte-stream (ByteArrayOutputStream.)
        buffer (byte-array 8192)]
    (do (loop [size 0]
          (when (not= size -1)
            (do (.write byte-stream buffer 0 size)
                (recur (.read audio-stream buffer 0 (alength buffer))))))
        (.close audio-stream)
        (.toByteArray byte-stream))))

(defn- byte-to-short-array [arr big-endian]
  (let [short-buffer (.asShortBuffer (.order (ByteBuffer/wrap arr)
                                             (if big-endian ByteOrder/BIG_ENDIAN ByteOrder/LITTLE_ENDIAN)))
        short-arr (short-array (.capacity short-buffer))]
    (do (.get short-buffer short-arr)
        short-arr)))

(defn- short-sample-to-float [sample]
  (/ (float sample) 0x8000))

; reads a mono 16-bit wav file into a seq of floats
(defn read-mono-wav [path]
  (let [file (File. path)
        audio-format (.getFormat (AudioSystem/getAudioFileFormat file))]
    (map short-sample-to-float
         (byte-to-short-array (wav-to-byte-array path)
                              (.isBigEndian audio-format)))))
