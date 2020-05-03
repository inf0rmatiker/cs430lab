import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class BasicUI extends JFrame implements ActionListener {

    public static final long serialVersionUID = 1;

    private JButton exitButton;
    private JFrame basicFrame;
    private JPanel middlePanel;
    private JPanel bottomPanel;
    private JPanel topPanel;
    private JOptionPane memberIdPane;

    private Member currentMember;
    private GuiExecutor executor;

    public BasicUI() {
        setupButtons();
        setupPanels();
        setupFrame();
        System.out.println("INFO: BasicUI Initialized");

        start();
    }


    public void setupButtons() {
        exitButton = new JButton("Exit");
        exitButton.setToolTipText("Exit Program");
        exitButton.addActionListener(this);
    }

    public void setupPanels() {
        middlePanel = new JPanel();
        topPanel = new JPanel();
        bottomPanel = new JPanel();

        middlePanel.setLayout(new BorderLayout());
        bottomPanel.add(exitButton);
    }

    public void setupFrame() {
        basicFrame = new JFrame();
        basicFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        basicFrame.setTitle("CS430 Lab 5 GUI");
        basicFrame.setSize(550, 650);
        basicFrame.setLocationRelativeTo(null);

        basicFrame.add(topPanel,    BorderLayout.NORTH);
        basicFrame.add(middlePanel, BorderLayout.CENTER);
        basicFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Last thing to do, display the JFrame
        basicFrame.setVisible(true);
    }

    private void exit() {
        System.out.println("INFO: exit() called, cleaning up application");

        middlePanel.removeAll();
        this.setVisible(false);
        this.dispose();

        System.out.println("INFO: BasicUI cleaned up, exiting...");
        System.exit(0);
    }

    private void start() {
        Integer memberId = getMemberId();
        System.out.println("INFO: Member ID " + memberId + " entered.");

        try {
            executor = new GuiExecutor();
            if (executor.memberExists(memberId)) {
                System.out.println("INFO: Member " + memberId + " exists; setting member as current.");
                executor.setMember(memberId);
                System.out.println(String.format("INFO: Successfully set member to %s %s.",
                        executor.getMemberFirstName(), executor.getMemberLastName()));

            }
            else {
                System.out.println("INFO: Member " + memberId + " does not exist; prompting creation");
                createMember(executor, memberId);
            }
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    private Integer getMemberId() {
        String input = JOptionPane.showInputDialog(basicFrame, "Enter your member ID:", "Member ID", JOptionPane.QUESTION_MESSAGE);
        try {
            if (input == null) {
                System.out.println("INFO: Member ID prompt cancelled. Exiting...");
                exit();
            }
            return Integer.parseInt(input); // Base case, member entered valid number
        }
        catch (NumberFormatException e) {
            System.err.println("ERROR: Malformed member ID entered: " + ((input.trim().isEmpty()) ? "[empty]" : input) );
        }
        return getMemberId(); // Recursive call to get member ID
    }

    private void createMember(GuiExecutor executor, int memberId) {
        int response = JOptionPane.showConfirmDialog(basicFrame, "Member ID does not exist. Would you like to create an account?");
        System.out.println(response);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == exitButton) {
            exit();
        }

    }
}
