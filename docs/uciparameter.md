# UCI Options

List of all UCI options of the engine.

## Variants

Game Variant parameter

### Option UCI_Chess960

indicates support for Chess960

- default value: false


#### Declaration

```
option name UCI_Chess960 type check default false
```

## Caching

Parameter for caching of information during search.

### Option Hash

TT Hash Size in MB

- min: 1
- max: 2048
- default: 128


#### Declaration

```
option name Hash type spin default 128 min 1 max 2048
```

## Limits

Parameter which limit the search or search time in some way.

### Option thinktime

the think time in milliseconds; this time is used if no other time restrictions are give by the uci go command.

- min: 1000
- max: 600000
- default: 15000


#### Declaration

```
option name thinktime type spin default 15000 min 1000 max 600000
```

### Option maxdepth

the maximum search depth to use if there is enough search time

- min: 3
- max: 63
- default: 63


#### Declaration

```
option name maxdepth type spin default 63 min 3 max 63
```

### Option quiescence

the maximum search depth in quiescence

- min: 0
- max: 63
- default: 63


#### Declaration

```
option name quiescence type spin default 63 min 0 max 63
```

### Option maxThreads

the maximum search threads when multi threading search is activated

- min: 1
- max: 8
- default: 1


#### Declaration

```
option name maxThreads type spin default 1 min 1 max 8
```

### Option searchalg

the search algorithm to use.

- default value: MULTITHREAD


#### Declaration

```
option name searchalg type combo default MULTITHREAD var SINGLETHREAD var MULTITHREAD
```

