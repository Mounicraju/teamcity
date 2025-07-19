version = "2023.11"

project {
    id("TeamCityCaCDemo")
    name = "TeamCity Configuration as Code Demo"
    description = "A simple Hello World project demonstrating TeamCity Kotlin DSL capabilities"
    
    // VCS Root - Git repository
    vcsRoot {
        id("GitRepository")
        name = "Git Repository"
        vcsName = "jetbrains.git"
        url = "https://github.com/your-username/teamcity-cac-demo.git"
        branch = "refs/heads/main"
        branchSpec = "+:refs/heads/*"
        authMethod = password {
            userName = "your-username"
            password = "%env.GITHUB_TOKEN%"
        }
    }
    
    // Build Configuration 1: Hello World Build
    buildType {
        id("HelloWorldBuild")
        name = "Hello World Build"
        description = "Simple build that runs the Hello World application"
        
        vcs {
            root(RelativeId("GitRepository"))
        }
        
        steps {
            script {
                name = "Run Hello World"
                scriptContent = """
                    echo "Starting Hello World build..."
                    node hello-world.js
                    echo "Build completed successfully!"
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
        }
        
        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }
        
        features {
            buildReportTab {
                id = "buildInfo"
                title = "Build Information"
                startPage = "buildInfo.html"
            }
        }
    }
    
    // Build Configuration 2: Test Suite
    buildType {
        id("TestSuite")
        name = "Test Suite"
        description = "Run tests for the Hello World application"
        
        vcs {
            root(RelativeId("GitRepository"))
        }
        
        steps {
            script {
                name = "Run Tests"
                scriptContent = """
                    echo "Running test suite..."
                    npm test
                    echo "Tests completed!"
                """.trimIndent()
            }
        }
        
        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }
        
        dependencies {
            snapshot(RelativeId("HelloWorldBuild")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }
    
    // Build Configuration 3: Code Quality
    buildType {
        id("CodeQuality")
        name = "Code Quality Check"
        description = "Run linting and code quality checks"
        
        vcs {
            root(RelativeId("GitRepository"))
        }
        
        steps {
            script {
                name = "Run Linting"
                scriptContent = """
                    echo "Running code quality checks..."
                    npm run lint
                    echo "Code quality check completed!"
                """.trimIndent()
            }
        }
        
        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }
    }
    
    // Build Configuration 4: Full Pipeline
    buildType {
        id("FullPipeline")
        name = "Full Pipeline"
        description = "Complete CI/CD pipeline: Quality → Test → Build"
        
        vcs {
            root(RelativeId("GitRepository"))
        }
        
        steps {
            script {
                name = "Pipeline Step 1: Code Quality"
                scriptContent = """
                    echo "=== STEP 1: Code Quality Check ==="
                    npm run lint
                    echo "Code quality check passed!"
                """.trimIndent()
            }
            
            script {
                name = "Pipeline Step 2: Run Tests"
                scriptContent = """
                    echo "=== STEP 2: Running Tests ==="
                    npm test
                    echo "Tests passed!"
                """.trimIndent()
            }
            
            script {
                name = "Pipeline Step 3: Build Application"
                scriptContent = """
                    echo "=== STEP 3: Building Application ==="
                    npm run build
                    echo "Build completed successfully!"
                """.trimIndent()
            }
            
            script {
                name = "Pipeline Step 4: Deploy (Simulated)"
                scriptContent = """
                    echo "=== STEP 4: Deployment (Simulated) ==="
                    echo "Application would be deployed here in a real scenario"
                    echo "Deployment completed successfully!"
                """.trimIndent()
            }
        }
        
        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }
    }
}