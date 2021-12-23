## Locally Run:

Go to project root directory and run the following command

### 1. Test run command

```commandline
mvn clean test -q
```

mvn test

### 2. Single test run command

```commandline
mvn clean test -Dcucumber.filter.tags="@login" -q
```

## Browserstack Run:

Go to project root directory and run the following command

### 1. Test run command

```commandline
mvn clean test -Dbrowserstack=true -Dprofile=s21 -q
```

### 2. Single test run command

```commandline
mvn clean test -Dbrowserstack=true -Dprofile=s21 -Dcucumber.filter.tags="@login" -q
```

### 3. Parallel test run command

```commandline
mvn clean test -Dbrowserstack=true -Dprofile=s21 -P parallel -Dcucumber.filter.tags="@login" -q
```