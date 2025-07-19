const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const morgan = require('morgan');

const app = express();
const PORT = process.env.PORT || 3000;

// Performance optimizations
app.set('etag', false); // Disable ETag for better performance
app.set('x-powered-by', false); // Remove X-Powered-By header

// Middleware
app.use(helmet({
  contentSecurityPolicy: false, // Allow inline styles for demo
  crossOriginEmbedderPolicy: false
}));
app.use(cors());
app.use(morgan('combined'));
app.use(express.json({ limit: '1mb' })); // Limit JSON payload size

// Cache static data for better performance
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

// API Routes
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
  res.status(200).json({ message: 'User deleted successfully' });
});

app.get('/api/analytics', (req, res) => {
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

// Main route - serve the web app
app.get('/', (req, res) => {
  res.json({
    message: 'Configuration as Code Demo',
    ...staticData,
    timestamp: new Date().toISOString()
  });
});

// Health endpoint (for backward compatibility)
app.get('/health', (req, res) => {
  res.json({
    status: 'healthy',
    uptime: process.uptime(),
    memory: process.memoryUsage(),
    timestamp: new Date().toISOString()
  });
});

// Static file serving (only in non-test environments)
if (process.env.NODE_ENV !== 'test') {
  app.use(express.static('public', {
    maxAge: '1h', // Cache static files for 1 hour
    etag: false
  }));
}

// Error handling middleware
app.use((err, req, res, _next) => {
  console.error(err.stack);
  
  if (err instanceof SyntaxError && err.status === 400 && 'body' in err) {
    return res.status(400).json({ error: 'Invalid JSON format' });
  }
  
  res.status(500).json({ error: 'Something went wrong!' });
});

// 404 handler
app.use((req, res) => {
  res.status(404).json({ error: 'Route not found' });
});

// Only start the server if this file is run directly
if (require.main === module) {
  const server = app.listen(PORT, () => {
    console.log(`🚀 Server running on port ${PORT}`);
    console.log(`📊 Health check available at http://localhost:${PORT}/health`);
    console.log(`👥 API available at http://localhost:${PORT}/api/users`);
  });

  // Handle graceful shutdown
  process.on('SIGTERM', () => {
    console.log('SIGTERM received, shutting down gracefully');
    server.close(() => {
      console.log('Process terminated');
    });
  });
}

module.exports = app; 