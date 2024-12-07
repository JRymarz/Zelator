-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 07, 2024 at 08:03 PM
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
-- Struktura tabeli dla tabeli `intention`
--

CREATE TABLE `intention` (
  `id` bigint(20) NOT NULL,
  `title` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `intention`
--

INSERT INTO `intention` (`id`, `title`, `description`) VALUES
(1, 'Pokój na świecie', 'Prosimy o pokój na świecie, szczególnie w krajach dotkniętych wojną i konfliktami.'),
(2, 'Zdrowie chorych', 'Modlimy się za wszystkich chorych, cierpiących oraz ich opiekunów o siłę, zdrowie i łaskę uzdrowienia.'),
(3, 'Jedność w rodzinach', 'Prosimy o jedność, miłość i wzajemne zrozumienie w rodzinach, zwłaszcza tych doświadczających trudności.'),
(4, 'Naśladowanie Jezusa przez młodzież', 'Modlimy się o młodzież, aby wzrastała w wierze i wierności nauce Chrystusa.'),
(5, 'Za kapłanów i osoby konsekrowane', 'Prosimy o siłę i świętość dla kapłanów oraz osób poświęconych Bogu w życiu zakonnym.'),
(6, 'O ochronę życia poczętego', 'Modlimy się o poszanowanie życia od poczęcia aż do naturalnej śmierci.'),
(7, 'Za osoby w żałobie', 'Prosimy o pocieszenie dla tych, którzy stracili bliskich, oraz o życie wieczne dla zmarłych.'),
(8, 'Nawrócenie grzeszników', 'Modlimy się o łaskę nawrócenia dla tych, którzy oddalili się od Boga i Jego miłości.'),
(9, 'Za prześladowanych chrześcijan', 'Prosimy o siłę, wytrwałość i ochronę dla chrześcijan prześladowanych za wiarę.'),
(10, 'Pokój i radość w sercach', 'Modlimy się, aby każdy człowiek odnalazł wewnętrzny pokój i prawdziwą radość w Chrystusie.');

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
-- Struktura tabeli dla tabeli `rosary_group`
--

CREATE TABLE `rosary_group` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `leader_id` bigint(20) NOT NULL,
  `intention_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rosary_group`
--

INSERT INTO `rosary_group` (`id`, `name`, `leader_id`, `intention_id`) VALUES
(1, 'Róża Świętej Teresy z Lisieux', 3, 2);

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
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `first_name`, `last_name`, `password`, `email`, `role`, `active`, `group_id`, `mystery_id`) VALUES
(1, 'Marek', 'Nowak', '$2a$12$PEtWsT9935qD7TjoO8jGzuwgr/acJuga8sDZScR2InGE6F.8dZmba', 'mareknowak@mail.com', 'MainZelator', 1, NULL, NULL),
(3, 'Anna', 'Kowalska', '$2a$10$tfyhxw2Gbi.fWP9ELdalWO2Op0tqzeuRegOcEvESyf4K17t04fMda', 'annakowalski@mail.com', 'Zelator', 1, 1, NULL),
(11, 'Maria', 'Wiśniewska', '$2a$10$kcZ4xDiM5LMpmdCGpkqVJu1SjpKKIx8BngCMP1cqxB7leY.I62loG', 'mariawisniewska@mail.com', 'Member', 1, NULL, NULL),
(13, 'Tomasz', 'Zieliński', '$2a$10$k.1C8LIU7.g0eICcSkOgx.VhWu.rJKkeJYkm9NrMFslvHod0UGxJ2', 'tomaszzielinski@mail.com', 'Member', 1, NULL, NULL),
(14, 'Piotr', 'Lewandowski', '$2a$10$wYAJgNP9MTdLDRNiBiYiPOHX3GspVg4VUUFzM068DjjeNrn4Mfdiu', 'piotrlewandowski@mail.com', 'Member', 1, NULL, NULL),
(15, 'Katarzyna', 'Kamińska', '$2a$10$i0N2TGtRKXdcACdYe6u3i.L5Ht2gCVHOrQWnNaGfQ.Ts5Uw3Ei6.m', 'katarzynakaminska@mail.com', 'Zelator', 1, NULL, NULL);

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
-- Indeksy dla tabeli `rosary_group`
--
ALTER TABLE `rosary_group`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_leader` (`leader_id`),
  ADD KEY `fk_intention` (`intention_id`);

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
-- AUTO_INCREMENT for table `intention`
--
ALTER TABLE `intention`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

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
-- AUTO_INCREMENT for table `rosary_group`
--
ALTER TABLE `rosary_group`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `calendar_event`
--
ALTER TABLE `calendar_event`
  ADD CONSTRAINT `calendar_event_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `rosary_group` (`id`),
  ADD CONSTRAINT `calendar_event_ibfk_2` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `chat`
--
ALTER TABLE `chat`
  ADD CONSTRAINT `chat_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `chat_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `chat_ibfk_3` FOREIGN KEY (`receiver_gr_id`) REFERENCES `rosary_group` (`id`);

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
-- Constraints for table `rosary_group`
--
ALTER TABLE `rosary_group`
  ADD CONSTRAINT `fk_intention` FOREIGN KEY (`intention_id`) REFERENCES `intention` (`id`),
  ADD CONSTRAINT `fk_leader` FOREIGN KEY (`leader_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_group` FOREIGN KEY (`group_id`) REFERENCES `rosary_group` (`id`),
  ADD CONSTRAINT `fk_mystery` FOREIGN KEY (`mystery_id`) REFERENCES `mystery` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
