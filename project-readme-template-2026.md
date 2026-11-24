# Project Name

## Team Information

### Team Members
- Yicheng Xia
- Haixin Wang
- Tony Shi
- Yixin Xu
- William Chen

---

## Project Description

A research paper manager that links bio-informatics paper and link papers based on genes mentioned. 

### Domain

Productivity/ Acedemic Research

---

## User Stories

## 1. Search the Paper

### User Story 1.1  Search Papers
As a bioinformatics student, I want to find research papers about genes of interest, so that I can conduct my research in an organized manner.

### User Story 1.2  Adjust Search Result Count
As a bioinformatics student, I want to change the number of papers shown on the search results page, so that I can control how many papers I review at once.

### User Story 1.3  View Paper Details
As a researcher, I want to view a paper's URL, title, authors, and abstract, so that I can determine whether the paper is relevant to my research.

## 2. Build Personal Paper Library

### User Story 2.1  Save Papers
As a researcher, I want to save links to research papers, so that I can easily access them again in the future.

### User Story 2.2  Remove Papers
As a researcher, I want to remove papers from my saved collection, so that I can keep my research library focused on the most relevant and useful literature.

### User Story 2.3  Organize Papers by Gene
As a bioinformatics student, I want to organize papers based on different genes, so that I can learn about their relationships with my genes of interest.

### User Story 2.4  View Saved Papers
As a researcher, I want to view all my saved papers, so that I can easily access and review my research collection.

### User Story 2.5  View Papers by Gene
As a researcher, I want to view my saved papers under different gene categories, so that I can find literature associated with a specific gene.

## 3. Gene Interaction Management

### User Story 3.1  Add Genes
As a bioinformatics student, I want to add genes of interest to my collection, so that I can organize and explore their biological relationships.

### User Story 3.2  Remove Genes
As a bioinformatics student, I want to remove genes from my collection, so that I can keep my gene collection relevant to my research.

### User Story 3.3  Open Genes in Genome Browser
As a bioinformatics student, I want to open genes in the UCSC Genome Browser, so that I can explore their genomic locations and related information.

### User Story 3.4  Create Gene Relationships
As a researcher, I want to create relationships between different genes, so that I can record and organize their biological interactions.

### User Story 3.5  Edit Gene Relationships
As a researcher, I want to edit existing gene relationships, so that I can update the information based on new research findings.

### User Story 3.6  Remove Gene Relationships
As a researcher, I want to remove existing gene relationships, so that I can correct inaccurate or outdated information.

### User Story 3.7  Visualize Gene Relationships
As a bioinformatics student, I want to visualize relationships between genes in a graph, so that I can better understand how different genes interact with each other.

## 4. PDF Management

### User Story 4.1  Upload PDFs
As a researcher, I want to upload research papers in PDF format, so that I can store and manage my research materials in one place.

### User Story 4.2 Associate PDFs with Papers
As a researcher, I want to associate uploaded PDFs with papers in my personal library, so that I can easily access the full text of my saved papers.

### User Story 4.3 Open PDFs
As a researcher, I want to open uploaded PDFs, so that I can read the full text of research papers.

### User Story 4.4 Delete PDFs
As a researcher, I want to delete uploaded PDFs, so that I can remove unnecessary files from my research library.

## Optional User Stories (If Time Permits)

### User Story 4.5 Extract Text from PDFs
As a researcher, I want to extract text from uploaded PDFs, so that I can easily access and use information from research papers.

### User Story 4.6 View PDFs Inside the Application
As a researcher, I want to view uploaded PDFs directly within the application, so that I can read research papers without opening an external PDF reader.

## 5. Comment the Paper

### User Story 5.1 Add Comments
As a researcher, I want to write comments under a paper's abstract, so that I can keep track of what the paper is talking about and share key information extracted from it.

### User Story 5.2 Edit Comments
As a researcher, I want to edit my previous comments, so that I can correct mistakes or update information based on new findings.

### User Story 5.3  Delete Comments
As a researcher, I want to delete my previous comments, so that I can remove outdated or irrelevant information.

### User Story 5.4  View Comments
As a researcher, I want to view comments under research papers, so that I can review previously recorded information and learn from other researchers' discussions.

## 6. Manage User Account

### User Story 6.1 Create Account
As a user, I want to create an account, so that my personal research records can be kept private and secure.

### User Story 6.2 Log In
As a user, I want to log into my account, so that I can access my saved papers, comments, and other research records.

### User Story 6.3 Log Out
As a user, I want to log out of my account, so that the next person opening the application cannot access or modify my personal research records.

---

## Use Cases
### Search the Paper
Search by keywords(PubMed API) , Adjust result count, View paper details

### Build Personal Paper Library
Save papers, Remove papers, Organize saved papers by gene categories, View Saved Papers, View Papers by Gene

### Manage Gene Interaction
Add Gene, Remove Gene, Open Gene in Genome Browser (UCSC), Create gene relationships, Edit gene relationships, Remove gene relationships, Visualize gene relationship graph

### Manage PDF 
Upload PDFs, Associate PDFs with papers, Open PDFs, Delete PDFs (If have time: Extract Text from PDF, View PDF Inside App)

### Comment the Paper
Add the common for the paper, Edit the common for the paper, Delate the common for the paper, View the common for the paper

### Manage User Account
create account, login account, logout account

(Add additional use cases as needed.)

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Name | Use Case |
| Name | Use Case |
| Name | Use Case |

---

## Entities

### Entity 1
paper: one piece of paper, in pdf or link form

### Entity 2
research topic: a piece of gene or other research subject of interest

### Entity 3
mind map: an generated graph illustrating link between research

(Add additional entities as needed.)

---

## API Usage

**API Name:**
arXiv API
**Documentation:** [Link]
https://info.arxiv.org/help/api/user-manual.html
**How it is used in the program:**
Search for academic papers.

**API Name:**
Europe PMC REST API
**Documentation:** [Link]
https://europepmc.org/restfulwebservice
**How it is used in the program:**
Search and categorize academic papers.

**API Name:**
NCBI E-utilities
**Documentation:** [Link]
https://www.ncbi.nlm.nih.gov/home/develop/api/
**How it is used in the program:**
gene normalization
---

## Design Diagrams

### UML Class Diagram


### Sequence Diagrams

---

## How to Run

[Instructions for running the program.]
Start the main program.

---

## Notes

[Any additional project information.]

--
## Data Persistence

Each paper will be stored as a JSON object, including link to the paper, file path to local copy, relevant comments, and a list of genes in that paper. The last shown graph will be cached and saved. 
