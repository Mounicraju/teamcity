// == IMPORTANT: THESE IMPORT STATEMENTS ARE REQUIRED ==
import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.CompositeBuildFeature
import jetbrains.buildServer.configs.kotlin.buildFeatures.composite
import jetbrains.buildServer.configs.kotlin.triggers.vcs

// Specifies the TeamCity version
version = "2024.03"

// == EVERYTHING MUST BE INSIDE THIS project { ... } BLOCK ==
project {
    // The Project ID and Name. "HelloWorldPipeline" is a valid ID.
    id("HelloWorldPipeline")
    name = "Hello World Pipeline"

    // VCS Root pointing to your application repository
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

    // == Build Configuration: 1. Build & Install ==
    buildType {
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

    // == Build Configuration: 2. Run Tests ==
    buildType {
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

        dependencies {
            snapshot(Build) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }

    // == Build Configuration: 3. Lint Code ==
    buildType {
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

        dependencies {
            snapshot(Build) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }

    // == Build Configuration: 4. Package for Production ==
    buildType {
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

        dependencies {
            snapshot(TestSuite) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
            snapshot(CodeQuality) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }


    // == Composite Build Configuration (The Full Pipeline View) ==
    buildType {
        id("FullPipeline")
        name = "Full CI/CD Pipeline"
        type = BuildTypeSettings.Type.COMPOSITE

        vcs {
            root(AbsoluteId("GitRepository"))
        }

        features {
            composite()
        }

        dependencies {
            snapshot(Package) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }
}