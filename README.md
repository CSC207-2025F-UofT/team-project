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

## Schedule / plan 

- Nov 10-14 - PRs + something to show (one PR each member, runnable code, be able to make API calls)
    - plan is to look at each UseCase and decide how to implement each
- Nov 17-21 - one unit test per Interactor + close-to-complete prototype of program to get final feedback
- Nov 24-25 - tests achieving 100% code coverage
- Dec 1 - Presentation 



## Zac: Main menu + gameplay 
- gameplayUseCase
- recordGameUseCase 
- retrieveSettingsUseCase
- randomPromptUseCase(Difficulty difficulty, UserSettingsDataBase??? idk)

- APICaller 
    - String APIToken
    + call(APIToken apiToken, ???) (returns the json / structured data from the call) 

    - HuggingFaceAPICaller
        - String APIToken

        + call()

## Ziyi: Advanced drawing features

- GameRecord
    - String imagePath (where the image is stored in our database)
    - DateTime date (i think this type exists)
    - bool hasWon (win/lose)
    - double timeTaken
    - double timeLimit
    - Difficulty difficulty 
    - String prompt

    + getImagePath()
    + getDate()
    + getHasWon()
    + getTimeTaken()
    + getTimeLimit()
    + getDifficulty()
    + getPrompt()

## David: gallery window 
- retrieveGamesUseCase

- DataBase
    - GameDataBase
    - UserSettingsDataBase


## OG: Settings
- editSettingsUseCase

- Difficulty
    - String difficultyName
    - String[] prompts

## Laney: Saving pictures + new picture window
- deleteGameUseCase
- saveImageToUserUseCase


UML / project layout: 
- main gameplay loop 
- gallery 
    - new window + download 
- settings



## guis
- Application (implement last)
- Game (Zac + Ziyi)
- MainMenu (Zac)
- Gallery (David)
- PictureWindow (Laney)
- Settings (OG)

