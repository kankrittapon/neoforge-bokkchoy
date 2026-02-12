# Roadmap: RPGEasyMode (Antigravity RPG)

> **อัปเดตล่าสุด:** 12 ก.พ. 2026  
> **Platform:** NeoForge 1.21.1  
> แผนพัฒนาทั้งหมดของ Mod — จัดรวมจาก Source Code + NotebookLM Research + แผนผู้พัฒนา

---

## 🟢 Phase 1: Core Foundation ✅
> *เสร็จสมบูรณ์ — GUI, Networking, Block/Item Registration*

- [x] **Alchemy Table:** Block + BlockEntity + Menu + Screen
- [x] **Ancient Forge Table:** Block + BlockEntity + Menu + Screen
- [x] **Networking:** `PacketUpgradeItem` (Client → Server)
- [x] **Item Registration:** Upgrade Stones (3 Tier), Alchemy Materials (4), Ethernal Bottle
- [x] **Block Registration:** Alchemy Table, Ancient Forge, Tome of Forgotten
- [ ] **Visuals:** Particle/Animation ตอน Crafting เสร็จ (Deferred)

---

## 🟡 Phase 2: Combat & Crafting Systems ✅ (เกือบเสร็จ)
> *ระบบ Potion, Upgrade, Boss Mob ทำเสร็จแล้ว — เหลือ Bug Fixes*

### ⚗️ Infinite Potion System ✅
- [x] ระบบ Sequential Crafting 3 Tier (Ethernal Bottle → T1 → T2 → T3)
- [x] Ingredient History Tracking (ป้องกันซ้ำ)
- [x] Tier 1: เลือก 1 จาก 3 (H/B/C) — Heal + Buff พื้นฐาน
- [x] Tier 2: 6 Combo (HB/HC/BH/BC/CH/CB) — Combat Buffs + Partial Cleanse
- [x] Tier 3: "The Elixir of Boundless Eternity" — Full Heal + Savior

### ⚔️ Combat Effects ✅
- [x] **Evasion (30%):** หลบดาเมจสมบูรณ์
- [x] **Iron Thorns (10%):** สะท้อนดาเมจ 200%
- [x] **Juggernaut:** +4 Max HP
- [x] **Unstoppable:** +100% Knockback Resistance
- [x] **Boundless Grace (The Savior):** Divine Reflection + Cleanse + Death Prevention

### 🛠️ Upgrade System ✅ (ขาดบางส่วน)
- [x] 3-Tier Upgrade Stone (70% / 40% / 10% success rate)
- [x] Attribute Modifiers: Attack Damage + Armor
- [x] Downgrade on Failure (Tier 2/3)
- [ ] ❌ **ขาด: เพิ่ม Stat อื่น** (Attack Speed, Max Health, Movement Speed, Crit Chance)
- [ ] ❌ **ขาด: Life Steal effect**
- [ ] ⚠️ **Bug: Savior Cleanse** ลบทุก Effect (ควรลบเฉพาะ Harmful)

### 🧟 Special Mobs ✅ (ขาด Spawning + Visuals)
- [x] **Zombie King** (300HP, 12 ATK) — Drop: Stone T1 (100%), T2 (50%)
- [x] **Skeleton Lord** (250HP, 15 ATK) — Drop: Stone T2 (100%), T3 (30%)
- [ ] ❌ **ขาด: Custom Renderer** (Scale ใหญ่ + Texture พิเศษ)
- [ ] ❌ **ขาด: Looting Enchantment** (hardcoded = 0)
- [ ] ❌ **ขาด: Texture** — Infinite Potion (3), Effect Icons (5), Boss Skins (2)

---

## 🟠 Phase 3: Item Drop & Loot System 🔲
> *ยังไม่เริ่ม — ต้องออกแบบ Logic ก่อน*

### วิธีที่เป็นไปได้ (จาก NotebookLM Research):

#### Option A: Global Loot Modifiers (แนะนำ)
- ใช้ NeoForge `LootModifier` + `LootItemCondition`
- Data-driven ผ่าน JSON Datapack → ปรับแต่งง่าย
- เพิ่มของพิเศษให้ Mob ที่มีอยู่แล้ว (Creeper, Zombie, etc.)
- **เหมาะสำหรับ:** เพิ่ม Alchemy Materials ให้ Drop จาก Vanilla Mobs

#### Option B: LivingDropsEvent (ใช้อยู่ตอนนี้)
- Hardcode ใน Java → ยืดหยุ่นน้อย
- ควบคุมได้ละเอียด (เช็ค Biome, เวลา, ระยะจาก Spawner)
- **เหมาะสำหรับ:** Boss Mob drops ที่ต้องการ Logic ซับซ้อน

#### สิ่งที่ต้องออกแบบ:
- [ ] กำหนดว่า Alchemy Materials (H/B/C) ดรอปจาก Mob ไหน
- [ ] กำหนดอัตราดรอป (Drop Rate %)
- [ ] ย้ายจาก `LivingDropsEvent` → `Global Loot Modifiers` (Data-driven)
- [ ] เพิ่ม Looting Enchantment support
- [ ] สร้าง `LootTableProvider` สำหรับ Block Loot Tables

---

## 🔴 Phase 4: Familia System (ระบบภูติ/นางฟ้าคู่หู) 🔲
> *ยังไม่เริ่ม — มี Research จาก NotebookLM (อ้างอิง BDO)*

### 🧚 คอนเซปต์หลัก
Fairy Companion = Entity ตัวเล็กบินตามผู้เล่น มี **Passive Skills** + ระบบเลเวล + เลื่อนขั้น (Tier)

### 📊 ระบบ Tier & Level

| Tier | ชื่อ | Max Level | สกิลสูงสุด | หมายเหตุ |
|------|------|-----------|-----------|---------|
| 1 | เลือนราง | 10 | Lv.I | เริ่มต้น |
| 2 | แจ่มใส | 20 | Lv.III | ต้อง Sprout |
| 3 | เจิดจรัส | 30 | Lv.IV | ต้อง Sprout |
| 4 | โชตช่วง | 40 | Lv.V | Max Tier |

### ✨ สกิลทั้งหมด (8 สกิล)

| สกิล | คำอธิบาย | ได้จาก |
|------|----------|--------|
| **โชคลาภ (Gift)** | Luck +1 ตลอด | Base Skill (มีทุกตัว) |
| **ดาวจรัสแสง (Morning Star)** | Dynamic Light รอบตัว (เปิด/ปิดได้) | สุ่ม (ทุก 10 Level) |
| **ลมหายใจแผ่วบาง (Tingling Breath)** | +5 ถึง +30 วินาที หายใจใต้น้ำ | สุ่ม |
| **ย่างก้าวเบาราวขนนก (Feathery Steps)** | ลด Overweight Penalty (แบกของเกิน 100% ไม่ช้า) | สุ่ม |
| **น้ำตานางฟ้า (Fairy's Tear)** | ชุบชีวิตอัตโนมัติ ไม่เสีย EXP/ของ (CD 1-12 ชม.) | สุ่ม |
| **แรงสนับสนุนลึกลับ (Miraculous Cheer)** | ⭐ **Auto-Potion** ใช้ HP/MP Potion อัตโนมัติตาม % | สุ่ม |
| **บ่อน้ำชุ่มฉ่ำ (Inexhaustible Well)** | Auto ใช้ไอเทมแก้ Debuff (โรค/สถานะผิดปกติ) | สุ่ม |
| **ฝ่ามือไพศาล (Continuous Care)** | ⭐ **Auto-Buff** กดไอเทมบัฟอัตโนมัติเมื่อบัฟหมด | สุ่ม |

### 🔄 ระบบอัปเลเวล & วิวัฒนาการ
- **กินอุปกรณ์/ไวน์น้ำผึ้ง** → เพิ่ม EXP (Resource Sink)
- **Sprouting (เลื่อนขั้น):** Level ตัน → วิวัฒนาการ Tier ถัดไป
  - ❗ **ความเสี่ยง:** ไม่ 100% สำเร็จ ถ้าล้มเหลว → ภูติตัวนั้นเลื่อนขั้นไม่ได้อีก **ตลอดไป**
  - สำเร็จ → Tier +1, Level reset เป็น 1, สกิลรีเซ็ต (สุ่มใหม่)
- **Re-skill:** ใช้ "ลูกแก้วของเธอาห์ (Theiah's Orb)" สุ่มเปลี่ยนสกิล 1 ตัว (ค่าใช้จ่ายเพิ่มตามจำนวนสกิล)
- **Reset Growth:** ใช้ "อำนาจราชินีนางฟ้า" รีเซ็ตเลเวลกลับเป็น 1 (ปั้นใหม่)

### 🔧 Implementation Guide (NeoForge)
- **Entity:** Custom Entity บินตาม Player (อ้างอิง GeckoEntity)
- **Data:** ใช้ Data Components/Capabilities เก็บ Level, Tier, Skills, XP
- **Skills Logic:** ใช้ Events (LivingHurtEvent, TickEvent.Player) + Player Data
- **GUI:** Custom Menu/Screen สำหรับจัดการภูติ (ลาก Item ให้กิน, Sprout, Re-skill)
- **Items:** Theiah's Orb, Sweet Honey Wine ผ่าน DeferredRegister

---

## 🟣 Phase 5: Mod Compatibility & Spawning 🔲
> *ยังไม่เริ่ม — ปรับให้เข้ากับ Mod อื่น*

### 🌍 Natural Spawning (Biome Modifiers)
- [ ] สร้าง JSON Biome Modifiers สำหรับ Zombie King / Skeleton Lord
- [ ] กำหนด Spawn Weight, Min/Max Count, Biome targets
- [ ] ใช้ `RegisterSpawnPlacementsEvent` กำหนดกฎ (ON_GROUND, ดาสว่าง)

### 🔗 Apotheosis Compatibility
- [ ] ตรวจสอบ API ของ Apotheosis สำหรับ Item Rarity / Affix system
- [ ] ปรับ Upgrade System ให้ไม่ conflict กับ Apotheosis modifiers
- [ ] อาจเพิ่ม Custom Affix สำหรับ RPGEasyMode items

### 📏 Level-Based Mob System (ใช้ Mod ภายนอก)
> **แนวทาง:** ไม่สร้างระบบ Level เอง → ใช้ Mod ที่มีระบบ Level อยู่แล้ว (ชื่อ Mod: **TBD — รอกำหนด**)

- [ ] วิเคราะห์ API ของ Mod Level ที่เลือกใช้
- [ ] สร้าง Config hooks ให้ Antigravity อ่านค่า Mob Level จาก Mod นั้นได้
- [ ] ปรับ Drop Rate / Loot ตาม Mob Level (ผ่าน Config)

### 🛡️ Mod Compatibility ทั่วไป
- [ ] **JEI (Just Enough Items):** แสดงสูตร Alchemy Table + Ancient Forge
- [ ] **Curios API:** รองรับ Fairy ผ่าน Curios Slot
- [ ] **WAILA/Jade:** แสดงข้อมูล Block Entities ใน Tooltip

---

## 🔵 Phase 6: Special Mobs & NPCs 🔲
> *ยังไม่เริ่ม — มีโครงร่างแล้ว*

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

## ⚫ Phase 7: RPG Core & Polish 🔲
> *ระยะยาว — ระบบ RPG เชิงลึก*

- [ ] **Player Stats System:** Strength, Intelligence, Agility → เชื่อมกับ Item + Fairy
- [ ] **Player Stats UI:** หน้าจอแสดงสถานะรวม
- [ ] **Dynamic Alchemy Leveling:** ยิ่งทำ Potion มาก → ผลลัพธ์ดีขึ้น
- [ ] **Quest System:** ภารกิจพื้นฐาน (Kill Mobs, Craft Items, Find Materials)
- [ ] **Custom Recipe JSON (Datapack):** ย้ายสูตร Alchemy จาก Hardcode → JSON
- [ ] **Tome of Forgotten Table:** ออกแบบ Logic (อาจเป็นระบบ Enchant/Disenchant)

---

## 📊 สรุปความสำเร็จ

| Phase | สถานะ | ความคืบหน้า |
|-------|-------|------------|
| Phase 1: Core Foundation | ✅ เสร็จ | 100% |
| Phase 2: Combat & Crafting | 🟡 เกือบเสร็จ | ~85% (ขาด Bug Fixes, Textures, Extra Stats) |
| Phase 3: Item Drop & Loot | 🔲 ยังไม่เริ่ม | 0% (ต้องออกแบบ Logic) |
| Phase 4: Familia System | 🔲 ยังไม่เริ่ม | 0% (มี Research แล้ว) |
| Phase 5: Mod Compatibility | 🔲 ยังไม่เริ่ม | 0% |
| Phase 6: Special Mobs & NPCs | 🔲 ยังไม่เริ่ม | 0% (มีโครงร่าง) |
| Phase 7: RPG Core & Polish | 🔲 ยังไม่เริ่ม | 0% |

---

*หมายเหตุ: ลำดับ Phase อาจปรับเปลี่ยนได้ตามความเหมาะสม ข้อมูล Familia อ้างอิงจากระบบ Fairy ของ Black Desert Online ปรับให้เข้ากับ Minecraft*
