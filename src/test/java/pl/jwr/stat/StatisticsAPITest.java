package pl.jwr.stat;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rx.Observable;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by mjawor on 2017-05-02.
 *
 * The test has been written for confirming of REST APIs, and based on a Wiremock solution.
 */
public class StatisticsAPITest {

    private static final int PORT = 8080;
    private static final String SERVER_RESPONSE = "{\"result\":\"6\"}";
    private Vertx vertx;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PORT);

    @Before
    public void setUp() {
        vertx = Vertx.vertx();
    }

    @After
    public void tearDown() {
        vertx.close();
    }

    @Test
    public void simpleTest() {
        //GIVEN
        stubFor(get(urlEqualTo("/test"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(SERVER_RESPONSE)));

        HttpClient httpClient = vertx.createHttpClient();

        // WHEN
        Observable<HttpClientResponse> request = RxHelper.get(httpClient, PORT, "localhost", "/test");

        Buffer bufferedResponse = request.flatMap(HttpClientResponse::toObservable)
                .reduce(Buffer.buffer(), Buffer::appendBuffer)
                .toBlocking().single();

        // THEN
        assertThat(bufferedResponse.toString()).isEqualTo(SERVER_RESPONSE);
    }
}
