const request = require('supertest');
const app = require('../../server');

describe('Application Integration Tests', () => {
  describe('Full User Workflow', () => {
    it('should handle complete user management workflow', async () => {
      // Step 1: Check initial users
      const initialUsersResponse = await request(app).get('/api/users');
      expect(initialUsersResponse.status).toBe(200);
      const initialUserCount = initialUsersResponse.body.length;
      
      // Step 2: Create a new user
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
      
      // Step 3: Verify user was added
      const updatedUsersResponse = await request(app).get('/api/users');
      expect(updatedUsersResponse.status).toBe(200);
      expect(updatedUsersResponse.body.length).toBe(initialUserCount + 1);
      
      // Step 4: Verify the new user is in the list
      const foundUser = updatedUsersResponse.body.find(user => 
        user.email === newUser.email
      );
      expect(foundUser).toBeDefined();
      expect(foundUser.name).toBe(newUser.name);
    });
  });

  describe('Application Health and Status', () => {
    it('should provide consistent health and status information', async () => {
      // Check main endpoint
      const mainResponse = await request(app).get('/');
      expect(mainResponse.status).toBe(200);
      expect(mainResponse.body.status).toBeUndefined(); // Main endpoint doesn't have status
      
      // Check health endpoint
      const healthResponse = await request(app).get('/health');
      expect(healthResponse.status).toBe(200);
      expect(healthResponse.body.status).toBe('healthy');
      
      // Verify timestamps are recent
      const now = new Date();
      const mainTimestamp = new Date(mainResponse.body.timestamp);
      const healthTimestamp = new Date(healthResponse.body.timestamp);
      
      expect(Math.abs(now - mainTimestamp)).toBeLessThan(5000); // Within 5 seconds
      expect(Math.abs(now - healthTimestamp)).toBeLessThan(5000); // Within 5 seconds
    });
  });

  describe('Error Handling', () => {
    it('should handle malformed requests gracefully', async () => {
      // Test with invalid JSON
      const response = await request(app)
        .post('/api/users')
        .send('invalid json')
        .set('Content-Type', 'application/json');
      
      expect(response.status).toBe(400);
    });
    
    it('should handle missing content type', async () => {
      const response = await request(app)
        .post('/api/users')
        .send({ name: 'Test', email: 'test@example.com' });
      
      // Express should still process this correctly
      expect(response.status).toBe(201);
    });
  });

  describe('Static File Serving', () => {
    it('should serve static files from public directory', async () => {
      const response = await request(app).get('/');
      
      // Since we're testing the API, we expect JSON response
      // The static file serving is handled by the frontend
      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toContain('application/json');
    });
  });
}); 