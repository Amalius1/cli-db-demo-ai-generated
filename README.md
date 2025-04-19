[![Java CI with Maven](https://github.com/Amalius1/cli-db-demo-ai-generated/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/Amalius1/cli-db-demo-ai-generated/actions/workflows/maven.yml)

# CLI Database Demo

This project is a Spring Shell CLI application that provides a command-line interface for managing user operations in a database. The application allows users to add financial operations, print user operations, save operations to a file, and generate PDF reports that can be optionally uploaded to S3.

## Features

- Add user operations with different operation types and amounts
- Find existing users or create new ones
- Print user operations by email
- Save user operations to a text file
- Generate PDF reports of user operations
- Upload PDF reports to S3

## Commands

- `add-user-operation`: Add a new operation for a user
- `print-operations`: Print operations for a user by email
- `save-operations`: Save operations to a text file
- `generate-pdf-report`: Generate a PDF report of user operations with optional S3 upload

## Technologies Used

- Spring Boot
- Spring Shell
- JPA/Hibernate
- H2 Database
- PDF Generation
- AWS S3 Integration

## Getting Started

1. Clone the repository
2. Build the project using Maven: `mvn clean install`
3. Run the application: `mvn spring-boot:run`

## Code Generation

This project's code was generated with the assistance of Junie AI, an advanced AI assistant for software development. Junie AI helped with code structure, implementation details, and best practices.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
