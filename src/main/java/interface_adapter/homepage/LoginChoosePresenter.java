package interface_adapter.homepage;

import interface_adapter.ViewManagerModel;
import interface_adapter.user_login.UserLoginState;
import interface_adapter.user_login.UserLoginViewModel;
import view.auth.UserLoginView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LoginChoosePresenter implements PropertyChangeListener {

    private final ViewManagerModel viewManagerModel;
    private final UserLoginViewModel userLoginViewModel;

    public LoginChoosePresenter(ViewManagerModel viewManagerModel,
    		UserLoginViewModel userLoginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.userLoginViewModel = userLoginViewModel;
    }
    

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        final String property = evt.getPropertyName();
        
        if ("login".equals(property)) {
            String userType = (String) evt.getNewValue(); 
            UserLoginState state = userLoginViewModel.getState();
            state.setUserType(userType);
            userLoginViewModel.setState(state); 
            userLoginViewModel.firePropertyChange(userType);

            String title;

            switch (userType) {
                case "user":
                    title = "User Login";
                    break;
                case "staff":
                    title = "Staff Login";
                    break;
                case "admin":
                    title = "Admin Login";
                    break;
                default:
                    title = "User Login";
                    break;
            }

            viewManagerModel.setState(userLoginViewModel.getViewName());
            viewManagerModel.setWindowTitle(title);
            viewManagerModel.firePropertyChange();
        }

        else if ("createAccount".equals(property)) {
            viewManagerModel.setState("CreateUserAccount");
            viewManagerModel.setWindowTitle("Create User Account");
            viewManagerModel.firePropertyChange();
        }

        else {
            viewManagerModel.setState("loginChoose");
            viewManagerModel.setWindowTitle("loginChoose");
            viewManagerModel.firePropertyChange();
        }
    }
}
