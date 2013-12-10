package es.udc.ws.util.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ServletUtils {

    public static final int DEFAULT_BUFFER_SIZE = 1024 * 8;
   
    public static void writeServiceResponse(HttpServletResponse response,
                                            int responseCode,
                                            Document document,
                                            Map<String, String> headers)
            throws IOException {
        writeResponse(response, responseCode, "application/xml", headers);
        
        if(document != null) {
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(document, response.getOutputStream());
        }
    }
    
    public static void writeServiceResponse(HttpServletResponse response,
                                            int responseCode,
                                            String contentType,
                                            InputStream content, 
                                            Map<String, String> headers)
            throws IOException {

        writeResponse(response, responseCode, contentType, headers);
        
        if (content != null) {
            copy(content, response.getOutputStream());
        }
    }

    private static void writeResponse(HttpServletResponse response,
                                      int responseCode,
                                      String contentType,
                                      Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                response.setHeader(key, value);
            }
        }
        response.setStatus(responseCode);
        if (contentType != null) {
            response.setContentType(contentType);
        }
    }
    
    private static int copy(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
