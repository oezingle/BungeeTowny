# BungeeTowny
A plugin that hooks into Towny across multiple servers, syncronizing with a MySQL database

# This plugin is WIP. Features and Goals are subject to change

# Features

## Current
- Chat features (Optional)
- TownyChat configuration compatibility
- Multiple chat moderation features (Optional)
  - /mute & /unmute
  - /ignore & /unignore
  - /message & /msg (Optional)
- Placeholder API Integration
- JSON text formatting in Global, Town and Nation chats
- Inter-player messaging across servers

## Planned
- Spigot chat API Integration
- Ally chats
- Towny synchronization
- Event logging
- Essentials Integration
- Town spawns and Outposts

# Commands
- /chat <channel> [msg] - use that channel for chat, or send a single message
- /mute and /unmute - mute/unmute a given player for everyone
- /ignore and /unignore - ignore what a given player is saying
- /message - aliased to /msg when the messaging submodule is enabled

# Permissions
See [plugin.yml](https://github.com/oezingle/BungeeTowny/blob/master/src/main/resources/plugin.yml)

# Dependencies
- A MySQL server - Mariadb, Microsoft SQL, etc
- BungeeCord / Velocity (With plugin messaging channel)
