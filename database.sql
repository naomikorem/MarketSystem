-- --------------------------------------------------------
-- Host:                         sql11.freemysqlhosting.net
-- Server version:               5.5.62-0ubuntu0.14.04.1 - (Ubuntu)
-- Server OS:                    debian-linux-gnu
-- HeidiSQL Version:             11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table sql11498449.Admins
CREATE TABLE IF NOT EXISTS `Admins` (
  `admin_name` varchar(50) NOT NULL,
  PRIMARY KEY (`admin_name`),
  CONSTRAINT `FK_name` FOREIGN KEY (`admin_name`) REFERENCES `users` (`userName`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.CompositeDiscount
CREATE TABLE IF NOT EXISTS `CompositeDiscount` (
  `id` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.CompositePolicy
CREATE TABLE IF NOT EXISTS `CompositePolicy` (
  `id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.CompositePredicates
CREATE TABLE IF NOT EXISTS `CompositePredicates` (
  `id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.discount
CREATE TABLE IF NOT EXISTS `discount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.DiscountsInComposite
CREATE TABLE IF NOT EXISTS `DiscountsInComposite` (
  `discountId` int(11) NOT NULL,
  `compositeId` int(11) NOT NULL,
  PRIMARY KEY (`discountId`,`compositeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.Items
CREATE TABLE IF NOT EXISTS `Items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(50) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `numberOfRatings` int(11) DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1037 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.itemsInBaskets
CREATE TABLE IF NOT EXISTS `itemsInBaskets` (
  `basketId` int(11) NOT NULL DEFAULT '0',
  `itemId` int(11) NOT NULL DEFAULT '0',
  `amount` int(11) DEFAULT '0',
  PRIMARY KEY (`basketId`,`itemId`) USING BTREE,
  KEY `itemFK2` (`itemId`),
  CONSTRAINT `basketFKK` FOREIGN KEY (`basketId`) REFERENCES `ShoppingBasket` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.ItemsInStores
CREATE TABLE IF NOT EXISTS `ItemsInStores` (
  `itemId` int(11) NOT NULL,
  `storeId` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`itemId`,`storeId`) USING BTREE,
  KEY `storeFK` (`storeId`),
  CONSTRAINT `itemFK` FOREIGN KEY (`itemId`) REFERENCES `Items` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `storeFK` FOREIGN KEY (`storeId`) REFERENCES `stores` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.keywords
CREATE TABLE IF NOT EXISTS `keywords` (
  `itemId` int(11) DEFAULT NULL,
  `keyword` varchar(50) DEFAULT NULL,
  KEY `itemFK3` (`itemId`),
  CONSTRAINT `itemFK3` FOREIGN KEY (`itemId`) REFERENCES `Items` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.Notifications
CREATE TABLE IF NOT EXISTS `Notifications` (
  `Username` varchar(50) NOT NULL,
  `message` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`Username`,`message`),
  CONSTRAINT `FK_username_notifications` FOREIGN KEY (`Username`) REFERENCES `users` (`userName`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.Permissions
CREATE TABLE IF NOT EXISTS `Permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `manager` varchar(50) NOT NULL DEFAULT '',
  `givenBy` varchar(50) NOT NULL DEFAULT '',
  `permissionMask` tinyint(4) DEFAULT NULL,
  `storeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `managerFK1` (`manager`),
  CONSTRAINT `managerFK1` FOREIGN KEY (`manager`) REFERENCES `users` (`userName`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.PoliciesInComposite
CREATE TABLE IF NOT EXISTS `PoliciesInComposite` (
  `policyId` int(11) NOT NULL,
  `compositeId` int(11) NOT NULL,
  PRIMARY KEY (`policyId`,`compositeId`),
  KEY `compositePolicyFK` (`compositeId`),
  CONSTRAINT `policyFK` FOREIGN KEY (`policyId`) REFERENCES `Policy` (`id`) ON DELETE CASCADE,
  CONSTRAINT `compositePolicyFK` FOREIGN KEY (`compositeId`) REFERENCES `CompositePolicy` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.Policy
CREATE TABLE IF NOT EXISTS `Policy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=273 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.Predicates
CREATE TABLE IF NOT EXISTS `Predicates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=586 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.PredicatesInComposite
CREATE TABLE IF NOT EXISTS `PredicatesInComposite` (
  `PredicateId` int(11) NOT NULL DEFAULT '0',
  `CompositeId` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`PredicateId`,`CompositeId`),
  KEY `CompositeFK` (`CompositeId`),
  CONSTRAINT `CompositeFK` FOREIGN KEY (`CompositeId`) REFERENCES `CompositePredicates` (`id`) ON DELETE CASCADE,
  CONSTRAINT `PredicateFK1` FOREIGN KEY (`PredicateId`) REFERENCES `Predicates` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.Services
CREATE TABLE IF NOT EXISTS `Services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Url` varchar(50) NOT NULL,
  `Type` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4101 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.ShoppingBasket
CREATE TABLE IF NOT EXISTS `ShoppingBasket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `storeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userFK` (`username`),
  CONSTRAINT `userFK` FOREIGN KEY (`username`) REFERENCES `users` (`userName`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=413 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.simpleDiscount
CREATE TABLE IF NOT EXISTS `simpleDiscount` (
  `id` int(11) NOT NULL,
  `predicateId` int(11) DEFAULT NULL,
  `percentage` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `predicateFK2` (`predicateId`),
  CONSTRAINT `discountFK2` FOREIGN KEY (`id`) REFERENCES `discount` (`id`) ON DELETE CASCADE,
  CONSTRAINT `predicateFK2` FOREIGN KEY (`predicateId`) REFERENCES `Predicates` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.SimplePredicate
CREATE TABLE IF NOT EXISTS `SimplePredicate` (
  `id` int(11) NOT NULL,
  `itemId` int(11) DEFAULT NULL,
  `minBasket` double DEFAULT '0',
  `category` int(11) DEFAULT '0',
  `hour` int(11) DEFAULT '0',
  `date` date DEFAULT '0000-00-00',
  `type` int(11) NOT NULL DEFAULT '0',
  `displayString` varchar(150) NOT NULL DEFAULT '',
  `minAmount` int(11) DEFAULT NULL,
  `maxAmount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `predicateFK` FOREIGN KEY (`id`) REFERENCES `Predicates` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.SimplePurchasePolicy
CREATE TABLE IF NOT EXISTS `SimplePurchasePolicy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hour` int(11) DEFAULT '24',
  `predicateId` int(11) DEFAULT NULL,
  `date` date DEFAULT '0000-00-00',
  PRIMARY KEY (`id`),
  KEY `predicateId` (`predicateId`),
  CONSTRAINT `FK_SimplePurchasePolicy_Predicates` FOREIGN KEY (`predicateId`) REFERENCES `Predicates` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.Statistics
CREATE TABLE IF NOT EXISTS `Statistics` (
  `Date` date NOT NULL,
  PRIMARY KEY (`Date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.statisticsGuests
CREATE TABLE IF NOT EXISTS `statisticsGuests` (
  `statistic` date DEFAULT NULL,
  `guestIp` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.storeManagers
CREATE TABLE IF NOT EXISTS `storeManagers` (
  `manager` varchar(50) NOT NULL DEFAULT '',
  `store` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`manager`,`store`),
  KEY `manager` (`manager`,`store`),
  KEY `store` (`store`),
  CONSTRAINT `store` FOREIGN KEY (`store`) REFERENCES `stores` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `manager` FOREIGN KEY (`manager`) REFERENCES `users` (`userName`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.storeOwners
CREATE TABLE IF NOT EXISTS `storeOwners` (
  `owner` varchar(50) NOT NULL DEFAULT '',
  `store` int(11) NOT NULL DEFAULT '0',
  `givenBy` varchar(50) DEFAULT '',
  PRIMARY KEY (`owner`,`store`),
  KEY `store1` (`store`,`owner`) USING BTREE,
  CONSTRAINT `store1` FOREIGN KEY (`store`) REFERENCES `stores` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `owner1` FOREIGN KEY (`owner`) REFERENCES `users` (`userName`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.stores
CREATE TABLE IF NOT EXISTS `stores` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text,
  `open` tinyint(4) DEFAULT NULL,
  `founder` varchar(50) DEFAULT NULL,
  `permanentlyClosed` tinyint(4) DEFAULT NULL,
  `discountPolicy` int(11) DEFAULT NULL,
  `purchasePolicy` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=554 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.UserHistoryItems
CREATE TABLE IF NOT EXISTS `UserHistoryItems` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `itemId` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `product_name` varchar(50) NOT NULL,
  `category` int(11) NOT NULL DEFAULT '0',
  `price_per_unit` double NOT NULL DEFAULT '0',
  `amount` int(11) NOT NULL DEFAULT '0',
  `date` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`id`),
  KEY `FK_username_history_items` (`username`),
  KEY `FK_item_id` (`itemId`),
  KEY `FK_store` (`store_id`),
  CONSTRAINT `FK_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_item_id` FOREIGN KEY (`itemId`) REFERENCES `Items` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_username_history_items` FOREIGN KEY (`username`) REFERENCES `users` (`userName`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=398 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.users
CREATE TABLE IF NOT EXISTS `users` (
  `userName` varchar(50) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `password` varchar(250) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`userName`),
  KEY `userName` (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table sql11498449.UsersInStatistics
CREATE TABLE IF NOT EXISTS `UsersInStatistics` (
  `statisticId` date DEFAULT NULL,
  `userName` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
