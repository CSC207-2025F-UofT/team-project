package com.studyarc.use_case.viewing_research_papers;

import java.util.List;
import com.studyarc.entity.ResearchPaper;

public interface ViewingResearchPapersDataAccessInterface {
    List<ResearchPaper> getResearchPapersByUser(String userId);
}
