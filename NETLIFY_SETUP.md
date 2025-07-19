# 🌐 Netlify + GitHub Actions Setup Guide

## 🎯 Quick Setup Steps

### 1. Get Your Netlify Site ID
✅ **Already configured!**
- **Site Name**: `demoforteamcity`
- **Site ID**: `492d1086-0844-46ed-9b2c-b61b7d1cf513`
- **Dashboard**: https://app.netlify.com/sites/demoforteamcity

### 2. Get Your Netlify Auth Token
1. Go to **User settings** → **Applications** → **Personal access tokens**
2. Click **New access token**
3. Give it a name like "GitHub Actions Deploy"
4. Copy the generated token

### 3. Add GitHub Secrets
1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add these two secrets:

#### Secret 1: `NETLIFY_SITE_ID`
- **Name**: `NETLIFY_SITE_ID`
- **Value**: `492d1086-0844-46ed-9b2c-b61b7d1cf513`

#### Secret 2: `NETLIFY_AUTH_TOKEN`
- **Name**: `NETLIFY_AUTH_TOKEN`
- **Value**: Your auth token from step 2

## ✅ How It Works

Once configured, the GitHub Actions workflow will:

1. **Run all build steps** (code quality, tests, security scan, etc.)
2. **If all steps pass** → Automatically deploy to Netlify
3. **Generate deployment report** with live site URL
4. **Update commit status** with deployment results

## 🚀 Test the Setup

1. Make a small change to your code
2. Commit and push to `main` branch
3. Watch the GitHub Actions workflow run
4. Check the Netlify deployment job for success
5. Visit your live site URL from the deployment report

## 🔗 Useful Links

- **Netlify Dashboard**: https://app.netlify.com/
- **GitHub Actions**: Your repo → Actions tab
- **Live Site**: Will be shown in deployment reports

---

**🎉 That's it!** Your app will now automatically deploy to Netlify whenever all build steps pass. 