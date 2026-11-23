package interface_adapter.view_pet_details;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ViewPetDetailsControllerTest {

    @Test
    void showSendsTheIdToTheUseCase() {
        final String[] received = new String[1];

        new ViewPetDetailsController(inputData -> received[0] = inputData.getPetId()).show("42");

        assertEquals("42", received[0]);
    }
}
