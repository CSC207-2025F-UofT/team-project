# CSC207: Internship Finder and Tracker
## [TUT0301] Group 40 - BA3185
### Team Members
- Emily Kim
- Jerry Zhou
- Austin Wu
- Yuheng Liu
- Lingnan Meng
---
## Project Description
### Domain
This project will be a Java Swing desktop application that helps students discover internship opportunities, 
create/record the resume used for each application, and track application progress and deadlines.
---
## User Stories
### User Story 1
As a user, I want to search for internship opportunities that match my search keywords so that I can find relevant positions and spend less time browsing company websites.

### User Story 2
As a user, I want to create or upload multiple resume versions and associate them with my saved internship 
applications so that I can keep track of which resume I submitted to each employer.

### User Story 3
As a user, I want to save internship opportunities, update their application statuses, and organize them by priority 
or deadline so that I can manage my applications in one place.

### User Story 4
As a user, I want to have a hub where I can receive a visual summary of my internship application progress, including 
the number of applications submitted over time and the number that have progressed beyond the first interview, 
so that I can evaluate my application progress.

### User Story 5
As a user, I want to create a profile with all my qualifications so that I would be recommended internships without 
having to search


---
## Use Cases
### Use Case 1
### Use Case 2
### Use Case 3
### Use Case 4: 
Owner: Yuheng Liu

Primary actor: A user looking for internship opportunities.

Supporting actor: An external job-listing service, initially the Greenhouse Job Board API.

Preconditions:
- The application is running, and the user can access the Finder feature.
- At least one supported company job board has been configured.
  Trigger:
  The user submits a keyword search.

Success outcome:
The system displays matching internship opportunities, the number of matches, and application links so that the user can review relevant positions.

Main success scenario:
1. The user enters one or more keywords and requests a search.
2. The system checks that the input contains at least one non-empty keyword.
3. The system retrieves job postings from the configured company job boards.
4. The system identifies internship postings and filters them according to the search rules below.
5. The system displays the number of matching postings and each posting’s title, company, location, and application link. Missing location information is displayed as “Not specified.”
6. The user reviews the search results.
   Extensions:
- 2a. The search input is empty or contains only whitespace.
  The system asks the user to enter at least one keyword and does not retrieve job postings. The user may correct the input and return to step 1.
- 3a. All configured job boards fail to return data because of a connection problem, timeout, or service error.
  The system explains that the search could not be completed and allows the user to retry. It does not present the failure as a successful search with zero matches.
- 3b. Some configured job boards fail to return data.
  The system continues to step 4 using the successfully retrieved postings. When displaying the results, it identifies the unavailable sources and states that the results are incomplete.
- 4a. No retrieved internship postings match the search criteria.
  The system displays a result count of zero and explains that no matching internships were found in the retrieved postings. The user may revise the keywords and return to step 1.
- 6a. The user selects an application link.
  The system opens the selected posting’s application page in the user’s browser. Opening the page does not create an application record or mark the position as applied.
  Search rules for the MVP:
- Internship identification uses explicit title indicators, such as “Intern,” “Internship,” or “Co-op.”
- Keyword matching ignores capitalization and leading or trailing whitespace.
- Every submitted keyword must appear in the posting’s title or description for the posting to match.
- Search results cover only the configured company job boards.
- A keyword match indicates relevance, not verified eligibility. Users must review the employer’s requirements before applying.
  Failure guarantee:
  An unsuccessful search does not modify saved jobs, resumes, or application records.
(Add additional use cases as needed.)
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
|Emily Kim| Use Case |
|Jerry Zhou| Use Case |
|Austin Wu|          |
|Lingnan Meng|          |
|Yuheng Liu| Use Case |
---
## Entities
- User
- Resume
- Application
- Internship
---
## API Usage
**API Name:**: Greenhouse Job Board API
**Documentation:** [https://docs.greenhouse.io/job-board.html]
**How it is used in the program:**
The Finder feature will use the Greenhouse Job Board API to retrieve
public job postings from a configured list of company job boards.

The program will send an HTTP GET request to:
`https://boards-api.greenhouse.io/v1/boards/{board_token}/jobs?content=true`

Each `board_token` identifies a company's job board. The `content=true`
parameter includes job descriptions in the JSON response. These public
GET requests do not require authentication or an API key.

The program will parse the response into JobPosting objects containing
information such as the job ID, title, location, description, and
application URL. It will then identify internship postings and apply
keyword matching to their titles and descriptions within the application.

Matching results will be displayed in the Swing interface with links
to the original application pages. Searches will cover only the
configured company job boards.
---
## Design Diagrams
### UML Class Diagram
### Sequence Diagrams
---
## How to Run
[Instructions for running the program.]
---
## Notes
[Any additional project information.]
