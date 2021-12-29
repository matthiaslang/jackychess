# Versions


### Version 0.8.2

First real "stable" version with "stable" algorithms playing reasonable strong.

#### Turnament Measurement: 10s per move:

    Stockfish Level 1 (Elo ~ 1350): wins
    Stockfisch Level 2 (Elo ~ 1420): sometimes wins...




### Version 0.9.0

new pst evaluation seems to play better: beats old Version 0.8.2 in turnaments.

    Stockfisch Level 2 (Elo ~ 1420): 10s move: ~50:50
    Stockfisch Level 2 (Elo ~ 1420): 15s move: ~60:40 ; Jacky seems to be bit better                        

### Version 0.9.1

TT Cache fixed and seems now working properly.

### Version 0.9.2

Slightly faster move generation using piece list instead of iterating complete board.

### Version 0.9.3

Experimental history heuristic for move ordering

    Stockfisch Level 2 (Elo ~ 1420): 10s move: jacky wins
    Stockfisch Level 3 (Elo ~ 1490): 10s move: jacky wins
    Stockfisch Level 4 (Elo ~ 1560): 10s move: jacky loss 70% of Games

### Version 0.9.4

- fixed problem in quiescence: incorrectly decided to have check mate
- reworked legal move filtering


    Stockfisch Level 4 (Elo ~ 1560): 10s move: jacky wins 60% of Games


### Version 0.9.5

- killer move heuristic
- clean up & rework move ordering (especially mvvlva)

### Version 0.9.6

- small code changes tt caching

### Version 0.9.7

- fix generation of non quiet moves which where introduced probably in 0.9.4.


    Stockfisch Level 4 (Elo ~ 1560): 10s move: jacky wins 60% of Games

### Version 0.9.8

- slightly better move ordering


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


### Version 0.9.8.2

- fix: missing reset in board stats generator lead to evaluate to wrong patt situations


### Version 0.9.9

- reworked patt checks
    - is now independant of evaluation function
    - fix: did not work in quiescence for the new evaluation function



    Stockfisch Level 5 (Elo ~ 1630): 10s move: wins 70%   
    Stockfisch Level 5 (Elo ~ 1630): 2s move: wins 70%   


### Version 0.9.10

- cleaned up unused code (old Move classes, etc)
- optimized move lists and move classes
- experimental code available as options:
    - null move pruning. not activated by default, as it doesnt seem any benefit so far.
    - late move reductions. not activated by default as it doesnt bring any benefit so far.
    - hash move ordering: doesnt bring anything (since it is always the same as the pv move)


    Stockfisch Level 5 (Elo ~ 1630): 10s move: wins    
    Stockfisch Level 5 (Elo ~ 1630): 2s move: wins 75%


### Version 0.9.11

- fixed UCI time control options: wtime, btime, winc, binc have been ignored if movestogo has not been set


### Version 0.9.12

- fixed missing promotion letter on best move
- reworked time control calucation for blitz chess a bit

### Version 0.9.13

- fixed stupid bug in time calculation which produced much too high values which exceeded the complete rest time.

### Version 0.9.14

- fixed issue when running engine within a batch file: uci quit has not exit process (because of other threads still running)
- log files are by default not written, but only if java property jacky.logging.activate is set to true.
- minor internal code refactorings


### Version 0.9.15

- cleaned up and fixed mvvLva code. Seems to give slightly better pruning.


### Version 0.9.15.1

- fixed history heuristic
    - history heuristic is now scaled by "bad" history heuristics
    - seems to gain now something by sprt testing ~40 Elos


### Version 0.9.16

- bitboard implementation
    - move generation uses magic bitboards
    - check checker uses bitboards

move generation and search should therefore a bit faster, overall search should be ~25% faster.

    Stockfisch Level 5 (Elo ~ 1630): 2s move:


### Version 0.9.17

- chess extensions (deactivated,does not bring anything so far...)
- pseudo legal move generation for faster move generation in Alpha Beta Pruning
- parameterized evaluation function (based on CPW) which is slightly better thant the minimal pst evaluation
- 

     
#### bugfix

- fixed issue delivering illegal pv moves after a check mate;