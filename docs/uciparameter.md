# UCI Options
List of all UCI Options of the engine.
## Move Order
Parameter influencing the move order in alpha beta search
### useHistoryHeuristic
should history heuristic be used for move ordering

default value: true

#### Declaration
    option name useHistoryHeuristic type check default true
### useKillerMoves
should killer moves heuristic be used for move ordering

default value: true

#### Declaration
    option name useKillerMoves type check default true
### useMvvLvaSorting
should mvv lva sorting be used for move ordering

default value: true

#### Declaration
    option name useMvvLvaSorting type check default true
### usePvSorting
should principal variation information used for move ordering

default value: true

#### Declaration
    option name usePvSorting type check default true
## Caching
Parameter for caching of information during search.
### useTTCache
Flag, if the tt cache to store scores should be activated

default value: true

#### Declaration
    option name useTTCache type check default true
## Search
Parameter that influence search.
### activatePvsSearch
should principal variation search be used

default value: true

#### Declaration
    option name activatePvsSearch type check default true
### evaluateFunction
the evaluation function to use. Only for development testing

default value: PARAMETERIZED

#### Declaration
    option name evaluateFunction type combo default PARAMETERIZED var DEFAULT var MINIMAL_PST var PARAMETERIZED
### evalParamSet
the evaluation parameter set used when evaluateFunction is set to Parameterized. Only for development testing

default value: CURRENT

#### Declaration
    option name evalParamSet type combo default CURRENT var DEFAULT var CURRENT var EXPERIMENTAL
## Pruning
Parameter influencing the pruning during alpha beta search
### aspiration
should aspiration windows be used during iterative deepening

default value: true

#### Declaration
    option name aspiration type check default true
### useNullMoves
should null move pruning be used during search

default value: true

#### Declaration
    option name useNullMoves type check default true
### staticNullMove
should static null move pruning be used during search

default value: true

#### Declaration
    option name staticNullMove type check default true
### razoring
should razoring be used during search

default value: true

#### Declaration
    option name razoring type check default true
### futilityPruning
should futility pruning be used during search

default value: true

#### Declaration
    option name futilityPruning type check default true
### deltaCutoff
should delta cutoff be used during quiescence search

default value: true

#### Declaration
    option name deltaCutoff type check default true
### useLateMoveReductions
should late move reductions be used during search

default value: true

#### Declaration
    option name useLateMoveReductions type check default true
## Experimental
Experimental parameter used during development
### TTCacheImpl
internally. Only for development testing

default value: STANDARD

#### Declaration
    option name TTCacheImpl type combo default STANDARD var STANDARD var V2 var V3 var V4 var V5 var BUCKETS
## Extensions
Parameter influencing the extension of the search tree
### useCheckExtension
on check, extend the search depth

default value: false

#### Declaration
    option name useCheckExtension type check default false
## Limits
Parameter which limit the search or search time in some way.
### thinktime
the think time in milliseconds; this time is used if no other time restrictions are give by the uci go command.

min: 1000
max: 600000
default: 15000

#### Declaration
    option name thinktime type spin default 15000 min 1000 max 600000
### maxdepth
the maximum search depth to use if there is enough search time

min: 3
max: 80
default: 40

#### Declaration
    option name maxdepth type spin default 40 min 3 max 80
### quiescence
the maximum search depth in quiescence

min: 0
max: 50
default: 10

#### Declaration
    option name quiescence type spin default 10 min 0 max 50

List of all Options of the engine.
## Internal
Internal Test Parameter for Development
### searchalg
the search algorithm to use. Only for development testing.

default value: STABLE

#### Declaration
    You can set a value via Property opt.searchalg
### MoveListImpl
internally. Only for development testing

default value: OPTIMIZED

#### Declaration
    You can set a value via Property opt.MoveListImpl
### expandPv
should the found PV expand by cache entries? Otherwise they could be shorter than the depth caused by pruning.

default value: true

#### Declaration
    You can set a value via Property opt.expandPv
