package xrate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Provide access to basic currency exchange rate services.
 *
 * @author hunter jubair
 */
public class ExchangeRateReader {
  String Url;
  String accessKey;

    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     *
     * @param newURL
     *            the base URL for requests
     */
    public ExchangeRateReader(String newURL) {
        // TODO Your code here
        /*
         * DON'T DO MUCH HERE!
         * People often try to do a lot here, but the action is actually in
         * the two methods below. All you need to do here is store the
         * provided `Url` in a field so it will be accessible later.
         */
         this.Url = newURL;
        try {
            this.accessKey = new BufferedReader(new FileReader("src/accessKey")).readLine();
        } catch (FileNotFoundException e) {
            System.out.println("Make sure the file exists");
            System.out.println(e);
        } catch (IOException e) {
            System.out.println("Something went wrong");
            System.out.println(e);
        }
    }

    /**
     * Get the exchange rate for the specified currency against the base
     * currency (the Euro) on the specified date.
     *
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException {
        // TODO Your code here
        URL completeURL = generateURL(year, month, day);

        InputStream inputStream = completeURL.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        JsonParser parser = new JsonParser();
        JsonObject Data = parser.parse(reader).getAsJsonObject();

        JsonElement exchangeRate = Data.getAsJsonObject("rates").get(currencyCode);
        Float parsedExchangeRate = exchangeRate.getAsFloat();

        return parsedExchangeRate;
    }

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     *
     * @param fromCurrency
     *            the currency code we're exchanging *from*
     * @param toCurrency
     *            the currency code we're exchanging *to*
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */

    public float getExchangeRate(
    String fromCurrency, String toCurrency,
    int year, int month, int day) throws IOException {

    URL completeURL = generateURL(year, month, day);

    InputStream inputStream = completeURL.openStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    JsonParser parser = new JsonParser();
    JsonObject Data = parser.parse(reader).getAsJsonObject();

    Float fromCurrencyValue = Data.getAsJsonObject("rates").get(fromCurrency).getAsFloat();
    Float toCurrencyValue = Data.getAsJsonObject("rates").get(toCurrency).getAsFloat();

    return fromCurrencyValue/toCurrencyValue;
}
}
