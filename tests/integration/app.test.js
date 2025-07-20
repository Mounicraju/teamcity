const request = require('supertest');
const app = require('../../server');

// Test the app as a whole, making sure everything works together
describe('Application Integration Tests', () => {
  describe('Full User Workflow', () => {
    it('should handle complete user management workflow', async () => {
      // First, let's see what users we have
      const initialUsersResponse = await request(app).get('/api/users');
      expect(initialUsersResponse.status).toBe(200);
      
      // Now let's create a new user
      const newUser = {
        name: 'Integration Test User',
        email: 'integration@test.com'
      };
      
      const createResponse = await request(app)
        .post('/api/users')
        .send(newUser)
        .set('Content-Type', 'application/json');
      
      expect(createResponse.status).toBe(201);
      expect(createResponse.body.name).toBe(newUser.name);
      expect(createResponse.body.email).toBe(newUser.email);
      
      // Since this is just a demo with fake data, we can't actually verify the user was added
      // But we can make sure the creation process worked
      expect(createResponse.body).toHaveProperty('id');
      expect(createResponse.body.id).toBeGreaterThan(0);
    });
  });

  describe('Application Health and Status', () => {
    it('should provide consistent health and status information', async () => {
      // Check the main endpoint
      const mainResponse = await request(app).get('/');
      expect(mainResponse.status).toBe(200);
      expect(mainResponse.body).toHaveProperty('message');
      expect(mainResponse.body).toHaveProperty('timestamp');
      
      // Check the health endpoint
      const healthResponse = await request(app).get('/health');
      expect(healthResponse.status).toBe(200);
      expect(healthResponse.body.status).toBe('healthy');
      expect(healthResponse.body).toHaveProperty('timestamp');
      
      // Make sure the timestamps are actually valid dates
      expect(() => new Date(mainResponse.body.timestamp)).not.toThrow();
      expect(() => new Date(healthResponse.body.timestamp)).not.toThrow();
      
      // And that they're reasonably recent (within the last minute)
      const now = new Date();
      const mainTimestamp = new Date(mainResponse.body.timestamp);
      const healthTimestamp = new Date(healthResponse.body.timestamp);
      
      expect(Math.abs(now - mainTimestamp)).toBeLessThan(60000); // Within 1 minute
      expect(Math.abs(now - healthTimestamp)).toBeLessThan(60000); // Within 1 minute
    });
  });

  describe('Error Handling', () => {
    it('should handle malformed requests gracefully', async () => {
      // Try sending some garbage JSON and see if the server handles it properly
      const response = await request(app)
        .post('/api/users')
        .send('invalid json')
        .set('Content-Type', 'application/json');
      
      expect(response.status).toBe(400);
      expect(response.body).toHaveProperty('error');
    });
    
    it('should handle missing content type', async () => {
      const response = await request(app)
        .post('/api/users')
        .send({ name: 'Test', email: 'test@example.com' });
      
      // Express should be smart enough to figure this out
      expect(response.status).toBe(201);
    });
  });

  describe('API Endpoints', () => {
    it('should serve API endpoints correctly', async () => {
      const response = await request(app).get('/');
      
      // The root endpoint returns JSON
      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toContain('application/json');
    });
    
    it('should serve API health endpoint', async () => {
      const response = await request(app).get('/api/health');
      expect(response.status).toBe(200);
      expect(response.body).toHaveProperty('status', 'healthy');
    });
  });
}); 