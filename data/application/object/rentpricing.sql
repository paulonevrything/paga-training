
DELIMITER ;

		

	DELIMITER $$

	DROP PROCEDURE IF EXISTS `tmp_sp_create_table_rentpricing` $$
	CREATE PROCEDURE tmp_sp_create_table_rentpricing()
	BEGIN

		IF NOT EXISTS (SELECT NULL FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'rentpricing') THEN

			CREATE TABLE `rentpricing`(
				`RentPricingId` INT NOT NULL auto_increment, 
			  `CreatedBy` varchar(50) NOT NULL,
			  `CreatedDate` DATETIME NOT NULL,
			  `DailyRate` decimal(18,2) NOT NULL,
			  `UpdatedBy` varchar(50) NOT NULL,
			  `UpdatedDate` DATETIME NULL,
			  `VideoTypeId` varchar(50) NOT NULL,
			  PRIMARY KEY  (`rentpricingId`)
			,  FOREIGN KEY (VideoTypeId) REFERENCES `videotype`(videotypeId)
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;



			END IF;

		END$$

		DELIMITER ;

		CALL tmp_sp_create_table_rentpricing();

		DROP PROCEDURE tmp_sp_create_table_rentpricing;
	
DELIMITER ;

		CREATE OR REPLACE ALGORITHM=UNDEFINED VIEW `vrentpricing`
			AS SELECT * FROM `rentpricing`;
		
DELIMITER ;

    	DROP PROCEDURE IF EXISTS `spDeleteRentPricing`;
        DELIMITER $$
        CREATE PROCEDURE `spDeleteRentPricing`(_RentPricingId  INT)
        BEGIN
        	    DELETE FROM `rentpricing`
        	    WHERE RentPricingId = _RentPricingId;
        END $$
    
DELIMITER ;

    	DROP PROCEDURE IF EXISTS `spGetRentPricing`;
        DELIMITER $$
        CREATE PROCEDURE `spGetRentPricing`(_RentPricingId  INT)
        BEGIN
        	    SELECT * FROM `rentpricing`
        	    WHERE RentPricingId = _RentPricingId;
        END $$
    
DELIMITER ;

    	
    	
    	DROP PROCEDURE IF EXISTS `spInsertRentPricing`;
        DELIMITER $$
        CREATE PROCEDURE `spInsertRentPricing`(
    			_createdBy varchar(50),
			_dailyRate decimal(18,2),
			_updatedBy varchar(50),
			_videoTypeId varchar(50)
    	)
    	BEGIN

    		INSERT INTO `rentpricing`(
    			`CreatedBy`,
			`DailyRate`,
			`UpdatedBy`,
			`VideoTypeId`
			) VALUES (
			_createdBy,
			_dailyRate,
			_updatedBy,
			_videoTypeId);
        	select LAST_INSERT_ID() AS RentPricingId;
        END $$
    
DELIMITER ;

    	
    	
    	DROP PROCEDURE IF EXISTS `spFindRentPricing`;
        DELIMITER $$
        CREATE PROCEDURE `spFindRentPricing`(
    			_createdBy varchar(50),
			_dailyRate decimal(18,2),
			_updatedBy varchar(50),
			_videoTypeId varchar(50)
    	)
    	BEGIN

    		SELECT * FROM `rentpricing` WHERE
    			(ISNULL(_createdBy) = 1 OR `CreatedBy` = _createdBy) AND 
			(ISNULL(_dailyRate) = 1 OR `DailyRate` = _dailyRate) AND 
			(ISNULL(_updatedBy) = 1 OR `UpdatedBy` = _updatedBy) AND 
			(ISNULL(_videoTypeId) = 1 OR `VideoTypeId` = _videoTypeId);
        END $$
    
DELIMITER ;

		
    	
    	DROP PROCEDURE IF EXISTS `spUpdateRentPricing`;
        DELIMITER $$
        CREATE PROCEDURE `spUpdateRentPricing`(
    			_dailyRate decimal(18,2),
			_rentPricingId INT,
			_updatedBy varchar(50),
			_videoTypeId varchar(50)
    	)
    	BEGIN

    		UPDATE `rentpricing` SET
    			`DailyRate` = _dailyRate,
			`UpdatedBy` = _updatedBy,
			`VideoTypeId` = _videoTypeId
		WHERE RentPricingId = _rentPricingId;
		END $$
	
DELIMITER ;


			
				
				
				
				
				
				
				
			

		DELIMITER ;
		DROP TRIGGER IF EXISTS RentPricing_insert;
		DELIMITER $$
		CREATE TRIGGER RentPricing_insert BEFORE INSERT ON `rentpricing`
		FOR EACH ROW  BEGIN

	
			SET NEW.UpdatedDate = NOW();
            SET NEW.CreatedDate = NOW();
        
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'RentPricing', 'RentPricingId', 'CreatedBy', NEW.`CreatedBy`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'RentPricing', 'RentPricingId', 'CreatedDate', NEW.`CreatedDate`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'RentPricing', 'RentPricingId', 'DailyRate', NEW.`DailyRate`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'RentPricing', 'RentPricingId', 'RentPricingId', NEW.`RentPricingId`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'RentPricing', 'RentPricingId', 'UpdatedBy', NEW.`UpdatedBy`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'RentPricing', 'RentPricingId', 'UpdatedDate', NEW.`UpdatedDate`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'RentPricing', 'RentPricingId', 'VideoTypeId', NEW.`VideoTypeId`, NULL;
			
			END
			$$
			DELIMITER ;
		

			
				
				
				
				
				
				
				
       		

		DELIMITER ;
		DROP TRIGGER IF EXISTS RentPricing_update;
		DELIMITER $$
		CREATE TRIGGER RentPricing_update BEFORE UPDATE ON `rentpricing`
		FOR EACH ROW  BEGIN

	
       		SET NEW.UpdatedDate = NOW();
       	
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'RentPricing', 'RentPricingId', 'CreatedBy', NEW.`CreatedBy`,
					OLD.`CreatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'RentPricing', 'RentPricingId', 'CreatedDate', NEW.`CreatedDate`,
					OLD.`CreatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'RentPricing', 'RentPricingId', 'DailyRate', NEW.`DailyRate`,
					OLD.`DailyRate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'RentPricing', 'RentPricingId', 'RentPricingId', NEW.`RentPricingId`,
					OLD.`RentPricingId`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'RentPricing', 'RentPricingId', 'UpdatedBy', NEW.`UpdatedBy`,
					OLD.`UpdatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'RentPricing', 'RentPricingId', 'UpdatedDate', NEW.`UpdatedDate`,
					OLD.`UpdatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'RentPricing', 'RentPricingId', 'VideoTypeId', NEW.`VideoTypeId`,
					OLD.`VideoTypeId`;
			
			END
			$$
			DELIMITER ;
		

			
				
				
				
				
				
				
				

		DELIMITER ;
		DROP TRIGGER IF EXISTS RentPricing_delete;
		DELIMITER $$
		CREATE TRIGGER RentPricing_delete BEFORE DELETE ON `rentpricing`
		FOR EACH ROW  BEGIN
	
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'RentPricing', 'RentPricingId', 'CreatedBy', NULL,
				OLD.`CreatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'RentPricing', 'RentPricingId', 'CreatedDate', NULL,
				OLD.`CreatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'RentPricing', 'RentPricingId', 'DailyRate', NULL,
				OLD.`DailyRate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'RentPricing', 'RentPricingId', 'RentPricingId', NULL,
				OLD.`RentPricingId`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'RentPricing', 'RentPricingId', 'UpdatedBy', NULL,
				OLD.`UpdatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'RentPricing', 'RentPricingId', 'UpdatedDate', NULL,
				OLD.`UpdatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'RentPricing', 'RentPricingId', 'VideoTypeId', NULL,
				OLD.`VideoTypeId`;
			
			END
			$$
			DELIMITER ;
		