# Roadmap: RPGEasyMode (Antigravity RPG)

> **อัปเดตล่าสุด:** 15 ก.พ. 2026 (15:20 ICT)  
> **Platform:** NeoForge 1.21.1  
> แผนพัฒนาทั้งหมดของ Mod — จัดรวมจาก Source Code + NotebookLM Research + แผนผู้พัฒนา + Trait Counter System Design  
> **Mod Ecosystem:** ทำงานร่วมกับ **Apotheosis + L2 Hostility + L2 Complements + L2 Library**

---

## 🟢 Phase 1: Core Foundation ✅

> _เสร็จสมบูรณ์ — GUI, Networking, Block/Item Registration_

- [x] **Alchemy Table:** Block + BlockEntity + Menu + Screen
- [x] **Ancient Forge Table:** Block + BlockEntity + Menu + Screen
- [x] **Networking:** `PacketUpgradeItem` (Client → Server)
- [x] **Item Registration:** Upgrade Stones (3 Tier), Alchemy Materials (4), Ethernal Bottle
- [x] **Block Registration:** Alchemy Table, Ancient Forge, Tome of Forgotten
- [ ] **Visuals:** Particle/Animation ตอน Crafting เสร็จ (Deferred)

---

## ✅ Phase 2: Combat & Crafting Systems (เสร็จสมบูรณ์)

> _เสร็จสมบูรณ์ 100%_

### ⚗️ Infinite Potion System ✅

- [x] ระบบ Sequential Crafting 3 Tier (Ethernal Bottle → T1 → T2 → T3)
- [x] Ingredient History Tracking (ป้องกันซ้ำ)
- [x] Tier 1: เลือก 1 จาก 3 (H/B/C) — Heal + Buff พื้นฐาน
- [x] Tier 2: 6 Combo (HB/HC/BH/BC/CH/CB) — Combat Buffs + Partial Cleanse
- [x] Tier 3: "The Elixir of Boundless Eternity" — Full Heal + Savior
- [x] **Heal/Buff Separation** — Heal = Instant, Buff = Cooldown แยกตาม Tier
- [x] **LastBuffTime NBT Tracking** — ระบบ Cooldown เฉพาะส่วน Buff

### ⚔️ Combat Effects ✅

- [x] **Evasion (30%):** หลบดาเมจสมบูรณ์ (Stack กับ `apothic_attributes:dodge_chance`)
- [x] **Iron Thorns (10%):** สะท้อนดาเมจ 200%
- [x] **Juggernaut:** +4 Max HP
- [x] **Unstoppable:** +100% Knockback Resistance
- [x] **Boundless Grace (The Savior):** Divine Reflection + Cleanse + Death Prevention

### 🛠️ Upgrade System ✅

- [x] 3-Tier Upgrade Stone (70% / 40% / 10% success rate)
- [x] Attribute Modifiers: Attack Damage + Armor
- [x] Downgrade on Failure (Tier 2/3)
- [x] **Forged Stone Crafting** — ระบบหลอมหิน (Zombie Heart, Bone of Maze, Cosmic Emerald)
- [x] **Weapon Upgrade Path** — ATK + Life Steal → Crit → Element Damage
- [x] **Armor Path System** — เลือกสายตอน +6:
  - 🧱 **Damage Reduction** (DR cap 80%, Reflect/Seal Resist)
  - 💨 **Damage Evasion** (EVA cap 50%, Seal Resist)
- [x] **Bug Fix: Savior Cleanse** — ลบเฉพาะ Harmful Effects แล้ว

### ⚗️ Boundless Grace V2 ✅

- [x] **Savior Aura V2** — Evasion↑, Reflect Shield (80%), Seal Ward
- [x] **Use CD ≠ Effect CD** — กด = Heal Instant เสมอ, Buff ได้เฉพาะตอน Cooldown หมด
- [x] **PlayerTickEvent.Post** — แก้ไขให้รองรับ NeoForge 1.21.1 Events ใหม่

---

## ✅ Phase 3: Item Drop & Loot System (เสร็จสมบูรณ์)

> _เสร็จสมบูรณ์ — Code & Datagen พร้อม_

### 🎯 แผนงาน (Finalized)

- [x] **Core System:** ใช้ **Global Loot Modifiers** (Modern NeoForge Standard)
- [x] **Loot Tables:**
  - **Zombie Heart:** Drop จาก Zombies (0.05%)
  - **Bone of Maze:** Drop จาก Skeletons (0.05%)
  - **Cosmic Emerald:** Drop จาก Endermen / Witches (0.05%)
  - **Ethernal Bottle:** Drop จาก Witches (0.1%)
- [x] **Upgrade Stones:**
  - **Tier 1:** Drop จาก Mob ทั่วไป (Rare ~0.1% + Scaling)
  - **Tier 2:** Drop จาก Bosses (Placeholder Logic)
  - **Tier 3:** Drop จาก Bosses (Placeholder Logic)
- [x] **Config:** ปรับ Rate ได้ผ่าน JSON Datapack
- [x] **Apocalypse Events:**
  - **Zombie Horde:** 5% โอกาสเกิดเมื่อ Zombie ตาย (Spawn 10 Zombies)
  - **Skeleton Rider:** 5% โอกาสเกิดเมื่อ Skeleton ตาย (Spawn Skeleton Horseman)
  - **Enderman Swarm:** 5% โอกาสเกิดเมื่อ Enderman ตาย (Spawn 5 Endermen)
- [x] **Villager Trade:**
  - **Master Cleric:** แลก Ethernal Bottle ด้วย 32 Emerald Blocks + 10 Diamond Blocks

---

## 🔴 Phase 4: Familia System (ระบบภูติ/นางฟ้าคู่หู) 🚧

> _กำลังดำเนินงาน — พัฒนาตามต้นฉบับ Black Desert Online_

### 🧚 4.1 Core Entity & Data (โครงสร้างพื้นฐาน)

- [x] **Entity Registration:** `rpgem:fairy` (Flying Mob)
- [x] **Owner Linking:** Tameable Entity (Owner UUID)
- [x] **Data Persistence:** เก็บ Level, Tier, XP, Skills ใน NBT
- [ ] **AI Behavior:** บินตามผู้เล่น, ไม่โดนตี, เก็บไอเทม (To Do)

### 🖥️ 4.2 GUI & Interaction (หน้าต่างสั่งการ) 🚧

- [x] **Menu & Screen:** `FairyMenu` + `FairyScreen`
- [x] **Packet System:** `PacketOpenFairyGUI` (Server -> Client)
- [ ] **BDO Style Layout:**
  - [x] Wireframe Implementation (Placeholder Graphics)
  - [ ] **Texture Implementation** (รอไฟล์ .png จากผู้ใช้)
  - [ ] **Button Logic:**
    - `Sprout` (เลื่อนขั้น)
    - `Growth` (กินของเพิ่ม XP)
    - `Setup Potion` (ตั้งค่า Auto-Potion)
    - `Change Skill` (เปลี่ยนสกิล)
    - `Release` (ปล่อย/ย่อย)
    - `Revive` (ชุบชีวิต)
    - `Unsummon` (เก็บเข้ากระเป๋า)

### 🧪 4.3 Items & Consumables (ไอเทมเลี้ยงดู)

- [ ] **Sweet Honey Wine:** อาหารเพิ่ม XP (Craftable / Shop)
- [ ] **Laila's Petal:** ไอเทมแลก Fairy (Drop from Boss/Gathering)
- [ ] **sealed_fairy_wings:** Fairy ในรูปไอเทม (สุ่ม Tier เมื่อเปิด)
- [ ] **Theiah's Orb:** ไอเทมเปลี่ยนสกิล (Shop Item)

### ✨ 4.4 Skills Logic (ระบบสกิล)

- [ ] **Luck +1:** Passive (Base Skill)
- [ ] **Feathery Steps:** ลดน้ำหนัก/กัน Slow (คำนวณ Weight Logic)
- [ ] **Miraculous Cheer (Auto-Potion):**
  - [ ] GUI ตั้งค่า % HP/MP
  - [ ] Auto-Consume Logic
- [ ] **Fairy's Tear:** ชุบชีวิตฟรี (Cooldown Tracking)
- [ ] **Morning Star:** Dynamic Light (Client-side)
- [ ] **Tingling Breath:** Underwater Breathing

### 🔄 4.5 Sprouting System (ระบบวิวัฒนาการ)

- [ ] **Leveling Logic:** XP from Items (Equipment/Honey Wine)
- [ ] **Sprout Rate:** คำนวณ % สำเร็จตามไอเทมที่ใช้จ่าย
- [ ] **Tier Up:** อัปเกรด Tier (1->2->3->4) และ Reset Level/Skills

---

### Phase 5: Mod Ecosystem Integration & Balancing (2026)

- **Status:** [x] Completed
- **Progress:** 100% (Integration Code & Datagen ready)

### 🎯 ปรัชญาการ Integrate

> **หลักสำคัญ:** Antigravity เป็น Mod **เสริม** — ไม่สร้างระบบ Level/Affix/Armor เอง แต่ออกแบบให้ทำงานร่วมกับ Mod ที่มีอยู่แล้ว

| Mod                | หน้าที่หลัก                                            | Antigravity ต้องปรับอะไร                             |
| ------------------ | ------------------------------------------------------ | ---------------------------------------------------- |
| **Apotheosis**     | Boss/Elite สุ่มเกิด + Affix ติดไอเทม + ปลด Enchant Cap | ❗ ระวัง Attribute ซ้อนกับ Upgrade System            |
| **L2 Hostility**   | Mob Level + 37 Traits + Scaling Difficulty             | ❗ ไม่ต้องสร้างระบบ Level เอง                        |
| **L2 Complements** | Endgame Armor (Sculkium/Eternium) + Enchants ใหม่      | ❗ Balance: ไม่ให้ Upgrade แรงกว่า Eternium เร็วเกิน |
| **L2 Library**     | Player Attribute Tab + Curios 54 Slots                 | ✅ ใช้ Curios สำหรับ Fairy Slot                      |

### ⚔️ Apotheosis Integration

- [x] ✅ **Affix System ทำงาน** — mob drop equipment มี affixes แล้ว
- [x] ✅ **Apothic Dodge Integration** — `apothic_attributes:dodge_chance` stack กับ `rpgem:evasion`
- [x] **Boss/Elite ที่ Apotheosis สุ่มเกิด** → ดรอปของจาก Antigravity แล้ว (ผ่าน `ScaledItemModifier`)
- [ ] Enchant Cap ที่ Apotheosis ปลด → Potion effects ไม่ควร OP เกินเมื่อรวมกับ Enchant สูง (กำลังจูน)
- [ ] เพิ่ม Custom Affix สำหรับ RPGEasyMode items

### 📏 L2 Hostility Integration (ระบบ Level & Traits)

> **แนวทาง:** ไม่สร้างระบบ Level เอง → ใช้ L2 Hostility เป็นหลัก

#### Traits ที่ต้อง Aware (37 Traits, 3 ระดับ):

| ระดับ                  | Traits สำคัญ                                                                        | ผลกระทบต่อ Antigravity                  |
| ---------------------- | ----------------------------------------------------------------------------------- | --------------------------------------- |
| **Regular**            | Fiery, Speedy, Tank                                                                 | Evasion/Iron Thorns ยังใช้ได้ปกติ       |
| **Advanced (Lv.100+)** | **Adaptive** (กัน damage ประเภทที่โดนซ้ำ), **Reflect** (สะท้อน physical)            | ⚠️ Reflect อาจ conflict กับ Iron Thorns |
| **Legendary**          | **Undying** (ฟื้นเรื่อยๆ), **Dementor** (กัน physical), **Ragnarok** (seal อุปกรณ์) | ⚠️ Savior อาจไม่พอรับมือ → ต้อง balance |

- [x] วิเคราะห์ API ของ L2 Hostility → อ่าน Mob Level จาก NBT (`hostility:level`)
- [x] กำหนด Level ขั้นต่ำให้ Zombie King / Skeleton Lord (ผ่าน Biome Spawner rules)
- [x] ปรับ Drop Rate ตาม Mob Level → รองรับใน `ScaledItemModifier` แล้ว
- [ ] Config hooks สำหรับเปิด/ปิด L2H integration

### 🛡️ Trait Counter Integration 🆕

> **หลักการ:** Potion T3 = counter ชั่วคราว (60s), Forge = counter ถาวร (passive)

- [x] ตรวจสอบ L2H Trait events → รองรับผ่าน NBT Data ใน `ModEvents`
- [x] Iron Thorns vs Reflect Trait → DamageSource filtering ป้องกัน loop แล้ว
- [x] **Weapon Specializations (NEW):**
  - [x] **Accuracy:** หักล้างค่า Evasion ของเป้าหมาย
  - [x] **Armor Penetration:** ดาเมจทะลุเกราะตาม % HP สูงสุด
  - [x] **Anti-Heal (Soul Purge):** ลดการฟื้นฟูเลือดมอนสเตอร์ (แก้ทาง Undying trait)
  - [x] **Configurable Fate Seal:** Chance-based kill mechanic (แก้ทาง Undying Trait ขั้นสูง)
- [x] **God of Element (NEW):** Permanent 5 Elements + 80% Evasion + Instant Kill (Judgement)
- [x] Seal Ward → ป้องกัน Ragnarok equipment seal (Attribute `rpgem:seal_resist`)

### 🛡️ L2 Complements Balancing

> **กฎสำคัญ:** Ancient Forge ต้องไม่ทำให้ไอเทม OP เกิน Endgame ของ L2C

| วัสดุ L2C        | ความแรง                                 | Antigravity ต้องทำ                            |
| ---------------- | --------------------------------------- | --------------------------------------------- |
| **Sculkium**     | > Netherite (HP + ATK สูงเหมือน Warden) | Upgrade ระดับ Tier 2 ควรเท่า Sculkium         |
| **Eternium**     | Infinite Durability                     | Upgrade Ultimate ควรมี bonus เทียบเท่า        |
| **Totemic Gold** | Auto-Heal + กัน Wither/Poison           | ไม่ควรซ้อนกับ Savior (เลือกอย่างใดอย่างหนึ่ง) |

- [x] ตรวจสอบ Attribute ของ L2C armors → ปรับสเกล Ancient Forge ให้สูสีแล้ว
- [ ] Enchants ใหม่ (Void Touch, Life Mending, Hardened) → ตรวจสอบ Compatibility
- [x] ออกแบบ "Upgrade Ceiling" ให้สอดคล้องกับ power curve ของ L2C (Lv.30 Max)

### 🌍 Natural Spawning (Biome Modifiers)

- [x] สร้าง JSON Biome Modifiers สำหรับ Zombie King / Skeleton Lord ([spawn_zombie_king.json](file:///c:/Users/zexqm/programing/neoforge-bokkchoy/src/main/resources/data/rpgem/neoforge/biome_modifier/spawn_zombie_king.json))
- [x] กำหนด Spawn Weight (2), Min/Max Count (1), Overworld Biomes
- [ ] ใช้ `RegisterSpawnPlacementsEvent` กำหนดกฎ (ON_GROUND, กลางคืน)

### 🛡️ Mod Compatibility ทั่วไป

- [x] **JEI (Just Enough Items):** แสดงสูตร Alchemy Table + Ancient Forge
- [ ] **Curios API:** รองรับ Fairy ผ่าน Curios Slot (ใช้ L2 Library's expanded slots)
- [x] **WAILA/Jade:** แสดงข้อมูล Block Entities ใน Tooltip

---

## 🔵 Phase 6: Special Mobs & NPCs 🔲

> _ยังไม่เริ่ม — มีโครงร่างแล้ว_

### 🧟 Special Mobs (มอนสเตอร์พิเศษ)

- [ ] ออกแบบ Boss Mob เพิ่มเติม (นอกจาก Zombie King / Skeleton Lord)
- [ ] สร้าง Custom Model + Animation (ใช้ Blockbench)
- [ ] ออกแบบ AI Goals เฉพาะ (Phase Attack, Summon Minions, Area Denial)
- [ ] Boss Arena / Spawn Conditions พิเศษ

### 👤 Special NPCs (NPC พิเศษ)

- [ ] สร้าง **Custom Villager Profession** ผ่าน NeoForge
  - ลงทะเบียน POI (Point of Interest) ผูกกับ Block เฉพาะ
  - ลงทะเบียน Profession ที่เชื่อม POI
- [ ] ออกแบบ **Trade System** ผ่าน `VillagerTradesEvent`
  - กำหนด Cost / Result / Max Uses / XP per Level
- [ ] NPC ที่อาจทำ:
  - **Alchemist NPC:** ขาย Alchemy Materials / สอนสูตร
  - **Blacksmith NPC:** ขาย Upgrade Stones / ให้บริการ Upgrade
  - **Fairy Keeper NPC:** ขาย Theiah's Orb / Honey Wine / บริการ Sprout

---

## 🔵 Phase 8: RPG Core Systems (กำลังดำเนินงาน)

> _ระบบพื้นฐาน RPG: UI และ Attribute Display_

- [x] **Player Stats Overlay (HUD):** แสดงค่าสถานะพื้นฐานบนหน้าจอ (Configurable, Default OFF) - _Implemented_
- [x] **Character Status GUI:** หน้าต่างกดปุ่ม 'K' เพื่อดูค่าสถานะละเอียด (Evasion, Armor Pen, etc.) - _Implemented_
- [x] **Reforger System Refactor:**
  - **Durability:** Logic การพัง (Broken State) และการซ่อม (Memory Fragment) - _Implemented_
  - **Fail Stack:** ระบบสะสมความล้มเหลวเพื่อเพิ่มโอกาสติด (1% per Stack) - _Implemented_
  - **Visuals:** ชื่อไอเท็มเปลี่ยนตาม Tier (`[+1]`, `[PRI]`, `[PEN]`) - _Implemented_
  - **Protection:** Protection Stone & Artisan's Memory - _Implemented_
- [x] **Infinite Potion Quest:**
  - **System:** Grind-based Quest หา Fragments เพื่อแลก Potion ถาวร - _Implemented_
  - **Key Item:** `Eternal Bottle` (Required to start quest)
  - **Tiers:** T1 (Heart), T2 (Bone), T3 (Cosmic Emerald)

---

## 📊 สรุปความสำเร็จ

| Phase                                  | สถานะ          | ความคืบหน้า                                    |
| -------------------------------------- | -------------- | ---------------------------------------------- |
| Phase 1: Core Foundation               | ✅ เสร็จ       | 100%                                           |
| Phase 2: Combat & Crafting             | ✅ เสร็จ       | 100% (Code + Compilation ผ่าน)                 |
| Phase 3: Item Drop & Loot              | ✅ เสร็จ       | 100% (Code & Datagen พร้อม)                    |
| Phase 4: Familia System                | 🔲 ยังไม่เริ่ม | 0% (มี Research แล้ว)                          |
| Phase 5: Mod Ecosystem (Apotheosis+L2) | ✅ เสร็จ       | 100% (Basic Integration ✅, Core balancing ✅) |
| Phase 6: Special Mobs & NPCs           | 🔲 ยังไม่เริ่ม | 0% (มีโครงร่าง)                                |
| Phase 7: Spawn Rules (Cleanup)         | ✅ เสร็จ       | 100% (Implemented Biome Modifiers)             |
| Phase 8: RPG Core Systems              | 🔵 กำลังทำ     | 95% (HUD Configurable, Attributes Exposed)     |

---

## 📦 Mod Dependencies สำหรับ Testing (NeoForge 1.21.1)

### กลุ่ม Apotheosis (5 mods)

| Mod                    | เวอร์ชัน          | ประเภท             | หมายเหตุ                 |
| ---------------------- | ----------------- | ------------------ | ------------------------ |
| **Placebo**            | 9.9.1             | Required (Library) | Core library ของ Shadows |
| **Apothic Attributes** | Latest for 1.21.1 | Required           | Attribute system         |
| **Apothic Enchanting** | Latest for 1.21.1 | Required           | ระบบ Enchant ใหม่        |
| **Apothic Spawners**   | Latest for 1.21.1 | Required           | จัดการ Spawner           |
| **Apotheosis**         | 8.4.2             | Main Mod           | Boss/Elite + Affix       |

### กลุ่ม L2 Series (4 mods)

| Mod                | เวอร์ชัน          | ประเภท             | หมายเหตุ                 |
| ------------------ | ----------------- | ------------------ | ------------------------ |
| **L2 Library**     | Latest for 1.21.1 | Required (Library) | Core library ของ L2      |
| **L2 Complements** | 3.1.2             | Required           | Endgame Armor/Items      |
| **L2 Hostility**   | Latest for 1.21.1 | Main Mod           | Mob Level + 37 Traits    |
| **Curios API**     | Latest for 1.21.1 | Required           | Extended equipment slots |

### แนะนำเพิ่มเติม (Optional)

| Mod           | ประเภท   | หมายเหตุ                         |
| ------------- | -------- | -------------------------------- |
| **Patchouli** | Optional | Guidebook สำหรับ Apotheosis + L2 |
| **JEI**       | Optional | ดูสูตรคราฟ                       |
| **Jade**      | Optional | ดูข้อมูล Block/Entity            |

> ⚠️ **ข้อระวัง:** ต้องดาวน์โหลด Mod ทุกตัวเป็นเวอร์ชัน **NeoForge 1.21.1** เท่านั้น (ไม่ใช่ Forge หรือ Fabric)

---

_หมายเหตุ: ลำดับ Phase อาจปรับเปลี่ยนได้ตามความเหมาะสม ข้อมูล Familia อ้างอิงจากระบบ Fairy ของ Black Desert Online ปรับให้เข้ากับ Minecraft_
