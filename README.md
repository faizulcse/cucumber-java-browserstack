## Locally Run:

Go to project root directory and run the following command

### 1. Test run command

```commandline
mvn test -q
```

mvn test

### 2. Single test run command

```commandline
mvn test -Dcucumber.filter.tags="@login" -q
```

## Browserstack Run:

Go to project root directory and run the following command

### 1. Test run command

```commandline
mvn test -Dbrowserstack=true -q
```

### 2. Single test run command

```commandline
mvn test -Dbrowserstack=true -Dcucumber.filter.tags="@login" -q
```

### 3. Parallel test run command

```commandline
mvn test -Dbrowserstack=true -P parallel -Dcucumber.filter.tags="@login" -q
```