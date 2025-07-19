version = "2020.1"

project {
    id("HelloWorldProject")
    name = "Hello World Project"
    
    vcsRoot {
        id("GitRepository")
        name = "Git Repository"
        url = "https://github.com/Mounicraju/teamcity.git"
        branch = "refs/heads/main"
        authMethod = password {
            userName = "Mounicraju"
            password = "%env.GITHUB_TOKEN%"
        }
    }
    
    buildType {
        id("Build")
        name = "Build"
        
        vcs {
            root(RelativeId("GitRepository"))
        }
        
        steps {
            script {
                name = "Hello World"
                scriptContent = "node hello-world.js"
            }
        }
    }
    
    buildType {
        id("TestSuite")
        name = "Test Suite"
        
        vcs {
            root(RelativeId("GitRepository"))
        }
        
        steps {
            script {
                name = "Run Tests"
                scriptContent = "npm test"
            }
        }
    }
    
    buildType {
        id("CodeQuality")
        name = "Code Quality"
        
        vcs {
            root(RelativeId("GitRepository"))
        }
        
        steps {
            script {
                name = "Lint Code"
                scriptContent = "npm run lint"
            }
        }
    }
    
    buildType {
        id("FullPipeline")
        name = "Full Pipeline"
        
        vcs {
            root(RelativeId("GitRepository"))
        }
        
        steps {
            script {
                name = "Build"
                scriptContent = "node hello-world.js"
            }
            script {
                name = "Test"
                scriptContent = "npm test"
            }
            script {
                name = "Lint"
                scriptContent = "npm run lint"
            }
            script {
                name = "Build App"
                scriptContent = "npm run build"
            }
        }
    }
}