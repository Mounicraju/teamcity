const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const morgan = require('morgan');

const app = express();
const PORT = process.env.PORT || 3000;

// Set up some basic performance tweaks
app.set('etag', false); // Turn off ETag to avoid unnecessary 304 responses
app.set('x-powered-by', false); // Hide that we're using Express for security

// Add the usual middleware stack
app.use(helmet({
  contentSecurityPolicy: false, // Need this off for the demo to work properly
  crossOriginEmbedderPolicy: false
}));
app.use(cors());
app.use(morgan('combined'));
app.use(express.json({ limit: '1mb' })); // Don't let people send huge JSON payloads

// Store some data we'll reuse throughout the app
const staticData = {
  version: process.env.npm_package_version || '2.0.0',
  environment: process.env.NODE_ENV || 'development',
  nodeVersion: process.version,
  features: [
    'User Management',
    'Task Management', 
    'Analytics Dashboard',
    'System Settings',
    'CI/CD Pipeline Monitor',
    'Material Design UI'
  ]
};

// Let's define our API endpoints
app.get('/api', (req, res) => {
  res.json({
    message: 'Configuration as Code Demo API',
    ...staticData,
    timestamp: new Date().toISOString()
  });
});

app.get('/api/health', (req, res) => {
  res.json({
    status: 'healthy',
    uptime: process.uptime(),
    memory: process.memoryUsage(),
    timestamp: new Date().toISOString()
  });
});

app.get('/api/users', (req, res) => {
  // Just some fake users for the demo - in real life this would come from a database
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
  
  // Create a new user with a random ID (not great for production, but works for demo)
  const newUser = {
    id: Math.floor(Math.random() * 1000) + 4,
    name,
    email,
    createdAt: new Date().toISOString()
  };
  
  res.status(201).json(newUser);
});

app.delete('/api/users/:id', (req, res) => {
  // eslint-disable-next-line no-unused-vars
  const userId = parseInt(req.params.id);
  // In a real app, we'd actually delete the user from the database
  res.status(200).json({ message: 'User deleted successfully' });
});

app.get('/api/analytics', (req, res) => {
  // Return some fake analytics data for the frontend
  res.json({
    totalUsers: 3,
    activeUsers: 3,
    systemUptime: '99.9%',
    lastUpdated: new Date().toISOString(),
    features: {
      userManagement: true,
      taskManagement: true,
      analytics: true,
      settings: true,
      pipelineMonitor: true
    }
  });
});

app.get('/api/status', (req, res) => {
  res.json({
    status: 'online',
    timestamp: new Date().toISOString(),
    ...staticData,
    uptime: process.uptime(),
    memory: process.memoryUsage()
  });
});

// The main route - just returns some basic info
app.get('/', (req, res) => {
  res.json({
    message: 'Configuration as Code Demo',
    ...staticData,
    timestamp: new Date().toISOString()
  });
});

// Keep the old health endpoint for compatibility
app.get('/health', (req, res) => {
  res.json({
    status: 'healthy',
    uptime: process.uptime(),
    memory: process.memoryUsage(),
    timestamp: new Date().toISOString()
  });
});

// Serve static files, but only when we're not running tests
if (process.env.NODE_ENV !== 'test') {
  app.use(express.static('public', {
    maxAge: '1h', // Cache files for an hour to reduce server load
    etag: false
  }));
}

// Catch any errors that slip through
app.use((err, req, res, _next) => {
  // Don't spam the console during tests
  if (process.env.NODE_ENV !== 'test') {
    console.error(err.stack);
  }
  
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

// Only start the server if someone runs this file directly
if (require.main === module) {
  const server = app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
    console.log(`Health check available at http://localhost:${PORT}/health`);
    console.log(`API available at http://localhost:${PORT}/api/users`);
  });

  // Clean shutdown when the process gets terminated
  process.on('SIGTERM', () => {
    console.log('SIGTERM received, shutting down gracefully');
    server.close(() => {
      console.log('Process terminated');
    });
  });
}

module.exports = app;