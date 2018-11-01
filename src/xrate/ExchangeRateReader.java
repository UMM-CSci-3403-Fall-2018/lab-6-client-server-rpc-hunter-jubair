package xrate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Provide access to basic currency exchange rate services.
 *
 * @author Hunter Jubair
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
     * @param baseURL
     *            the base URL for requests
     */


    public ExchangeRateReader(String baseURL) throws IOException {

        // TODO Your code here
        /*
         * DON'T DO MUCH HERE!
         * People often try to do a lot here, but the action is actually in
         * the two methods below. All you need to do here is store the
         * provided `Url` in a field so it will be accessible later.
         */

        this.Url = baseURL;

        readAccessKeys();

    }


    private void readAccessKeys() throws IOException {

        Properties properties = new Properties();
        FileInputStream input = null;

        try {

            input = new FileInputStream("etc/access_keys.properties");

        }

        catch (FileNotFoundException exception) {

            System.err.println("Couldn't open etc/access_keys.properties; have you renamed the sample file?");

            throw(exception);

        }

        properties.load(input);

        accessKey = properties.getProperty("fixer_io");

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


    // The getRate method

    public float getRate(JsonObject ratesInfo, String currency){
        return ratesInfo.getAsJsonObject("rates").get(currency).getAsFloat();
    }


    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException {
        // TODO Your code here

        String modMonth = addZero(month);
        String modDay = addZero(day);

        String newURL = Url + year + "-" + modMonth + "-" + modDay + "?access_key=" + accessKey;

        URL finalURL = new URL(newURL);

        InputStream inputStream = finalURL.openStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        JsonParser parser = new JsonParser();

        JsonObject exchangeRate = parser.parse(reader).getAsJsonObject();

        float parsedExchangeRate = getRate(exchangeRate, currencyCode);


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


    // Write code to change single digit dates to 2 digit dates

    public String addZero(int dateOrMonth) {

        if (dateOrMonth < 10) {

            return "0" + String.valueOf(dateOrMonth);

        }

        else {

            return String.valueOf(dateOrMonth);

        }

    }


    public float getExchangeRate(
    String fromCurrency, String toCurrency,
    int year, int month, int day) throws IOException {

    URL finalURL = createURL(year, month, day);

    InputStream inputStream = finalURL.openStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    JsonParser parser = new JsonParser();
    JsonObject Data = parser.parse(reader).getAsJsonObject();

    Float fromCurrencyValue = Data.getAsJsonObject("rates").get(fromCurrency).getAsFloat();
    Float toCurrencyValue = Data.getAsJsonObject("rates").get(toCurrency).getAsFloat();

    return fromCurrencyValue/toCurrencyValue;
}
}
