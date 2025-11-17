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




# NOTES ON CLEAN ARCHITECTURE
base - all dependencies should point inward 
![[Pasted image 20251117140434.png]]
isolating what the system does 

entity - GameRecord
use case - retrieveGame
interface adapter - DataBase, APICaller
- .save, .retrieve, . find, .search 

```java
interface DataBase {
	save() {}
	retrieve() {}
}
```

interface - GameDataBase, GUI, APIs,
- .save {csv.parse}
- java.swing
- HuggingFaceAPIHandler

```java
class GameDataBase implements DataBase {

}
```

- **entities** hold core business logic / rules (Ride, Passengers, Drivers), doesn't care about where data comes from or how it's stored 
Ride - status, rideId, assignDriver 
- always takes in rideIds and passengerIds, not the actual objects. 

- **use cases** orchestrate entities - RequestRide matches passenger with available driver 

RequestRide - only dependent on the interface adapter rideRepository (independent of how data is stored)
- makes a new Ride object and saves it in the repository

- **interface adapters** are translators between core logic and APIs / databases - REST controller takes an incoming hTTP request, calls usecase, then formats response back to client 

RideRepository - an **interface** that has `save`, `findById`, `update`. customizable depending on the db. 

Controller - **connects HTML requests with usecases** - gets POST request and triggers usecases 

- **interfaces** - UI, DB, APIs 

Repository - implements RideRepository

when http req comes in, 
- controller triggers usecase 
- usecase interacts with repo and returns response 

```python
TextField(label: "prompt", text: "banana")

later...

import Controller 

info = controller.get(game)
TextField(label: "prompt", text: info.prompt)
```
