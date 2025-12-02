package use_case.view_pets;

import entity.Pet;
import service.Backend;

import java.util.ArrayList;
import java.util.List;

public class ViewPetsInteractor implements ViewPetsInputBoundary {

    private final Backend backend;
    private final ViewPetsOutputBoundary presenter;

    public ViewPetsInteractor(Backend backend,
                              ViewPetsOutputBoundary presenter) {
        this.backend = backend;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewPetsInputData inputData) {
        try {
            List<Pet> list = new ArrayList<>(backend.pets.values());

            ViewPetsOutputData out =
                    new ViewPetsOutputData(list, null);
            presenter.present(out);
        } catch (Exception e) {
            ViewPetsOutputData out =
                    new ViewPetsOutputData(new ArrayList<>(), e.getMessage());
            presenter.present(out);
        }
    }
}
