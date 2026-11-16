# Team Project

Please keep this up-to-date with information about your project throughout the term.

The readme should include information such as:
- a summary of what your application is all about
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses 
- screenshots or animations demonstrating current functionality

By keeping this README up-to-date,
your team will find it easier to prepare for the final presentation
at the end of the term.


Team Information 

Team Members 

    Mithuran Mathurangan 

    Lee Tak Hei 

    Runyu Tian

    EdwinYichun Deng

Project Description 

Domain 

Stock analysis platform using Apl advantage stock api. 

 

User Stories 

    Detailed analysis of 1 stock 

    Comparison of 2 stocks 

    Watch list for relevant changes to any given stock 

    Investment portfolio  

 

Use Cases 

 

    1.  

    Input a stock and get the relevant analysis as an output.  

    The user will have access to a search bar with a drop-down menu  

    The analysis comes in the form of graphs for historical data and lists for other data.  

    If the user inputs a name that isn’t in the stock list, they get an error 

 

    2.  

    Input 2 stocks and get the comparison.  

    The comparison shows the respective price history, any correlations if present, and any projected prices.  

 

    3.  

    The watchlist saves given stocks to files on the user’s account. 

    From there, the user can open the program whenever they want, and the program will pull up any recent changes to the saved stocks in their watchlist.  

    4.  

    A platform for the user to use fake money to simulate the process of buying the stock.  

    The program will increase or decrease their money based off of how their stocks of choice do in real time.   

    The user can also buy/sell their stocks when they choose 

    The user will have access to their portfolio performance  

    If extra time is available, this can be changed to a proper portfolio timeline 

 

 

Minimal Viable Product (MVP) 


Investment porfoilo
Feature	MVP behaviour
Initial balance	Give each user $10,000 in virtual cash
Buy stocks	Enter a ticker and quantity; buy at the current market price
Sell stocks	Sell shares that the user currently owns
Available cash	Display remaining virtual cash
Holdings	Show each stock, quantity, average purchase price and current price
Portfolio value	Cash + current market value of all holdings
Profit/loss	Current portfolio value − initial $10,000
Transaction history	Record each buy/sell, quantity, price and timestamp
Persistence	Save and reload the portfolio when the user logs in again

Add team member who does what user story here

 

Entities 

    - Stocks 

    - Portfolios 

    - Watchlists 

    - StockData 

    - Transcations 
    

API Usage 

Alpha Vantage 

Documentation: https://www.alphavantage.co/documentation/ 

The program uses the Api to retrieve stock data for the user stories 

### Stock API data access

This is Edwin's first milestone: retrieve daily stock prices and display them in
a console demo so the team can inspect and reuse the data. The separate second
milestone is the two-stock comparison feature: price histories, correlation,
projected prices, and its Swing interface. Those comparison features are not
implemented in this prototype.

The reusable classes are in `src/main/java/stockprototype`:

- `StockDataSource`: interface for retrieving raw daily closing prices.
- `AlphaVantageClient`: requests and parses Alpha Vantage's `TIME_SERIES_DAILY`
  response, validates data, and reports request errors.
- `StockApiDemo`: demonstrates the interface by printing prices; other features
  should call the data source rather than parse this demo's console output.

Example usage inside a method that handles or declares `IOException`:

```java
import stockprototype.AlphaVantageClient;
import stockprototype.StockDataSource;

// Inside your method:
StockDataSource source =
        new AlphaVantageClient(System.getenv("ALPHA_VANTAGE_API_KEY"));
java.util.NavigableMap<java.time.LocalDate, java.math.BigDecimal> prices =
        source.getDailyClosingPrices("IBM");
```

Each call makes one request and returns an immutable map ordered from oldest to
newest date. Prices are raw closing prices, not split/dividend-adjusted prices or
real-time quotes. The response does not establish the stock's currency. The
client requests `outputsize=compact`, which returns up to 100 recent daily
prices; full daily history requires premium access. See the
[provider's daily API documentation](https://www.alphavantage.co/documentation/#daily).

This interface is provisional until reconciled with the team's agreed domain
types. Inject the interface into use cases and construct the concrete client in
application setup. Network calls must run off Swing's event dispatch thread.
Caching, persistence, charts, and comparison calculations are not included.

#### Running the API demo

1. Open this repository's `pom.xml` as a Maven project in IntelliJ and select
   JDK 11 or later.
2. Run `stockprototype.StockApiDemo` without program arguments to read the
   explicitly synthetic `sample-daily.json`. This makes no API request.
3. For a real request, set `ALPHA_VANTAGE_API_KEY` in the local run configuration's
   environment variables and set program arguments to `--live IBM` (or another
   stock symbol). Here, `--live` means a real HTTP request, not real-time quotes.
4. The demo prints the symbol, daily price count, date range, and latest five
   closing prices. Never put the key in source code or shared run configurations.

#### Validation status

Run `mvn test` with JDK 11+ to execute nine offline tests using synthetic data and
fake HTTP responses. They cover parsing, date ordering, invalid input, provider
notices, malformed data, HTTP/network errors, credential-safe errors, and thread
interruption. No API key is needed for these tests.

On November 16, 2026, the existing IntelliJ console run of `StockApiDemo --live IBM`
showed a successful live response with 100 daily prices, covering
June 25 through November 13, 2026, and exit code 0. The API key is not included.
The integrated copy also passes the nine offline tests and the synthetic demo;
no additional live request was made during integration.

 

Design Diagrams 

Add UML Class Diagram 
<img width="5160" height="3732" alt="mermaid-diagram (2)" src="https://github.com/user-attachments/assets/a8ed3c5f-de14-4cc5-83bb-3095ca5d8842" />

Add Sequence Diagrams 

 

How to Run 

Once the program has begun, the user will be given a login screen in order to either sign up or input their login information. Once done, the user will be led to a screen with four options, one for each user story. In the first option, a user can search up a stock and 
see it's analysis. In the same window they will have an option to add the stock to their watch list. The search bar will remain on the screen if the user wishes to search a different stock.
The second button will allow the user to search up two different stocks and see their relevant comparison. The third button will allow the user to view their watch list and see the current status of the stocks on the list. The fourth button will take the user to a screen where 
they can use fake money to simulate buying stocks and view their portfolio performance. On all screens there will be a back button available for if the user wants to go back to the initial screen with the four buttons. 

Notes 

[Any additional project information.] 
Your program must demonstrate some form of data persistence.
This could be storing data through some kind of API, writing data to files, or writing data to some kind of database.
Using the API that is used by the user login system example's DBUserDataAccessObject classLinks to an external site. [updated Nov 7th with version using the correct API url for this term] to store and retrieve a "user" and an associated JSON object could satisfy both of the above requirements for API usage and data persistence. This would be appropriate for projects that rely on being able to share data across multiple users across devices, for example. 
