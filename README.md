# reactor-logback-hystrix
An extension from reactor-logback addon eriched with hystrix circuit breaker

Follow below configurations to add to your project:

1) Define maven repository
'''
<repository>
  <id>sonatype-snapshots</id>
  <name>Sonatype Snapshots</name>
  <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
</repository>
'''

2) Add dependency
'''
		<dependency>
			<groupId>io.github.noorulhaq</groupId>
			<artifactId>reactor-logback-hystrix</artifactId>
			<version>1.0.0-SNAPSHOT</version>
		</dependency>
'''
