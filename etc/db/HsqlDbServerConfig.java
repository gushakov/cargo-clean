import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

/*

    Notes:
    -----

    This configuration can be used to run an embedded web server to explore HSQL database
    used by the original DDDSample. It should be added to the scan path of the application.

    References:
    ---------

    1.  How to start HSQL DB web server: https://bit.ly/3xFOvn8

 */

@Configuration
public class HsqlDbServerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server hsqldbServer() {

        final Server server = new Server();

        final Properties props = new Properties();
        props.setProperty("server.database.0", "mem:dddsample");
        props.setProperty("server.dbname.0", "dddsample");
        props.setProperty("server.no_system_exit", "true");
        props.setProperty("server.port", "9001");

        try {
            server.setProperties(new HsqlProperties(props));
        } catch (IOException | ServerAcl.AclFormatException e) {
            throw new BeanInitializationException(e.getMessage(), e);
        }

        return server;

    }

}
