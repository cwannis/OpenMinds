-- --------------------------------------------------------
-- Hôte:                         127.0.0.1
-- Version du serveur:           11.4.10-MariaDB-ubu2404 - mariadb.org binary distribution
-- SE du serveur:                debian-linux-gnu
-- HeidiSQL Version:             12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE DATABASE IF NOT EXISTS `openminds` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci */;
USE `openminds`;

CREATE TABLE IF NOT EXISTS `abadge` (
  `idBadge` int(11) DEFAULT NULL,
  `idUser` int(11) DEFAULT NULL,
  KEY `FK__badge` (`idBadge`),
  KEY `FK__user` (`idUser`),
  CONSTRAINT `FK__badge` FOREIGN KEY (`idBadge`) REFERENCES `badge` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK__user` FOREIGN KEY (`idUser`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO `abadge` (`idBadge`, `idUser`) VALUES
	(1, 2);

CREATE TABLE IF NOT EXISTS `badge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titre` text NOT NULL,
  `description` text NOT NULL,
  `imageUrl` text NOT NULL,
  `datePubli` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO `badge` (`id`, `titre`, `description`, `imageUrl`, `datePubli`) VALUES
	(1, 'badge de test', 'c\'est un premier badge de test voila', 'http://10.0.2.2:8080/openMinds/images/badge1.png', '2026-03-04 23:30:57');

-- Listage de la structure de table openminds. formation
CREATE TABLE IF NOT EXISTS `formation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titre` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `datePubli` datetime NOT NULL DEFAULT current_timestamp(),
  `imageUrl` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Listage des données de la table openminds.formation : ~0 rows (environ)
INSERT INTO `formation` (`id`, `titre`, `description`, `datePubli`, `imageUrl`) VALUES
	(1, 'formation test', 'c\'est un test rien de plus', '2026-03-04 14:54:19', 'https://images.ctfassets.net/hrltx12pl8hq/28ECAQiPJZ78hxatLTa7Ts/2f695d869736ae3b0de3e56ceaca3958/free-nature-images.jpg?fit=fill&w=1200&h=630');

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `organization` varchar(255) NOT NULL,
  `ppLink` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO `user` (`id`, `name`, `email`, `password`, `organization`, `ppLink`) VALUES
	(1, 'admin', 'admin@openminds.local', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'OpenMinds', NULL),
	(2, 'yannis', 'wannis1710@gmail.com', 'a94a8fe5ccb19ba61c4c0873d391e987982fbbd3', 'rive de seine', 'https://media.printler.com/media/photo/182985-1.jpg?rmode=crop&width=638&height=900');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
