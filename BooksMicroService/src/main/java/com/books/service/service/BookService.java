package com.books.service.service;

import com.books.service.entity.Books;

public interface BookService {
public Books findByBookId(Integer id);
public void saveBook(Books book);
}
