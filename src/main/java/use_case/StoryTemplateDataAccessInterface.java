package use_case;

import entity.StoryTemplate;
import java.util.List;

public interface StoryTemplateDataAccessInterface {
    List<StorySummary> listTemplates();
    StoryTemplate load(String templateId);
}
