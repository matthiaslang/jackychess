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
### searchalg
the search algorithm to use. Only for development testing.

default value: STABLE

#### Declaration
    option name searchalg type combo default STABLE var MTDF var STABLE
### activatePvsSearch
should principal variation search be used

default value: true

#### Declaration
    option name activatePvsSearch type check default true
### evaluateFunction
the evaluation function to use. Only for development testing

default value: MINIMAL_PST

#### Declaration
    option name evaluateFunction type combo default MINIMAL_PST var DEFAULT var MINIMAL_PST var PST2
## Pruning
Parameter influencing the pruning during alpha beta search
### aspiration
should aspiration windows be used during iterative deepening

default value: true

#### Declaration
    option name aspiration type check default true
### useNullMoves
should null move pruning be used during search

default value: false

#### Declaration
    option name useNullMoves type check default false
### useLateMoveReductions
should late move reductions be used during search

default value: false

#### Declaration
    option name useLateMoveReductions type check default false
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
max: 50
default: 20

#### Declaration
    option name maxdepth type spin default 20 min 3 max 50
### quiescence
the maximum search depth in quiescence

min: 0
max: 10
default: 10

#### Declaration
    option name quiescence type spin default 10 min 0 max 10
## Common
Common parameter
### MoveListImpl
internally. Only for development testing

default value: OPTIMIZED

#### Declaration
    option name MoveListImpl type combo default OPTIMIZED var OPTIMIZED
