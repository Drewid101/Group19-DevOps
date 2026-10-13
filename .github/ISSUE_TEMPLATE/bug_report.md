---
name: Bug report
about: Create a report to help us improve
title: ''
labels: ''
assignees: ''

---


## Bug Report

Thanks for taking the time to report a bug!  
Please provide as much detail as possible so we can reproduce and fix the issue quickly.

---

### Contact Details
**How can we reach you for clarification?**  
*Example:* your.email@example.com

---

### What happened?
Describe what went wrong and what you expected to happen.

*Example:*
```pgsql
Error when generating Report 7 – SQL column not found
```

---

### Which report or section caused the issue?

Select one:

- Connection / Database Startup
- Country Reports (1–6)
- City Reports (7–16)
- Capital City Reports (17–22)
- Population Reports (23–31)
- Language Report (32)
- Other / Unknown

---

### Full Error Message or Stack Trace

Copy and paste the console or log output below:

```shell
java.sql.SQLSyntaxErrorException: Unknown column 'Population' in 'field list'
```

### SQL Query (if applicable)
If the issue involves a database query, paste it here:

```sql
    SELECT Name, Population FROM country WHERE Continent='Asia';
```
### Environment
Where did you run the system?

- Localhost (MySQL Workbench or Docker)

- Remote Server

- GitHub Actions / CI

- Other

### System Version
Which version or branch of the Java system are you using?

- version 0.1

- version 0.2

- latest

- Custom Branch

### Steps to Reproduce
Help us recreate the bug.


```
1. Run App.java
2. Wait for connection
3. Error occurs during Report 3 generation
```
### Relevant Log Output
Paste any additional logs or error messages here.

---

### Code of Conduct

By submitting this issue, you agree to follow our
[Code of Conduct](https://github.com/Drewid101/Group19-DevOps/blob/master/CODE_OF_CONDUCT.md).

- [ ] I agree to follow this project's Code of Conduct