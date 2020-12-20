# MinecraftDiscordSync Plugin

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](LICENSE)
[![Discord](https://img.shields.io/discord/785956343407181824.svg)](https://discord.gg/wvcX92VyEH)

#### The Minecraft Discord Sync Plugin allows you to Sync your Minecraft and discord Server.

#### Synchronize Minecraft and Discord Ranks and Ingame <-> Discord Chat

--- 

#### Supported ServerVersions:

Minecraft Java:
- Bukkit Based Servers: 
  - [Spigot](https://getbukkit.org/download/spigot)  API: 1.15 ++
  - [PaperMC](https://papermc.io/downloads)  API: 1.15 ++
  
- Default Supported Permission Plugins:
  - [LuckPerms](https://www.spigotmc.org/resources/luckperms.28140/)
  - [Vault](https://www.spigotmc.org/resources/vault.34315/)
  
- TODO: bungeecord support

Minecraft Bedrock:
- Nukkit Based Servers: 
  - [Nukkit](https://github.com/CloudburstMC/Nukkit)  API: 1.0.9 ++
  - [PowerNukkit ( untested )](https://github.com/PowerNukkit/PowerNukkit)

- Default Supported Permission Plugins:
  - [LuckPerms](https://cloudburstmc.org/resources/luckperms.51/)
  - [Multipass](https://cloudburstmc.org/resources/multipass.29/)

- TODO: Waterdog support

---

## Commands:

- IngameCommands:
  - verify ( PERM: mcdcs.command.verify ) - Sends you an code to verify at the discord server
  - discord ( PERM: mcdcs.command.discord ) - send the discord invitelink
  - discordinfo/dcinfo ( PERM: mcdcs.command.discordinfo ) - shows some informations about the connected discordserver
- DiscordCommands:
  - verify (verificationcode) - Verifies the user by the ingame given verification code ( IngameCommand verify )
  - about - prints plugin and author infos
  - online - shows the online players at the server
  - playerlist - shows the online player list
  - serverinfo - shows some general minecraft server infos of the connected minecraft server
  
  
---

### More Features:
- Custom Activity ( GameStatus / OnlineStatus )
- Custom OnlineCounter Channel - displays every minute the live online count of the minecraftserver

---

### Default Storage Providers
- YAML ( config file in plugin datafolder )
- MongoDB ( stores data in a database - its a nice feature if the plugin is running on multiple servers to sync verifications )

---

## API:
#### The plugin provides an API wich you can use to add own discordcommands and discordevents to the discordbot instance or to use a custom permission provider or storage provider

Resolve API:
```java
MinecraftDiscordSyncAPI api = MinecraftDiscordSyncAPI.get();
```

Example PermissionProvider:

```java
class MyPermissionProvider implements IPermissionProvider {

		@Override
		public String getPrimaryPlayerGroupName(UUID uuid) {
			return MyPlugin.getInstance().resolvePrimaryGroupForPlayerByUUID(uuid);
		}

		@Override
		public String getPrimaryPlayerGroupName(String name) {
			return MyPlugin.getInstance().resolvePrimaryGroupForPlayerByName(name);;
		}
		
	}
```

Set new providers:

```java
void setNewProviderExample() {
	api.setPermissionProvider(new MyPermissionProvider());
	api.setStorageProvider(new MyStorageProvider())
}
```
---

#### TODOS:
- TODO: bungeecord support
- TODO: velocity support
- TODO: Waterdog/Waterfall support
- :)
- TODO: SQL Storage provider support
- TODO: SQLite Storage provider support
- :)
- TODO: CloudNet Perms support
- TODO: PermissionsEx support

---

## Maven Repository for the API:
```xml
<!-- WILL BE ADDED SOON -->
```
---

<div align="center">
    <h1 style="color:#154444">Other Links:</h1>
    <a style="color:#00ff00" target="_blank" href="https://github.com/VironLab"><img src="https://img.shields.io/github/followers/VironLab?label=GitHub%20Followers&logo=GitHub&logoColor=%23ffffff&style=flat-square"></img></a>
    <a style="color:#00ff00" target="_blank" href="https://discord.gg/wvcX92VyEH"><img src="https://img.shields.io/discord/785956343407181824?label=vironlab.eu%20Discord&logo=Discord&logoColor=%23ffffff&style=flat-square"></img></a>
    <a style="color:#00ff00" target="_blank" href="https://www.paypal.com/paypalme/depascaldc"><img src="https://img.shields.io/static/v1?label=Donate%20Via%20Paypal&message=paypal&style=flat-square&logo=paypal&color=lightgrey"></img></a>
</div>
