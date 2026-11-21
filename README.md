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

# frutiger aero yams OW Workouts

## Team Information
### Team Members
- Terry Zhu
- William Li
- Tian Qi
- Denise Ma

## Project Description
### Domain: Fitness and Health Technology
An app that allows a user to search for exercises and create workouts.

## User Stories
### As a new gymgoer, I want to discover new exercises by muscle group or equipment so that I can more easily target the muscle I want to train.
### As an injured gymgoer, I want to cross reference my form against a reputable database so that I can safely perform exercises without worsening my injury.
### As an athlete, I want to track my working weights over time so I can make sure I’m on the path to reaching sport-specific goals.
### As a fitness trainer, I want to easily share a customized workout plan with a client so that I can send updated weekly routines in bulk without spending time manually texting/emailing them.
### As a general user, I want to quickly compile a workout routine I saw online so that I can follow it in my gym session.

## Use Cases
### Query database for exercises
### Import / export exercise template
### Use exercise template during a workout
### Log exercises completed during a workout

## MVP

| Team Member | MVP Use Case|
|---------------------|---------------------|
| Terry | Database |
| Denise | Use exercise template during workout & save to log |
| Tianqi | Create and save template outside workout |
| William | Import/export template as JSON |

---
## Entities
### Exercise
An immutable exercise from ExerciseDB: id, name, target muscles, equipment, body parts, video URL and instructions.

### ExerciseSetGroup
One exercise within a template plus its planned sets, where each set has a target weight and rep count. Sets can be added, removed or edited. The copy constructor makes a separate copy, so an ongoing workout never changes the template it started from.

### Template
A named, reusable workout plan: an ordered list of `ExerciseSetGroup`s.

### OngoingWorkout
A workout in progress, started from a `Template` at a recorded start time. It works on copies of the template's groups and tracks whether each set is completed. Sets can be added, removed or updated mid-workout. `toLogEntry()` turns the workout into a `LogEntry` that keeps only the completed sets.

### LogEntry
An immutable record of a finished workout: the template name, the date and time (`LocalDateTime`), and a list of `LogExerciseSet`s.

### LogExerciseSet
An immutable record of the sets actually performed for one exercise: the exercise name and matching lists of weights and reps.

### FilterCriteria
Optional search filters for exercises: name (matches any part of the name), body part, target muscle and equipment. `matches(Exercise)` ignores case and skips empty criteria.

### Editor
(TBD)
---
## API Usage
**ExerciseDB**
**Documentation:** Link
**How it is used in the program:**
---
## Design Diagrams
### UML Class Diagram

@startuml



class Template
Template : String name
Template : List<ExerciseSetGroup> groups

class ExerciseSetGroup
ExerciseSetGroup : Exercise exercise
ExerciseSetGroup : int setCount
ExerciseSetGroup : List<Double> weights
ExerciseSetGroup : List<Integer> reps

class Exercise
Exercise : -String id
Exercise : -String name
Exercise : -List<String> targetMuscles
Exercise : -List<String> equipments
Exercise : -List<String> bodyParts
Exercise : -String videoUrl
Exercise : -List<String> instructions
Exercise : -Exercise(Builder builder)
Exercise : +getId() String
Exercise : +getName() String
Exercise : +getTargetMuscles() List<String>
Exercise : +getEquipments() List<String>
Exercise : +getBodyParts() List<String>
Exercise : +getVideoUrl() String
Exercise : +getInstructions() List<String>

class ExerciseBuilder
ExerciseBuilder : -String id
ExerciseBuilder : -String name
ExerciseBuilder : -List<String> targetMuscles
ExerciseBuilder : -List<String> equipments
ExerciseBuilder : -List<String> bodyParts
ExerciseBuilder : -String videoUrl
ExerciseBuilder : -List<String> instructions
ExerciseBuilder : +id(String id) Builder
ExerciseBuilder : +name(String name) Builder
ExerciseBuilder : +targetMuscles(List<String> targetMuscles) Builder
ExerciseBuilder : +equipments(List<String> equipments) Builder
ExerciseBuilder : +bodyParts(List<String> bodyParts) Builder
ExerciseBuilder : +videoUrl(String videoUrl) Builder
ExerciseBuilder : +instructions(List<String> instructions) Builder
ExerciseBuilder : +build() Exercise

Exercise +-- ExerciseBuilder
ExerciseBuilder ..> Exercise


Template --* ExerciseSetGroup
ExerciseSetGroup --> Exercise
OngoingWorkout --* ExerciseSetGroup
OngoingWorkout ..> LogEntry : creates
LogEntry --* LogExerciseSet
FilterCriteria ..> Exercise : matches

class Editor

class OngoingWorkout
OngoingWorkout : String templateName
OngoingWorkout : LocalDateTime startTime
OngoingWorkout : List<ExerciseSetGroup> groups
OngoingWorkout : List<List<Boolean>> setCompleted

class LogEntry
LogEntry : String templateName
LogEntry : LocalDateTime time
LogEntry : List<LogExerciseSet> exercises

class LogExerciseSet
LogExerciseSet : String exerciseName
LogExerciseSet : List<Double> weights
LogExerciseSet : List<Integer> reps

class FilterCriteria
FilterCriteria : String nameQuery
FilterCriteria : String bodyPart
FilterCriteria : String targetMuscle
FilterCriteria : String equipment


interface TemplateAccess
interface ExerciseAccess
interface LogAccess
interface WorkoutCacheAccess


@enduml


###. Sequence Diagrams
---
## How to Run
[Instructions for running the program.]
---
## Notes
[Any additional project information.]
