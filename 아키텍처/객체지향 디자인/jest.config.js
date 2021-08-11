module.exports = {
    moduleFileExtensions: ['js', 'json', 'ts'],
    testPathIgnorePatterns: [
        "<rootDir>/node_modules/"
    ],
    testMatch: [
        '<rootDir>/test/*.test.ts'
    ],
    transform: {
        '^.+\\.(t|j)s$': 'ts-jest',
    },
    collectCoverageFrom: [
        'src/**/*.ts',
    ],
    coverageDirectory: 'coverage',
    testEnvironment: 'node'
};
