# Configuration as Code Demo

A comprehensive demonstration of **Configuration as Code (CaC)** capabilities using GitHub Actions for CI/CD pipelines.

## Project Structure

```
├── .github/workflows/          # GitHub Actions configurations
│   └── ci-cd-pipeline.yml      # Main CI/CD pipeline (10 jobs)
├── public/                     # Web application frontend
├── tests/                      # Unit and integration tests
├── netlify/                    # Netlify deployment configuration
├── server.js                   # Express.js web server
├── package.json                # Dependencies and scripts
└── .gitignore                  # Git ignore rules
```

## Configuration as Code Benefits

### **Version Control & History**
- Track pipeline changes in git history
- Easy rollback to previous configurations
- Complete audit trail of modifications

### **Consistency & Standardization**
- Same pipeline across all environments
- Reduced manual configuration errors
- Enforced best practices

### **Automation & Efficiency**
- Automated setup for new projects
- Faster team onboarding
- Centralized configuration management

## CI/CD Pipeline Features

### **10 Comprehensive Build Jobs:**
1. **Install Dependencies** - npm ci installation
2. **Code Quality & Linting** - ESLint analysis
3. **Unit Tests** - Jest testing with coverage
4. **Integration Tests** - End-to-end testing
5. **Security Scan** - Vulnerability analysis
6. **Performance Tests** - Application benchmarking
7. **Build & Package** - Application compilation
8. **Deploy to Staging** - Automated staging deployment
9. **Deploy to Netlify** - Cloud deployment
10. **Pipeline Summary** - Comprehensive reporting

### **Multi-Platform Deployment:**
- **Staging Environment** - For testing
- **Netlify** - Cloud hosting with serverless functions
- **Production** - Final deployment (via GitHub Actions)

## Web Application

### **Features:**
- **Express.js Backend** - RESTful API with user management
- **Modern Frontend** - Responsive web interface
- **Health Monitoring** - Application status endpoints
- **User Management** - CRUD operations

### **API Endpoints:**
- `GET /` - Application info
- `GET /health` - Health status
- `GET /api/users` - List users
- `POST /api/users` - Create user

## Quick Start

```bash
# Install dependencies
npm install

# Run tests
npm test

# Start application
npm start

# Access application
# Web Interface: http://localhost:3000
# API: http://localhost:3000/api/users
# Health: http://localhost:3000/health
```

## GitHub Actions Workflow

### **Triggers:**
- Push to `main` or `develop` branches
- Pull requests
- Manual workflow dispatch

### **Pipeline Flow:**
1. **Parallel Jobs**: Code quality, tests, security scan
2. **Sequential Jobs**: Build → Deploy staging → Deploy Netlify → Deploy production
3. **Quality Gates**: Each job must pass before proceeding

## CI/CD Pipeline Features

### **Visual Pipeline Representation:**
GitHub Actions provides a comprehensive dashboard showing:
- **Real-time build status** for each step
- **Dependency relationships** with arrows
- **Live progress indicators**
- **Professional UI** for demo presentations

### **Build Configurations:**
All 10 build steps are visible in GitHub Actions UI with:
- **Clear step names** and descriptions
- **Execution logs** and outputs
- **Status tracking** (success/failed/running)
- **Artifact management**

## Netlify Deployment

### **Automatic Deployment:**
- Triggered by successful GitHub Actions builds
- Serverless functions for API backend
- Global CDN for fast loading
- Automatic HTTPS

### **Configuration:**
- `netlify.toml` - Deployment configuration
- `netlify/functions/api.js` - Serverless API
- Environment variables for production settings

## Success Metrics

- **✅ All Tests Passing**: 13/13 tests
- **✅ Code Quality**: ESLint clean
- **✅ Security**: Vulnerability scanning
- **✅ Performance**: Benchmarking included
- **✅ Deployment**: Multi-platform automation
- **✅ TeamCity Integration**: Visual pipeline monitoring

## Live URLs

- **Netlify Site**: https://demoforteamcity.netlify.app
- **GitHub Actions**: https://github.com/Mounicraju/teamcity/actions
