package pl.edu.pwr.awt.books.author;

import pl.edu.pwr.awt.books.book.Book;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import pl.edu.pwr.awt.books.Exceptions.*;

public interface IAuthorService {

    public Collection<Author> getAuthors();
    public Author getAuthor(int id);
    public void createAuthor(Author newAuthor)  throws AlreadyExistsException;
    public void updateAuthor(int id, Author newAuthor) throws NotFoundException;
    public Author deleteAuthor(int id) throws NotFoundException, PermissionDeniedException;
    public void addBook(int id, Book book);
    public void removeBook(Book book);

    public int countAuthors();


}