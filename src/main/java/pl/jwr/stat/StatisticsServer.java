package pl.jwr.stat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.handler.CorsHandler;
import pl.jwr.stat.services.StatisticsService;
import pl.jwr.stat.services.dto.DataTable;
import pl.jwr.stat.utils.BindingModule;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mjawor on 2017-04-21.
 */
public class StatisticsServer extends AbstractVerticle {
    private static final String HTTP_HOST = "127.0.0.1";
    private static final Integer HTTP_PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsServer.class);

    @Inject
    private StatisticsService statisticsService;


    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();

        //Binds all dependencies to already initialized vertx instance
        Guice.createInjector(new BindingModule(vertx)).injectMembers(this);

        Router router = Router.router(vertx);
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        allowHeaders.add("Content-Length");

        router.route().handler(BodyHandler.create());
        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.DELETE));

        //Endpoints
        router.get("/").handler(this::handleSimpleOperation);
        router.post("/cor").handler(this::handleCorrelationOperation);
        router.get("/script").handler(this::handleScriptSimpleOperation);
        router.get("/csv").handler(this::handleCSVScriptAnalyze);



        //properties from the config.json file
        Integer port = config().getInteger("http.port", HTTP_PORT);
        String host = config().getString("http.address", HTTP_HOST);
        LOGGER.info("http://" + host + ":" + port);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(port, host)
                .subscribe(
                        server -> {
                            future.complete();
                        },
                        error -> {
                            future.fail(future.cause());
                        }
                );

    }

    public void handleSimpleOperation(RoutingContext context) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String result = objectMapper.writeValueAsString(statisticsService.getSimpleOperationResult());
            context.response()
                    .putHeader("Content-Type", "application/json")
                    .setStatusCode(200)
                    .end(result);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(400, context.response());
        }
    }

    public void handleCorrelationOperation(RoutingContext context) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DataTable data = objectMapper.readValue(context.getBody().toString(), DataTable.class);
            String result = objectMapper.writeValueAsString(statisticsService.getCorrelationResult(data));
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(result);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(400, context.response());
        }
    }

    public void handleScriptSimpleOperation(RoutingContext context){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String result = objectMapper.writeValueAsString(statisticsService.getScriptSimpleOperationResult());
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(result);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(400, context.response());
        }
    }

    public void handleCSVScriptAnalyze(RoutingContext context){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String result = objectMapper.writeValueAsString(statisticsService.getResultOfCsvDataAnalyze());
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(result);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(400, context.response());
        }
    }


    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }
}
