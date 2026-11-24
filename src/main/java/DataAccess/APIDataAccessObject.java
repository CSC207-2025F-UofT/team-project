package DataAccess;

import Entities.Item;
import UseCases.Buyers.ItemPricing.ItemPricingDataAccessInterface;
import com.ebay.api.client.auth.oauth2.*;
import com.ebay.api.client.auth.oauth2.model.AccessToken;
import com.ebay.api.client.auth.oauth2.model.Environment;
import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class APIDataAccessObject implements ItemPricingDataAccessInterface{
    private final String SCOPE_PRODUCTION = "https://api.ebay.com/oauth/api_scope";
    private final String BASE_URL = "https://api.ebay.com/buy/browse/v1/item_summary/search";

    private final String APIConfigFile;
    private final Optional<AccessToken> ApplicationAccessToken;
    private final OkHttpClient client = new OkHttpClient();

    /**
     *  Reads the supplied Config File containing API information and stores the API Token.
     *
     */
    public APIDataAccessObject(String APIConfigFile) throws IOException {
        this.APIConfigFile = APIConfigFile;
        try {
            CredentialUtil.load(new FileInputStream(APIConfigFile));
        } catch (FileNotFoundException e) {
            // Re-throw as a RuntimeException or log a severe error and throw
            throw new IOException("eBay config file not found at: " + APIConfigFile, e);
        }

        OAuth2Api instancedAPI = new OAuth2Api();
        OAuthResponse response = instancedAPI.getApplicationToken(
                Environment.PRODUCTION,
                List.of(SCOPE_PRODUCTION)
        );

        // Log the full response to see any error messages from eBay
        System.out.println("OAuth Response: " + response);

        this.ApplicationAccessToken = response.getAccessToken();
        if (this.ApplicationAccessToken.isEmpty()) {
            // If it's empty, log the error description from the response
            System.err.println("Failed to get access token. Response errors: " + response.getErrors());
        }
    }

    public ArrayList<Item> getItemsForSearchTerm(String searchTerm) throws IOException {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(this.BASE_URL)).newBuilder()
                .addQueryParameter("q", searchTerm)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            //return response.body().string(); Build the actual entities here.
            //return Item();
            return null;
        }
    }
}
