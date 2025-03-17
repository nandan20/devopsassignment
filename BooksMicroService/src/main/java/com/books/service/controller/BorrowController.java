package com.books.service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.books.service.dao.BooksRepository;
import com.books.service.dao.BorrowRepository;
import com.books.service.entity.Books;
import com.books.service.entity.Borrow;
import com.books.service.entity.Users;
import com.books.service.exceptions.NotFoundException;
import com.books.service.service.BookService;
import com.books.service.service.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowRepository borrowRepository;
    
    @Autowired
    private BooksRepository booksRepository;
    
   
    
    @Autowired
    private UserService userService;


    @PostMapping
    public String borrowBook(@RequestBody Borrow borrow) {
        Users user = userService.findByUserId(borrow.getUserId());
       
        Books book = booksRepository.findById(borrow.getBookId()).orElseThrow(() -> new NotFoundException("Book with id "+ borrow.getBookId() +" does not exist."));

        if (book.getNoOfCopies() < 1) {
            return "The book \"" + book.getBookName() + "\" is out of stock!";
        }

        book.borrowBook();
        booksRepository.save(book);

        Date currentDate = new Date();
        Date overdueDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(overdueDate);
        c.add(Calendar.DATE, 7);
        overdueDate = c.getTime();
        borrow.setIssueDate(currentDate);
        borrow.setDueDate(overdueDate);
        borrowRepository.save(borrow);
        return user.getName() + " has borrowed one copy of \"" + book.getBookName() + "\"!";
    }

    @GetMapping
    public List<Borrow> getAllBorrow() {
        return borrowRepository.findAll();
    }

    
    @PutMapping
    public Borrow returnBook(@RequestBody Borrow borrow) {
        Borrow borrowBook = borrowRepository.findById(borrow.getBorrowId()).get();
        Books book = booksRepository.findById(borrow.getBookId()).orElseThrow(() -> new NotFoundException("Book with id "+ borrow.getBookId() +" does not exist."));

        book.returnBook();
        booksRepository.save(book);

        Date currentDate = new Date();
        borrowBook.setReturnDate(currentDate);
        return borrowRepository.save(borrowBook);
    }

    @GetMapping("user/{id}")
    public List<Borrow> booksBorrowedByUser(@PathVariable Integer id) {
        return borrowRepository.findByUserId(id);
    }

    @GetMapping("book/{id}")
    public List<Borrow> bookBorrowHistory(@PathVariable Integer id) {
        return borrowRepository.findByBookId(id);
    }

}
