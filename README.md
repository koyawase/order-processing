## Project Overview

This project implements a RESTful API for managing orders.
It provides endpoints for creating, retrieving and updating orders. 

The API is built using Spring Boot and uses a H2 in memory database.
To load data on startup, use the `SeedData.java` command line runner. There are examples pre-loaded already.

## API Specification
[Orders api specificatopn](./api-spec.yaml)

## Considerations

Following are extra considerations that are not implemented due to time constraints but would be important in a production system.

### Security

#### Authentication and Authorization
* **OAuth 2.0 / OIDC:** The API acts as a Resource Server, validating JWTs issued by any standard OpenID Connect provider.
* **RBAC (Role-Based Access Control):** Fine-grained permissions are enforced at the method level using `@PreAuthorize` to validate the user's roles and session status.
  * e.g. `@PreAuthorize("hasRole('USER')")` and `@PreAuthorize("hasRole('ADMIN')")`
* **Ownership validation:** Ensures users can only access or modify their own orders by checking the `userId` in the JWT against the `userId` associated with the order.

#### Infrastructure Security
* **IAM Roles:** Utilizes Least Privilege permissions; the API can only publish to specific SNS topics and has no access to other AWS resources.
* **Secrets Management:** Sensitive data like DB credentials and API keys are stored in AWS Secrets Manager, never hardcoded.
* **Network Security:** Application is hosted in a Private Subnet protected by an Application Load Balancer (ALB) and WAF.

### Cloud Services

#### Asynchronous Messaging
* **AWS SNS:** Receives a single "Order Updated" event from the API and broadcasts it to multiple subscribers simultaneously.
* **AWS SQS:** Individual queues for Email and SMS subscribe to the SNS topic. This decouples the services, ensuring one slow provider doesn't block the other or the main API.
* **AWS Lambda:** Serverless workers triggered by SQS to handle the final delivery via AWS SES (Email) and SNS Direct (SMS).
* **Dead Letter Queues:** A safety net that captures failed notifications after maximum retries, preventing data loss and enabling manual recovery.

#### Logging and Monitoring
* **AWS CloudWatch:** Acts as the centralized ingestion point for all application and Lambda logs, providing real-time visibility into system health.
* **Splunk Integration:** Logs are streamed from CloudWatch via a Kinesis Data Firehose for long-term retention, security auditing, and complex querying.

### CI/CD

* **Automated Build:** GitHub Actions pipeline triggers on every push to compile code and manage dependencies via Maven.
* **Testing & Coverage:** Runs JUnit/Mockito suites; JaCoCo ensures a minimum 80% code coverage threshold.
* **Quality Gate:** SonarQube analysis identifies code smells and security vulnerabilities before merging.
* **Deployment:** Automated Docker image creation pushed to AWS ECR, followed by a rolling deployment to AWS EKS for zero-downtime.