# io-streamer


In order to run it do:
```
sbt compile

sbt "run 9090 rn"
```

In a different terminal you can connect to it using telnet:

```
telnet localhost 9090
start:500
```
Notice that `start` is for starting the incoming stream and `:500` indicates how fast streamer will push data in. In this case `500 ms`.

![Running the Streamer](https://github.com/anicolaspp/io-streamer/blob/master/Screen Shot 2018-05-18 at 11.54.34 AM.png)

![Connecting using telnet](https://github.com/anicolaspp/io-streamer/blob/master/Screen Shot 2018-05-18 at 11.54.47 AM.png)
