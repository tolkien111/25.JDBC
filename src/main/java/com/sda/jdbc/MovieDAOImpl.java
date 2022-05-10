package com.sda.jdbc;
/*
Zaimplementuj klasę MovieDAOImpl (DAO - Data Access Object). Powinna ona realizować podstawowe operacje bazodanowe dla tabeli MOVIES,
której struktura została opisana w zadaniu 1. Załóż, że obiekt reprezentujący otwarte połączenie do bazy danych przychodzi w konstruktorze tej klasy.
Pamiętaj o wykorzystaniu PreparedStatement w miejscach, gdzie to możliwe i zamykaniu obiektów (wykorzystaj try-with-resources).
Zaimplementuj również klasę Movie, która reprezentuje pojedynczy wiersz w tabeli MOVIES, która wykorzystasz w implementacji klasy MovieDAOImple.
Zaimplementuj poniższe operacje. Każda z nich powinna być reprezentowana przez osobną metodę publiczną:
 tworzenie tabeli MOVIES
 usuwanie tabeli MOVIES
 dodawanie rekordu
 usuwanie rekordu po identyfikatorze
 aktualizacja tytułu filmu o danych identyfikatorze
 szukanie filmu po identyfikatorze
 pobieranie wszystkich filmów W przypadku wystąpienia wyjątku SQLException, niech metoda wyrzuca wyjątek DatabaseActionException.
Klasa ta powinna dziedziczyć po klasie RuntimeException. Klasa MovieDAOImpl powinna implementować poniższy interfejs:
import java.util.List;
import java.util.Optional;
public interface MovieDAO {
void createTable();
void deleteTable();
void createMovie(final Movie movie);
void deleteMovie(int id);
void updateMoviesTitle(int id, String newTitle);
Optional<Movie> findMovieById(int id);
List<Movie> findAll();
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAOImpl implements MovieDAO {
    private Connection connection;

    private static MovieDAOImpl instance;   // dorzucamy Singleton, lazy

    public static MovieDAOImpl getInstance(Connection connection) { // zwykły Singleton nie zawiera argumentu przy wywołaniu metody, w tym przypadku jest on zmodyfikowany
        if (instance == null) {
            instance = new MovieDAOImpl(connection);
        }
        return instance;
    }


    private MovieDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTable() {
        try (Statement statement = connection.createStatement()) { // tworzymy zapytanie do tworzenia tabel
            statement.execute("CREATE TABLE IF NOT EXISTS MOVIES(\n" +
                    "id INTEGER AUTO_INCREMENT, \n" +
                    "title  VARCHAR(255), \n" +
                    "genre  VARCHAR(255), \n" +
                    "yearOfRelease  INTEGER,\n" +
                    "PRIMARY KEY (id)\n" +
                    ");");
        } catch (SQLException throwables) {
            throw new DateBaseActionException("Problem with query execution", throwables); // w stacktrace poleci nasz wyjątek, poźniej reszta stacktrace
        }
    }

    @Override
    public void deleteTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM movies;");
        } catch (SQLException throwables) {
            throw new DateBaseActionException("Problem with table deletion", throwables);
        }
    }

    @Override
    public int createMovie(Movie movie) {
        String query = "INSERT INTO MOVIES (title, genre, yearOfRelease) values (?,?,?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) { // tworzenie zapytania do tworzenia nowych rekordów
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getGenre());
            preparedStatement.setInt(3, movie.getYearOfRelease());
            return preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DateBaseActionException("Problem with inserting new movie", throwables);
        }
    }

    @Override
    public void deleteMovie(int id) {
        String query = "DELETE FROM movies WHERE id = ?;"; //dokończyć
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throw new DateBaseActionException("Problem with deleting movie with", throwables);
        }


    }

    @Override
    public void updateMoviesTitle(int id, String newTitle) {

    }

    @Override
    public Optional<Movie> findMovieById(int id) { // Optional.of - do pomocy
        return Optional.empty();
    }

    @Override
    public List<Movie> findAll() {
        try (Statement statement = connection.createStatement()) {
            boolean result = statement.execute("SELECT * FROM movies;");
            if (result) {
                ResultSet resultSet = statement.getResultSet();
                List<Movie> movieList = new ArrayList<>();
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String title = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    int yearOfRelease = resultSet.getInt("yearOfRelease");
                    Movie movie = new Movie(id, title, genre, yearOfRelease);
                    movieList.add(movie);
                }
                return movieList;
            }
            return new ArrayList<>(); // jeżeli nie wykona się nas if (result) to musimy i tak zwrócić listę dlatego zwracamy tutaj pustą listę
            // kod jest prostrzy. Możemy użyć elsa, ale spowoduje to mniejszą czytelność kodu

        } catch (SQLException throwables) {
            throw new DateBaseActionException("Problem with finding movies", throwables);
        }
    }
}
