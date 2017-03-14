import groovy.io.FileType

DIR_DATA = "data"
DIR_CONFIGURATION = "application/configuration"
CRLF = String.format("%n")
TAB = "\t"
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
		
		 if (file.isFile()) {
			 
			 log("Generating model objects for configuration file: ${file.name}\n")
			 
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
	
	configuration.recreateObjects = configurationRoot.attribute("recreateObjects") == "true"
	
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
		
		table.recreateModel = it.attribute("recreateModel") == null || it.attribute("recreateModel") == "true"
		
		table.modelNamespace = it.attribute("modelNamespace")
		
		table.isIgnoreModel = it.attribute("isIgnoreModel")
		
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
			archivingStatusId.dataType = "varchar(50)"
			archivingStatusId.isNullable = false
			table.fields[archivingStatusId.name] = archivingStatusId
			
			def masterEntityId = [:]
			masterEntityId.name = "MasterEntityId"
			masterEntityId.dataType = "INT"
			masterEntityId.isNullable = false
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
			
			if(table.isExcludeModels == true) {
			
				log("Skipping model generation for table '${table.name}'. Excluded.")
				
			} else if(table.clazz != null) {
					
				log("Skipping model generation for table '${table.name}'. Model class provided.")
			
			} else if(table.isIgnoreModel == "true") {
					
				log("Skipping model generation for table '${table.name}'. Ignore model specified")
			
			} else {
			
				def sourcePath = table.sourcePath ?: configuration.sourcePath
			
				def modelNamespace = table.modelNamespace ?: configuration.modelNamespace
				
				def modelDir = projectDirPath + "/" + sourcePath + "/" + configuration.rootNamespace.replace(".", "/") + "/" + modelNamespace.replace(".", "/")
	 
				def modelFilePath = modelDir + "/${table.name}.java"
				
				System.out.println("Table: ${table.name}");
				
				if(table.type == "entity") {
					
					if((table.recreateModel && configuration.recreateObjects) || !fileExists(modelFilePath)) {
						
						createModel(configuration, table, modelFilePath)
						
					} else {
					
						log("Skipping model generation for table: " + table.name)
					}
				
				} else if(table.type == "reference") {
					
					def enumFilePath = modelDir + "/${table.name}Enum.java"
					
					System.out.println("Enum: ${table.name}");
					
					if((table.recreateModel && configuration.recreateObjects) || !fileExists(enumFilePath)) {
						
						createEnum(configuration, table, enumFilePath)
						
					} else {
						
						log("Skipping Enum generation for: " + table.name)
					}	
					
				} else {
				
					log("Type: ${table.tableType} set for table: ${table.name} is unknown.")
				}
			}
		}
	}
}
				
def createEnum(configuration, table, srcFilePath) {
	
	log("Creating Type Enum For: " + table.name)
		
	def modelNamespace = table.modelNamespace ?: configuration.modelNamespace
	
	def src = "package ${configuration.rootNamespace}." + modelNamespace + ";" + CRLF + CRLF \
		+ "public enum ${toProperCase(table.name)}Enum {" + CRLF + CRLF
	
	//Add Enum Values
	def valueIdx = 0
	table.referenceValues.each { referenceValue ->
	
		if(valueIdx > 0) {
			 src += ", " + CRLF + TAB + referenceValue.name
		} else {
			src += TAB + referenceValue.name
		}
		
		valueIdx++
	}
	
	if(valueIdx > 0) {
		src += ";" + CRLF
	}
   
	src += CRLF + "}" + CRLF
			   
	writeToFile(srcFilePath, src)
}

				
				
def createModel(configuration, table, modelFilePath) {
	
	log("Creating Model: ${table.name}")
	
	def modelNamespace = table.modelNamespace ?: configuration.modelNamespace
		
	def src = "package ${configuration.rootNamespace}.${modelNamespace};" + CRLF + CRLF
		
	if(table.isConcurrent) {
		src += "import com.apposit.training.video.rental.data.concurrency.Concurrent;" + CRLF
	}
	
	if(table.isAudited) {
		src += "import com.apposit.training.video.rental.data.auditing.Audited;" + CRLF + CRLF
	}
	
	if(table.hasDateType &&  !table.isMultiCalendar) {
		src += "import java.util.Date;" + CRLF + CRLF
	}
	
	/*	
	if(table.isInternationalized) {
		
		dim intnlField
		
		fields = table.fields
			   
		for each intnlField in table.internationalizedFields
			
			set tempField = new Field
			tempField.name = intnlField
			tempField.dataType = "String"
			tempField.isInternationalized = true
			redim Preserve fields(ubound(fields) + 1)
			set fields(ubound(fields)) = tempField
		
		next
	
		table.fields = fields
	}
	*/
	
	//Add Imports for custom fields (where fields may be Enums but not in the same namespace as the current model)
	table.fields.each { fieldName, field ->
	
		if(table.isMultiCalendar && "Date" == field.dataType) {
						
			field.dataType = "String"	
		}
			
		if(field.isRefToTypeEnum && !field.isExcludeFromModel) {
			
		    def tableConfigForField = configuration.tables[field.dataType.replaceAll("Enum", "")]
			
			if(tableConfigForField != null) {
			
				if((table.modelNamespace == null && tableConfigForField.modelNamespace != null)
						|| (table.modelNamespace != null && !table.modelNamespace.equalsIgnoreCase(tableConfigForField.modelNamespace))) {
				
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
					
					src +="import ${fieldClassName};" + CRLF
				}
			}
		}
	}
	
	if(table.isArchived) {
		
		src += "import com.apposit.training.video.rental.data.archiving.Archiveable;" + CRLF \
			+ "import com.apposit.training.video.rental.data.archiving.Archived;" + CRLF \
			+ "import java.util.ArrayList;" + CRLF \
			+ "import java.util.List;" + CRLF
	}
   
	src += "import com.apposit.training.video.rental.data.AbstractIntegerIdObject;" + CRLF + CRLF
   		
	if(table.isAudited) {
		src += "@Audited" + CRLF
	}
		
	if(table.isArchived) {
		src += "@Archived" + CRLF
	}
	
	if(table.isConcurrent) {
		src += "@Concurrent" + CRLF
	}
		
	src += "public class ${toProperCase(table.name)} extends AbstractIntegerIdObject {" + CRLF + CRLF
		
	src += TAB + "private static final long serialVersionUID = 0L;" + CRLF
		  
	//Add Private Model Attributes 
	table.fields.each { fieldName, field ->
		
		if(!field.isExcludeFromModel) {
		
			src += CRLF + TAB + "private ${field.dataType} ${toVariableCase(field.name)};" 
		}
	}
	
	src += CRLF
	
	//Model Constructor
	src += CRLF + TAB + "public ${table.name}(int id) {" + CRLF \
		+ TAB + TAB + "super(id);" + CRLF \
		+ TAB + "}" + CRLF + CRLF \
		+ TAB + "public ${table.name}() {" + CRLF \
		+ TAB + TAB + "super();" + CRLF \
		+ TAB + "}" + CRLF
   
    //Override Methods
	src += CRLF + TAB + "@Override" + CRLF \
		+ TAB  + "public boolean isReadOnly() {" + CRLF + CRLF \
		+ TAB + TAB + "return false;" + CRLF \
		+ TAB + "}" + CRLF
   
   if(table.isArchived) {
   
	   src += CRLF + TAB + "@Override" + CRLF \
		+ TAB  + "public List<Archiveable> getChildArchivingObjectFilters() {" + CRLF + CRLF \
		+ TAB + TAB + "return null;" + CRLF \
		+ TAB + "}" + CRLF
		
	   src += CRLF + TAB + "@Override" + CRLF \
		+ TAB  + "public void setIdentifyingReferenceId(Object id) {}" + CRLF
   
		   src += CRLF + TAB + "@Override" + CRLF \
		+ TAB  + "public List<String> getArchivingFields() {" + CRLF + CRLF \
		+ TAB + TAB + "List<String> versionedFieldList = new ArrayList<String>();" + CRLF
	   
		table.fields.each { fieldName, field ->
		
			if(field.isArchived) {
				src += TAB + TAB + "versionedFieldList.add(\"${archivingField}\");" + CRLF + CRLF
			}
	   	}
	    
	    src += CRLF + TAB + TAB + "return versionedFieldList;" + CRLF + CRLF \
	    	+ TAB + "}" + CRLF
   	}
   
	table.fields.each { fieldName, field ->
        
        if(!field.isExcludeFromModel) {
		
            //Add Getter
            src += CRLF + TAB + "public ${field.dataType} get${toProperCase(field.name)}() {" + CRLF \
	            + TAB + TAB + "return this.${toVariableCase(field.name)};" + CRLF \
                + TAB + "}" + CRLF 
	            
            //Add Setter
            src += CRLF + TAB + "public void set${toProperCase(field.name)}(${field.dataType} ${toVariableCase(field.name)}) {" + CRLF \
	            + TAB + TAB + "this.${toVariableCase(field.name)} = ${toVariableCase(field.name)};" + CRLF \
                + TAB + "}" + CRLF	        
         }
    }
   
    src += CRLF + "}" + CRLF
               
    writeToFile(modelFilePath, src)
    
}
		
		
def getFieldType(dataType) {

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
				