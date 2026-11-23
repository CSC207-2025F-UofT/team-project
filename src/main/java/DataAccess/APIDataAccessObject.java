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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class APIDataAccessObject implements ItemPricingDataAccessInterface{
    private static final List<String> SCOPE_LIST_PRODUCTION = Arrays.asList(new String[]{"https://api.ebay.com/oauth/api_scope"});

    private final String APIConfigFile;
    private final Optional<AccessToken> ApplicationAccessToken;

    /**
     *  Reads the supplied Config File containing API information and gets the API Token via request.
     *
     */
    public APIDataAccessObject(String APIConfigFile) throws IOException {
        this.APIConfigFile = APIConfigFile;
        try {
            CredentialUtil.load(new FileInputStream(APIConfigFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OAuth2Api InstancedAPI = new OAuth2Api();
        OAuthResponse response = InstancedAPI.getApplicationToken(Environment.PRODUCTION, SCOPE_LIST_PRODUCTION);
        this.ApplicationAccessToken =  response.getAccessToken();

        // Do actual API requests in concrete interface implementations.
    }

    public ArrayList<Item> getItemsForSearchTerm(String searchTerm){
    this.ApplicationAccessToken.get().getToken();
    }
}
