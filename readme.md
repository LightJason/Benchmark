# LightJason - Benchmark

![Docker](https://img.shields.io/docker/build/lightjason/benchmark.svg)

This repository contains a _benchmarking suite_ for the LightJason multi-agent framework. We try to keep the structure of a [Java Microbenchmark Harness](http://openjdk.java.net/projects/code-tools/jmh/) and adapted for the structure of a multi-agent system. Additional information of benchmarking can be found 

* [Oracle - Avoiding Benchmarking Pitfalls on the JVM](http://www.oracle.com/technetwork/articles/java/architect-benchmarking-2266277.html)
* [Codecentric - Performance measurement with JMH](https://blog.codecentric.de/en/2017/10/performance-measurement-with-jmh-java-microbenchmark-harness/)
* [Heise - JMH: Microbenchmarking auf der Java Virtual Machine](https://www.heise.de/developer/artikel/JMH-Microbenchmarking-auf-der-Java-Virtual-Machine-2162093.html?seite=all)
* [Jaxcenter - Aus der Java-Trickkiste: Microbenchmarking](https://jaxenter.de/aus-der-java-trickkiste-microbenchmarking-24155)

For our scenario we are using our [example docker container](https://hub.docker.com/r/lightjason/examples/) with the shellcommand ```benchmark```, that allows to run the scenario defintion
