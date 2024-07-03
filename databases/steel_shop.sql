-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 03, 2024 at 10:01 PM
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
-- Database: `steel_shop`
--

-- --------------------------------------------------------

--
-- Table structure for table `auth`
--

CREATE TABLE `auth` (
  `id` int(10) UNSIGNED NOT NULL,
  `password_hash` char(128) NOT NULL,
  `salt` char(32) NOT NULL,
  `password_reset_token` char(64) DEFAULT NULL,
  `account_status` enum('active','locked','pending_verification') NOT NULL DEFAULT 'pending_verification'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `auth`
--

INSERT INTO `auth` (`id`, `password_hash`, `salt`, `password_reset_token`, `account_status`) VALUES
(1, '$2a$10$iEgSGeCjzW0.HAnqzO4.Vu4jeF0onjSewle7Q8QU/pOqaPgpACOWC', '$2a$10$iEgSGeCjzW0.HAnqzO4.Vu', NULL, 'pending_verification'),
(3, '$2a$10$4gkWnK9fgqoxqgMvMWW.ceCOQPRfNsDIDzZOJnE1VU2v0aBd5VOlW', '$2a$10$4gkWnK9fgqoxqgMvMWW.ce', NULL, 'pending_verification');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `brand` varchar(50) NOT NULL,
  `product_type` varchar(50) NOT NULL,
  `size` varchar(20) DEFAULT NULL,
  `weight` decimal(10,2) DEFAULT NULL,
  `weight_unit` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `brand`, `product_type`, `size`, `weight`, `weight_unit`) VALUES
(1, 'Besi', 'KS', '6 mm x 1,2 x 2,4 M', 140.00, 'kg'),
(2, 'Besi', 'GDS', '6 mm x 1,2 x 2,4 M', 140.00, 'kg'),
(3, 'Besi', 'GG', '6 mm x 1,2 x 2,4 M', 140.00, 'kg'),
(4, 'Besi', 'IBB', '6 mm x 1,2 x 2,4 M', 140.00, 'kg'),
(5, 'Pipa', 'Bakrie', '6 inch', 50.00, 'kg'),
(6, 'Pipa', 'Spindo', '6 inch', 50.00, 'kg'),
(8, 'Pipa', 'China', '6 inch', 140.00, 'kg'),
(480, 'Besi', 'GDS', '6 mm x 1,2 x 2,4 M', 140.00, 'kg'),
(630, 'Besi', 'KS', '6 inch', 50.00, 'kg'),
(865, 'Besi', 'KS', '6 mm x 1,2 x 2,4 M', 140.00, 'kg');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `customer_name` varchar(50) NOT NULL,
  `contact` varchar(50) NOT NULL,
  `address` text NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`customer_id`, `customer_name`, `contact`, `address`, `email`) VALUES
(1, 'PT. Meindo Elang Indah', '08123456789', 'Jl. Raya Jakarta 123', 'meindo@elang.com'),
(2, 'PT. Timas Suplindo', '08123456789', 'Jl. Raya Jakarta 456', 'timas@example.com'),
(3, 'PT. Hume Sakti Indonesia', '08123456789', 'Jl. Raya Jakarta 789', 'hume@example.com'),
(4, 'PT. Pasifik Agro Sentosa Tbk.', '08123456789', 'Jl. Raya Jakarta 901', 'pasifik@example.com'),
(5, 'PT. Inti Ganda Perdana', '08123456789', 'Jl. Raya Jakarta 234', 'inti@example.com'),
(6, 'PT. Bangkitgiat Usaha Mandiri', '08123456789', 'Jl. Raya Jakarta 567', 'bangkitgiat@example.com');

-- --------------------------------------------------------

--
-- Table structure for table `purchasing`
--

CREATE TABLE `purchasing` (
  `purchase_date` date NOT NULL,
  `invoice_number` int(11) NOT NULL,
  `stock_id` int(11) NOT NULL,
  `brand` varchar(50) NOT NULL,
  `product_type` varchar(50) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `sub_total` decimal(10,2) NOT NULL,
  `price_total` decimal(10,2) NOT NULL,
  `supplier_id` int(11) NOT NULL,
  `supplier_name` varchar(50) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `purchasing`
--

INSERT INTO `purchasing` (`purchase_date`, `invoice_number`, `stock_id`, `brand`, `product_type`, `price`, `sub_total`, `price_total`, `supplier_id`, `supplier_name`, `quantity`) VALUES
('2024-06-01', 1, 1, 'Besi', 'KS', 13000.00, 130000.00, 130000.00, 1, 'PT. Pelita Tatamas Jaya', 10),
('2024-06-02', 2, 2, 'Besi', 'GDS', 12000.00, 60000.00, 60000.00, 2, 'PT. Bjamarga Kharsma Utama', 5),
('2024-06-03', 3, 3, 'Besi', 'GG', 11000.00, 220000.00, 220000.00, 3, 'PT. Gracia Abadi', 20),
('2024-06-04', 4, 4, 'Besi', 'IBB', 10000.00, 150000.00, 150000.00, 4, 'PT. Harapan Masa', 15),
('2024-06-05', 5, 5, 'Pipa', 'Bakrie', 14000.00, 112000.00, 112000.00, 5, 'PT. Valconindo Inti Perkasa', 8),
('2024-06-06', 6, 6, 'Pipa', 'Spindo', 13000.00, 156000.00, 156000.00, 6, 'PT. Sinar Bintang', 12),
('2024-07-03', 487, 1, 'Besi', 'KS', 13000.00, 130000.00, 130000.00, 1, 'PT. Pelita Tatamas Jaya', 10),
('2024-07-03', 487, 1, 'Besi', 'KS', 13000.00, 260000.00, 260000.00, 1, 'PT. Pelita Tatamas Jaya', 20),
('2024-07-03', 487, 1, 'Besi', 'KS', 13000.00, 390000.00, 390000.00, 1, 'PT. Pelita Tatamas Jaya', 30),
('2024-07-03', 890, 1, 'Besi', 'KS', 13000.00, 273000.00, 273000.00, 1, 'PT. Pelita Tatamas Jaya', 21),
('2024-07-03', 890, 1, 'Besi', 'KS', 13000.00, 27703000.00, 27703000.00, 1, 'PT. Pelita Tatamas Jaya', 2131),
('2024-07-04', 481, 1, 'Besi', 'KS', 13000.00, 27573000.00, 27573000.00, 1, 'PT. Pelita Tatamas Jaya', 2121),
('2024-07-04', 426, 553, 'Besi', 'IBB', 2313.00, 27756.00, 27756.00, 4, 'PT. Harapan Masa', 12);

-- --------------------------------------------------------

--
-- Table structure for table `returns`
--

CREATE TABLE `returns` (
  `return_date` date NOT NULL,
  `return_id` int(11) NOT NULL,
  `return_type` varchar(15) NOT NULL,
  `invoice_number` int(11) NOT NULL,
  `reason` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `returns`
--

INSERT INTO `returns` (`return_date`, `return_id`, `return_type`, `invoice_number`, `reason`) VALUES
('2024-07-16', 17, 'PEMBELIAN', 1, 'nln'),
('2024-07-16', 18, 'PENJUALAN', 4, 'nln12');

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `sale_date` date NOT NULL,
  `invoice_number` int(11) NOT NULL,
  `stock_id` int(11) NOT NULL,
  `brand` varchar(50) NOT NULL,
  `product_type` varchar(50) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `sub_total` decimal(10,2) NOT NULL,
  `price_total` decimal(10,2) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `customer_name` varchar(50) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sales`
--

INSERT INTO `sales` (`sale_date`, `invoice_number`, `stock_id`, `brand`, `product_type`, `price`, `sub_total`, `price_total`, `customer_id`, `customer_name`, `quantity`) VALUES
('2024-06-07', 1, 1, 'Besi', 'KS', 15000.00, 150000.00, 150000.00, 1, 'PT. Meindo Elang Indah', 10),
('2024-06-08', 2, 2, 'Besi', 'GDS', 14000.00, 70000.00, 70000.00, 2, 'PT. Timas Suplindo', 5),
('2024-06-09', 3, 3, 'Besi', 'GG', 13000.00, 260000.00, 260000.00, 3, 'PT. Hume Sakti Indonesia', 20),
('2024-06-10', 4, 4, 'Besi', 'IBB', 12000.00, 180000.00, 180000.00, 4, 'PT. Pasifik Agro Sentosa Tbk.', 15),
('2024-06-11', 5, 5, 'Pipa', 'Bakrie', 16000.00, 128000.00, 128000.00, 5, 'PT. Inti Ganda Perdana', 8),
('2024-06-12', 6, 6, 'Pipa', 'Spindo', 15000.00, 180000.00, 180000.00, 6, 'PT. Bangkitgiat Usaha Mandiri', 12),
('2024-07-04', 561, 1, 'Besi', 'KS', 15000.00, 180000.00, 180000.00, 1, 'PT. Meindo Elang Indah', 12),
('2024-07-04', 426, 1, 'Besi', 'KS', 15000.00, 315000.00, 315000.00, 4, 'PT. Pasifik Agro Sentosa Tbk.', 21);

-- --------------------------------------------------------

--
-- Table structure for table `stocks`
--

CREATE TABLE `stocks` (
  `stock_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `purchase_price` decimal(10,2) NOT NULL,
  `selling_price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stocks`
--

INSERT INTO `stocks` (`stock_id`, `category_id`, `quantity`, `purchase_price`, `selling_price`) VALUES
(1, 1, 100, 13000.00, 15000.00),
(2, 2, 50, 12000.00, 14000.00),
(3, 3, 200, 11000.00, 13000.00),
(4, 4, 150, 10000.00, 12000.00),
(5, 5, 80, 14000.00, 16000.00),
(6, 6, 120, 13000.00, 15000.00),
(214, 8, 111, 2121.00, 31.00),
(553, 4, 3213, 2313.00, 1231.00);

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_id` int(11) NOT NULL,
  `supplier_name` varchar(50) NOT NULL,
  `contact` varchar(20) NOT NULL,
  `address` text NOT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`supplier_id`, `supplier_name`, `contact`, `address`, `email`) VALUES
(1, 'PT. Pelita Tatamas Jaya', '08123456789', 'Jl. Raya Jakarta 123', 'pelita@jaya.com'),
(2, 'PT. Bjamarga Kharsma Utama', '08123456789', 'Jl. Raya Jakarta 456', 'bjamarga@example.com'),
(3, 'PT. Gracia Abadi', '08123456789', 'Jl. Raya Jakarta 789', 'gracia@example.com'),
(4, 'PT. Harapan Masa', '08123456789', 'Jl. Raya Jakarta 901', 'harapan@example.com'),
(5, 'PT. Valconindo Inti Perkasa', '08123456789', 'Jl. Raya Jakarta 234', 'valconindo@example.com'),
(6, 'PT. Sinar Bintang', '08123456789', 'Jl. Raya Jakarta 567', 'sinarbintang@example.com');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(10) UNSIGNED NOT NULL,
  `username` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(50) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `name`, `created_at`, `updated_at`) VALUES
(1, 'rudiger', 'rioclasher4@gmail.com', 'rudiger', '2024-06-28 16:04:07', '2024-06-28 16:04:07'),
(3, 'admin', 'rio@gmail.com', 'admin', '2024-06-29 13:32:25', '2024-06-29 13:32:25');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `auth`
--
ALTER TABLE `auth`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `purchasing`
--
ALTER TABLE `purchasing`
  ADD KEY `stock_id` (`stock_id`),
  ADD KEY `supplier_id` (`supplier_id`);

--
-- Indexes for table `returns`
--
ALTER TABLE `returns`
  ADD PRIMARY KEY (`return_id`),
  ADD KEY `invoice_number` (`invoice_number`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD KEY `stock_id` (`stock_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `stocks`
--
ALTER TABLE `stocks`
  ADD PRIMARY KEY (`stock_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=984;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=981;

--
-- AUTO_INCREMENT for table `returns`
--
ALTER TABLE `returns`
  MODIFY `return_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `stocks`
--
ALTER TABLE `stocks`
  MODIFY `stock_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=554;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=785;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `auth`
--
ALTER TABLE `auth`
  ADD CONSTRAINT `fk_auth_users` FOREIGN KEY (`id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `purchasing`
--
ALTER TABLE `purchasing`
  ADD CONSTRAINT `purchasing_ibfk_1` FOREIGN KEY (`stock_id`) REFERENCES `stocks` (`stock_id`),
  ADD CONSTRAINT `purchasing_ibfk_2` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`);

--
-- Constraints for table `sales`
--
ALTER TABLE `sales`
  ADD CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`stock_id`) REFERENCES `stocks` (`stock_id`),
  ADD CONSTRAINT `sales_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);

--
-- Constraints for table `stocks`
--
ALTER TABLE `stocks`
  ADD CONSTRAINT `stocks_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
