## Cargo with CA

The idea behind this project is to build something similar to [DDDSample](https://github.com/citerus/dddsample-core).
But using slightly different approach. I'll try to use ideas and principles behind:

- Hexagonal architecture
- Clean architecture
- Use case driven development

There should be series of companion articles on Medium which will detail the interesting points and decisions behind this
application.

### Copyright notice

This application is based heavily on the original [DDDSample](https://github.com/citerus/dddsample-core) application
by [Citerus AB](http://www.citerus.se/) and [Domain Language](https://www.domainlanguage.com/).

The original work is distributed under the MIT licence. I've included the original licence with this project.

Wherever possible, I'll try to mention the original code which was consulted for any specific implementation point. 

### How run

This application is a standard Spring Boot application. There is also a Docker compose file with a Postgres database,
which should be started prior to the execution of the application.