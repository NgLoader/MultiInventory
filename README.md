# MultiInventory [![Build Status](https://jenkins.wuffy.eu/buildStatus/icon?job=MultiInventory)](https://wuffy.eu.net/job/MultiInventory/)

### Links
- **[Jenkins](https://jenkins.zockercraft.net/job/MultiInventory/)**
- **[Discord](https://chat.wuffy.eu)**
- **[Imprex-Storage](https://github.com/ImprexLabs/imprex-storage)**

### Downloads
- MultiInventory [Download latest](https://jenkins.zockercraft.net/job/MultiInventory/lastBuild/)
- Imprex-Storage [Download Latest](https://jenkins.wuffy.eu/job/Imprex-Storage/lastBuild/)

### Storage supports
- Locale
- MongoDB

### Todo
- Support SQL
- Add scoreboard saving

## IMPORTANT
Needed:
- Imprex-Storage [Download](https://github.com/ImprexLabs/imprex-storage)

## Configurate
1. Download [Imprex-Storage](https://jenkins.zockercraft.net/job/Imprex-Storage/lastBuild/)
2. Download [MultiInventory](https://jenkins.zockercraft.net/job/MultiInventory/lastBuild/)
3. Restart your minecraft server
4. Go into your plugins/ImprexStorage folder and open the config.yml
5. Add ``multiinventory`` to extensions with your storage type
#### EXAMPLE
```json
"extensions": {
    "uuid": "mongo",
    "multiinventory": "locale"
  }
```
7. Save config.yml
8. That was it.

## Developer

### Events
- **MultiInventoryLoadPlayerEvent** *Will called when the player is switch the world or joining*
- **MultiInventorySavePlayerEvent** *Will called when the player is switch the world or disconnect*

### Maven
```xml
<repositories>
	<repository>
		<id>NgLoader/MultiInventory-mvn-repo</id>
		<url>https://raw.github.com/NgLoader/MultiInventory/repository</url>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</snapshots>
	</repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>de.ngloader</groupId>
		<artifactId>multiinventory</artifactId>
		<version>1.0.0</version>
	</dependency>
</dependencies>
```

### Add this to your Plugin.yml
```yml
depend: [MultiInventory, ImprexStorage]
loadbefore: [MultiInventory, ImprexStorage]
```

License
----

Apache-2.0
