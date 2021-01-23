
DELIMITER ;

		

	DELIMITER $$

	DROP PROCEDURE IF EXISTS `tmp_sp_create_table_videotype` $$
	CREATE PROCEDURE tmp_sp_create_table_videotype()
	BEGIN

		IF NOT EXISTS (SELECT NULL FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'videotype') THEN

			CREATE TABLE `videotype`(
				`VideoTypeId` VARCHAR(50) NOT NULL, 
			  `Description` nvarchar(50) NULL,
			  PRIMARY KEY  (`videotypeId`)
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;



				INSERT INTO `videotype`(VideoTypeId, Description)
				SELECT 'REGULAR', 'Regular';
			

				INSERT INTO `videotype`(VideoTypeId, Description)
				SELECT 'CHILDRENS_MOVIE', 'Children''s Movie';
			

				INSERT INTO `videotype`(VideoTypeId, Description)
				SELECT 'NEW_RELEASE', 'New Release';
			

			END IF;

		END$$

		DELIMITER ;

		CALL tmp_sp_create_table_videotype();

		DROP PROCEDURE tmp_sp_create_table_videotype;
	