# Jacky Chess

A simple UCI chess engine written in Java.

This project is mainly a self educational project to 
learn the various algorithms around chess computers. 
Inspiration was mainly given by the great chess programming wiki
https://www.chessprogramming.org/ where you can get a good overview about all used
algorithms.

## Goals of the engine

- educational purpose: main focus is to learn about chess programming
- the code is simple and clear since the main purpose is to understand the algorithms.
- the code should be exchangeable: the different implementations for search algorithms and
  related implementations are implemented by interfaces and exchangeable. This is needed to compare the algorithms, too.
- the engine should be at least so good that it can defeat me (a mediocre amateur chess player, I hope.. :)
- having fun to program it :)

## Requirements

Java 8 to compile the engine.

The chess engine should work under any UCI chess client.
It was mainly tested with Arena http://www.playwitharena.de/ 
and works fine with this client. It was also tested with pyChess. However - any UCI complient UI should work.

Simply add the engine to your prefered UCI Gui Client and then you should be ready to use it.
You should have several UCI options able to set in the UI then.



## License

    Jacky Chess 
    Copyright (C) 2021  Matthias Lang

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses/.

## Implementation

The chess engine uses following technics/algorithms 

- a simple Board using 64 bytes for coding the figures.
- Move Generator using a "mailbox" 10x12 Board  
- a simple Evaluation function which mainly checks material
and basic positional things like mobility and captures
- an alternative PST only evaluation function
- Search Algorithm is PVS with an iterative deepening algorithm using Alpha Beta Negamax.
- Aspiration windowing
- Move sorting by PV Moves, killer moves, history heuristic
- Transposition Table using zobrist hashing to cache Scores
- Quiescence Search



Still a lot is missing to have a really good, competing engine and the engine
will probable never reach game strength of its competitors.
Nevertheless the goal is to make it stronger with each version.

## UCI Parameter

The engine has several UCI parameter. Find more information under [UCI Parameter](docs/uciparameter.md)

           
## Experimental Implementations

### table based move generation

in branch experimental/tableBasedMoveGenerator

This implementation uses a pre-filled move table with all possible moves, instead of a 12x8 field. But it does not look like an improvement.
First simple tests show that it is slightly slower than the current move generator implementation.

### mtdf

The mtdf implementation is currently not working properly and therefore does not give beneficial results.


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

- experimental code available as options:
    - null move pruning. not activated by default, as it doesnt seem any benefit so far.
    - late move reductions. not activated by default as it doesnt bring any benefit so far.
    - hash move ordering: doesnt bring anything (since it is always the same as the pv move)

- fixed UCI time control options: wtime, btime, winc, binc have been ignored if movestogo has not been set


### Version 0.9.12

- fixed missing promotion letter on best move
- reworked time control calucation for blitz chess a bit


## todo 

- todo: experiments/perfts with better tt caching...
- todo: combine pst evaluation with some non-static-evaluations like pawn, king safety, etc, endgame (king distance)...

- todo add pseudo rnd opening option to better measure turnaments          



## bugs

see [Current known bugs](docs/bugs.md)
