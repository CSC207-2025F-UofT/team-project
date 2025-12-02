package use_case.view_pets;

import java.util.List;

import dataAccess.PetDataAccessInterface;
import entity.Pet;

public class ViewPetsInteractor2 implements ViewPetsInputBoundary {
    private final PetDataAccessInterface petDataAccess;
    private final ViewPetsOutputBoundary presenter;

    public ViewPetsInteractor2(PetDataAccessInterface petDataAccess,
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
