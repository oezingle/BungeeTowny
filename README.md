# BungeeTowny
A plugin that hooks into Towny across multiple servers, syncronizing with a MySQL database

# This plugin is WIP. Features and Goals are subject to change

# Features

## Current
- Chat features (Optional)
  - TownyChat configuration compatibility
  - Inter-player messaging across servers (Optional)
- Multiple chat moderation features (Optional)
  - /mute & /unmute
  - /ignore & /unignore
  - /message & /msg (Optional)
- Placeholder API Integration
- JSON text formatting in Global, Town and Nation chats
- Async database access, keeping the server TPS high

## Planned
- Spigot chat API Integration
- Ally chats
- Towny synchronization
- Event logging
- Essentials Integration
- Town spawns and Outposts
- Configuration synchronization

# Commands
## Chat
- /chat <channel> [msg] - use that channel for chat, or send a single message
- /mute and /unmute - mute/unmute a given player for everyone
- /ignore and /unignore - ignore what a given player is saying
- /message - aliased to /msg when the messaging submodule is enabled
## Plugin
 - /bungeetowny - status of the plugin and reloading options

# Permissions
See [plugin.yml](https://github.com/oezingle/BungeeTowny/blob/master/src/main/resources/plugin.yml)

# Issues
Feel free to [submit an issue](https://github.com/oezingle/BungeeTowny/issues/new)

# Dependencies
- A MySQL server - Mariadb, Microsoft SQL, etc
- BungeeCord / Velocity (With plugin messaging channel)
- Towny on at least one server. Optimally, it would be the server players first find themselves on
