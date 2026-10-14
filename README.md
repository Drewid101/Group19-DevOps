# Group19-DevOps
Created for ENU DevOps Module Assessment 1

A Java 17 batch application that reads the MySQL `world` sample database and
generates 32 population reports. Report queries, parameters, and Markdown
formats are unchanged. The committed [reports](./reports/) are the reference
output; local runs are copied into a separate, ignored directory.

## Generate reports locally with Docker

Install Docker with Docker Compose v2 or newer. You do **not** need a local JDK,
Maven installation, or an existing JAR.

From the repository root, with no environment-variable or password setup:

```bash
docker compose config --quiet
docker compose build
docker compose up --no-build --pull never --abort-on-container-exit --exit-code-from app
mkdir -p generated-reports
docker compose cp app:/app/reports/. generated-reports/
sh scripts/verify-reports.sh generated-reports
docker compose down
```

The app starts only after MySQL accepts an authenticated connection to `world`.
It prints the reports, writes them inside the app container, and exits. Compose
returns the app's exit code. The verifier checks all 32 filenames and compares
their full contents with the committed reports.

Read the output in `generated-reports/`. Copy it **before** removing the app
container. No bind mount overwrites the committed reports or introduces host
filesystem permission problems for the non-root app user.

`docker compose down` keeps the named database volume. Running the same commands
again reuses the dataset. SQL initialization runs only on an empty database
volume. Do not use `down --volumes` unless you deliberately want to delete that
project's database data.

The original port mapping remains `33060:3306`. If another application uses
that host port, stop this project's startup and choose a free host port in
`docker-compose.yml`; the app's internal `db:3306` connection stays unchanged.
Use `docker compose --project-name <unique-name>` consistently for every command
when running multiple independent checkouts to avoid sharing Compose resources.

## Run the published containers

After the first successful `master` delivery and after both GHCR packages have
been made public, use the same checkout:

```bash
docker compose pull
docker compose up --no-build --pull never --abort-on-container-exit --exit-code-from app
mkdir -p generated-reports
docker compose cp app:/app/reports/. generated-reports/
sh scripts/verify-reports.sh generated-reports
docker compose down
```

Published images:

- `ghcr.io/drewid101/group19-devops-app:latest`
- `ghcr.io/drewid101/group19-devops-database:latest`

Both images are published for Linux AMD64 and ARM64. `latest` moves only after
the published candidates pass acceptance checks. For a reproducible run, replace
`:latest` in both Compose image strings with `:sha-<full-commit-hash>` before
pulling and running. Use a checkout of the same commit when comparing its
reports.

The release also includes the tested JAR, a reports archive, and the Compose
file. Downloading just the Compose file is enough to pull and run public images
and extract the reports as above. Source and the verification script are needed
only for local builds and comparisons against the committed output.

Private packages require `docker login ghcr.io`. Public distribution requires a
maintainer to change **both** package visibilities to **Public** in GitHub
package settings; a workflow cannot assume that has already happened.

## Fixed coursework settings and host execution

The app does not read environment variables. The original Java credentials,
database name, host-run port, and retry defaults are retained:

| Setting | Host-run default | Compose setting |
| --- | --- | --- |
| Database location | `localhost:33060` | `db:3306` |
| Database | `world` | `world` |
| User | `root` | `root` |
| Password | `root` | `root` |
| Connection attempts | `5` | `5` |
| Retry delay | `2000` ms | `2000` ms |

Compose uses literal values for the official MySQL image's required initialization
settings. Its original `example` password is aligned with the Java app's original
`root` password. There is no `.env` file, host-variable interpolation, or app
environment reader. The container gets its database location through the
existing two command-line arguments.

When overriding a host JAR run, provide both location and retry delay. The
location must not be blank and the delay must be a non-negative integer.
Connection exhaustion, interruption, invalid arguments, and report write
failures now fail the process rather than silently succeeding.

With JDK 17+ and Maven installed:

```bash
mvn --batch-mode --no-transfer-progress clean package
docker compose up --detach --no-build --pull never --wait db
java -jar target/semApp.jar localhost:33060 2000
docker compose down
```

Build the database image first if it is not already available. A direct host
JAR run writes into `reports/`, as before; unlike Docker output extraction,
this replaces the committed report files.

The static `root` credentials, published database port, and disabled JDBC TLS
are retained **only for this coursework dataset**. Production must use managed
secrets, a restricted database user, verified TLS, and private or explicitly
restricted database networking. Do not use real or production data here. These
warnings also appear beside the settings in the Java source and Compose file.

## CI/CD

[The workflow](./.github/workflows/ci.yml) preserves checks for pull requests and
pushes to `master`, `Develop`, and `Release`, with manual dispatch available.

1. **All supported branches:** run connection regression tests and package
   `semApp.jar` using Java 17.
2. **Pull requests and non-master branches:** build both containers locally,
   run them with the real database, and verify the 32 fresh reports. These jobs
   do not publish images or modify repository contents.
3. **Master pushes/manual runs:** build and push AMD64/ARM64 images under
   `sha-<commit>` tags. Builds and unit tests can run in parallel.
4. **Published-image acceptance:** pull those exact candidates without rebuilding,
   run the two-container integration test, compare all reports, validate the
   packaged JAR, check connection-failure exit behavior, and confirm both
   architecture manifests exist. Fixed Compose `latest` names are locally
   retagged to these pulled candidates without changing the registry's tags.
5. **After acceptance:** promote the existing manifests to `latest`, release the
   JAR copied from the tested image without rebuilding it, and publish the tested
   reports to the `reports` branch.

Every delivery gets a new `build-<run-id>-<attempt>` release tag. Existing Git
tags and release assets are not rewritten. Reports publication retains branch
history, does not force-push, and does not delete unrelated files.

The workflow uses the automatic `GITHUB_TOKEN`, with package/repository write
permissions only in the jobs that need them. No Codecov token or personal access
token is required. Repository/organization policy must permit GHCR publication,
release creation, and writing the `reports` branch.

CI uses the same fixed coursework credentials and always cleans up its own test
containers and volumes on its isolated runner. Local instructions deliberately
retain database volumes. Documentation-only changes skip automatic CI; committed
report changes do **not**, because those reports are acceptance-test inputs.

The development repository's full report unit-test suite and Codecov setup have
not been imported. This repository has focused application connection tests
and a real two-container, full-output integration check.
