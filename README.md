# Team Project

# Team Name: MetaGross
**Domain: Pokemon Data**
A Pokemon team management application that allows the user to look up specific Pokemon, filter by different Pokemon characteristics, as well as build a team of Pokemon that can then be graded on how good it is based on different factors.

# User Stories:
* As a user I want to look up a Pokemon so I can see all its information
* As a user I want to filter Pokemon by type so I can see the type distributions of Pokemon
* As a user I want to filter by moves, so I can find Pokemon that can learn moves that I require to beat a Gym Leader
* As a user I want to see the Pokemon in a certain Egg Group so I can pass on an Egg Move
* As a user I want to save my built teams so I can refer to them later or reuse them
* As a user I want to build teams so I can plan ahead in a playthrough
* As a user I want to grade my team so I can check how good my coverage is
* As a user I want to see the Pokedex of a specific region so I can build my team for a specific game

# Use Cases:
# Use Case 1: Look Up a Pokemon:
**Main flow**
* User types in a Pokemon name
* System looks up that Pokemon in the API

**Alternate flows**
* User types in a Pokemon that does not exist ->  Prompt the user to input a new name
* User types in a form of an existing Pokemon (like Aegislash-Shield) -> Return the Pokemon itself
# Use Case 2: Filter by Pokemon Characteristic:
**Main flow**
* User selects what kind of characteristic they’d like to filter by
(such as type, Egg Group, et cetera)
* User types in the specific characteristic (like the Fire type or Field egg group)
System looks up the characteristic in the API

**Alternate flows**
* User inputs an instance of the characteristic that does not exist (like typing a ‘Sound’ type) -> prompt that it does not exist
# Use Case 3: Build Pokemon Team:
**Main flow**
* User selects a Pokemon
* User clicks ‘Add’ button
* Once done building team, user clicks “Save” button
* User is prompted to name team

**Alternate flows**
* Adding to a full team of six -> Prompt the user to either remove a Pokemon or make a new team
* Duplicate team name -> Prompt the user to change name
# Use Case 4: Retrieve a Saved Team
**Main flow**
* User inputs team name
* System returns team

**Alternate flows**
* User inputs name that doesn’t exist -> Prompts the user to type in a different name
# Use Case 5: Grade a Saved Team
**Main flow**
* User returns their team
* User clicks grade team button
* System calculates a team aptitude score based on coverage of types, stats and so on
* System returns team aptitude score
# Use Case 6: See a Region’s Pokedex
**Main flow**
* User types in a Pokemon game
* System looks up that game’s Pokedex in the API and returns every Pokemon in that dex

**Alternate flows**
* Input is a game that does not exist -> Prompts the use to type a different game


# Proposed API for the project:
# API Name: PokeApi (https://pokeapi.co/)
**Main Service Provided**
* Contains data on every single Pokemon, moves, abilities and so on
* Can search by Pokemon, Moves, Egg Groups, etc
* Returns JSON output containing how the searched characteristic relates to the other characteristics
* Output for a Pokemon contains: Basic Info, Learnable Moves, Generation, Egg Group, etc
Status: Not yet tested

