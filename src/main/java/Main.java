public class Main {

    private static void printUsageMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("SYNOPSIS:\n");
        sb.append("\tjava Main ( DUMP_OPT | CHECKOUTS_OPT | GUI_OPT )\n\n");
        sb.append("DESCRIPTION:\n");
        sb.append("\n\t1. Generating dumps\n");
        sb.append("\t\tReads all the ./data/*.txt files, and generates SQL dump files into ./dumps/\n");
        sb.append("\n\t2. Executing checkouts\n");
        sb.append("\t\tReads an xml file specified by the user, containing check-in/check-out records.\n");
        sb.append("\t\tUpdates the database accordingly.\n");
        sb.append("\n\t3. Opening a GUI for the user\n");
        sb.append("\t\tCreates a windowed application for the user to check book availability and request info.\n\n");
        sb.append("OPTIONS:\n");
        sb.append("\tDUMP_OPT\t-d, --dumps\n\n");
        sb.append("\tCHECKOUTS_OPT\t-c, --checkouts XML_FILE\n");
        sb.append("\t\t\t\tXML_FILE: A list of activities, in the form:\n" +
                "\t\t\t\t\t<activities>\n" +
                "\t\t\t\t\t  <Borrowed_by>\n" +
                "\t\t\t\t\t    <MemberID> 2011 </MemberID>\n" +
                "\t\t\t\t\t    <ISBN> 96-42103-10800 </ISBN>\n" +
                "\t\t\t\t\t    <Checkout_date> N/A </Checkout_date>\n" +
                "\t\t\t\t\t    <Checkin_date> 06/10/2016 </Checkin_date>\n" +
                "\t\t\t\t\t  </Borrowed_by> \n" +
                "\t\t\t\t\t  ... \n" +
                "\t\t\t\t\t</activities>\n\n");
        sb.append("\tGUI_OPT\t\t-g, --gui\n\n");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            if (args[0].equals("-d") || args[0].equals("--dumps")) {
                DataReader.generateDumps();
            }
            else if (args[0].equals("-g") || args[0].equals("--gui")) {
                // Launch GUI
                System.out.println("Launching GUI...");
                BasicUI gui = new BasicUI();
            }
            else {
                printUsageMessage();
                System.exit(1);
            }
        }
        else if (args.length == 2) {
            if (args[0].equals("-c") || args[0].equals("--checkouts")) {
                String libdata = args[1];
                CheckoutXmlParser parser = new CheckoutXmlParser(libdata);

                if (parser.checkouts.isEmpty()) {
                    System.out.println("No checkin/out records to process");
                }
                else {
                    StatementExecutor.executeStatements(parser.checkouts);
                    System.out.println("Program executed successfully.");
                }
            }
        }
        else {
            printUsageMessage();
            System.exit(1);
        }

    }
}
