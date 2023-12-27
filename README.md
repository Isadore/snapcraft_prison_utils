Uses the Minecraft Forge API to implement quality of life improvements for the SnapCraft.net "Prison" gamemode

**Features:**
- Inventory stack percentage tracker (Emerald and Diamond)
* Slicing Pickaxe Timer
  - Automatically detects slicing action by recording number of items updated in the players inventory by the server at once
  - Stores timestamp to disk in case client restarts

**Custom Bar Graphics:**

![Bar Graphics](https://github.com/Isadore/snapcraft_prison_utils/blob/master/readme/sample_image.png)
```
1. Percent of Inventory Stacks Full of Diamonds

2. Percent of Inventory Stacks Full of Emeralds

3. Slicing Pickaxe Cooldown Timer
```

*Notes:*

*Item packet watching based on code from [https://github.com/TheAlphaEpsilon/Minecraft-Forge-1.15-Packet-Event](https://github.com/TheAlphaEpsilon/Minecraft-Forge-1.15-Packet-Event)*
