# LightJason - Benchmark

![Docker](https://img.shields.io/docker/build/lightjason/benchmark.svg)

This repository contains a _benchmarking suite_ for the LightJason multi-agent framework. We try to keep the structure of a [Java Microbenchmark Harness](http://openjdk.java.net/projects/code-tools/jmh/) and adapted for the structure of a multi-agent system. Additional information of benchmarking can be found 

* [Oracle - HotSpot](http://www.oracle.com/technetwork/java/hotspotfaq-138619.html)
* [Oracle - Runtime flags](http://www.oracle.com/technetwork/articles/java/vmoptions-jsp-140102.html)
* [Oracle - Avoiding Benchmarking Pitfalls on the JVM](http://www.oracle.com/technetwork/articles/java/architect-benchmarking-2266277.html)
* [Oracle - Tuning For a Small Memory Footprint](https://docs.oracle.com/cd/E13150_01/jrockit_jvm/jrockit/geninfo/diagnos/tune_footprint.html)
* [Codecentril - Useful JVM Flags](https://blog.codecentric.de/en/2012/07/useful-jvm-flags-part-3-printing-all-xx-flags-and-their-values/)
* [Codecentric - Performance measurement with JMH](https://blog.codecentric.de/en/2017/10/performance-measurement-with-jmh-java-microbenchmark-harness/)
* [Codecentric - Nützliche JVM Flags – Teil 7 (CMS Collector)](https://blog.codecentric.de/2013/03/nutzliche-jvm-flags-teil-7/)
* [Heise - JMH: Microbenchmarking auf der Java Virtual Machine](https://www.heise.de/developer/artikel/JMH-Microbenchmarking-auf-der-Java-Virtual-Machine-2162093.html?seite=all)
* [Jaxcenter - Aus der Java-Trickkiste: Microbenchmarking](https://jaxenter.de/aus-der-java-trickkiste-microbenchmarking-24155)

For our benchmark scenarios we are using our [docker container](https://hub.docker.com/r/lightjason/benchmark/) with the shellcommand ```benchmark```, that allows to run the scenario defintion


## Scenario Configuration

A scenario will be defined by a [YAML](https://en.wikipedia.org/wiki/YAML) file and a set of ASL++ files, which contains the agent code. The main structure of the YAML is:

```yaml
# main definition
global:
    
    # statistic definition (default summary)
    statistic: summary | descriptive

    # warum-up runs of the runtime
    warmup: 5
    # number of runs of the simulation
    runs: 15
    # number of iteration on each run
    iterations: 3
    
    # logging rate in milliseconds of memory, zero disables logging
    memorylograte: 2500
    # alive message in milliseconds to show a message on screen
    alive: 480000

    # output json will be formated
    prettyprint: true | false


# runtime definition for agent execution
runtime:
    
    # type of the runtime (parameter t shows the possibility of setting threads)
    type: synchronized | workstealing | fixedsize(t) | cached | scheduled(t) | single
    # number of thread which are used, not all runtimes uses this value (default 1)
    threads: 3
    
    # neighborhood action type for an agent
    neighborhood: leftright


# agent definition
agent:

    # constants / variables of an agent
    constant:
        MaxCount: 5

    # agent sources, which are stored relative to the configuration file
    source:
    
        # first agent script with a list of agent counts for each run
        agent1.asl: [1, 2, 5, 10, 15, 30, 100, 200, 500, 1000, 5000, 10000, 50000, 100000, 500000]
        
        # second agent script with a formula for calculating agent count, "i" defines the run with is started with 1 
        agent2.asl: "i^2"
```        

## Data Output

The result of a run is a [JSON](https://en.wikipedia.org/wiki/JSON) file with the statistic values of the runs. The statistic structure is defined of the statistic configuration.
