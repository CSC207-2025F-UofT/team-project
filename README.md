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
