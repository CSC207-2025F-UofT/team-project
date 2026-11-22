package app;

/**
 * Main Component / composition root for the lecture-style Clean Architecture.
 *
 * TODO: Construct concrete Frameworks and Drivers data-access objects, inject
 * them into the Use Case Interactors, inject Presenters into the Interactors,
 * inject Input Boundaries into Controllers, and finally construct the Views.
 *
 * Keep all "new ConcreteJsonDataAccess(...)" wiring here. No Interactor should
 * instantiate its own Presenter, Controller, View, or concrete data gateway.
 */
public final class MainComponent {
    private MainComponent() {
        // Utility/composition-root class.
    }
}
