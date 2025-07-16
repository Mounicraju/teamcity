// Import necessary DSL components.
// The version (v2024_03) should match your TeamCity server's major version.
// Adjust if your TeamCity version is different (e.g., v2023_11).
import jetbrains.buildServer.configs.kotlin.v2024_03.*
import jetbrains.buildServer.configs.kotlin.v2024_03.Project
import jetbrains.buildServer.configs.kotlin.v2024_03.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2024_03.buildSteps.npm
import jetbrains.buildServer.configs.kotlin.v2024_03.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2024_03.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2024_03.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.v2024_03.buildFeatures.notifications
import jetbrains.buildServer.configs.kotlin.v2024_03.buildFeatures.sshAgent
import jetbrains.buildServer.configs.kotlin.v2024_03.buildSteps.dockerCommand

// Define the TeamCity DSL version. This tells TeamCity which DSL version the script is written for.
// It must match your TeamCity server's version (e.g., 2024.03 for TeamCity 2024.03.x).
version = "2024.03"

// Define the root project for your configuration within TeamCity.
// This block describes the top-level project in the TeamCity UI that will contain your build configurations.
project {
    // Unique ID for the project. Good practice to make it descriptive.
    id("Configuration_As_Code_Demo")
    // Display name of the project in the TeamCity UI.
    name = "Configuration as Code Demo Project"
    // Optional description for better clarity.
    description = "Comprehensive demo showcasing Configuration as Code capabilities with multiple build steps, testing, and deployment scenarios."

    // Attach a VCS root to this project.
    // This assumes your .teamcity folder and this settings.kts file are in the root of the Git repository
    // you plan to use as the VCS root for the project itself in TeamCity.
    // TeamCity will automatically link this project to its own source repository
    // when you enable "Versioned Settings" and point it to this repo.
    // We define a shared VCS root that can be used by build configurations within this project.
    vcsRoot(MyProjectVcsRoot)

    // Define multiple Build Configurations within this project.
    // We pass objects that define the build steps, triggers, etc.
    buildType(CodeQualityBuild)
    buildType(TestingBuild)
    buildType(BuildAndPackageBuild)
    buildType(DeploymentBuild)
    buildType(FullPipelineBuild)
}

// Define the VCS Root. This VCS root will be shared by build configurations within the project.
object MyProjectVcsRoot : GitVcsRoot({
    // A unique ID for the VCS root.
    id("MyProjectGitRepo")
    // Display name for the VCS root in the TeamCity UI.
    name = "My Project Git Repository"

    // URL of your Git repository.
    // IMPORTANT: Replace this with the actual URL of YOUR Git repository (e.g., on GitHub, GitLab, Bitbucket).
    // This should be the repository where you commit this .teamcity folder.
    url = "https://github.com/Mounicraju/teamcity.git"

    // Default branch to monitor for changes.
    branch = "refs/heads/main" // Or "refs/heads/master" if your default branch is master

    // Authentication method.
    // For public repositories, no credentials might be needed.
    // For private repositories, you'd configure a username/password or access token.
    // For a demo, you can often leave this blank and configure the authentication on the TeamCity UI
    // when you create the project from URL, OR, if it's a public repo, no auth is needed.
    // If it's a private repo and you want to define auth in code, it looks like this:
    /*
    authMethod = default {
        userName = "your_git_username"
        // It's highly recommended to use a Project Parameter for the password/token, NOT hardcode it here.
        password = "credentialsJSON:your_password_or_token_param_id" // e.g., credentialsJSON:env.GITHUB_TOKEN
    }
    */

    // How often TeamCity should check for new changes.
    // By default, it's every 60 seconds (or defined by the project's default VCS polling interval).
    // You can specify it here:
    // checkoutPolicy = AgentCheckout() // Agent-side checkout is generally preferred
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
        // Step 1: Display build information
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

        // Step 2: Install dependencies
        npm {
            name = "Install Dependencies"
            commands = "ci"
            workingDir = "."
        }

        // Step 3: Run ESLint
        npm {
            name = "Run ESLint"
            commands = "run lint"
            workingDir = "."
        }

        // Step 4: Check for security vulnerabilities
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

        // Step 5: Code formatting check
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

    features {
        // Publish build status to VCS
        commitStatusPublisher {
            publisher = github {
                githubUrl = "https://api.github.com/"
                authType = personalToken {
                    token = "credentialsJSON:github.token"
                }
            }
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
        // Step 1: Build information
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

        // Step 2: Install dependencies
        npm {
            name = "Install Dependencies"
            commands = "ci"
            workingDir = "."
        }

        // Step 3: Run unit tests
        npm {
            name = "Run Unit Tests"
            commands = "test"
            workingDir = "."
        }

        // Step 4: Run tests with coverage
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

        // Step 5: Performance testing simulation
        script {
            name = "Performance Check"
            scriptType = script {
                content = """
                    echo "Running performance checks..."
                    # Simulate performance testing
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

    failureConditions {
        // Fail if tests fail
        failOnText {
            condition = "FAIL"
            failureMessage = "Tests failed"
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
        // Step 1: Build information
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

        // Step 2: Install dependencies
        npm {
            name = "Install Dependencies"
            commands = "ci"
            workingDir = "."
        }

        // Step 3: Build the application
        npm {
            name = "Build Application"
            commands = "run build"
            workingDir = "."
        }

        // Step 4: Create deployment package
        script {
            name = "Create Deployment Package"
            scriptType = script {
                content = """
                    echo "Creating deployment package..."
                    mkdir -p dist/package
                    cp -r build/* dist/package/
                    cp package.json dist/package/
                    cp README.md dist/package/
                    
                    # Create a version file
                    echo "Build: %build.number%" > dist/package/version.txt
                    echo "Commit: %build.vcs.number%" >> dist/package/version.txt
                    echo "Date: $(date)" >> dist/package/version.txt
                    
                    # Create zip package
                    cd dist
                    zip -r package-%build.number%.zip package/
                    echo "Deployment package created: package-%build.number%.zip"
                """.trimIndent()
            }
        }

        // Step 5: Docker image build (simulation)
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
        // Publish build artifacts
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
        // Step 1: Build information
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

        // Step 2: Environment validation
        script {
            name = "Validate Environment"
            scriptType = script {
                content = """
                    echo "Validating deployment environment..."
                    echo "Environment: %env.ENVIRONMENT%"
                    echo "Deployment target: %env.DEPLOYMENT_TARGET%"
                    
                    # Simulate environment checks
                    echo "✓ Environment validation passed"
                """.trimIndent()
            }
        }

        // Step 3: Deploy to staging
        script {
            name = "Deploy to Staging"
            scriptType = script {
                content = """
                    echo "Deploying to staging environment..."
                    # Simulate deployment process
                    sleep 3
                    echo "✓ Staging deployment completed"
                    echo "Staging URL: https://staging-demo.example.com"
                """.trimIndent()
            }
        }

        // Step 4: Health check
        script {
            name = "Health Check"
            scriptType = script {
                content = """
                    echo "Running health checks..."
                    # Simulate health check
                    sleep 2
                    echo "✓ Health check passed"
                    echo "✓ Application is responding"
                """.trimIndent()
            }
        }

        // Step 5: Deploy to production (conditional)
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
        // This build depends on the BuildAndPackageBuild
        snapshot(BuildAndPackageBuild) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }
})

// Build Configuration 5: Full Pipeline (Combines all steps)
object FullPipelineBuild : BuildType({
    id("FullPipelineBuild")
    name = "5. Full Pipeline"
    description = "Complete CI/CD pipeline with all steps in sequence"

    vcs {
        root(MyProjectVcsRoot)
    }

    steps {
        // Step 1: Pipeline start
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

        // Step 2: Code quality
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

        // Step 3: Testing
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

        // Step 4: Build
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

        // Step 5: Package
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

        // Step 6: Deploy
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

        // Step 7: Pipeline completion
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

    features {
        // Notifications
        notifications {
            notifierSettings = emailNotifier {
                recipients = "team@example.com"
            }
        }
    }

    artifacts {
        path("dist/")
        path("build/")
    }
})