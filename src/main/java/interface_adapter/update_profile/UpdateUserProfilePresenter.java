package interface_adapter.update_profile;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import interface_adapter.ViewManagerModel;
import service.Frontend;
import use_case.update_profile.UpdateUserProfileOutputBoundary;

public class UpdateUserProfilePresenter implements UpdateUserProfileOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final Frontend frontend;

    public UpdateUserProfilePresenter(ViewManagerModel viewManagerModel, Frontend frontend) {
        this.viewManagerModel = viewManagerModel;
        this.frontend = frontend;
    }

	@Override
    public void showEditProfile() {
        viewManagerModel.setState("editProfile");
        viewManagerModel.setWindowTitle("Edit Profile");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void showProfileUpdateSuccess() {
        JOptionPane.showMessageDialog(
                null,
                "Profile updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
        SwingUtilities.invokeLater(() -> {
        	frontend.showMyProfile();        	
        });
    }
    
//    @Override
//    public void prepareFailView(String message) {
//        javax.swing.JOptionPane.showMessageDialog(
//                null,
//                message,
//                "Error",
//                javax.swing.JOptionPane.ERROR_MESSAGE
//        );
//    }
}
