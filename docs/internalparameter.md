# internal options of the engine.

These options is mainly only for testing and development. These options can be set via system properties or a configuration property file

## Move Order

Parameter influencing the move order in alpha beta search

### Option useHistoryHeuristic

should history heuristic be used for move ordering

- default value: true


#### Declaration

> You can set a value via Property opt.useHistoryHeuristic

### Option useKillerMoves

should killer moves heuristic be used for move ordering

- default value: true


#### Declaration

> You can set a value via Property opt.useKillerMoves

### Option useCounterMoves

should counter moves heuristic be used for move ordering

- default value: true


#### Declaration

> You can set a value via Property opt.useCounterMoves

### Option useMvvLvaSorting

should mvv lva sorting be used for move ordering

- default value: true


#### Declaration

> You can set a value via Property opt.useMvvLvaSorting

## Internal

Internal Test Parameter for Development

### Option useTTCache

Flag, if the tt cache to store scores should be activated

- default value: true


#### Declaration

> You can set a value via Property opt.useTTCache

### Option mateDistancePruning

should mate distance pruning be activated?

- default value: true


#### Declaration

> You can set a value via Property opt.mateDistancePruning

### Option iid

should internal iterative deepening be activated?

- default value: true


#### Declaration

> You can set a value via Property opt.iid

## Search

Parameter that influence search.

### Option activatePvsSearch

should principal variation search be used

- default value: true


#### Declaration

> You can set a value via Property opt.activatePvsSearch

### Option evaluateFunction

the evaluation function to use. Only for development testing

- default value: PARAMETERIZED


#### Declaration

> You can set a value via Property opt.evaluateFunction

### Option evalParamSet

the evaluation parameter set used when evaluateFunction is set to Parameterized. Only for development testing

- default value: CURRENT


#### Declaration

> You can set a value via Property opt.evalParamSet

## Pruning

Parameter influencing the pruning during alpha beta search

### Option aspiration

should aspiration windows be used during iterative deepening

- default value: true


#### Declaration

> You can set a value via Property opt.aspiration

### Option useNullMoves

should null move pruning be used during search

- default value: true


#### Declaration

> You can set a value via Property opt.useNullMoves

### Option staticNullMove

should static null move pruning be used during search

- default value: true


#### Declaration

> You can set a value via Property opt.staticNullMove

### Option razoring

should razoring be used during search

- default value: true


#### Declaration

> You can set a value via Property opt.razoring

### Option futilityPruning

should futility pruning be used during search

- default value: true


#### Declaration

> You can set a value via Property opt.futilityPruning

### Option deltaCutoff

should delta cutoff be used during quiescence search

- default value: true


#### Declaration

> You can set a value via Property opt.deltaCutoff

### Option useLateMoveReductions

should late move reductions be used during search

- default value: true


#### Declaration

> You can set a value via Property opt.useLateMoveReductions

## Extensions

Parameter influencing the extension of the search tree

### Option useCheckExtension

on check, extend the search depth

- default value: false


#### Declaration

> You can set a value via Property opt.useCheckExtension

