package entity;

public class SurveyInfo {
    enum EnergyOfHome {QUIET, LOUD, BOTH}
    enum AnimalStaying {LOOSE, CONFINED, CRATED, OUTSIDE, OTHER}
    enum AloneTime {MORE_THAN_TEN, EIGHT_TO_TEN, FOUR_TO_SIX, LESS_THAN_FOUR}
    private EnergyOfHome adopterEnergy;
    private AnimalStaying animalStaying;
    private AloneTime animalAlone;


    enum LiveWithIndex {ADULT, CHILDREN, SENIORS, TEENAGERS, CATS, DOGS, BIRDS, MAMMALS}
    final int LIVEWITHSIZE = 8;
    private boolean[] adopterLivesWith = new boolean[LIVEWITHSIZE];

    enum TimeWithIndex {CITY, PARKS_AND_PUBLIC, HIKES, JOGGING_OR_BIKING, WATER_TIMER, CAMPING, QUIET_DAYS, OTHER}
    final int TIMEWITHSIZE = 8;
    private boolean[] adopterSpendsTimeWith = new boolean[TIMEWITHSIZE];

    enum EnrichmentIndex {TOYS, BRUSHING_OR_PETTING, OTHER_ANIMALS, PLAYTIME, OTHER}
    final int ENRICHMENTSIZE = 5;
    private boolean[] adopterEnrichment = new boolean[ENRICHMENTSIZE];


    /* public SurveyInfo(EnergyOfHome energy, AnimalStaying animals, AloneTime alone, boolean[] liveWith, boolean[] timeWith, boolean[] enrichment){
        if (liveWith.length != LIVEWITHSIZE) { throw new IllegalArgumentException("Wrong liveWith length: " + liveWith.length); }
        if (timeWith.length != TIMEWITHSIZE) { throw new IllegalArgumentException("Wrong timeWith length: " + timeWith.length); }
        if (enrichment.length != ENRICHMENTSIZE) { throw new IllegalArgumentException("Wrong enrichment length: " + enrichment.length); }

        adopterEnergy = energy;
        animalStaying = animals;
        animalAlone = alone;
        adopterLivesWith = liveWith;
        adopterSpendsTimeWith = timeWith;
        adopterEnrichment = enrichment;
    } // COMMENTED OUT DUE TO NOT BEING USED */

    public SurveyInfo() {

    }

    /* GETTERS */

    public EnergyOfHome getAdopterEnergy() {
        return adopterEnergy;
    }

    public AnimalStaying getAnimalStaying() {
        return animalStaying;
    }

    public AloneTime getAnimalAlone() {
        return animalAlone;
    }

    public boolean[] getAdopterLivesWith() {
        return adopterLivesWith;
    }

    public boolean[] getAdopterSpendsTimeWith() {
        return adopterSpendsTimeWith;
    }

    public boolean[] getAdopterEnrichment() {
        return adopterSpendsTimeWith;
    }

    /* SETTERS */

    public void setAdopterEnergy(EnergyOfHome adopterEnergy) {
        this.adopterEnergy = adopterEnergy;
    }

    public void setAnimalStaying(AnimalStaying animalStaying) {
        this.animalStaying = animalStaying;
    }

    public void setAnimalAlone(AloneTime animalAlone) {
        this.animalAlone = animalAlone;
    }

    public void setAdopterLivesWith(boolean[] adopterLivesWith) {
        if (adopterLivesWith.length != LIVEWITHSIZE) { throw new IllegalArgumentException("Wrong liveWith length: " + adopterLivesWith.length); }
        this.adopterLivesWith = adopterLivesWith;
    }

    public void setAdopterSpendsTimeWith(boolean[] adopterSpendsTimeWith) {
        if (adopterSpendsTimeWith.length != TIMEWITHSIZE) { throw new IllegalArgumentException("Wrong timeWith length: " + adopterSpendsTimeWith.length); }
        this.adopterSpendsTimeWith = adopterSpendsTimeWith;
    }

    public void setAdopterEnrichment(boolean[] adopterEnrichment) {
        if (adopterEnrichment.length != ENRICHMENTSIZE) { throw new IllegalArgumentException("Wrong enrichment length: " + adopterEnrichment.length); }
        this.adopterEnrichment = adopterEnrichment;
    }
}
