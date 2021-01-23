
DELIMITER ;

		

	DELIMITER $$

	DROP PROCEDURE IF EXISTS `tmp_sp_create_table_videogenre` $$
	CREATE PROCEDURE tmp_sp_create_table_videogenre()
	BEGIN

		IF NOT EXISTS (SELECT NULL FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'videogenre') THEN

			CREATE TABLE `videogenre`(
				`VideoGenreId` VARCHAR(50) NOT NULL, 
			  `Description` nvarchar(50) NULL,
			  PRIMARY KEY  (`videogenreId`)
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;



				INSERT INTO `videogenre`(VideoGenreId, Description)
				SELECT 'ACTION', 'Action';
			

				INSERT INTO `videogenre`(VideoGenreId, Description)
				SELECT 'DRAMA', 'Drama';
			

				INSERT INTO `videogenre`(VideoGenreId, Description)
				SELECT 'ROMANCE', 'Romance';
			

				INSERT INTO `videogenre`(VideoGenreId, Description)
				SELECT 'COMEDY', 'Comedy';
			

				INSERT INTO `videogenre`(VideoGenreId, Description)
				SELECT 'HORROR', 'Horror';
			

			END IF;

		END$$

		DELIMITER ;

		CALL tmp_sp_create_table_videogenre();

		DROP PROCEDURE tmp_sp_create_table_videogenre;
	