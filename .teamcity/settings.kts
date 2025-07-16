// Import necessary DSL components.
// The version (v2022_10) should match your TeamCity server's major version.
import jetbrains.buildServer.configs.kotlin.v2022_10.*
import jetbrains.buildServer.configs.kotlin.v2022_10.Project
import jetbrains.buildServer.configs.kotlin.v2022_10.buildSteps.script

// Define the TeamCity DSL version.
version = "2022.10"

// Define the root project for your configuration within TeamCity.
project {
    id("Configuration_As_Code_Demo")
    name = "Configuration as Code Demo Project"
    description = "Comprehensive demo showcasing Configuration as Code capabilities with multiple build steps, testing, and deployment scenarios."

    vcsRoot(MyProjectVcsRoot)
    buildType(SimpleDemoBuild)
}

// Define the VCS Root.
object MyProjectVcsRoot : GitVcsRoot({
    id("MyProjectGitRepo")
    name = "My Project Git Repository"
    url = "https://github.com/Mounicraju/teamcity.git"
    branch = "refs/heads/main"
})

// Simple Demo Build Configuration
object SimpleDemoBuild : BuildType({
    id("SimpleDemoBuild")
    name = "Simple Demo Build"
    description = "A simple build to test Configuration as Code"

    vcs {
        root(MyProjectVcsRoot)
    }

    steps {
        script {
            name = "Hello World Step"
            scriptType = script {
                content = """
                    echo "=== CONFIGURATION AS CODE DEMO ==="
                    echo "Build ID: %build.number%"
                    echo "Branch: %vcsroot.branch%"
                    echo "Commit: %build.vcs.number%"
                    echo "Agent: %teamcity.agent.name%"
                    echo "Time: $(date)"
                    echo "This build was triggered from Configuration as Code!"
                    echo "================================================"
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