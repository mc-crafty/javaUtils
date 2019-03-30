//                   ::::::::
//         :+:      :+:    :+:
//    +++++++++++  +:+         +++++
//       +:+      +#+         +#  +#
//      +#+      +#+         +#
//     #+#      #+#     +#  +#  +#
//    ###       ########+   ####+

package org.vdyll.utils.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;

/**
 * Utility class to send and process HTTP POST Requests
 *
 * @author MC_Crafty
 *
 */
public class HttpPost {

    /**
     * Fire an HTTP POST request with JSON payload and return a JSON Response as
     * String
     *
     * @param targetURL
     *            URL to access with GET
     * @param charset
     *            Character Set for encoding
     * @param params
     *            URL Parameters as Key Value pairs
     * @return HTTP Response as a String
     */
    @SafeVarargs
    public static String executeRequestJSON(final String targetURL, final String charset,
            final SimpleEntry<String, String>... params) throws IOException {
        return executeRequest(true, targetURL, "application/json", charset, params);
    }

    /**
     * Fire an HTTP POST request with URL Form payload and return the Response
     * as String
     *
     * @param targetURL
     *            URL to access with GET
     * @param charset
     *            Character Set for encoding
     * @param params
     *            URL Parameters as Key Value pairs
     * @return HTTP Response as a String
     */
    @SafeVarargs
    public static String executeRequest(final String targetURL, final String charset,
            final SimpleEntry<String, String>... params) throws IOException {
        return executeRequest(false, targetURL, "application/x-www-form-urlencoded", charset, params);
    }

    /**
     * Fire an HTTP POST request and return the Response as String
     *
     * @param jsonResponse
     *            Accept a JSON response
     * @param targetURL
     *            URL to access with GET
     * @param contentType
     *            String for the data type of the data being POST
     * @param charset
     *            Character Set for encoding
     * @param params
     *            URL Parameters as Key Value pairs
     * @return HTTP Response as a String
     */
    @SafeVarargs
    public static String executeRequest(final boolean jsonResponse, final String targetURL, final String contentType,
            final String charset, final SimpleEntry<String, String>... params) {
        HttpURLConnection connection = null;

        try {
            final String urlParameters = Http.getEncodedParamString(charset, params);

            // Create connection
            connection = (HttpURLConnection) new URL(targetURL).openConnection();
            connection.setRequestMethod("POST");
            if (jsonResponse) {
                connection.setRequestProperty("Accept", "application/json");
            }
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", contentType + ";charset=" + charset);

            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Send request
            final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            // Get Response
            return Http.getResponse(connection, jsonResponse);

        } catch (final Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
