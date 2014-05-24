jerry-http
==========

Common Java functionality for working with HTTP requests.

`jerry-http` is a module library for the uber `jerry` library project. This module provides easy
wrapper functionality to work with HTTP requests using the Apache HTTP client library.

For more information on the project, refer to the uber https://github.com/sangupta/jerry project.

Downloads
---------

The library can be downloaded from Maven Central using:

```xml
<dependency>
    <groupId>com.sangupta</groupId>
    <artifactId>jerry-http</artifactId>
    <version>0.9.5</version>
</dependency>
```

Release Notes
-------------

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

Versioning
----------

For transparency and insight into our release cycle, and for striving to maintain backward compatibility, 
`jerry-http` will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

License
-------
	
```
jerry - Common Java Functionality
Copyright (c) 2012-2014, Sandeep Gupta

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
