version = "2025.03.3"

project {
    id("TeamCityDemo")
    name = "TeamCity Demo Project"
    
    vcsRoot {
        id("GitHubRepo")
        name = "GitHub Repository"
        url = "https://github.com/Mounicraju/teamcity-demo.git"
        branch = "refs/heads/main"
        authMethod = password {
            userName = "Mounicraju"
            password = "%github.token%"
        }
    }
    
    buildType {
        id("CodeQuality")
        name = "1. Code Quality & Linting"
        
        vcs {
            root(jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot.absoluteId("GitHubRepo"))
        }
        
        steps {
            script {
                name = "Build Information"
                scriptContent = """
                    echo "=== CODE QUALITY BUILD STARTED ==="
                    echo "Build ID: %build.number%"
                    echo "Branch: %vcsroot.branch%"
                    echo "Commit: %build.vcs.number%"
                    echo "Agent: %teamcity.agent.name%"
                    echo "Time: $(date)"
                    echo "====================================="
                """
            }
            
            npm {
                name = "Install Dependencies"
                commands = "ci"
            }
            
            npm {
                name = "Run Linting"
                commands = "run lint"
            }
        }
        
        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }
    }
    
    buildType {
        id("Testing")
        name = "2. Testing Suite"
        
        vcs {
            root(jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot.absoluteId("GitHubRepo"))
        }
        
        steps {
            script {
                name = "Build Information"
                scriptContent = """
                    echo "=== TESTING BUILD STARTED ==="
                    echo "Build ID: %build.number%"
                    echo "Branch: %vcsroot.branch%"
                    echo "Commit: %build.vcs.number%"
                    echo "Agent: %teamcity.agent.name%"
                    echo "Time: $(date)"
                    echo "====================================="
                """
            }
            
            npm {
                name = "Install Dependencies"
                commands = "ci"
            }
            
            npm {
                name = "Run Tests"
                commands = "run test"
            }
        }
        
        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }
    }
    
    buildType {
        id("BuildPackage")
        name = "3. Build & Package"
        
        vcs {
            root(jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot.absoluteId("GitHubRepo"))
        }
        
        steps {
            script {
                name = "Build Information"
                scriptContent = """
                    echo "=== BUILD & PACKAGE STARTED ==="
                    echo "Build ID: %build.number%"
                    echo "Branch: %vcsroot.branch%"
                    echo "Commit: %build.vcs.number%"
                    echo "Agent: %teamcity.agent.name%"
                    echo "Time: $(date)"
                    echo "====================================="
                """
            }
            
            npm {
                name = "Install Dependencies"
                commands = "ci"
            }
            
            npm {
                name = "Build Application"
                commands = "run build"
            }
            
            script {
                name = "Package Artifacts"
                scriptContent = """
                    echo "Creating build artifacts..."
                    mkdir -p artifacts
                    cp -r build/* artifacts/
                    echo "Build artifacts created successfully!"
                """
            }
        }
        
        artifacts {
            path = "artifacts/**"
        }
        
        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }
    }
    
    buildType {
        id("NetlifyDeploy")
        name = "4. Netlify Deployment"
        
        vcs {
            root(jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot.absoluteId("GitHubRepo"))
        }
        
        steps {
            script {
                name = "Build Information"
                scriptContent = """
                    echo "=== NETLIFY DEPLOYMENT STARTED ==="
                    echo "Build ID: %build.number%"
                    echo "Branch: %vcsroot.branch%"
                    echo "Commit: %build.vcs.number%"
                    echo "Agent: %teamcity.agent.name%"
                    echo "Time: $(date)"
                    echo "====================================="
                """
            }
            
            npm {
                name = "Install Dependencies"
                commands = "ci"
            }
            
            npm {
                name = "Build Application"
                commands = "run build"
            }
            
            script {
                name = "Deploy to Netlify"
                scriptContent = """
                    echo "Installing Netlify CLI..."
                    npm install -g netlify-cli
                    
                    echo "Deploying to Netlify..."
                    netlify deploy --prod --dir=build --token=%env.NETLIFY_TOKEN% --site=%env.NETLIFY_SITE_ID%
                    
                    echo "Deployment completed successfully!"
                """
            }
        }
        
        dependencies {
            snapshot(jetbrains.buildServer.configs.kotlin.BuildType.absoluteId("BuildPackage")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
        
        triggers {
            vcs {
                branchFilter = "+:main"
            }
        }
    }
}