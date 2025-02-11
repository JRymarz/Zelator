# Zelator
System wspomagający zarządzanie grupami religijnymi – ZELATOR

## Instrukcja uruchamiania:
Przed uruchomieniem aplikacji należy zainstalować Java Development Kit (JDK 17) oraz XAMPP (z MySQL i Apache). Po instalacji programu XAMPP potrzeba go uruchomić wraz z modułami Apache i MySQL. Gdy serwer MySQL jest już uruchomiony należy przejść do phpMyAdmin, wchodząc na adres http://localhost/phpmyadmin. Następnie w panelu należy utworzyć nową bazę danych, wykonując w zakładce SQL następujące zapytanie: CREATE DATABASE zelator_db;
Po utworzeniu bazy konieczne jest jej uzupełnienie danymi. W tym celu należy przejść do zakładki Import, a następnie wybrać plik zelator_db.sql, znajdujący się w katalogu głównym projektu. Po załadowaniu pliku i kliknięciu Wykonaj, baza danych zostanie poprawnie skonfigurowana.
Teraz by uruchomić aplikację, należy otworzyć wiersz polecenia i przejść do katalogu build, znajdującego się w głównym katalogu projektu. Aplikacja jest uruchamiana poprzez wpisanie polecenia: java -jar zelator-0.0.1-SNAPSHOT.jar
Od tego momentu backend jest dostępny pod adresem http://localhost:9002
