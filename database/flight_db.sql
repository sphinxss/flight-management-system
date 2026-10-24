-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 04, 2026 at 01:56 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `flight_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `flights`
--

CREATE TABLE `flights` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `date` varchar(50) DEFAULT NULL,
  `time` varchar(50) DEFAULT NULL,
  `origin` varchar(100) DEFAULT NULL,
  `destination` varchar(100) DEFAULT NULL,
  `flight_no` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `aircraft` varchar(50) DEFAULT NULL,
  `economy_price` decimal(10,2) DEFAULT NULL,
  `first_class_price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `flights`
--

INSERT INTO `flights` (`id`, `name`, `date`, `time`, `origin`, `destination`, `flight_no`, `status`, `aircraft`, `economy_price`, `first_class_price`) VALUES
(1, 'John Smith', '2026-05-01', '08:00 AM', 'USA', 'Japan', 'FL001', 'Confirmed', 'Boeing 777', 650.00, 2200.00),
(2, 'Maria Cruz', '2026-05-01', '09:00 AM', 'Philippines', 'Australia', 'FL002', 'Confirmed', 'Airbus A320', 400.00, 1500.00),
(3, 'David Lee', '2026-05-02', '10:00 AM', 'Japan', 'USA', 'FL003', 'Cancelled', 'Boeing 787', 700.00, 2500.00),
(4, 'Anna Reyes', '2026-05-02', '11:00 AM', 'Australia', 'Philippines', 'FL004', 'Confirmed', 'Airbus A330', 450.00, 1700.00),
(5, 'Chris Tan', '2026-05-03', '12:00 PM', 'USA', 'Korea', 'FL005', 'Confirmed', 'Boeing 737 MAX', 500.00, 1800.00),
(6, 'Mark Santos', '2026-05-03', '01:00 PM', 'Philippines', 'Japan', 'FL006', 'Not Confirmed', 'Airbus A321', 350.00, 1300.00),
(7, 'Lisa Wong', '2026-05-04', '02:00 PM', 'Japan', 'Australia', 'FL007', 'Confirmed', 'Boeing 777', 680.00, 2300.00),
(8, 'Kevin Lim', '2026-05-04', '03:00 PM', 'Australia', 'USA', 'FL008', 'Confirmed', 'Boeing 787', 720.00, 2600.00),
(9, 'Sophia Kim', '2026-05-05', '04:00 PM', 'Korea', 'Philippines', 'FL009', 'Confirmed', 'Airbus A320', 300.00, 1100.00),
(10, 'Daniel Cruz', '2026-05-05', '05:00 PM', 'USA', 'Australia', 'FL010', 'Not Confirmed', 'Airbus A350', 800.00, 2800.00),
(11, 'Alex Garcia', '2026-05-06', '06:00 AM', 'Japan', 'Philippines', 'FL011', 'Confirmed', 'Boeing 737', 320.00, 1200.00),
(12, 'Emma White', '2026-05-06', '07:00 AM', 'Australia', 'Korea', 'FL012', 'Confirmed', 'Airbus A321', 420.00, 1500.00),
(13, 'Nathan Hall', '2026-05-07', '08:00 AM', 'USA', 'Japan', 'FL013', 'Not Confirmed', 'Boeing 777', 670.00, 2250.00),
(14, 'Grace Miller', '2026-05-07', '09:00 AM', 'Philippines', 'USA', 'FL014', 'Confirmed', 'Boeing 787', 750.00, 2700.00),
(15, 'Lucas Green', '2026-05-08', '10:00 AM', 'Korea', 'Australia', 'FL015', 'Confirmed', 'Airbus A330', 480.00, 1750.00),
(16, 'Olivia Scott', '2026-05-08', '11:00 AM', 'Japan', 'USA', 'FL016', 'Confirmed', 'Airbus A350', 810.00, 2900.00),
(17, 'Ethan Brown', '2026-05-09', '12:00 PM', 'Australia', 'Japan', 'FL017', 'Not Confirmed', 'Boeing 777', 690.00, 2350.00),
(18, 'Mia Johnson', '2026-05-09', '01:00 PM', 'USA', 'Philippines', 'FL018', 'Confirmed', 'Boeing 787', 740.00, 2650.00),
(19, 'James Lee', '2026-05-10', '02:00 PM', 'Korea', 'USA', 'FL019', 'Confirmed', 'Airbus A350', 790.00, 2850.00),
(20, 'Ava Wilson', '2026-05-10', '03:00 PM', 'Philippines', 'Australia', 'FL020', 'Confirmed', 'Airbus A320', 390.00, 1450.00),
(21, 'Noah Adams', '2026-05-11', '04:00 PM', 'USA', 'Korea', 'FL021', 'Confirmed', 'Boeing 737 MAX', 520.00, 1850.00),
(22, 'Chloe Baker', '2026-05-11', '05:00 PM', 'Japan', 'Australia', 'FL022', 'Confirmed', 'Airbus A330', 500.00, 1800.00),
(23, 'Benjamin Clark', '2026-05-12', '06:00 AM', 'Philippines', 'USA', 'FL023', 'Not Confirmed', 'Boeing 787', 760.00, 2750.00),
(24, 'Ella Martinez', '2026-05-12', '07:00 AM', 'Australia', 'Japan', 'FL024', 'Confirmed', 'Boeing 777', 670.00, 2300.00),
(25, 'Matthew Young', '2026-05-13', '08:00 AM', 'Korea', 'Philippines', 'FL025', 'Confirmed', 'Airbus A321', 340.00, 1250.00),
(26, 'Scarlett King', '2026-05-13', '09:00 AM', 'USA', 'Australia', 'FL026', 'Confirmed', 'Airbus A350', 820.00, 2950.00),
(27, 'Henry Walker', '2026-05-14', '10:00 AM', 'Japan', 'Korea', 'FL027', 'Not Confirmed', 'Boeing 737', 370.00, 1350.00),
(28, 'Victoria Allen', '2026-05-14', '11:00 AM', 'Philippines', 'Japan', 'FL028', 'Confirmed', 'Airbus A320', 360.00, 1300.00),
(29, 'Jack Hernandez', '2026-05-15', '12:00 PM', 'Australia', 'USA', 'FL029', 'Confirmed', 'Boeing 787', 730.00, 2600.00),
(30, 'Lily Perez', '2026-05-15', '01:00 PM', 'Korea', 'Australia', 'FL030', 'Confirmed', 'Airbus A330', 470.00, 1700.00),
(31, 'Samuel Wright', '2026-05-16', '02:00 PM', 'USA', 'Philippines', 'FL031', 'Not Confirmed', 'Boeing 777', 680.00, 2400.00),
(32, 'Zoe Campbell', '2026-05-16', '03:00 PM', 'Japan', 'USA', 'FL032', 'Confirmed', 'Airbus A350', 830.00, 3000.00),
(33, 'Andrew Torres', '2026-05-17', '04:00 PM', 'Philippines', 'Korea', 'FL033', 'Confirmed', 'Airbus A321', 350.00, 1280.00),
(34, 'Hannah Mitchell', '2026-05-17', '05:00 PM', 'Australia', 'Japan', 'FL034', 'Confirmed', 'Boeing 777', 700.00, 2450.00),
(35, 'Joseph Roberts', '2026-05-18', '06:00 AM', 'Korea', 'USA', 'FL035', 'Not Confirmed', 'Boeing 787', 770.00, 2780.00),
(36, 'Amelia Carter', '2026-05-18', '07:00 AM', 'USA', 'Japan', 'FL036', 'Confirmed', 'Airbus A350', 850.00, 3100.00),
(37, 'Gabriel Phillips', '2026-05-19', '08:00 AM', 'Japan', 'Philippines', 'FL037', 'Confirmed', 'Airbus A320', 340.00, 1220.00),
(38, 'Charlotte Evans', '2026-05-19', '09:00 AM', 'Australia', 'Korea', 'FL038', 'Confirmed', 'Airbus A330', 460.00, 1680.00),
(39, 'Logan Turner', '2026-05-20', '10:00 AM', 'Philippines', 'Australia', 'FL039', 'Not Confirmed', 'Boeing 737 MAX', 410.00, 1500.00),
(40, 'Harper Collins', '2026-05-20', '11:00 AM', 'Korea', 'Japan', 'FL040', 'Confirmed', 'Boeing 737', 390.00, 1400.00),
(41, 'Sebastian Rivera', '2026-05-21', '12:00 PM', 'USA', 'Australia', 'FL041', 'Confirmed', 'Airbus A350', 840.00, 3050.00),
(42, 'Evelyn Stewart', '2026-05-21', '01:00 PM', 'Japan', 'USA', 'FL042', 'Confirmed', 'Boeing 787', 760.00, 2700.00),
(43, 'David Morris', '2026-05-22', '02:00 PM', 'Philippines', 'Japan', 'FL043', 'Not Confirmed', 'Airbus A321', 355.00, 1320.00),
(44, 'Abigail Rogers', '2026-05-22', '03:00 PM', 'Australia', 'Philippines', 'FL044', 'Confirmed', 'Airbus A320', 430.00, 1550.00),
(45, 'Michael Reed', '2026-05-23', '04:00 PM', 'Korea', 'Australia', 'FL045', 'Confirmed', 'Airbus A330', 480.00, 1730.00),
(46, 'Emily Cook', '2026-05-23', '05:00 PM', 'USA', 'Korea', 'FL046', 'Confirmed', 'Boeing 737 MAX', 510.00, 1820.00),
(47, 'Daniel Morgan', '2026-05-24', '06:00 AM', 'Japan', 'Australia', 'FL047', 'Not Confirmed', 'Boeing 777', 710.00, 2500.00),
(48, 'Sofia Bell', '2026-05-24', '07:00 AM', 'Philippines', 'USA', 'FL048', 'Confirmed', 'Boeing 787', 780.00, 2800.00),
(49, 'Jacob Murphy', '2026-05-25', '08:00 AM', 'Australia', 'Japan', 'FL049', 'Confirmed', 'Airbus A350', 790.00, 2875.00),
(50, 'Isabella Bailey', '2026-05-25', '09:00 AM', 'Korea', 'Philippines', 'FL050', 'Confirmed', 'Airbus A321', 330.00, 1200.00),
(51, 'Joshua Barrera', '2026-06-01', '12:04', 'Philippines', 'Japan', '56', 'Confirmed', 'Boeing 777-300ER', 4600000.00, 17480000.00),
(52, 'Leo', '2026-09-07', '12:32', 'Philippines', 'Australia', '51', 'Confirmed', 'Boeing 747-400', 32000.00, 121600.00);

-- --------------------------------------------------------

--
-- Table structure for table `flight_logs`
--

CREATE TABLE `flight_logs` (
  `id` int(11) NOT NULL,
  `action_type` varchar(20) NOT NULL,
  `flight_no` varchar(50) NOT NULL,
  `passenger_name` varchar(255) NOT NULL,
  `details` text DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `flight_logs`
--

INSERT INTO `flight_logs` (`id`, `action_type`, `flight_no`, `passenger_name`, `details`, `timestamp`) VALUES
(1, 'ADD', '51', 'Leo', 'Flight created. Route: Philippines -> Australia | Date: 2026-09-07 | Aircraft: Boeing 747-400', '2026-05-31 04:33:02');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `flights`
--
ALTER TABLE `flights`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `flight_logs`
--
ALTER TABLE `flight_logs`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `flights`
--
ALTER TABLE `flights`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- AUTO_INCREMENT for table `flight_logs`
--
ALTER TABLE `flight_logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
