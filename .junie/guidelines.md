# Hyrule ID Generator - Development Guidelines

This document provides specific information for developers working on the Hyrule ID Generator project.

## Build/Configuration Instructions

### Prerequisites
- Java 19 or higher
- Maven 3.8 or higher

### Building the Project
The project uses Maven as its build system. Here are the key commands:

```bash
# Build the project
mvn clean package

# Build with tests
mvn clean verify

# Build with integration tests
mvn clean verify -P integration-test

# Create an executable JAR with dependencies
mvn clean package assembly:single
```

### Configuration
The application accepts the following command-line parameters:

- `-s, --size`: Size of the identifiers (default: 9)
- `--seed`: Starting seed for the generator (default: "Hyrule")
- `-p, --port`: Listening port for HTTP server (default: 8888)
- `--timeout`: Waiting timeout for HTTP server (default: 10)
- `-h, --help`: Show help message
- `-V, --version`: Print version information

Example:
```bash
java -jar target/hyrule-id-1.0-SNAPSHOT-jar-with-dependencies.jar --port=9000 --size=12
```

## Testing Information

### Running Tests
The project uses JUnit 5 for testing. Tests can be run using Maven:

```bash
# Run unit tests
mvn test

# Run integration tests
mvn failsafe:integration-test

# Run with code coverage (JaCoCo)
mvn test jacoco:report

# Run mutation tests (PIT)
mvn org.pitest:pitest-maven:mutationCoverage
```

### Test Reports
After running tests, reports are available at:
- JUnit reports: `target/surefire-reports/`
- JaCoCo coverage: `target/site/jacoco/`
- PIT mutation testing: `target/pit-reports/`

### Adding New Tests
1. Create a new test class in the `src/test/java/pro/verron/hyrule/` directory
2. Use the naming convention `*Tests.java` for unit tests and `*_IT.java` for integration tests
3. Use JUnit 5 annotations (`@Test`, `@BeforeEach`, etc.)
4. Follow the Arrange-Act-Assert pattern in test methods
5. Use descriptive method names that explain the test's purpose (e.g., `should_convert_iterator_to_stream()`)

### Example Test
Here's a simple example test for the `Generator` class:

```java
package pro.verron.hyrule;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeneratorTests {

    @Test
    void should_convert_iterator_to_stream() {
        // Arrange
        Iterator<Integer> iterator = List.of(1, 2, 3, 4, 5).iterator();
        Generator<Integer> generator = new Generator<>(iterator);

        // Act
        List<Integer> result = generator.stream()
                .collect(Collectors.toList());

        // Assert
        assertEquals(List.of(1, 2, 3, 4, 5), result);
    }
}
```

## Additional Development Information

### Code Style
- The project follows standard Java code style conventions
- Use meaningful variable and method names
- Include JavaDoc comments for public classes and methods
- Follow the immutability principle where possible (many classes are immutable)
- Use assertions for validating preconditions

### Project Structure
- `pro.verron.hyrule.Hyrule`: Main entry point class
- `pro.verron.hyrule.Id`: Immutable class representing an ID
- `pro.verron.hyrule.Generator`: Wrapper for iterators that provides stream conversion
- `pro.verron.hyrule.RandomIdIterator`: Iterator that generates random IDs
- `pro.verron.hyrule.HyruleServer`: HTTP server that serves generated IDs
- `pro.verron.hyrule.Server`: Interface for server implementations

### Logging
The project uses Java's built-in logging system. Logging configuration is in:
- `src/main/resources/logging.properties` (for application)
- `src/test/resources/logging.properties` (for tests)

### Security Considerations
- The application uses SecureRandom with SHA1PRNG for generating IDs
- The random generator is seeded for reproducibility in tests
- For production use, consider using a more secure seed

### Docker Support
The project includes a Dockerfile for containerization. Build and run with:

```bash
# Build the Docker image
docker build -t hyrule-id .

# Run the container
docker run -p 8888:8888 hyrule-id
```
