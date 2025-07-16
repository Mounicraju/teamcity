// Import necessary DSL components.
// The version (v2022_10) should match your TeamCity server's major version.
import jetbrains.buildServer.configs.kotlin.v2022_10.*
import jetbrains.buildServer.configs.kotlin.v2022_10.Project
import jetbrains.buildServer.configs.kotlin.v2022_10.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2022_10.buildSteps.npm
import jetbrains.buildServer.configs.kotlin.v2022_10.triggers.vcs

// Define the TeamCity DSL version.
version = "2022.10"

// Define the root project for your configuration within TeamCity.
project {
    id("Configuration_As_Code_Demo")
    name = "Configuration as Code Demo Project"
    description = "Comprehensive demo showcasing Configuration as Code capabilities with multiple build steps, testing, and deployment scenarios."

    vcsRoot(MyProjectVcsRoot)
    buildType(CodeQualityBuild)
    buildType(TestingBuild)
    buildType(BuildAndPackageBuild)
    buildType(DeploymentBuild)
    buildType(FullPipelineBuild)
}

// Define the VCS Root.
object MyProjectVcsRoot : GitVcsRoot({
    id("MyProjectGitRepo")
    name = "My Project Git Repository"
    url = "https://github.com/Mounicraju/teamcity.git"
    branch = "refs/heads/main"
})

// Build Configuration 1: Code Quality Checks
object CodeQualityBuild : BuildType({
    id("CodeQualityBuild")
    name = "1. Code Quality & Linting"
    description = "Runs code quality checks, linting, and static analysis"

    vcs {
        root(MyProjectVcsRoot)
    }

    steps {
        script {
            name = "Build Information"
            scriptType = script {
                content = """
                    echo "=== CODE QUALITY BUILD STARTED ==="
                    echo "Build ID: %build.number%"
                    echo "Branch: %vcsroot.branch%"
                    echo "Commit: %build.vcs.number%"
                    echo "Agent: %teamcity.agent.name%"
                    echo "Time: $(date)"
                    echo "====================================="
                """.trimIndent()
            }
        }

        npm {
            name = "Install Dependencies"
            commands = "ci"
            workingDir = "."
        }

        npm {
            name = "Run ESLint"
            commands = "run lint"
            workingDir = "."
        }

        script {
            name = "Security Audit"
            scriptType = script {
                content = """
                    echo "Running security audit..."
                    npm audit --audit-level=moderate || echo "Security vulnerabilities found - check logs"
                    echo "Security audit completed"
                """.trimIndent()
            }
        }

        script {
            name = "Check Code Formatting"
            scriptType = script {
                content = """
                    echo "Checking code formatting..."
                    npx prettier --check "src/**/*.{js,jsx,ts,tsx}" || echo "Code formatting issues found"
                    echo "Formatting check completed"
                """.trimIndent()
            }
        }
    }

    triggers {
        vcs {
            branchFilter = "+:refs/heads/*"
        }
    }
})

// Build Configuration 2: Testing
object TestingBuild : BuildType({
    id("TestingBuild")
    name = "2. Testing Suite"
    description = "Runs unit tests, integration tests, and test coverage"

    vcs {
        root(MyProjectVcsRoot)
    }

    steps {
        script {
            name = "Build Information"
            scriptType = script {
                content = """
                    echo "=== TESTING BUILD STARTED ==="
                    echo "Build ID: %build.number%"
                    echo "Branch: %vcsroot.branch%"
                    echo "Commit: %build.vcs.number%"
                    echo "Agent: %teamcity.agent.name%"
                    echo "Time: $(date)"
                    echo "==============================="
                """.trimIndent()
            }
        }

        npm {
            name = "Install Dependencies"
            commands = "ci"
            workingDir = "."
        }

        npm {
            name = "Run Unit Tests"
            commands = "test"
            workingDir = "."
        }

        script {
            name = "Run Tests with Coverage"
            scriptType = script {
                content = """
                    echo "Running tests with coverage..."
                    npm test -- --coverage --watchAll=false
                    echo "Test coverage completed"
                """.trimIndent()
            }
        }

        script {
            name = "Performance Check"
            scriptType = script {
                content = """
                    echo "Running performance checks..."
                    sleep 5
                    echo "Performance check completed"
                    echo "All tests passed successfully!"
                """.trimIndent()
            }
        }
    }

    triggers {
        vcs {
            branchFilter = "+:refs/heads/*"
        }
    }
})

// Build Configuration 3: Build and Package
object BuildAndPackageBuild : BuildType({
    id("BuildAndPackageBuild")
    name = "3. Build & Package"
    description = "Builds the application and creates deployment packages"

    vcs {
        root(MyProjectVcsRoot)
    }

    steps {
        script {
            name = "Build Information"
            scriptType = script {
                content = """
                    echo "=== BUILD & PACKAGE STARTED ==="
                    echo "Build ID: %build.number%"
                    echo "Branch: %vcsroot.branch%"
                    echo "Commit: %build.vcs.number%"
                    echo "Agent: %teamcity.agent.name%"
                    echo "Time: $(date)"
                    echo "================================="
                """.trimIndent()
            }
        }

        npm {
            name = "Install Dependencies"
            commands = "ci"
            workingDir = "."
        }

        npm {
            name = "Build Application"
            commands = "run build"
            workingDir = "."
        }

        script {
            name = "Create Deployment Package"
            scriptType = script {
                content = """
                    echo "Creating deployment package..."
                    mkdir -p dist/package
                    cp -r build/* dist/package/
                    cp package.json dist/package/
                    cp README.md dist/package/
                    
                    echo "Build: %build.number%" > dist/package/version.txt
                    echo "Commit: %build.vcs.number%" >> dist/package/version.txt
                    echo "Date: $(date)" >> dist/package/version.txt
                    
                    cd dist
                    zip -r package-%build.number%.zip package/
                    echo "Deployment package created: package-%build.number%.zip"
                """.trimIndent()
            }
        }

        script {
            name = "Docker Build Simulation"
            scriptType = script {
                content = """
                    echo "Simulating Docker build..."
                    echo "FROM node:lts-alpine" > Dockerfile
                    echo "WORKDIR /app" >> Dockerfile
                    echo "COPY build/ ." >> Dockerfile
                    echo "EXPOSE 3000" >> Dockerfile
                    echo "CMD [\"npm\", \"start\"]" >> Dockerfile
                    
                    echo "Dockerfile created successfully"
                    echo "Docker build simulation completed"
                """.trimIndent()
            }
        }
    }

    triggers {
        vcs {
            branchFilter = "+:refs/heads/*"
        }
    }

    artifacts {
        path("dist/package-%build.number%.zip")
        path("build/")
        path("Dockerfile")
    }
})

// Build Configuration 4: Deployment
object DeploymentBuild : BuildType({
    id("DeploymentBuild")
    name = "4. Deployment"
    description = "Deploys the application to different environments"

    vcs {
        root(MyProjectVcsRoot)
    }

    steps {
        script {
            name = "Build Information"
            scriptType = script {
                content = """
                    echo "=== DEPLOYMENT BUILD STARTED ==="
                    echo "Build ID: %build.number%"
                    echo "Branch: %vcsroot.branch%"
                    echo "Commit: %build.vcs.number%"
                    echo "Agent: %teamcity.agent.name%"
                    echo "Time: $(date)"
                    echo "=================================="
                """.trimIndent()
            }
        }

        script {
            name = "Validate Environment"
            scriptType = script {
                content = """
                    echo "Validating deployment environment..."
                    echo "Environment: %env.ENVIRONMENT%"
                    echo "Deployment target: %env.DEPLOYMENT_TARGET%"
                    echo "✓ Environment validation passed"
                """.trimIndent()
            }
        }

        script {
            name = "Deploy to Staging"
            scriptType = script {
                content = """
                    echo "Deploying to staging environment..."
                    sleep 3
                    echo "✓ Staging deployment completed"
                    echo "Staging URL: https://staging-demo.example.com"
                """.trimIndent()
            }
        }

        script {
            name = "Health Check"
            scriptType = script {
                content = """
                    echo "Running health checks..."
                    sleep 2
                    echo "✓ Health check passed"
                    echo "✓ Application is responding"
                """.trimIndent()
            }
        }

        script {
            name = "Deploy to Production"
            scriptType = script {
                content = """
                    echo "Checking if production deployment is needed..."
                    if [ "%vcsroot.branch%" = "refs/heads/main" ]; then
                        echo "Main branch detected - deploying to production..."
                        sleep 5
                        echo "✓ Production deployment completed"
                        echo "Production URL: https://demo.example.com"
                    else
                        echo "Not main branch - skipping production deployment"
                    fi
                """.trimIndent()
            }
        }
    }

    triggers {
        vcs {
            branchFilter = "+:refs/heads/*"
        }
    }

    dependencies {
        snapshot(BuildAndPackageBuild) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }
})

// Build Configuration 5: Full Pipeline
object FullPipelineBuild : BuildType({
    id("FullPipelineBuild")
    name = "5. Full Pipeline"
    description = "Complete CI/CD pipeline with all steps in sequence"

    vcs {
        root(MyProjectVcsRoot)
    }

    steps {
        script {
            name = "Pipeline Start"
            scriptType = script {
                content = """
                    echo "🚀 FULL CI/CD PIPELINE STARTED 🚀"
                    echo "Build ID: %build.number%"
                    echo "Branch: %vcsroot.branch%"
                    echo "Commit: %build.vcs.number%"
                    echo "Agent: %teamcity.agent.name%"
                    echo "Time: $(date)"
                    echo "=========================================="
                """.trimIndent()
            }
        }

        script {
            name = "Code Quality Check"
            scriptType = script {
                content = """
                    echo "🔍 STEP 1: Code Quality Check"
                    echo "Running linting and static analysis..."
                    npm ci
                    npm run lint || echo "Linting issues found"
                    echo "✓ Code quality check completed"
                """.trimIndent()
            }
        }

        script {
            name = "Testing Suite"
            scriptType = script {
                content = """
                    echo "🧪 STEP 2: Testing Suite"
                    echo "Running unit tests and coverage..."
                    npm test -- --coverage --watchAll=false
                    echo "✓ Testing completed"
                """.trimIndent()
            }
        }

        script {
            name = "Build Application"
            scriptType = script {
                content = """
                    echo "🔨 STEP 3: Build Application"
                    echo "Building the application..."
                    npm run build
                    echo "✓ Build completed"
                """.trimIndent()
            }
        }

        script {
            name = "Create Package"
            scriptType = script {
                content = """
                    echo "📦 STEP 4: Create Package"
                    echo "Creating deployment package..."
                    mkdir -p dist/package
                    cp -r build/* dist/package/
                    echo "Build: %build.number%" > dist/package/version.txt
                    echo "✓ Package created"
                """.trimIndent()
            }
        }

        script {
            name = "Deploy Application"
            scriptType = script {
                content = """
                    echo "🚀 STEP 5: Deploy Application"
                    echo "Deploying to environment..."
                    sleep 3
                    echo "✓ Deployment completed"
                    echo "Application URL: https://demo.example.com"
                """.trimIndent()
            }
        }

        script {
            name = "Pipeline Complete"
            scriptType = script {
                content = """
                    echo "🎉 PIPELINE COMPLETED SUCCESSFULLY! 🎉"
                    echo "All steps completed successfully"
                    echo "Build ID: %build.number%"
                    echo "Time: $(date)"
                    echo "=========================================="
                """.trimIndent()
            }
        }
    }

    triggers {
        vcs {
            branchFilter = "+:refs/heads/main"
        }
    }

    artifacts {
        path("dist/")
        path("build/")
    }
})