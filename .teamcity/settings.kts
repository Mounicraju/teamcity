// Import necessary DSL components.
// The version (v2024_03) should match your TeamCity server's major version.
// Adjust if your TeamCity version is different (e.g., v2023_11).
import jetbrains.buildServer.configs.kotlin.v2024_03.*
import jetbrains.buildServer.configs.kotlin.v2024_03.Project
import jetbrains.buildServer.configs.kotlin.v2024_03.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2024_03.triggers.vcs

// Define the TeamCity DSL version. This tells TeamCity which DSL version the script is written for.
// It must match your TeamCity server's version (e.g., 2024.03 for TeamCity 2024.03.x).
version = "2024.03"

// Define the root project for your configuration within TeamCity.
// This block describes the top-level project in the TeamCity UI that will contain your build configurations.
project {
    // Unique ID for the project. Good practice to make it descriptive.
    id("Hello_World_Kotlin_DSL_Demo")
    // Display name of the project in the TeamCity UI.
    name = "Hello World Kotlin DSL Demo Project"
    // Optional description for better clarity.
    description = "Demonstrates Configuration as Code with a simple Hello World build."

    // Attach a VCS root to this project.
    // This assumes your .teamcity folder and this settings.kts file are in the root of the Git repository
    // you plan to use as the VCS root for the project itself in TeamCity.
    // TeamCity will automatically link this project to its own source repository
    // when you enable "Versioned Settings" and point it to this repo.
    // We define a shared VCS root that can be used by build configurations within this project.
    vcsRoot(MyProjectVcsRoot)

    // Define a Build Configuration within this project.
    // We pass an object that defines the build steps, triggers, etc.
    buildType(HelloWorldBuild)
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
    url = "https://github.com/your-username/your-repository-name.git" // <<<--- !!! IMPORTANT: UPDATE THIS WITH YOUR REPO URL !!!

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

// Define the "Hello World" Build Configuration (Build Type).
// This block describes the actual build process.
object HelloWorldBuild : BuildType({
    // Unique ID for the build configuration.
    id("HelloWorldBuildConfiguration")
    // Display name of the build configuration in the TeamCity UI.
    name = "Hello World Build"
    // Optional description.
    description = "A simple build that echoes 'Hello, World!' to demonstrate Kotlin DSL."

    // Attach the previously defined VCS root to this build configuration.
    // This tells the build where to get its source code from.
    vcs {
        root(MyProjectVcsRoot) // Reference the VCS root defined above
        // You can specify checkout rules if only a subset of the repository is needed.
        // checkoutMode = CheckoutMode.AUTO // Or CheckoutMode.ON_AGENT, CheckoutMode.ON_SERVER
    }

    // Define build steps. These are the commands that will be executed during the build.
    steps {
        // A simple command-line script step.
        script {
            name = "Echo Hello World Message" // Name of the step shown in TeamCity UI
            // Specify the operating system type for the script if needed.
            // AUTO will try to determine based on the agent's OS.
            // You can use LINUX, WINDOWS, MAC, or AUTO.
            scriptType = script {
                // The actual command to execute.
                // Using platform-agnostic `echo` as much as possible.
                content = """
                    echo "Hello from TeamCity Configuration as Code!"
                    echo "This build was triggered from a repository change."
                    echo "The current TeamCity agent is: %teamcity.agent.name%"
                    echo "Build checkout directory: %system.teamcity.build.checkoutDir%"
                    echo "Current time: $(date +%H:%M:%S)"
                """.trimIndent()
                // You can specify a specific shell (e.g., "bash", "cmd", "powershell")
                // if the commands are OS-specific, but 'echo' is generally safe.
            }
        }
    }

    // Define triggers. These automatically start a new build.
    triggers {
        // Automatically start a build when new changes are detected in the VCS.
        vcs {
            // Optional: Specify branches to monitor.
            // branchFilter = "+:refs/heads/*" // Monitors all branches
            // Optional: Delay before starting a build after changes are detected.
            // quietPeriod = 60 // seconds to wait for more commits before starting a build
        }
    }

    // Define requirements. These specify conditions for an agent to run this build.
    // E.g., if you need a specific OS, software installed, etc.
    // For "Hello World", typically no special requirements needed.
    requirements {
        // Example: Only run on agents with 'Java 11' installed
        // exists("JAVA_HOME_11")
    }

    // Define build features. These extend build functionality (e.g., status publishers, automatic merge).
    features {
        // Example: Publish build status to GitHub
        // commitStatusPublisher {
        //     publisher = github {
        //         githubUrl = "https://api.github.com/" // For GitHub Enterprise, specify your instance URL
        //         authType = personalToken {
        //             // This token should be a TeamCity parameter, not hardcoded.
        //             // E.g., a parameter named 'github.token' with 'credentialsJSON' spec.
        //             token = "credentialsJSON:github.token"
        //         }
        //     }
        // }
    }

    // Define build failure conditions.
    failureConditions {
        // failBuildOnMetricChange {
        //     metric = BuildMetric.TEST_COUNT
        //     threshold = 100 // Fail if test count drops by more than 100
        //     unit = MetricUnit.DEFAULT_UNIT
        //     comparison = Comparison.LESS
        // }
    }

    // Define build options like execution timeout
    // options {
    //     executionTimeoutMinutes = 10 // Build will be stopped after 10 minutes
    // }
})