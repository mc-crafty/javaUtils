//                   ::::::::
//         :+:      :+:    :+:
//    +++++++++++  +:+         +++++
//       +:+      +#+         +#  +#
//      +#+      +#+         +#
//     #+#      #+#     +#  +#  +#
//    ###       ########+   ####+

package org.vdyll.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.AbstractMap.SimpleEntry;

/**
 * Utility class to handle some boiler-plate for HTTP Requests
 *
 * @author MC_Crafty
 *
 */
public class Http {

    /**
     * ENUM of common HTTP Requests Methods
     *
     * @author MC_Crafty
     *
     */
    public static enum RequestMethod {
        GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS, TRACE;
    }

    /**
     * Reads a response from an HTTP URL Connection. This is expected to be used
     * from the Http[METHOD] classes in the same package
     * 
     * @param connection
     *            The HttpURLConnection object. This is expected to already be
     *            filled and ready.
     * @param json
     *            If we are expecting a JSON response, do not include \r\n
     * @return The HTTP response as a String
     * @throws IOException
     */
    public static String getResponse(final HttpURLConnection connection, final boolean json) throws IOException {
        final int responseCode = connection.getResponseCode();
        InputStream is = null;
        if (isSuccessfulResponseCode(responseCode)) {
            is = connection.getInputStream();
        } else {
            is = connection.getErrorStream();
        }

        final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        final StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            if (!json) {
                response.append("\r\n");
            }
        }
        rd.close();

        return response.toString();
    }

    /**
     * Takes key value pair parameters and encodes them into URL format.
     * [["Key", "Value"]["Key", "Val ue"]] -> ?Key=Value&Key=Val%20ue
     * 
     * @param charset
     *            The character set for encoding
     * @param params
     *            Key Value pairs of parameters
     * @return The URI formatted and encoded string.
     * @throws UnsupportedEncodingException
     */
    @SafeVarargs
    public static String getEncodedParamString(final String charset, final SimpleEntry<String, String>... params)
            throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();
        boolean firstParam = true;

        for (final SimpleEntry<String, String> param : params) {
            if (firstParam) {
                sb.append("?");
                firstParam = false;
            } else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(charset, param.getKey()));
            sb.append("=");
            sb.append(URLEncoder.encode(charset, param.getValue()));
        }

        return sb.toString();
    }

    /**
     * QOL to determine if the HTTP Response is safe to proceed with data
     * processing or should be treated as an error
     * 
     * @param responseCode
     *            The HTTP Response Code returned from the HTTP URL Connection
     * @return
     */
    public static boolean isSuccessfulResponseCode(final int responseCode) {
        return responseCode >= 200 && responseCode < 300;
    }
}
