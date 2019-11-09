// Create List of build stages to suit
def prepareBuildStages() {
  def buildList = []

  for (i=1; i<5; i++) {
    def buildStages = [:]
    for (name in [ 'one', 'two', 'three' ] ) {
      def n = "${name} ${i}"
      buildStages.put(n, prepareOneBuildStage(n))
    }
    buildList.add(buildStages)
  }
  return buildList
}

def prepareOneBuildStage(String name) {
  return {
    stage("Build stage:${name}") {
      println("Building ${name}")
      sh(script:'sleep 5', returnStatus:true)
    }
  }
}

node {
    stage ('clone') {
        git 'https://github.com/chanranhalee/jenkins_script.git' // git clone
    }
    
    stage ('init') {
        sh "echo 'file' > file"
        stash name: "file", includes: "file"
    }
    
    stage ('build') {
        parallel "build firmware": {
            dir ('build.firmware') { // clone 받은 프로젝트 안의 sample 디렉토리에서 stage 실행
                unstash name: "file"
                sh "ls -al"
                sh './build.firmware.sh'
            }
            dir ('../') {
                sh 'echo ${PWD}'
            }
        }, "build framework": {
            dir ("${WORKSPACE}/build.framework") {
                sh "echo 'hello framework'"
                unstash name: "file"
                sh "ls -al"
            }
        }
    }
    
    // main script block
    // could use eg. params.parallel build parameter to choose parallel/serial 
    def runParallel = true
    def buildStages
    
    node('master') {
      stage('Initialise') {
        // Set up List<Map<String,Closure>> describing the builds
        buildStages = prepareBuildStages()
        println("Initialised pipeline.")
      }
    
      for (builds in buildStages) {
        if (runParallel) {
          parallel(builds)
        } else {
          // run serially (nb. Map is unordered! )
          for (build in builds.values()) {
            build.call()
          }
        }
      }
    
      stage('Finish') {
          println('Build complete.')
      }
}

}
