module.exports = {
  testEnvironment: 'node',
  testMatch: [
    '**/tests/**/*.test.js'
  ],
  collectCoverage: true,
  coverageDirectory: 'coverage',
  coverageReporters: ['text', 'lcov', 'html'],
  coveragePathIgnorePatterns: [
    '/node_modules/',
    '/tests/'
  ],
  verbose: true,
  testTimeout: 10000,
  setupFilesAfterEnv: [],
  testEnvironmentOptions: {
    NODE_ENV: 'test'
  },
  // Prevent Jest from running tests in parallel to avoid port conflicts
  maxWorkers: 1,
  // Force exit after tests complete
  forceExit: true,
  // Detect open handles
  detectOpenHandles: true
}; 