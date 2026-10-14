# Project Contributions

This document records what each member of **Group 19** contributed to the Population Reporting System and how the team worked together. It is based on the repository history (commits and merged pull requests). Each member should read their own entry and correct it if something is missing.

## Team summary

Group 19 (Edinburgh Napier University, DevOps coursework) split the project into planning and governance, build and delivery tooling, and the application itself:

- **Planning and governance:** the team Code of Conduct, the use cases and use-case diagrams, the product backlog, the contributing guide and the security policy.
- **Build and delivery:** a Maven project that builds a runnable JAR, a Dockerfile, a Docker Compose setup (MySQL plus the app), a GitHub Actions pipeline that builds the JAR and the Docker image, and the `world` database script.
- **The application:** the Java code for all 32 required reports, the generated Markdown report files, and the unit and integration tests (kept in the development repository, `osamorju/GROUP19-PREDEVELOPMENT`).

All 32 report requirements are implemented and marked **Met** in the requirements table (see `PROJECT_SUMMARY.md` and `PRODUCT_BACKLOG.md`).

## Contribution summary

| Member | GitHub username | Main areas | Evidence | Confirmation |
|---|---|---|---|---|
| Jude Osamor | @osamorju | Product backlog and prioritisation, the report application (32 reports), report output files, tests, project documents, reviewing and merging team pull requests | PRs #6, #8, #44, #57, #58, #59, #60 | Confirmed |
| Britney | @Britney-Med | Team Code of Conduct, use cases and use-case diagrams, `world` database script | Commits of 12 and 14 Oct 2026; branches `docs/Code-of-conduct`, `docs/use-cases`, `feature/world-sql-file` | Pending |
| Reynold John | [username to confirm] | Maven starter project, Dockerfile, GitHub Actions CI pipeline, promoting work through `Develop`, `Release` and `master` | Commits of 10 and 13 Oct 2026; PRs #53 to #56 | Pending |
| Sayed Habib | @mrhabib11 | Docker Compose setup for the database and app, reviewing and merging documentation and Docker work | Commit "Add Docker Compose setup for database and app (#41)"; PRs #45, #48, #49, #50 | Pending |
| Drewid101 | @Drewid101 | Created and owns the team repository | Initial commit, 8 Oct 2026 | Pending |

## Detailed contributions

### Jude Osamor (@osamorju)

**Responsibilities:** managing and prioritising the Product Backlog using GitHub Issues and Projects, deciding the order in which the 32 reports were built, defining input limits for the "Top N" reports, and checking that report output matches the required column names, order and structure.

**Completed work:**

- Wrote and maintained the Product Backlog and the requirements table.
- Built and tested the report application: all 32 reports, retrying database connection, Markdown report output, and unit and integration tests (154 unit tests and 35 integration tests passing).
- Moved the code into the team repository under `com.napier.sem`, added the MySQL dependency, made the app exit cleanly when no database is available so the CI build stays green, and added the 32 generated reports.
- Added the project summary, contributing guide, security policy and licence.
- Reviewed and merged teammates' pull requests, and followed up on team progress.

### Britney (@Britney-Med)

**Completed work:**

- Wrote the team Code of Conduct, including attendance expectations.
- Wrote the initial use cases and use-case diagrams (`docs/use-cases/`, `docs/use-case-diagrams/`).
- Added the `world` database SQL script (`db/world.sql`).

### Reynold John

**Completed work:**

- Created the starter Maven project that builds `semApp.jar`.
- Added the Dockerfile for the app container.
- Added the GitHub Actions workflow that builds the JAR and the Docker image.
- Merged the pull requests that promoted work from `Develop` to `Release` and `master`.

### Sayed Habib (@mrhabib11)

**Completed work:**

- Added the Docker Compose setup for the MySQL database and the app, including a database health check.
- Reviewed and merged the use-case and Docker pull requests.

## How the team collaborated

- **Git branching:** work was done on separate `feature/` and `docs/` branches (for example `feature/dockerfile`, `feature/maven-jar`, `feature/github-actions`, `feature/docker-compose-db`, `docs/use-cases`) and brought in through pull requests rather than direct commits.
- **Review and merging:** pull requests were merged by teammates other than the author, so most changes had a second person look at them.
- **Release flow:** changes moved through `Develop` and `Release` before `master`, following `CONTRIBUTING.md`.
- **Continuous integration:** GitHub Actions builds the JAR and the Docker image on pushes and pull requests, so broken builds are caught early.
- **Planning:** tasks were tracked on the GitHub Kanban board and in the Product Backlog, with user stories, acceptance criteria and story points.
- **Team agreements:** the Code of Conduct sets expectations on conduct and attendance, and the contributing guide sets branch naming and pull request rules.
- **Communication and meetings:** [add how and when the team met and communicated].

## Notes for members

- Please check your entry and send corrections, then change **Pending** to **Confirmed**.
- Do not add passwords, access tokens or private messages to this file.
