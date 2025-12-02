import entity.Item;
import entity.User;
import org.junit.Test;
import use_case.InventoryUseItem.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UseItemInteractorTest {

    // healing item used by user test
    @Test
    public void testUseHealingItem() {
        User user = new User();
        Item potion = new Item("Health Potion", "hppotion");
        user.addItem(potion);
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                // assuming heal default value is 0 or handled internally
                if (user.getInventory().getItems().contains(potion))
                    throw new AssertionError("Item not removed from inventory"); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {} };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        interactor.useItem(new InventoryUseItemInputData("Health Potion")); }

    // armour value test
    @Test
    public void testUseArmourItem() {
        User user = new User();
        Item shield = new Item("Iron Shield", "ring");
        user.addItem(shield);
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                if (user.getInventory().getItems().contains(shield))
                    throw new AssertionError("Item not removed from inventory"); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {} };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        interactor.useItem(new InventoryUseItemInputData("Iron Shield")); }

    // weapon type item
    @Test
    public void testUseWeaponItem() {
        User user = new User();
        Item sword = new Item("Steel Sword", "weapon");
        user.addItem(sword);
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                if (user.getInventory().getItems().contains(sword))
                    throw new AssertionError("Item not removed from inventory"); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {} };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        interactor.useItem(new InventoryUseItemInputData("Steel Sword")); }

    // no item test
    @Test
    public void testUseNonexistentItem() {
        User user = new User();
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                throw new AssertionError("Should not call useItem for nonexistent item"); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {}};
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        interactor.useItem(new InventoryUseItemInputData("Ghost Item")); }

    // no user
    @Test
    public void testUseItemUserNull() {
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                throw new AssertionError("Should not call useItem when user is null"); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {} };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.useItem(new InventoryUseItemInputData("Any Item")); }

    //test inventory
    @Test
    public void testViewInventoryEmpty() {
        User user = new User();
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                throw new AssertionError("Should not call useItem when only viewing inventory");}
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {} };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        interactor.viewInventory();}

    // test if input data is empty
    @Test
    public void testUseItemNullInput() {
        User user = new User();
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                throw new AssertionError("Should not call useItem for null input"); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {} };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        interactor.useItem(null); }

    // null type == heal
    @Test
    public void testNullTypeDefaultsToHeal() {
        User user = new User();
        Item item = new Item("Unknown Item", null);
        user.addItem(item);
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                if (user.getInventory().getItems().contains(item))
                    throw new AssertionError("Item not removed"); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {} };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        interactor.useItem(new InventoryUseItemInputData("Unknown Item")); }

    // weapon mapping test
    @Test
    public void testItemTypeWeaponVariants() {
        String[] types = {"rod", "wand", "staff"};
        for (String type : types) {
            User user = new User();
            Item item = new Item(type + " of Power", type);
            user.addItem(item);
            InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
                @Override
                public void useItem(InventoryUseItemOutputData data) {
                    assertTrue(data.getDmgIncrease() == 1 || data.getDmgIncrease() == 2);
                    assertFalse(user.getInventory().getItems().contains(item)); }
                @Override
                public void viewInventory(InventoryUseItemOutputData data) {} };
            InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
            interactor.setUser(user);
            interactor.useItem(new InventoryUseItemInputData(type + " of Power")); } }

    // test armour mapping
    @Test
    public void testArmourDefBranches() {
        int[] values = {4, 6, 8}; // value -4 → 0,2,4 → clamped to 1,2,4
        for (int val : values) {
            User user = new User();
            // simulate value indirectly through name length if needed
            String name = "Armour" + "x".repeat(val * 3);
            Item item = new Item(name, "armor");
            user.addItem(item);
            InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
                @Override
                public void useItem(InventoryUseItemOutputData data) {
                    assertTrue(data.getDefIncrease() >= 1 && data.getDefIncrease() <= 4);
                    assertFalse(user.getInventory().getItems().contains(item)); }
                @Override
                public void viewInventory(InventoryUseItemOutputData data) {} };
            InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
            interactor.setUser(user);
            interactor.useItem(new InventoryUseItemInputData(name)); } }

    // test value leng corresponds to even or odd
    @Test
    public void testWeaponDmgBranches() {
        User user = new User();
        Item evenWeapon = new Item("SwordSix", "weapon"); // length 8 → value 2 → even → dmg=2
        Item oddWeapon = new Item("Odd Axe", "weapon");    // length 7 → value 2 → odd → dmg=1
        user.addItem(evenWeapon);
        user.addItem(oddWeapon);
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                assertTrue(data.getDmgIncrease() == 1 || data.getDmgIncrease() == 2); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {} };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        // first weapon
        interactor.useItem(new InventoryUseItemInputData("SwordSix"));
        assertFalse(user.getInventory().getItems().contains(evenWeapon), "evenWeapon should be removed");
        // second weapon
        interactor.useItem(new InventoryUseItemInputData("Odd Axe"));
        assertFalse(user.getInventory().getItems().contains(oddWeapon), "oddWeapon should be removed"); }

    // tesf for viewing inventory with items
    @Test
    public void testViewInventoryWithItems() {
        User user = new User();
        Item potion = new Item("Health Potion", "hppotion");
        user.addItem(potion);
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                throw new AssertionError("useItem should not be called when viewing inventory"); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {
                assertTrue(data.getInventory().getItems().contains(potion)); } };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        interactor.viewInventory();     }

    // test for healing branch
    @Test
    public void testUnknownTypeDefaultsToHeal() {
        User user = new User();
        Item item = new Item("Mystery Box", "mystery"); // triggers DEFAULT → heal
        user.addItem(item);

        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                assertTrue(data.getHpIncrease() >= 1);  // heal branch
                assertFalse(user.getInventory().getItems().contains(item)); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {} };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        interactor.useItem(new InventoryUseItemInputData("Mystery Box")); }

    // test for early return cover missing coverage
    @Test
    public void testUseItemUserNullAndInputNull() {
        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                throw new AssertionError("Shouldn't call useItem when user AND input are null"); }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {} };
        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.useItem(null); }

    // final coverage for test case of using switch
// Full switch branch coverage for Inventory_UseItem_Interactor

    // heal branch with minimum HP
    @Test
    public void testHealBranch() {
        User user = new User();
        Item potion = new Item("A", "potion"); // name length 1 → value 1
        user.addItem(potion);

        InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
            @Override
            public void useItem(InventoryUseItemOutputData data) {
                assertTrue(data.getHpIncrease() >= 1); // Math.max(1, value)
                assertFalse(user.getInventory().getItems().contains(potion));
            }
            @Override
            public void viewInventory(InventoryUseItemOutputData data) {}
        };

        InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
        interactor.setUser(user);
        interactor.useItem(new InventoryUseItemInputData("A"));
    }

    // armour branch: def = 1, 2-3, 4
    @Test
    public void testArmourDefBranches2() {
        int[] nameLengths = {7, 10, 15}; // generates value = len/3 → 2,3,5 → value-4 = -2, -1, 1 → clamped to 1,2,4
        String[] names = {"Arm1xxxx", "Arm2xxxxxxx", "Arm3xxxxxxxxxxxx"}; // lengths match above
        for (int i = 0; i < names.length; i++) {
            User user = new User();
            Item armour = new Item(names[i], "armor");
            user.addItem(armour);

            InventoryUseItemOutputBoundary output = new InventoryUseItemOutputBoundary() {
                @Override
                public void useItem(InventoryUseItemOutputData data) {
                    assertTrue(data.getDefIncrease() >= 1 && data.getDefIncrease() <= 4);
                    assertFalse(user.getInventory().getItems().contains(armour));
                }
                @Override
                public void viewInventory(InventoryUseItemOutputData data) {}
            };

            InventoryUseItemInteractor interactor = new InventoryUseItemInteractor(output);
            interactor.setUser(user);
            interactor.useItem(new InventoryUseItemInputData(names[i]));
        }
    }







}
