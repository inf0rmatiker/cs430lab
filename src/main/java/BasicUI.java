import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicUI extends JFrame implements ActionListener {

    public static final long serialVersionUID = 1;

    private JButton exitButton;
    private JButton searchButton;
    private JTextField isbnField;
    private JTextField titleField;
    private JTextField authorField;
    private JLabel welcomeLabel;
    private JLabel isbnLabel;
    private JLabel titleLabel;
    private JLabel authorLabel;
    private JFrame basicFrame;
    private JPanel middlePanel;
    private JPanel bottomPanel;
    private JPanel topPanel;

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

        //middlePanel.setLayout(new BorderLayout());
        //middlePanel.setBorder(new LineBorder(Color.BLACK));
        middlePanel.setLayout(new SpringLayout());
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

    /**
     * MAIN ENTRY POINT FOR LOGIC
     */
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
                createMember(memberId);
            }

            addSearchButton();
            addInputFields();

            middlePanel.updateUI();

        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    private void addWelcomeLabel() {
        middlePanel.removeAll();
        String welcomeMessage = String.format("Welcome,  %s %s", executor.getMemberFirstName(), executor.getMemberLastName());
        welcomeLabel = new JLabel(welcomeMessage);
        welcomeLabel.setFont(new Font("Verdana",1,20));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        middlePanel.add(welcomeLabel);
    }

    private void addInputFields() {
        SpringUtilities.makeCompactGrid(middlePanel, 4,2,6,6,6,6);

        addWelcomeLabel();

        isbnLabel = new JLabel("ISBN");
        titleLabel = new JLabel("Title");
        authorLabel = new JLabel("Author");

        isbnField = new JTextField(14);
        titleField = new JTextField(30);
        authorField = new JTextField(20);

        middlePanel.add(isbnLabel);
        middlePanel.add(isbnField);

        middlePanel.add(titleLabel);
        middlePanel.add(titleField);

        middlePanel.add(authorLabel);
        middlePanel.add(authorField);
    }

    private void addSearchButton() {
        searchButton = new JButton("Search");
        searchButton.setToolTipText("Search for book");
        searchButton.addActionListener(this);

        bottomPanel.add(searchButton);
        bottomPanel.updateUI();
    }

    private Integer getMemberId() {
        String input = JOptionPane.showInputDialog(basicFrame, "Enter your member ID:", "Account", JOptionPane.QUESTION_MESSAGE);
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

    private void createMember(int memberId) {
        int response = JOptionPane.showConfirmDialog(basicFrame, "Member ID does not exist. Would you like to create an account?",
                "Account", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        switch(response) {
            case JOptionPane.YES_OPTION: createMemberHelper(memberId); break;
            case JOptionPane.NO_OPTION:
            case JOptionPane.CANCEL_OPTION:
            default: return;
        }
    }

    private void createMemberHelper(int memberId) throws IllegalStateException {
        String firstName = JOptionPane.showInputDialog(basicFrame, "First name:", "Account", JOptionPane.QUESTION_MESSAGE);
        if (isValidName(firstName)) {
            String lastName = JOptionPane.showInputDialog(basicFrame, "Last name:", "Account", JOptionPane.QUESTION_MESSAGE);
            if (isValidName(lastName)) {
                String DOB = JOptionPane.showInputDialog(basicFrame, "Date of birth in MM/DD/YYYY format:", "Account", JOptionPane.QUESTION_MESSAGE);
                if (isValidDOBPattern(DOB)) {
                    String gender = JOptionPane.showInputDialog(basicFrame, "Gender in M/F format:", "Account", JOptionPane.QUESTION_MESSAGE);
                    if (isValidGender(gender)) {
                        Character genderChar = gender.toUpperCase().charAt(0);
                        Member newMember = new Member(memberId, firstName, lastName, DOB, genderChar);
                        executor.setMember(newMember);
                        if (executor.addMemberToDatabase()) {
                            System.out.println("INFO: Successfully added member " + newMember.id + " to the database.");
                            return;
                        }
                    }
                    throw new IllegalStateException("Invalid gender " + gender + " entered!");
                }
                throw new IllegalStateException("Invalid DOB " + DOB + " entered!");
            }
            throw new IllegalStateException("Invalid last name " + lastName + " entered!");
        }
        throw new IllegalStateException("Invalid first name " + firstName + " entered!");
    }

    private boolean isValidDOBPattern(String dob) {
        Pattern format = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
        Matcher matcher = format.matcher(dob);
        return matcher.matches();
    }

    private boolean isValidGender(String gender) {
        gender = gender.toUpperCase();
        Pattern format = Pattern.compile("^[MF]$");
        Matcher matcher = format.matcher(gender);
        return matcher.matches();
    }

    private boolean isValidName(String name) {
        name = name.trim();
        Pattern format = Pattern.compile("^\\w+$");
        Matcher matcher = format.matcher(name);
        return matcher.matches();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == exitButton) {
            exit();
        }
        else if (actionEvent.getSource() == searchButton) {
            System.out.println("Search button pressed");
        }

    }
}
