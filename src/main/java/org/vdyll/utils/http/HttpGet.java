//                   ::::::::
//         :+:      :+:    :+:
//    +++++++++++  +:+         +++++
//       +:+      +#+         +#  +#
//      +#+      +#+         +#
//     #+#      #+#     +#  +#  +#
//    ###       ########+   ####+

package org.vdyll.utils.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;

/**
 * Utility class to send and process HTTP GET Requests
 *
 * @author MC_Crafty
 *
 */
public class HttpGet {

    /**
     * Fire a UTF-8 HTTP GET request and return a JSON Response as String
     * 
     * @param targetURL
     *            URL to access with GET
     * @return HTTP Response as a String
     */
    public static String executeRequestJSON(final String targetURL) {
        return executeRequest(true, targetURL, "UTF-8");
    }

    /**
     * Fire a UTF-8 HTTP GET request and return the Response as String. For JSON
     * response, use {@link HttpGet#executeRequestJSON(String)}
     * 
     * @param targetURL
     *            URL to access with GET
     * @return HTTP Response as a String
     */
    public static String executeRequest(final String targetURL) {
        return executeRequest(false, targetURL, "UTF-8");
    }

    /**
     * Fire a UTF-8 HTTP GET request and return a JSON Response as String
     * 
     * @param targetURL
     *            URL to access with GET
     * @param params
     *            URL Parameters as Key Value pairs
     * @return HTTP Response as a String
     */
    @SafeVarargs
    public static String executeRequestJSON(final String targetURL, final SimpleEntry<String, String>... params) {
        return executeRequest(true, targetURL, "UTF-8", params);
    }

    /**
     * Fire a UTF-8 HTTP GET request and return a JSON Response as String. For a
     * JSON response, use {@link #executeRequestJSON(String, SimpleEntry...)}
     * 
     * @param targetURL
     *            URL to access with GET
     * @param params
     *            URL Parameters as Key Value pairs
     * @return HTTP Response as a String
     */
    @SafeVarargs
    public static String executeRequest(final String targetURL, final SimpleEntry<String, String>... params) {
        return executeRequest(false, targetURL, "UTF-8", params);
    }

    /**
     * Fire an HTTP GET request and return the Response as String
     * 
     * @param jsonResponse
     *            Accept a JSON response
     * @param targetURL
     *            URL to access with GET
     * @param charset
     *            Character Set for encoding
     * @param params
     *            URL Parameters as Key Value pairs
     * @return HTTP Response as a String
     */
    @SafeVarargs
    public static String executeRequest(final boolean jsonResponse, final String targetURL, final String charset,
            final SimpleEntry<String, String>... params) {
        HttpURLConnection connection = null;

        try {

            final String urlParameters = Http.getEncodedParamString(charset, params);

            connection = (HttpURLConnection) new URL(urlParameters == null ? targetURL : targetURL + urlParameters)
                    .openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            if (jsonResponse) {
                connection.setRequestProperty("Accept", "application/json");
            }
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
