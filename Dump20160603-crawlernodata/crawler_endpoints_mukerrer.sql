CREATE DATABASE  IF NOT EXISTS `crawler` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `crawler`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: crawler
-- ------------------------------------------------------
-- Server version	5.6.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `endpoints_mukerrer`
--

DROP TABLE IF EXISTS `endpoints_mukerrer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endpoints_mukerrer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `datasetName` varchar(200) DEFAULT NULL,
  `endpointUrl` varchar(2083) DEFAULT NULL,
  `dateCreated` datetime DEFAULT CURRENT_TIMESTAMP,
  `source` varchar(50) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  `lastCheckedDate` datetime DEFAULT NULL,
  `threadNumber` int(11) DEFAULT NULL,
  `checkFlag` int(11) DEFAULT NULL,
  `triples` int(11) DEFAULT NULL,
  `entities` int(11) DEFAULT NULL,
  `distinctResourceURIs` int(11) DEFAULT NULL,
  `distinctClasses` int(11) DEFAULT NULL,
  `distinctPredicates` int(11) DEFAULT NULL,
  `distinctSubjectNodes` int(11) DEFAULT NULL,
  `distinctObjectNodes` int(11) DEFAULT NULL,
  `triplesLCD` datetime DEFAULT NULL,
  `entitiesLCD` datetime DEFAULT NULL,
  `distinctResourceURIsLCD` datetime DEFAULT NULL,
  `distinctClassesLCD` datetime DEFAULT NULL,
  `distinctPredicatesLCD` datetime DEFAULT NULL,
  `distinctSubjectNodesLCD` datetime DEFAULT NULL,
  `distinctObjectNodesLCD` datetime DEFAULT NULL,
  `sameAs` int(11) DEFAULT NULL,
  `disabled` int(11) DEFAULT NULL,
  `commentsWordCount` int(11) DEFAULT NULL,
  `outLinksCount` int(11) DEFAULT NULL,
  `tfidfprocessed` int(11) DEFAULT NULL,
  `subject` varchar(200) DEFAULT NULL,
  `totalcheck` int(11) DEFAULT NULL,
  `sourceCode` longtext,
  `sourceCodeXML` longtext,
  `sourceCodeHTML` longtext,
  `domain` varchar(50) DEFAULT NULL,
  `domainSource` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1679 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-06-03 13:42:43
