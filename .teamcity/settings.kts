version = "2020.1"

project {
    id("TeamCityCaCDemo")
    name = "TeamCity Configuration as Code Demo"
    
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
        id("HelloWorldBuild")
        name = "Hello World Build"
        
        vcs {
            root(RelativeId("GitRepository"))
        }
        
        steps {
            script {
                name = "Run Hello World"
                scriptContent = "echo 'Hello, TeamCity!' && node hello-world.js"
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
        name = "Code Quality Check"
        
        vcs {
            root(RelativeId("GitRepository"))
        }
        
        steps {
            script {
                name = "Run Linting"
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
    }
}