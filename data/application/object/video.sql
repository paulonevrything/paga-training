
DELIMITER ;

		

	DELIMITER $$

	DROP PROCEDURE IF EXISTS `tmp_sp_create_table_video` $$
	CREATE PROCEDURE tmp_sp_create_table_video()
	BEGIN

		IF NOT EXISTS (SELECT NULL FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'video') THEN

			CREATE TABLE `video`(
				`VideoId` INT NOT NULL auto_increment, 
			  `CreatedBy` varchar(50) NOT NULL,
			  `CreatedDate` DATETIME NOT NULL,
			  `ImgUrl` varchar(100) NULL,
			  `MaximumAge` int NULL,
			  `Title` varchar(100) NOT NULL,
			  `UpdatedBy` varchar(50) NOT NULL,
			  `UpdatedDate` DATETIME NULL,
			  `VideoGenreId` varchar(50) NOT NULL,
			  `VideoTypeId` varchar(50) NOT NULL,
			  `YearReleased` int NULL,
			  PRIMARY KEY  (`videoId`)
			,  FOREIGN KEY (VideoTypeId) REFERENCES `videotype`(videotypeId)
			,  FOREIGN KEY (VideoGenreId) REFERENCES `videogenre`(videogenreId)
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;



			END IF;

		END$$

		DELIMITER ;

		CALL tmp_sp_create_table_video();

		DROP PROCEDURE tmp_sp_create_table_video;
	
DELIMITER ;

		CREATE OR REPLACE ALGORITHM=UNDEFINED VIEW `vvideo`
			AS SELECT * FROM `video`;
		
DELIMITER ;

    	DROP PROCEDURE IF EXISTS `spDeleteVideo`;
        DELIMITER $$
        CREATE PROCEDURE `spDeleteVideo`(_VideoId  INT)
        BEGIN
        	    DELETE FROM `video`
        	    WHERE VideoId = _VideoId;
        END $$
    
DELIMITER ;

    	DROP PROCEDURE IF EXISTS `spGetVideo`;
        DELIMITER $$
        CREATE PROCEDURE `spGetVideo`(_VideoId  INT)
        BEGIN
        	    SELECT * FROM `video`
        	    WHERE VideoId = _VideoId;
        END $$
    
DELIMITER ;

    	
    	
    	DROP PROCEDURE IF EXISTS `spInsertVideo`;
        DELIMITER $$
        CREATE PROCEDURE `spInsertVideo`(
    			_createdBy varchar(50),
			_imgUrl varchar(100),
			_maximumAge int,
			_title varchar(100),
			_updatedBy varchar(50),
			_videoGenreId varchar(50),
			_videoTypeId varchar(50),
			_yearReleased int
    	)
    	BEGIN

    		INSERT INTO `video`(
    			`CreatedBy`,
			`ImgUrl`,
			`MaximumAge`,
			`Title`,
			`UpdatedBy`,
			`VideoGenreId`,
			`VideoTypeId`,
			`YearReleased`
			) VALUES (
			_createdBy,
			_imgUrl,
			_maximumAge,
			_title,
			_updatedBy,
			_videoGenreId,
			_videoTypeId,
			_yearReleased);
        	select LAST_INSERT_ID() AS VideoId;
        END $$
    
DELIMITER ;

    	
    	
    	DROP PROCEDURE IF EXISTS `spFindVideo`;
        DELIMITER $$
        CREATE PROCEDURE `spFindVideo`(
    			_createdBy varchar(50),
			_imgUrl varchar(100),
			_maximumAge int,
			_title varchar(100),
			_updatedBy varchar(50),
			_videoGenreId varchar(50),
			_videoTypeId varchar(50),
			_yearReleased int
    	)
    	BEGIN

    		SELECT * FROM `video` WHERE
    			(ISNULL(_createdBy) = 1 OR `CreatedBy` = _createdBy) AND 
			(ISNULL(_imgUrl) = 1 OR `ImgUrl` = _imgUrl) AND 
			(ISNULL(_maximumAge) = 1 OR `MaximumAge` = _maximumAge) AND 
			(ISNULL(_title) = 1 OR `Title` = _title) AND 
			(ISNULL(_updatedBy) = 1 OR `UpdatedBy` = _updatedBy) AND 
			(ISNULL(_videoGenreId) = 1 OR `VideoGenreId` = _videoGenreId) AND 
			(ISNULL(_videoTypeId) = 1 OR `VideoTypeId` = _videoTypeId) AND 
			(ISNULL(_yearReleased) = 1 OR `YearReleased` = _yearReleased);
        END $$
    
DELIMITER ;

		
    	
    	DROP PROCEDURE IF EXISTS `spUpdateVideo`;
        DELIMITER $$
        CREATE PROCEDURE `spUpdateVideo`(
    			_imgUrl varchar(100),
			_maximumAge int,
			_title varchar(100),
			_updatedBy varchar(50),
			_videoGenreId varchar(50),
			_videoId INT,
			_videoTypeId varchar(50),
			_yearReleased int
    	)
    	BEGIN

    		UPDATE `video` SET
    			`ImgUrl` = _imgUrl,
			`MaximumAge` = _maximumAge,
			`Title` = _title,
			`UpdatedBy` = _updatedBy,
			`VideoGenreId` = _videoGenreId,
			`VideoTypeId` = _videoTypeId,
			`YearReleased` = _yearReleased
		WHERE VideoId = _videoId;
		END $$
	
DELIMITER ;


			
				
				
				
				
				
				
				
				
				
				
				
			

		DELIMITER ;
		DROP TRIGGER IF EXISTS Video_insert;
		DELIMITER $$
		CREATE TRIGGER Video_insert BEFORE INSERT ON `video`
		FOR EACH ROW  BEGIN

	
			SET NEW.UpdatedDate = NOW();
            SET NEW.CreatedDate = NOW();
        
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'CreatedBy', NEW.`CreatedBy`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'CreatedDate', NEW.`CreatedDate`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'ImgUrl', NEW.`ImgUrl`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'MaximumAge', NEW.`MaximumAge`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'Title', NEW.`Title`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'UpdatedBy', NEW.`UpdatedBy`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'UpdatedDate', NEW.`UpdatedDate`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'VideoGenreId', NEW.`VideoGenreId`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'VideoId', NEW.`VideoId`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'VideoTypeId', NEW.`VideoTypeId`, NULL;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', 'Video', 'VideoId', 'YearReleased', NEW.`YearReleased`, NULL;
			
			END
			$$
			DELIMITER ;
		

			
				
				
				
				
				
				
				
				
				
				
				
       		

		DELIMITER ;
		DROP TRIGGER IF EXISTS Video_update;
		DELIMITER $$
		CREATE TRIGGER Video_update BEFORE UPDATE ON `video`
		FOR EACH ROW  BEGIN

	
       		SET NEW.UpdatedDate = NOW();
       	
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'CreatedBy', NEW.`CreatedBy`,
					OLD.`CreatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'CreatedDate', NEW.`CreatedDate`,
					OLD.`CreatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'ImgUrl', NEW.`ImgUrl`,
					OLD.`ImgUrl`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'MaximumAge', NEW.`MaximumAge`,
					OLD.`MaximumAge`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'Title', NEW.`Title`,
					OLD.`Title`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'UpdatedBy', NEW.`UpdatedBy`,
					OLD.`UpdatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'UpdatedDate', NEW.`UpdatedDate`,
					OLD.`UpdatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'VideoGenreId', NEW.`VideoGenreId`,
					OLD.`VideoGenreId`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'VideoId', NEW.`VideoId`,
					OLD.`VideoId`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'VideoTypeId', NEW.`VideoTypeId`,
					OLD.`VideoTypeId`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', 'Video', 'VideoId', 'YearReleased', NEW.`YearReleased`,
					OLD.`YearReleased`;
			
			END
			$$
			DELIMITER ;
		

			
				
				
				
				
				
				
				
				
				
				
				

		DELIMITER ;
		DROP TRIGGER IF EXISTS Video_delete;
		DELIMITER $$
		CREATE TRIGGER Video_delete BEFORE DELETE ON `video`
		FOR EACH ROW  BEGIN
	
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'CreatedBy', NULL,
				OLD.`CreatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'CreatedDate', NULL,
				OLD.`CreatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'ImgUrl', NULL,
				OLD.`ImgUrl`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'MaximumAge', NULL,
				OLD.`MaximumAge`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'Title', NULL,
				OLD.`Title`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'UpdatedBy', NULL,
				OLD.`UpdatedBy`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'UpdatedDate', NULL,
				OLD.`UpdatedDate`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'VideoGenreId', NULL,
				OLD.`VideoGenreId`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'VideoId', NULL,
				OLD.`VideoId`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'VideoTypeId', NULL,
				OLD.`VideoTypeId`;
			
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', 'Video', 'VideoId', 'YearReleased', NULL,
				OLD.`YearReleased`;
			
			END
			$$
			DELIMITER ;
		