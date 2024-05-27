

# OneBlockPlugin (1.16.4)

A Minecraft plugin that adds a random block distribution system.

## Features

- Random block distribution to players.
- Color-changing BossBar that shows the countdown for the next block.
- Configurable welcome message and block interval.

## Installation

1. Clone the repository or download the source code.
2. Compile the plugin using Maven or Gradle.
3. Place the compiled `.jar` file in your Minecraft server's `plugins` folder.
4. Start your server to generate the default configuration files.
5. Configure the plugin by editing the `config.yml` file located in the `plugins/OneBlockPlugin` folder.

## Configuration

- `settings.welcome_message`: The message sent to players when they join the server.
- `settings.block_interval`: The interval (in seconds) between each random block distribution.
- `settings.random_block_message`: The message sent to all players when someone receives a random block. Use `{player}` and `{block}` placeholders.

Example `config.yml` file:

```yaml
settings:
  welcome_message: "Welcome to the server!"
  block_interval: 60
  random_block_message: "{player} received a {block}!"
```

## Commands

- `/startblocks`: Starts the random block distribution.
- `/stopblocks`: Stops the random block distribution.

## Permissions

- `oneblock.receive`: Allows the player to receive random blocks.

## Usage

1. Start the server.
2. Join the server and use the `/startblocks` command to begin the random block distribution.
3. Use the `/stopblocks` command to stop the distribution.
4. Players with the `oneblock.receive` permission will receive random blocks at the configured interval.

## License

This project is licensed under the MIT License.
```
