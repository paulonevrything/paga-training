
DELIMITER ;

		

	DELIMITER $$

	DROP PROCEDURE IF EXISTS `tmp_sp_create_table_audit` $$
	CREATE PROCEDURE tmp_sp_create_table_audit()
	BEGIN

		IF NOT EXISTS (SELECT NULL FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'audit') THEN

			CREATE TABLE `audit`(
				`AuditId` INT NOT NULL auto_increment, 
			  `Action` char(1) NOT NULL,
			  `ColumnName` varchar(40) NULL,
			  `CreatedBy` nvarchar(50) NULL,
			  `CreatedDate` DATETIME NULL,
			  `DateTime` DATETIME NOT NULL,
			  `LoginId` varchar(50) NOT NULL,
			  `NewValue` varchar(MAX) NULL,
			  `OldValue` varchar(MAX) NULL,
			  `PrimaryKey` varchar(50) NOT NULL,
			  `TableName` varchar(50) NOT NULL,
			  `UpdatedBy` nvarchar(50) NULL,
			  `UpdatedDate` DATETIME NULL,
			  PRIMARY KEY  (`auditId`)
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;



			END IF;

		END$$

		DELIMITER ;

		CALL tmp_sp_create_table_audit();

		DROP PROCEDURE tmp_sp_create_table_audit;
	