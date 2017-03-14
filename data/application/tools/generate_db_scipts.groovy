import groovy.io.FileType

DIR_CONFIGURATION = "application/configuration"
DIR_TEMP = ".temp/sql"
DEFAULT_LOCALE_SHORT="en"
DEFAULT_LOCALE_FULL="en_US"
CRLF = String.format("%n")
TAB = "\t"
NUMERIC_TYPES = new HashSet(["INT","BIGINT","TINYINT","BOOL","BOOLEAN","SMALLINT","MEDIUMINT","INTEGER","REAL","SERIAL","FLOAT","DOUBLE","DOUBLE PRECISION","DECIMAL","NUMERIC"])


log("Running Update Groovy Script")

projectPath = "."

log("Using Script Path: ${projectPath}")

outputDirPath = System.properties['java.io.tmpdir']

serverName = ""
databaseName = ""
username = ""
password = ""

configurations = []

update()

def update(){

	initialize()

	execute()
}

def initialize(){

	String dbPropertiesFilePath = "${projectPath}/configuration/database.properties"

	Properties dbProperties = loadPropertiesFile(dbPropertiesFilePath)

	if(null == dbProperties){
		String errMsg = "Unbale to load DB Properties File: ${dbPropertiesFilePath}"
		log(errMsg)
		throw new RuntimeException(errMsg)
	}

	log("Using Database Properties ${dbPropertiesFilePath}")

	String url = dbProperties['jdbc.url']

	def urlParts = url.tokenize("//")

	if(urlParts.size() < 2) {

		String errMsg = "Invalid database connection URL: ${url}"
		log(errMsg)
		throw new RuntimeException(errMsg)
	}

	serverName = urlParts[1].tokenize(";")[0]
	databaseName = dbProperties['jdbc.catalog']
	username = dbProperties['jdbc.username']
	password = dbProperties['jdbc.password']

	def configDirPath = DIR_CONFIGURATION + "/"
	
	def configDir = new File(configDirPath)

	if(!configDir.exists()) {

		throw new RuntimeException("Unable to locate configuration directory: ${configDirPath}. Exiting.")
	}
	
	def configFiles = new TreeMap();

	configDir.eachFile (FileType.FILES) { file ->
	  configFiles[file.name] = file
	}
	
	configDir.eachFile { file ->
		
		 if (file.isFile()) {
			 
			 log("Generating database objects for configuration file: ${file.name}\n")
			 
			 def configuration = new XmlParser().parse(file)
			 
			 def parentConfig = configuration.attribute("parentConfig");
			 
			 if(parentConfig != null) {
				 
				 log("Parent configuration: ${parentConfig} found for current configuration - loading parent configuration first")
				 
				 configFile = new File(configDirPath + "/" + parentConfig)
				 
				 if(!configFile.exists()) {
					 
					 log("Unable to locate configuration file: ${configDirPath} / ${parentConfig}. Skipping.")
				 
				 } else {
				 
				 	configurations << loadConfiguration(new XmlParser().parse(configDirPath + "/" + parentConfig), true)
				 }
				 
			 } else {
			 
				 log("No parent configuration found for current configuration.")
			 }
			 
			 configurations << loadConfiguration(configuration, false)
		 }
	 }
}

def loadConfiguration(configurationRoot, isReferenced) {

	def configuration = [:]
	
	configuration.dbServer = configurationRoot.attribute("dbServer")
	if(configuration.dbServer == null) {
		throw new RuntimeException("Config property: {dbServer} is not set. Exiting.")
	}
	
	configuration.dbName = configurationRoot.attribute("dbName")
	if(configuration.dbName == null) {
		throw new RuntimeException("Config property: {dbName} is not set. Exiting.")
	}
	
	configuration.dbUser = configurationRoot.attribute("dbUser")
	if(configuration.dbUser == null) {
		throw new RuntimeException("Config property: {dbUser} is not set. Exiting.")
	}
	
	configuration.dbPassword = configurationRoot.attribute("dbPassword")
	if(configuration.dbPassword == null) {
		throw new RuntimeException("Config property: {dbPassword} is not set. Exiting.")
	}
	
	configuration.dbSuperUser = configurationRoot.attribute("dbSuperUser")
	if(configuration.dbSuperUser == null) {
		throw new RuntimeException("Config property: {dbSuperUser} is not set. Exiting.")
	}
	
	configuration.dbSuperUserPassword = configurationRoot.attribute("dbSuperUserPassword")
	if(configuration.dbSuperUserPassword == null) {
		throw new RuntimeException("Config property: {dbSuperUserPassword} is not set. Exiting.")
	}
	
	configuration.recreateObjects = configurationRoot.attribute("recreateObjects") == "true"
	
	configuration.deployed = configurationRoot.attribute("deployed") == "true"
	
	configuration.usesShortLocale = configurationRoot.attribute("shortLocale") == "true"
	
	configuration.defaultLocale = (configurationRoot.attribute("defaultLocale") == null ? 
		(configuration.usesShortLocale ? DEFAULT_LOCALE_SHORT : DEFAULT_LOCALE_FULL) : configurationRoot.attribute("defaultLocale"))
	
	configuration.resetData = configurationRoot.attribute("resetData") == "true"
	
	configuration.tables = [:]
	
	configurationRoot.table.each {
		
		def table = [:]
		
		table.isParent = isReferenced
		
		table.name = it.attribute("name")
		
		table.type = it.attribute("type").toLowerCase()
		
		log("Loading data for ${table.type} table ${table.name}")
		
		table.isExtended = it.attribute("extended") == "true"
		
		table.recreateTable = it.attribute("recreateTable") == null || it.attribute("recreateTable") == "true"
		
		//Get Table Fields----------------------------------------------------------------------
		table.fields = new TreeMap([compare: { o1 , o2 ->
	            String s1 = (String) o1;
	            String s2 = (String) o2;
	            return s1.toLowerCase().compareTo(s2.toLowerCase());
	        }] as Comparator)
		it.fields.field.each {
			
			def field = [:]
			
			field.name = it.attribute("fieldName")
			field.dataType = it.attribute("dataType")
			field.isNullable = it.attribute("isNull") == "true"
			field.defaultValue = it.attribute("default")
			table.fields[field.name] = field
		}
		
		//Get Table relationships ---------------------------------------------------------------------
		table.relationships = []
		it.relationships.relationship.each {
			
			def relationship = [:]
			
			relationship.parentTable = it.attribute("parentTable")
			relationship.fkField = it.attribute("fkField")
			table.relationships << relationship
		}
		
		//Get Table features ---------------------------------------------------------------------
		table.isAudited = false
		table.isConcurrent = false
		table.isArchived = false
		table.isInternationalized = false
		it.features.feature.each {
			
			switch (it.attribute("name")) {
				case "Concurrent":
					table.isConcurrent = true
					break
				case "Audit":
					table.isAudited = true
					break

				default:
					log("Invalid or missing table feature type ${it.name}. Ignoring.")
			}
		}
		
		if(table.isAudited) {
			
			def createdBy = [:]
			createdBy.name = "CreatedBy"
			createdBy.dataType = "varchar(50)"
			createdBy.isNullable = false
			table.fields[createdBy.name] = createdBy
			
			def createdDate = [:]
			createdDate.name = "CreatedDate"
			createdDate.dataType = getFieldType("DATETIME")
			createdDate.isNullable = false
			table.fields[createdDate.name] = createdDate
			
			def updatedBy = [:]
			updatedBy.name = "UpdatedBy"
			updatedBy.dataType = "varchar(50)"
			updatedBy.isNullable = false
			table.fields[updatedBy.name] = updatedBy
			
			def updatedDate = [:]
			updatedDate.name = "UpdatedDate"
			updatedDate.dataType = getFieldType("DATETIME")
			updatedDate.isNullable = true
			table.fields[updatedDate.name] = updatedDate
		}

		//Get Reference Values --------------------------------------------------------------------------------
		table.referenceValues = []
		it.values.value.each {
			
			def value = [:]
			
			value.name = it.attribute("name")
			value.description = it.attribute("description")
			table.referenceValues << value
		}
		
		//Get Excluded objects --------------------------------------------------------------------------------
		table.excludeObjects = []
		it.excludeObjects.object.each {
			
			def excludeObject = [:]
			
			switch (it.attribute("name")) {
				case "view":
					table.isExcludeViews = true
					break
				case "procedures":
					table.isExcludeProcedures = true
					break
				case "triggers":
					table.isExcludeTriggers = true
					break
				case "model":
					table.isExcludeModels = true
					break
				case "mapping":
					table.isExcludeMappers = true
					break
				default:
					log("Unrecognized table exclude object type type ${it.name} provided. Ignoring.")
			}
		}
		
		configuration.tables[table.name] = table
	}
	
	return configuration
}


def execute() {
	
	configurations.each { configuration ->
		
		// drop existing tables (if required)
		if(!configuration.deployed && configuration.recreateObjects) {
			
			dropTables(configuration)
		}
		
		// reset existing tables (if required)
		if(!configuration.deployed) {
			
			if(configuration.resetData) {
				
				log("Resetting Data")
				
				resetData(configuration)
			}
			
		} else {
		
			log("Project in deployed mode. Skipping table recreation and data reset.")
		}
		
		// create new tables, or for deployed mode, create new table scripts
		configuration.tables.each { name, table ->
			
			if(!table.isParent) {
				
				if ((configuration.deployed && table.recreateTable) || !isTableExist(configuration, table.name)) {
					
					log("Creating table " + table.name)
					
					createTable(configuration, table)
				
				} else {
				 	log("Skipping table generation for table: " + table.name + ". Table already exists.")
				}
			}
		}
	}
}

static void log(message) {

   println message             
}

static String toVariableCase( String text ) {
    text[0].toLowerCase() + text[1..text.length() -1 ]
}

static String toProperCase( String text ) {
	text[0].toUpperCase() + text[1..text.length() -1 ]
}

def isNumericType(dataType) {

	dataType = dataType.toUpperCase()
	
	int pos = dataType.indexOf('(');

	return pos > 0 ? NUMERIC_TYPES.contains(dataType.substring(0, dataType.indexOf('('))) : dataType
	
}


def createTable(configuration, table) {

	def sql = """

	DELIMITER \$\$

	DROP PROCEDURE IF EXISTS `tmp_sp_create_table_${table.name.toLowerCase()}` \$\$
	CREATE PROCEDURE tmp_sp_create_table_${table.name.toLowerCase()}()
	BEGIN

		IF NOT EXISTS (SELECT NULL FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = '${table.name.toLowerCase()}') THEN

			CREATE TABLE `${table.name.toLowerCase()}`(
	"""

	if(table.type == "entity") {
					
		sql += "\t\t\t`${table.name}Id` INT NOT NULL auto_increment, ${CRLF}\t\t\t"
	
	} else if(table.type == "reference") {
	
		sql += "\t\t\t`${table.name}Id` VARCHAR(50) NOT NULL, ${CRLF}\t\t\t"
	}

	table.fields.each { fieldName, field ->

		sql += "  `${field.name}` ${field.dataType}"

		if(field.isNullable) {
			sql += " NULL"
		} else {
			sql += " NOT NULL"
		}

		if(field.defaultValue != null && !field.defaultValue.trim().isEmpty()) {

			if(isNumericType(field.dataType)) {

				sql += " DEFAULT ${field.defaultValue}"

			} else {

				sql += " DEFAULT '${field.defaultValue}'"
			}

		}

		sql += ",${CRLF}\t\t\t"
	}
					
	//Primary Key Constraint
	sql = sql + "  PRIMARY KEY  (`${table.name.toLowerCase()}Id`)${CRLF}\t\t\t"

	def referencedTable
	def missingReference = false
						
	table.relationships.each { relationship ->

		if(table.name.toLowerCase() != relationship.parentTable.toLowerCase()) {
					
			referencedTable = configuration.tables[relationship.parentTable]

			if(referencedTable == null) {
			
				log("Warning! - Unable to find referenced table definition: ${relationship.parentTable} in this configuration file. Continuing if table exists in db. (may be in separate configuration file)")
				missingReference = true
			}
			
		}

		sql = sql + ",  FOREIGN KEY (${relationship.fkField}) REFERENCES `${relationship.parentTable.toLowerCase()}`("

		sql = sql + "${relationship.parentTable.toLowerCase()}Id)${CRLF}\t\t\t"

	}

	sql = "${sql}) ENGINE=InnoDB DEFAULT CHARSET=utf8;${CRLF}${CRLF}"

	if(table.type != "entity") {

		//add pre-populated reference values
		table.referenceValues.each { referenceValue ->

			def desc = referenceValue.description.replace("'","''")

			sql += """

				INSERT INTO `${table.name.toLowerCase()}`(${table.name}Id, Description)
				SELECT '${referenceValue.name}', '${desc}';
			"""
		}

	}

	sql = """
		${sql}

			END IF;

		END\$\$

		DELIMITER ;

		CALL tmp_sp_create_table_${table.name.toLowerCase()}();

		DROP PROCEDURE tmp_sp_create_table_${table.name.toLowerCase()};
	"""
	
	executeQuery(configuration, table.name.toLowerCase(), sql, true)

	if(table.type == "entity") {

		//create views
		if(!table.isExcludeViews) {
			createView(configuration, table)
		}
					
		//create stored procedures
		if(!table.isExcludeProcedures) {
			createSP(configuration, table)
		}

	}

	//create triggers
	if(!table.isExcludeTriggers) {
		createTriggers(configuration, table)
	}

}
					
def createView(configuration, table) {

	def sql = """
		CREATE OR REPLACE ALGORITHM=UNDEFINED VIEW `v${table.name.toLowerCase()}`
			AS SELECT * FROM `${table.name.toLowerCase()}`;
		"""

	executeQuery(configuration, table.name.toLowerCase(), sql)
}


def createTriggers(configuration, table) {

	if(table.isExcludeTriggers)
		return

	boolean hasInsertTrigger = false
	boolean hasUpdateTrigger = false
	boolean hasDeleteTrigger = false

	def insertTriggerSql = """

		DELIMITER ;
		DROP TRIGGER IF EXISTS ${table.name}_insert;
		DELIMITER \$\$
		CREATE TRIGGER ${table.name}_insert BEFORE INSERT ON `${table.name.toLowerCase()}`
		FOR EACH ROW  BEGIN

	"""

	def updateTriggerSql = """

		DELIMITER ;
		DROP TRIGGER IF EXISTS ${table.name}_update;
		DELIMITER \$\$
		CREATE TRIGGER ${table.name}_update BEFORE UPDATE ON `${table.name.toLowerCase()}`
		FOR EACH ROW  BEGIN

	"""

	def deleteTriggerSql = """

		DELIMITER ;
		DROP TRIGGER IF EXISTS ${table.name}_delete;
		DELIMITER \$\$
		CREATE TRIGGER ${table.name}_delete BEFORE DELETE ON `${table.name.toLowerCase()}`
		FOR EACH ROW  BEGIN
	"""

	if(table.isAudited) {

        hasInsertTrigger = true
    	hasUpdateTrigger = true
    	hasDeleteTrigger = true

		insertTriggerSql =  """
			${insertTriggerSql}
			SET NEW.UpdatedDate = NOW();
            SET NEW.CreatedDate = NOW();
        """

       	updateTriggerSql =  """
       		${updateTriggerSql}
       		SET NEW.UpdatedDate = NOW();
       	"""

		table.fields.each { fieldName, field ->
		
			insertTriggerSql =  """
				${insertTriggerSql}
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'I', '${table.name}', '${table.name}Id', '${field.name}', NEW.`${field.name}`, NULL;
			"""

			updateTriggerSql = """
				${updateTriggerSql}
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'U', '${table.name}', '${table.name}Id', '${field.name}', NEW.`${field.name}`,
					OLD.`${field.name}`;
			"""

			deleteTriggerSql = """
				${deleteTriggerSql}
				INSERT INTO audit (LoginID, `DATETIME`, `ACTION`, TableName, PrimaryKey, ColumnName, NewValue, OldValue)
				SELECT SYSTEM_USER(), NOW(), 'D', '${table.name}', '${table.name}Id', '${field.name}', NULL,
				OLD.`${field.name}`;
			"""

		}

		table.fields.each { fieldName, field ->

			if(field.dataType == "timestamp" && field.defaultValue != null && !field.defaultValue.trim().isEmpty()) {

				hasInsertTrigger = true

				insertTriggerSql =  "${insertTriggerSql} SET NEW.${field.name} = NOW();"
			}
		}

		insertTriggerSql = """
			${insertTriggerSql}
			END
			\$\$
			DELIMITER ;
		"""

		updateTriggerSql = """
			${updateTriggerSql}
			END
			\$\$
			DELIMITER ;
		"""

		deleteTriggerSql = """
			${deleteTriggerSql}
			END
			\$\$
			DELIMITER ;
		"""
		
	    def triggerSql = ""

	    if(hasInsertTrigger)
	    	triggerSql = "${triggerSql}${CRLF}${insertTriggerSql}"

	    if(hasUpdateTrigger)
	    	triggerSql = "${triggerSql}${CRLF}${updateTriggerSql}"

	    if(hasDeleteTrigger)
	    	triggerSql = "${triggerSql}${CRLF}${deleteTriggerSql}"

        if(!triggerSql.isEmpty())
			executeQuery(configuration, table.name.toLowerCase(), triggerSql)
	}
}



def createSP(configuration, table) {

println("##################${table.name}")

	//Create delete sp  ------------------------------------------------------------------------------------------------------
    def createSpSql = """
    	DROP PROCEDURE IF EXISTS `spDelete${table.name}`;
        DELIMITER \$\$
        CREATE PROCEDURE `spDelete${table.name}`(_${table.name}Id  INT)
        BEGIN
        	    DELETE FROM `${table.name.toLowerCase()}`
        	    WHERE ${table.name}Id = _${table.name}Id;
        END \$\$
    """

	executeQuery(configuration, table.name.toLowerCase(), createSpSql)
		

	//Create Get sp  ------------------------------------------------------------------------------------------------------
    createSpSql = """
    	DROP PROCEDURE IF EXISTS `spGet${table.name}`;
        DELIMITER \$\$
        CREATE PROCEDURE `spGet${table.name}`(_${table.name}Id  INT)
        BEGIN
        	    SELECT * FROM `${table.name.toLowerCase()}`
        	    WHERE ${table.name}Id = _${table.name}Id;
        END \$\$
    """

	executeQuery(configuration, table.name.toLowerCase(), createSpSql)
		
	//Create Insert Sp --------------------------------------------------------------------------------------------------------
    createSpSql = """
    	DROP PROCEDURE IF EXISTS `spInsert${table.name}`;
        DELIMITER \$\$
        CREATE PROCEDURE `spInsert${table.name}`(
    """
		
	table.fields.each { fieldName, field ->
		//don't include updatedDate and createdDate
		if (field.name.toUpperCase() != "createdDate".toUpperCase() && field.name.toUpperCase()  != "updatedDate".toUpperCase() ) {

			createSpSql = "${createSpSql}\t\t\t_${toVariableCase(field.name)} ${field.dataType},${CRLF}"

		}
	}

	//remove the rightmost 2 characters
	createSpSql = createSpSql[0..-3]
		
    createSpSql = """
    	${createSpSql}
    	)
    	BEGIN

    		INSERT INTO `${table.name.toLowerCase()}`(
    """

	table.fields.each { fieldName, field ->

		//don't include updatedDate and createdDate
		if (field.name.toUpperCase() != "createdDate".toUpperCase() && field.name.toUpperCase()  != "updatedDate".toUpperCase()) {

			createSpSql = "${createSpSql}\t\t\t`${field.name}`,${CRLF}"
		}

	}
		
	//remove the rightmost 2 characters
	createSpSql = createSpSql[0..-3]

	createSpSql = "${createSpSql}${CRLF}\t\t\t) VALUES (${CRLF}"

	table.fields.each { fieldName, field ->

		//don't include updatedDate and createdDate
		if(field.name.toUpperCase() != "createdDate".toUpperCase() && field.name.toUpperCase() != "updatedDate".toUpperCase()) {

			createSpSql ="${createSpSql}\t\t\t_${toVariableCase(field.name)},${CRLF}"

		}

	}

	//remove the rightmost 2 characters
	createSpSql = createSpSql[0..-3]

    createSpSql = """
    	${createSpSql});
        	select LAST_INSERT_ID() AS ${table.name}Id;
        END \$\$
    """

	executeQuery(configuration, table.name.toLowerCase(), createSpSql)


	//Create Find Sp --------------------------------------------------------------------------------------------------------
    createSpSql = """
    	DROP PROCEDURE IF EXISTS `spFind${table.name}`;
        DELIMITER \$\$
        CREATE PROCEDURE `spFind${table.name}`(
    """

	table.fields.each { fieldName, field ->

		//don't include updatedDate and createdDate
		if(field.name.toUpperCase() != "createdDate".toUpperCase() && field.name.toUpperCase() != "updatedDate".toUpperCase()) {

			createSpSql = "${createSpSql}\t\t\t_${toVariableCase(field.name)} ${field.dataType},${CRLF}"

		}

	}

	//remove the righmost 2 characters
	createSpSql = createSpSql[0..-3]

    createSpSql = """
    	${createSpSql}
    	)
    	BEGIN

    		SELECT * FROM `${table.name.toLowerCase()}` WHERE
    """

	def selectList = ""
	def partitionList = ""
	def groupList

	table.fields.each { fieldName, field ->

		//don't include updatedDate and createdDate
		if(field.name.toUpperCase() != "createdDate".toUpperCase() && field.name.toUpperCase() != "updatedDate".toUpperCase()) {

            createSpSql =  "${createSpSql}\t\t\t(ISNULL(_${toVariableCase(field.name)}) = 1 OR `${field.name}` = _${toVariableCase(field.name)}) AND ${CRLF}"

		}
	}
	
	//remove the rightmost 6 characters
	createSpSql = createSpSql[0..-7]

    createSpSql = """
    	${createSpSql};
        END \$\$
    """

	executeQuery(configuration, table.name.toLowerCase(), createSpSql)


	//Create Update Sp --------------------------------------------------------------------------------------------------------

	//Add <tablename>Id field
	def newField = [:]
	newField.name = table.name +"Id"
	newField.dataType = "INT"
	table.fields[newField.name] = newField

    createSpSql = """
    	DROP PROCEDURE IF EXISTS `spUpdate${table.name}`;
        DELIMITER \$\$
        CREATE PROCEDURE `spUpdate${table.name}`(
    """

	table.fields.each { fieldName, field ->

		//don't include updatedDate, createdDate, createdBy
		if(field.name.toUpperCase() != "createdDate".toUpperCase() && field.name.toUpperCase() != "updatedDate".toUpperCase()
			&& field.name.toUpperCase() != "createdBy".toUpperCase()) {

			createSpSql = "${createSpSql}\t\t\t_${toVariableCase(field.name)} ${field.dataType},${CRLF}"

		}

	}

	//remove the righmost 2 characters
	createSpSql = createSpSql[0..-3]

    createSpSql = """
    	${createSpSql}
    	)
    	BEGIN

    		UPDATE `${table.name.toLowerCase()}` SET
    """

	table.fields.each { fieldName, field ->
		
		//don't include updatedDate and createdDate and MasterEntityId and locale and internationalized fields and indentity field
		if(field.name.toUpperCase() != "createdDate".toUpperCase() && field.name.toUpperCase() != "createdBy".toUpperCase() &&
			   field.name.toUpperCase() != "updatedDate".toUpperCase() && field.name.toUpperCase() != (table.name + "Id").toUpperCase()) {

			createSpSql = "${createSpSql}\t\t\t`${field.name}` = _${toVariableCase(field.name)},${CRLF}"
		 }
	}

	//remove the righmost 2 characters
	createSpSql = createSpSql[0..-3]

	createSpSql = """
		${createSpSql}
		WHERE ${table.name}Id = _${toVariableCase(table.name)}Id;
		END \$\$
	"""

	executeQuery(configuration, table.name.toLowerCase(), createSpSql)
}


def executeQuery(configuration, name, query, isReplace = false) {

	def tempDirPath = DIR_TEMP

	def tempDir = new File(tempDirPath)

	if(!tempDir.exists()) {

		tempDir.mkdir()
	}

	def scriptFile =  new File("${tempDirPath}/${name}.sql")

	log("Storing temp script: " + scriptFile.path)

	if(!scriptFile.exists()) {

		scriptFile.createNewFile()

	} else {

		if(isReplace) {

			scriptFile.delete()

			scriptFile.createNewFile()

		}

		scriptFile << "${CRLF}DELIMITER ;${CRLF}"
	}

	scriptFile << query
}

def getFieldType(dataType) {
   
	if(dataType.contains("date")) {
		return "timestamp"
	}

	return dataType
}

def loadPropertiesFile(filePath) {

	log("Loading Properties File: ${filePath}")

	def propertiesFile = new File(filePath)

	if(!propertiesFile.exists())
		return null

	def props = new Properties()

	propertiesFile.withInputStream {stream ->
		props.load(stream)
	}

	return props
}
