package example;

import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import io.helidon.media.jsonp.JsonpSupport;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.spi.ConfigSource;

public class Main{

  private static Routing setRoutes(){
    Routing routing = Routing.builder()
        .register("/dog", new DogResource())
        .get("/", (request, response) -> response.send("Hello World !"))
        .build();
    return routing;
  }

  private static int getPort(){      
    ConfigSource configSource = ConfigSources.classpath("application.yml").build();
    
    Config config = Config.builder()
            .disableSystemPropertiesSource()
            .disableEnvironmentVariablesSource()
            .sources(configSource)
            .build();

    return config.get("server.port").asInt().get(); 
  }

  public static void main(String... args) throws Exception {
      Routing routing = setRoutes();
      int port = getPort();

      WebServer.builder(routing)
        .port(port)
        .addMediaSupport(JsonpSupport.create())
        .addRouting(routing)
        .build()
        .start()
        .thenAccept(ws ->
            System.out.println("Server started at: http://localhost:" + ws.port())
        );


  }

}