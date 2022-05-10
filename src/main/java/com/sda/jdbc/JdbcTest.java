package com.sda.jdbc;
/*
Zadanie 1 Wykorzystując JDBC API oraz dowolną relacyjną bazę danych (np. MySQL, H2)
wykonaj następujące zapytania:  stwórz tabelę MOVIES z kolumnami:
id typu INTEGER AUTO INCREMENT, title typu VARCHAR(255), genre typu VARCHAR(255), yearOfRelease typu INTEGER.
Weź pod uwagę, że tabela o nazwie MOVIE może już istnieć. W takim przypadku usuń ją.
 dodaj trzy dowolne rekordy do tabeli MOVIES  zaktualizuj jeden wybrany rekord (skorzystaj z PreparedStatement)
 usuń wybrany rekord o określonym identyfikatorze
 wyświetl wszystkie pozostałe rekordy w bazie W zadaniu skup się na poprawnym wykorzystaniu JDBC API.
Wszystkie zapytania mogą być wykonane bezpośrednio w metodzie main. Wykorzystaj pojedyncze połączenie do wykonania wszystkich zapytań.
Pamiętaj jednak o wykorzystaniu try-with-resources podczas otwierania połączenia oraz tworzenia obiektów typu Statement czy PreparedStatement.
W zadaniu tym również nie przejmuj się obsługą wyjątków (w przypadku błędu, wyświetl stacktrace).
 */

import java.sql.*;

public class JdbcTest {
    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection //  tworzymy połączenie dla wszystkich zapytań - try with resources
                ("jdbc:mysql://localhost:3306/jdbc_example", "Kamil", "Kamil");
        ) {
            try (Statement statement = connection.createStatement()) { // tworzymy zapytanie do tworzenia tabel
                statement.execute("CREATE TABLE IF NOT EXISTS MOVIES(\n" +
                        "id INTEGER AUTO_INCREMENT, \n" +
                        "title  VARCHAR(255), \n" +
                        "genre  VARCHAR(255), \n" +
                        "yearOfRelease  INTEGER,\n" +
                        "PRIMARY KEY (id)\n" +
                        ");");
            }

//            try (Statement statement = connection.createStatement()) { // tworzenie zapytania do tworzenia nowych rekordów
//                final String insertLord = "INSERT INTO movies VALUES(id, 'Lord of the Rings', 'Fantasy', 2001);";
//                final String insertIndiana = "INSERT INTO movies VALUES(id, 'Indiana Jones', 'Adventure', 1988);";
//                final String insertStar = "INSERT INTO movies VALUES(id, 'Star Wars', 'S-Fi', 1978);";
//                statement.execute(insertLord);
//                statement.execute(insertIndiana);
//                statement.execute(insertStar);
//            } -- zakomentowane, aby nie były dodawane kolejne rekordy

            // aktualizacja rekordu
            String updateQuery = "UPDATE movies SET title = ?, genre = ? WHERE id = ?;";  // ? - przekazuje parametr
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) { // PreparedStatement używamy jak mamy zapytanie parametryzowane
                preparedStatement.setString(1, "Indiana Jones 2"); // 1 odpowiada za pierwszy ?
                preparedStatement.setString(2, "Action");
                preparedStatement.setInt(3, 2);
                preparedStatement.executeUpdate(); // wykonanie update'u - inaczej nie wykona się update
            }
            // usuwanie rekordu
            String deleteQuery = "DELETE FROM movies  WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, 6);
                preparedStatement.execute();
            }


                MovieDAOImpl movieDAO = MovieDAOImpl.getInstance(connection);
                movieDAO.deleteMovie(8);


            try (Statement statement = connection.createStatement()) {
                String showTableQuery = "SELECT * FROM movies;";
                ResultSet resultSet = statement.executeQuery(showTableQuery);

                while (resultSet.next()) { // wyświetlanie  rekordów
                    System.out.print(resultSet.getInt("id") + ", ");
                    System.out.print(resultSet.getString("title") + ", ");
                    System.out.print(resultSet.getString("genre") + ", ");
                    System.out.print(resultSet.getInt("yearOfRelease"));
                    System.out.println();
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }




    }
}
