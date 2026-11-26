package view;

import interface_adapter.pet_shop.PetShopState;
import interface_adapter.pet_shop.PetShopViewModel;
import interface_adapter.pet_shop.PetShopViewModelListener;
import interface_adapter.purchase_pet_item.PurchasePetItemController;

import javax.swing.*;

public class PetShopView extends JPanel
        implements PetShopViewModelListener {

    private final PetShopViewModel viewModel;

    private final JLabel coinLabel = new JLabel();
    private final JLabel foodQuantityLabel = new JLabel();
    private final JLabel medicineQuantityLabel = new JLabel();
    private final JLabel messageLabel = new JLabel();

    private final JButton backButton =
            new JButton("Back to Main Menu");

    private PurchasePetItemController purchaseController;

    public PetShopView(PetShopViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Pet Item Shop"));
        add(coinLabel);

        JPanel foodPanel = new JPanel();
        JButton buyFoodButton = new JButton("Buy Food");

        foodPanel.add(new JLabel("Basic Food — 3 coins"));
        foodPanel.add(foodQuantityLabel);
        foodPanel.add(buyFoodButton);
        add(foodPanel);

        JPanel medicinePanel = new JPanel();
        JButton buyMedicineButton = new JButton("Buy Medicine");

        medicinePanel.add(new JLabel("Basic Medicine — 5 coins"));
        medicinePanel.add(medicineQuantityLabel);
        medicinePanel.add(buyMedicineButton);
        add(medicinePanel);

        buyFoodButton.addActionListener(
                event -> purchaseItem("basic_food"));

        buyMedicineButton.addActionListener(
                event -> purchaseItem("basic_medicine"));

        add(messageLabel);
        add(backButton);

        stateChanged();
    }

    private void purchaseItem(String itemId) {
        purchaseController.execute(
                viewModel.getState().getUsername(),
                itemId);
    }

    public void setPurchaseController(
            PurchasePetItemController purchaseController) {
        this.purchaseController = purchaseController;
    }

    public void setBackAction(Runnable action) {
        backButton.addActionListener(event -> action.run());
    }

    @Override
    public void stateChanged() {
        PetShopState state = viewModel.getState();

        coinLabel.setText("Coins: " + state.getCoinBalance());
        foodQuantityLabel.setText(
                "Owned: " + state.getFoodQuantity());
        medicineQuantityLabel.setText(
                "Owned: " + state.getMedicineQuantity());
        messageLabel.setText(state.getMessage());
    }
}