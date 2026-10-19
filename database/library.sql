-- Library Book Issue Management System
-- Database setup for MySQL 8.x

CREATE DATABASE IF NOT EXISTS Library;
USE Library;

DROP TABLE IF EXISTS `lib`;

CREATE TABLE `lib` (
    `id` INT NOT NULL,
    `Name` VARCHAR(20) NOT NULL,
    `phone` VARCHAR(15) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `address` VARCHAR(255) NOT NULL,
    `book` VARCHAR(50) NOT NULL,
    `Date_of_issue` DATE NOT NULL,
    `Date_of_returning` DATE NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

-- Sample records
-- Replace the sample contact details with real data when testing.

INSERT INTO `lib`
(`id`, `Name`, `phone`, `email`, `address`, `book`,
 `Date_of_issue`, `Date_of_returning`)
VALUES
(1, 'anni', '9000000001', 'anni@example.com', 'Ajmer, Rajasthan',
 'oliver twist', '2025-12-25', '2025-12-28'),

(2, 'anil', '9000000002', 'anil@example.com', 'Ajmer, Rajasthan',
 'Around the world in 80 days', '2025-12-01', '2026-01-01'),

(3, 'anirudh', '9000000003', 'anirudh@example.com', 'Ajmer, Rajasthan',
 'tom sawyer', '2025-08-10', '2026-01-01'),

(4, 'janna', '9000000004', 'janna@example.com', 'Ajmer, Rajasthan',
 'Phantom', '2025-10-01', '2026-02-10'),

(5, 'Kalu', '9000000005', 'kalu@example.com', 'Ajmer, Rajasthan',
 'Fight Club', '2025-09-01', '2026-03-03'),

(9, 'anni', '9000000006', 'anni2@example.com', 'Ajmer, Rajasthan',
 'harry potter', '2025-09-12', '2026-04-11');
