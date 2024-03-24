import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Human implements Serializable {
    private String firstName;
    private String lastName;
    public Human(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String toString() {
        return firstName + " " + lastName;
    }
}
class Author extends Human {
    public Author(String firstName, String lastName) {
        super(firstName, lastName);
    }
}
class Book implements Serializable {
    private String title;
    private transient List<Author> authors;
    private int publicationYear;
    private int editionNumber;
    public Book(String title, List<Author> authors, int publicationYear, int editionNumber) {
        this.title = title;
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.editionNumber = editionNumber;
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Title: ").append(title).append("\n");
        stringBuilder.append("Authors: ");
        for (Author author : authors) {
            stringBuilder.append(author.toString()).append(", ");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append("\nPublication Year: ").append(publicationYear).append("\n");
        stringBuilder.append("Edition Number: ").append(editionNumber).append("\n");
        return stringBuilder.toString();
    }
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(authors);
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        authors = (List<Author>) in.readObject();
    }
}

class BookStore implements Serializable {
    private String name;
    private List<Book> books;
    public BookStore(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }
    public void addBook(Book book) {
        books.add(book);
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BookStore Name: ").append(name).append("\n");
        stringBuilder.append("Books:\n");
        for (Book book : books) {
            stringBuilder.append(book.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}
class BookReader extends Human implements Serializable {
    private int registrationNumber;
    private transient List<Book> borrowedBooks;
    public BookReader(String firstName, String lastName, int registrationNumber) {
        super(firstName, lastName);
        this.registrationNumber = registrationNumber;
        this.borrowedBooks = new ArrayList<>();
    }
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString()).append("\n");
        stringBuilder.append("Registration Number: ").append(registrationNumber).append("\n");
        stringBuilder.append("Borrowed Books:\n");
        for (Book book : borrowedBooks) {
            stringBuilder.append(book.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(borrowedBooks);
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        borrowedBooks = (List<Book>) in.readObject();
    }
}

class Library implements Serializable {
    private String name;
    private List<BookStore> bookStores;
    private List<BookReader> readers;
    public Library(String name) {
        this.name = name;
        this.bookStores = new ArrayList<>();
        this.readers = new ArrayList<>();
    }
    public void addBookStore(BookStore bookStore) {
        bookStores.add(bookStore);
    }
    public void addReader(BookReader reader) {
        readers.add(reader);
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Library Name: ").append(name).append("\n");
        stringBuilder.append("BookStores:\n");
        for (BookStore store : bookStores) {
            stringBuilder.append(store.toString()).append("\n");
        }
        stringBuilder.append("Readers:\n");
        for (BookReader reader : readers) {
            stringBuilder.append(reader.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}
public class Main {
    public static void main(String[] args) {
        Author author1 = new Author("Fauthor1", "Lauthor1");
        Author author2 = new Author("Fauthor2", "Lauthor2");
        Book book1 = new Book("Book1", List.of(author1, author2), 2000, 1);
        Book book2 = new Book("Book2", List.of(author2), 2001, 2);
        BookStore bookStore = new BookStore("Book store");
        bookStore.addBook(book1);
        bookStore.addBook(book2);
        BookReader reader = new BookReader("Freader1", "Lreader1", 1);
        reader.borrowBook(book1);
        Library library = new Library("City Library");
        library.addBookStore(bookStore);
        library.addReader(reader);
        serializeLibrary(library, "library.ser");
        Library deserializedLibrary = deserializeLibrary("library.ser");
        System.out.println("Deserialized Library\n" + deserializedLibrary);
    }
    public static void serializeLibrary(Library library, String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(library);
            out.close();
            fileOut.close();
            System.out.println("Library serialized successfully. File saved as " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Library deserializeLibrary(String filename) {
        Library library = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            library = (Library) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Library deserialized successfully from " + filename);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return library;
    }
}