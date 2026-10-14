# Group 19 – Population Reporting System: Project Summary

## 1. About Group 19

Group 19 of **Edinburgh Napier University** built this project for the **DevOps coursework**. The team works in the GitHub repository `Drewid101/Group19-DevOps`, plans work on a Kanban board and keeps its agreed practices in the repository documents:

| Document | What it covers |
|---|---|
| `README.md` | Overview, Docker build/run steps, fixed coursework settings and CI/CD |
| `PRODUCT_BACKLOG.md` | User stories, acceptance criteria, priorities, story points |
| `docs/use-cases/` and `docs/use-case-diagrams/` | Use cases (UC-01 to UC-13) and diagrams for the Data Analyst |
| `CONTRIBUTING.md` / `Code of Conduct.md` | Branching, pull request and conduct rules |
| `SECURITY.md` | Security policy |

## 2. What the project is

A **Population Reporting System** for a fictional organisation that wants to analyse population data. It is a Java console application that reads the MySQL **`world`** sample database (countries, cities, languages) and produces **32 reports**. Each report is printed to the terminal and saved as a Markdown file in `reports/`.

The intended user is a **Data Analyst** who needs population information at different geographic levels.

## 3. Scope and aims

**In scope**

- Generate the 32 required reports across five areas:

| Area | Reports | What it shows |
|---|---|---|
| Countries | 1–6 | All countries, and top N, for the world, a continent, a region |
| Cities | 7–16 | All cities, and top N, for the world, continent, region, country, district |
| Capital cities | 17–22 | All capitals, and top N, for the world, continent, region |
| Population distribution | 23–25 | Total, city and non-city population (with percentages) per continent, region, country |
| Population totals | 26–31 | Population of the world, a continent, a region, a country, a district, a city |
| Languages | 32 | Speakers and % of world population for Chinese, English, Hindi, Spanish, Arabic |

- Sort results from the largest population to the smallest, with the column names and order the organisation requires.
- Demonstrate **DevOps practice**: version control with Git/GitHub, automated builds and tests (CI), containers with Docker, and a team workflow with branches, pull requests and a Kanban board.

**Out of scope**: a web or graphical interface, editing data, and any database other than `world`. The "top N", continent, region and similar values are set in the code, not entered by the user while it runs.

**Status:** all 32 requirements are marked **Done** in `PRODUCT_BACKLOG.md`.

## 4. How it works

```
world.sql  ->  MySQL (Docker)  ->  App.java  ->  report classes  ->  terminal + reports/*.md
```

- `App` connects to MySQL (retrying while it starts), runs reports 1–32 in order, then disconnects.
- Report classes are grouped by theme (`Countries…`, `Cities…`, `CapitalCities…`, `Population…`, `LanguagePopulationReport`). Each runs an SQL query and builds a list of objects.
- Shared base classes (`CountriesReportBase`, `CitiesReportBase`, `CapitalCitiesReportBase`) turn query results into objects and print them.
- `City`, `Country` and `CountryLanguage` are the data objects.
- `PopulationUtils` calculates city and non-city percentages.
- `ReportWriter` saves each report as a numbered Markdown file, for example `01_Countries_World_Report.md`.

## 5. Technology

| Area | Choice |
|---|---|
| Language | Java 17 (the development repo uses JDK 24) |
| Build | Maven (`pom.xml`) |
| Database | MySQL with the `world` dataset (`db/world.sql`) |
| Testing | JUnit 5 connection regression tests and full-output container acceptance checks |
| Containers | Docker and Docker Compose |
| CI/CD | GitHub Actions, GHCR candidates and promotion, tested-JAR releases and reports publication |

## 6. Testing

- **Connection regression tests** in this repository cover invalid connection arguments and interruption handling. Database settings use the original Java static strings, not environment variables.
- **Container integration checks** build or pull the application and database images, run all reports against MySQL, and compare all 32 fresh Markdown files with the committed output.
- The full report unit/integration suite and coverage setup described for `GROUP19-PREDEVELOPMENT` remain in that separate repository and have not been imported here.

## 7. How to run it

```bash
docker compose build
docker compose up --no-build --pull never --abort-on-container-exit --exit-code-from app
mkdir -p generated-reports
docker compose cp app:/app/reports/. generated-reports/
sh scripts/verify-reports.sh generated-reports
docker compose down                     # keep the database volume
```

Needs only Docker with Compose v2 or newer. Docker builds `semApp.jar` from source using Java 17 and Maven. The local reports appear in `generated-reports/`, leaving the committed `reports/` unchanged. See `README.md` for published-image and optional host-JAR instructions.

## 8. Repositories and branches

| Repo | Purpose |
|---|---|
| `Drewid101/Group19-DevOps` | The team repository: Java 17, package `com.napier.sem`, CI in `.github/workflows/ci.yml` |
| `osamorju/GROUP19-PREDEVELOPMENT` | Jude's development repository, where the code and tests were first written (JDK 24, package `com.napier.gp19`) |

Work in the team repository flows `feature/…` → `Develop` → `Release` → `master`.

**Delivery and local configuration**

- MySQL Connector/J is packaged in the executable JAR.
- Java and Compose use fixed `root`/`root` credentials. Compose keeps host port `33060` and connects the application to `db:3306` through the existing command-line arguments. Comments warn that production must use managed secrets, restricted database access and TLS.
- The application image builds its own JAR and runs as a non-root user; the database image embeds the existing `db/world.sql` without changing the MySQL 8.0 version.
- Published candidates are acceptance-tested before `latest` is promoted. Releases contain the exact tested JAR and reports, and use new tags rather than rewriting existing releases.
- A maintainer must permit workflow package/repository writes and make both GHCR packages public before unauthenticated image pulls work.
- The separate development repository's full test suite is still a possible future import, not part of this container replication.

## 9. Team workflow

- Work happens on `feature/`, `bugfix/` and `hotfix/` branches, merged by pull request after review.
- Tasks are tracked using the product backlog and the team's GitHub project board.
- CI runs connection tests and complete container report checks; no PMD or Codecov job is currently configured in this repository.
