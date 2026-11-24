package use_case.signup;
import entity.UserProfile;

/** Registration rules. Passwords are hashed before a profile is persisted. */
public final class SignUpInteractor implements SignUpInputBoundary {
    private final SignUpUserDataAccessInterface users;
    private final PasswordHasher passwordHasher;
    private final SignUpOutputBoundary presenter;
    public SignUpInteractor(SignUpUserDataAccessInterface users, PasswordHasher passwordHasher, SignUpOutputBoundary presenter) {
        this.users=users; this.passwordHasher=passwordHasher; this.presenter=presenter;
    }
    @Override public void execute(SignUpInputData input) {
        if (input == null || input.getName()==null || input.getName().isBlank()) { presenter.prepareFailView("Enter your name."); return; }
        if (input.getEmail()==null || !input.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) { presenter.prepareFailView("Enter a valid email address."); return; }
        if (input.getPassword()==null || input.getPassword().length()<8) { presenter.prepareFailView("Password must contain at least 8 characters."); return; }
        if (!input.getPassword().equals(input.getConfirmPassword())) { presenter.prepareFailView("Passwords do not match."); return; }
        String email=input.getEmail().trim().toLowerCase();
        try {
            if (users.emailExists(email)) { presenter.prepareFailView("An account with this email already exists."); return; }
            UserProfile profile=new UserProfile(users.nextUserId(),input.getName().trim(),email,passwordHasher.hash(input.getPassword()));
            users.saveUserProfile(profile);
            presenter.prepareSuccessView(new SignUpOutputData(profile.getId(),profile.getName(),"Registration successful. You can now log in."));
        } catch (IllegalArgumentException exception) { presenter.prepareFailView(exception.getMessage()); }
    }
}
