package com.books.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.books.service.entity.Books;

@Repository
public interface BooksRepository extends JpaRepository<Books, Integer> {
}
