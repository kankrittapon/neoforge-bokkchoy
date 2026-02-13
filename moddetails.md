# Mod Details: RPGEasyMode (Antigravity RPG)

> **อัปเดตล่าสุด:** 13 ก.พ. 2026  
> **Platform:** NeoForge 1.21.1  
> **Mod ID:** `rpgem`

เอกสารนี้เป็นรายละเอียดทั้งหมดของ Mod ตรวจสอบจาก Source Code โดยตรง

---

## 1. ⚔️ Items (ไอเท็มทั้งหมด)

### 💎 Upgrade Stones — หินธรรมดา (ใช้ +1 ถึง +15)
| ไอเท็ม | Registry ID | คำอธิบาย | แหล่งที่มา |
|--------|-------------|----------|------------|
| Upgrade Stone Tier 1 | `upgrade_stone_tier_1` | หินตีบวกระดับต้น (ใช้ +1 ถึง +15) | Drop: Zombie King (100%) |
| Upgrade Stone Tier 2 | `upgrade_stone_tier_2` | วัตถุดิบหลอม Forged Stone (Tier 2) | Drop: Zombie King (50%), Skeleton Lord (100%) |
| Upgrade Stone Tier 3 | `upgrade_stone_tier_3` | วัตถุดิบหลอม Forged Stone (Ultimate) | Drop: Skeleton Lord (30%) |

### 🔨 Forged Stones — หินหลอม (ใช้ I~X + Ultimate) 🆕
> **ต้องหลอมจากวัตถุดิบ** — ชนิดหินกำหนดสายเกราะ / สายอาวุธ

| ไอเท็ม | Registry ID | สำหรับ | วัตถุดิบ |
|--------|-------------|--------|----------|
| Forged Stone: Fortitude | `forged_stone_fortitude` | เกราะสาย 🧱 DR (I~X) | Stone T2 + Iron Block + Diamond |
| Forged Stone: Agility | `forged_stone_agility` | เกราะสาย 💨 EVA (I~X) | Stone T2 + Gold Block + Emerald |
| Forged Stone: Destruction | `forged_stone_destruction` | อาวุธ (I~X) | Stone T2 + Blaze Rod + Nether Star |
| Forged Stone: Ult Fortitude | `forged_stone_ultimate_fortitude` | เกราะสาย 🧱 DR (Ultimate) | Stone T3 + Netherite + Totem |
| Forged Stone: Ult Agility | `forged_stone_ultimate_agility` | เกราะสาย 💨 EVA (Ultimate) | Stone T3 + Netherite + Enchanted Book |
| Forged Stone: Ult Destruction | `forged_stone_ultimate_destruction` | อาวุธ (Ultimate) | Stone T3 + Netherite + Dragon Breath |

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
**ข้อมูลเก็บใน:** `DataComponent` → `rpgem:upgrade_level` (Integer) + `rpgem:armor_path` (String: `"none"` / `"reduction"` / `"evasion"`)

#### หินตีบวก (3 ระดับ):

| ระดับ | หินที่ใช้ | ช่วงเลเวล | อัตราสำเร็จ | หมายเหตุ |
|------|----------|-----------|------------|---------|
| Tier 1 | Upgrade Stone Tier 1 (ธรรมดา) | +1 ถึง +15 | **70%** | ล้มเหลว: ไม่ลดเลเวล |
| Tier 2 | **Forged Stone** (หินหลอม) | I ถึง X (16-25) | **40%** | ล้มเหลว: 50% ลดเลเวล 1 |
| Ultimate | **Forged Stone Ultimate** (หินหลอม) | Ult 1-3 (26-28) | **10%** | ล้มเหลว: 50% ลดเลเวล 1 |

> **สายเกราะถูกกำหนดตอนใช้ Forged Stone ครั้งแรก (ที่ +6)**  
> ถ้าเกราะเป็นสาย 🧱 แล้ว → ใช้ Forged Stone: Agility ไม่ได้

---

#### ⚔️ อาวุธ (Weapon Upgrade) — ใช้ Forged Stone: Destruction

| ระดับ | ATK | Trait Counter |
|------|-----|--------------|
| +1 ~ +5 | +1/lv | — |
| +6 ~ +10 | +1/lv | Life Steal 2% |
| +11 ~ +15 | +1/lv | Crit Chance 8% |
| I ~ V | +2/lv | Element Damage 15% |
| VI ~ X | +2/lv | Crit 15% + Life Steal 4% |
| Ultimate 1 | +5/lv | Element 25% + Life Steal 5% |
| Ultimate 2 | +5/lv | Crit 20% + Element 30% |
| Ultimate 3 | +5/lv | **ALL MAX** (LS 8%, Crit 25%, Ele 35%) |

---

#### 🛡️ เกราะ (Armor Upgrade) — 2 สาย เลือกตอน +6

> ⚖️ **Cap สถิติ:** ทั้งสองสายได้ทั้ง DR + EVA แต่ cap ต่างกัน

| สถิติ | 🧱 Reduction สาย | 💨 Evasion สาย |
|-------|:---:|:---:|
| **Damage Reduction** | **50%** (หลัก) | 15% (รอง) |
| **Evasion** | 15% (รอง) | **50%** (หลัก) |
| **Life Steal** | 8% | 8% |
| **Reflect Resist** | 80% | 40% |
| **Seal Resist** | 80% | 60% |

**สาย 🧱 Damage Reduction** — ใช้ Forged Stone: Fortitude  
*"โดนตี − ดาเมจน้อย เลือดเยอะ"*

| ระดับ | Armor | DR | EVA | อื่นๆ |
|------|-------|----|----|------|
| +1 ~ +5 | +0.5/lv | — | — | — |
| +6 ~ +10 | +0.5/lv | 5% | — | Max HP +2/lv |
| +11 ~ +15 | +0.5/lv | 10% | 3% | Reflect Resist 30% |
| I ~ V | +1.0/lv | 20% | 5% | Reflect Resist 50% |
| VI ~ X | +1.0/lv | 30% | 8% | Seal Resist 40% |
| Ult 1 | +1.5/lv | 35% | 10% | Seal 60% + Reflect 60% |
| Ult 2 | +1.5/lv | 40% | 12% | Seal 70% + Reflect 70% |
| Ult 3 | +1.5/lv | **50%** | **15%** | Seal **80%** + Reflect **80%** + HP+30 |

**สาย 💨 Damage Evasion** — ใช้ Forged Stone: Agility  
*"โดนตี — มีโอกาสหลบ ไม่โดนเลย"*

| ระดับ | Armor | EVA | DR | อื่นๆ |
|------|-------|-----|----|------|
| +1 ~ +5 | +0.3/lv | — | — | — |
| +6 ~ +10 | +0.3/lv | 8% | — | Speed on Dodge (Speed I 2s) |
| +11 ~ +15 | +0.3/lv | 15% | 3% | Reflect Resist 20% |
| I ~ V | +0.5/lv | 25% | 5% | Seal Resist 20% |
| VI ~ X | +0.5/lv | 33% | 8% | Seal 35% + Reflect 30% |
| Ult 1 | +0.5/lv | 38% | 10% | Seal 45% |
| Ult 2 | +0.5/lv | 44% | 12% | Seal 50% + Reflect 35% |
| Ult 3 | +0.5/lv | **50%** | **15%** | Seal **60%** + Reflect **40%** |

---

#### 🆕 Custom Attributes (ต้องสร้างใหม่)

| Attribute | Registry ID | สำหรับ | คำอธิบาย |
|-----------|------------|--------|----------|
| Life Steal | `rpgem:life_steal` | ⚔️ Weapon | ดูดเลือด % ต่อ Hit |
| Crit Chance | `rpgem:crit_chance` | ⚔️ Weapon | โอกาสดาเมจ ×2 |
| Element Damage | `rpgem:element_damage` | ⚔️ Weapon | เพิ่ม Magic damage % |
| Damage Reduction | `rpgem:damage_reduction` | 🧱 Armor | ลดดาเมจตรง % |
| Evasion | `rpgem:evasion` | 💨 Armor | โอกาสหลบโจมตี % |
| Reflect Resist | `rpgem:reflect_resist` | 🛡️ Armor | ลด reflected damage % |
| Seal Resist | `rpgem:seal_resist` | 🛡️ Armor | โอกาสกัน equipment seal % |

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
| **The Elixir of Boundless Eternity** 🌟 | Full Heal 100% + **Boundless Grace V2** (60s) + Regen III (15s) + Resistance II (15s) + Absorption IV (2m) + Fire Resistance (20s) |

**ลักษณะพิเศษ:** มี Enchantment Glint (เรืองแสงรุ้ง), ชื่อสีทอง

#### 🆕 Boundless Grace V2 — Counter ทุก L2H Trait:
> **กฎ:** Use CD (2s) ≠ Effect CD (60s) — กดใช้ได้ทุก 2s เพื่อ Heal 3 Hearts, Buff ได้เฉพาะตอน Effect ไม่ active

| Protection | ทำอะไร | Counter Traits |
|-----------|--------|---------------|
| Fire Resistance ✅ | กันไฟ | Fiery |
| Evasion↑ 🔄 | หลบ 30%→50% | Speedy, Dementor |
| Mob Slowness Aura 🆕 | Mob รอบ 8 บล็อก Slow II | Speedy |
| Resistance II + Absorb IV ✅ | ลดดาเมจ+เกราะเสริม | Tank |
| Reflect Shield 🆕 | ลด reflected dmg 80% | Reflect |
| Element Aura 🆕 | ดาเมจ cycle ทุก 5s | Adaptive, Dementor |
| Anti-Heal Aura 🆕 | Mob ฟื้น HP -80% | Regenerating |
| Soul Purge 🆕 | Mob ตายแล้วไม่ฟื้น | Undying |
| Seal Ward 🆕 | กัน equipment seal | Ragnarok |
| Death Prevention ✅ | กันตาย 1 ครั้ง | ทุก Trait |

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

## 7. 🌐 Mod Ecosystem (Apotheosis + L2 Mods)

> **หลักการ:** Antigravity เป็น Mod **เสริม** ไม่สร้างระบบ Level/Affix/Endgame Armor เอง  
> ใช้ Mod ภายนอกเป็นหลักแล้วออกแบบให้ทำงานร่วมกัน

### Mod ที่ใช้ร่วมกัน:

| Mod | เวอร์ชัน | หน้าที่หลัก |
|-----|---------|-----------|
| **Apotheosis** | 1.21.x | Boss/Elite สุ่มเกิด, Affix ติดไอเทม, ปลด Enchant Level Cap |
| **L2 Hostility** | 1.21.x | ระบบ Mob Level + 37 Traits (Regular/Advanced/Legendary) + Scaling Difficulty |
| **L2 Complements** | 1.21.x | Endgame Armor (Sculkium, Eternium, Totemic Gold, Poseidite) + Enchants ใหม่ |
| **L2 Library** | 1.21.x | Player Attribute Tab UI + ขยาย Curios Slots (54 ช่อง) |

### Traits สำคัญจาก L2 Hostility + Counter จาก Antigravity:

| Trait (บน Mob) | ผู้เล่นโดนอะไร | Counter: Potion | Counter: Forge |
|---------------|---------------|----------------|----------------|
| **Fiery** | ถูกจุดไฟ | ✅ T1 C→B Fire Resist | — |
| **Speedy** | Mob ตีถี่ หนีไม่ทัน | ✅ T1 Evasion / T3 Slow Aura | 💨 EVA หลบ |
| **Tank** | Mob อึด สู้ไม่จบ | ✅ T3 Resist+Absorb | ⚔️ Crit+LS / 🧱 DR ทน |
| **Adaptive** | กัน damage ประเภทที่โดนซ้ำ | ✅ T3 Element Aura | ⚔️ Element Dmg |
| **Reflect** | ตี Mob แล้วดาเมจสะท้อนกลับ | ✅ T3 Reflect Shield 80% | 🧱 Reflect Resist 80% |
| **Regenerating** | Mob ฟื้น HP ตลอด | ✅ T3 Anti-Heal Aura | ⚔️ Crit (ฆ่าเร็ว) |
| **Undying** | ฆ่าแล้วฟื้นขึ้นมาใหม่ | ✅ T3 Soul Purge | ⚔️ Crit (ฆ่าเร็ว) |
| **Dementor** | กัน physical + เจาะเกราะ | ✅ T3 Element+Evasion↑ | ⚔️ Element / 💨 EVA หลบ |
| **Ragnarok** | seal อุปกรณ์ | ✅ T3 Seal Ward | 🧱 Seal Resist 80% |

### Balance Guidelines:

| หัวข้อ | กฎเกณฑ์ |
|--------|---------|
| **Upgrade vs L2C** | Ancient Forge Ultimate **ไม่ควร** แรงกว่า Eternium/Sculkium เร็วเกิน |
| **Potion vs Enchant** | Potion effects + Apotheosis Enchant Cap ปลด = ต้องระวังไม่ OP |
| **Boss Level** | Zombie King/Skeleton Lord ควรอยู่ที่ L2H Level 50+ |
| **Drop Scaling** | ของดี Drop จากมอนสเตอร์ Level สูง (อ่านจาก L2H API) |
| **Affix Overlap** | `UPGRADE_LEVEL` Attribute ต้องไม่ซ้อนกับ Apotheosis Affix |
| **T3 = Counter ALL** | Potion T3 Boundless Grace V2 ต้อง counter ได้ทุก Trait (60s) |
| **Forge = Passive** | Ancient Forge ให้ passive Trait protection ตามระดับ Upgrade |

---

## 8. 🚧 ระบบที่ยังขาด / ต้องปรับปรุง

| ลำดับ | ระบบ | รายละเอียด | ความสำคัญ |
|-------|------|-----------|----------|
| 1 | **Spawn Rules** | Mob ไม่เกิดเองในโลก ต้องทำ Biome Modifiers | 🔴 สูง |
| 2 | **Forged Stone Crafting** | 🆕 ระบบหลอมหิน 6 ชนิด ใน Alchemy Table / Forge | 🔴 สูง |
| 3 | **Custom Attributes** | 🆕 ลงทะเบียน 7 Attributes ใหม่ (Life Steal, Crit, Element, DR, EVA, Reflect/Seal Resist) | 🔴 สูง |
| 4 | **Armor Path System** | 🆕 ระบบเลือกสายเกราะ (🧱 DR / 💨 EVA) ตอน +6 | 🔴 สูง |
| 5 | **Boundless Grace V2** | 🆕 เพิ่ม 6 protections ใหม่ใน T3 (Reflect Shield, Element Aura, Seal Ward, Soul Purge, Spirit Piercing, Anti-Heal Aura) | 🔴 สูง |
| 6 | **Looting Enchantment** | ยังไม่ทำงานกับ Special Mob drops (hardcoded = 0) | 🟡 กลาง |
| 7 | **Tome of Forgotten Table** | บล็อกลงทะเบียนแล้วแต่ไม่มี Logic/GUI ใดๆ | 🟡 กลาง |
| 8 | **Custom Mob Renderer** | Boss ดูเหมือน Zombie/Skeleton ปกติ ยังไม่มี Scale/Texture พิเศษ | 🟡 กลาง |
| 9 | **Savior Cleanse Bug** | `removeAllEffects()` ลบทุก Effect รวมถึง Beneficial → ควรลบเฉพาะ Harmful | 🟠 ปานกลาง |
| 10 | **GUI Animations** | ไม่มี Particle/Animation ตอน Crafting/Upgrade เสร็จ | 🟢 ต่ำ |
| 11 | **Potion Texture ขาด** | Infinite Potion ทั้ง 3 Tier ไม่มี Item Model ใน assets | 🟡 กลาง |
| 12 | **Player Stats UI** | ไม่มีหน้าจอแสดงค่าสถานะรวมของผู้เล่น | 🟢 ต่ำ (Phase 4) |

---

## 9. 🎨 Textures ที่ต้องทำ

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

## 10. 📁 โครงสร้างไฟล์ Source Code

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
