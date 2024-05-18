package bulletinBoardService;

import javax.swing.*;

public class UITasksImpl implements UITasks {
    private final JTextField textFieldMsg;
    private final JTextArea textArea;
    public UITasksImpl(JTextField textFieldMsg, JTextArea textArea) {
        this.textFieldMsg = textFieldMsg;
        this.textArea = textArea;
    }
    @Override
    public String getMessage() {
        String res = textFieldMsg.getText();
        textFieldMsg.setText("");
        return res;
    }
    @Override
    public void setText(String txt) {
        textArea.append(txt + "\n");
    }
}
