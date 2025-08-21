package httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionThreadWorker extends Thread{
    private final Logger LOGGER = LoggerFactory.getLogger(HttpConnectionThreadWorker.class);
    private Socket socket;

    public HttpConnectionThreadWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            String html = """
                          <html>
                            <head>
                                <title>Simple Java HTTP Server</title>
                            </head>
                            <body>
                                <h1>This page was served using my Simple HTTP Server in Java</h1>
                            </body>
                          </html>
                          """;

            final String CRLF = "\n\r";
            String response =
                    "HTTP/1.1 200 OK" +  CRLF + // Status Line: HTTP version, Response code, Response message
                            "Content-Length: " + html.getBytes().length + CRLF // Header
                            + CRLF + html + CRLF + CRLF;

            outputStream.write(response.getBytes());
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            LOGGER.info("Connection Processing Finished...");
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }

            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }

            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }

        }
    }
}
