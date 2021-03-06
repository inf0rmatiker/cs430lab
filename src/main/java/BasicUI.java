import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
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
    private JPanel resultsPanel;
    private SpringLayout resultsLayout;

    private List<JButton> results;


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

        try {
            executor = new GuiExecutor();
            addWelcomeLabel();
            captureMember();
            updateWelcomeLabel();

            addSearchButton();
            addInputFields();
            addResultsPanel();

            SpringUtilities.makeCompactGrid(middlePanel, 8,1,6,6,100,40);
            middlePanel.updateUI();

        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }


    }

    private void captureMember() {
        Integer memberId = getMemberId();
        System.out.println("INFO: Member ID " + memberId + " entered.");

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
    }

    private void addWelcomeLabel() {
        middlePanel.removeAll();
        welcomeLabel = new JLabel("");
        middlePanel.add(welcomeLabel);
    }

    private void updateWelcomeLabel() {
        String welcomeMessage = String.format("Welcome, %s %s", executor.getMemberFirstName(), executor.getMemberLastName());
        welcomeLabel.setText(welcomeMessage);
        middlePanel.updateUI();
    }

    private void addInputFields() {
        isbnLabel = new JLabel("ISBN");
        titleLabel = new JLabel("Title");
        authorLabel = new JLabel("Author");

        isbnField = new JTextField(14);
        titleField = new JTextField(30);
        authorField = new JTextField(20);

        isbnField.setHorizontalAlignment(JTextField.CENTER);
        titleField.setHorizontalAlignment(JTextField.CENTER);
        authorField.setHorizontalAlignment(JTextField.CENTER);

        middlePanel.add(isbnLabel);
        middlePanel.add(isbnField);

        middlePanel.add(titleLabel);
        middlePanel.add(titleField);

        middlePanel.add(authorLabel);
        middlePanel.add(authorField);
    }

    private void addResultsPanel() {
        this.resultsLayout = new SpringLayout();
        this.results = new ArrayList<>();
        resultsPanel = new JPanel();
        resultsPanel.setLayout(resultsLayout);

        middlePanel.add(resultsPanel);
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

        if (response == JOptionPane.YES_OPTION) {
            createMemberHelper(memberId);
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
            results.clear(); // Clear previous search results
            String isbnInput = isbnField.getText();
            String titleInput = titleField.getText();
            String authorInput = authorField.getText();

            List<Book> books = executor.getBooks(isbnInput, titleInput, authorInput);

            resultsPanel.removeAll();

            if (books.isEmpty()) {
                resultsPanel.add(new JLabel("None of the libraries have that book in stock."));
            }
            else {
                resultsPanel.add(new JLabel("Search Results:"));

                for (Book b: books) {
                    JButton resultButton = new JButton(new Action() { // Just make it an anonymous class, because lazy
                        Book book;

                        boolean enabled = true;

                        @Override
                        public Object getValue(String s) {
                            if (s.equals("book")) return this.book;
                            return null;
                        }

                        @Override
                        public void putValue(String s, Object o) {
                            if (o instanceof Book) {
                                Book val = (Book) o;
                                if (s.equals("book")) this.book = val;
                            }
                        }

                        @Override
                        public void setEnabled(boolean b) {
                            this.enabled = b;
                        }

                        @Override
                        public boolean isEnabled() {
                            return enabled;
                        }

                        @Override
                        public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                            // I don't think we use this here
                        }

                        @Override
                        public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                            // I don't think we use this here
                        }

                        // ActionEvent for choosing a book.
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            List<String> results = executor.findBook(this.book);
                            String[] resultStrs  = new String[results.size()];
                            for (int i = 0; i < results.size(); i++) {
                                resultStrs[i] = results.get(i);
                            }
                            JList<String> statuses = new JList<>(resultStrs);
                            resultsPanel.removeAll();
                            resultsPanel.add(statuses);

                            SpringUtilities.makeCompactGrid(resultsPanel, 1, 1, 6, 6, 6,6);
                            resultsPanel.updateUI();

                            captureMember();
                            updateWelcomeLabel();
                        }
                    });

                    resultButton.getAction().putValue("book", b);
                    resultButton.setText(b.title);
                    results.add(resultButton);
                    resultsPanel.add(resultButton);

                }

                SpringUtilities.makeCompactGrid(resultsPanel, results.size() + 1, 1, 6, 6, 6,6);
            }

            resultsPanel.updateUI();
        }

    }
}
