import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {

  private HttpClient client;

  public ApiClient(){
    client = HttpClient.newHttpClient();
  }


  // Gets a user's income from the backend
  public List<ApiTransaction> getIncome(String username)
      throws IOException, InterruptedException {

    String url = "http://localhost:8080/api/users/"
        + username + "/income";

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build();

    HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
    );

    JSONArray transactions = new JSONArray(response.body());

    List<ApiTransaction> incomeList = new ArrayList<>();

    for (int i = 0; i < transactions.length(); i++) {

      JSONObject transaction = transactions.getJSONObject(i);

      int id = transaction.getInt("id");
      double amount = transaction.getDouble("amount");
      String description = transaction.getString("description");
      String date = transaction.getString("date");

      ApiTransaction apiTransaction = new ApiTransaction(
          id, amount, description, date
      );

      incomeList.add(apiTransaction);
    }

    return incomeList;
  }


  // Gets a user's expenses from the backend
  public List<ApiTransaction> getExpenses(String username)
      throws IOException, InterruptedException {

    // Build the URL
    String url = "http://localhost:8080/api/users/"
        + username + "/expenses";

    // Create the GET request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build();

    // Send request and receive response
    HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
    );

    // Convert JSON response into a JSONArray
    JSONArray transactions = new JSONArray(response.body());

    // Create a list to store expenses
    List<ApiTransaction> expensesList = new ArrayList<>();

    // Loop through each expense
    for (int i = 0; i < transactions.length(); i++) {

      JSONObject transaction = transactions.getJSONObject(i);

      int id = transaction.getInt("id");
      double amount = transaction.getDouble("amount");
      String description = transaction.getString("description");
      String date = transaction.getString("date");

      // Create an ApiTransaction object
      ApiTransaction apiTransaction = new ApiTransaction(
          id, amount, description, date
      );

      // Add it to the list
      expensesList.add(apiTransaction);
    }

    return expensesList;
  }


  // Sends a new transaction to the backend
  public void addTransaction(String username, String type,
                             double amount, String description,
                             String date)
      throws IOException, InterruptedException {

    // Build the URL
    String url = "http://localhost:8080/api/users/"
        + username + "/" + type;

    // Create the JSON transaction
    JSONObject transaction = new JSONObject();

    transaction.put("amount", amount);
    transaction.put("description", description);
    transaction.put("date", date);

    // Create the POST request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(
            transaction.toString()
        ))
        .build();

    // Send the request
    HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
    );

    // Check whether the transaction was successfully added
    if (response.statusCode() != 201) {
      throw new IOException(
          "Failed to add transaction: " + response.body()
      );
    }
  }


  // Deletes a transaction through the backend
  public void deleteTransaction(String username, String type,
                                int transactionId)
      throws IOException, InterruptedException {

    // Build the URL with the transaction ID
    String url = "http://localhost:8080/api/users/"
        + username + "/" + type
        + "?id=" + transactionId;

    // Create the DELETE request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .DELETE()
        .build();

    // Send the request
    HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
    );

    // Check whether deletion was successful
    if (response.statusCode() != 200) {
      throw new IOException(
          "Failed to delete transaction: " + response.body()
      );
    }
  }



  public static void main(String[] args) throws IOException, InterruptedException {

    ApiClient api = new ApiClient();

    // Get income and expenses
    List<ApiTransaction> income = api.getIncome("Meshac");
    List<ApiTransaction> expenses = api.getExpenses("Meshac");

    // Display income
    System.out.println("INCOME:");

    for (ApiTransaction transaction : income) {
      System.out.println(
          transaction.getDescription() + " - $" +
              transaction.getAmount()
      );
    }

    // Display expenses
    System.out.println("\nEXPENSES:");

    for (ApiTransaction transaction : expenses) {
      System.out.println(
          transaction.getDescription() + " - $" +
              transaction.getAmount()
      );
    }
  }
}
