package httpserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
    private static final int SP = 0x20;
    private static final int CR = 0x0D;
    private static final int LF = 0x0A;

    public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException, HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();

        parseRequestLine(reader, request);
        parseHeaders(reader, request);
        parseBody(reader, request);

        return request;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        int _byte;
        StringBuilder processingDataBuffer = new StringBuilder();
        boolean methodParsed = false;
        boolean reqTargetParsed = false;

        while((_byte = reader.read()) >= 0) {
            if(_byte == CR) {
                _byte = reader.read();
                if(_byte == LF) {
                    if(!methodParsed || !reqTargetParsed) {
                        throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    LOGGER.debug("Request Line VERSION to Process: {}", processingDataBuffer);
                    return;
                }
            }

            if(_byte == SP) {
                if(!methodParsed) {
                    LOGGER.debug("Request Line METHOD to Process: {}", processingDataBuffer);
                    request.setMethod(processingDataBuffer.toString());
                    methodParsed = true;
                }
                else if(!reqTargetParsed) {
                    LOGGER.debug("Request Line REQ TARGET to Process: {}", processingDataBuffer);
                    reqTargetParsed = true;
                }
                else {
                    throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                }
                processingDataBuffer.delete(0, processingDataBuffer.length());
            }
            else {
                processingDataBuffer.append((char)_byte);
                if(!methodParsed) {
                    if(processingDataBuffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }

        }
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) {

    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {

    }





}
