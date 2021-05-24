package com.sda.jdbc;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieDAOImplTest {

    private static MovieDAO movieDAO; // static ponieważ...
    private static Connection connection;

    @BeforeAll
    static void setup() throws SQLException { // static ponieważ muszą się wykonać na samym początku, jeszcze przed inicjalizacją naszych testów w
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_example", "Kamil", "11111111");
        movieDAO = MovieDAOImpl.getInstance(connection);
    }

    @BeforeEach
    void clearTableBeforeUse(){
        movieDAO.deleteTable();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void createTable() {
        //Given

        //When
        movieDAO.createTable();
        //Then
    }

    @Test
    @Disabled
        // ignoruje nam test
    void shouldThrowSqlExceptionWhileCreatingTable() { // wystąpi tylko wtedy gdy mamy problem w składni metody createTable, jak jest wszystko w porządku to nie przejdzie
        //Given

        //When

        //Then
        Assertions.assertThrows(DateBaseActionException.class, () -> movieDAO.createTable());
    }

    @Test
    void deleteTable() {
        //When

        //Given
        movieDAO.deleteTable();
        //Then
    }

    @Test
    void createMovie() {
        //Given
        Movie movie = new Movie("Indiana Jones", "Action", 1988);
        //When
        int result = movieDAO.createMovie(movie);
        //Then
        Assertions.assertEquals(1, result);
    }

    @Test
    void deleteMovie(){

    }

    @Test
    void updateMoviesTitle() {
        //Given

        //When

        //Then
}

    @Test
    void findMovieById() {
    }

    @Test
    void findAll() {
        //Given
        movieDAO.createMovie(new Movie("Shrek", "anime", 2001));
        movieDAO.createMovie(new Movie("Shrek 2", "anime", 2003));
        //When
        List<Movie> movieList = movieDAO.findAll();
        int result = movieList.size();
        //Then
        Assertions.assertEquals(2, result);
    }
}