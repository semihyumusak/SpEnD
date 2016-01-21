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
-- Temporary table structure for view `crawl_page`
--

DROP TABLE IF EXISTS `crawl_page`;
/*!50001 DROP VIEW IF EXISTS `crawl_page`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `crawl_page` (
  `pagecontentid` tinyint NOT NULL,
  `crawlid` tinyint NOT NULL,
  `queryText` tinyint NOT NULL,
  `searchEngineName` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `pagecontent`
--

DROP TABLE IF EXISTS `pagecontent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagecontent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `htmlContent` longtext,
  `dateCreated` datetime DEFAULT CURRENT_TIMESTAMP,
  `resultPageNumber` int(11) NOT NULL,
  `searchEngineName` varchar(45) DEFAULT NULL,
  `crawlRecordId` int(11) NOT NULL,
  `queryText` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19194 DEFAULT CHARSET=utf8 COMMENT='					';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sparqlqueryresult`
--

DROP TABLE IF EXISTS `sparqlqueryresult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sparqlqueryresult` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commonsparqlqueryid` int(11) DEFAULT NULL,
  `queryqueueid` int(11) DEFAULT NULL,
  `result` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `queryqueue`
--

DROP TABLE IF EXISTS `queryqueue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `queryqueue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `endpointUrl` varchar(2083) DEFAULT NULL,
  `resultSet` text,
  `isProcessStarted` int(11) DEFAULT NULL,
  `processStartDate` datetime DEFAULT NULL,
  `createdDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `commonQueryId` int(11) NOT NULL,
  `processEndDate` datetime DEFAULT NULL,
  `disabled` int(11) NOT NULL,
  `tryCount` int(11) NOT NULL DEFAULT '0',
  `resultSetData` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9980 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seedurlraw`
--

DROP TABLE IF EXISTS `seedurlraw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seedurlraw` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(2083) DEFAULT NULL,
  `searchEngine` varchar(45) DEFAULT NULL,
  `dateCreated` datetime DEFAULT CURRENT_TIMESTAMP,
  `resultOrder` int(11) DEFAULT NULL,
  `isEndpoint` int(11) DEFAULT NULL,
  `pageContentId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `perf` (`searchEngine`),
  KEY `perf2` (`isEndpoint`) USING BTREE,
  FULLTEXT KEY `fulltext` (`url`)
) ENGINE=InnoDB AUTO_INCREMENT=180074 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `searchqueue`
--

DROP TABLE IF EXISTS `searchqueue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `searchqueue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `searchText` varchar(500) DEFAULT NULL,
  `isProcessStarted` int(11) DEFAULT NULL,
  `processStartDate` datetime DEFAULT NULL,
  `createdDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `processEndDate` datetime DEFAULT NULL,
  `disabled` int(11) NOT NULL,
  `searchEngineName` varchar(45) NOT NULL,
  `maxSearchPage` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17082 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `crawlrecord`
--

DROP TABLE IF EXISTS `crawlrecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crawlrecord` (
  `crawlid` int(11) NOT NULL AUTO_INCREMENT,
  `dateStarted` datetime DEFAULT CURRENT_TIMESTAMP,
  `dateEnded` datetime DEFAULT NULL,
  `crawlerName` varchar(45) DEFAULT NULL,
  `queryText` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`crawlid`)
) ENGINE=InnoDB AUTO_INCREMENT=240 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commonsparqlquery`
--

DROP TABLE IF EXISTS `commonsparqlquery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commonsparqlquery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sparqlQuery` text,
  `enabled` int(11) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `endpoints`
--

DROP TABLE IF EXISTS `endpoints`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endpoints` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `datasetName` varchar(200) DEFAULT NULL,
  `endpointUrl` varchar(2083) DEFAULT NULL,
  `dateCreated` datetime DEFAULT CURRENT_TIMESTAMP,
  `source` varchar(50) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  `lastCheckedDate` datetime DEFAULT NULL,
  `triples` int(11) DEFAULT NULL,
  `checkFlag` int(11) DEFAULT NULL,
  `entities` int(11) DEFAULT NULL,
  `distinctResourceURIs` int(11) DEFAULT NULL,
  `distinctClasses` int(11) DEFAULT NULL,
  `distinctPredicates` int(11) DEFAULT NULL,
  `distinctSubjectNodes` int(11) DEFAULT NULL,
  `distinctObjectNodes` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2332 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `crawl_page`
--

/*!50001 DROP TABLE IF EXISTS `crawl_page`*/;
/*!50001 DROP VIEW IF EXISTS `crawl_page`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `crawl_page` AS select `p`.`id` AS `pagecontentid`,`c`.`crawlid` AS `crawlid`,`c`.`queryText` AS `queryText`,`p`.`searchEngineName` AS `searchEngineName` from (`crawlrecord` `c` join `pagecontent` `p` on((`p`.`crawlRecordId` = `c`.`crawlid`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Dumping routines for database 'crawler'
--
/*!50003 DROP PROCEDURE IF EXISTS `reset_sparql_query_exceptions_for_retry` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `reset_sparql_query_exceptions_for_retry`()
BEGIN
update queryqueue set resultSet=null, isProcessStarted=null,processStartDate=null where (resultSet LIKE 'Exception%' or resultSet is null)   and id>1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `select_distinct_urls_where_not_exist_in_other_search_engines` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `select_distinct_urls_where_not_exist_in_other_search_engines`(in SE varchar(200))
BEGIN
SELECT DISTINCT(url) FROM crawler.list_all_urls where searchEngine=SE and isEndpoint="2" and url not in (select url  FROM crawler.list_all_urls where searchEngine <> SE) ;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-01-20 12:03:56
