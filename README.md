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


UML / project layout: 
- main gameplay loop 
- gallery 
    - new window + download 
- settings

## usecases 
- gameplayUseCase

- deleteGameUseCase
- recordGameUseCase 
- saveImageToUserUseCase
- editSettingsUseCase
- retrieveSettingsUseCase
- randomPromptUseCase(Difficulty difficulty, UserSettingsDataBase??? idk)



## entities 
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

- Difficulty
    - String difficultyName
    - String[] prompts

## apis 
- DataBase
    - GameDataBase
    - UserSettingsDataBase


- APICaller 
    - String APIToken
    + call(APIToken apiToken, ???) (returns the json / structured data from the call) 


    - HuggingFaceAPICaller
        - String APIToken

        + call()
    



## guis
- Application 
- Game
- MainMenu 
- Gallery
- PictureWindow
- Settings 

