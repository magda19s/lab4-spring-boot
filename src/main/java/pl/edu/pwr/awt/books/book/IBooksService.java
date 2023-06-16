package pl.edu.pwr.awt.books.book;

import pl.edu.pwr.awt.books.Exceptions.NotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IBooksService {
    public Collection<Book> getBooks();
    public Book getBook(int id);
    public void createBook(Book newBook);
    public void updateBook(int id, Book newBook) throws NotFoundException;
    public Book deleteBook(int id) throws NotFoundException;

    public int countBook();
}