# UCI Options
List of all UCI options of the engine.
## Group Limits
Parameter which limit the search or search time in some way.
### Option thinktime
the think time in milliseconds; this time is used if no other time restrictions are give by the uci go command.

min: 1000
max: 600000
default: 15000

#### Declaration
    option name thinktime type spin default 15000 min 1000 max 600000
### Option maxdepth
the maximum search depth to use if there is enough search time

min: 3
max: 64
default: 40

#### Declaration
    option name maxdepth type spin default 40 min 3 max 64
### Option quiescence
the maximum search depth in quiescence

min: 0
max: 50
default: 10

#### Declaration
    option name quiescence type spin default 10 min 0 max 50

# internal options of the engine.

 These options is mainly only for testing and development. These options can be set via system properties or a configuration property file
## Group Move Order
Parameter influencing the move order in alpha beta search
### Option useHistoryHeuristic
should history heuristic be used for move ordering

default value: true

#### Declaration
    You can set a value via Property opt.useHistoryHeuristic
### Option useKillerMoves
should killer moves heuristic be used for move ordering

default value: true

#### Declaration
    You can set a value via Property opt.useKillerMoves
### Option useCounterMoves
should counter moves heuristic be used for move ordering

default value: true

#### Declaration
    You can set a value via Property opt.useCounterMoves
### Option useMvvLvaSorting
should mvv lva sorting be used for move ordering

default value: true

#### Declaration
    You can set a value via Property opt.useMvvLvaSorting
### Option usePvSorting
should principal variation information used for move ordering

default value: true

#### Declaration
    You can set a value via Property opt.usePvSorting
## Group Internal
Internal Test Parameter for Development
### Option maxThreads
the maximum search threads when multi threading search is activated

min: 1
max: 8
default: 4

#### Declaration
    You can set a value via Property opt.maxThreads
### Option useTTCache
Flag, if the tt cache to store scores should be activated

default value: true

#### Declaration
    You can set a value via Property opt.useTTCache
### Option MoveListImpl
internally. Only for development testing

default value: OPTIMIZED

#### Declaration
    You can set a value via Property opt.MoveListImpl
### Option expandPv
should the found PV expand by cache entries? Otherwise they could be shorter than the depth caused by pruning.

default value: true

#### Declaration
    You can set a value via Property opt.expandPv
### Option mateDistancePruning
should mate distance pruning be activated?

default value: true

#### Declaration
    You can set a value via Property opt.mateDistancePruning
## Group Search
Parameter that influence search.
### Option searchalg
the search algorithm to use.

default value: STABLE

#### Declaration
    You can set a value via Property opt.searchalg
### Option activatePvsSearch
should principal variation search be used

default value: true

#### Declaration
    You can set a value via Property opt.activatePvsSearch
### Option evaluateFunction
the evaluation function to use. Only for development testing

default value: PARAMETERIZED

#### Declaration
    You can set a value via Property opt.evaluateFunction
### Option evalParamSet
the evaluation parameter set used when evaluateFunction is set to Parameterized. Only for development testing

default value: CURRENT

#### Declaration
    You can set a value via Property opt.evalParamSet
## Group Pruning
Parameter influencing the pruning during alpha beta search
### Option aspiration
should aspiration windows be used during iterative deepening

default value: true

#### Declaration
    You can set a value via Property opt.aspiration
### Option useNullMoves
should null move pruning be used during search

default value: true

#### Declaration
    You can set a value via Property opt.useNullMoves
### Option staticNullMove
should static null move pruning be used during search

default value: true

#### Declaration
    You can set a value via Property opt.staticNullMove
### Option razoring
should razoring be used during search

default value: true

#### Declaration
    You can set a value via Property opt.razoring
### Option futilityPruning
should futility pruning be used during search

default value: true

#### Declaration
    You can set a value via Property opt.futilityPruning
### Option deltaCutoff
should delta cutoff be used during quiescence search

default value: true

#### Declaration
    You can set a value via Property opt.deltaCutoff
### Option useLateMoveReductions
should late move reductions be used during search

default value: true

#### Declaration
    You can set a value via Property opt.useLateMoveReductions
## Group Experimental
Experimental parameter used during development
### Option TTCacheImpl
internally. Only for development testing

default value: STANDARD

#### Declaration
    You can set a value via Property opt.TTCacheImpl
## Group Extensions
Parameter influencing the extension of the search tree
### Option useCheckExtension
on check, extend the search depth

default value: false

#### Declaration
    You can set a value via Property opt.useCheckExtension
