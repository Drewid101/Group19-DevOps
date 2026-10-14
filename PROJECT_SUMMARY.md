# Group 19 – Population Reporting System: Project Summary

## 1. About Group 19

Group 19 of **Edinburgh Napier University** built this project for the **DevOps coursework**. The team works in the GitHub repository `Drewid101/Group19-DevOps`, plans work on a Kanban board and keeps its agreed practices in the repository documents:

| Document | What it covers |
|---|---|
| `README.md` | Overview, requirements, build and run steps, requirements table |
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

**Status:** all 32 requirements are marked **Met** in `README.md` and **Done** in `PRODUCT_BACKLOG.md`.

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
| Testing | JUnit 5, JaCoCo coverage (in the development repo) |
| Containers | Docker and Docker Compose |
| CI | GitHub Actions, with Codecov coverage |

## 6. Testing

- **Unit tests** (in the `GROUP19-PREDEVELOPMENT` repo) cover every report class, the data objects, `PopulationUtils` and `ReportWriter`: 154 tests, all passing.
- **Integration tests** (`AppIntegrationTest`, 35 tests) run the reports against a real MySQL database, all passing when the database is running.
- Integration tests are excluded from the normal `mvn package` run, so they run only when asked for: `mvn test -Dtest=AppIntegrationTest`.

## 7. How to run it

```bash
docker compose up -d db                 # start MySQL and load world.sql (wait ~30s)
mvn clean package                       # build and run unit tests
java -jar target/semApp.jar localhost:33060 3000          # generate the reports (team repo jar name)
docker compose down                     # stop the database
```

Needs JDK 17 or later, Maven 3.9+ and Docker. The reports appear in `reports/`.

## 8. Repositories and branches

| Repo | Purpose |
|---|---|
| `Drewid101/Group19-DevOps` | The team repository: Java 17, package `com.napier.sem`, CI in `.github/workflows/ci.yml` |
| `osamorju/GROUP19-PREDEVELOPMENT` | Jude's development repository, where the code and tests were first written (JDK 24, package `com.napier.gp19`) |

Work in the team repository flows `feature/…` → `Develop` → `Release` → `master`.

**Known follow-ups**

- The team `pom.xml` needs the `mysql-connector-j` dependency for the app to connect to MySQL. Without it the app starts, prints "Could not load SQL driver" and skips the reports.
- The code uses the database password `root`, while `docker-compose.yml` sets `example`.
- In `docker-compose.yml` the app container needs `db:3306` as its database address.
- The unit and integration tests live in the `GROUP19-PREDEVELOPMENT` repository and have not yet been moved to the team repository.

## 9. Team workflow

- Work happens on `feature/`, `bugfix/` and `hotfix/` branches, merged by pull request after review.
- Tasks are tracked on the project Kanban board linked from `README.md`.
- Code style is checked against PMD rules (naming conventions, for example), as described in `README.md`.
