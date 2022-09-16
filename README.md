# SpotMyBackupReverse

A little playlist reverser for the useful [SpotMyBackup](http://www.spotmybackup.com) tool.

SpotMyBackup is a nice tool to import and export your [Spotify](https://www.spotify.com) playlists from one account to
another.
The only problem is that it imports the tracks in the wrong direction, so
that the newest tracks are at the top of the playlist, which is not similar to Spotify.

This tool fixes this problem and **reorders the tracks as well as the playlists** in the backup file.

## Table of Contents

* [Usage](#usage)
* [Authors](#authors--contributors)
* [License](#license)

## Usage

There are currently two ways to use the tool.

1. [Standalone via command-line](#1-standalone-via-command-line)
2. [As an library in another java project](#2-as-an-library-in-another-java-project)

#### 1. Standalone via command-line

Simply use SpotMyBackupReverse by running the following at the command line:
`java -jar SpotMyBackupReverse.jar <file>`
The file attribute should be replaced with your backup json file.

#### 2. As an library in another java project

SpotMyBackupReverse also provides a programming interface through which any
Java developer can access SpotMyBackupReverse's functionalities in their program.
For this purpose, the class `SpotMyBackupReverseAPI` is provided,
which offers various methods.

<br>

To do this, an instance of the API must first be created.
Both a standard API instance and a customized API instance can be created.
For this purpose, the class provides two constructors.

Create an default api instance:

```
SpotMyBackupReverseAPI spotMyBackupReverseAPI = new SpotMyBackupReverseAPI();
```

Create an customized api instance:

```
Logger logger = LogManager.getLogger("loggerName");
ObjectMapper objectMapper = new ObjectMapper();

SpotMyBackupReverseAPI spotMyBackupReverseAPI = new SpotMyBackupReverseAPI(logger, LogLevel.INFO, objectMapper);
```

<br>
<br>

Afterwards, the two `SpotMyBackupReverseAPI.reverseSpotMyBackupFile(...)` methods can be used to take advantage of the
tool's capabilities.

Reverse an SpotMyBackup file by providing an input and output file:

```
Path inputPath = Path.of("input.json");
Path outputPath = Path.of("output.json");

spotMyBackupReverseAPI.reverseSpotMyBackupFile(inputPath, outputPath);
```

Reverse an SpotMyBackup file by providing the content of an SpotMyBackup created file:

```
String content = "...";
String newContent = spotMyBackupReverseAPI.reverseSpotMyBackupFile(content);
```

<br>

If there are any further questions or areas for improvement, please feel free to note them via the issues.

## Authors & Contributors

* **Dominic Wienzek** - *Development* - [Skyleiger](https://github.com/skyleiger)

These are the most common authors and contributors.
See also the [github list of contributors](https://github.com/Skyleiger/SpotMyBackupReverse/contributors) who
participated in
this project.

## License

This project is is the property of the author(s) and may only be used with the consent of the author(s). Other uses are
not permitted.
