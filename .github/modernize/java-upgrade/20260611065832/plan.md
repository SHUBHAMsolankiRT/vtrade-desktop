# Upgrade Plan: VTrade Project (20260611065832)

- **Generated**: 2026-06-11 12:59:00
- **HEAD Branch**: N/A (Git not available)
- **HEAD Commit ID**: N/A (Git not available)

## Version Control Note

⚠️ **Git is not available in this workspace.** Changes will not be version-controlled during this upgrade. All modifications will be tracked in progress.md for manual review and backup.

## Available Tools

**JDKs**
- JDK 23: C:\Program Files\Java\jdk-23\bin
- JDK 25: C:\Users\ACER\AppData\Local\jdks\jdk-25.0.2\bin (target JDK)
- JDK 26: C:\Program Files\Java\jdk-26\bin

**Build Tools**
- Maven 3.9.16: C:\Users\ACER\.maven\maven-3.9.16\bin

## Guidelines

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

- Target Java version: **Java 25 (LTS)**
- Build system: Maven 3.9.16
- Git unavailable: changes will be tracked locally only

## Options

- Working branch: modernize/java-20260611122737 (pre-created by upgrade coordinator)
- Run tests before and after the upgrade: true

## Upgrade Goals

- **Primary Goal**: Upgrade Java version from 21 to 25 (LTS)
- **Project**: VTrade Desktop Trading Platform
- **Scope**: Update compiler source/target versions in pom.xml and verify compatibility with all dependencies

## Technology Stack

| Technology/Dependency | Current | Min Compatible | Why Incompatible |
|----------------------|---------|-----------------|------------------|
| Java | 21 | 25 | User requested |
| Maven | 3.9.16 | 3.9.0 | Currently meets minimum; no upgrade needed |
| mysql-connector-j | 9.6.0 | 9.6.0 | Fully compatible with Java 25 |
| Swing (JDK built-in) | 21 | 25 | Supported across all Java versions |

## Derived Upgrades

No additional framework upgrades required. MySQL Connector 9.6.0 is fully compatible with Java 25, and there are no breaking changes in the Java language between versions 21 and 25 affecting this project's code.

## Impact Analysis

### Subsection: Dependency Changes

| File | Dependency | Current | Action | Target | Reason |
|------|-----------|---------|--------|--------|--------|
| pom.xml | maven.compiler.source | 21 | upgrade | 25 | User requested target version |
| pom.xml | maven.compiler.target | 21 | upgrade | 25 | User requested target version |
| pom.xml | mysql-connector-j | 9.6.0 | keep | 9.6.0 | Fully compatible with Java 25; no upgrade needed |

### Subsection: Source Code Changes

**No source code changes required.** The project uses standard Java and Swing APIs compatible with Java 25. Examined files:
- LedgerScreen.java: Standard Swing usage
- DBConnection.java: Standard JDBC usage with mysql-connector-j
- Other UI/networking/core_logic classes: No deprecated or incompatible patterns detected

Java 21→25 introduces no breaking changes affecting this codebase.

### Subsection: Configuration Changes

No configuration file changes required beyond pom.xml compiler properties.

### Subsection: CI/CD Changes

No CI/CD files (Dockerfile, workflows, etc.) detected in project root. If deployment configuration exists elsewhere, update any hardcoded Java version references from 21 to 25 before deployment.

### Subsection: Risks & Warnings

None identified. This is a straightforward version bump with no API breaking changes. The project uses only standard JDK APIs (Swing, JDBC, standard collections) which are stable across Java 21 and 25.

## Upgrade Steps

- **Step 1: Setup Environment**
  - **Rationale**: Ensure all required JDKs and build tools are available before compilation
  - **Changes to Make**: Verify Maven 3.9.16 and JDK 25 are in place (already completed during planning)
  - **Verification**: 
    - Command: `java -version` and `mvn --version`
    - JDK: 25.0.2
    - Expected Result: Both tools report correct versions

- **Step 2: Setup Baseline**
  - **Rationale**: Establish baseline compilation and test metrics with current Java 21 (for comparison)
  - **Status**: **SKIP** — JDK 21 is not available on this system; baseline will be skipped and final validation will use Java 25 as the reference point
  - **Verification**: Skipped due to unavailable base JDK

- **Step 3: Update Maven Compiler Target to Java 25**
  - **Rationale**: Upgrade the project to compile and run on Java 25 LTS
  - **Changes to Make**: Update `maven.compiler.source` and `maven.compiler.target` properties in pom.xml from 21 to 25 (see Dependency Changes table)
  - **Verification**: 
    - Command: `mvn clean test-compile -q`
    - JDK: 25.0.2 (C:\Users\ACER\AppData\Local\jdks\jdk-25.0.2\bin\java)
    - Build tool: C:\Users\ACER\.maven\maven-3.9.16\bin\mvn
    - Expected Result: Compilation SUCCESS (main + test code)

- **Step 4: Final Validation**
  - **Rationale**: Verify upgrade goals met, all code compiles and all tests pass on Java 25
  - **Changes to Make**: No code changes; verify Step 3 changes are in place
  - **Verification**: 
    - Command: `mvn clean test -q`
    - JDK: 25.0.2
    - Build tool: Maven 3.9.16
    - Expected Result: BUILD SUCCESS, 100% test pass rate
    - Acceptance Criteria: All tests pass on Java 25 LTS

---

**Plan Summary**: This upgrade is low-risk, requiring only pom.xml updates and compatibility verification. No source code changes needed. Estimated effort: 15 minutes for compilation + testing.
