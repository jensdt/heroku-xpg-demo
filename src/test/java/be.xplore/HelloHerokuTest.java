package be.xplore;


import io.undertow.Undertow;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class HelloHerokuTest {

    private Undertow server;
    private String baseUrl;

    @Before
    public void setup() {
        int randomHttpPort = new Random().nextInt(3000) + 1024;
        HelloHeroku helloHeroku = new HelloHeroku(randomHttpPort);

        baseUrl = "http://" + helloHeroku.listenerHost() + ":" + helloHeroku.listenerPort();
        server = helloHeroku.createServer();
        server.start();
    }

    @After
    public void teardown() {
        server.stop();
    }

    @Test
    public void testRoot() throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            String response = EntityUtils.toString(client.execute(new HttpGet(baseUrl + "/")).getEntity(), "UTF-8");

            assertEquals("Hello World", response);
        }
    }
}
