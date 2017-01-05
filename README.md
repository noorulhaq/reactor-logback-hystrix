#Reactor Logback Hystrix

An extension of reactor-logback addon enriched with hystrix circuit breaker. This library is an outcome of below issue. Please read the comments for further detail.
<br/>https://github.com/reactor/reactor-addons/issues/63

## Getting it

1) Define maven repository
```xml
	<repository>
	  <id>sonatype-snapshots</id>
	  <name>Sonatype Snapshots</name>
	  <url>https://oss.sonatype.org/content/repositories/snapshots</url>
	  <snapshots>
	    <enabled>true</enabled>
	  </snapshots>
	</repository>
```

Provide your required reactor library.

```xml
	<dependency>
		<groupId>org.projectreactor</groupId>
		<artifactId>reactor-logback</artifactId>
		<version>${version}</version>
	</dependency>
```
  or
```xml
	<dependency>
		<groupId>io.projectreactor</groupId>
		<artifactId>reactor-logback</artifactId>
		<version>${version}</version>
	</dependency>	
```

2) Add dependency
```xml
	<dependency>
	   <groupId>io.github.noorulhaq</groupId>
	   <artifactId>reactor-logback-hystrix</artifactId>
       <version>1.0.0-SNAPSHOT</version>
	</dependency>
```
The libary support all three versions for reactor 1,2 & 3.

## Logging Setup

Below is the sample logback configuration with cuircuit breaker default options. ***loggingTimeout*** is the maximum time in which the underlying logger is suppose to write log on file system. It is equivalent to ***execution.isolation.thread.timeoutInMilliseconds*** cuircuit breaker configuration. 

```xml
	<appender name="asyncAppender" class="io.github.noorulhaq.reactor.logback.hystrix.AsyncAppender">
		<appender-ref ref="consoleAppender"/>
		<backlog>4096</backlog>
		<loggingTimeout>1000</loggingTimeout>
		<circuitBreakerSleepWindow>3000</circuitBreakerSleepWindow>
		<circuitBreakerErrorThresholdPercentage>25</circuitBreakerErrorThresholdPercentage>
		<circuitBreakerRequestVolumeThreshold>5</circuitBreakerRequestVolumeThreshold>
	</appender>
```

## Fallback Strategy

The default fallback implementation will lose logs in case the log writter is unable to write logs on file system. If you want to implement different fallback strategy then give your own AsyncAppender implemention by inheriting **io.github.noorulhaq.reactor.logback.hystrix.AsyncAppender** class and overriding **onFallback** method.

## Metrics
In case if you are interested to see circuit breaker metrics then follow below link.
https://github.com/noorulhaq/dropwizard-hystrix-metrics

-------------------------------------
_Licensed under [Apache Software License 2.0](www.apache.org/licenses/LICENSE-2.0)_
