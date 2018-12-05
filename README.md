# YaCy Grid Component: Search

The YaCy Grid is the second-generation implementation of YaCy, a peer-to-peer search engine.
A YaCy Grid installation consists of a set of micro-services which communicate with each other
using the MCP, see https://github.com/yacy/yacy_grid_mcp

## Purpose

The Search microservice is a search aggregation server.
In the current implementation status there is not yet any aggregation implemented.
However, the microservice can be used to access the MCP-internal search servlet
which is deployed in the search microservices as well.

To use the search service, call:
http://localhost:8800/yacy/grid/mcp/index/yacysearch.json?query=42
or
http://localhost:8800/yacy/grid/mcp/index/gsasearch.xml?q=42

## Installation: Download, Build, Run
At this time, yacy_grid_parser is not provided in compiled form, you easily build it yourself. It's not difficult and done in one minute! The source code is hosted at https://github.com/yacy/yacy_grid_search, you can download it and run it with:

    > git clone --recursive https://github.com/yacy/yacy_grid_search.git

If you just want to make a update, do the following

    > git pull origin master
    > git submodule foreach git pull origin master

To build and start the search, run

    > cd yacy_grid_search
    > gradle run

Please read also https://github.com/yacy/yacy_grid_search/README.md for further details.


## Contribute

This is a community project and your contribution is welcome!

1. Check for [open issues](https://github.com/yacy/yacy_grid_search/issues)
   or open a fresh one to start a discussion around a feature idea or a bug.
2. Fork [the repository](https://github.com/yacy/yacy_grid_search.git)
   on GitHub to start making your changes (branch off of the master branch).
3. Write a test that shows the bug was fixed or the feature works as expected.
4. Send a pull request and bug us on Gitter until it gets merged and published. :)


## What is the software license?
LGPL 2.1

Have fun!

@0rb1t3r
