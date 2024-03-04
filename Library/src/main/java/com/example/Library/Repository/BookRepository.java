package com.example.Library.Repository;

import com.example.Library.Model.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookModel, Integer> {
    @Query("Select b.ISBN from BookModel b where b.ISBN = :ISBN")
    Integer findByISBN(int ISBN);

    @Query("Select b from BookModel b where b.ISBN = :ISBN")
    BookModel findBooksByISBN(int ISBN);
    @Query("SELECT b FROM BookModel b WHERE LOWER(b.Title) LIKE CONCAT('%', LOWER(:Title), '%')")
    List<BookModel> findBooksByTile(String Title);
    @Query("SELECT b FROM BookModel b WHERE LOWER(b.Genre) = LOWER(:Genre)")
    List<BookModel> findBooksByGenre(String Genre);
}
