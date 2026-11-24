package view;
import interface_adapter.signup.SignUpController;
import interface_adapter.signup.SignUpViewModel;
import javax.swing.*;
import java.awt.*;

/** Swing registration boundary. Validation and registration rules live in the use case. */
public final class SignUpView extends JFrame {
    private final SignUpController controller;
    private final SignUpViewModel viewModel;
    private final JTextField nameField=new JTextField(20), emailField=new JTextField(20);
    private final JPasswordField passwordField=new JPasswordField(20), confirmField=new JPasswordField(20);
    private final JLabel messageLabel=new JLabel(" ");
    public SignUpView(SignUpController controller,SignUpViewModel viewModel){
        super("Finance Tracker - Sign Up"); this.controller=controller; this.viewModel=viewModel;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel=new JPanel(new GridBagLayout()); panel.setBorder(BorderFactory.createEmptyBorder(24,28,24,28));
        GridBagConstraints c=new GridBagConstraints(); c.insets=new Insets(6,6,6,6); c.fill=GridBagConstraints.HORIZONTAL;
        c.gridx=0;c.gridy=0;c.gridwidth=2;
        JLabel heading=new JLabel("Create your account",SwingConstants.CENTER); heading.setFont(heading.getFont().deriveFont(Font.BOLD,20f)); panel.add(heading,c);
        addRow(panel,c,1,"Name",nameField); addRow(panel,c,2,"Email",emailField);
        addRow(panel,c,3,"Password",passwordField); addRow(panel,c,4,"Confirm password",confirmField);
        c.gridx=0;c.gridy=5;c.gridwidth=2;panel.add(messageLabel,c);
        JButton submit=new JButton("Sign Up");submit.addActionListener(e->submit());c.gridy=6;panel.add(submit,c);
        setContentPane(panel);pack();setLocationRelativeTo(null);setResizable(false);
    }
    private void addRow(JPanel panel,GridBagConstraints c,int row,String label,JComponent field){
        c.gridy=row;c.gridx=0;c.gridwidth=1;c.weightx=0;panel.add(new JLabel(label),c);
        c.gridx=1;c.weightx=1;panel.add(field,c);
    }
    private void submit(){
        controller.signUp(nameField.getText(),emailField.getText(),new String(passwordField.getPassword()),new String(confirmField.getPassword()));
        messageLabel.setText(viewModel.getMessage());
        messageLabel.setForeground(viewModel.isSuccess()?new Color(30,125,60):new Color(170,40,40));
        if(viewModel.isSuccess()){passwordField.setText("");confirmField.setText("");}
    }
}
