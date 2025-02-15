-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 13, 2025 at 11:38 AM
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
  `creator_id` bigint(20) NOT NULL,
  `state` enum('scheduled','completed','undone') NOT NULL DEFAULT 'scheduled'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `calendar_event`
--

INSERT INTO `calendar_event` (`id`, `title`, `event_date`, `event_type`, `group_id`, `creator_id`, `state`) VALUES
(2, 'Zmiana tajemnic różańcowych', '2025-01-14 15:33:00', 'MYSTERYCHANGE', 1, 3, 'completed'),
(3, 'Zmiana tajemnic różańcowych', '2025-01-14 18:00:00', 'MYSTERYCHANGE', 1, 3, 'completed'),
(4, 'Zmiana tajemnic różańcowych', '2025-01-23 18:44:00', 'MYSTERYCHANGE', 1, 3, 'completed'),
(5, 'Zmiana tajemnic różańcowych', '2025-01-23 18:48:00', 'MYSTERYCHANGE', 1, 3, 'completed'),
(6, 'Niedmówiona modlitwa', '2025-01-23 00:00:00', 'PRAYER', NULL, 11, 'undone'),
(7, 'Niedmówiona modlitwa', '2025-01-23 00:00:00', 'PRAYER', NULL, 13, 'undone'),
(8, 'Niedmówiona modlitwa', '2025-01-23 00:00:00', 'PRAYER', NULL, 14, 'undone'),
(9, 'Niedmówiona modlitwa', '2025-01-23 00:00:00', 'PRAYER', NULL, 19, 'undone'),
(10, 'Msza Święta: Za zmarłych', '2025-01-31 17:30:00', 'MASS', NULL, 13, 'scheduled'),
(11, 'Msza Święta: Dziękczynna', '2025-03-10 08:00:00', 'MASS', NULL, 13, 'scheduled'),
(12, 'Mój event', '2025-01-30 08:00:00', 'OTHER', NULL, 3, 'scheduled'),
(13, 'Własne wydarzenie', '2025-01-29 12:00:00', 'OTHER', NULL, 13, 'scheduled'),
(14, 'Zmiana tajemnic różańcowych', '2025-02-04 20:00:00', 'MYSTERYCHANGE', 1, 3, 'completed'),
(15, 'Zmiana tajemnic różańcowych', '2025-03-02 18:00:00', 'MYSTERYCHANGE', 1, 3, 'scheduled'),
(16, 'Zmiana tajemnic różańcowych', '2025-02-01 18:00:00', 'MYSTERYCHANGE', 2, 1, 'completed');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `chat`
--

CREATE TABLE `chat` (
  `id` bigint(20) NOT NULL,
  `sender_id` bigint(20) DEFAULT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `receiver_gr_id` bigint(20) DEFAULT NULL,
  `message` varchar(1000) NOT NULL,
  `time_stamp` datetime NOT NULL DEFAULT current_timestamp(),
  `is_read` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chat`
--

INSERT INTO `chat` (`id`, `sender_id`, `receiver_id`, `receiver_gr_id`, `message`, `time_stamp`, `is_read`) VALUES
(2, 3, NULL, 1, 'Witam w naszej grupie.', '2025-01-27 18:00:17', 1),
(3, 3, 13, NULL, 'Cześć', '2025-01-27 18:16:15', 1),
(5, 13, 3, NULL, 'Odpowiedź', '2025-01-27 22:41:39', 1),
(7, 13, NULL, 1, 'Cześć', '2025-01-27 22:47:44', 1),
(22, 1, 20, NULL, 'WItam', '2025-01-31 12:54:10', 0),
(23, NULL, 14, NULL, 'Przypomnienei: Nie odmówiłeś jeszcze dzisiejszej modlitwy.', '2025-01-31 14:49:46', 0),
(28, NULL, 13, NULL, 'Przypomnienie: Czas na modlitwę!', '2025-01-31 22:18:12', 1),
(29, 11, 13, NULL, 'Cześć, Tomek! Jak się czujesz?', '2025-02-09 20:50:49', 1),
(30, 13, 11, NULL, 'Dzień dobry, Mario! Dobrze, dziękuję. A Ty?', '2025-02-09 20:51:25', 0),
(31, 11, 13, NULL, 'Też dobrze! Pomyślałam, że może warto by było pomodlić się dzisiaj razem.', '2025-02-09 20:51:56', 1),
(32, NULL, 14, NULL, 'Przypomnienei: Nie odmówiłeś jeszcze dzisiejszej modlitwy.', '2025-02-10 09:12:44', 0),
(33, NULL, 3, NULL, 'Przypomnienie: Czas na modlitwę!', '2025-02-11 20:46:59', 1);

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

--
-- Dumping data for table `mass_request`
--

INSERT INTO `mass_request` (`id`, `user_id`, `intention`, `request_date`, `mass_date`, `status`) VALUES
(1, 13, 'Za zmarłych', '2025-01-25', '2025-01-31 17:30:00', 'APPROVED'),
(2, 13, 'Za zdrowie', '2025-01-25', '2025-02-02 17:00:00', 'REJECTED'),
(3, 13, 'Dziękczynna', '2025-01-25', '2025-03-10 08:00:00', 'APPROVED'),
(4, 13, 'Za zmarłych', '2025-01-25', '2025-02-28 12:00:00', 'REJECTED'),
(5, 13, 'O uzdrowienie', '2025-02-09', '2025-02-28 09:00:00', 'PENDING');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `mystery`
--

CREATE TABLE `mystery` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `meditation` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mystery`
--

INSERT INTO `mystery` (`id`, `name`, `meditation`) VALUES
(1, 'Zwiastowanie Najświętszej Maryi Pannie', 'Rozważamy moment, gdy Archanioł Gabriel zwiastował Maryi, że zostanie Matką Bożą.'),
(2, 'Nawiedzenie św. Elżbiety', 'Rozważamy spotkanie Maryi z Elżbietą, która napełniona Duchem Świętym poznała, że Maryja nosi w łonie Zbawiciela.'),
(3, 'Narodzenie Pana Jezusa', 'Rozważamy narodziny Jezusa Chrystusa w ubogiej stajence w Betlejem.'),
(4, 'Ofiarowanie Jezusa w świątyni', 'Rozważamy ofiarowanie Jezusa w świątyni zgodnie z prawem Mojżeszowym.'),
(5, 'Odnalezienie Jezusa w świątyni', 'Rozważamy moment, gdy Maryja i Józef odnaleźli dwunastoletniego Jezusa nauczającego w świątyni.'),
(6, 'Modlitwa Jezusa w Ogrójcu', 'Rozważamy mękę Jezusa w Ogrójcu, gdy modlił się i przyjął wolę Ojca.'),
(7, 'Biczowanie Pana Jezusa', 'Rozważamy cierpienie Jezusa, który był biczowany za nasze grzechy.'),
(8, 'Cierniem ukoronowanie Pana Jezusa', 'Rozważamy mękę Jezusa, gdy został upokorzony i ukoronowany cierniami.'),
(9, 'Dźwiganie krzyża na Kalwarię', 'Rozważamy drogę Jezusa na Golgotę, gdzie dźwigał krzyż na naszych barkach.'),
(10, 'Ukrzyżowanie i śmierć Pana Jezusa', 'Rozważamy moment, gdy Jezus oddał życie za nasze grzechy na krzyżu.'),
(11, 'Zmartwychwstanie Pana Jezusa', 'Rozważamy chwalebne zmartwychwstanie Jezusa, które przyniosło zbawienie światu.'),
(12, 'Wniebowstąpienie Pana Jezusa', 'Rozważamy wniebowstąpienie Jezusa do Ojca, gdzie przygotowuje miejsce dla swoich wiernych.'),
(13, 'Zesłanie Ducha Świętego', 'Rozważamy moment, gdy Duch Święty zstąpił na Apostołów i Maryję w Wieczerniku.'),
(14, 'Wniebowzięcie Najświętszej Maryi Panny', 'Rozważamy, jak Maryja została wzięta do nieba z ciałem i duszą.'),
(15, 'Ukoronowanie Maryi na Królową Nieba i Ziemi', 'Rozważamy koronację Maryi jako Królowej nieba i ziemi.'),
(16, 'Chrzest Pana Jezusa w Jordanie', 'Rozważamy chrzest Jezusa przez Jana Chrzciciela, który ukazał Go jako Syna Bożego.'),
(17, 'Objawienie Jezusa na weselu w Kanie Galilejskiej', 'Rozważamy pierwszy cud Jezusa, gdy przemienił wodę w wino na weselu w Kanie.'),
(18, 'Głoszenie Królestwa Bożego i wezwanie do nawrócenia', 'Rozważamy nauczanie Jezusa o Królestwie Bożym i wezwaniu do pokuty.'),
(19, 'Przemienienie na górze Tabor', 'Rozważamy przemienienie Jezusa, gdy objawił swoją chwałę przed wybranymi Apostołami.'),
(20, 'Ustanowienie Eucharystii', 'Rozważamy ustanowienie Eucharystii jako sakramentu miłości i pamiątki męki Chrystusa.');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `mystery_change_task`
--

CREATE TABLE `mystery_change_task` (
  `id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `intention_id` bigint(20) NOT NULL,
  `state` enum('PENDING','EXECUTED','CANCELLED') NOT NULL DEFAULT 'PENDING',
  `event_date` datetime NOT NULL,
  `calendar_event_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mystery_change_task`
--

INSERT INTO `mystery_change_task` (`id`, `group_id`, `intention_id`, `state`, `event_date`, `calendar_event_id`) VALUES
(15, 1, 8, 'PENDING', '2025-03-02 18:00:00', 15);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `mystery_change_task_member`
--

CREATE TABLE `mystery_change_task_member` (
  `id` bigint(20) NOT NULL,
  `mystery_change_task_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `mystery_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mystery_change_task_member`
--

INSERT INTO `mystery_change_task_member` (`id`, `mystery_change_task_id`, `user_id`, `mystery_id`) VALUES
(44, 15, 3, 3),
(45, 15, 11, 14),
(46, 15, 13, 7),
(47, 15, 14, 10),
(48, 15, 19, 2);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `prayer_status`
--

CREATE TABLE `prayer_status` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 0,
  `prayer_date` date NOT NULL,
  `prayer_reminder_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prayer_status`
--

INSERT INTO `prayer_status` (`id`, `user_id`, `status`, `prayer_date`, `prayer_reminder_time`) VALUES
(1, 19, 1, '2025-01-24', NULL),
(2, 14, 0, '2025-01-24', NULL),
(3, 3, 1, '2025-01-24', NULL),
(4, 11, 0, '2025-01-24', NULL),
(5, 13, 1, '2025-01-24', '22:18:00'),
(6, 1, 0, '2025-01-31', NULL),
(7, 20, 0, '2025-01-31', NULL),
(8, 21, 0, '2025-02-09', NULL),
(9, 23, 0, '2025-02-09', NULL),
(10, 24, 0, '2025-02-09', NULL),
(11, 25, 0, '2025-02-09', NULL),
(12, 26, 0, '2025-02-09', NULL),
(13, 27, 0, '2025-02-09', NULL),
(14, 28, 0, '2025-02-09', NULL),
(15, 29, 0, '2025-02-09', NULL),
(16, 30, 0, '2025-02-09', NULL),
(17, 31, 0, '2025-02-09', NULL),
(18, 32, 0, '2025-02-09', NULL),
(19, 33, 0, '2025-02-09', NULL);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `rosary_group`
--

CREATE TABLE `rosary_group` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `leader_id` bigint(20) DEFAULT NULL,
  `intention_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rosary_group`
--

INSERT INTO `rosary_group` (`id`, `name`, `leader_id`, `intention_id`) VALUES
(1, 'Róża Świętej Teresy z Lisieux', 3, 2),
(2, 'Róża Matki Bożej Fatimskiej', 1, 5),
(3, 'Róża Różańcowa św. Ojca Pio', 39, 2);

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
(1, 'Marek', 'Nowak', '$2a$12$PEtWsT9935qD7TjoO8jGzuwgr/acJuga8sDZScR2InGE6F.8dZmba', 'mareknowak@mail.com', 'MainZelator', 1, 2, 16),
(3, 'Anna', 'Kowalska', '$2a$10$tfyhxw2Gbi.fWP9ELdalWO2Op0tqzeuRegOcEvESyf4K17t04fMda', 'annakowalski@mail.com', 'Zelator', 1, 1, 2),
(11, 'Maria', 'Wiśniewska', '$2a$10$kcZ4xDiM5LMpmdCGpkqVJu1SjpKKIx8BngCMP1cqxB7leY.I62loG', 'mariawisniewska@mail.com', 'Member', 1, 1, 20),
(13, 'Tomasz', 'Zieliński', '$2a$10$k.1C8LIU7.g0eICcSkOgx.VhWu.rJKkeJYkm9NrMFslvHod0UGxJ2', 'tomaszzielinski@mail.com', 'Member', 1, 1, 7),
(14, 'Piotr', 'Lewandowski', '$2a$10$wYAJgNP9MTdLDRNiBiYiPOHX3GspVg4VUUFzM068DjjeNrn4Mfdiu', 'piotrlewandowski@mail.com', 'Member', 1, 1, 6),
(15, 'Katarzyna', 'Kamińska', '$2a$10$i0N2TGtRKXdcACdYe6u3i.L5Ht2gCVHOrQWnNaGfQ.Ts5Uw3Ei6.m', 'katarzynakaminska@mail.com', 'Zelator', 1, NULL, NULL),
(18, 'Monika', 'Dąbrowska', '$2a$10$BfIxLhoAhUpYI.n.dkj1g.HhQ0e/c59aPT2E6S7y85qfVQzDxI8IC', 'monikadabrowska@mail.com', 'Zelator', 1, NULL, NULL),
(19, 'Mariusz', 'Nowak', '$2a$10$Ps.WZsYqFSiew1fg/QlIM.HHQQZ.gZVbf0i7QS8pDZ/sEBlTHJImO', 'mariusznowak@mail.com', 'Member', 1, 1, 11),
(20, 'Piotr', 'Wiśniewski', '$2a$10$VXHrUJQmHYVqWq2GJpQE1ux9YiGgABTvHO0S/ADZ7UrdI7QPgFilS', 'piotr.wisniewski@example.com', 'Member', 1, 2, 15),
(21, 'Maria', 'Lis', '$2a$10$WLGck9pUJqjoGQ0nS5gMo.x19yVgwjMU3wuHT.I745PmKL4M0hx3q', 'maria.lis@example.com', 'Member', 1, 1, 13),
(22, 'Julia', 'Wójcik', '$2a$10$jZzPc.C5zhcNkCQ/7w7/N.U.Z18tIpfaXCS3xTpnPCuBrCWm/C2FO', 'juliawojcik@mail.com', 'Zelator', 1, NULL, NULL),
(23, 'Zofia', 'Nowicka', '$2a$10$2PRAq4qIdO1.LZSZiV5.MOJbVhtU1IaLg4XXlSaAiwrDzXAcQQTUS', 'zofianowicka@mail.com', 'Member', 1, 1, 15),
(24, 'Jan', 'Kowalski', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'jan.kowalski@example.com', 'Member', 1, 1, 9),
(25, 'Anna', 'Nowak', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'anna.nowak@example.com', 'Member', 1, 1, 1),
(26, 'Tomasz', 'Wiśniewski', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'tomasz.wisniewski@example.com', 'Member', 1, 1, 10),
(27, 'Maria', 'Kaczmarek', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'maria.kaczmarek@example.com', 'Member', 1, 1, 17),
(28, 'Piotr', 'Zieliński', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'piotr.zielinski@example.com', 'Member', 1, 1, 4),
(29, 'Paweł', 'Wójcik', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'pawel.wojcik@example.com', 'Member', 1, 1, 16),
(30, 'Zofia', 'Wójcik', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'zofia.wojcik@example.com', 'Member', 1, 1, 12),
(31, 'Michał', 'Lewandowski', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'michal.lewandowski@example.com', 'Member', 1, 1, 8),
(32, 'Julia', 'Wróblewska', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'julia.wroblewska@example.com', 'Member', 1, 1, 14),
(33, 'Kamil', 'Mazur', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'kamil.mazur@example.com', 'Member', 1, 1, 19),
(34, 'Katarzyna', 'Kowalczyk', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'katarzyna.kowalczyk@example.com', 'Member', 1, 1, NULL),
(35, 'Wojciech', 'Kozłowski', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'wojciech.kozlowski@example.com', 'Member', 1, 1, NULL),
(36, 'Natalia', 'Pawlak', 'c1ddd7824190de22c5c4258f54083741772161f04cf83820e502d10a1a4c7b76', 'natalia.pawlak@example.com', 'Member', 1, 1, NULL),
(38, 'Mariusz', 'Kowalski', '$2a$10$l/wrbgJdbbliLMGU1HtgYeOZpWMHmqyNytJRfl7uJwCGPvXxSjc5i', 'mariuszkowalski@mail.com', 'Member', 1, NULL, NULL),
(39, 'Mariusz', 'Kamiński', '$2a$10$xMD7st4/lc7ScpD17qVYtuth.lffVdu3GtwD/hignOumvDsoWFpFu', 'mariuszkaminski@mail.com', 'Zelator', 1, 3, NULL);

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
-- Indeksy dla tabeli `mystery_change_task`
--
ALTER TABLE `mystery_change_task`
  ADD PRIMARY KEY (`id`),
  ADD KEY `group_id` (`group_id`),
  ADD KEY `intention_id` (`intention_id`),
  ADD KEY `FK_calendar_event` (`calendar_event_id`);

--
-- Indeksy dla tabeli `mystery_change_task_member`
--
ALTER TABLE `mystery_change_task_member`
  ADD PRIMARY KEY (`id`),
  ADD KEY `mystery_change_task_id` (`mystery_change_task_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `mystery_id` (`mystery_id`);

--
-- Indeksy dla tabeli `prayer_status`
--
ALTER TABLE `prayer_status`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `chat`
--
ALTER TABLE `chat`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `intention`
--
ALTER TABLE `intention`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `mass_request`
--
ALTER TABLE `mass_request`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `mystery`
--
ALTER TABLE `mystery`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `mystery_change_task`
--
ALTER TABLE `mystery_change_task`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `mystery_change_task_member`
--
ALTER TABLE `mystery_change_task_member`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `prayer_status`
--
ALTER TABLE `prayer_status`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `rosary_group`
--
ALTER TABLE `rosary_group`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

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
-- Constraints for table `mystery_change_task`
--
ALTER TABLE `mystery_change_task`
  ADD CONSTRAINT `FK_calendar_event` FOREIGN KEY (`calendar_event_id`) REFERENCES `calendar_event` (`id`),
  ADD CONSTRAINT `mystery_change_task_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `rosary_group` (`id`),
  ADD CONSTRAINT `mystery_change_task_ibfk_2` FOREIGN KEY (`intention_id`) REFERENCES `intention` (`id`);

--
-- Constraints for table `mystery_change_task_member`
--
ALTER TABLE `mystery_change_task_member`
  ADD CONSTRAINT `mystery_change_task_member_ibfk_1` FOREIGN KEY (`mystery_change_task_id`) REFERENCES `mystery_change_task` (`id`),
  ADD CONSTRAINT `mystery_change_task_member_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `mystery_change_task_member_ibfk_3` FOREIGN KEY (`mystery_id`) REFERENCES `mystery` (`id`);

--
-- Constraints for table `prayer_status`
--
ALTER TABLE `prayer_status`
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

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
