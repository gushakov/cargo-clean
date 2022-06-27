## Cargo tracking with Clean DDD

The idea behind this project is to build something similar to [DDDSample](https://github.com/citerus/dddsample-core).
But using slightly different approach. I'll try to use ideas and principles behind:

- Hexagonal architecture
- Clean architecture
- Use case driven development

There should be an articles on Medium which will detail the interesting points and decisions behind this application.

### Copyright notice

This application is based heavily on the original [DDDSample](https://github.com/citerus/dddsample-core) application
by [Citerus AB](http://www.citerus.se/) and [Domain Language](https://www.domainlanguage.com/).

The original work is distributed under the MIT licence. I've included the original licence with this project.

Wherever possible, I'll try to mention the original code which was consulted for any specific implementation point.

### How to run

This application is a standard Spring Boot application. There is also a Docker compose file with a Postgres database,
which should be started prior to the execution of the application. You may need to start the database for some of
the integration tests as well.

### Exploring DDDSample DB

To be able to explore the relational model used by DDDSample, we can add an embedded web server to the original application.
There is an example configuration available here: [HsqlDbServerConfig.java](./etc/db/HsqlDbServerConfig.java).

To launch the server, you need `hsqldb-2.4.0.jar` (available from Maven central):

> java -cp ~/.m2/repository/org/hsqldb/hsqldb/2.4.0/hsqldb-2.4.0.jar org.hsqldb.util.DatabaseManagerSwing

This should start the UI of the DB viewer. The type of connection to use is "HSQL Database Engine WebServer" and JDBC 
URL to use is `jdbc:hsqldb:hsql://localhost/dddsample`.