# COROS Training Hub Exporter

## Introduction

This app can bulk export activities and export training schedule from the [COROS Training Hub](https://t.coros.com/).

The `FIT` or `ICS` files for activities or training schedule can be imported in other tools ([Garmin Connect](https://connect.garmin.com), [Strava](https://www.strava.com), Apple Calendar and Google Calendar, etc.) 

⚠️ This repository is using a **non-public API** from [COROS Training Hub](https://t.coros.com/) that could break anytime.

## Getting started

- Install Java
- Download the [latest release](https://github.com/futoshita/Coros-Training-Hub-Exporter/releases/latest) of this app
- Run the command below in a terminal

`java -cp coros.jar --username $COROS_USERNAME --password $COROS_PASSWORD --startDate $YYYYMMDD --endDate $YYYYMMDD --type $EXPORT_TYPE --output $OUTPUT_DIRECTORY`

## Parameters

Values for `$EXPORT_TYPE`:
- `TRAINING_CALENDAR`
- `ACTIVITIES`

If `EXPORT_TYPE`=`TRAINING_CALENDAR`, a `.ics` file is generated in the output directory with name `coros-training-calendar.ics`.  
If `EXPORT_TYPE`=`ACTIVITIES`, `.fit` files are downloaded and stored in the output directory with name `$date_$activityId.fit`.

## Examples

```shell
java -jar coros.jar --username foo@bar.com --password baz --startDate 20220101 --endDate 20220131 --type TRAINING_CALENDAR --output ~/Documents
```

```shell
java -jar coros.jar --username foo@bar.com --password baz --startDate 20220101 --endDate 20220131 --type ACTIVITIES --output ~/Documents/coros-activities
```

## Running tests

Create the file `src/test/resources/authentication.properties` with properties `username` and `password` of the COROS account.

## Licence

[MIT License](LICENSE.md)
