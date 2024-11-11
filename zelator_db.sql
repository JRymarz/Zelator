-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Lis 11, 2024 at 05:09 PM
-- Wersja serwera: 10.4.32-MariaDB
-- Wersja PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `zelator_db`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `calendar_event`
--

CREATE TABLE `calendar_event` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `event_date` datetime NOT NULL,
  `event_type` enum('MYSTERYCHANGE','MASS','PRAYER','OTHER') NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `creator_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `chat`
--

CREATE TABLE `chat` (
  `id` bigint(20) NOT NULL,
  `sender_id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `receiver_gr_id` bigint(20) DEFAULT NULL,
  `message` varchar(1000) NOT NULL,
  `time_stamp` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `group`
--

CREATE TABLE `group` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `leader_id` bigint(20) NOT NULL,
  `intention_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `intention`
--

CREATE TABLE `intention` (
  `id` bigint(20) NOT NULL,
  `title` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `mass_request`
--

CREATE TABLE `mass_request` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `intention` varchar(255) NOT NULL,
  `request_date` date NOT NULL,
  `mass_date` datetime NOT NULL,
  `status` enum('PENDING','APPROVED','REJECTED') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `mystery`
--

CREATE TABLE `mystery` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `meditation` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `prayer_status`
--

CREATE TABLE `prayer_status` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `mystery_id` bigint(20) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 0,
  `prayer_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `role` enum('MainZelator','Zelator','Member') NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 1,
  `group_id` bigint(20) DEFAULT NULL,
  `mystery_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `calendar_event`
--
ALTER TABLE `calendar_event`
  ADD PRIMARY KEY (`id`),
  ADD KEY `group_id` (`group_id`),
  ADD KEY `creator_id` (`creator_id`);

--
-- Indeksy dla tabeli `chat`
--
ALTER TABLE `chat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sender_id` (`sender_id`),
  ADD KEY `receiver_id` (`receiver_id`),
  ADD KEY `receiver_gr_id` (`receiver_gr_id`);

--
-- Indeksy dla tabeli `group`
--
ALTER TABLE `group`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_leader` (`leader_id`),
  ADD KEY `fk_intention` (`intention_id`);

--
-- Indeksy dla tabeli `intention`
--
ALTER TABLE `intention`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `mass_request`
--
ALTER TABLE `mass_request`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeksy dla tabeli `mystery`
--
ALTER TABLE `mystery`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `prayer_status`
--
ALTER TABLE `prayer_status`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `mystery_id` (`mystery_id`);

--
-- Indeksy dla tabeli `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `fk_group` (`group_id`),
  ADD KEY `fk_mystery` (`mystery_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `calendar_event`
--
ALTER TABLE `calendar_event`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `chat`
--
ALTER TABLE `chat`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `group`
--
ALTER TABLE `group`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `intention`
--
ALTER TABLE `intention`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mass_request`
--
ALTER TABLE `mass_request`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mystery`
--
ALTER TABLE `mystery`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `prayer_status`
--
ALTER TABLE `prayer_status`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `calendar_event`
--
ALTER TABLE `calendar_event`
  ADD CONSTRAINT `calendar_event_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`),
  ADD CONSTRAINT `calendar_event_ibfk_2` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `chat`
--
ALTER TABLE `chat`
  ADD CONSTRAINT `chat_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `chat_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `chat_ibfk_3` FOREIGN KEY (`receiver_gr_id`) REFERENCES `group` (`id`);

--
-- Constraints for table `group`
--
ALTER TABLE `group`
  ADD CONSTRAINT `fk_intention` FOREIGN KEY (`intention_id`) REFERENCES `intention` (`id`),
  ADD CONSTRAINT `fk_leader` FOREIGN KEY (`leader_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `mass_request`
--
ALTER TABLE `mass_request`
  ADD CONSTRAINT `mass_request_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `prayer_status`
--
ALTER TABLE `prayer_status`
  ADD CONSTRAINT `prayer_status_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `prayer_status_ibfk_2` FOREIGN KEY (`mystery_id`) REFERENCES `mystery` (`id`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_group` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`),
  ADD CONSTRAINT `fk_mystery` FOREIGN KEY (`mystery_id`) REFERENCES `mystery` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
