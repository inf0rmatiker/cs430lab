package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.File;

public class Main {

    public static List<Library> readLibraries(String filename) {
        List<Library> libraries = new ArrayList<Library>();

        File libraryFile = new File(filename);
        try {
            Scanner scan = new Scanner(libraryFile);
            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] lineSplit = line.split(",");
                    String libraryName = lineSplit[0].trim();
                    String street      = lineSplit[1].trim();
                    String city        = lineSplit[2].trim();
                    String state       = lineSplit[3].trim();

                    libraries.add(new Library(libraryName, street, city, state));
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't open " + filename);
        }

        return libraries;
    }

    public static void writeLibraries(List<Library> libraries, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT INTO library VALUES");
            for (int i = 0; i < libraries.size(); i++) {
                writer.print(libraries.get(i));
                writer.println( (i < libraries.size()-1) ? "," : "");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't write to " + filename);
        }
    }

    public static List<Author> readAuthors(String filename) {
        List<Author> authors = new ArrayList<Author>();

        File authorFile = new File(filename);
        try {
            Scanner scan = new Scanner(authorFile);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (!line.trim().isEmpty()) {
                    String[] lineSplit = line.split(",");
                    String[] fullName = lineSplit[1].trim().split(" ");
                    Integer id = Integer.parseInt(lineSplit[0].trim());

                    Author currentAuthor = new Author(id, fullName[0].trim(), fullName[1].trim());

                    // Add phones
                    for (int i = 2; i < lineSplit.length; i++) {
                        String phoneField = lineSplit[i].trim();
                        if (!phoneField.equals("None") && !phoneField.isEmpty()) {
                            String[] phoneParts = phoneField.split("\\(");
                            String phoneNumber = phoneParts[0].trim();
                            String phoneType   = phoneParts[1].trim().substring(0,1); // Cut off the )
                            currentAuthor.addPhone(new Phone(phoneNumber, phoneType));
                        }
                    }

                    authors.add(currentAuthor);
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't open " + filename);
        }

        return authors;
    }

    public static void writeAuthors(List<Author> authors, String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName);
            writer.println("INSERT INTO author VALUES");
            for (int i = 0; i < authors.size(); i++) {
                writer.print(authors.get(i)); // Write the Author toString
                writer.print((i == authors.size()-1) ? "\n" : ",\n"); // Don't write trailing comma on last author
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Publisher> readPublishers(String filename) {
        List<Publisher> publishers = new ArrayList<Publisher>();

        File publisherFile = new File(filename);
        try {
            Scanner scan = new Scanner(publisherFile);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (!line.trim().isEmpty()) {
                    String[] lineSplit = line.split(",");

                    Integer id = Integer.parseInt(lineSplit[0].trim());
                    String name = lineSplit[1].trim();

                    Publisher currentPublisher = new Publisher(id, name);

                    // Add phones
                    for (int i = 2; i < lineSplit.length; i++) {
                        String phoneField = lineSplit[i].trim();
                        if (!phoneField.equals("None") && !phoneField.isEmpty()) {
                            String[] phoneParts = phoneField.split("\\(");
                            String phoneNumber = phoneParts[0].trim();
                            String phoneType   = phoneParts[1].trim().substring(0,1); // Cut off the )
                            currentPublisher.addPhone(new Phone(phoneNumber, phoneType));
                        }
                    }


                    publishers.add(currentPublisher);
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't open " + filename);
        }

        return publishers;
    }

    public static void writePublishers(List<Publisher> publishers, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT INTO publisher VALUES");
            for (int i = 0; i < publishers.size(); i++) {
                writer.print(publishers.get(i)); // Write the Author toString
                writer.print((i == publishers.size()-1) ? "\n" : ",\n"); // Don't write trailing comma on last author
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writePhones(List<Author> authors, List<Publisher> publishers, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT IGNORE INTO phone VALUES");
            for (Author author: authors) {
                for (Phone phone: author.phones) {
                    writer.println(phone + ",");
                }
            }
            for (int i = 0; i < publishers.size(); i++) {
                Publisher publisher = publishers.get(i);
                for (int j = 0; j < publisher.phones.size(); j++) {
                    Phone phone = publisher.phones.get(j);
                    if (i == (publishers.size()-1) && j == (publisher.phones.size()-1)) { // Last phone of last publisher
                        writer.println(phone);
                    }
                    else {
                        writer.println(phone + ",");
                    }
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writeAuthorPhones(List<Author> authors, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT INTO author_phone VALUES");
            for (int i = 0; i < authors.size(); i++) {
                Author author = authors.get(i);
                for (int j = 0; j < author.phones.size(); j++) {
                    Phone phone = author.phones.get(j);
                    if (i == (authors.size()-1) && j == (author.phones.size()-1)) { // Last phone of last author
                        writer.println(String.format("(%d,'%s')", author.id, phone.number));
                    }
                    else {
                        writer.println(String.format("(%d,'%s'),", author.id, phone.number));
                    }
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writePublisherPhones(List<Publisher> publishers, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT INTO publisher_phone VALUES");
            for (int i = 0; i < publishers.size(); i++) {
                Publisher publisher = publishers.get(i);
                for (int j = 0; j < publisher.phones.size(); j++) {
                    Phone phone = publisher.phones.get(j);
                    if (i == (publishers.size()-1) && j == (publisher.phones.size()-1)) { // Last phone of last author
                        writer.println(String.format("(%d,'%s')", publisher.id, phone.number));
                    }
                    else {
                        writer.println(String.format("(%d,'%s'),", publisher.id, phone.number));
                    }
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Member> readMembers(String filename) {
        List<Member> members = new ArrayList<Member>();

        File memberFile = new File(filename);
        try {
            Scanner scan = new Scanner(memberFile);
            Member currentMember = null;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!Character.isWhitespace(line.charAt(0))) {
                    String[] lineSplit = line.split(",");
                    String[] fullName = lineSplit[1].trim().split(" ");
                    Integer id = Integer.parseInt(lineSplit[0].trim());
                    String dateOfBirth = lineSplit[3].trim();
                    Character gender = lineSplit[2].trim().charAt(0);

                    currentMember = new Member(id, fullName[0], fullName[1], dateOfBirth, gender);
                    members.add(currentMember);
                }
                else if (currentMember != null && !line.trim().isEmpty()) {
                    String[] checkoutInfo = line.trim().split(",");
                    String isbn = checkoutInfo[0].trim();
                    String checkoutDate = checkoutInfo[1].trim();
                    String checkinDate = null;
                    if (checkoutInfo.length == 3) {
                        checkinDate = checkoutInfo[2].trim();
                    }

                    currentMember.addCheckout(new Checkout(isbn, checkoutDate, checkinDate));
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't open Authors.txt");
        }

        return members;
    }

    public static void writeMembers(List<Member> members, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT INTO member VALUES");
            for (int i = 0; i < members.size(); i++) {
                writer.print(members.get(i)); // Write the Author toString
                writer.print((i == members.size()-1) ? "\n" : ",\n"); // Don't write trailing comma on last author
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void readBooks(Library library, Map<String, Book> globalBooksSet, String filename) {
        File bookFile = new File(filename);
        try {
            Scanner scan = new Scanner(bookFile);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!Character.isWhitespace(line.charAt(0))) { // Read book info
                    String[] lineSplit = line.split(",");
                    String isbn = lineSplit[0].trim();
                    Integer numberCopies = Integer.parseInt(lineSplit[1].trim());
                    Integer shelfNumber = Integer.parseInt(lineSplit[2].trim());
                    Integer floorNumber = Integer.parseInt(lineSplit[3].trim());
                    String title = lineSplit[4].trim();
                    Integer pub_id = Integer.parseInt(lineSplit[5].trim());
                    String[] datePublished = lineSplit[6].trim().split("/");
                    Integer yearPublished = Integer.parseInt(datePublished[2]);

                    Book currentBook = new Book(isbn, title, yearPublished, pub_id, numberCopies);
                    if (!globalBooksSet.containsKey(isbn)) {
                        // Read authors
                        if (scan.hasNextLine()) {
                            String[] authorIds = scan.nextLine().trim().split(",");
                            for (String author : authorIds) {
                                currentBook.authorIds.add(Integer.parseInt(author.trim()));
                            }
                        }
                        globalBooksSet.put(isbn, currentBook);
                    }
                    library.addBook(currentBook, shelfNumber, floorNumber);
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't open " + filename);
        }

    }

    public static void writeBooks(Map<String, Book> books, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT INTO book VALUES");
            for (String isbn: books.keySet()) {
                writer.println(books.get(isbn) + ",");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writeBorrowedBy(List<Member> members, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT INTO borrowed_by VALUES");
            for (int i = 0; i < members.size(); i++) {
                Member member = members.get(i);
                for (int j = 0; j < member.checkouts.size(); j++) {
                    Checkout checkout = member.checkouts.get(j);
                    if (i == (members.size()-1) && j == (member.checkouts.size()-1)) {
                        writer.println(String.format("(%d,'%s','%s',%s)", member.id, checkout.isbn, checkout.checkout_date, checkout.checkin_date));
                    }
                    else {
                        writer.println(String.format("(%d,'%s','%s',%s),", member.id, checkout.isbn, checkout.checkout_date, checkout.checkin_date));
                    }
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writeWrittenBy(Map<String, Book> books, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT INTO written_by VALUES");
            for (String isbn: books.keySet()) {
                Book book = books.get(isbn);
                for (int j = 0; j < book.authorIds.size(); j++) {
                    Integer authorId = book.authorIds.get(j);
                    writer.println(String.format("(%d,'%s'),", authorId, book.isbn));
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Write the shelves for each of the libraries
    public static void writeShelves(List<Library> libraries, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT INTO shelf VALUES");
            for (Library library: libraries) {
                Map<Integer, Shelf> shelves = library.shelves;
                for (Integer i: shelves.keySet()) {
                    Shelf s = shelves.get(i);
                    writer.println(s + ",");
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to write to file " + filename);
        }
    }

    public static void writeStoredOn(List<Library> libraries, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println("INSERT INTO stored_on VALUES");
            for (Library library: libraries) {
                Map<Integer, Shelf> shelves = library.shelves;
                for (Integer shelfNum: shelves.keySet()) {
                    Shelf s = shelves.get(shelfNum);
                    for (Book book: s.books) {
                        writer.println(String.format("('%s','%s',%d,%d),", book.isbn, library.name, s.s_num, book.num_copies));
                    }
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to write to file " + filename);
        }
    }

    public static void main(String[] args) {
        List<Library> libraries = readLibraries("Library.txt");
        writeLibraries(libraries, "LoadLibraries.dump");

        List<Author> authors = readAuthors("Author.txt");
        writeAuthors(authors, "LoadAuthors.dump");

        List<Publisher> publishers = readPublishers("Publisher.txt");
        writePublishers(publishers, "LoadPublishers.dump");

        writePhones(authors, publishers, "LoadPhones.dump");
        writeAuthorPhones(authors, "LoadAuthorPhones.dump");
        writePublisherPhones(publishers, "LoadPublisherPhones.dump");

        List<Member> members = readMembers("Member.txt");
        writeMembers(members,"LoadMembers.dump");
        writeBorrowedBy(members, "LoadBorrowedBy.dump");

        // Read books for the Main library
        Library mainLibrary      = libraries.get(0);
        Library southParkLibrary = libraries.get(1);

        Map<String, Book> globalBooksSet = new HashMap<String, Book>();
        readBooks(mainLibrary, globalBooksSet, "Book.txt");
        readBooks(southParkLibrary, globalBooksSet, "NewBook.txt");

        writeBooks(globalBooksSet, "LoadBooks.dump");
        writeWrittenBy(globalBooksSet, "LoadWrittenBy.dump");
        writeShelves(libraries, "LoadShelves.dump");
        writeStoredOn(libraries, "LoadStoredOn.dump");

    }
}
