package entity;

public class SurveyBuilder {
    SurveyInfo survey;

    public SurveyBuilder(){
        survey = new SurveyInfo();
    }

    public SurveyInfo build(){ return survey; }

    /* ADDING INFORMATION */
    public SurveyBuilder addAdopterEnergy(SurveyInfo.EnergyOfHome energy){
        survey.setAdopterEnergy(energy);
        return this;
    }

    public SurveyBuilder addAnimalStaying(SurveyInfo.AnimalStaying staying){
        survey.setAnimalStaying(staying);
        return this;
    }

    public SurveyBuilder addAnimalAlone(SurveyInfo.AloneTime aloneTime){
        survey.setAnimalAlone(aloneTime);
        return this;
    }

    public SurveyBuilder addLivesWith(boolean[] livesWith){
        survey.setAdopterLivesWith(livesWith);
        return this;
    }

    public SurveyBuilder addTimeWith(boolean[] timeWith){
        survey.setAdopterSpendsTimeWith(timeWith);
        return this;
    }

    public SurveyBuilder addEnrichment(boolean[] enrichment){
        survey.setAdopterEnrichment(enrichment);
        return this;
    }

}
