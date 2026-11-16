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
 
**A game platform that allows players to match robots/players to play against in a Texas Hold'em poker game**

Basic Components:
_Log in/Signup/Log out_

_Matching:_
Join a User-Player with 1-5 entity(Robots) into a (6 member max) poker game with classical setting for Texas Hold'em 
poker game.
Set x amount of robot for the game depending on the Player's request each with randomized personalities

_Turn:_

    -Initiation
    Once the game is formed, card shall be "shuffled"
    Public cards(5) will be placed and covered in the middle of the poker table 
    Private cards will be distributed to the Player and each Robot

    -in the Course 
        Player: Able to take (Check/Fold/Raise) action when Player hasn't been raised by any other robots yet
        to take (Call/Fold/Reraise) action when Player has already been raised
    
        Robot: Each robot has characteristic. Robot make decision depends on the context of probability combine with their 
        assigned characteristic

    -End Game
    Player always have the option to start a new game with original initiation or new setup or quit the game once a game 
    ends.
        -Quit
        Once the Player leaves a finished game, all data will be summarised, calculated and stored to the database

_Robot:_
    -Characteristic
    **to fill**
    
    -AI support
    **to fill**

_Database:_
    -User Profile
        -Name + id
        -Photo
        -Summarisation
        User asset
        User All time/past 10 games win rate
        User All time/past 10 games biggest win and hand form
    -Strategical
        -User Preference
        fold/check/raise rate with 20%/40%/60%/80%/90% of probability to win with current hand
        fold/check/raise rate facing rerise
        Chance of doing three bet
        Amount of raise tend to bluff Player off (>50%) *May depend on Player's remaining asset*

    -Robot/AI
    **To Fill**

_API:_
    -Texas Probability Calculater
    -Robot characteristic (AI) - robot chat on different circumstance
