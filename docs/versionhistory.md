# Versions
                  
#### Version 0.12.0-003

- remove pv sorting and all code which tries to extend the pv line to the calculated depth as it is now anyway only for informational purpose
  - sort order does not use pv for sorting anymore, since the sorting by tt move is a generalization of this concept.
  - makes the code a bit cleaner and easier in that area
  - pv line is only for informational purpose now, and only for UCI output
  - pv line is now always checked for validity and shortened if necessary, so we have no cutechess warnings about this anymore
  - gives some ELO points, I think because of reduced code in that area...

Elo: +16-32



### Version 0.12.2

- tuning of evaluation pst values. Brings ~70 elos.


### Version 0.12.0

Official Release, same as 0.11.11.

Main difference to 0.10.0:

- fixed draw by repetition issue
- IID only used on higher depths; seem to bring some Elos...
- PST prodProduct calc optimization.
- special end game functions for common basic end games
- multi threading using lazy SMP

Elo Difference to 0.10.0: +187

### Version 0.11.11

RC02 for next official release.

- fixed draw by repetition issue: internal board copy method has not copied move history, therefore winning situations lead often to draws.
- 

Elo difference: ~30 compared to 0.11.10

### Version 0.11.10

- IID only used on higher depths; seem to bring some Elos...
- PST prodProduct calc optimization. brings ~60 ELO compared to 0.11.9

Elo difference: ~60 compared to 0.11.9 

### Version 0.11.9      

RC01 for next official release.

- experimental move order change to sort bad captures at least
- fixed bug parsing UCI fen position: under certain circumstands a move string has been misinterpreting as en passant move.
- fixed bug with draw by material: only return draw score for higher plies than 1 to properly return a best move

Elo difference: 85.8 compared to 0.10.0

Elo difference: 107.5 4 Threads vs 1 Thread



### Version 0.11.8

- more special end game functions

### Version 0.11.7

- special end game eval function for KX vs K

### Version 0.11.6.5

- refactored code around the Move Interface
- optimized eval cache
- experiments with futility pruning by See (not used, doesnt bring anything)

### Version 0.11.6.4

- first simple draw by material detection during negamax search

- experimental staged move gen. not used since it does not bring anything for now. 

### Version 0.11.6.1

- cleaned up sorting a bit; dont use ply 0 move scores for sorting; higher sorting for queen promotion

### Version 0.11.6

- internal iterative deepening (doesnt bring currently anything...)

### Version 0.11.5

- undo experimental worker adjusting depth
- refactored code in mobility evaluation

### Version 0.11.4

- experimental worker adjusting depth in multithread mode (again..).......

### Version 0.11.3

- experimental worker adjusting depth in multithread mode....
- some refactorings


### Version 0.11.2

- cache size as UCI option
- illegal pvs are now corrected (again)
- multi threading test version: lazy SMP with 4 threads seem to bring benefits with 4x bigger cache.

### Version 0.11.1

- using smaller aspiration windows and more increments. Seem to bring some small benefits (~7 Elos).


### Version 0.10.0

Official release. It plays ~2000 elos. 

- cleaned up Uci options.

### Version 0.9.33

- move list iteration clean up
- tt cache probe in quiescence

### Version 0.9.32

- counter move heuristic

### Version 0.9.31    0.10.0.RC04

- fixed tt cache upper/lower bounds issue
  - fixed lower/upper bound flag mistake
  - fixed saving of exact values
  - dont save tt values in quiescence


### Version 0.9.30

- move generation in quiescence to only generate captures

### Version 0.9.29

- reworked tt cache
- cleaned up quiescence

### Version 0.9.28

- Experimental Lazy SMP disabled by default. Doesnt bring anything so far. Seems we have anyway weird problems with tt caching (e.g ttcach3)
  and pv extraction. This seems also the reason why SMP doesnt speed up search.
- material based end game evaluation: one first rule for KX vs K.                                            

### Version 0.9.27

- refactoring: eval cache is now thread safe

### Version 0.9.26  0.9.26.02 0.10.0.RC03

- static search thread context holding move list instances for preparation for multi threading. therefore slightly faster
  than 0.9.25.
- disabled late move pruning from 0.9.25 as it makes it worser

### Version 0.9.25

- late move pruning. but does not seem to bring any benefit in elos or is nearly not measurable. Seem to be worse.
  so we should disable it for now...

### Version 0.9.24  0.10.0.RC02
               
- skip low promotions in quiescence. seem to bring some elos compared to previous version....

### Version 0.9.23

- refine static Eval during search for some pruning things (seems to bring some elos)

### Version 0.9.22  0.10.0.RC01

- move ordering of captures uses see instead of blind

### Version 0.9.21

- some bitboard code optimizations saving some time

### Version 0.9.20

- some bb move do/undo opts saving some time and giving some strength
- 

### Version 0.9.19

- evaluation: eval some special blockage patterns
- changed handling of repetitions
- mate distance pruning


### Version 0.9.18

The last versions have been mainly refactorings using now Bitboard and the respective move generation using magic
bitboards. Not everything in the code is optimized for bitboards, yet, but its an ongoing task.

This Version seems now roughly ~200 Elo stronger than the last official release 0.9.14, so the estimate is ~1650 Elo.

                                                            
- Late move reductions 
- Null move pruning and static null move reduction
- Razoring
- Delta cut off in quiescence
- pseudo legal move generation for faster move generation when not all moves are traversed
- parameterized handcrafted evaluation function (based on CPW) which is slightly better or at least equal than the old pst evaluation
- move sorting: captures are sorted by BLIND algorithm to find good captures. this seems to improve the pruning

     
#### bugfix

- fixed issue delivering illegal pv moves after a check mate;



### Version 0.9.16

- bitboard implementation
  - move generation uses magic bitboards
  - check checker uses bitboards

move generation and search should therefore a bit faster, overall search should be ~25% faster.



### Version 0.9.15.1

- fixed history heuristic
  - history heuristic is now scaled by "bad" history heuristics
  - seems to gain now something by sprt testing ~40 Elos

### Version 0.9.15

- cleaned up and fixed mvvLva code. Seems to give slightly better pruning.

### Version 0.9.14  first official stable release

- fixed issue when running engine within a batch file: uci quit has not exit process (because of other threads still running)
- log files are by default not written, but only if java property jacky.logging.activate is set to true.
- minor internal code refactorings


### Version 0.9.13

- fixed stupid bug in time calculation which produced much too high values which exceeded the complete rest time.

### Version 0.9.12

- fixed missing promotion letter on best move
- reworked time control calucation for blitz chess a bit

### Version 0.9.11

- fixed UCI time control options: wtime, btime, winc, binc have been ignored if movestogo has not been set

### Version 0.9.10

- cleaned up unused code (old Move classes, etc)
- optimized move lists and move classes
- experimental code available as options:
  - null move pruning. not activated by default, as it doesnt seem any benefit so far.
  - late move reductions. not activated by default as it doesnt bring any benefit so far.
  - hash move ordering: doesnt bring anything (since it is always the same as the pv move)


    Stockfisch Level 5 (Elo ~ 1630): 10s move: wins    
    Stockfisch Level 5 (Elo ~ 1630): 2s move: wins 75%


### Version 0.9.9

- reworked patt checks
  - is now independant of evaluation function
  - fix: did not work in quiescence for the new evaluation function



    Stockfisch Level 5 (Elo ~ 1630): 10s move: wins 70%   
    Stockfisch Level 5 (Elo ~ 1630): 2s move: wins 70%   


### Version 0.9.8.2

- fix: missing reset in board stats generator lead to evaluate to wrong patt situations



### Version 0.9.8.1

- experimental rewrite of move class and movelist (as uci option)
- some opts for en passant moves saving some mem per move in general.
- experimental aspiration window (as uci option)
- reuse TT Cache for the complete game with aging


- there seems to be a problem with search not to deliver best move under som circumstands.
  - seems to happen in endgames, maybe because when detecting draw by repetition or mate it does not set best move...

with the new move list impl and aspiration, it seem to be a bit better than before on turnaments with 15s time per move.
On lower time turnaments 2s etc. there does not seem any benefit.



    Jacky 0.9.7 vs 0.9.8.1         : Turnament 5min: jacky 0.9.8.1 
    Stockfisch Level 4 (Elo ~ 1560): 10s move: wins 85%


### Version 0.9.8

- slightly better move ordering

### Version 0.9.7

- fix generation of non quiet moves which where introduced probably in 0.9.4.


    Stockfisch Level 4 (Elo ~ 1560): 10s move: jacky wins 60% of Games

### Version 0.9.6

- small code changes tt caching


### Version 0.9.5

- killer move heuristic
- clean up & rework move ordering (especially mvvlva)


### Version 0.9.4

- fixed problem in quiescence: incorrectly decided to have check mate
- reworked legal move filtering


    Stockfisch Level 4 (Elo ~ 1560): 10s move: jacky wins 60% of Games

### Version 0.9.3

Experimental history heuristic for move ordering

    Stockfisch Level 2 (Elo ~ 1420): 10s move: jacky wins
    Stockfisch Level 3 (Elo ~ 1490): 10s move: jacky wins
    Stockfisch Level 4 (Elo ~ 1560): 10s move: jacky loss 70% of Games
### Version 0.9.2

Slightly faster move generation using piece list instead of iterating complete board.

### Version 0.9.1

TT Cache fixed and seems now working properly.

### Version 0.9.0

new pst evaluation seems to play better: beats old Version 0.8.2 in turnaments.

    Stockfisch Level 2 (Elo ~ 1420): 10s move: ~50:50
    Stockfisch Level 2 (Elo ~ 1420): 15s move: ~60:40 ; Jacky seems to be bit better                        


### Version 0.8.2

First real "stable" version with "stable" algorithms playing reasonable strong.

#### Turnament Measurement: 10s per move:

    Stockfish Level 1 (Elo ~ 1350): wins
    Stockfisch Level 2 (Elo ~ 1420): sometimes wins...

