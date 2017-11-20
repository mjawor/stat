package pl.jwr.stat;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by mjawor on 2017-04-21.
 */
@RunWith(VertxUnitRunner.class)
public class StatisticsServerTest {
    private static final int PORT = 8080;
    private Vertx vertx;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(StatisticsServer.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testMain(TestContext context) {
        final Async async = context.async();

        vertx.createHttpClient().getNow(PORT, "localhost", "/", response -> {
            response.handler(body -> {
                context.assertTrue(body.toString().contains("6"));
                async.complete();
            });
        });
    }

    @Test
    public void corTestMain(TestContext context) {
        final Async async = context.async();

        vertx.createHttpClient().post(PORT, "localhost", "/cor", response -> {
            response.handler(body -> {
                context.assertTrue(body.toString().contains("0.612821562361268"));
                async.complete();
            });
        }).putHeader("content-length", Integer.toString(TEST_CONTENT.length())).write(TEST_CONTENT);
    }

    @Test
    public void testSimpleScript(TestContext context) {
        final Async async = context.async();

        vertx.createHttpClient().getNow(PORT, "localhost", "/script", response -> {
            response.handler(body -> {
                context.assertTrue(body.toString().contains("6"));
                async.complete();
            });
        });
    }

    @Test
    public void testCSVScript(TestContext context) {
        final Async async = context.async();

        vertx.createHttpClient().getNow(PORT, "localhost", "/csv", response -> {
            response.handler(body -> {
                context.assertTrue(body.toString().contains("0.32465246735835"));
                async.complete();
            });
        });
    }


    public static final String TEST_CONTENT = "{\n" +
            "      \"keyColumn\":[\n" +
            "         20,\n" +
            "         19,\n" +
            "         19,\n" +
            "         19,\n" +
            "         19,\n" +
            "         22,\n" +
            "         22,\n" +
            "         22,\n" +
            "         22,\n" +
            "         22,\n" +
            "         21,\n" +
            "         21,\n" +
            "         18,\n" +
            "         18,\n" +
            "         18,\n" +
            "         25,\n" +
            "         24,\n" +
            "         24,\n" +
            "         23,\n" +
            "         23\n" +
            "      ],\n" +
            "      \"valueColumn\":[\n" +
            "         0,\n" +
            "         0,\n" +
            "         1,\n" +
            "         0,\n" +
            "         0,\n" +
            "         0,\n" +
            "         0,\n" +
            "         1,\n" +
            "         0,\n" +
            "         1,\n" +
            "         1,\n" +
            "         0,\n" +
            "         0,\n" +
            "         1,\n" +
            "         0,\n" +
            "         1,\n" +
            "         1,\n" +
            "         1,\n" +
            "         0,\n" +
            "         1\n" +
            "      ]\n" +
            "}";


}
