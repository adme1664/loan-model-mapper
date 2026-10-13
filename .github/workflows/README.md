# GitHub Actions Workflows

This directory contains CI/CD workflows for the loan-model-mapper application.

## Workflows

### 1. CI Pipeline (`ci.yml`)

**Triggers:**
- Push to `main` branch
- Push to any `feature/**` branch
- Pull requests targeting `main` branch

**Jobs:**
- **checkout**: Validates repository checkout and displays build information
- **build**: Compiles the application, runs tests, and uploads artifacts

**What it does:**
- Checks out the code
- Sets up JDK 25
- Caches Maven dependencies for faster builds
- Builds the application with `./mvnw clean package`
- Runs unit tests with `./mvnw test`
- Uploads JAR artifacts (retained for 7 days)

### 2. Main Branch CI/CD (`main.yml`)

**Triggers:**
- Push to `main` branch
- Manual workflow dispatch

**Jobs:**
- **checkout**: Repository checkout with full git history
- **build**: Builds, tests, and creates artifacts
- **code-quality**: Runs additional quality checks

**What it does:**
- Complete build and test cycle
- Generates test reports
- Uploads artifacts (retained for 30 days on main branch)
- Runs code quality verification

## Branch Strategy

### Feature Branches
```bash
# Create a feature branch
git checkout -b feature/your-feature-name

# Push to trigger CI
git push origin feature/your-feature-name
```

The CI pipeline will automatically run on any branch starting with `feature/`.

### Main Branch
```bash
# Merge to main triggers both workflows
git checkout main
git merge feature/your-feature-name
git push origin main
```

Merging to `main` triggers:
1. Standard CI pipeline (`ci.yml`)
2. Enhanced main branch pipeline (`main.yml`) with extended artifact retention

## Artifacts

Build artifacts are uploaded after successful builds:

- **Feature branches**: Retained for 7 days
- **Main branch**: Retained for 30 days

To download artifacts:
1. Go to the Actions tab in GitHub
2. Click on a workflow run
3. Scroll down to "Artifacts"
4. Download the JAR file

## Local Testing

Test the build locally before pushing:

```bash
# Build without tests
./mvnw clean package -DskipTests

# Build with tests
./mvnw clean package

# Run tests only
./mvnw test
```

## Requirements

- **JDK**: Java 25 (Temurin distribution)
- **Build tool**: Maven (wrapper included)
- **Dependencies**: Managed in `pom.xml`

## Troubleshooting

### Build fails with "mvnw: Permission denied"
```bash
# Fix locally
chmod +x mvnw
git add mvnw
git commit -m "Fix mvnw permissions"
git push
```

### Maven cache issues
The workflow uses GitHub Actions cache for Maven. If you encounter issues:
- The cache key is based on `pom.xml` hash
- Cache is automatically invalidated when `pom.xml` changes
- Manual cache clearing: Go to Actions → Caches (in repository settings)

## Future Enhancements

Potential additions to these workflows:
- Docker image building
- Deployment to staging/production
- Security scanning (OWASP, Snyk)
- Code coverage reporting (JaCoCo)
- SonarQube analysis
- Release automation
- Notification to Slack/Teams