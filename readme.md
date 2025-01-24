
<div align="center">
<img src="docs/logos/jacky_chess_logo_01.png" width="400">
<h3>Jacky Chess</h3>

A simple UCI chess engine written in Java named after my dog "Jacky"!
<br>
play at [jackychessbot](https://lichess.org/@/jackychessbot)
<br>
<br>
</div>
                              
## Overview

The engine is written from scratch as an educational project to learn chess programming.
It started as a simple proof of concept experiment on a weekend, but is now derived to a stable playing UCI engine.

I got a lot of inspiration from other resources in the web, mainly

- the great https://www.chessprogramming.org/ wiki: This was really the starting point once I stumbled upon this great website. It describes in all details how chess engines work and all related algorithms, optimizations, etc. Its a great source of inspiration when you try to create your own chess engine or if you want to understand how chess engines work
- cpw: an educational chess engine which is very simple, but therefore easy to understand
- stockfish: one of the top engines. Therefore rather complex but you get a lot of ideas from the source
- fruit: one of the revolutionary engines at its release time and inspiration for many following engines with an easy understandable source code
- chess22k: implemented in java it helped me of how to deal with some java specific problems
- laser: another interesting engine with good source code documentation
- https://github.com/asdfjkl/neural_network_chess: great book explaining NNUE. Probably my next big thing...


                      
## Features
                                 
- regular Chess
- Fischer Random Chess (Chess960)
- Multithreading support

## Goals of the engine

- educational purpose: main focus is to learn about chess programming and the aim was to create an engine playing better than myself... (well.. that was eventually easy...)
- the code is (hopefully) simple and clear since the main purpose is to understand the algorithms.
- the code should be flexible: it uses interfaces and configurations to exchange different implementations for testing. Its split into several modules for reusage.
- the evaluation function is configurable to switch parameters for experimentation and to easy tune it via the texel method
- having fun to program it and play with it :)
           
## Usage

You need a Java JRE Environment installed on your computer. At least Java 11 is required, any newer Java Environment should work.

Since this is only a UCI engine you need an UCI compatible chess gui to register the engine for usage. The chess engine should work fine with any UCI chess gui.
It was mainly tested with [Arena](http://www.playwitharena.de/), with [Shredder 13](https://www.shredderchess.com/) and with [cutechess](https://github.com/cutechess/cutechess)
and works fine with these clients. However - any UCI compliant UI should work.

Simply add the engine to your preferred UCI Gui Client, and then you should be ready to use it. Most clients should be fine
to select the jar file directly. Some other clients may need special configuration. Please refer to the user manual of your chess gui.

Here is described how to add the engine to the Chess Guis which I use:

### Arena

In [Arena](http://www.playwitharena.de/) you can simply select the jar file directly in the Dialog to add a new engine. Arena recognizes the engine
as java application itself and knows how to deal with it.

### Shredder

Its not possible to install a java uci engine in Shredder directly via the user interface. You need to manually
create a new *.eng file under c:\<your user>\AppData\Local\ShredderChess\GUI13\Engines folder or copy one of the existing.

The file must contain the following rows, you need of course adjust the paths to your needs:

      [ENGINE]
      Name=Jacky Chess 24.06
      Author=Matthias Lang
      Filename=<path to your java installation folder>\java.exe
      Parameter=-jar <path to where you copied the jacky chess jar file to>\jackychess-24.06.jar


### Cute Chess

You can configure the engine under Cute Chess via the user interface. Create a new engine and in the dialog you need to enter:

- Name: the descriptive name you give the engine
- Command: java -jar jackychess-24.06.jar
- Working Directory: the directory where the jar file is saved
- Protocol: UCI





## UCI Parameter

The engine has some UCI parameter which can be set either by the user interface or via configuration. Usually all 
chess guis have the option to set these parameters via the user interface.

Find more information under [UCI Parameter](docs/uciparameter.md)


## Lichess

You can play against the engine at lichess under [jackychessbot](https://lichess.org/@/jackychessbot)


## CCRL Rating

Many thanks to the CCRL team for rating my engine. You can find the details 
on http://ccrl.chessdom.com/ccrl/404/:          

Here is a quick overview of my estimated rating and the results from the CCRL team (the numbers maybe outdated):

| Version | Estimated Rating | CCRL Blitz | 40/15 | 40/2 FRC |
|---------|------------------|------------|-------|----------|
| 24.06   | 2840             | 2794       | 2847  |          |
| 24.04   | 2800             | 2792       | 2781  | 2764     |
| 23.12   | 2760             | 2695       | 2731  |          |
| 23.06   | 2680             | 2688       | 2675  |          |
| 0.14.3  | 2580             | 2595       | 2601  |          |
| 0.13.1  | 2330             | --         | 2397  |          |
| 0.13.0  | 2330             | 2388       | --    |          |
| 0.12.0  | 2180             | 2284       | --    |          |
| 0.10.0  | 2000             | 2119       | --    |          |
| 0.9.14  | --               | 1453       | --    |          |

## Copyright


                Jacky Chess 
    Copyright (C) 2025  Matthias Lang

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
- move generator using magic bitboards  
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




## Internal Parameters

The engine has several parameters which are mainly for developing and testing purpose and should not be changed
by a regular user.

[Parameter Documentation](docs/internalparameter.md)



## Versions

see [Version History](docs/versionhistory.md)                     


## Building and development

At least Java 11 & maven to compile the engine.
However, it should run under any newer Java version. It is mainly tested with Java 11 under Linux.

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

Generate a test version with assertions:

    mvn clean package -P tests

Generate a release version without assertions:

    mvn clean package

### Optional Assertion code

The project uses the  [templating-maven-plugin](https://www.mojohaus.org/templating-maven-plugin/index.html)to change some constant values in the source code during build.
It is used in the project to include/exclude some additional test/validation code in the build (just like C Preprocessors).

The class BuildConstants is generated by a template and holds the templated variables. The default profile sets the
values to exclude all additional code to generate the fastest code. Other profiles use the additionally generated
code. It is possible to set those variables during maven build to override the values, e.g.

    mvn clean package -DactivateAssertions=true -DactivateStatistics=true

This is used to add assertions to test versions, but to exclude the complete assertion code from release versions.


## todo and future plans
           
- optimizations on all ends...
- make evaluation better by considering more aspects
- more tuning of evaluation parameter and using better tuning data
- pruning optimization
- alternative evaluation with nnue?


