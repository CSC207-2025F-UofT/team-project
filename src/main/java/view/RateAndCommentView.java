package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.clicking.ClickingViewModel;
import interface_adapter.rate_and_comment.CommentController;
import interface_adapter.rate_and_comment.CommentState;
import interface_adapter.rate_and_comment.CommentViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RateAndCommentView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "comment";
    private final CommentViewModel commentViewModel;
    private final ClickingViewModel clickingViewModel;
    private final ViewManagerModel viewManagerModel;

    //TODO 设置一个方法让从上一UI跳转过来时输入username和medianame

    private String medianame;
    private int rating = 0; // 当前评分
    private JLabel[] stars = new JLabel[5];
    private JTextArea reviewArea;
    private JButton submitButton;
    private JButton returnButton;

    private CommentController commentController = null;

    public RateAndCommentView(ViewManagerModel viewManagerModel, CommentViewModel commentViewModel,
                              ClickingViewModel clickingViewModel) {
        this.clickingViewModel = clickingViewModel;
        this.viewManagerModel = viewManagerModel;
        this.commentViewModel = commentViewModel;
        this.commentViewModel.addPropertyChangeListener(this);
        //TODO 注意这两个加的位置
        //this.commentViewModel.getState().setMedianame(medianame);


        setLayout(new BorderLayout(10, 10));
        setBackground(Color.LIGHT_GRAY);

        // ====== 上部：星级评分 ======
        JPanel ratingPanel = new JPanel();
        ratingPanel.setBackground(Color.LIGHT_GRAY);
        ratingPanel.add(new JLabel("Rating: "));

        for (int i = 0; i < 5; i++) {
            JLabel star = new JLabel("★");
            star.setFont(new Font("SansSerif", Font.PLAIN, 30));
            star.setForeground(Color.GRAY);
            final int index = i;
            star.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    setRating(index + 1);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    highlightStars(index + 1);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    highlightStars(rating);
                }
            });
            stars[i] = star;
            ratingPanel.add(star);
        }

        add(ratingPanel, BorderLayout.NORTH);

        //TODO 加一个listener改变viewmodel的rate
        //已在下方更改

        // ====== 中部：文本框 ======
        //TODO 改成文本框初始状态empty
        reviewArea = new JTextArea("", 5, 20);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);
        reviewArea.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

        add(reviewArea, BorderLayout.CENTER);

        reviewArea.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final CommentState currentState = commentViewModel.getState();
                currentState.setComment(reviewArea.getText());
                commentViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        // ====== 下部：提交按钮 ======
        submitButton = new JButton("Submit");
        submitButton.setBackground(Color.DARK_GRAY);
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(submitButton)) {
                        if (reviewArea.getText().equals("")) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "You haven't inserted any comment!",
                                    "Failed",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            //final CommentState currentState = commentViewModel.getState();

                            commentController.execute(medianame, reviewArea.getText(), rating);// 写完controller改写此处execute
                            //清除评论和打分
                            //setRating(0);
                            //reviewArea.setText("");
                        }
                    }
                }
        );

        // ✅ Return按钮（放在左下角）
        returnButton = new JButton("Return");
        returnButton.setBackground(new Color(70, 130, 180));
        returnButton.setForeground(Color.WHITE);
        returnButton.setPreferredSize(new Dimension(100, 40));

        returnButton.addActionListener(e -> {
            // 清空评分
            setRating(0);
            setMedianame("");
            // 清空评论框
            reviewArea.setText("");
            //清空viewmodel
            commentViewModel.setState(new CommentState());

            // TODO: 跳转到其他UI（此处先弹提示）
            viewManagerModel.setState(clickingViewModel.getViewName());
            viewManagerModel.firePropertyChange();
            JOptionPane.showMessageDialog(
                    this,
                    "Return clicked — clearing input and switching to another UI.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        // 左右分别放 Return 和 Submit
        bottomPanel.add(returnButton, BorderLayout.WEST);
        bottomPanel.add(submitButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setRating(int newRating) {
        rating = newRating;
        highlightStars(newRating);
        // ✅ 更新 ViewModel 中的评分
        CommentState currentState = commentViewModel.getState();
        currentState.setRate(newRating);
        commentViewModel.setState(currentState);
    }

    private void highlightStars(int count) {
        for (int i = 0; i < 5; i++) {
            if (i < count) {
                stars[i].setForeground(Color.GREEN);
            } else {
                stars[i].setForeground(Color.GRAY);
            }
        }
    }


    private void setMedianame(String mn) {
        medianame = mn;
    }

    /**
     * React to a button click that results in evt.
     *
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        CommentState state = (CommentState) evt.getNewValue();
        reviewArea.setText(state.getComment());
        setRating(state.getRate());
        setMedianame(state.getMedianame());
    }

    public String getViewName() {
        return viewName;
    }

    public void setCommentController(CommentController commentController) {
        this.commentController = commentController;
    }
}
