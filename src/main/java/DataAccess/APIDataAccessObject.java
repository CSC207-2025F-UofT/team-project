package DataAccess;

import Entities.Item;
import Entities.ItemFactory;
import UseCases.Buyers.ItemPricing.ItemPricingDataAccessInterface;
import com.ebay.api.client.auth.oauth2.CredentialUtil;
import com.ebay.api.client.auth.oauth2.OAuth2Api;
import com.ebay.api.client.auth.oauth2.model.AccessToken;
import com.ebay.api.client.auth.oauth2.model.Environment;
import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** Main API Data Access Object. Handles eBay OAuth on construct */
public class APIDataAccessObject implements ItemPricingDataAccessInterface {
  private final String SCOPE_PRODUCTION = "https://api.ebay.com/oauth/api_scope";
  private final String BASE_URL_OAUTH = "https://api.ebay.com/buy/browse/v1/item_summary/search";
  private String baseUrlApi = "https://api.ebay.com";
  private static final String MARKETPLACE = "EBAY_US";
  private static final int PAGE_SIZE = 200; // Pagination constant for browse API. Max 200.
  private final ItemFactory itemFactory = new ItemFactory();

  private final String APIConfigFile;
  private Optional<AccessToken> ApplicationAccessToken;
  private OkHttpClient client = new OkHttpClient();

  /** Reads the supplied Config File containing API information and stores the API Token. */
  public APIDataAccessObject(String APIConfigFile) throws IOException {
    this.APIConfigFile = APIConfigFile;
    try {
      CredentialUtil.load(new FileInputStream(APIConfigFile));
    } catch (FileNotFoundException e) {
      // Re-throw as a RuntimeException or log a severe error and throw
      throw new IOException("eBay config file not found at: " + APIConfigFile, e);
    }

    OAuth2Api instancedAPI = new OAuth2Api();
    OAuthResponse response =
        instancedAPI.getApplicationToken(Environment.PRODUCTION, List.of(SCOPE_PRODUCTION));

    // Log the full response to see any error messages from eBay
    System.out.println("OAuth Response: " + response);

    this.ApplicationAccessToken = response.getAccessToken();
    if (this.ApplicationAccessToken.isEmpty()) {
      // If it's empty, log the error description from the response
      System.err.println(
          "Failed to get access token. Response errors: " + response.getErrorMessage());
    }
  }

  /**
   * Searches the eBay API and returns an ArrayList of Item Entities matching the search term.
   * Returns an empty list if no items are found.
   */
  @Override
  public List<Item> getItemsForSearchTerm(String searchTerm) throws IOException {
    if (searchTerm == null || searchTerm.trim().isEmpty()) {
      return Collections.emptyList();
    }
    String body = search(searchTerm.trim(), false);
    try {
      return parseItems(new JSONObject(body));
    } catch (JSONException e) {
      throw new IOException("Unexpected response format from eBay", e);
    }
  }

  // HTTP Handling for generic item search
  /** Returns the raw JSON body for the eBay Browse API lookup as a string. */
  private String search(String term, boolean isRetry) throws IOException {
    HttpUrl url =
        Objects.requireNonNull(
                HttpUrl.parse(this.baseUrlApi + "/buy/browse/v1/item_summary/search"))
            .newBuilder()
            .addQueryParameter("q", term)
            .addQueryParameter("limit", String.valueOf(PAGE_SIZE))
            .addQueryParameter("filter", "buyingOptions:{FIXED_PRICE}")
            .build();

    Request request =
        new Request.Builder()
            .url(url)
            .header("Authorization", "Bearer " + this.ApplicationAccessToken.get().getToken())
            .header("X-EBAY-C-MARKETPLACE-ID", MARKETPLACE)
            .header("Accept", "application/json")
            .build();

    try (Response response = client.newCall(request).execute()) {
      String body = response.body() == null ? "" : response.body().string();
      // Retry if the AuthToken has expired
      if (response.code() == 401 && !isRetry) {
        return search(term, true);
      }
      if (response.code() == 429) {
        throw new IOException("eBay rate limit reached");
      }
      if (!response.isSuccessful()) {
        throw new IOException("eBay search failed: HTTP " + response.code() + " " + body);
      }
      return body;
    }
  }

  /** Returns a list of Item entities, given an eBay API search JSONObject. */
  List<Item> parseItems(JSONObject root) {
    JSONArray summaries = root.optJSONArray("itemSummaries");
    List<Item> items = new ArrayList<>();
    if (summaries == null) {
      return items; // eBay omits the field when nothing matches
    }
    for (int i = 0; i < summaries.length(); i++) {
      JSONObject s = summaries.getJSONObject(i);
      JSONObject price = s.optJSONObject("price");
      if (price == null) {
        continue;
      }
      try {
        items.add(
            itemFactory.create(
                s.optString("itemId", null),
                s.optString("title", null),
                price.optString("value", null),
                price.optString("currency", null),
                s.optString("condition", null),
                firstCategoryName(s.optJSONArray("categories")),
                feedback(s.optJSONObject("seller"))));
      } catch (IllegalArgumentException e) {
        // NumberFormatException is an IllegalArgumentException: bad row, skip it
      }
    }
    return items;
  }

  /** Returns the "primary" category associated with a searched item's listing. */
  private static String firstCategoryName(JSONArray categories) {
    if (categories == null || categories.length() == 0) {
      return null;
    }
    return categories.getJSONObject(0).optString("categoryName", null);
  }

  /** Returns the feedback percentage for the seller associated with a particular item listing. */
  private static Double feedback(JSONObject seller) {
    if (seller == null) {
      return null;
    }
    try {
      return Double.valueOf(seller.getString("feedbackPercentage"));
    } catch (JSONException | NumberFormatException e) {
      return null;
    }
  }
}
