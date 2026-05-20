/** @type {import('jest').Config} */
module.exports = {
  preset: 'jest-preset-angular',
  setupFilesAfterFramework: ['<rootDir>/setup-jest.ts'],
  testMatch: ['<rootDir>/src/**/*.spec.ts'],
  transform: {
    '^.+\\.(ts|mjs|js|html)$': [
      'jest-preset-angular',
      {
        tsconfig: '<rootDir>/tsconfig.spec.json',
        stringifyContentPathRegex: '\\.(html|svg)$'
      }
    ]
  },
  transformIgnorePatterns: ['node_modules/(?!.*\\.mjs$)'],
  moduleNameMapper: {
    '^@core/(.*)$':         '<rootDir>/src/app/core/$1',
    '^@shared/(.*)$':       '<rootDir>/src/app/shared/$1',
    '^@modules/(.*)$':      '<rootDir>/src/app/modules/$1',
    '^@environments/(.*)$': '<rootDir>/src/environments/$1'
  },
  collectCoverageFrom: [
    'src/app/**/*.ts',
    '!src/app/**/*.spec.ts',
    '!src/main.ts'
  ],
  coverageReporters: ['text', 'lcov', 'html']
};
