package use_case.create_account;
import entity.*;
/** Creates a typed account and associates it with an existing profile. */
public final class CreateAccountInteractor implements CreateAccountInputBoundary {
    private final AccountDataAccessInterface dataAccess;
    private final CreateAccountOutputBoundary presenter;
    public CreateAccountInteractor(AccountDataAccessInterface dataAccess,CreateAccountOutputBoundary presenter){this.dataAccess=dataAccess;this.presenter=presenter;}
    @Override public void execute(CreateAccountInputData input){
        try{
            if(input.getAccountName()==null||input.getAccountName().isBlank()){presenter.prepareFailView("Account name cannot be empty.");return;}
            if(input.getAccountType()==null){presenter.prepareFailView("Choose an account type.");return;}
            if(input.getInitialBalance()==null||input.getInitialBalance().signum()<0){presenter.prepareFailView("Initial balance cannot be negative.");return;}
            if(input.getCurrencyCode()==null||!input.getCurrencyCode().matches("[A-Z]{3}")){presenter.prepareFailView("Enter a valid three-letter currency code.");return;}
            UserProfile profile=dataAccess.getUserProfile(input.getUserProfileId());
            if(profile==null){presenter.prepareFailView("User profile could not be found.");return;}
            if(dataAccess.accountNameExists(profile.getId(),input.getAccountName())){presenter.prepareFailView("You already have an account with that name.");return;}
            int id=dataAccess.nextAccountId();
            Account account;
            switch(input.getAccountType()){
                case SAVINGS: account=new SavingsAccount(id,profile.getId(),input.getAccountName(),input.getInitialBalance(),input.getCurrencyCode());break;
                case CHEQUING: account=new ChequingAccount(id,profile.getId(),input.getAccountName(),input.getInitialBalance(),input.getCurrencyCode());break;
                case CASH: account=new CashAccount(id,profile.getId(),input.getAccountName(),input.getInitialBalance(),input.getCurrencyCode());break;
                default: throw new IllegalArgumentException("Unsupported account type.");
            }
            profile.addAccount(account);dataAccess.saveAccount(account,profile);
            presenter.prepareSuccessView(new CreateAccountOutputData(account.getAccountID(),account.getAccountName(),false));
        }catch(IllegalArgumentException exception){presenter.prepareFailView(exception.getMessage());}
    }
}
