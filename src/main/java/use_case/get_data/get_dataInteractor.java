package use_case.get_data;

public class get_dataInteractor implements get_dataInputBoundary {
    private final get_dataDataAccessInterface dao;
    private final get_dataOutputBoundary presenter;

    public get_dataInteractor(get_dataDataAccessInterface dao,
                              get_dataOutputBoundary presenter) {
        this.dao = dao;
        this.presenter = presenter;
    }

    @Override
    public void execute(get_dataInputData inputData) {
        try {
            presenter.prepareSuccessView(
                    new get_dataOutputData(dao.fetchByUsername(inputData.getUsername()))
            );
        } catch (RuntimeException ex) {
            presenter.prepareFailView(ex.getMessage());
        }
    }
}