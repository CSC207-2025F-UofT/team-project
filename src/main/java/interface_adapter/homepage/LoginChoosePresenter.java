package interface_adapter.homepage;

import interface_adapter.ViewManagerModel;
import view.UserLoginView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LoginChoosePresenter implements PropertyChangeListener {

    private final ViewManagerModel viewManagerModel;
    private final UserLoginView loginView;

    public LoginChoosePresenter(ViewManagerModel viewManagerModel,
                                UserLoginView loginView) {
        this.viewManagerModel = viewManagerModel;
        this.loginView = loginView;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        String property = evt.getPropertyName();

        // 获取窗口
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(loginView);

        if ("login".equals(property)) {

            // ★ 接收到 userType: user/staff/admin
            String userType = (String) evt.getNewValue();

            // ★ 必须先更新 LoginView 的 userType
            loginView.setUserType(userType);

            // ★ 然后才能更新窗口标题（顺序非常重要）
            if (frame != null) {
                frame.setTitle(loginView.getTitleText());
            }

            // ★ 切换到 login 页面
            viewManagerModel.setState(loginView.getViewName());
            viewManagerModel.firePropertyChange();
        }

        else if ("createAccount".equals(property)) {
            if (frame != null) {
                frame.setTitle("Create User Account");
            }
            viewManagerModel.setState("CreateUserAccount");
            viewManagerModel.firePropertyChange();
        }

        else {
            // 返回 loginChoose
            if (frame != null) {
                frame.setTitle("loginChoose");
            }
            viewManagerModel.setState("loginChoose");
            viewManagerModel.firePropertyChange();
        }
    }
}
