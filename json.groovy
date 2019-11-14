import groovy.json.JsonSlurper;

def getJsonFile(file) {
    def jsonSlurper = new JsonSlurper()
    File fl = new File(file)
    // parse(File file) method is available since 2.2.0
    // def obj = jsonSlurper.parse(fl)
    def obj = jsonSlurper.parseText(fl.text)
    return obj 
}

def getPipelineConfig(ci_module) {
    def conf = getJsonFile('conf.json')
    return conf["pipeline_config"]
}

def pipeline_config = getPipelineConfig()

for (build in pipeline_config.builds) {
    parallel(build)
}
