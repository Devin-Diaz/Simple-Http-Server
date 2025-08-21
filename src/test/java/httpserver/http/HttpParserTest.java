package httpserver.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest_HappyPath() throws IOException {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValidTestCase_GET());
        } catch (HttpParsingException e) {
            fail(e);
        }
        assertEquals(HttpMethod.GET, request.getMethod());
    }

    @Test
    void parseHttpRequest_BadPath1() throws IOException {
        try {
            HttpRequest request =  httpParser.parseHttpRequest(generateBadTestCase_InvalidGET());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequest_BadPath2() throws IOException {
        try {
            HttpRequest request =  httpParser.parseHttpRequest(generateBadTestCase_InvalidMethodName());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequest_BadPath3() throws IOException {
        try {
            HttpRequest request =  httpParser.parseHttpRequest(generateBadTestCase_InvalidNumItemsInRequestLine());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequest_BadPath4() throws IOException {
        try {
            HttpRequest request =  httpParser.parseHttpRequest(generateBadTestCase_EmptyRequestLine());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    private InputStream generateValidTestCase_GET() {
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:141.0) Gecko/20100101 Firefox/141.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Accept-Language: en-US,en;q=0.5\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    private InputStream generateBadTestCase_InvalidGET() {
        String rawData = "Get / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:141.0) Gecko/20100101 Firefox/141.0\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    private InputStream generateBadTestCase_InvalidMethodName() {
        String rawData = "GETTTTTTT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:141.0) Gecko/20100101 Firefox/141.0\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    private InputStream generateBadTestCase_InvalidNumItemsInRequestLine() {
        String rawData = "GET / INVALID_FIELD HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:141.0) Gecko/20100101 Firefox/141.0\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    private InputStream generateBadTestCase_EmptyRequestLine() {
        String rawData = "\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:141.0) Gecko/20100101 Firefox/141.0\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

}