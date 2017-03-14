import groovy.io.FileType

DIR_DATA = "data"
DIR_CONFIGURATION = "application/configuration"
CRLF = String.format("%n")
TAB = "\t"
MAPPING_TYPE_SQL = "sql"
NUMERIC_TYPES = new HashSet(["INT","BIGINT","TINYINT","BOOL","BOOLEAN","SMALLINT","MEDIUMINT","INTEGER","REAL","SERIAL","FLOAT","DOUBLE","DOUBLE PRECISION","DECIMAL","NUMERIC"])

projectDirPath = ".."

configurations = []

build()

def build(){

	initialize()

	execute()
}

def initialize(){

	def configDirPath = DIR_CONFIGURATION + "/"

	def configDir = new File(configDirPath)

	if(!configDir.exists()) {
		
		log("Unable to locate configuration directory: ${configDirPath}. Exiting.")
		System.exit(1)
	}
	
	def configFiles = new TreeMap([compare: { o1 , o2 ->
	            String s1 = (String) o1;
	            String s2 = (String) o2;
	            return s1.toLowerCase().compareTo(s2.toLowerCase());
	        }] as Comparator);

	configDir.eachFile (FileType.FILES) { file ->
	  configFiles[file.name] = file
	}
	
	configDir.eachFile { file ->
		
		 if(file.isFile()) {
			 
			 log("Generating mapping objects for configuration file: ${file.name}\n")
			 
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
	
	configuration.rootNamespace = configurationRoot.attribute("rootNamespace")
	if(configuration.rootNamespace == null) {
		log("configuration property: {rootNamespace} is not set. Exiting.")
		System.exit(1)
	}
	
	configuration.sourcePath = configurationRoot.attribute("sourcePath")
	if(configuration.sourcePath == null) {
		log("configuration property: {sourcePath} is not set. Exiting.")
		System.exit(1)
	}
	
	configuration.modelNamespace = configurationRoot.attribute("modelNamespace")
	if(configuration.modelNamespace == null) {
		log("configuration property: {modelNamespace} is not set. Exiting.")
		System.exit(1)
	}
	
	def mappingNamespaces = configurationRoot.attribute("mappingNamespaces")
    if(mappingNamespaces == null) {
		log("configuration property: {mappingNamespaces} is not set. Exiting.")
		System.exit(1)
    }
	
	configuration.mappingNamespace = configuration.modelNamespace + "." + MAPPING_TYPE_SQL
	
	configuration.recreateObjects = configurationRoot.attribute("recreateObjects") == "true"
	
	configuration.shortLocale = configurationRoot.attribute("shortLocale") == "true"
	
	configuration.deployed = configurationRoot.attribute("deployed") == "true"
	
	
	configuration.tables = [:]
	
	configurationRoot.table.each {
		
		def table = [:]
		
		table.isParent = isReferenced
		
		table.name = toProperCase(it.attribute("name"))
		
		table.type = it.attribute("type").toLowerCase()
		
		log("Loading data for ${table.type} table ${table.name}")
		
		table.isExtended = it.attribute("extended") == "true"
		
		table.recreateTable = it.attribute("recreateTable") == null || it.attribute("recreateTable") == "true"
		
		table.sourcePath = it.attribute("sourcePath")
		
		table.recreateMapper = it.attribute("recreateMapper") == null || it.attribute("recreateMapper") == "true"
		
		it.mappingClasses.mapping.each { mapping ->
		
			if(mapping.attribute("type") == MAPPING_TYPE_SQL) {
				
				table.mappingClass = mapping.attribute("class")
			
				if(table.mappingClass == null) {
					log("Config Property: {mappingClasses/mapping} missing 'class' attribute for table ${table.name}")
					System.exit(1)
				}
			}
		}

		table.modelNamespace = it.attribute("modelNamespace")
		
		table.isIgnoreModel = it.attribute("isIgnoreModel")
		
		table.isIgnoreMapper = it.attribute("isIgnoreMapper")
		
		table.clazz = it.attribute("class")
		
		table.isInternationalized = it.attribute("isInternationalized") == "true"	
	
		//Get Table Fields----------------------------------------------------------------------
		table.fields = new TreeMap([compare: { o1 , o2 ->
	            String s1 = (String) o1;
	            String s2 = (String) o2;
	            return s1.toLowerCase().compareTo(s2.toLowerCase());
	        }] as Comparator)
	  
		it.fields.field.each {
			
			def field = [:]
			
			field.name = it.attribute("fieldName")
			field.dataType = getFieldType(it.attribute("dataType"))
			field.javaSQLType = getJavaSQLType(it.attribute("dataType"))
			field.isNullable = it.attribute("isNull") == "true"
			field.defaultValue = it.attribute("default")
			field.isExcludeFromModel = false
			field.isRefToTypeEnum = false
			if(field.dataType == "Date") {
				table.hasDateType = true
			}
		
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
				case "MultiCalendar":
					table.isMultiCalendar = true
					break
				case "Audit":
					table.isAudited = true
					break
				case "Archived":
					table.isArchived = true
					it.field.each {
						def name = it.attribute("name")
						def field = table.fields[name]
						if(field == null) {
							log("Archived field name ${name} is not a valid field name for this table. Skipping.")
						} else {
							field.isArchived = true;
						}
					}
					break
				case "Internationalized":
					table.isInternationalized = true
					it.field.each {
						def name = it.attribute("name")
						def field = table.fields[name]
						if(field == null) {
							log("Internationalized field name ${name} is not a valid field name for this table. Skipping.")
						} else {
							field.isInternationalized = true;
						}
					}
					break
					
				default:
					log("Invalid or missing table feature type ${it.name}. Ignoring.")
			}
		}
		
		
		if(table.isAudited) {
			
			def createdBy = [:]
			createdBy.name = "CreatedBy"
			createdBy.dataType = "String"
			createdBy.isNullable = false
			createdBy.isExcludeFromModel = true
			createdBy.isRefToTypeEnum = false
			table.fields[createdBy.name] = createdBy
			
			def createdDate = [:]
			createdDate.name = "CreatedDate"
			createdDate.dataType = getFieldType("DATETIME")
			createdDate.isNullable = false
			createdDate.isExcludeFromModel = true
			createdDate.isRefToTypeEnum = false
			table.fields[createdDate.name] = createdDate
			
			def updatedBy = [:]
			updatedBy.name = "UpdatedBy"
			updatedBy.dataType = "String"
			updatedBy.isNullable = false
			updatedBy.isExcludeFromModel = true
			updatedBy.isRefToTypeEnum = false
			table.fields[updatedBy.name] = updatedBy
			
			def updatedDate = [:]
			updatedDate.name = "UpdatedDate"
			updatedDate.dataType = getFieldType("DATETIME")
			updatedDate.isNullable = true
			updatedDate.isExcludeFromModel = true
			updatedDate.isRefToTypeEnum = false
			table.fields[updatedDate.name] = updatedDate
		}
		
		if(table.isArchived) {
			
			def archivingStatusId = [:]
			archivingStatusId.name = "ArchivingStatusId"
			archivingStatusId.dataType = "ArchivingStatus"
			archivingStatusId.isNullable = false
			tempField.isRefToTypeEnum = true
			tempField.javaSQLType = "Types.VARCHAR"
			table.fields[archivingStatusId.name] = archivingStatusId
			
			def masterEntityId = [:]
			masterEntityId.name = "MasterEntityId"
			masterEntityId.dataType = "Integer"
			masterEntityId.isNullable = true
			tempField.isRefToTypeEnum = false
			tempField.javaSQLType = "Types.INTEGER"
			table.fields[masterEntityId.name] = masterEntityId
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
	
	configuration.tables.each { tableName, table ->
		
		table.relationships.each { relationship ->
			
			if(table.name.toLowerCase() != relationship.parentTable.toLowerCase()) {
			
				def referenced = configuration.tables[relationship.parentTable]
			
				if(referenced != null && referenced.type == "reference") {
		
					table.fields.each { fieldName, field ->
						
						if(field.name.toLowerCase() == relationship.fkField.toLowerCase()) {
							
							field.isRefToTypeEnum = true
							field.dataType = relationship.parentTable + "Enum"
						}
					}
				}
			}
		}
	}
	
	return configuration
}


def execute() {
	
	configurations.each { configuration ->
		
		configuration.tables.each { tableName, table ->
			
			if(table.mappingClass != null ) {
			
				log("Skipping mapping generation for table '${table.name}'. Mapping class provided.")
		
			} else if(table.isExcludeMappers) {
			
				log("Skipping mapping generation for table '${table.name}'. Excluded.")
				
			} else if(table.isIgnoreMapper == "true") {
					
				log("Skipping mapper generation for table '${table.name}'. Ignore mapper specified")
			
			} else {
			
				def sourcePath = table.sourcePath ?: configuration.sourcePath
			
				def mappingNamespace = table.mappingNamespace ?: configuration.mappingNamespace
				
				def mapperDir = projectDirPath + "/" + sourcePath + "/" + configuration.rootNamespace.replaceAll("\\.", "/") + "/" + mappingNamespace.replaceAll("\\.", "/")
				
				def mapperFilePath = mapperDir + "/${table.name}Mapper.java"
						   
			   if(table.type == "entity") {
				   
				   if((table.recreateMapper && configuration.recreateObjects) || !fileExists(mapperFilePath)) {
					   
					   createMapper(configuration, table, mapperFilePath)
					   
				   } else {
				   
					   log("Skipping mapper generation for table: " + table.name)
				   }
			   
			   }
			}	   
		}
	}
}
				
				
def createMapper(configuration, table, mapperFilePath) {
	
	log("Creating Mapper: ${table.name}Mapper")
	
	def modelNamespace = table.modelNamespace ?: configuration.modelNamespace
				
	def mappingNamespace = table.mappingNamespace ?: configuration.mappingNamespace
		
	def src = "package ${configuration.rootNamespace}.${mappingNamespace};" + CRLF + CRLF \
        + "import java.math.BigDecimal;" + CRLF \
        + "import java.sql.Connection;" + CRLF \
        + "import java.sql.PreparedStatement;" + CRLF \
        + "import java.sql.ResultSet;" + CRLF \
        + "import java.sql.SQLException;" + CRLF \
        + "import java.sql.Types;" + CRLF \
        + "import java.util.Locale;" + CRLF \
        + "import com.apposit.training.video.rental.data.DAOObject;" + CRLF
	
    if(table.hasDateType) {
        
	    if(table.isMultiCalendar == true) {
	    
	        src += "import com.apposit.training.video.rental.data.sql.AbstractCalendarAwareSQLDomainObjectMapper;" + CRLF \
				+ "import com.apposit.training.video.rental.time.CalendarSystemEnum;" + CRLF \
				+ "import com.apposit.training.video.rental.time.DateTimeUtils;" + CRLF + CRLF

	    } else {
	    
	        src += "import com.apposit.training.video.rental.data.sql.AbstractSQLDomainObjectMapper;" + CRLF
		}
	
        src += "import com.apposit.training.video.rental.data.sql.SQLUtils;" + CRLF + CRLF
    
    } else { 
	    
	    src += "import com.apposit.training.video.rental.data.sql.AbstractSQLDomainObjectMapper;" + CRLF
    }   
    
    if(table.isArchived) {
    
	    src += "import com.apposit.training.video.rental.data.archiving.ArchivingStatus;" + CRLF
    }
	
	/*
	if(table.isInternationalized) {
		
		def field = [:]
		field.name = "Locale"
		field.dataType = "String"
		field.isNullable = true
		field.isRefToTypeEnum = false
		field.javaSQLType = "Types.VARCHAR"
		table.fields[field.name] = field
		
		for each intnlField in table.internationalizedFields
			
			set field = new Field
			field.name = intnlField
			field.dataType = "String"
			field.isInternationalized = true
			redim Preserve fields(ubound(fields) + 1)
			set fields(ubound(fields)) = field
		
		next
	}
	*/
		
    //Add Imports for custom fields (where fields may be Enums but not in the same namespace as the current model)
    table.fields.each { fieldName, field ->
    
		if(field.isRefToTypeEnum && !field.isExcludeFromModel) {
			
			def tableConfigForField = configuration.tables[field.dataType.replaceAll("Enum", "")]
			
			if(tableConfigForField != null) {

				if((mappingNamespace == null && tableConfigForField.modelNamespace != null)
					|| (mappingNamespace != null && !mappingNamespace.equalsIgnoreCase(tableConfigForField.modelNamespace))) {
			
					def fieldClassName
					
					if(tableConfigForField.clazz != null) {
		
						fieldClassName = tableConfigForField.clazz
		
					} else {
					
						if(tableConfigForField.modelNamespace != null) {
							
							fieldClassName = configuration.rootNamespace + ".${tableConfigForField.modelNamespace}.${field.dataType}"
							
						} else {
							
							fieldClassName = configuration.rootNamespace + ".${configuration.modelNamespace}.${field.dataType}"
							
						}
					}
					
					if(!src.contains("import " + fieldClassName + ";")) {
						src += "import " + fieldClassName + ";" + CRLF
					}
				}
			}
		}	
    }     
	
	def classImports = new HashSet();
    
    if(table.clazz != null) {
       src += "import ${table.clazz};" + CRLF + CRLF
    } else { 
    	src += "import ${configuration.rootNamespace}.${modelNamespace}.${toProperCase(table.name)};" + CRLF + CRLF
    }
        
    if(table.isMultiCalendar) {
    
    	src += "public class ${toProperCase(table.name)}Mapper extends AbstractCalendarAwareSQLDomainObjectMapper {" + CRLF + CRLF 
    
	} else {  
    	
    	src += "public class ${toProperCase(table.name)}Mapper extends AbstractSQLDomainObjectMapper {" + CRLF + CRLF 
    }
    
    //--Add mapRow()----------------------------------------------------
    src += TAB + "@Override" + CRLF \
        + TAB + "public DAOObject mapRow(ResultSet resultSet, Locale locale) throws SQLException {" + CRLF + CRLF
        
    src += TAB + TAB + "${toProperCase(table.name)} ${toVariableCase(table.name)} = new " + toProperCase(table.name) + "();" + CRLF \
            + TAB + TAB + toVariableCase(table.name) + ".setId(resultSet.getInt(\"${toProperCase(table.name)}Id\"));" + CRLF + CRLF
            
    //Map Fields
    table.fields.each { fieldName, field ->
    
        if(!field.isRefToTypeEnum) {
			
        	if(field.isNullable && field.dataType != "String" && field.dataType != "Date") {
				
        		if(field.dataType == "Double") {
					
        			src += TAB + TAB + "Object ${toVariableCase(field.name)}BigDecimalValue = resultSet.getObject(\"${field.name}\");" + CRLF
        			src += TAB + TAB + "if(null !=  ${toVariableCase(field.name)}BigDecimalValue)" + CRLF
        	 		src += TAB + TAB + TAB + "${toVariableCase(table.name)}.set${toProperCase(field.name)}(((BigDecimal) ${toVariableCase(field.name)}BigDecimalValue).doubleValue());" + CRLF
        		
				} else {
					  
        	 		src += TAB + TAB + "${toVariableCase(table.name)}.set${toProperCase(field.name)}((${field.dataType})resultSet.getObject(\"${field.name}\"));" + CRLF
        	 	}
				
        	} else { 
        	
        		if(table.isMultiCalendar == true && field.dataType == "Date" && !field.name.equalsIgnoreCase("createdDate") && !field.name.equalsIgnoreCase("updatedDate")) {
        		
            		src += TAB + TAB + "${toVariableCase(table.name)}.set${toProperCase(field.name)}(DateTimeUtils.toStringConvert(resultSet.getDate(\"${field.name}\")," \
						+ " CalendarSystemEnum.ISO, getDefaultCalendarSystem(), locale));" + CRLF
        			        		
        		} else { 
            	
            		src += TAB + TAB + "${toVariableCase(table.name)}.set${toProperCase(field.name)}(resultSet.get${toProperCase(getSqlDataAccessor(field))}(\"${field.name}\"));" + CRLF
                }
            }
		    
        } else {
        
            	src += TAB + TAB + "${toVariableCase(table.name)}.set" \
                	+ "${toProperCase(field.name)}(null != resultSet.get${toProperCase(getSqlDataAccessor(field))}(\"${field.name}\") ? " \
                		+ "${field.dataType}.valueOf(resultSet.get${toProperCase(getSqlDataAccessor(field))}(\"${field.name}\")) : null);" + CRLF            
        }
    }
     
    if(table.isInternationalized) {
				               
        table.fields.each { fieldName, field ->
			
			if(field.isInternationalized) {
			
	    		src += TAB + TAB + toVariableCase(table.name) + ".set${toProperCase(intnlField)}(resultSet.getString(\"${intnlField}\"));" + CRLF					
		
			}
        }
    }
                
    src = src+ TAB + TAB + "return ${toVariableCase(table.name)};" + CRLF \
			+ TAB + "}" + CRLF + CRLF
   
   //----Add prepareDeleteStatement() ------------------------------------------
    src += TAB + "@Override" + CRLF \
        + TAB + "public PreparedStatement prepareDeleteStatement(DAOObject object, Connection connection) throws SQLException {" + CRLF + CRLF \
        + TAB + TAB + "PreparedStatement statement = connection.prepareCall(\"{call spDelete${toProperCase(table.name)}(?)}\");" + CRLF \
        + TAB + TAB + "statement.setInt(1, (Integer)object.getId());" + CRLF \
        + TAB + TAB + "return statement;" + CRLF \
        + TAB + "}" + CRLF + CRLF

   //----Add prepareGetStatement() ------------------------------------------
   
    if(table.isInternationalized) {
    
    	src += TAB + "@Override" + CRLF \
	        + TAB + "public PreparedStatement prepareGetStatement(Class<?> clazz, Object id, Connection connection, Locale locale) throws SQLException {" + CRLF + CRLF \
	        + TAB + TAB + "PreparedStatement statement = connection.prepareCall(\"{call spGet${toProperCase(table.name)}(?,?)}\");" + CRLF \
	        + TAB + TAB + "statement.setInt(1, (Integer)id);" + CRLF
	        
	        if(configuration.usesShortLocale) {
  				src = src  + TAB + TAB + "statement.setString(2, locale == null ? null : locale.getLanguage());" + CRLF
	        } else { 
  				src = src  + TAB + TAB + "statement.setString(2, locale == null ? null : locale.toString());" + CRLF
  			}
  			
	        src += TAB + TAB + "return statement;" + CRLF \
	        + TAB + "}" + CRLF + CRLF   
	        		
    } else { 
    
	    src += TAB + "@Override" + CRLF \
	        + TAB + "public PreparedStatement prepareGetStatement(Class<?> clazz, Object id, Connection connection, Locale locale) throws SQLException {" + CRLF + CRLF \
	        + TAB + TAB + "PreparedStatement statement = connection.prepareCall(\"{call spGet${toProperCase(table.name)}(?)}\");" + CRLF \
	        + TAB + TAB + "statement.setInt(1, (Integer)id);" + CRLF \
	        + TAB + TAB + "return statement;" + CRLF \
	        + TAB + "}" + CRLF + CRLF   
    }
    
   //----Add prepareInsertStatement() ------------------------------------------
    src += TAB + "@Override" + CRLF \
        + TAB + "public PreparedStatement prepareInsertStatement(DAOObject object, Connection connection) throws SQLException {" + CRLF + CRLF \
        + TAB + TAB + "${toProperCase(table.name)} ${toVariableCase(table.name)} = (${toProperCase(table.name)})object;" + CRLF \
        + TAB + TAB + "PreparedStatement statement = connection.prepareCall(\"{call spInsert${toProperCase(table.name)}" \
            + "(${getParamSymbols(table, 'insert')})}\");" + CRLF
        
    def idx = 1
    
	table.fields.each { fieldName, field ->
		
        if(!field.name.equalsIgnoreCase("createdDate") && !field.name.equalsIgnoreCase("updatedDate")) {
                        
            if(table.isMultiCalendar == true && field.datatype == "Date") {
            	                		
            	src += TAB + TAB + "statement.setDate(${idx}, SQLUtils.toDate(DateTimeUtils.toDateConvert(${toVariableCase(table.name)}." \
					+ "get${toProperCase(field.name)}(), getDefaultCalendarSystem(), CalendarSystemEnum.ISO, object.getLocale())));" + CRLF
                	 			
			} else if(field.name == "Locale") {
  			
  				if(configuration.usesShortLocale) {
  				
  					src += TAB + TAB + "statement.setString(" \
                		 + "${idx},${toVariableCase(table.name)}.get${toProperCase(field.name)}() == null ? null : ${toVariableCase(table.name)}.getLocale().getLanguage());" + CRLF
               	
  				} else {
				   
  					src += TAB + TAB + "statement.setString(" \
                		 + "${idx},${toVariableCase(table.name)}.get${toProperCase(field.name)}() == null ? null : ${toVariableCase(table.name)}.getLocale().toString());" + CRLF
               	}
  			
  			} else if(field.isNullable && field.dataType != "String") {
			
  				src += TAB + TAB + "statement.setObject(" \
                	 + "${idx},${getPropertyAccessor(table, field)},${field.javaSQLType});" + CRLF
  			} else {
            	 src += TAB + TAB + "statement.set${toProperCase(getSqlDataAccessor(field))}(${idx},${getPropertyAccessor(table, field)});" + CRLF
            }
                
            idx++
        }
    }
	
    src += TAB + TAB + "return statement;" + CRLF \
        + TAB + "}" + CRLF + CRLF  

   //----Add prepareFindStatement() ------------------------------------------
    src += TAB + "@Override" + CRLF \
        + TAB + "public PreparedStatement prepareFindStatement(DAOObject object, Connection connection) throws SQLException {" + CRLF + CRLF \
        + TAB + TAB + toProperCase(table.name) + " ${toVariableCase(table.name)} = (${toProperCase(table.name)})object;" + CRLF \
        + TAB + TAB + "PreparedStatement statement = connection.prepareCall(\"{call spFind${toProperCase(table.name)}" \
            + "(${getParamSymbols(table, 'insert')})}\");" + CRLF
        
    idx = 1
    table.fields.each { fieldName, field ->
		
		if(!field.name.equalsIgnoreCase("createdDate") && !field.name.equalsIgnoreCase("updatedDate")) {
  
  			if(table.isMultiCalendar == true && field.datatype == "Date") {
            	                		
            	src += TAB + TAB + "statement.setDate(" \
                	 + "${idx}, SQLUtils.toDate(DateTimeUtils.toDateConvert(${toVariableCase(table.name)}.get${toProperCase(field.name)}(), getDefaultCalendarSystem(), CalendarSystemEnum.ISO, object.getLocale())));" + CRLF
                	 			
			} else if(field.name == "Locale") {
  			
  				if(configuration.usesShortLocale) {
  				
  					src += TAB + TAB + "statement.setString(" \
                		 + "${idx},${toVariableCase(table.name)}.get${toProperCase(field.name)}() == null ? null : ${toVariableCase(table.name)}.getLocale().getLanguage());" + CRLF
               	
  				} else {
				   
	  				src += TAB + TAB + "statement.setString(" \
	                	 + "${idx},${toVariableCase(table.name)}.get${toProperCase(field.name)}() == null ? null : ${toVariableCase(table.name)}.getLocale().toString());" + CRLF
                }	 
  			
  			} else if(field.dataType != "String") {
			  
  				src += TAB + TAB + "statement.setObject(${idx},${getPropertyAccessor(table, field)},${field.javaSQLType});" + CRLF
  			
			} else {
			   
            	src += TAB + TAB + "statement.set${toProperCase(getSqlDataAccessor(field))}(${idx},${getPropertyAccessor(table, field)});" + CRLF
            }
               
            idx++
        }
    }
    
    src += TAB + TAB + "return statement;" + CRLF \
        + TAB + "}" + CRLF + CRLF 
                
   //----Add prepareUpdateStatement() ------------------------------------------
    src += TAB + "@Override" + CRLF \
        + TAB + "public PreparedStatement prepareUpdateStatement(DAOObject object, Connection connection) throws SQLException {" + CRLF + CRLF \
        + TAB + TAB +toProperCase(table.name) + " ${toVariableCase(table.name)} = (${toProperCase(table.name)})object;" + CRLF \
        + TAB + TAB + "PreparedStatement statement = connection.prepareCall(\"{call spUpdate${toProperCase(table.name)}" \
            + "(" +getParamSymbols(table, "update") + ")}\");" + CRLF
        
    def newField = [:]
    newField.name = table.name + "Id"
    newField.dataType = "Integer"
    table.fields[newField.name] = newField
    
    idx = 1        
    table.fields.each { fieldName, field ->
	
		if(!field.name.equalsIgnoreCase("createdDate") && !field.name.equalsIgnoreCase("updatedDate")
				&& !field.name.equalsIgnoreCase("createdBy") && !field.name.equalsIgnoreCase("MasterEntityId")) {
        
        	if(table.isMultiCalendar == true && field.datatype == "Date") {
            	                		
            	src += TAB + TAB + "statement.setDate(" \
                	 + "${idx}, SQLUtils.toDate(DateTimeUtils.toDateConvert(${toVariableCase(table.name)}.get${toProperCase(field.name)}(), getDefaultCalendarSystem(), CalendarSystemEnum.ISO, object.getLocale())));" + CRLF
                	 			
			} else if(field.name == "Locale") {
  			
  				if(configuration.usesShortLocale) {
  				
  					src += TAB + TAB + "statement.setString(" \
                		 + "${idx},${toVariableCase(table.name)}.get${toProperCase(field.name)}() == null ? null : ${toVariableCase(table.name)}.getLocale().getLanguage());" + CRLF
               	
  				} else {
	  				src += TAB + TAB + "statement.setString(" \
                	 + "${idx},${toVariableCase(table.name)}.get${toProperCase(field.name)}() == null ? null : ${toVariableCase(table.name)}.getLocale().toString());" + CRLF
                }	 
  			
  			} else if(field.isNullable && field.dataType != "String") {
			  
  				src += TAB + TAB + "statement.setObject(" \
                	 + "${idx},${getPropertyAccessor(table, field)},${field.javaSQLType});" + CRLF
  			
			} else {
			   
            	 src += TAB + TAB + "statement.set${toProperCase(getSqlDataAccessor(field))}(${idx},${getPropertyAccessor(table, field)});" + CRLF
            }
                
            idx++
        }
    }
    
    src += TAB + TAB + "return statement;" + CRLF \
        + TAB + "}" + CRLF + CRLF 
                
   //----Add supports() ------------------------------------------
    src += TAB + "@Override" + CRLF \
        + TAB + "public Class<? extends DAOObject> supports() {" + CRLF + CRLF \
        + TAB + TAB + "return ${toProperCase(table.name)}.class;" + CRLF \
        + TAB + "}" + CRLF
                               
    src = src	+ CRLF + "}" + CRLF + CRLF + CRLF + CRLF 
          
	//Remove unused imports
	if(!src.contains("(BigDecimal)")) {
	    src = src.replaceAll("import java.math.BigDecimal;", "")
	}	
	
	if(!src.contains("Types.")) {
	    src = src.replaceAll("import java.sql.Types;", "")
	}
	
    writeToFile(mapperFilePath, src)    
    
}		
		
def getFieldType(dataType) {

	if(dataType == null) { return null }
	
	dataType = dataType.trim().toUpperCase()
	
	if(dataType.startsWith("DATE")) {
		return "Date"
	} else if(dataType.startsWith("INT")) {
        return "Integer"
	} else if(dataType.startsWith("TINYINT")) {
        return "Short"
	} else if(dataType.startsWith("VARCHAR") || dataType.startsWith("CHAR") || dataType.startsWith("NVARCHAR") || dataType.startsWith("TEXT")) {
	    return "String"
	} else if(dataType.startsWith("BIT")) {
        return "Boolean"  
	} else if(dataType.startsWith("NUMERIC") || dataType.startsWith("DECIMAL") || dataType.startsWith("MONEY") || dataType.startsWith("REAL") || dataType.startsWith("FLOAT")) {
	    return "Double"
	} else if(dataType.startsWith("VARBINARY")) {
        return "byte[]"
	}    
}

def getParamSymbols(table, paramType) {

	def paramSymbols = ""
	
	table.fields.each { fieldName, field ->
		
		if(!field.name.equalsIgnoreCase("createdDate") && !field.name.equalsIgnoreCase("updatedDate")) {
		
			if(!paramType.equalsIgnoreCase("update") || (!field.name.equalsIgnoreCase("MasterEntityId") && !field.name.equalsIgnoreCase("CreatedBy"))) {
				paramSymbols += "?,"
			}
		}
	}

	if(paramType.equalsIgnoreCase("insert")) {
		paramSymbols = paramSymbols[0..-2]
	} else {
		paramSymbols = paramSymbols + "?"
	}

	return paramSymbols
}

def getSqlDataAccessor(field) {
	
	if(field == null) { return null }
	
	if(field.isRefToTypeEnum) {
		return "String"
	} else if (field.dataType == "Integer") {
		return "Int"
	} else if (field.dataType == "Date") {
		return "Timestamp"
	}  else if (field.dataType == "byte[]") {
		return "Bytes"
	} else {
		return field.dataType
	}
}

def getPropertyAccessor(table, field) {

	if(field.dataType == "Date") {
		
		return "SQLUtils.toTimeStamp(${toVariableCase(table.name)}.get${toProperCase(field.name)}())"
		
	} else if(field.isRefToTypeEnum) {
	
		return "(${toVariableCase(table.name)}.get${toProperCase(field.name)}() == null ? null : ${toVariableCase(table.name)}.get${toProperCase(field.name)}().name())"
	
	} else {
	
		if(field.name != table.name + "Id") {
			return "(${field.dataType})${toVariableCase(table.name)}.get${toProperCase(field.name)}()"
		} else {
			return "(${field.dataType})${toVariableCase(table.name)}.getId()"
		}
	}
}
			

def getJavaSQLType(dataType) {
	
	if(dataType == null) { return null }

	dataType = dataType.trim().toUpperCase()
	
	if(dataType.startsWith("DATE")) {
		return "Types.TIMESTAMP"
	} else if(dataType.startsWith("INT")) {
		return "Types.INTEGER"
	} else if(dataType.startsWith("VARCHAR")) {
		return "Types.VARCHAR"
	} else if(dataType.startsWith("CHAR")) {
		return "Types.CHAR"
	} else if(dataType.startsWith("NVARCHAR")) {
		return "Types.NVARCHAR"
	} else if(dataType.startsWith("TEXT")) {
		return "Types.NVARCHAR"
	} else if(dataType.startsWith("BIT")) {
		return "Types.BOOLEAN"
	} else if(dataType.startsWith("DECIMAL") || dataType.startsWith("MONEY")) {
		return "Types.DECIMAL"
	} else if(dataType.startsWith("REAL")) {
		return "Types.REAL"
	} else if(dataType.startsWith("FLOAT")) {
		return "Types.FLOAT"
	} else if(dataType.startsWith("DOUBLE")) {
		return "Types.DOUBLE"
	} else if(dataType.startsWith("NUMERIC")) {
		return "Types.NUMERIC"
	} else if(dataType.startsWith("VARBINARY")) {
		return "Types.VARBINARY"
	}

}


def isNumericType(dataType) {

	dataType = dataType.toUpperCase()
	
	int pos = dataType.indexOf('(');

	return pos > 0 ? NUMERIC_TYPES.contains(dataType.substring(0, dataType.indexOf('('))) : dataType
	
}
		
static void writeToFile(filePath, contents) {

    log("Writing file: " + filePath)
    
	def file = new File(filePath)

	if(file.exists()) { file.delete() }

	file.createNewFile()
	
	file.write(contents)
}

static boolean fileExists(filePath) {
	
	return new File(filePath).exists()
}
				
static void log(message) {

   println message             
}

static String toProperCase( String text ) {
	text[0].toUpperCase() + text[1..text.length() -1 ]
}
			
static String toVariableCase( String text ) {
	text[0].toLowerCase() + text[1..text.length() -1 ]
}
				