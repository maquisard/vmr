-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 19, 2013 at 03:04 AM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `vrec`
--

-- --------------------------------------------------------

--
-- Table structure for table `settingsproperty`
--

CREATE TABLE IF NOT EXISTS `settingsproperty` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `settingsid` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `settingsid` (`settingsid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `settingsproperty`
--

INSERT INTO `settingsproperty` (`id`, `name`, `value`, `type`, `settingsid`) VALUES
('106f4394-62e0-4257-a32c-29dd221bad84', 'message.email.protocol', 'smtps', '', 'a885ded9-417a-4008-a00e-d156215074fd'),
('902da2f5-718b-439d-9c38-b87c948ece0d', 'message.email.host', 'smtp.googlemail.com', '', 'a885ded9-417a-4008-a00e-d156215074fd'),
('e7d878b2-f2d1-4895-a2fc-73b5572a529a', 'message.email.username', 'zuluv2@gmail.com', '', 'a885ded9-417a-4008-a00e-d156215074fd'),
('5d0a0828-3d79-4e6e-bda4-54b4713b4738', 'message.email.from', 'zuluv2@gmail.com', '', 'a885ded9-417a-4008-a00e-d156215074fd'),
('3a5f56ae-0d93-442a-981f-9baff604dc0e', 'message.email.password', '', '', 'a885ded9-417a-4008-a00e-d156215074fd'),
('a66dcb7b-aad2-41e3-80e8-11b51b529d60', 'message.email.port', '465', '', 'a885ded9-417a-4008-a00e-d156215074fd'),
('c6f00187-7c07-476d-9ff4-a4de034d9e91', 'message.email.auth', 'true', '', 'a885ded9-417a-4008-a00e-d156215074fd'),
('3f0f0755-2268-4dfd-8dd8-279010934831', 'cookie.age', '36000', '', 'a885ded9-417a-4008-a00e-d156215074fd'),
('5f6bc826-f0bd-11e1-a0d6-0025900a72c6', 'eventhandler.package', 'vrec.data.crudeventhandlers', '', 'a885ded9-417a-4008-a00e-d156215074fd'),
('29e7ff2c-fd19-11e1-a0d6-0025900a72c6', 'template.password.reset.id', 'aa2d053f-adca-4b83-9a3c-a6089c305414', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('4efed669-4d48-11e2-a99c-e0915348cc66', 'authentication.usernameproperty', 'email', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('4eff4a37-4d48-11e2-a99c-e0915348cc66', 'authentication.entity', 'user', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('7f14c9b8-4d48-11e2-a99c-e0915348cc66', 'authentication.passwordproperty', 'password', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('65ec18da-4dfb-11e2-a99c-e0915348cc66', 'profile.image.width', '128', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('65ecac6b-4dfb-11e2-a99c-e0915348cc66', 'profile.image.height', '128', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('98a84bc2-d9eb-11e2-abd2-e0915348cc66', 'user.class', 'MovieUser', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('6f2ba588-c349-11e2-993c-e0915348cc66', 'default.moviegenre.id', '44535cbf-e211-4244-be3a-8011a1bf192a', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('da043a60-cf10-11e2-95cd-e0915348cc66', 'default.contentrating.id', '6ea4d041-78ce-4984-9ad0-887ba20a0898', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('696ecaf7-c336-11e2-993c-e0915348cc66', 'scraper.class', 'MovieScraper', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('09cb750e-c337-11e2-993c-e0915348cc66', 'item.class', 'MovieItem', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('2d41e282-c339-11e2-993c-e0915348cc66', 'default.movieuseroccupation.id', '5dd90431-b9eb-4334-b539-cf3579f1995c', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('35185a5d-5201-11e2-a99c-e0915348cc66', 'image.scale.width.padding', '50', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('3518ada9-5201-11e2-a99c-e0915348cc66', 'image.scale.height.padding', '60', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('696e7b6c-c336-11e2-993c-e0915348cc66', 'item.package', 'vrec.data.movie', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('baf03cff-337f-11e3-9a30-18a905c0e529', 'tmdb.api.key', 'f1cc870b7511053c8a989625c2555d2a', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('096915a1-3c59-11e3-ba50-18a905c0e529', 'user.ml_id.max', '943', NULL, 'a885ded9-417a-4008-a00e-d156215074fd'),
('ed78ce21-47e1-11e3-9219-18a905c0e529', 'movie.ml_id.max', '1682', NULL, 'a885ded9-417a-4008-a00e-d156215074fd');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
