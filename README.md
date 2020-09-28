# Sample Java web filter library [![Travis CI status](https://travis-ci.com/AnatolyevPavel/webmetrics.svg?branch=master)](https://travis-ci.com/github/AnatolyevPavel/webmetrics)
Simple metric-gathering library in Java for web application. Library is implemented as a servlet filter and collects the following information:
* Request Time: time spent between when the application starts to process the request and the time when the application sends the response to the client.
* Response Sizes: the size of the HTTP response body in bytes.
* Metrics: minimum, average, and maximum for Request Time and Response Size.

The library stores all information in memory.

The purpose of creating the library was to get on-hands knowledge about Java servlet filters.

## Building the library
Run `mvn clean install` to build and install the library to your local Maven repository.

## Using the library
Here are the steps to add the library to a Maven Spring Boot project:
1. Add library dependency to pom.xml
```
<dependency>
	<groupId>pa.pavelan</groupId>
	<artifactId>webmetric</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```
2. Add filter configuration to a configuration file
```
@Component
public class FilterRegistrationConfig {
    @Bean
    public FilterRegistrationBean<MetricsFilter> filterRegistration () {
        FilterRegistrationBean<MetricsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MetricsFilter());
        registration.setOrder(1);
        return registration;
    }
}
```
3. Have fun
