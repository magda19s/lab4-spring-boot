package pl.edu.pwr.awt.books.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.edu.pwr.awt.books.Exceptions.NotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class BooksService implements IBooksService {
    @Autowired
    private BookRepository booksRepo;


    @Override
    public Collection<Book> getBooks() {
        return booksRepo.findAll();
    }

    @Override
    public Book getBook(int id) {
        return booksRepo.findById(id).orElse(null);
    }

    @Override
    public void createBook(Book newBook) {
        booksRepo.save(newBook);
    }

    @Override
    public void updateBook(int id,  Book newBook) throws NotFoundException {
        Book to_be_updated = booksRepo.findById(id)
                .orElse(null);

        if (to_be_updated != null) {
            to_be_updated.setPages(newBook.getPages());
            to_be_updated.setTitle(newBook.getTitle());
            to_be_updated.setPrice(newBook.getPrice());
        } else {
            throw new NotFoundException("Book not found!");
        }

        booksRepo.saveAndFlush(to_be_updated);
    }

    @Override
    public Book deleteBook(int id) throws NotFoundException {
        Book to_be_deleted = booksRepo.findById(id)
                .orElse(null);
        if (to_be_deleted != null) {
            booksRepo.deleteById(id);
            return to_be_deleted;
        } else {
            throw new NotFoundException("Book not found!");
        }
    }

    @Override
    public int countBook()
    {
        return (int) booksRepo.count();
    }

}
