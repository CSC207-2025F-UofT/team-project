import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import interface_adapter.TransactionControl;
import interface_adapter.TransactionControl_App;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiServer {

  // Keep track of the server so we can stop it later
  private static HttpServer server;

  // Starts the API server
  public static void startServer() throws IOException {

    server = HttpServer.create(
        new InetSocketAddress(8080), 0
    );

    server.createContext("/api/users/", exchange -> {

      String method = exchange.getRequestMethod();

      // Get the requested URL
      String path = exchange.getRequestURI().getPath();

      // Split URL into sections
      String[] parts = path.split("/");

      // Expected: /api/users/username/income
      if (parts.length != 5) {
        exchange.sendResponseHeaders(400, -1);
        exchange.close();
        return;
      }

      String username = parts[3];
      String transactionType = parts[4];

      // Validate transaction type
      if (!transactionType.equals("income") &&
          !transactionType.equals("expenses")) {

        exchange.sendResponseHeaders(400, -1);
        exchange.close();
        return;
      }

      try {

        TransactionControl_App control =
            new TransactionControl_App("UserTransactions.json");

        // Check whether the user exists
        JSONObject users =
            control.getContents().getJSONObject("users");

        if (!users.has(username)) {

          String response =
              "{\"error\":\"User not found\"}";

          byte[] responseBytes =
              response.getBytes(StandardCharsets.UTF_8);

          exchange.getResponseHeaders().set(
              "Content-Type", "application/json"
          );

          exchange.sendResponseHeaders(
              404, responseBytes.length
          );

          try (OutputStream output = exchange.getResponseBody()) {
            output.write(responseBytes);
          }

          return;
        }

        // Handle GET requests
        if (method.equals("GET")) {

          JSONArray transactions;

          if (transactionType.equals("income")) {
            transactions = control.getIncome(username);
          } else {
            transactions = control.getExpenses(username);
          }

          String response = transactions.toString();

          byte[] responseBytes =
              response.getBytes(StandardCharsets.UTF_8);

          exchange.getResponseHeaders().set(
              "Content-Type", "application/json"
          );

          exchange.sendResponseHeaders(
              200, responseBytes.length
          );

          try (OutputStream output = exchange.getResponseBody()) {
            output.write(responseBytes);
          }
        }

        // Handle POST requests
        else if (method.equals("POST")) {

          // Read JSON sent by the client
          String requestBody = new String(
              exchange.getRequestBody().readAllBytes(),
              StandardCharsets.UTF_8
          );

          JSONObject transaction = new JSONObject(requestBody);

          double amount = transaction.getDouble("amount");
          String description =
              transaction.getString("description");
          String date = transaction.getString("date");

          // Save the new transaction
          control.addTransaction(
              username,
              transactionType,
              amount,
              description,
              date
          );

          String response =
              "{\"message\":\"Transaction added successfully\"}";

          byte[] responseBytes =
              response.getBytes(StandardCharsets.UTF_8);

          exchange.getResponseHeaders().set(
              "Content-Type", "application/json"
          );

          exchange.sendResponseHeaders(
              201, responseBytes.length
          );

          try (OutputStream output = exchange.getResponseBody()) {
            output.write(responseBytes);
          }
        }

        // Handle DELETE requests
        else if (method.equals("DELETE")) {

          // Get transaction ID from URL
          String query = exchange.getRequestURI().getQuery();

          if (query == null || !query.startsWith("id=")) {
            exchange.sendResponseHeaders(400, -1);
            exchange.close();
            return;
          }

          int transactionId;

          // Validate transaction ID
          try {
            transactionId = Integer.parseInt(query.substring(3));

            if (transactionId <= 0) {
              exchange.sendResponseHeaders(400, -1);
              exchange.close();
              return;
            }

          } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, -1);
            exchange.close();
            return;
          }

          // Delete the transaction
          boolean deleted = control.deleteTransaction(
              username,
              transactionType,
              transactionId
          );

          if (deleted) {

            String response =
                "{\"message\":\"Transaction deleted successfully\"}";

            byte[] responseBytes =
                response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set(
                "Content-Type", "application/json"
            );

            exchange.sendResponseHeaders(
                200, responseBytes.length
            );

            try (OutputStream output = exchange.getResponseBody()) {
              output.write(responseBytes);
            }

          } else {

            String response =
                "{\"error\":\"Transaction not found\"}";

            byte[] responseBytes =
                response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set(
                "Content-Type", "application/json"
            );

            exchange.sendResponseHeaders(
                404, responseBytes.length
            );

            try (OutputStream output = exchange.getResponseBody()) {
              output.write(responseBytes);
            }
          }
        }

        // Reject unsupported methods
        else {
          exchange.sendResponseHeaders(405, -1);
          exchange.close();
        }

      } catch (org.json.JSONException e) {

        String response =
            "{\"error\":\"Invalid transaction data or user\"}";

        byte[] responseBytes =
            response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
            "Content-Type", "application/json"
        );

        exchange.sendResponseHeaders(
            400, responseBytes.length
        );

        try (OutputStream output = exchange.getResponseBody()) {
          output.write(responseBytes);
        }

      } catch (IOException e) {

        e.printStackTrace();

        exchange.sendResponseHeaders(500, -1);
        exchange.close();
      }

    });

    server.start();

    System.out.println("API Server running on port 8080");
  }

  // Stops the API server
  public static void stopServer() {

    if (server != null) {
      server.stop(0);
      server = null;

      System.out.println("API Server stopped.");
    }
  }
}

