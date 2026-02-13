# Roadmap: RPGEasyMode (Antigravity RPG)

> **อัปเดตล่าสุด:** 13 ก.พ. 2026  
> **Platform:** NeoForge 1.21.1  
> แผนพัฒนาทั้งหมดของ Mod — จัดรวมจาก Source Code + NotebookLM Research + แผนผู้พัฒนา + Trait Counter System Design  
> **Mod Ecosystem:** ทำงานร่วมกับ **Apotheosis + L2 Hostility + L2 Complements + L2 Library**

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

### 🛠️ Upgrade System ✅ (ขาดบางส่วน → 🆕 ออกแบบใหม่)
- [x] 3-Tier Upgrade Stone (70% / 40% / 10% success rate)
- [x] Attribute Modifiers: Attack Damage + Armor
- [x] Downgrade on Failure (Tier 2/3)
- [ ] 🆕 **Forged Stone Crafting** — ระบบหลอมหิน 6 ชนิด (Fortitude/Agility/Destruction × Tier 2 + Ultimate)
- [ ] 🆕 **Custom Attributes** — ลงทะเบียน 7 Attributes ใหม่ (Life Steal, Crit, Element Dmg, DR, EVA, Reflect/Seal Resist)
- [ ] 🆕 **Weapon Upgrade Path** — ATK + Life Steal → Crit → Element Damage
- [ ] 🆕 **Armor Path System** — เลือกสายตอน +6:
  - 🧱 **Damage Reduction** (DR cap 50%, EVA 15%) — Forged Stone: Fortitude
  - 💨 **Damage Evasion** (EVA cap 50%, DR 15%) — Forged Stone: Agility
- [ ] ⚠️ **Bug: Savior Cleanse** ลบทุก Effect (ควรลบเฉพาะ Harmful)

### ⚗️ Boundless Grace V2 🆕
- [ ] 🆕 **Potion T3 Counter ALL Traits** — เพิ่ม 6 protections ใหม่:
  - Evasion↑ (30%→50%), Mob Slowness Aura, Reflect Shield (80%)
  - Element Aura (cycle damage type), Anti-Heal Aura, Soul Purge
  - Seal Ward (กัน Ragnarok)
- [ ] 🆕 **Use CD ≠ Effect CD** — กด = Heal 3 Hearts เสมอ, Buff ได้เฉพาะตอน Effect ไม่ active

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

## 🟣 Phase 5: Mod Ecosystem Integration (Apotheosis + L2) 🔲
> *ยังไม่เริ่ม — Antigravity ต้อง "เสริม" ระบบจาก Mod เหล่านี้ ไม่ใช่ทำซ้ำ*

### 🎯 ปรัชญาการ Integrate
> **หลักสำคัญ:** Antigravity เป็น Mod **เสริม** — ไม่สร้างระบบ Level/Affix/Armor เอง แต่ออกแบบให้ทำงานร่วมกับ Mod ที่มีอยู่แล้ว

| Mod | หน้าที่หลัก | Antigravity ต้องปรับอะไร |
|-----|-----------|------------------------|
| **Apotheosis** | Boss/Elite สุ่มเกิด + Affix ติดไอเทม + ปลด Enchant Cap | ❗ ระวัง Attribute ซ้อนกับ Upgrade System |
| **L2 Hostility** | Mob Level + 37 Traits + Scaling Difficulty | ❗ ไม่ต้องสร้างระบบ Level เอง |
| **L2 Complements** | Endgame Armor (Sculkium/Eternium) + Enchants ใหม่ | ❗ Balance: ไม่ให้ Upgrade แรงกว่า Eternium เร็วเกิน |
| **L2 Library** | Player Attribute Tab + Curios 54 Slots | ✅ ใช้ Curios สำหรับ Fairy Slot |

### ⚔️ Apotheosis Integration
- [ ] ตรวจสอบ Affix system → ไม่ทับซ้อนกับ `UPGRADE_LEVEL` Attribute Modifiers
- [ ] Boss/Elite ที่ Apotheosis สุ่มเกิด → ต้องดรอปของจาก Antigravity ด้วย (ผ่าน Global Loot Modifier)
- [ ] Enchant Cap ที่ Apotheosis ปลด → Potion effects ไม่ควร OP เกินเมื่อรวมกับ Enchant สูง
- [ ] อาจเพิ่ม Custom Affix สำหรับ RPGEasyMode items

### 📏 L2 Hostility Integration (ระบบ Level & Traits)
> **แนวทาง:** ไม่สร้างระบบ Level เอง → ใช้ L2 Hostility เป็นหลัก

#### Traits ที่ต้อง Aware (37 Traits, 3 ระดับ):
| ระดับ | Traits สำคัญ | ผลกระทบต่อ Antigravity |
|-------|------------|----------------------|
| **Regular** | Fiery, Speedy, Tank | Evasion/Iron Thorns ยังใช้ได้ปกติ |
| **Advanced (Lv.100+)** | **Adaptive** (กัน damage ประเภทที่โดนซ้ำ), **Reflect** (สะท้อน physical) | ⚠️ Reflect อาจ conflict กับ Iron Thorns |
| **Legendary** | **Undying** (ฟื้นเรื่อยๆ), **Dementor** (กัน physical), **Ragnarok** (seal อุปกรณ์) | ⚠️ Savior อาจไม่พอรับมือ → ต้อง balance |

- [ ] วิเคราะห์ API ของ L2 Hostility → อ่าน Mob Level สำหรับ Drop Rate scaling
- [ ] กำหนด Level ขั้นต่ำให้ Zombie King / Skeleton Lord (เช่น Lv.50+)
- [ ] ปรับ Drop Rate ตาม Mob Level → ของดีดรอปจาก Mob Level สูง
- [ ] Config hooks สำหรับเปิด/ปิด L2H integration

### 🛡️ Trait Counter Integration 🆕
> **หลักการ:** Potion T3 = counter ชั่วคราว (60s), Forge = counter ถาวร (passive)

- [ ] ตรวจสอบ L2H Trait events → hook เข้ากับ Boundless Grace V2 protections
- [ ] Iron Thorns vs Reflect Trait → DamageSource filtering ป้องกัน loop
- [ ] Element Aura → auto cycle damage type ทุก 5s (Physical→Magic→Fire→Ice)
- [ ] Anti-Heal Aura → ลด mob regeneration 80% ในรัศมี 8 บล็อก
- [ ] Soul Purge → ป้องกัน Undying trait mob ฟื้นจากตาย
- [ ] Seal Ward → ป้องกัน Ragnarok equipment seal

### 🛡️ L2 Complements Balancing
> **กฎสำคัญ:** Ancient Forge ต้องไม่ทำให้ไอเทม OP เกิน Endgame ของ L2C

| วัสดุ L2C | ความแรง | Antigravity ต้องทำ |
|-----------|---------|-------------------|
| **Sculkium** | > Netherite (HP + ATK สูงเหมือน Warden) | Upgrade ระดับ Tier 2 ควรเท่า Sculkium |
| **Eternium** | Infinite Durability | Upgrade Ultimate ควรมี bonus เทียบเท่า |
| **Totemic Gold** | Auto-Heal + กัน Wither/Poison | ไม่ควรซ้อนกับ Savior (เลือกอย่างใดอย่างหนึ่ง) |

- [ ] ตรวจสอบ Attribute ของ L2C armors → ปรับ Upgrade scaling ไม่ให้เกินกว่า
- [ ] Enchants ใหม่ (Void Touch, Life Mending, Hardened) → ไม่ conflict กับ Potion effects
- [ ] ออกแบบ "Upgrade Ceiling" ให้สอดคล้องกับ power curve ของ L2C

### 🌍 Natural Spawning (Biome Modifiers)
- [ ] สร้าง JSON Biome Modifiers สำหรับ Zombie King / Skeleton Lord
- [ ] กำหนด Spawn Weight, Min/Max Count, Biome targets
- [ ] ใช้ `RegisterSpawnPlacementsEvent` กำหนดกฎ (ON_GROUND, กลางคืน)

### 🛡️ Mod Compatibility ทั่วไป
- [ ] **JEI (Just Enough Items):** แสดงสูตร Alchemy Table + Ancient Forge
- [ ] **Curios API:** รองรับ Fairy ผ่าน Curios Slot (ใช้ L2 Library's expanded slots)
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
| Phase 5: Mod Ecosystem (Apotheosis+L2) | 🔲 ยังไม่เริ่ม | 0% (มี Research ครบ) |
| Phase 6: Special Mobs & NPCs | 🔲 ยังไม่เริ่ม | 0% (มีโครงร่าง) |
| Phase 7: RPG Core & Polish | 🔲 ยังไม่เริ่ม | 0% |

---

*หมายเหตุ: ลำดับ Phase อาจปรับเปลี่ยนได้ตามความเหมาะสม ข้อมูล Familia อ้างอิงจากระบบ Fairy ของ Black Desert Online ปรับให้เข้ากับ Minecraft*
