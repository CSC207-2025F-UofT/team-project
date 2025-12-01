package use_case.view_pets;
import data_access.PetDataAccessInterface;
import java.util.List;
import entity.Pet;

public class ViewPetsInteractor implements ViewPetsInputBoundary {
    private final PetDataAccessInterface petDataAccess;
    private final ViewPetsOutputBoundary presenter;

    public ViewPetsInteractor(PetDataAccessInterface petDataAccess,
                              ViewPetsOutputBoundary presenter) {
        this.petDataAccess = petDataAccess;
        this.presenter = presenter;
    }
    @Override
    public void execute(ViewPetsInputData inputData) {
        try {
            System.out.println("Debug: ViewPetsInteractor executing");
            List<Pet> pets = petDataAccess.getPets();
            ViewPetsOutputData outputData =
                    new ViewPetsOutputData(pets, null);
            presenter.present(outputData);
        } catch (Exception e) {
            ViewPetsOutputData outputData =
                    new ViewPetsOutputData(List.of(), "Could not load pets.");
            presenter.present(outputData);
        }
    }
}
