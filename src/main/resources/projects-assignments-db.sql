CREATE DATABASE  IF NOT EXISTS `assignments` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `assignments`;

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


DROP TABLE IF EXISTS `assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `assignment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date DEFAULT NULL,
  `requested_from_manager_id` int(11) NOT NULL,
  `requested_to_manager_id` int(11) DEFAULT NULL,
  `status` enum('PENDING_APPROVAL','NOT_APPROVED','IN_PROGRESS','DONE') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `employee_id` (`employee_id`),
  KEY `project_id` (`project_id`),
  KEY `requested_from_manager_id` (`requested_from_manager_id`),
  KEY `requested_to_manager_id` (`requested_to_manager_id`),
  CONSTRAINT `assignment_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `users` (`id`),
  CONSTRAINT `assignment_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  CONSTRAINT `assignment_ibfk_3` FOREIGN KEY (`requested_from_manager_id`) REFERENCES `users` (`id`),
  CONSTRAINT `assignment_ibfk_4` FOREIGN KEY (`requested_to_manager_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=164 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;




DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `department_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;




DROP TABLE IF EXISTS `employeeskill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `employeeskill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  `manager_id` int(11) NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=783 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;




DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;




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
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



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
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;




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



DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(450) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



DROP TABLE IF EXISTS `skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `skills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `type` enum('TECHNICAL','PRODUCT') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



DROP TABLE IF EXISTS `userrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `userrole` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `userrole_ibfk_2_idx` (`role_id`),
  CONSTRAINT `userrole_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `userrole_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



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
  UNIQUE KEY `employee_number_UNIQUE` (`employee_number`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `work_site_id` (`work_site_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`work_site_id`) REFERENCES `worksite` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



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
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;




DROP TABLE IF EXISTS login;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE login (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(45) NOT NULL,
  `attempts` int(11) NOT NULL,
  `last_attempt_time` datetime DEFAULT NULL,
  PRIMARY KEY (id),
  KEY id_idx (user_id),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`),
  CONSTRAINT FK_login_user_id FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--
LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,'Israel'),(2,'Mexico'),(3,'Canada'),(4,'Michigan'),(5,'New Jersey'),(6,'Pennsylvania'),(7,'California'),(8,'Brazil'),(9,'Georgia'),(10,'Virginia'),(11,'Illinois'),(12,'Washington'),(13,'Australia'),(14,'India'),(15,'Philippines'),(16,'China'),(17,'Singapore'),(18,'Taiwan'),(19,'Japan'),(20,'Korea'),(21,'Thailand'),(22,'Vietnam'),(23,'Austria'),(24,'South Africa'),(25,'Cyprus'),(26,'Italy'),(27,'Spain'),(28,'Kazakhstan'),(29,'Czech Republic'),(30,'Netherlands'),(31,'United Arab Emirates - Dubai'),(32,'United Kingdom'),(33,'Poland'),(34,'France'),(35,'Russia'),(36,'Germany'),(37,'Ireland');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `department`
--
LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'R&D'),(2,'QA'),(3,'HR'),(4,'NOC'),(5,'Human Resources'),(6,'Research And Development'),(7,'QQA');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'admin','An Administrator provides office and administrative support to either a team or individual. This role is vital for the smooth-running of a business. Duties may include fielding telephone calls, receiving and directing visitors, word processing, creating spreadsheets and presentations, and filing.'),(2,'manager','The manager is responsible for overseeing and leading the work of a group of people in many instances. The manager is also responsible for planning and maintaining work systems, procedures, and policies that enable and encourage the optimum performance of its people and other resources within a business unit.'),(3,'employee','Employee job descriptions are written statements that describe the duties, responsibilities, required qualifications and reporting relationships of a particular job. ... Effectively developed, employee job descriptions are communication tools that are significant to your organization\'s success'),(4,'HR manager','HR managing position'),(5,'intern','intern description'),(6,'Team Leader','Can Assign&Skill  and Approve Assign&Skills');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES (1,'AddNewUser'),(2,'UpdateUser'),(3,'approveSkill'),(4,'AddSkill'),(5,'AddAssignments'),(6,'AddProject');
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `rolepermissions`
--

LOCK TABLES `rolepermissions` WRITE;
/*!40000 ALTER TABLE `rolepermissions` DISABLE KEYS */;
INSERT INTO `rolepermissions` VALUES (1,1),(1,2),(1,3),(2,3),(1,4),(2,4),(3,4),(1,5),(2,5),(1,6),(2,6);
/*!40000 ALTER TABLE `rolepermissions` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping data for table `skills`
--

LOCK TABLES `skills` WRITE;
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
INSERT INTO `skills` VALUES (1,'Java','TECHNICAL'),(2,'C','TECHNICAL'),(3,'CSS','TECHNICAL'),(4,'JS','TECHNICAL'),(5,'Python','TECHNICAL'),(7,'Asp.net','TECHNICAL'),(8,'TypeScript','TECHNICAL'),(9,'C++','TECHNICAL'),(10,'React','TECHNICAL'),(11,'AngularJs','TECHNICAL'),(12,'HTML5','TECHNICAL'),(13,'PHP','TECHNICAL'),(14,'C#','TECHNICAL'),(15,'Swift','TECHNICAL'),(16,'CRM','PRODUCT'),(17,'Biling','PRODUCT'),(18,'Team Player','PRODUCT'),(19,'Managment','PRODUCT'),(20,'OMS','PRODUCT');
/*!40000 ALTER TABLE `skills` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping data for table `worksite`
--
LOCK TABLES `worksite` WRITE;
/*!40000 ALTER TABLE `worksite` DISABLE KEYS */;
INSERT INTO `worksite` VALUES (1,'HiTech Park',1,'Nazareth'),(2,'HaPnina',1,'Ra\'anana'),(3,'Kefar Saba',1,'Kefar Sava'),(4,'Nazareth',1,'Nazareth'),(5,'Ejercito Nacional',2,'Palmas Polanco'),(6,'Av Rafael Sanzio',2,'Camichines Vallarta'),(7,'Tech Avenue',3,'Ontario'),(8,'Terry Fox Drive, Suite',3,'Ottawa'),(9,'Eglinton Ave',3,'Michigan'),(10,'Livernois',4,'Michigan'),(11,'Hudson Street',5,'Jersey City'),(12,'Logan',6,' Philadelphia'),(13,'Investment Boulevard',7,'El Dorado Hills'),(14,'Amdocs Rio',8,'Rio de Janeiro'),(15,'Amdocs Sao Paulo',8,'Sao Paulo'),(16,'Atlanta-Rainwater',9,'Alpharetta'),(17,'Dulles View Dr',10,'Herndon'),(18,'Fox Drive',11,'Champaign'),(19,'Elliott Avenu',12,'Seattle'),(20,'Surrey Hills Data Centre',13,'Surrey Hills'),(21,'Collins Street',13,'Melbourne'),(22,'CyberCity Tower',14,'Pune'),(23,' Raheja Centre Point',14,'Mumbai'),(24,'Tower C',14,'Gurgaon'),(25,'Net One Center Bldg',15,'Taguig'),(26,'The Imperial Hotel Tower',19,'Tokyo');
/*!40000 ALTER TABLE `worksite` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,1,'Admin','Admin','Admin@gmail.com',1,'R&D',1,'Israel','0541234567',_binary '',_binary '\0',_binary '\0','123456','path'),(2,2,'Shahar','Grauman','graumanoz2@gmail.com',2,'R&D',1,'Israel','0541234567',_binary '',_binary '\0',_binary '','123456','path'),(3,3,'Jeries','Zamel','majdrizik@gmail.com',3,'R&D',1,'Israel','0541234568',_binary '',_binary '\0',_binary '\0','123456','path'),(4,4,'Fadi','Mohamad','S.Lilian1@amdocs.com',4,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '','123456','path'),(5,5,'Lama','Azaizi','S.Lilian2@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(6,6,'Lilian','Shaer','S.Lilian3@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(7,7,'Emad','Silawi','S.Lilian4@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(8,8,'Chris','Issa','issa@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(9,9,'Wisam ','Sa5nene','fadi@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '','123456','path'),(10,10,'Jeries','Zamel','zamel@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(11,11,'Amjad','Nassar','nassar@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(12,12,'Lama','Azaiza','azaiza@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(13,13,'Majd','Nassar','nassarm@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(14,14,'omair','Issa','issao@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(15,15,'Raneem','Daher','daher@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(16,16,'Amani','Espanioly','espanioly@amdocs.com',2,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(17,17,'Ezer','Biron','biron@amdocs.com',3,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(18,18,'Elias','Nijim','nijim@amdocs.com',3,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(19,19,'Sami','Abishai','abishi@amdocs.com',3,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(20,20,'Saheer','Jacob','jacob@amdocs.com',3,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(21,21,'Tareq','Arafat','arafat@amdocs.com',3,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(22,22,'Tareq','Husein','husein@amdocs.com',3,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(23,23,'Walaa','Hamzy','hamzy@amdocs.com',3,'R&D',1,'Israel','0527891111',_binary '\0',_binary '\0',_binary '\0','123456','path'),(24,24,'Rayah','Shaer','S.Lilian5@amdocs.com',3,'QA',1,'Israel','05279999999',_binary '\0',_binary '\0',_binary '\0','123456','path'),(25,25,'Raya2','Shaer2','S.Lilian@amdocs.com',3,'QA',1,'Israel','05279999999',_binary '\0',_binary '\0',_binary '\0','123456','path'),(26,26,'Laheeb','Maaruf','bkjfe@gmail.com',3,'QA',1,'Israel','123456987',_binary '\0',_binary '\0',_binary '\0','123456','path'),(27,27,'da','fe','Fadi2@gmail.com',3,'QA',1,'Israel','123542',_binary '\0',_binary '\0',_binary '\0','123456','path'),(28,28,'fr','rfe','gfh@gmal.com',3,'HR',1,'Israel','fd',_binary '\0',_binary '\0',_binary '\0','123456','path'),(29,29,'FadiTest','MuhammadTest','fad2i@gmail.com',4,'QA',1,'Israel','123456798',_binary '\0',_binary '\0',_binary '','123456',NULL),(30,30,'lala','lala','lala@gmal.com',4,'HR',1,'Israel','',_binary '\0',_binary '\0',_binary '\0','123456',NULL),(31,31,'Test1','TestFamily1','Test@gmail.com',4,'QA',1,'Israel','054444444',_binary '\0',_binary '\0',_binary '\0','123456',NULL),(32,32,'John','Snow','john2@hotmail.com',4,'R&D',1,'Israel','123456456',_binary '\0',_binary '\0',_binary '\0','123456',NULL),(33,33,'John2','Snow2','john@hotmail.com',4,'R&D',1,'Israel','123456456',_binary '\0',_binary '\0',_binary '','123456',NULL),(34,34,'some user','some user family','1231321@gmail.com',4,'R&D',1,'Israel','789654456',_binary '\0',_binary '\0',_binary '','123456',NULL),(35,35,'Sasha','Lambert','Sasha_Lambert1579@cispeto.com',4,'R&D',1,'Israel',NULL,NULL,_binary '\0',_binary '\0','123456',NULL),(36,36,'Ronald','Campbell','Ronald_Campbell3895@fuliss.net',4,'R&D',1,'Israel',NULL,NULL,_binary '\0',_binary '\0','123456',NULL),(37,37,'Aurelia','Sanchez','Aurelia_Sanchez26@cispeto.com',4,'R&D',1,'Israel',NULL,NULL,_binary '\0',_binary '\0','123456',NULL),(38,38,'Moira','Murphy','Moira_Murphy5612@gompie.com',4,'R&D',1,'Israel',NULL,NULL,_binary '\0',_binary '\0','123456',NULL),(39,39,'Eduardo','Craig','Eduardo_Craig7237@bretoux.com',4,'R&D',1,'Israel',NULL,NULL,_binary '\0',_binary '\0','123456',NULL),(40,40,'Barney','Potts','Barney_Potts1846@hourpy.biz',4,'R&D',1,'Israel',NULL,NULL,_binary '\0',_binary '\0','123456',NULL),(41,1150,'Carla','Bowen','Carla_Bowen9374@supunk.biz',4,'R&D',1,'Israel',NULL,NULL,_binary '\0',_binary '\0','123456',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `project`
--
LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,'DocuStar Documentation Services',2,'Design and production of comprehensive software and hardware documentation, primarily for Internet, communication and multimedia products.','2012-02-02'),(2,'Vodafone',2,'Additional list of companies that produce software development tools that can be used in automotive design.','2013-01-01'),(3,'Skype',2,'Searchable and indexed database of computer and electronics related jobs in Israel.','2014-03-03'),(4,'Viber',3,'Developer of Adobe Photoshop plug-ins. Puzzle Pro - plug-in to generate jigsaw puzzles. Page Curl - plug-in to generate realistic turning page effects','2015-05-05'),(5,'whatsApp',3,'Software company that implements and integrates BI, CRM and ERP systems in JAVA, SAP, Oracle and Microsoft environments. ActionBase, tool for decision follow-up and task management.','2016-04-04'),(6,'Facebook',3,'Agile/Lean solutions to improve project management, software and hardware development. Training for Kanban method of developing software products.','2017-07-07'),(7,'Instagram',2,'ERP, CRM and MRP systems for medium to large companies.','2018-08-08'),(8,'Paypal',2,'Custom business solutions for accounting management, salary payroll, human resources designed for IBM AS/400 systems. Represents Informatic in Israel.','2019-09-09'),(9,'Kiwi',4,'Custom projects in the fields of management engineering; information systems; behavioral sciences; economics and business development','2019-05-05'),(10,'Ask.fm',4,'Custom development of mobile phone applications. Web solutions.','2019-04-02'),(11,'Postman',2,'Development of interactive multimedia projects, kiosks, training (CBT), web graphics, courseware, presentations. Technical illustrations, simulations, animation.','2012-01-01'),(12,'Waze',2,'Attunity B2B enables enterprises to create, share, automate and manage business processes that integrate customers, suppliers, partners, eMarketplaces, employees and enterprise systems over the Internet.','2013-01-02'),(13,'ACTL Systems',2,'Software house, specializing in complex voice, VoIP, video systems. Real time software, embedded software. Sub contracting and system engineering projects for High Tech companies','2014-05-02'),(14,'Babylon.com',2,'CARE software tools to test reliability and maintenance of CAD / CAE / CAM designs of complex systems such as: railways, automotive, telecommunications, production lines, power stations, aircraft, ships.','2015-05-05'),(15,'Beyond Security ',2,'Automated Scanning service to locate security holes in hosts and networks, exposing vulnerabilities in the corporate network. ','2016-06-06'),(16,'Britannica Knowledge Systems',2,'e-Learning solutions for the professional development market. HighLearn - a virtual campus that is designed for institutions of higher education and also addresses professional development needs.','2017-07-07'),(17,'Callflow Software ',2,'Syncro suite - software for analyzing customer service processes, providing real-time monitoring, alerts and statistics. Customer service management in large contact centers and back offices.','2018-08-08'),(18,'Cardonet',2,'Comprehensive tools for the development and management of procurement, supplier and e-marketplace catalogs.','2019-09-09'),(19,'Celebros',2,'Localization of information in vast databases using novel artificial intelligence algorithms.','2019-02-07'),(20,'CerEm JVM Software ',2,'CrE-ME Java Virtual Machine is the Industry Standard JVM for Windows Mobile and Windows CE (WM/CE) based products.','2019-08-07'),(21,'Cimatron ',3,'Data-to-Steel Solution: integrated CAD/CAM solutions for mold, tool and die makers.','2012-02-02'),(22,'ClickWise',3,'Web marketing strategies. Search Engine Optimization (SEO).','2013-03-03'),(23,'Comda ',3,'Integrator of IT security solutions. Daughter firm: ComSign. Establishment of public key infrastructure','2014-04-04'),(24,'Complete Information Technologies ',3,'Bioinformatics software tools for analysis of genomic and protein sequences to extract useful information from genomic and protein data.','2015-05-05'),(25,'Comsec Group',3,'Global software development service provider in Marketing and AdTech, E-Commerce, FinTech, HealthTech industries','2016-06-06'),(26,'ConKor Systems',3,'Development of embedded, Real-Time, object-oriented software packages. Equipment control software, GUI design.','2017-07-07'),(27,'Creambit Systems ',3,'Internet payment and e-commerce solutions for online management of billing and auditing services.','2018-08-08'),(28,'Cubus Engineering Software ',3,'Software for civil engineering. Tunnel-4H: design of Concrete Box Culverts using AutoCAD','2019-09-09'),(29,'DataBank',3,'Storage and management of corporate computer back up tapes. Offsite tape storage and transportation.','2019-07-08'),(30,'Davka Writer',3,'Developer of Judaic software for Jewish history, customs and traditions, Hebrew/English word processor, educational games, encyclopedia, Jewish clipart and graphics, Hebrew fonts. ','2019-06-08');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `projectskill`
--

LOCK TABLES `projectskill` WRITE;
/*!40000 ALTER TABLE `projectskill` DISABLE KEYS */;
INSERT INTO `projectskill` VALUES (1,1,1,4),(2,1,2,3),(3,1,3,5),(4,1,16,3),(5,1,17,4),(6,2,4,5),(7,2,3,4),(8,2,5,3),(9,2,2,1),(10,2,18,3),(11,2,20,5),(12,3,5,4),(13,3,8,4),(14,3,7,3),(15,3,13,4),(16,3,15,3),(18,3,16,4),(19,3,19,3),(20,4,1,4),(21,4,17,3),(40,4,20,5),(41,4,9,3),(42,4,10,4),(43,4,11,3),(44,5,4,5),(45,5,13,3),(46,5,8,5),(47,5,14,4),(48,5,16,4),(49,5,2,3),(50,6,1,3),(51,6,3,3),(52,6,5,4),(53,6,7,5),(54,6,8,4),(55,6,10,3),(56,6,16,2),(57,6,18,4),(58,7,2,2),(59,7,4,3),(60,7,7,3),(61,7,10,4),(62,7,11,5),(63,7,14,5),(64,7,17,4),(65,7,18,3),(72,8,9,2),(73,8,19,3),(74,8,12,5),(75,8,14,3),(76,8,15,4),(77,8,17,3),(78,8,19,4),(88,9,17,2),(89,9,18,3),(90,9,9,4),(91,9,7,5),(92,9,7,4),(93,9,5,5),(94,9,4,3),(95,9,3,3),(96,9,2,3);
/*!40000 ALTER TABLE `projectskill` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `assignment`
--
LOCK TABLES `assignment` WRITE;
/*!40000 ALTER TABLE `assignment` DISABLE KEYS */;
INSERT INTO `assignment` VALUES (1,1,5,'2019-01-01','2019-12-12',2,2,'IN_PROGRESS'),(2,1,6,'2019-08-08','2020-05-05',2,2,'IN_PROGRESS'),(3,1,7,'2015-06-11','2022-05-05',2,2,'IN_PROGRESS'),(4,2,8,'2019-01-01','2021-08-08',2,2,'IN_PROGRESS'),(5,4,9,'2013-09-05','2014-12-04',3,2,'PENDING_APPROVAL'),(6,2,10,'2012-12-12','2013-03-13',2,2,'IN_PROGRESS'),(7,2,11,'2007-01-01','2010-01-04',2,2,'IN_PROGRESS'),(8,3,12,'2019-01-01','2019-01-04',2,2,'IN_PROGRESS'),(9,3,13,'2019-09-01','2020-01-20',2,2,'IN_PROGRESS'),(10,3,14,'2019-08-02','2021-01-20',2,2,'IN_PROGRESS'),(20,3,15,'2019-09-21','2021-01-20',2,3,'IN_PROGRESS'),(21,1,16,'2019-09-21','2021-01-20',2,3,'PENDING_APPROVAL'),(24,4,17,'2019-09-21',NULL,2,3,'PENDING_APPROVAL'),(25,4,18,'2019-09-21',NULL,3,NULL,'IN_PROGRESS'),(26,4,19,'2019-09-21',NULL,3,NULL,'IN_PROGRESS'),(27,5,20,'2019-09-21',NULL,3,NULL,'IN_PROGRESS'),(28,5,21,'2019-09-21',NULL,3,NULL,'IN_PROGRESS'),(29,5,22,'2019-09-21',NULL,3,NULL,'IN_PROGRESS'),(30,6,24,'2019-09-21',NULL,3,NULL,'IN_PROGRESS'),(31,6,12,'2019-09-21',NULL,3,NULL,'IN_PROGRESS'),(32,6,26,'2019-09-21',NULL,3,NULL,'IN_PROGRESS'),(33,7,26,'2019-09-21',NULL,3,4,'PENDING_APPROVAL'),(34,7,27,'2019-09-21',NULL,3,4,'PENDING_APPROVAL'),(35,7,8,'2019-09-21',NULL,4,2,'PENDING_APPROVAL'),(36,7,29,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(37,7,30,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(38,7,31,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(39,8,32,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(40,8,33,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(41,8,34,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(42,9,34,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(43,9,35,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(44,9,36,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(45,9,37,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(46,10,39,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(47,10,40,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(48,10,41,'2019-09-21',NULL,4,NULL,'IN_PROGRESS'),(49,11,5,'2019-01-01','2019-12-12',4,2,'DONE'),(50,11,6,'2019-08-08','2020-05-05',2,2,'DONE'),(51,11,7,'2015-06-11','2022-05-05',2,2,'DONE'),(52,12,8,'2019-01-01','2021-08-08',2,2,'DONE'),(53,12,9,'2013-09-05','2014-12-04',3,2,'DONE'),(54,12,10,'2012-12-12','2013-03-13',2,2,'DONE'),(55,12,11,'2007-01-01','2010-01-04',2,2,'DONE'),(56,14,12,'2019-01-01','2019-01-04',2,2,'DONE'),(57,14,13,'2019-09-01','2020-01-20',2,2,'DONE'),(58,14,14,'2019-08-02','2021-01-20',2,2,'DONE'),(59,13,15,'2019-09-21','2021-01-20',2,3,'DONE'),(60,13,16,'2019-09-21','2021-01-20',2,3,'DONE'),(61,13,17,'2019-09-21',NULL,2,3,'DONE'),(62,13,18,'2019-09-21',NULL,3,NULL,'DONE'),(63,15,19,'2019-09-21',NULL,3,NULL,'DONE'),(64,16,20,'2019-09-21',NULL,3,NULL,'DONE'),(65,17,21,'2019-09-21',NULL,3,NULL,'DONE'),(66,16,22,'2019-09-21',NULL,3,NULL,'DONE'),(67,17,24,'2019-09-21',NULL,3,NULL,'DONE'),(68,18,25,'2019-09-21',NULL,3,NULL,'DONE'),(69,19,26,'2019-09-21',NULL,3,NULL,'DONE'),(70,19,26,'2019-09-21',NULL,3,4,'DONE'),(71,20,27,'2019-09-21',NULL,3,4,'DONE'),(72,21,7,'2019-09-21',NULL,4,2,'PENDING_APPROVAL'),(73,20,29,'2019-09-21',NULL,4,NULL,'DONE'),(74,21,30,'2019-09-21',NULL,4,NULL,'DONE'),(75,21,31,'2019-09-21',NULL,4,NULL,'DONE'),(76,22,32,'2019-09-21',NULL,4,NULL,'DONE'),(77,23,33,'2019-09-21',NULL,4,NULL,'DONE'),(78,23,34,'2019-09-21',NULL,4,NULL,'DONE'),(79,22,34,'2019-09-21',NULL,4,NULL,'DONE'),(80,23,35,'2019-09-21',NULL,4,NULL,'DONE'),(81,25,36,'2019-09-21',NULL,4,NULL,'DONE'),(82,26,37,'2019-09-21',NULL,4,NULL,'DONE'),(83,27,39,'2019-09-21',NULL,4,NULL,'DONE'),(84,26,40,'2019-09-21',NULL,4,NULL,'DONE'),(85,28,41,'2019-09-21',NULL,4,NULL,'DONE'),(86,29,5,'2019-01-01','2019-12-12',2,2,'DONE'),(87,30,6,'2019-08-08','2020-05-05',2,2,'DONE'),(88,30,7,'2015-06-11','2022-05-05',2,2,'DONE'),(89,29,8,'2019-01-01','2021-08-08',2,2,'DONE'),(90,28,9,'2013-09-05','2014-12-04',3,2,'DONE'),(91,27,10,'2012-12-12','2013-03-13',2,2,'DONE'),(92,26,11,'2007-01-01','2010-01-04',2,2,'DONE'),(93,25,12,'2019-01-01','2019-01-04',2,2,'DONE'),(94,24,13,'2019-09-01','2020-01-20',2,2,'DONE'),(95,23,14,'2019-08-02','2021-01-20',2,2,'DONE'),(96,22,15,'2019-09-21','2021-01-20',2,3,'DONE'),(97,21,16,'2019-09-21','2021-01-20',2,3,'DONE'),(98,20,17,'2019-09-21',NULL,2,3,'DONE'),(99,19,18,'2019-09-21',NULL,3,NULL,'DONE'),(100,18,19,'2019-09-21',NULL,3,NULL,'DONE'),(101,17,20,'2019-09-21',NULL,3,NULL,'DONE'),(102,16,21,'2019-09-21',NULL,3,NULL,'DONE'),(103,15,22,'2019-09-21',NULL,3,NULL,'DONE'),(104,14,24,'2019-09-21',NULL,3,NULL,'DONE'),(105,14,25,'2019-09-21',NULL,3,NULL,'DONE'),(106,13,26,'2019-09-21',NULL,3,NULL,'DONE'),(107,12,26,'2019-09-21',NULL,3,4,'DONE'),(108,12,27,'2019-09-21',NULL,3,4,'DONE'),(109,15,6,'2019-09-21',NULL,4,2,'PENDING_APPROVAL'),(110,11,29,'2019-09-21',NULL,4,NULL,'DONE'),(111,10,30,'2019-09-21',NULL,4,NULL,'DONE'),(112,11,31,'2019-09-21',NULL,4,NULL,'DONE'),(113,15,32,'2019-09-21',NULL,4,NULL,'DONE'),(114,25,33,'2019-09-21',NULL,4,NULL,'DONE'),(115,22,34,'2019-09-21',NULL,4,NULL,'DONE'),(116,23,34,'2019-09-21',NULL,4,NULL,'DONE'),(117,29,35,'2019-09-21',NULL,4,NULL,'DONE'),(118,20,36,'2019-09-21',NULL,4,NULL,'DONE'),(119,19,37,'2019-09-21',NULL,4,NULL,'DONE'),(120,18,39,'2019-09-21',NULL,4,NULL,'DONE'),(121,19,40,'2019-09-21',NULL,4,NULL,'DONE'),(122,20,41,'2019-09-21',NULL,4,NULL,'DONE'),(123,11,5,'2019-01-01','2019-12-12',2,3,'NOT_APPROVED'),(124,11,6,'2019-08-08','2020-05-05',2,3,'NOT_APPROVED'),(125,11,7,'2015-06-11','2022-05-05',2,3,'NOT_APPROVED'),(126,12,8,'2019-01-01','2021-08-08',2,4,'NOT_APPROVED'),(127,12,9,'2013-09-05','2014-12-04',3,4,'NOT_APPROVED'),(128,12,10,'2012-12-12','2013-03-13',2,3,'NOT_APPROVED'),(129,12,11,'2007-01-01','2010-01-04',2,4,'NOT_APPROVED'),(130,14,12,'2019-01-01','2019-01-04',2,3,'NOT_APPROVED'),(131,14,13,'2019-09-01','2020-01-20',2,4,'NOT_APPROVED'),(132,14,14,'2019-08-02','2021-01-20',2,3,'NOT_APPROVED'),(133,13,15,'2019-09-21','2021-01-20',2,4,'NOT_APPROVED'),(134,13,16,'2019-09-21','2021-01-20',2,3,'NOT_APPROVED'),(135,13,17,'2019-09-21',NULL,2,4,'NOT_APPROVED'),(136,13,18,'2019-09-21',NULL,3,4,'NOT_APPROVED'),(137,15,19,'2019-09-21',NULL,3,2,'NOT_APPROVED'),(138,16,20,'2019-09-21',NULL,3,4,'NOT_APPROVED'),(139,17,21,'2019-09-21',NULL,3,4,'NOT_APPROVED'),(140,16,22,'2019-09-21',NULL,3,2,'NOT_APPROVED'),(141,17,24,'2019-09-21',NULL,3,4,'NOT_APPROVED'),(142,18,25,'2019-09-21',NULL,3,2,'NOT_APPROVED'),(143,19,26,'2019-09-21',NULL,3,4,'NOT_APPROVED'),(144,19,26,'2019-09-21',NULL,3,2,'NOT_APPROVED'),(145,20,27,'2019-09-21',NULL,3,4,'NOT_APPROVED'),(146,21,5,'2019-09-21',NULL,4,2,'NOT_APPROVED'),(147,20,29,'2019-09-21',NULL,4,3,'NOT_APPROVED'),(148,21,30,'2019-09-21',NULL,4,2,'NOT_APPROVED'),(149,21,31,'2019-09-21',NULL,4,3,'NOT_APPROVED'),(150,22,32,'2019-09-21',NULL,4,3,'NOT_APPROVED'),(151,23,33,'2019-09-21',NULL,4,2,'NOT_APPROVED'),(152,23,34,'2019-09-21',NULL,4,3,'NOT_APPROVED'),(153,22,34,'2019-09-21',NULL,4,2,'NOT_APPROVED'),(154,23,35,'2019-09-21',NULL,4,3,'NOT_APPROVED'),(155,25,36,'2019-09-21',NULL,4,3,'NOT_APPROVED'),(156,26,37,'2019-09-21',NULL,4,2,'NOT_APPROVED'),(157,27,39,'2019-09-21',NULL,4,3,'NOT_APPROVED'),(158,26,40,'2019-09-21',NULL,4,2,'NOT_APPROVED'),(159,28,41,'2019-09-21',NULL,4,3,'NOT_APPROVED'),(160,7,41,'2019-09-22',NULL,1,4,'PENDING_APPROVAL'),(161,7,5,'2019-09-21',NULL,4,2,'PENDING_APPROVAL'),(162,15,26,'2019-09-22',NULL,3,4,'PENDING_APPROVAL');
/*!40000 ALTER TABLE `assignment` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `employeeskill`
--
LOCK TABLES `employeeskill` WRITE;
/*!40000 ALTER TABLE `employeeskill` DISABLE KEYS */;
INSERT INTO `employeeskill` VALUES (1,5,1,1,'APPROVED',5,'2019-04-01 00:00:00','Good'),(2,6,2,1,'APPROVED',5,'2019-04-01 00:00:00','Good'),(3,7,3,1,'APPROVED',5,'2019-04-01 00:00:00','Good'),(4,8,4,1,'APPROVED',5,'2019-04-01 00:00:00','Good'),(5,9,1,1,'APPROVED',5,'2019-04-01 00:00:00','Good'),(6,10,12,1,'APPROVED',5,'2019-04-01 00:00:00','Good'),(7,11,7,1,'APPROVED',5,'2019-04-01 00:00:00','Good'),(8,12,1,1,'APPROVED',5,'2019-04-01 00:00:00','Good'),(9,13,2,1,'APPROVED',5,'2019-04-01 00:00:00','Good'),(10,14,3,1,'APPROVED',5,'2019-04-01 00:00:00','Good'),(11,15,8,1,'APPROVED',4,'2019-04-01 00:00:00','Good'),(12,16,9,1,'APPROVED',4,'2019-04-01 00:00:00','Good'),(13,17,1,1,'APPROVED',4,'2019-04-01 00:00:00','Good'),(14,18,2,1,'APPROVED',4,'2019-04-01 00:00:00','Good'),(15,19,10,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(16,20,12,1,'APPROVED',4,'2019-04-01 00:00:00','Good'),(17,21,12,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(18,22,10,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(19,23,1,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(20,24,4,1,'APPROVED',4,'2019-04-01 00:00:00','Good'),(21,25,12,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(22,26,10,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(23,27,3,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(24,28,4,1,'APPROVED',4,'2019-04-01 00:00:00','Good'),(25,29,9,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(26,30,5,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(27,31,5,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(28,32,10,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(29,33,11,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(30,34,12,1,'APPROVED',4,'2019-04-01 00:00:00','Good'),(31,35,5,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(32,36,12,1,'APPROVED',2,'2019-04-01 00:00:00','Good'),(633,40,4,4,'APPROVED',5,'2019-04-01 00:00:00',NULL),(634,35,2,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(635,32,3,4,'APPROVED',2,'2019-04-01 00:00:00',NULL),(636,39,12,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(637,32,5,4,'APPROVED',2,'2019-04-01 00:00:00',NULL),(638,38,17,4,'APPROVED',1,'2019-04-01 00:00:00',NULL),(639,30,8,4,'APPROVED',1,'2019-04-01 00:00:00',NULL),(640,36,3,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(641,34,9,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(642,41,10,4,'APPROVED',5,'2019-04-01 00:00:00',NULL),(643,32,5,4,'APPROVED',5,'2019-04-01 00:00:00',NULL),(644,29,15,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(645,40,20,4,'APPROVED',2,'2019-04-01 00:00:00',NULL),(646,30,18,4,'APPROVED',1,'2019-04-01 00:00:00',NULL),(647,36,10,4,'APPROVED',1,'2019-04-01 00:00:00',NULL),(648,38,11,4,'APPROVED',1,'2019-04-01 00:00:00',NULL),(649,35,13,4,'APPROVED',2,'2019-04-01 00:00:00',NULL),(650,32,7,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(651,38,10,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(652,35,16,4,'APPROVED',2,'2019-04-01 00:00:00',NULL),(653,40,14,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(654,41,7,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(655,39,5,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(656,36,8,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(657,29,4,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(658,39,4,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(659,30,9,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(660,39,2,4,'APPROVED',5,'2019-04-01 00:00:00',NULL),(661,35,11,4,'APPROVED',1,'2019-04-01 00:00:00',NULL),(662,33,12,4,'APPROVED',2,'2019-04-01 00:00:00',NULL),(663,31,8,4,'APPROVED',1,'2019-04-01 00:00:00',NULL),(664,34,8,4,'APPROVED',2,'2019-04-01 00:00:00',NULL),(665,32,7,4,'APPROVED',2,'2019-04-01 00:00:00',NULL),(666,34,12,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(667,31,12,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(668,38,14,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(669,37,1,4,'APPROVED',1,'2019-04-01 00:00:00',NULL),(670,30,3,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(671,41,2,4,'APPROVED',2,'2019-04-01 00:00:00',NULL),(672,37,9,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(675,41,13,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(676,30,15,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(677,37,18,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(678,34,2,4,'APPROVED',1,'2019-04-01 00:00:00',NULL),(679,41,2,4,'APPROVED',2,'2019-04-01 00:00:00',NULL),(680,33,4,4,'APPROVED',4,'2019-04-01 00:00:00',NULL),(681,34,14,4,'APPROVED',3,'2019-04-01 00:00:00',NULL),(684,11,9,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(685,14,20,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(686,15,18,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(687,9,2,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(688,6,9,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(689,11,4,2,'APPROVED',3,'2019-04-01 00:00:00',NULL),(690,15,4,2,'APPROVED',4,'2019-04-01 00:00:00',NULL),(691,6,3,2,'APPROVED',4,'2019-04-01 00:00:00',NULL),(692,6,14,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(693,15,11,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(694,6,7,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(695,14,12,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(696,10,5,2,'APPROVED',4,'2019-04-01 00:00:00',NULL),(697,6,5,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(698,9,5,2,'APPROVED',3,'2019-04-01 00:00:00',NULL),(700,9,12,2,'APPROVED',4,'2019-04-01 00:00:00',NULL),(702,15,4,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(704,15,4,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(705,16,15,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(706,8,13,2,'APPROVED',4,'2019-04-01 00:00:00',NULL),(707,7,19,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(708,10,3,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(709,9,14,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(710,16,11,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(711,8,4,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(712,11,1,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(713,5,19,2,'APPROVED',4,'2019-04-01 00:00:00',NULL),(714,15,7,2,'APPROVED',3,'2019-04-01 00:00:00',NULL),(715,13,13,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(716,11,3,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(717,15,9,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(718,15,2,2,'APPROVED',3,'2019-04-01 00:00:00',NULL),(719,15,15,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(720,11,9,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(721,7,9,2,'APPROVED',3,'2019-04-01 00:00:00',NULL),(722,9,16,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(723,8,18,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(724,7,18,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(725,10,18,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(726,5,10,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(727,15,7,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(728,13,4,2,'APPROVED',2,'2019-04-01 00:00:00',NULL),(729,10,4,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(730,8,4,2,'APPROVED',1,'2019-04-01 00:00:00',NULL),(731,5,18,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(732,12,14,2,'APPROVED',5,'2019-04-01 00:00:00',NULL),(733,17,1,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(734,22,17,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(735,23,16,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(737,24,10,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(738,17,19,3,'APPROVED',4,'2019-04-01 00:00:00',NULL),(739,22,10,3,'APPROVED',1,'2019-04-01 00:00:00',NULL),(740,21,2,3,'APPROVED',4,'2019-04-01 00:00:00',NULL),(741,17,10,3,'APPROVED',1,'2019-04-01 00:00:00',NULL),(742,18,7,3,'APPROVED',2,'2019-04-01 00:00:00',NULL),(743,18,3,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(744,20,19,3,'APPROVED',4,'2019-04-01 00:00:00',NULL),(745,17,14,3,'APPROVED',1,'2019-04-01 00:00:00',NULL),(746,24,4,3,'APPROVED',1,'2019-04-01 00:00:00',NULL),(747,27,8,3,'APPROVED',1,'2019-04-01 00:00:00',NULL),(748,26,20,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(749,18,8,3,'APPROVED',2,'2019-04-01 00:00:00',NULL),(750,20,14,3,'APPROVED',2,'2019-04-01 00:00:00',NULL),(751,25,9,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(752,19,7,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(753,18,5,3,'APPROVED',4,'2019-04-01 00:00:00',NULL),(754,22,13,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(755,18,18,3,'APPROVED',4,'2019-04-01 00:00:00',NULL),(756,20,16,3,'APPROVED',1,'2019-04-01 00:00:00',NULL),(757,24,7,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(758,23,20,3,'APPROVED',2,'2019-04-01 00:00:00',NULL),(759,25,8,3,'APPROVED',2,'2019-04-01 00:00:00',NULL),(760,21,9,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(761,28,10,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(762,27,16,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(763,21,12,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(765,28,14,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(766,25,11,3,'APPROVED',1,'2019-04-01 00:00:00',NULL),(767,26,15,3,'APPROVED',1,'2019-04-01 00:00:00',NULL),(768,20,16,3,'APPROVED',4,'2019-04-01 00:00:00',NULL),(769,28,16,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(770,26,17,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(771,21,4,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(772,25,1,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(773,21,9,3,'APPROVED',4,'2019-04-01 00:00:00',NULL),(774,18,1,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(775,22,17,3,'APPROVED',1,'2019-04-01 00:00:00',NULL),(776,26,18,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(777,18,12,3,'APPROVED',4,'2019-04-01 00:00:00',NULL),(778,22,3,3,'APPROVED',1,'2019-04-01 00:00:00',NULL),(779,21,10,3,'APPROVED',3,'2019-04-01 00:00:00',NULL),(780,28,3,3,'APPROVED',4,'2019-04-01 00:00:00',NULL),(781,26,12,3,'APPROVED',5,'2019-04-01 00:00:00',NULL),(782,23,15,3,'APPROVED',5,'2019-04-01 00:00:00',NULL);
/*!40000 ALTER TABLE `employeeskill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `userrole`
--

LOCK TABLES `userrole` WRITE;
/*!40000 ALTER TABLE `userrole` DISABLE KEYS */;
INSERT INTO `userrole` VALUES (1,1),(2,2),(3,2),(4,2),(5,3),(6,3),(7,3),(8,3),(9,3),(10,3),(11,3),(12,3),(13,3),(14,3),(15,3),(16,3),(17,3),(18,3),(19,3),(20,3),(21,3),(22,3),(23,3),(24,3),(25,3),(26,3),(27,3),(28,3),(29,3),(30,3),(31,3),(32,3),(33,3),(34,3),(35,3),(36,3),(37,3),(38,3),(39,3),(40,3),(41,3);
/*!40000 ALTER TABLE `userrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `audit`
--
LOCK TABLES `audit` WRITE;
/*!40000 ALTER TABLE `audit` DISABLE KEYS */;
INSERT INTO `audit` VALUES (1,2,'2019-09-15 13:00:52',1,'Login'),(2,3,'2019-09-15 13:00:52',2,'Logout'),(3,4,'2019-09-15 13:00:52',3,'Login'),(4,5,'2019-09-15 13:00:52',1,'Reset Password'),(5,6,'2019-09-15 13:00:52',1,'Login'),(6,7,'2019-09-15 13:00:52',1,'Did Something'),(7,8,'2019-09-15 13:00:52',1,'Something Else'),(8,9,'2019-09-15 13:00:52',1,'Logout'),(9,10,'2019-09-15 13:00:52',1,'Login'),(10,11,'2019-09-15 13:00:52',1,'Lala'),(11,12,'2019-09-15 13:00:52',2,'Lala'),(12,13,'2019-09-15 13:00:52',2,'Lala'),(13,14,'2019-09-15 13:00:52',2,'Lala'),(14,15,'2019-09-15 13:00:52',2,'Lala'),(15,16,'2019-09-15 13:00:52',2,'Lala'),(16,17,'2019-09-15 13:00:52',3,'Lala'),(17,18,'2019-09-15 13:00:52',3,'Lala'),(18,19,'2019-09-15 13:00:52',3,'Lala'),(19,20,'2019-09-15 13:00:52',3,'Lala'),(20,21,'2019-09-15 13:00:52',1,'Lala');
/*!40000 ALTER TABLE `audit` ENABLE KEYS */;
UNLOCK TABLES;

/*!0101 
SET SQL_MODE=@OLD_SQL_MODE /;
/*!0014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!0014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!0101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!0101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!0101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!0111 SET SQL_NOTES=@OLD_SQL_NOTES */;
