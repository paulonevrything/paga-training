import java.nio.charset.Charset;

DIR_DATA = "application"
DIR_STORED_PROCEDURE = "sp"
DIR_USER_DEFINED_TYPE = "uddt"
DIR_USER_DEFINED_FUNCTION = "udf"
DIR_OBJECT = "object"
DIR_VIEW = "view"
DIR_TRIGGER = "trig"
DIR_DEVELOPMENT = "environment/dev"
DIR_TIMESTAMP = ".timestamp"


log("Running Update Groovy Script")

projectPath = "."

log("Using Script Path: ${projectPath}")

dbPropertiesFilePath = "${projectPath}/configuration/database.properties"

dbProperties = loadPropertiesFile(dbPropertiesFilePath)

if(null == dbProperties){
    String errMsg = "Unbale to load DB Properties File: ${dbPropertiesFilePath}"
    log(errMsg)
    throw new RuntimeException(errMsg)
}

log("Using Database Properties ${dbPropertiesFilePath}")

timestampDirPath = "${DIR_TIMESTAMP}"

outputDirPath = System.properties['java.io.tmpdir']


    update()

def update(){

    initialize()

    execute()
}

def initialize(){

    def timestampDir = new File(timestampDirPath)

    if(!timestampDir.exists()) {

        timestampDir.mkdirs();
    }

	if(!outputDirPath.endsWith(File.separator))
		outputDirPath = "${outputDirPath}"

}

def execute() {
    
    //update object scripts
    log("Running custom object scripts...")
    executeScripts(DIR_OBJECT)

    //update user defined datatypes
	log("Running user-defined data type scripts...")
	executeScripts(DIR_USER_DEFINED_TYPE)
    
    //update user defined functions
    log("Running user-defined function scripts...")
    executeScripts(DIR_USER_DEFINED_FUNCTION)
    
    //update view
    log("Running custom view scripts...")
    executeScripts(DIR_VIEW)
    
    //update stored procedures
    log("Running custom stored procedure scripts...")
    executeScripts(DIR_STORED_PROCEDURE)
    
    //update triggers
    log("Running custom trigger scripts...")
    executeScripts(DIR_TRIGGER)
    
    //update environment-specific scripts
    log("Running custom environment-specific scripts...")
    executeScripts(DIR_DEVELOPMENT)
}


def executeScripts(directory) {

    def scriptDir = new File("${projectPath}/${DIR_DATA}/${directory}")

	directory = directory.replace("/", "_");
	directory = directory.replace("\\", "_");

    if(!scriptDir.exists()) {
        
		log("Unable to locate script directory: ${scriptDir}. Skipping '${directory}' scripts")
        return
    }

    def statusFile = new File("${outputDirPath}/DatabaseUpdateStatus_${directory}.txt")

    statusFile.createNewFile()

    def fileNames = scriptDir.list()

	if(null != fileNames) {

    fileNames = fileNames.sort()

    fileNames.each { fileName ->

			if(fileName.endsWith(".sql")) {

				def dbScriptFilePath = "${scriptDir.path}/${fileName}"
            
				log("Executing DB Script: ${dbScriptFilePath}")
                    
                try {
            
                    def exeResult = executeProcess(
                            "mysql -u ${dbProperties['jdbc.username']} -p${dbProperties['jdbc.password']} ${dbProperties['jdbc.catalog']}",
                            dbScriptFilePath,
                            null)
			
                    if (0 != exeResult.retCode) {
				
                        println("Unable to execute script: ${dbScriptFilePath}. Result: ${exeResult}")
                        throw new Exception("Unable to execute script: ${dbScriptFilePath}. Result: ${exeResult}")
    }

                } catch(Exception ex) {

                    log("Error: Unable to execute ${dbScriptFilePath} Error: ${ex.message}")

        }
    }
    }

}

	setLastBuildDate(directory)

}

def getLastBuildDate(directory) {
                    
	timestampFile = new File("${timestampDirPath}/${dbProperties['db.catalog']}.${directory}")
                    
    if(!timestampFile.exists()) {
        def calendar = new GregorianCalendar(1990,0,1)
        return calendar.getTimeInMillis()
    }
                    
    timestampFile.lastModified()
    
}


def setLastBuildDate(directory) {

	timestampFile = new File("${timestampDirPath}/${dbProperties['db.catalog']}.${directory}")
    
    if(timestampFile.exists()) {
        timestampFile.delete()
    }
    
    timestampFile.createNewFile()
    
}   

def log(message) {

   println message             
}

def getFileEncoding(srcFile) {
		
		CharsetToolkit toolkit = new CharsetToolkit(srcFile)
		
		Charset guessedCharset = toolkit.getCharset()
		
		return guessedCharset.name()

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

def executeProcess(commandString, inputFilePath, workingDir) {

	println("Executing Command: ${commandString} Using Input file: ${inputFilePath}")

	def sout = new StringBuffer()
	def serr = new StringBuffer()

	Process proc = null != workingDir ? commandString.execute([], new File(workingDir)) : commandString.execute()

	proc.consumeProcessErrorStream(serr)
	proc.consumeProcessOutputStream(sout)

	if(null != inputFilePath) {
		proc.withWriter { writer ->
			writer << new File(inputFilePath).text
		}
	}

	retCode = proc.waitFor()

	retText = sout +'' + serr

	return([retCode:retCode, retText:retText])
}

