import data_access.*;
import entity.Transaction;
import interface_adapter.view_budget_progress.*;
import use_case.record_transaction.*;
import use_case.view_budget_progress.*;
import view.ViewBudgetProgressView;
import java.math.BigDecimal;
import java.time.*;
import javax.swing.*;
import java.awt.*;

public class BudgetProgressCheck {
    private static int checks;
    private static void check(boolean ok) {
        checks++;
        if (!ok) throw new AssertionError("Check " + checks + " failed");
    }
    private static JButton button(Container container, String text) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton && text.equals(((JButton)c).getText())) return (JButton)c;
            if (c instanceof Container) {
                JButton found = button((Container)c, text);
                if (found != null) return found;
            }
        }
        return null;
    }
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            InMemoryUserDataAccessObject users = new InMemoryUserDataAccessObject();
            InMemoryTransactionDataAccessObject tx = new InMemoryTransactionDataAccessObject(users);
            InMemoryBudgetDataAccessObject budgets = new InMemoryBudgetDataAccessObject();
            ViewBudgetProgressViewModel vm = new ViewBudgetProgressViewModel();
            ViewBudgetProgressView view = new ViewBudgetProgressView(vm);
            Clock clock = Clock.fixed(Instant.parse("2026-11-22T18:00:00Z"), ZoneOffset.UTC);
            LocalDate today = LocalDate.now(clock);
            ViewBudgetProgressController controller = new ViewBudgetProgressController(
                    new ViewBudgetProgressInteractor(tx, budgets, new ViewBudgetProgressPresenter(vm), clock));
            view.setController(controller);
            controller.execute();
            check(vm.getState().isError());
            users.setCurrentUsername("alice");
            controller.execute();
            check(vm.getState().getMessage().equals("Set a daily budget first."));
            budgets.saveDailyLimit("alice", new BigDecimal("100"));
            controller.execute();
            check(vm.getState().getMessage().contains("Spent today: 0.00"));
            tx.save("alice", new Transaction(new BigDecimal("25.25"), Transaction.Type.EXPENSE, today));
            tx.save("alice", new Transaction(new BigDecimal("1000"), Transaction.Type.INCOME, today));
            tx.save("alice", new Transaction(new BigDecimal("50"), Transaction.Type.EXPENSE, today.minusDays(1)));
            tx.save("alice", new Transaction(new BigDecimal("60"), Transaction.Type.EXPENSE, today.plusDays(1)));
            tx.save("bob", new Transaction(new BigDecimal("10"), Transaction.Type.EXPENSE, today));
            button(view,"Refresh").doClick();
            check(vm.getState().getMessage().contains("Spent today: 25.25"));
            check(vm.getState().getMessage().contains("Remaining: 74.75"));
            check(vm.getState().getMessage().contains("25.3%"));
            tx.save("alice", new Transaction(new BigDecimal("100"), Transaction.Type.EXPENSE, today));
            controller.execute();
            check(vm.getState().getMessage().contains("Over budget: 25.25"));
            check(vm.getState().getMessage().contains("125.3%"));
            check(vm.getState().getPercentage()==100);
            budgets.saveDailyLimit("alice", new BigDecimal("200"));
            controller.execute();
            check(vm.getState().getMessage().contains("Remaining: 74.75"));
            users.setCurrentUsername("bob");
            controller.execute();
            check(vm.getState().isError());
            check(!vm.getState().getMessage().contains("125.25"));
            budgets.saveDailyLimit("bob", new BigDecimal("10"));
            controller.execute();
            check(vm.getState().getMessage().contains("Remaining: 0.00"));
            check(vm.getState().getMessage().contains("100.0%"));
            final boolean[] back = {false};
            view.setBackAction(() -> back[0] = true);
            button(view,"Back").doClick();
            check(back[0]);
            for (BigDecimal invalid : new BigDecimal[]{null, BigDecimal.ZERO, new BigDecimal("-1"),new BigDecimal("1.234")}) {
                boolean rejected=false;
                try { budgets.saveDailyLimit("bob",invalid); }
                catch (IllegalArgumentException e) { rejected=true; }
                check(rejected);
            }
            check(budgets.getDailyLimit("bob").compareTo(new BigDecimal("10"))==0);
            users.setCurrentUsername(null);
            controller.execute();
            check(vm.getState().isError());
            check(vm.getState().getPercentage()==0);
            // Existing record use case still writes to the same store with today's date.
            users.setCurrentUsername("new-user");
            final boolean[] saved = {false};
            new RecordTransactionInteractor(tx, new RecordTransactionOutputBoundary() {
                public void prepareSuccessView() { saved[0]=true; }
                public void prepareFailView(String error) { throw new AssertionError(error); }
            }).execute(new RecordTransactionInputData("12.50", Transaction.Type.EXPENSE));
            check(saved[0]);
            check(tx.getTransactions("new-user").get(0).getDate().equals(LocalDate.now(ZoneOffset.UTC)));
        });
        System.out.println("Passed " + checks + " checks, including Swing Refresh/Back and Record Transaction compatibility.");
    }
}
