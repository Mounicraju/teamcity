# Configuration as Code Demo Project

This project demonstrates the power and benefits of **Configuration as Code (CaC)** using TeamCity's Kotlin DSL. It showcases a complete CI/CD pipeline with multiple build steps, testing, and deployment scenarios.

## 🎯 Demo Overview

This demo is designed to showcase Configuration as Code capabilities to both technical and non-technical audiences, including:
- **Product Managers**: Understanding the business benefits
- **DevOps Engineers**: Technical implementation details
- **Development Teams**: Practical usage examples

## 🏗️ Project Structure

```
teamcity/
├── .teamcity/
│   ├── settings.kts          # Main TeamCity configuration
│   └── pom.xml              # Maven configuration for DSL
├── src/                     # React application source
├── public/                  # Static assets
├── Dockerfile              # Container configuration
├── docker-compose.yml      # Multi-service deployment
├── nginx.conf             # Web server configuration
├── .prettierrc            # Code formatting rules
├── .eslintrc.js           # Code quality rules
└── package.json           # Node.js dependencies
```

## 🚀 Build Configurations

The demo includes **5 different build configurations** that showcase various CI/CD capabilities:

### 1. Code Quality & Linting
- **Purpose**: Ensures code quality and consistency
- **Steps**: 
  - Install dependencies
  - Run ESLint for code quality
  - Security audit with npm audit
  - Code formatting checks with Prettier
- **Benefits**: Catches issues early, maintains code standards

### 2. Testing Suite
- **Purpose**: Validates application functionality
- **Steps**:
  - Unit tests execution
  - Test coverage analysis
  - Performance testing simulation
- **Benefits**: Ensures reliability, prevents regressions

### 3. Build & Package
- **Purpose**: Creates deployable artifacts
- **Steps**:
  - Application build
  - Deployment package creation
  - Docker image preparation
- **Benefits**: Consistent, reproducible builds

### 4. Deployment
- **Purpose**: Deploys to different environments
- **Steps**:
  - Environment validation
  - Staging deployment
  - Health checks
  - Conditional production deployment
- **Benefits**: Automated, safe deployments

### 5. Full Pipeline
- **Purpose**: Complete end-to-end CI/CD pipeline
- **Steps**: Combines all previous steps in sequence
- **Benefits**: Single command deployment, full automation

## 🎭 Demo Scenarios

### Scenario 1: Code Quality Demonstration
1. **Show the configuration file** (`.teamcity/settings.kts`)
2. **Explain the benefits**:
   - Version controlled configuration
   - Consistent quality checks
   - Automated enforcement
3. **Run the build** and show real-time feedback

### Scenario 2: Testing Automation
1. **Demonstrate test execution**
2. **Show coverage reports**
3. **Explain failure handling**
4. **Highlight time savings**

### Scenario 3: Deployment Pipeline
1. **Show multi-environment deployment**
2. **Demonstrate conditional logic**
3. **Explain safety measures**
4. **Show rollback capabilities**

### Scenario 4: Configuration Changes
1. **Make a configuration change** in the code
2. **Commit and push** to trigger builds
3. **Show automatic deployment**
4. **Demonstrate consistency**

## 💡 Key Benefits Demonstrated

### For Non-Technical Audience
- **Consistency**: Same process every time
- **Speed**: Faster deployments
- **Reliability**: Fewer human errors
- **Transparency**: Everything is visible in code
- **Compliance**: Audit trail of all changes

### For Technical Audience
- **Version Control**: Configuration changes are tracked
- **Code Review**: Configuration changes go through review process
- **Testing**: Configuration can be tested before deployment
- **Reusability**: Templates and shared configurations
- **Scalability**: Easy to replicate across projects

## ⚠️ Potential Downsides

### Complexity
- **Learning Curve**: TeamCity DSL requires Kotlin knowledge
- **Debugging**: Configuration errors can be harder to debug
- **Tooling**: Limited IDE support for DSL

### Maintenance
- **Version Compatibility**: DSL version must match TeamCity version
- **Documentation**: Less documentation compared to UI configuration
- **Expertise**: Requires specialized knowledge

## 🛠️ Setup Instructions

### Prerequisites
- TeamCity server (Cloud or On-Premise)
- Git repository
- Node.js and npm
- Docker (optional, for containerization demo)

### Configuration Steps
1. **Update VCS Root**: Modify the repository URL in `settings.kts`
2. **Enable Versioned Settings**: In TeamCity project settings
3. **Point to Repository**: Configure the VCS root to point to this repository
4. **Set Parameters**: Configure any required parameters (GitHub tokens, etc.)

### Running the Demo
1. **Commit and Push**: Push the configuration to your repository
2. **Trigger Builds**: Make changes to trigger automatic builds
3. **Monitor Progress**: Watch the builds execute in TeamCity
4. **Explain Each Step**: Use the build logs to explain what's happening

## 📊 Demo Metrics

Track these metrics during your demo:
- **Build Time**: How long each step takes
- **Success Rate**: Percentage of successful builds
- **Deployment Frequency**: How often deployments occur
- **Error Reduction**: Fewer configuration-related issues

## 🎪 Presentation Tips

### Opening (2 minutes)
- "Imagine you're a team lead managing 10 developers..."
- "Every time someone deploys, they have to remember 15 steps..."
- "What if we could automate all of this?"

### Technical Deep Dive (5 minutes)
- Show the configuration file
- Explain the structure
- Demonstrate a configuration change
- Show the automated response

### Business Impact (3 minutes)
- Time savings calculations
- Error reduction statistics
- Compliance benefits
- Cost savings

### Q&A Preparation
- Common questions about complexity
- Migration strategies
- Team adoption challenges
- ROI calculations

## 🔧 Customization

### Adding New Build Steps
1. Create a new `BuildType` object in `settings.kts`
2. Define the steps, triggers, and dependencies
3. Add it to the project configuration
4. Commit and push to trigger

### Environment-Specific Configurations
- Use TeamCity parameters for environment variables
- Create separate build configurations for different environments
- Use conditional logic based on branch names

### Integration Examples
- GitHub status publishing
- Slack notifications
- JIRA integration
- Artifact publishing

## 📚 Additional Resources

- [TeamCity Kotlin DSL Documentation](https://www.jetbrains.com/help/teamcity/kotlin-dsl.html)
- [Configuration as Code Best Practices](https://www.jetbrains.com/help/teamcity/kotlin-dsl.html#Best+Practices)
- [TeamCity Versioned Settings](https://www.jetbrains.com/help/teamcity/storing-project-settings-in-version-control.html)

---

**Remember**: This demo is designed to be interactive. Encourage questions and be prepared to show real-time changes and their effects!
