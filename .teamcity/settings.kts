import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.composite
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
This file defines your actual CI/CD pipeline. It creates the project,
VCS connection, and all the build steps. It relies on the libraries
defined in pom.xml to work correctly.
*/
version = "2024.03"

// The main project block that contains all settings.
project {
    id("HelloWorldPipeline")
    name = "Hello World Pipeline"

    // Defines the connection to your Git repository.
    vcsRoot {
        id("GitRepository")
        name = "Git Repository"
        url = "https://github.com/Mounicraju/teamcity.git"
        branch = "refs/heads/main"
        authMethod = password {
            userName = "Mounicraju"
            // It's best practice to use a token stored as a secure
            // environment variable in TeamCity.
            password = "%env.GITHUB_TOKEN%"
        }
    }

    // A build step for installing dependencies and running a basic script.
    val buildAndInstall = buildType {
        id("Build")
        name = "1. Build & Install"

        vcs {
            root(AbsoluteId("GitRepository"))
        }

        steps {
            script {
                name = "Install NPM Dependencies"
                scriptContent = "npm install"
            }
            script {
                name = "Run Hello World"
                scriptContent = "node hello-world.js"
            }
        }
    }

    // A build step for running tests.
    val testSuite = buildType {
        id("TestSuite")
        name = "2. Run Tests"

        vcs {
            root(AbsoluteId("GitRepository"))
        }

        steps {
            script {
                name = "Run Tests"
                scriptContent = "npm test"
            }
        }

        // This step depends on the 'buildAndInstall' step finishing successfully.
        dependencies {
            snapshot(buildAndInstall) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }

    // A build step for checking code quality.
    val codeQuality = buildType {
        id("CodeQuality")
        name = "3. Lint Code"

        vcs {
            root(AbsoluteId("GitRepository"))
        }

        steps {
            script {
                name = "Lint Code"
                scriptContent = "npm run lint"
            }
        }

        // This step also depends on the 'buildAndInstall' step.
        // It can run in parallel with the test suite.
        dependencies {
            snapshot(buildAndInstall) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }

    // A build step for creating the final package.
    val packageApp = buildType {
        id("Package")
        name = "4. Package for Production"

        vcs {
            root(AbsoluteId("GitRepository"))
        }

        steps {
            script {
                name = "Create Production Build"
                scriptContent = "npm run build"
            }
        }

        // This final step depends on both tests and linting passing.
        dependencies {
            snapshot(testSuite) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
            snapshot(codeQuality) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }

    // A special "Composite" build that creates the visual pipeline view.
    buildType {
        id("FullPipeline")
        name = "Full CI/CD Pipeline"
        type = BuildTypeSettings.Type.COMPOSITE

        vcs {
            root(AbsoluteId("GitRepository"))
            showDependenciesChanges = true
        }

        features {
            composite()
        }

        // The whole pipeline depends on the final 'packageApp' step.
        dependencies {
            snapshot(packageApp) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }
}
