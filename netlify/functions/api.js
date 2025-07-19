const express = require('express');
const serverless = require('serverless-http');

const app = express();

// Middleware
app.use(express.json());

// API Routes
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
  // Mock user data for demo
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
  
  // Mock user creation
  const newUser = {
    id: Math.floor(Math.random() * 1000) + 4,
    name,
    email
  };
  
  res.status(201).json(newUser);
});

// Health endpoint (for backward compatibility)
app.get('/health', (req, res) => {
  res.json({
    status: 'healthy',
    uptime: process.uptime(),
    memory: process.memoryUsage(),
    timestamp: new Date().toISOString(),
    platform: 'Netlify Functions'
  });
});

// Error handling middleware
app.use((err, req, res, _next) => {
  console.error(err.stack);
  
  // Handle JSON parsing errors
  if (err instanceof SyntaxError && err.status === 400 && 'body' in err) {
    return res.status(400).json({ error: 'Invalid JSON format' });
  }
  
  res.status(500).json({ error: 'Something went wrong!' });
});

// 404 handler
app.use((req, res) => {
  res.status(404).json({ error: 'Route not found' });
});

// Export the serverless function
module.exports.handler = serverless(app); 