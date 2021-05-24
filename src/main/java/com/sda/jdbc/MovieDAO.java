package com.sda.jdbc;

import java.util.List;
import java.util.Optional;

public interface MovieDAO {
    void createTable();
    void deleteTable();
    int createMovie(final Movie movie);
    void deleteMovie(int id);
    void updateMoviesTitle(int id, String newTitle);
    Optional<Movie> findMovieById(int id);
    List<Movie> findAll();
}
