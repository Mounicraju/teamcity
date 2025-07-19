import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.*
import jetbrains.buildServer.configs.kotlin.triggers.*
import jetbrains.buildServer.configs.kotlin.vcs.*

version = "2020.1"

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
}

object Build : BuildType({
    name = "Build"
    
    vcs {
        root(DslContext.settingsRoot)
    }
    
    steps {
        script {
            name = "Build Demo 1"
            scriptContent = "echo 'Hello, TeamCity!' && node hello-world.js"
        }
    }
    
    triggers {
        vcs {
        }
    }
})

object TestSuite : BuildType({
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
    
    triggers {
        vcs {
        }
    }
})

object CodeQuality : BuildType({
    name = "Code Quality Check"
    
    vcs {
        root(DslContext.settingsRoot)
    }
    
    steps {
        script {
            name = "Run Linting"
            scriptContent = "npm run lint"
        }
    }
    
    triggers {
        vcs {
        }
    }
})

object FullPipeline : BuildType({
    name = "Full Pipeline"
    
    vcs {
        root(DslContext.settingsRoot)
    }
    
    steps {
        script {
            name = "Pipeline Step 1: Code Quality"
            scriptContent = "echo 'Step 1: Code Quality' && npm run lint"
        }
        
        script {
            name = "Pipeline Step 2: Run Tests"
            scriptContent = "echo 'Step 2: Tests' && npm test"
        }
        
        script {
            name = "Pipeline Step 3: Build"
            scriptContent = "echo 'Step 3: Build' && npm run build"
        }
        
        script {
            name = "Pipeline Step 4: Deploy"
            scriptContent = "echo 'Step 4: Deploy (simulated)'"
        }
    }
    
    triggers {
        vcs {
        }
    }
})