
DELIMITER ;

		

	DELIMITER $$

	DROP PROCEDURE IF EXISTS `tmp_sp_create_table_user` $$
	CREATE PROCEDURE tmp_sp_create_table_user()
	BEGIN

		IF NOT EXISTS (SELECT NULL FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user') THEN

			CREATE TABLE `user`(
				`UserId` INT NOT NULL auto_increment, 
			  `CreatedBy` varchar(50) NOT NULL,
			  `CreatedDate` DATETIME NOT NULL,
			  `FirstName` varchar(50) NOT NULL,
			  `LastName` nvarchar(50) NOT NULL,
			  `UpdatedBy` varchar(50) NOT NULL,
			  `UpdatedDate` DATETIME NULL,
			  `Username` nvarchar(255) NOT NULL,
			  PRIMARY KEY  (`userId`)
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;



			END IF;

		END$$

		DELIMITER ;

		CALL tmp_sp_create_table_user();

		DROP PROCEDURE tmp_sp_create_table_user;
	
DELIMITER ;

		CREATE OR REPLACE ALGORITHM=UNDEFINED VIEW `vuser`
			AS SELECT * FROM `user`;
		
DELIMITER ;

    	DROP PROCEDURE IF EXISTS `spDeleteUser`;
        DELIMITER $$
        CREATE PROCEDURE `spDeleteUser`(_UserId  INT)
        BEGIN
        	    DELETE FROM `user`
        	    WHERE UserId = _UserId;
        END $$
    
DELIMITER ;

    	DROP PROCEDURE IF EXISTS `spGetUser`;
        DELIMITER $$
        CREATE PROCEDURE `spGetUser`(_UserId  INT)
        BEGIN
        	    SELECT * FROM `user`
        	    WHERE UserId = _UserId;
        END $$
    
DELIMITER ;

    	
    	
    	DROP PROCEDURE IF EXISTS `spInsertUser`;
        DELIMITER $$
        CREATE PROCEDURE `spInsertUser`(
    			_createdBy varchar(50),
			_firstName varchar(50),
			_lastName nvarchar(50),
			_updatedBy varchar(50),
			_username nvarchar(255)
    	)
    	BEGIN

    		INSERT INTO `user`(
    			`CreatedBy`,
			`FirstName`,
			`LastName`,
			`UpdatedBy`,
			`Username`
			) VALUES (
			_createdBy,
			_firstName,
			_lastName,
			_updatedBy,
			_username);
        	select LAST_INSERT_ID() AS UserId;
        END $$
    
DELIMITER ;

    	
    	
    	DROP PROCEDURE IF EXISTS `spFindUser`;
        DELIMITER $$
        CREATE PROCEDURE `spFindUser`(
    			_createdBy varchar(50),
			_firstName varchar(50),
			_lastName nvarchar(50),
			_updatedBy varchar(50),
			_username nvarchar(255)
    	)
    	BEGIN

    		SELECT * FROM `user` WHERE
    			(ISNULL(_createdBy) = 1 OR `CreatedBy` = _createdBy) AND 
			(ISNULL(_firstName) = 1 OR `FirstName` = _firstName) AND 
			(ISNULL(_lastName) = 1 OR `LastName` = _lastName) AND 
			(ISNULL(_updatedBy) = 1 OR `UpdatedBy` = _updatedBy) AND 
			(ISNULL(_username) = 1 OR `Username` = _username);
        END $$
    
DELIMITER ;

		
    	
    	DROP PROCEDURE IF EXISTS `spUpdateUser`;
        DELIMITER $$
        CREATE PROCEDURE `spUpdateUser`(
    			_firstName varchar(50),
			_lastName nvarchar(50),
			_updatedBy varchar(50),
			_userId INT,
			_username nvarchar(255)
    	)
    	BEGIN

    		UPDATE `user` SET
    			`FirstName` = _firstName,
			`LastName` = _lastName,
			`UpdatedBy` = _updatedBy,
			`Username` = _username
		WHERE UserId = _userId;
		END $$
	
DELIMITER ;


			
				
				
				
				
				
				
				
				
			

		DELIMITER ;
		DROP TRIGGER IF EXISTS User_insert;
		DELIMITER $$
		CREATE TRIGGER User_insert BEFORE INSERT ON `user`
		FOR EACH ROW  BEGIN

	
			SET NEW.UpdatedDate = NOW();
            SET NEW.CreatedDate = NOW();
        
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'User', 'UserId', 'CreatedBy', NEW.`CreatedBy`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'User', 'UserId', 'CreatedDate', NEW.`CreatedDate`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'User', 'UserId', 'FirstName', NEW.`FirstName`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'User', 'UserId', 'LastName', NEW.`LastName`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'User', 'UserId', 'UpdatedBy', NEW.`UpdatedBy`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'User', 'UserId', 'UpdatedDate', NEW.`UpdatedDate`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'User', 'UserId', 'UserId', NEW.`UserId`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'User', 'UserId', 'Username', NEW.`Username`, NULL;
			
			END
			$$
			DELIMITER ;
		

			
				
				
				
				
				
				
				
				
       		

		DELIMITER ;
		DROP TRIGGER IF EXISTS User_update;
		DELIMITER $$
		CREATE TRIGGER User_update BEFORE UPDATE ON `user`
		FOR EACH ROW  BEGIN

	
       		SET NEW.UpdatedDate = NOW();
       	
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'User', 'UserId', 'CreatedBy', NEW.`CreatedBy`,
					OLD.`CreatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'User', 'UserId', 'CreatedDate', NEW.`CreatedDate`,
					OLD.`CreatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'User', 'UserId', 'FirstName', NEW.`FirstName`,
					OLD.`FirstName`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'User', 'UserId', 'LastName', NEW.`LastName`,
					OLD.`LastName`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'User', 'UserId', 'UpdatedBy', NEW.`UpdatedBy`,
					OLD.`UpdatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'User', 'UserId', 'UpdatedDate', NEW.`UpdatedDate`,
					OLD.`UpdatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'User', 'UserId', 'UserId', NEW.`UserId`,
					OLD.`UserId`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'User', 'UserId', 'Username', NEW.`Username`,
					OLD.`Username`;
			
			END
			$$
			DELIMITER ;
		

			
				
				
				
				
				
				
				
				

		DELIMITER ;
		DROP TRIGGER IF EXISTS User_delete;
		DELIMITER $$
		CREATE TRIGGER User_delete BEFORE DELETE ON `user`
		FOR EACH ROW  BEGIN
	
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'User', 'UserId', 'CreatedBy', NULL,
				OLD.`CreatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'User', 'UserId', 'CreatedDate', NULL,
				OLD.`CreatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'User', 'UserId', 'FirstName', NULL,
				OLD.`FirstName`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'User', 'UserId', 'LastName', NULL,
				OLD.`LastName`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'User', 'UserId', 'UpdatedBy', NULL,
				OLD.`UpdatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'User', 'UserId', 'UpdatedDate', NULL,
				OLD.`UpdatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'User', 'UserId', 'UserId', NULL,
				OLD.`UserId`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'User', 'UserId', 'Username', NULL,
				OLD.`Username`;
			
			END
			$$
			DELIMITER ;
		