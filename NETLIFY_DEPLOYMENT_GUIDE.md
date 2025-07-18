# Netlify Deployment Guide

This guide explains how to deploy your React application to Netlify using TeamCity's Configuration as Code.

## 🎯 Overview

After all your builds are successful, you can deploy to Netlify using the **"6. Netlify Deployment"** build configuration that's been added to your TeamCity setup.

## 🚀 Prerequisites

### 1. **Netlify Account**
- Sign up at [netlify.com](https://netlify.com)
- Create a new site or use an existing one

### 2. **Netlify Access Token**
1. Go to **User Settings** → **Applications** → **Personal access tokens**
2. Click **"New access token"**
3. Give it a name (e.g., "TeamCity Deployment")
4. Copy the token (you'll need this for TeamCity)

### 3. **Site ID**
1. Go to your Netlify site dashboard
2. Click **"Site settings"**
3. Copy the **Site ID** (found in the "Site information" section)

## ⚙️ TeamCity Configuration

### **Step 1: Add Environment Variables**
In your TeamCity project settings, add these environment variables:

1. **Go to Project Settings** → **Parameters**
2. **Add the following parameters**:

```
NETLIFY_TOKEN = [your-netlify-access-token]
NETLIFY_SITE_ID = [your-netlify-site-id]
```

### **Step 2: Configure Build Dependencies**
The Netlify deployment build is configured to:
- **Depend on** the "Build & Package" build
- **Only run on main branch** commits
- **Automatically trigger** after successful builds

## 🎪 Demo Deployment Process

### **Step 1: Run the Pipeline**
1. **Commit and push** changes to your repository
2. **Watch the builds** execute in sequence:
   - Code Quality & Linting ✅
   - Testing Suite ✅
   - Build & Package ✅
   - Deployment ✅
   - Full Pipeline ✅
   - **Netlify Deployment** 🚀

### **Step 2: Monitor Netlify Deployment**
The "6. Netlify Deployment" build will:
1. **Install dependencies**
2. **Build the application**
3. **Install Netlify CLI**
4. **Deploy to Netlify**
5. **Verify deployment**

### **Step 3: View Your Live App**
- **Check the build logs** for the Netlify URL
- **Visit your Netlify site** to see the live application
- **Share the URL** with your demo audience

## 📊 What You'll See in TeamCity

### **Build Steps for Netlify Deployment:**
1. **Build Information** - Shows build details
2. **Install Dependencies** - npm ci
3. **Build for Production** - npm run build
4. **Install Netlify CLI** - Installs deployment tools
5. **Deploy to Netlify** - Actual deployment
6. **Post-Deployment Check** - Verification

### **Build Logs Example:**
```
=== NETLIFY DEPLOYMENT STARTED ===
Build ID: 1
Branch: refs/heads/main
Commit: d79546e
Agent: your-agent-name
Time: 2024-01-15 22:30:00
=====================================

✓ Installing Netlify CLI...
✓ Building for production...
✓ Deploying to Netlify...
✓ Netlify deployment completed
Your app is now live on Netlify!
```

## 🔧 Configuration Files

### **netlify.toml**
```toml
[build]
  publish = "build"
  command = "npm run build"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

### **package.json Scripts**
```json
{
  "scripts": {
    "deploy:netlify": "npm run build && netlify deploy --prod --dir=build",
    "deploy:preview": "npm run build && netlify deploy --dir=build"
  }
}
```

## 🎭 Demo Scenarios

### **Scenario 1: Complete CI/CD Pipeline**
1. **Show the 6 builds** running in sequence
2. **Highlight the Netlify deployment** as the final step
3. **Demonstrate live deployment** to production

### **Scenario 2: Configuration as Code Benefits**
1. **Show the Netlify configuration** in code
2. **Explain environment variables** for security
3. **Demonstrate automated deployment**

### **Scenario 3: Real-time Deployment**
1. **Make a code change**
2. **Push to repository**
3. **Watch automatic deployment** to Netlify
4. **Show live application** updates

## 🚨 Troubleshooting

### **Common Issues:**

#### **1. Netlify Token Issues**
- **Error**: "Authentication failed"
- **Solution**: Check NETLIFY_TOKEN environment variable

#### **2. Site ID Issues**
- **Error**: "Site not found"
- **Solution**: Verify NETLIFY_SITE_ID is correct

#### **3. Build Failures**
- **Error**: "Build failed"
- **Solution**: Check React build logs in previous steps

#### **4. Deployment Failures**
- **Error**: "Deployment failed"
- **Solution**: Check Netlify CLI installation and permissions

## 📈 Advanced Features

### **1. Preview Deployments**
- Deploy to preview URLs for testing
- Use `npm run deploy:preview` for staging

### **2. Environment-Specific Deployments**
- Different configurations for staging/production
- Use Netlify contexts for environment variables

### **3. Custom Domains**
- Configure custom domains in Netlify
- Set up SSL certificates automatically

## 🎯 Success Metrics

### **For Your Demo:**
- ✅ **All 6 builds** complete successfully
- ✅ **Live application** accessible on Netlify
- ✅ **Real-time deployment** from code changes
- ✅ **Automated CI/CD** pipeline working

### **Business Benefits:**
- **Faster deployments** (minutes vs hours)
- **Reduced manual errors** (automated process)
- **Better reliability** (consistent deployment)
- **Improved developer experience** (one-click deployment)

## 🔄 Next Steps

1. **Set up Netlify account** and get credentials
2. **Configure TeamCity environment variables**
3. **Test the deployment** with a small change
4. **Show the live application** to your audience
5. **Demonstrate real-time updates** with code changes

---

**Remember**: The Netlify deployment is the final step in your Configuration as Code demo, showing the complete CI/CD pipeline from code to production! 🚀 