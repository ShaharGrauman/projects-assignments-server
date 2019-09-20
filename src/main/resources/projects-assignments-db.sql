CREATE DATABASE  IF NOT EXISTS `assignments` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `assignments`;
-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: assignments
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assignment`
--

DROP TABLE IF EXISTS `assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `assignment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `requested_from_manager_id` int(11) NOT NULL,
  `requested_to_manager_id` int(11) DEFAULT NULL,
  `status` enum('Pending approval','Not approved','In progress','Done!') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `employee_id` (`employee_id`),
  KEY `project_id` (`project_id`),
  KEY `requested_from_manager_id` (`requested_from_manager_id`),
  KEY `requested_to_manager_id` (`requested_to_manager_id`),
  CONSTRAINT `assignment_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `users` (`id`),
  CONSTRAINT `assignment_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  CONSTRAINT `assignment_ibfk_3` FOREIGN KEY (`requested_from_manager_id`) REFERENCES `users` (`id`),
  CONSTRAINT `assignment_ibfk_4` FOREIGN KEY (`requested_to_manager_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit`
--

DROP TABLE IF EXISTS `audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employee_number` int(11) NOT NULL,
  `date_time` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  `activity` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employeeskill`
--

DROP TABLE IF EXISTS `employeeskill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `employeeskill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  `manager_id` int(11) DEFAULT NULL,
  `status` enum('PENDING','APPROVED','DECLINED') NOT NULL DEFAULT 'PENDING',
  `level` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `comment` text,
  PRIMARY KEY (`id`),
  KEY `id_idx` (`user_id`),
  KEY `id_idx1` (`skill_id`),
  KEY `Fk_manager_id_idx` (`manager_id`),
  CONSTRAINT `FK_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`),
  CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `Fk_manager_id` FOREIGN KEY (`manager_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `manager_id` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `manager_id` (`manager_id`),
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`manager_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `projectskill`
--

DROP TABLE IF EXISTS `projectskill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `projectskill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  `skill_level` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`),
  KEY `skill_id` (`skill_id`),
  CONSTRAINT `projectskill_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  CONSTRAINT `projectskill_ibfk_2` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rolepermissions`
--

DROP TABLE IF EXISTS `rolepermissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `rolepermissions` (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `permission_id_idx` (`permission_id`),
  KEY `role_permission_id` (`role_id`),
  CONSTRAINT `permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`),
  CONSTRAINT `role_permission_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(450) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `skills`
--

DROP TABLE IF EXISTS `skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `skills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `type` enum('TECHNICAL','PRODUCT') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userrole`
--

DROP TABLE IF EXISTS `userrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `userrole` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `userrole_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `userrole_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employee_number` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `manager_id` int(11) DEFAULT NULL,
  `department` text NOT NULL,
  `work_site_id` int(11) NOT NULL,
  `country` text NOT NULL,
  `phone` text,
  `login_status` bit(1) DEFAULT NULL,
  `locked` bit(1) DEFAULT b'0',
  `deactivated` bit(1) DEFAULT b'0',
  `password` text NOT NULL,
  `image` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `work_site_id` (`work_site_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`work_site_id`) REFERENCES `worksite` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `worksite`
--

DROP TABLE IF EXISTS `worksite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `worksite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `country_id` int(11) NOT NULL,
  `city` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `country_id` (`country_id`),
  CONSTRAINT `country_id` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-20 14:04:42



INSERT INTO `assignments`.`worksite` (`name`, `country_id`, `city`) VALUES ('HiTech Park', '1', 'Nazareth');

INSERT INTO `assignments`.`users` (`employee_number`, `first_name`, `last_name`, `email`, `department`, `work_site_id`, `country`, `phone`, `login_status`, `locked`, `deactivated`, `password`) 
VALUES ('1212', 'Shahar', 'Grauman', 'graumanoz@gmail.com', 'R&D', '1', 'Israel', '0541234567', b'1', b'0', b'0', '123456');

INSERT INTO `assignments`.`users` (`employee_number`, `first_name`, `last_name`, `email`, `department`, `work_site_id`, `country`, `phone`, `login_status`, `locked`, `deactivated`, `password`) 
VALUES ('1414', 'Shahar', 'Grauman', 'graumanoz@yahoo.com', 'R&D', '1', 'Israel', '0541234567', b'1', b'0', b'0', '123456');

INSERT INTO `assignments`.`users` (`employee_number`, `first_name`, `last_name`, `email`, `manager_id`, `department`, `work_site_id`, `country`, `phone`, `login_status`, `locked`, `deactivated`, `password`) 
VALUES ('3232', 'Majd', 'Rizik', 'majdrizik@gmail.com', '1', 'R&D', '1', 'Israel', '0541234568', b'1', b'0', b'0', '123456');

--
-- Dumping data for table assignment
--
LOCK TABLES assignment WRITE;
/!40000 ALTER TABLE assignment DISABLE KEYS /;
INSERT INTO assignment VALUES (1,3,1,'2019-01-01','2019-01-04',1,3,'Done!'),(2,3,1,'2019-08-08','2020-08-08',2,3,'In progress'),(3,3,1,'2015-06-11','2017-06-04',1,3,'Done!'),(4,3,2,'2019-01-01','2019-10-23',3,2,'In progress'),(5,3,2,'2013-09-05','2014-12-04',2,2,'Not approved'),(6,3,2,'2012-12-12','2013-03-13',1,2,'Done!'),(7,3,2,'2007-01-01','2010-01-04',1,2,'Done!'),(8,2,3,'2019-01-01','2019-01-04',3,1,'Not approved'),(9,3,3,'2019-09-01','2020-01-20',1,1,'Done!'),(10,3,1,'2019-08-02','2021-01-20',2,1,'Pending approval');
/!40000 ALTER TABLE assignment ENABLE KEYS /;
UNLOCK TABLES;
--
-- Dumping data for table project
--
LOCK TABLES project WRITE;
/!40000 ALTER TABLE project DISABLE KEYS /;
INSERT INTO project VALUES (2,'Vodafone',1,'asdksaldkas','2019-10-05'),(3,'Skype',1,'alohaaaa','2001-04-15'),(4,'Viber',1,'heloo','2013-07-23'),(5,'whatsApp',1,'hello','2007-12-27'),(6,'Facebook',1,'no description','2009-09-01'),(7,'Instagram',1,'no description','2018-11-25'),(8,'Paypal',1,'-----','2003-12-23'),(9,'Kiwi',1,'hello','2000-11-14'),(10,'Ask.fm',1,'alohaaaa','2019-10-05'),(11,'Postman',1,'allllohaaaa','2019-10-05'),(12,'Waze',1,'-----','2011-06-06'),(17,'Cellcom',2,'asdksajdksad','2019-08-07'),(49,'ccc',2,'asdksaldkas','2019-10-05'),(50,'aaa',2,'asdksaldkas','2019-10-05'),(51,'bbb',2,'asdksaldkas','2019-10-05');
/!40000 ALTER TABLE project ENABLE KEYS /;
UNLOCK TABLES;
--
-- Dumping data for table projectskill
--
LOCK TABLES projectskill WRITE;
/!40000 ALTER TABLE projectskill DISABLE KEYS /;
INSERT INTO projectskill VALUES (1,3,2,5),(2,8,2,5),(3,3,1,4),(4,9,3,5),(5,3,2,5),(6,9,1,4),(7,10,3,3),(8,10,2,5),(9,10,1,4),(10,11,3,3),(11,11,2,5),(12,11,1,4),(13,49,1,4),(14,49,1,3),(15,50,1,4),(16,50,1,3),(18,51,1,4),(19,51,1,3);
/!40000 ALTER TABLE projectskill ENABLE KEYS /;
UNLOCK TABLES;
--
-- Dumping data for table skills
--
LOCK TABLES skills WRITE;
/!40000 ALTER TABLE skills DISABLE KEYS /;
INSERT INTO skills VALUES (1,'Java','TECHNICAL'),(2,'C','TECHNICAL'),(3,'CSS','TECHNICAL'),(4,'JS','TECHNICAL'),(5,'CRM','PRODUCT'),(7,'Asp.net','TECHNICAL'),(8,'TypeScript','TECHNICAL'),(9,'C++','TECHNICAL'),(10,'Testing','PRODUCT');
/!40000 ALTER TABLE skills ENABLE KEYS /;
UNLOCK TABLES;
/!40101 SET SQL_MODE=@OLD_SQL_MODE /;
/!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS /;
/!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS /;
/!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT /;
/!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS /;
/!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION /;
/!40111 SET SQL_NOTES=@OLD_SQL_NOTES /;
-- Dump completed on 2019-09-18 14:42:29
