# Export Coros Training Calendar to ICS Calendar

⚠️ This repository is using a **non-public API** from [COROS Training Hub](https://t.coros.com/) that could break
anytime.

## Getting started

- Install Java and Maven
- Run `mvn clean package -DskipTests`
- Run `java -cp target/coros.jar --username $COROS_USERNAME --password $COROS_PASSWORD --startDate $YYYYMMDD --endDate $YYYYMMDD --output ICS_FILE_PATH`

For example:

```shell
java -jar coros.jar --username foo@bar.com --password baz --startDate 20220101 --endDate 20220131 --output ./coros-training-calendar.ics
```

## Running tests

- Create the file `src/test/resources/authentication.properties` with properties username and password of the Coros account

## Licence

[MIT License](LICENSE.md)
