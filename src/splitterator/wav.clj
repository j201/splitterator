(ns splitterator.wav
  (:import (javax.sound.sampled AudioSystem
                                AudioInputStream
                                AudioFormat
                                AudioFormat$Encoding
                                AudioFileFormat
                                AudioFileFormat$Type)
           (java.nio ByteBuffer
                     ByteOrder
                     ShortBuffer)
           (java.io ByteArrayOutputStream
                    ByteArrayInputStream
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

(defn- byte-array-to-audio-stream [arr]
  (AudioInputStream. (ByteArrayInputStream. arr)
                     (AudioFormat. (float 44100)
                                   (int 16)
                                   (int 1)
                                   true
                                   false)
                     (alength arr)))

(defn- byte-to-short-array [arr big-endian]
  (let [short-buffer (.asShortBuffer (.order (ByteBuffer/wrap arr)
                                             (if big-endian ByteOrder/BIG_ENDIAN ByteOrder/LITTLE_ENDIAN)))
        short-arr (short-array (.capacity short-buffer))]
    (do (.get short-buffer short-arr)
        short-arr)))

(defn- short-to-byte-array [arr big-endian]
  (let [byte-arr (byte-array (* 2 (alength arr)))
        byte-buffer (.asShortBuffer (.order (ByteBuffer/wrap byte-arr)
                                            (if big-endian ByteOrder/BIG_ENDIAN ByteOrder/LITTLE_ENDIAN)))]
    (do (.put byte-buffer arr)
        byte-arr)))

(defn- short-sample-to-float [sample]
  (/ (float sample) 0x8000))

(defn- float-sample-to-short [sample]
  (short (* sample 0x8000)))

; reads a mono 16-bit wav file into a seq of floats
(defn read-mono-wav [path]
  (let [file (File. path)
        audio-format (.getFormat (AudioSystem/getAudioFileFormat file))]
    (map short-sample-to-float
         (byte-to-short-array (wav-to-byte-array path)
                              (.isBigEndian audio-format)))))

; writes a mono 16-bit wav file from a seq of floats
(defn write-mono-wav [path sq]
  (AudioSystem/write (-> (map float-sample-to-short sq)
                         short-array
                         (short-to-byte-array false)
                         byte-array-to-audio-stream)
                     AudioFileFormat$Type/WAVE
                     (File. path)))
