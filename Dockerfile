FROM lightjason/agentspeak

# --- configuration section ----------------------
ENV DOCKERIMAGE_BENCHMARK_VERSION HEAD


# --- machine configuration section --------------
RUN git clone https://github.com/LightJason/Benchmark.git /tmp/benchmark
RUN cd /tmp/benchmark && git checkout $DOCKERIMAGE_BENCHMARK_VERSION
RUN cd /tmp/benchmark && mvn package -DskipTests

RUN mkdir -p /root/bin
RUN cd /tmp/benchmark && export JAR=$(mvn -B help:evaluate -Dexpression=project.build.finalName | grep -vi info | grep -ivvv "warning") && mv target/$JAR.jar /root/bin
RUN cd /tmp/benchmark && export JAR=$(mvn -B help:evaluate -Dexpression=project.build.finalName | grep -vi info | grep -ivvv "warning") && echo -e "#!/bin/sh -e\\nSRC=\$(dirname \$0)\\njava -jar \$JAVA_OPTS \$SRC/$JAR.jar \$@" > /usr/local/bin//benchmark
RUN chmod a+x /usr/local/bin/benchmark
