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

### Option UCI_AnalyseMode

indicates we are in Analysis Mode

- default value: false


#### Declaration

```
option name UCI_AnalyseMode type check default false
```

### Option UCI_EngineAbout

uci engine about

#### Declaration

```
option name UCI_EngineAbout type string default JackyChess by Matthias Lang, see https://github.com/matthiaslang/jackychess
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

### Option quiescence

the maximum search depth in quiescence

- min: 0
- max: 63
- default: 63


#### Declaration

```
option name quiescence type spin default 63 min 0 max 63
```

### Option Threads

the maximum search threads to use for search

- min: 1
- max: 8
- default: 1


#### Declaration

```
option name Threads type spin default 1 min 1 max 8
```

