import groovy.json.JsonSlurper;

def getJsonFile(file) {
    def jsonSlurper = new JsonSlurper()
    File fl = new File(file)
    // parse(File file) method is available since 2.2.0
    // def obj = jsonSlurper.parse(fl)
    def obj = jsonSlurper.parseText(fl.text)
    return obj 
}

def getPipelineConfig(conf, ci_module) {
    return conf["pipeline_config"][ci_module]
//    return conf.pipeline_config.ci_module
}

def conf = getJsonFile('conf.json')
def ci_module = "npu"
def pipeline_config = getPipelineConfig(conf, ci_module)
println(pipeline_config.builds)