# Jacky Chess

A simple UCI chess engine written in Java named after my dog "Jacky"!
                                                  
The engine is written from scratch as an educational project to learn chess programming.
It started as a simple proof of concept experiment within a weekend, but is now derived to a stable playing UCI engine.

I got a lot of inspiration from other resources in the web, mainly the great https://www.chessprogramming.org/ wiki
and several open source engines, just to name a few of them: cpw, stockfish, fruit, chess22k.
                      
## Features
                                 
- regular Chess
- Fischer Random Chess (Chess960)
- Multithreading

## Goals of the engine

- educational purpose: main focus is to learn about chess programming
- the code is (hopefully) simple and clear since the main purpose is to understand the algorithms.
- the code should be flexible: it uses interfaces and configurations to exchange different implementations for testing.
- the evaluation function is configurable to switch parameters for experimentation
- having fun to program it :)

## Requirements
                                   
## Building

At least Java 11 & maven to compile the engine.
However, it should run under any newer Java version. It is mainly tested with Java 11 under Linux.
           
## Usage


 - You need a Java JRE Environment, at least Java 11 to run the engine.
 - You need an UCI Gui Client to register the engine for usage. The chess engine should work with any UCI chess client.
It was mainly tested with [Arena](http://www.playwitharena.de/) and with [cutechess](https://github.com/cutechess/cutechess)
and works fine with these clients. However - any UCI compliant UI should work.

Simply add the engine to your preferred UCI Gui Client, and then you should be ready to use it. Most clients should be fine
to select the jar file directly.
You should have a few UCI options able to set in the UI then.

## CCRL Rating

Many thanks to the CCRL team for rating my engine. You can find the details 
on http://ccrl.chessdom.com/ccrl/404/:          

Here is a quick overview of my estimated rating and the results from the CCRL team (the numbers maybe outdated):

| Version | Estimated Rating | CCRL Blitz | 40/15 |
|---------|------------------|------------|-------|
| 24.04   | 2800             |            |       |
| 23.12   | 2760             | 2695       | 2731  |
| 23.06   | 2680             | 2688       | 2675  |
| 0.14.3  | 2580             | 2595       | 2601  |
| 0.13.1  | 2330             | --         | 2397  |
| 0.13.0  | 2330             | 2388       | --    |
| 0.12.0  | 2180             | 2284       | --    |
| 0.10.0  | 2000             | 2119       | --    |
| 0.9.14  | --               | 1453       | --    |

## Copyright


                Jacky Chess 
    Copyright (C) 2023  Matthias Lang

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

- bitboards
- move generator using a magic bitboards  
- a configurable evaluation function considering material, mobility and several other simple evaluations
  - evaluation cache and pawn/king cache
  - most parameters are tuned via the texel tuning method
  - special end game evaluations
- negamax with PVS and alpha beta pruning
- iterative deepening with aspiration windowing
- null move pruning and static null move pruning
- razoring
- late move reduction
- staged move generation and separate move generation for quiescence
- move sorting by hash moves, killer moves, history heuristic, counter moves; captures sorted by SEE
- transposition table using zobrist hashing to cache scores
- quiescence search with pruning, e.g. delta cut off, futility pruning and move count based pruning
- multithreading with Lazy SMP


## UCI Parameter

The engine has some UCI parameter which can be set either by the user interface or via configuration.

Find more information under [UCI Parameter](docs/uciparameter.md)

## Internal Parameters

The engine has a lot of parameters which are mainly for developing and testing purpose and should not be changed
by a regular user.

[Parameter Documentation](docs/internalparameter.md)



# Versions

see [Version History](docs/versionhistory.md)                     


# Build and Development 

Building the project

    mvn clean package

This will build the project and execute all fast unit tests

    mvn clean package -PallTests

this will build and execute all tests including slow ones and will take several minutes to run.

### run integration tests

    mvn verify

### Generate test reports

after building and running tests, call:

    mvn surefire-report:report-only

or just 

    mvn site


The project uses the  [templating-maven-plugin](https://www.mojohaus.org/templating-maven-plugin/index.html)to change some constant values in the source code during build.
It is used in the project to include/exclude some additonal test/valiation code in the build (just like C Preprocessors).

The class BuildConstants is generated by a template and holds the templated variables. The default profile sets the
values to exclude all additional code to generate the fastest code. Other profiles use the addtional generated
code. Its possible to set those variables during maven build to override the values, e.g.

    mvn clean package -DactivateAssertions=true -DactivateStatistics=true

Currently this is used to add assertions to test versions, but to exclude the complete assertion code from release versions.

Generate a test version with assertions:

    mvn clean package -P tests

Generate a release version without assertions:

    mvn clean package


## todo 
           
- optimizations on all ends...
- make evaluation better by considering more aspects
- more tuning of evaluation parameter and using better tuning data
- pruning optimization
- tt cache optimization


