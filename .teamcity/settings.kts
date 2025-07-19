import jetbrains.buildServer.configs.kotlin.v2024_03.*
import jetbrains.buildServer.configs.kotlin.v2024_03.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2024_03.triggers.vcs

version = "2024.03"

project {
    id("Teamcity")
    name = "Teamcity"
    
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
            root(DslContext.settingsRoot)
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
            root(DslContext.settingsRoot)
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
            root(DslContext.settingsRoot)
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
            root(DslContext.settingsRoot)
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