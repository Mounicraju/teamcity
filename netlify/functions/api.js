const express = require('express');
const serverless = require('serverless-http');

const app = express();

// Set up the middleware we need
app.use(express.json());

// Define our API endpoints
app.get('/api', (req, res) => {
  res.json({
    message: 'Welcome to GitHub Actions Configuration as Code Demo!',
    version: process.env.npm_package_version || '1.0.0',
    environment: process.env.NODE_ENV || 'production',
    timestamp: new Date().toISOString(),
    nodeVersion: process.version,
    deployed: 'Netlify'
  });
});

app.get('/api/health', (req, res) => {
  res.json({
    status: 'healthy',
    uptime: process.uptime(),
    memory: process.memoryUsage(),
    timestamp: new Date().toISOString(),
    platform: 'Netlify Functions'
  });
});

app.get('/api/users', (req, res) => {
  // Just some fake users for the demo
  const users = [
    { id: 1, name: 'John Doe', email: 'john@example.com' },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com' },
    { id: 3, name: 'Bob Johnson', email: 'bob@example.com' }
  ];
  res.json(users);
});

app.post('/api/users', (req, res) => {
  const { name, email } = req.body;
  if (!name || !email) {
    return res.status(400).json({ error: 'Name and email are required' });
  }
  
  // Create a fake user (in real life this would go to a database)
  const newUser = {
    id: Math.floor(Math.random() * 1000) + 4,
    name,
    email
  };
  
  res.status(201).json(newUser);
});

// Keep the old health endpoint around for compatibility
app.get('/health', (req, res) => {
  res.json({
    status: 'healthy',
    uptime: process.uptime(),
    memory: process.memoryUsage(),
    timestamp: new Date().toISOString(),
    platform: 'Netlify Functions'
  });
});

// Catch any errors that slip through
app.use((err, req, res, _next) => {
  console.error(err.stack);
  
  // Handle JSON parsing errors specifically
  if (err instanceof SyntaxError && err.status === 400 && 'body' in err) {
    return res.status(400).json({ error: 'Invalid JSON format' });
  }
  
  res.status(500).json({ error: 'Something went wrong!' });
});

// If nothing else matches, return 404
app.use((req, res) => {
  res.status(404).json({ error: 'Route not found' });
});

// Export the function for Netlify
module.exports.handler = serverless(app); 