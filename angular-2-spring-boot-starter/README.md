# Angular 2 Spring Boot Project

## Overview

The goal of this project is to be a template for an Angular 2 UI running in a 
Spring Boot project. The Spring Boot project is created using SPRING INITIALIZR 
from:

	http://start.spring.io

The AngularJS portion was created using the AngularJS quick-start seed project located here: 

	https://github.com/angular/quickstart/archive/master.zip

## Spring Boot Setup

As part of the spring init form, I selected / entered the following:

- Spring boot version 1.5.1
- Dependencies: Web, Security, Actuator
- Java version 1.8

I created a gradle.properties file and externalized the Spring Boot release number. 

I added the dependency for Spring Boot devtools to the gradle dependencies as it has features that 
are meant to help with static templating, etc.  

## Angular JS 2 Quickstart Setup

The quickstart from Angular is simply a zip download. I unzipped it, moved files into the project and 
manipulated as needed. 

### package.json

* I needed to change from using src/ to src/main/webapp/. This was changed for the build, build watch and lint scripts. 

### bs-config.json

* I needed to change from using src to src/main/resources/static. 

### bs-config.e2e.json

* I needed to change baseDir from src to src/main/resources/static.

### karma.conf.js

* I needed to change the appBase value from src/ to src/main/resources/static/

### karma-test-shim.js

* I needed to change the builtPaths value to use src/main/resources/static/. 




 
