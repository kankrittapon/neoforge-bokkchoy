# Mod Details: RPGEasyMode (Antigravity RPG)

> **อัปเดตล่าสุด:** 12 ก.พ. 2026  
> **Platform:** NeoForge 1.21.1  
> **Mod ID:** `rpgem`

เอกสารนี้เป็นรายละเอียดทั้งหมดของ Mod ตรวจสอบจาก Source Code โดยตรง

---

## 1. ⚔️ Items (ไอเท็มทั้งหมด)

### 💎 Upgrade Materials (หินตีบวก)
| ไอเท็ม | Registry ID | คำอธิบาย | แหล่งที่มา |
|--------|-------------|----------|------------|
| Upgrade Stone Tier 1 | `upgrade_stone_tier_1` | หินตีบวกระดับต้น (ใช้ +1 ถึง +15) | Drop: Zombie King (100%) |
| Upgrade Stone Tier 2 | `upgrade_stone_tier_2` | หินตีบวกระดับกลาง (ใช้ I ถึง X) | Drop: Zombie King (50%), Skeleton Lord (100%) |
| Upgrade Stone Tier 3 | `upgrade_stone_tier_3` | หินตีบวกระดับสูง (ใช้ Ultimate) | Drop: Skeleton Lord (30%) |

### 🧪 Alchemy Materials (วัตถุดิบ Alchemy)
| ไอเท็ม | Registry ID | รหัสย่อ | คำอธิบาย |
|--------|-------------|---------|----------|
| Zombie Heart | `zombie_heart` | **H** | หัวใจซอมบี้ (สาย Health/Regen) |
| Bone of Maze | `bone_of_maze` | **B** | กระดูกแห่งเขาวงกต (สาย Defense/Thorns) |
| Cosmic Emerald | `cosmic_emerald` | **C** | มรกตจักรวาล (สาย Evasion/Absorption) |
| Ethernal Bottle | `ethernal_bottle` | — | ขวดนิรันดร์ (ขวดเปล่า Base สำหรับเริ่มทำ Potion) |

### 🧴 Infinite Potions (Potion ใช้ไม่หมด)
| ไอเท็ม | Registry ID | Tier | Cooldown | คำอธิบาย |
|--------|-------------|------|----------|----------|
| Infinite Potion Tier 1 | `infinite_potion_tier_1` | 1 | 8 วินาที | Potion พื้นฐาน (ส่วนผสม 1 ชิ้น) |
| Infinite Potion Tier 2 | `infinite_potion_tier_2` | 2 | 6 วินาที | Potion ผสม (ส่วนผสม 2 ชิ้น + Cleanse) |
| Infinite Potion Tier 3 | `infinite_potion_tier_3` | 3 | 2 วินาที | **The Elixir of Boundless Eternity** (ส่วนผสม 3 ชิ้น + Full Cleanse + Savior) |

---

## 2. 🧱 Blocks (บล็อก)

| บล็อก | Registry ID | Strength | คำอธิบาย | สถานะ |
|-------|-------------|----------|----------|-------|
| Alchemy Table | `alchemy_table` | 2.5 | โต๊ะเล่นแร่แปรธาตุ (ปรุง Potion) | ✅ ใช้งานได้ |
| Ancient Forge Table | `ancient_forge_table` | 3.5 | โต๊ะตีบวกอุปกรณ์ (Upgrade System) | ✅ ใช้งานได้ |
| Tome of Forgotten Table | `tome_of_forgotten_table` | 1.5 | โต๊ะตำราลืมเลือน (ยังไม่มี Logic) | ⚠️ บล็อกเปล่า |

---

## 3. ✨ Mob Effects (เอฟเฟคทั้งหมด)

### Custom Effects ที่ลงทะเบียน:
| Effect | Registry ID | ประเภท | สี | Logic |
|--------|-------------|--------|----|----|
| **Boundless Grace** (The Savior) | `boundless_grace` | Beneficial | 🟡 Gold `#FFD700` | ดูหัวข้อ 4.3 |
| **Juggernaut** | `juggernaut` | Beneficial | 🔴 Dark Red `#8B0000` | +4 Max HP (2 Hearts) |
| **Iron Thorns** | `iron_thorns` | Beneficial | ⚪ Grey `#808080` | 10% โอกาสสะท้อน Damage 200% |
| **Evasion** | `evasion` | Beneficial | 🔵 Cyan `#00FFFF` | 30% โอกาสหลบโจมตีทั้งหมด (Damage = 0) |
| **Unstoppable** | `unstoppable` | Beneficial | 🟠 Orange `#FFFA500` | +100% Knockback Resistance (ไม่ถูกดันถอย) |

---

## 4. ⚙️ ระบบทั้งหมด (Logic จาก Source Code)

### 4.1 🛠️ Ancient Forge (ระบบตีบวก)

**ช่อง UI:** 2 ช่อง (Equipment + Upgrade Stone)  
**ข้อมูลเก็บใน:** `DataComponent` → `rpgem:upgrade_level` (Integer)

#### ระดับการอัพ (3 Tier):

| Tier | หินที่ใช้ | ช่วงเลเวล | อัตราสำเร็จ | แสดงผล | หมายเหตุ |
|------|----------|-----------|------------|--------|---------|
| Tier 1 | `upgrade_stone_tier_1` | +1 ถึง +15 | **70%** (Config) | `+1`, `+2`, ... `+15` | ล้มเหลว: ไม่ลดเลเวล |
| Tier 2 | `upgrade_stone_tier_2` | I ถึง X (16-25) | **40%** (Config) | `I`, `II`, ... `X` | ล้มเหลว: **50% โอกาสลดเลเวล 1** (ไม่ต่ำกว่า +15) |
| Tier 3 | `upgrade_stone_tier_3` | Ultimate 1-3 (26-28) | **10%** (Config) | `Ultimate 1`, `Ultimate 2`, `Ultimate 3` | ล้มเหลว: **50% โอกาสลดเลเวล 1** (ไม่ต่ำกว่า +25) |

**Max Level: 28 (Ultimate 3)**

#### ค่าสถานะที่เพิ่ม (Attribute Modifiers):

| ค่า | สูตร | EquipmentSlot | ตัวอย่าง (+15) | ตัวอย่าง (Ultimate 3 = 28) |
|-----|------|---------------|----------------|---------------------------|
| **Attack Damage** | Lv 1-15: `+1.0/lv` ❘ Lv 16-25: `+2.0/lv` ❘ Lv 26+: `+5.0/lv` | MAINHAND | +15.0 | +15 + 20 + 15 = **+50.0** |
| **Armor** | `+0.5/lv` ทุกระดับ | ARMOR | +7.5 | **+14.0** |

> [!IMPORTANT]
> **คำถามผู้ใช้:** "เพิ่มแค่ Attack Damage / Armor เหมาะสมไหมสำหรับ MMORPG?"
> 
> **คำตอบ:** สำหรับ MVPPhase แรกนี้เพียงพอ แต่สำหรับ MMORPG ที่สมบูรณ์ ควรเพิ่มค่าสถานะเพิ่มเติมในอนาคต เช่น:
> - **Attack Speed** — ความเร็วโจมตี
> - **Movement Speed** — ความเร็วเคลื่อนที่
> - **Max Health** — เลือดสูงสุด
> - **Knockback Resistance** — ต้านทานการดันถอย
> - **Critical Chance (Custom)** — โอกาสคริ
> - **Life Steal (Custom)** — ดูดเลือด
> 
> ตอนนี้โครงสร้าง `ItemAttributeModifierEvent` รองรับการเพิ่มค่าเหล่านี้ได้ทันที (ยกเว้น Custom stat ที่ต้องสร้าง Attribute ใหม่)

---

### 4.2 ⚗️ Alchemy Table (ระบบปรุง Potion)

**ช่อง UI:** 5 ช่อง (1 Input + 3 Ingredient + 1 Output)  
**Process Time:** Tier 1 = 200 ticks (10s) → Tier 2 = 300 ticks (15s) → Tier 3 = 400 ticks (20s)

#### กลไกการทำงาน:
1. ใส่ **Ethernal Bottle** ในช่อง Input + **Ingredient 1 ชิ้น** → ได้ **Infinite Potion Tier 1**
2. ใส่ **Infinite Potion Tier 1** ในช่อง Input + **Ingredient ใหม่ 1 ชิ้น** (ต้องไม่ซ้ำกับที่ใช้ไปแล้ว) → ได้ **Infinite Potion Tier 2**
3. ใส่ **Infinite Potion Tier 2** ในช่อง Input + **Ingredient ใหม่ 1 ชิ้น** (ต้องเป็นตัวสุดท้ายที่ยังไม่ใช้) → ได้ **Infinite Potion Tier 3**

> [!NOTE]
> **อัตราสำเร็จ 100%** — ไม่มี RNG ในการปรุง เพราะ Ingredient แต่ละชิ้นหายากมาก (Drop จาก Mob เฉพาะ)
> 
> ระบบติดตาม **Ingredient History** ผ่าน `CustomData` NBT tag → ป้องกันการใส่ Ingredient ซ้ำ

#### Tier 1 — Potion พื้นฐาน (เลือก 1 จาก 3):

| Ingredient | ชื่อ Potion | เอฟเฟค |
|------------|------------|--------|
| H (Zombie Heart) | Potion of Undying Vitality | Heal 4 Hearts + Regen II (15s) + Saturation |
| B (Bone of Maze) | Potion of Unyielding Structure | Heal 4 Hearts + Resistance I (20s) |
| C (Cosmic Emerald) | Potion of Cosmic Clarity | Heal 4 Hearts + Absorption II (2m) |

#### Tier 2 — Potion ผสม (6 คอมโบที่เป็นไปได้):

| Combo | ชื่อ Potion | เอฟเฟค | Cleanse |
|-------|------------|--------|---------|
| **H→B** | Potion of Armored Vitality | Health Boost V (30s, +20HP) + Juggernaut + Full Heal | Partial (ไม่ลบ Wither/Levitation/Darkness) |
| **H→C** | Potion of Enlightened Pulse | Regen III (10s) | Partial |
| **B→H** | Potion of Living Structure | Resistance II (15s) + **Iron Thorns** (30s) | Partial |
| **B→C** | Potion of Astral Spine | Resistance II (15s) + **Unstoppable** (30s) | Partial |
| **C→H** | Potion of Cosmic Flesh | Speed II (15s) + Invisibility (15s) + **Evasion** (30s) | Partial |
| **C→B** | Potion of Solidified Void | Absorption IV (30s) + Fire Resistance (30s) | Partial |

#### Tier 3 — The Savior (ทุกคอมโบได้ผลเดียวกัน):

| ชื่อ | เอฟเฟค |
|------|--------|
| **The Elixir of Boundless Eternity** 🌟 | Full Heal 100% + **Boundless Grace** (60s) + Regen III (15s) + Resistance II (15s) + Absorption IV (2m) + Fire Resistance (20s) |

**ลักษณะพิเศษ:** มี Enchantment Glint (เรืองแสงรุ้ง), ชื่อสีทอง

---

### 4.3 🛡️ Combat System (ระบบต่อสู้)

#### Evasion (การหลบหลีก):
- **Trigger:** เมื่อ Player มี Effect `evasion` และถูกโจมตี
- **โอกาส:** 30% (Config: `dodgeChance`)
- **ผล:** Damage ถูก cancel ทั้งหมด (= 0)
- **Feedback:** เสียง Ender Dragon Flap + ข้อความ `<< Dodged! >>`

#### Iron Thorns (สะท้อนกลับ):
- **Trigger:** เมื่อ Player มี Effect `iron_thorns` และถูกโจมตีโดย LivingEntity
- **โอกาส:** 10% (Config: `thornsChance`)
- **Damage สะท้อน:** `Original Damage × 2.0` (Config: `reflectionMultiplier`)
- **Feedback:** เสียง Anvil Land + ข้อความ `<< Thorns Reflected! >>`

#### Boundless Grace / The Savior (พระคุณไร้ขอบเขต):

**ส่วนที่ 1 — Divine Reflection:**
- **โอกาสสะท้อน:** 25% (Config: `saviorReflectionChance`) — Override Iron Thorns ถ้าสูงกว่า
- **Feedback:** ข้อความ `<< DIVINE REFLECTION! >>`

**ส่วนที่ 2 — Divine Reaction (Cleanse):**
- เมื่อถูกโจมตีขณะมี Effect → ลบ Effect ทั้งหมด (`removeAllEffects()`)
- ให้ Boundless Grace กลับ (200 ticks) + Regen IV (3s) + Speed II (5s)
- ⚠️ **Bug ที่ทราบ:** `removeAllEffects()` ลบ Beneficial effects ด้วย ต้องแก้ในอนาคต

**ส่วนที่ 3 — Death Prevention (ป้องกันความตาย):**
- เมื่อ Player จะตายขณะมี Effect → **Cancel Death**
- ตั้ง HP เป็น 50% ของ Max HP
- ลบ Effect Boundless Grace (ใช้ได้ครั้งเดียว)
- ให้ Fire Resistance (3s) + Resistance V (3s, แทบไม่รับ Damage)
- Knockback มอนสเตอร์รอบๆ ในรัศมี 5 บล็อก
- **Feedback:** Explosion Particles + เสียง Totem of Undying

---

### 4.4 📡 Networking

| Packet | Type ID | ทิศทาง | คำอธิบาย |
|--------|---------|--------|----------|
| `PacketUpgradeItem` | `rpgem:upgrade_item` | Client → Server | ส่ง BlockPos เพื่อสั่ง Upgrade (มีตรวจระยะ ≤ 8 บล็อก) |

---

### 4.5 📊 Config (ค่าที่ปรับแต่งได้)

| ค่า | Default | พิสัย | คำอธิบาย |
|-----|---------|-------|----------|
| `alchemyTableTickRate` | 10 | 1-100 | ความถี่ตรวจสอบสูตร (ticks) |
| `alchemyTableBaseTime` | 100 | 1-10000 | เวลาพื้นฐานในการปรุง (ticks) |
| `dodgeChance` | 0.3 | 0.0-1.0 | โอกาสหลบ (30%) |
| `thornsChance` | 0.1 | 0.0-1.0 | โอกาสสะท้อน (10%) |
| `reflectionMultiplier` | 2.0 | 0.0-100.0 | ตัวคูณ Damage สะท้อน (200%) |
| `saviorReflectionChance` | 0.25 | 0.0-1.0 | โอกาสสะท้อน Savior (25%) |
| `upgradeSuccessRateTier1` | 0.7 | 0.0-1.0 | อัตราสำเร็จ Tier 1 (70%) |
| `upgradeSuccessRateTier2` | 0.4 | 0.0-1.0 | อัตราสำเร็จ Tier 2 (40%) |
| `upgradeSuccessRateTier3` | 0.1 | 0.0-1.0 | อัตราสำเร็จ Tier 3 (10%) |

---

## 5. 🧟 Mobs (มอนสเตอร์พิเศษ)

### 👑 Zombie King (ราชาซอมบี้)
| ค่า | ตัวเลข |
|-----|--------|
| Registry ID | `rpgem:zombie_king` |
| Base Entity | Zombie (สืบทอดทุก AI) |
| HP | **300** |
| Attack Damage | **12** |
| Movement Speed | 0.25 |
| Knockback Resistance | **50%** |
| Drop 1 | Upgrade Stone Tier 1 (**100%**) |
| Drop 2 | Upgrade Stone Tier 2 (**50%**) |
| Renderer | ใช้ Vanilla ZombieRenderer (ยังไม่มี Custom Texture/Scale) |

### 💀 Skeleton Lord (จ้าวโครงกระดูก)
| ค่า | ตัวเลข |
|-----|--------|
| Registry ID | `rpgem:skeleton_lord` |
| Base Entity | Skeleton (สืบทอดทุก AI + ธนู) |
| HP | **250** |
| Attack Damage | **15** (Melee) |
| Movement Speed | **0.3** (เร็วกว่าปกติ) |
| Drop 1 | Upgrade Stone Tier 2 (**100%**) |
| Drop 2 | Upgrade Stone Tier 3 (**30%**) |
| Renderer | ใช้ Vanilla SkeletonRenderer (ยังไม่มี Custom Texture/Scale) |

> [!NOTE]
> **Looting Enchantment:** ยังไม่ทำงาน (Hardcoded `looting = 0`) เนื่องจาก API ใน 1.21 เปลี่ยนแปลง ต้องใช้วิธีใหม่ในการดึงค่า Looting

---

## 6. 🌍 Spawning Conditions (เงื่อนไขการเกิดม็อบ)

> [!CAUTION]
> **ยังไม่มี Spawn Rules เลย**
> 
> ปัจจุบัน Zombie King และ Skeleton Lord **ไม่เกิดเองตามธรรมชาติ** สามารถเรียกได้จากคำสั่งเท่านั้น:
> ```
> /summon rpgem:zombie_king
> /summon rpgem:skeleton_lord
> ```

### แผนที่วางไว้:
- **Biome:** ยังไม่กำหนด (แนะนำ: Plains ทั่วไป, Deep Dark สำหรับ Skeleton Lord)
- **Condition:** ยังไม่กำหนด (แนะนำ: Spawn Weight ต่ำมาก, กลางคืนเท่านั้น)
- **วิธีทำ:** ใช้ NeoForge Biome Modifiers (JSON Datapack)

---

## 7. 🚧 ระบบที่ยังขาด / ต้องปรับปรุง

| ลำดับ | ระบบ | รายละเอียด | ความสำคัญ |
|-------|------|-----------|----------|
| 1 | **Spawn Rules** | Mob ไม่เกิดเองในโลก ต้องทำ Biome Modifiers | 🔴 สูง |
| 2 | **Looting Enchantment** | ยังไม่ทำงานกับ Special Mob drops (hardcoded = 0) | 🟡 กลาง |
| 3 | **เพิ่ม Stat ที่อัพได้** | ตอนนี้มีแค่ Attack Damage + Armor → ควรเพิ่ม Attack Speed, Max Health, Speed | 🟡 กลาง |
| 4 | **Tome of Forgotten Table** | บล็อกลงทะเบียนแล้วแต่ไม่มี Logic/GUI ใดๆ | 🟡 กลาง |
| 5 | **Custom Mob Renderer** | Boss ดูเหมือน Zombie/Skeleton ปกติ ยังไม่มี Scale/Texture พิเศษ | 🟡 กลาง |
| 6 | **Savior Cleanse Bug** | `removeAllEffects()` ลบทุก Effect รวมถึง Beneficial → ควรลบเฉพาะ Harmful | 🟠 ปานกลาง |
| 7 | **Life Steal** | ยังไม่ได้ทำ (อยู่ใน Roadmap Phase 2) | 🟡 กลาง |
| 8 | **GUI Animations** | ไม่มี Particle/Animation ตอน Crafting/Upgrade เสร็จ | 🟢 ต่ำ |
| 9 | **Potion Texture ขาด** | Infinite Potion ทั้ง 3 Tier ไม่มี Item Model ใน assets | 🟡 กลาง |
| 10 | **Player Stats UI** | ไม่มีหน้าจอแสดงค่าสถานะรวมของผู้เล่น | 🟢 ต่ำ (Phase 4) |

---

## 8. 🎨 Textures ที่ต้องทำ

### ✅ มีแล้ว (มี Item Model JSON):
| ไอเท็ม | ไฟล์ Model |
|--------|-----------|
| Alchemy Table (Block + Item) | ✅ |
| Ancient Forge Table (Block + Item) | ✅ |
| Tome of Forgotten Table (Block + Item) | ✅ |
| Bone of Maze | ✅ |
| Cosmic Emerald | ✅ |
| Ethernal Bottle | ✅ |
| Zombie Heart | ✅ |
| Upgrade Stone Tier 1 | ✅ |
| Upgrade Stone Tier 2 | ✅ |
| Upgrade Stone Tier 3 | ✅ |

### ❌ ขาด (ไม่มี Item Model JSON):
| ไอเท็ม/Entity | ที่ต้องทำ | หมายเหตุ |
|---------------|----------|---------|
| **Infinite Potion Tier 1** | `models/item/infinite_potion_tier_1.json` + Texture | ยังไม่มี model json |
| **Infinite Potion Tier 2** | `models/item/infinite_potion_tier_2.json` + Texture | ยังไม่มี model json |
| **Infinite Potion Tier 3** | `models/item/infinite_potion_tier_3.json` + Texture | ยังไม่มี model json |
| **Zombie King** | `textures/entity/zombie_king.png` | ต้องออกแบบ Skin (ใหญ่ + มงกุฎ + เกราะทอง) |
| **Skeleton Lord** | `textures/entity/skeleton_lord.png` | ต้องออกแบบ Skin (ผ้าคลุม + เกราะดำ) |
| **GUI: Ancient Forge** | ปรับปรุง GUI Texture ให้สวยงาม | มีอยู่แล้วแต่อาจต้องปรับปรุง |
| **GUI: Alchemy Table** | ปรับปรุง GUI Texture ให้สวยงาม | มีอยู่แล้วแต่อาจต้องปรับปรุง |
| **Effect Icons (5 ตัว)** | `textures/mob_effect/*.png` | Icon สำหรับ Boundless Grace, Juggernaut, Iron Thorns, Evasion, Unstoppable |

---

## 9. 📁 โครงสร้างไฟล์ Source Code

```
src/main/java/net/kankrittapon/rpgem/
├── RPGEasyMode.java          # Main Mod Class
├── RPGEasyModeClient.java    # Client Setup (Screens, Renderers)
├── Config.java               # Config ค่าต่างๆ ทั้งหมด
├── block/
│   ├── AlchemyTableBlock.java
│   ├── AncientForgeBlock.java
│   └── entity/
│       ├── AlchemyTableBlockEntity.java  # Logic ปรุง Potion ทั้งหมด
│       └── AncientForgeBlockEntity.java  # Logic ตีบวกทั้งหมด
├── entity/custom/
│   ├── ZombieKing.java        # Boss Zombie (300 HP)
│   └── SkeletonLord.java      # Boss Skeleton (250 HP)
├── event/
│   ├── ModEvents.java         # Combat Logic + Attribute Modifiers + Mob Drops
│   └── ModEventBusEvents.java # Entity Attribute Registration
├── init/
│   ├── ModBlocks.java         # Register Blocks (3)
│   ├── ModBlockEntities.java  # Register Block Entities (2)
│   ├── ModDataComponents.java # Register DataComponent (upgrade_level)
│   ├── ModEntities.java       # Register Entities (2)
│   ├── ModItems.java          # Register Items (10)
│   ├── ModMenuTypes.java      # Register Menus
│   └── ModMobEffects.java     # Register Effects (5)
├── item/
│   └── SequentialInfinitePotion.java  # Potion Logic ทั้งหมด
├── menu/
│   ├── AlchemyTableMenu.java
│   └── AncientForgeMenu.java
├── network/
│   └── PacketUpgradeItem.java  # Client→Server Upgrade Packet
└── screen/
    ├── AlchemyTableScreen.java
    └── AncientForgeScreen.java
```
