const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const morgan = require('morgan');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(helmet());
app.use(cors());
app.use(morgan('combined'));
app.use(express.json());

// API Routes
app.get('/api', (req, res) => {
  res.json({
    message: 'Welcome to GitHub Actions Configuration as Code Demo!',
    version: process.env.npm_package_version || '1.0.0',
    environment: process.env.NODE_ENV || 'development',
    timestamp: new Date().toISOString(),
    nodeVersion: process.version
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
  
  // Mock user creation
  const newUser = {
    id: Math.floor(Math.random() * 1000) + 4,
    name,
    email,
    createdAt: new Date().toISOString()
  };
  
  res.status(201).json(newUser);
});

app.delete('/api/users/:id', (req, res) => {
  const userId = parseInt(req.params.id);
  
  // Mock user deletion
  res.status(200).json({ message: 'User deleted successfully' });
});

// Analytics API
app.get('/api/analytics', (req, res) => {
  const analytics = {
    totalUsers: 3,
    activeUsers: 3,
    systemUptime: '99.9%',
    lastUpdated: new Date().toISOString(),
    features: {
      userManagement: true,
      taskManagement: true,
      analytics: true,
      settings: true
    }
  };
  
  res.json(analytics);
});

// System Status API
app.get('/api/status', (req, res) => {
  const status = {
    status: 'online',
    timestamp: new Date().toISOString(),
    version: '2.0.0',
    environment: process.env.NODE_ENV || 'development',
    uptime: process.uptime(),
    memory: process.memoryUsage(),
    features: [
      'User Management',
      'Task Management', 
      'Analytics Dashboard',
      'System Settings',
      'Real-time Notifications',
      'Material Design UI'
    ]
  };
  
  res.json(status);
});

// Main route (for backward compatibility)
app.get('/', (req, res) => {
  res.json({
    message: 'Welcome to GitHub Actions Configuration as Code Demo!',
    version: process.env.npm_package_version || '1.0.0',
    environment: process.env.NODE_ENV || 'development',
    timestamp: new Date().toISOString(),
    nodeVersion: process.version
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
  app.use(express.static('public'));
}

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