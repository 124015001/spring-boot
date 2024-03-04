package com.example.Library.Service;

import com.example.Library.Model.BookModel;
import com.example.Library.Model.BorrowModel;
import com.example.Library.Repository.BookRepository;
import com.example.Library.Repository.BorrowRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {

    @Autowired
    BookRepository bookRepository;

    public BookModel createBook(BookModel bookModel) {
        if (bookRepository.findByISBN(bookModel.getISBN()) != null) {
            throw new IllegalArgumentException("ISBN number already exists");
        }
        return bookRepository.save(bookModel);
    }

    public List<BookModel> createAllBooks(List<BookModel> bookModel) {

        return bookRepository.saveAll(bookModel);
    }

    public BookModel readBook(int ISBN) {
        return bookRepository.findById(ISBN).orElse(null);
    }

    public List<BookModel> readAllBooks() {
        return bookRepository.findAll();
    }

    public BookModel updateBookDetails(int ISBN, BookModel bookModel) {
        BookModel oldData = null;
        oldData = bookRepository.findById(ISBN).get();
        oldData.setAuthor(bookModel.getAuthor());
        oldData.setTitle(bookModel.getTitle());
        oldData.setGenre(bookModel.getGenre());
        return bookRepository.save(oldData);
    }

    public String deleteBookDetails(int ISBN) {
        if (bookRepository.findById(ISBN).isPresent()) {
            bookRepository.deleteById(ISBN);
            return "Data Deleted!";
        } else {
            return "Not data Found!";
        }

    }

    @Autowired
    BorrowRepository borrowRepository;

    public BorrowModel createBorrow(BorrowModel borrowModel) {
        if (bookRepository.findByISBN(borrowModel.getISBN()) == null) {
            throw new IllegalArgumentException("ISBN number doesn't exists");
        }
        return borrowRepository.save(borrowModel);
    }

    public List<BorrowModel> createAllBorrow(List<BorrowModel> borrowModel) {
        return borrowRepository.saveAll(borrowModel);
    }

    public List<BorrowModel> getBorrow1(int customerId) {
        return borrowRepository.borrowGet(customerId);
    }

    public List<BorrowModel> getAllBorrow() {
        return borrowRepository.findAll();
    }

    public List<String> popular() {
        return borrowRepository.popularBook();
    }


    BorrowModel borrowModel = new BorrowModel();

    @Transactional
    public String returnBookDate(int customerId, int ISBN) {
        BorrowModel borrowModel = borrowRepository.findByCustomerIdAndISBN(customerId, ISBN);

        if (borrowModel == null) {
            throw new EntityNotFoundException("BorrowModel not found for customerId: " + customerId + " and ISBN: " + ISBN);
        }

        if (borrowModel.getReturnDate() != null) {
            throw new IllegalStateException("Book is already returned.");
        }

        borrowRepository.returnDate(customerId, ISBN);

        borrowModel = borrowRepository.findByCustomerIdAndISBN(customerId, ISBN);

        return "Book has been returned on " + borrowModel.getReturnDate();
    }

    public String deleteBorrowDetails(int customerId, int ISBN) {
        if (borrowRepository.findById(customerId).isPresent()) {
            borrowRepository.deleteById(customerId);
            return "Borrow data Deleted!";
        } else {
            return "Borrow data Not Found!";
        }
    }



    public List<BorrowModel> overDueBook() {
        return borrowRepository.overDue();
    }

    public BookModel findByIsbn(int isbn) {
        return bookRepository.findBooksByISBN(isbn);
    }

    public List<BookModel> findByTitle(String Title) {
        return bookRepository.findBooksByTile(Title);
    }

    public List<BookModel> findByGenre(String Genre) {
        return bookRepository.findBooksByGenre(Genre);
    }

}
