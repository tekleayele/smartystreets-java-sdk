package com.smartystreets.api;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neo and Oshion on 4/27/16.
 */
public class HttpSender implements Sender {
    private final String CHARSET = "UTF-8";

    public Response send(Request request) throws IOException {

        //open the url connection
        HttpsURLConnection connection = (HttpsURLConnection) new URL(request.getUrlString()).openConnection();

        //set headers
        Map<String, String> headers = request.getHeaders();
        for (String headerName : headers.keySet()) {
            connection.setRequestProperty(headerName, headers.get(headerName));
        }

        //write the bytes of JSON payload to the output stream
        if (request.getJsonPayload() != null) {
            connection.setDoOutput(true);
            //create output stream
            OutputStream output = connection.getOutputStream();
            output.write(request.getJsonPayload().getBytes(CHARSET));
        }

        //create an input stream for the response
        InputStream input = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, CHARSET));
        String rawJSON = reader.readLine();

        //build response object from the input stream
        Response response = new Response();
        response.setStatusCode(connection.getResponseCode());
        response.setStatus(connection.getResponseMessage());
        response.setRawJSON(rawJSON);

        Map<String, List<String>> rawResponseHeaders = connection.getHeaderFields();
        Map<String, String> responseHeaders = new HashMap<>();

        for (String headerName : rawResponseHeaders.keySet()) {
            responseHeaders.put(headerName, rawResponseHeaders.get(headerName).get(0));
        }

        response.setHeaders(responseHeaders);
        return response;
    }
}



















