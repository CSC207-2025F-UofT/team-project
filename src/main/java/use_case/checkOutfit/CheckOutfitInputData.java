package use_case.checkOutfit;

public class CheckOutfitInputData {
    private final String outfitId;      // "HOODIE", "COAT"...
    private final String weatherMain;   // "Clear", "Rain"...
    private final String temperature;   // maybe "9.99", or "9.99°C"

    public CheckOutfitInputData(String outfitId, String weatherMain, String temperature) {
        this.outfitId = outfitId;
        this.weatherMain = weatherMain;
        this.temperature = temperature;
    }

    public String getOutfitId() { return outfitId; }
    public String getWeatherMain() { return weatherMain; }
    public String getTemperature() { return temperature; }
}
