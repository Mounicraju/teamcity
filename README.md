# TeamCity Configuration as Code Demo

## Overview

This is a simple "Hello World" project demonstrating TeamCity's Configuration as Code (CaC) capabilities using Kotlin DSL. The project showcases how CI/CD configurations can be stored, versioned, and managed as code.

## Project Structure

```
teamcity-cac-demo/
├── .teamcity/
│   └── settings.kts          # TeamCity Kotlin DSL configuration
├── hello-world.js            # Simple Hello World application
├── package.json              # Node.js project configuration
└── README.md                 # This file
```

## Build Configurations

The TeamCity project includes 4 build configurations:

1. **Hello World Build** - Simple build that runs the Hello World application
2. **Test Suite** - Runs tests for the application
3. **Code Quality Check** - Performs linting and code quality checks
4. **Full Pipeline** - Complete CI/CD pipeline: Quality → Test → Build → Deploy

## Key Features Demonstrated

- **VCS Root Configuration** - Git repository integration
- **Multiple Build Steps** - Script execution, npm commands
- **Build Dependencies** - Sequential execution of builds
- **VCS Triggers** - Automatic builds on code changes
- **Build Features** - Custom build report tabs

## Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/Mounicraju/teamcity.git
   cd teamcity
   ```

2. **Run locally**
   ```bash
   npm start
   ```

3. **Test the application**
   ```bash
   npm test
   ```

4. **Check code quality**
   ```bash
   npm run lint
   ```

## TeamCity Setup

1. **Create a new project in TeamCity**
2. **Enable Versioned Settings** (Project Settings → Versioned Settings)
3. **Set VCS Root** to point to this repository
4. **Configure environment variables**:
   - `GITHUB_TOKEN` - Your GitHub personal access token (provided separately)
5. **Load project settings from VCS**

## Configuration as Code Benefits

### For Product Managers
- **Consistency**: All environments use identical configurations
- **Reliability**: Reduced human error in configuration
- **Transparency**: Clear audit trail of all changes
- **Compliance**: Version control meets regulatory requirements
- **Speed**: Faster deployment and configuration updates

### For DevOps Engineers
- **Version Control**: Track configuration changes over time
- **Code Review**: Peer review of configuration changes
- **Testability**: Test configurations before applying
- **Reusability**: Share configurations across projects
- **Scalability**: Manage multiple environments efficiently

## Demo Flow

1. **Initial Setup** - Show TeamCity UI and current configuration
2. **Generate DSL** - Demonstrate generating Kotlin DSL from UI
3. **Modify Configuration** - Make changes directly in code
4. **Commit & Push** - Version control the changes
5. **Auto-Apply** - Show TeamCity picking up changes automatically
6. **Version History** - Demonstrate configuration versioning

## Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `GITHUB_TOKEN` | GitHub personal access token | Yes |

## Troubleshooting

- **DSL Compilation Errors**: Check TeamCity version compatibility
- **VCS Connection Issues**: Verify GitHub token and repository access
- **Build Failures**: Check Node.js installation on build agents

## Next Steps

- Add more complex build steps
- Implement artifact publishing
- Add deployment configurations
- Set up build chains and dependencies
- Configure build parameters and environment variables
