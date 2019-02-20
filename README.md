# jerry-http

[![Build Status](https://travis-ci.org/sangupta/jerry-http.svg?branch=master)](https://travis-ci.org/sangupta/jerry-http)
[![Coverage Status](https://coveralls.io/repos/github/sangupta/jerry-http/badge.svg?branch=master)](https://coveralls.io/github/sangupta/jerry-http?branch=master)
[![Maven Version](https://maven-badges.herokuapp.com/maven-central/com.sangupta/jerry-http/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.sangupta/jerry-http)

Common Java functionality for working with HTTP requests. It hides the complexities of setting up
and configuring the `Apache HTTP Client Library` in the code, and uses the same to make all requests.
This helps reduce a lot of boiler-plate code, as you can get started from the word `go`.

`jerry-http` is a module library for the uber `jerry` library project. This module provides easy
wrapper functionality to work with HTTP requests using the Apache HTTP client library.

For more information on the project, refer to the uber https://github.com/sangupta/jerry project.

The library is tested on the following JDK versions:

* Oracle JDK 9
* Oracle JDK 8
* Oracle JDK 7
* Open JDK 7

## Features

* Make web requests without all the boiler plate code of the Apache HTTP Client Library
* Use sensible defaults for connection/socket timeout and others
* Adds mechanism for rate-limiting calls to an end-point or host
* Many convenience methods to get request/response data
* Deal with two standard POJO objects than deal with many Apache HC specific objects

## Roadmap

* Add a download manager
* Add an import/export for `curl` commands

## Breaking changes from version 1.0.0

* `WebUtils` methods have now moved to `HttpService`
 

## Usage

`jerry-http` makes working with HTTP requests super easy. To fetch a response from the server, use,

```java
WebResponse response = WebInvoker.getResponse("https://github.com");
```

If you want to have total control over your request, you may do as:

```java
WebRequest request = WebRequest.get("https://github.com")		// create the request
							   .connectTimeout(1000)			// specify the connection timeout
							   .socketTimeout(1000)				// specify the socket timeout
							   .cookiePolicy(cookiePolicy)		// set up the cookie policy to be used
							   .followRedirects();				// whether redirects need to be followed or not
							   
WebResponse response = request.execute().webResponse();

// check if we succeeded
if(response.isServerError()) {
}

if(response.isClientError()) {
}

if(response.isSuccess()) {
	String content = response.getContent();
}
```

Many other convenience methods are available in `WebInvoker` and `WebResponse` to deal with.


## Downloads

The library can be downloaded from Maven Central using:

```xml
<dependency>
    <groupId>com.sangupta</groupId>
    <artifactId>jerry-http</artifactId>
    <version>2.0.0</version>
</dependency>
```

## Release Notes

**2.0.0 (26 Dec 2016)**

* Better API structuring
* Align for loose coupling and easy unit-testing
* Upgraded HttpClient library
* A few bug fixes

**1.0.0 (13 Nov 2014)**

* Move to 1.0.0 release as library is quite stable now
* Added flag in `WebResponse` to indicate if the request redirected before getting a response
* Added method to `WebResponse` to fetch the entire redirected chain of `URI`s

**0.10.0 (28 Oct 2014)**

* Upgraded Apache HttpClient and other dependencies to latest released versions

**0.9.6 (13 Jun 2014)**

* Added methods to update the request body from `String` objects

**0.9.5 (23 May 2014)**

* Updated dependency versions for `xstream`, `gson`, and `jerry-core`

**0.9.4 (23 May 2014)**

* Stopped cloning on response byte[] stream for performance reasons
* Fixed a null pointer exception
* Added method to return the value of parameter in a `WebForm`

**0.9.2 (02 Apr 2014)**

* Updated `WebResponse.trace()` method to display more relevant details
* Added option to replace a parameter in `WebForm`
* Updated JavaDocs

**0.9.0 (08 Feb 2014)**

* Initial release after bifurcation from `jerry` project

## Versioning

For transparency and insight into our release cycle, and for striving to maintain backward compatibility, 
`jerry-http` will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

## License
	
```
jerry - Common Java Functionality
Copyright (c) 2012-2016, Sandeep Gupta

http://sangupta.com/projects/jerry-http

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
