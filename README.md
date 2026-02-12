# ⚔️ Antigravity RPG — Minecraft Mod (NeoForge 1.21.1)

> **Mod ID:** `rpgem` (RPGEasyMode)  
> **Platform:** NeoForge 1.21.1  
> **ประเภท:** RPG Enhancement Mod — ระบบ Potion, ตีบวก, Boss Mobs, และ Fairy Companion

---

## 📖 Mod นี้ทำอะไร?

**Antigravity RPG** เปลี่ยน Minecraft ให้มีกลไกแบบ MMORPG ด้วยระบบหลัก 4 อย่าง:

### 1. ⚗️ Infinite Potion System
- Potion **ใช้ไม่หมด** (Infinite Use + Cooldown)
- ปรุงจาก Alchemy Table ด้วยวัตถุดิบหายาก 3 ชนิด
- **3 Tiers** → Tier 3 "The Elixir of Boundless Eternity" ป้องกันความตาย

### 2. 🛠️ Ancient Forge (ระบบตีบวก)
- อัปเกรดอาวุธ/เกราะด้วย **Upgrade Stones** (3 Tier)
- ระบบ RNG: อัตราสำเร็จ 70% / 40% / 10%
- ล้มเหลว Tier สูง → เสี่ยงลดเลเวล (Risk vs. Reward)
- **Max Level:** Ultimate 3 (+50 Damage, +14 Armor)

### 3. 🧟 Boss Mobs
- **Zombie King** (300 HP) — Drop: Upgrade Stone Tier 1-2
- **Skeleton Lord** (250 HP, 15 ATK) — Drop: Upgrade Stone Tier 2-3

### 4. ✨ Custom Combat Effects
- **Evasion (30%):** หลบโจมตีสมบูรณ์
- **Iron Thorns (10%):** สะท้อนดาเมจ 200%
- **Boundless Grace:** สะท้อน + ล้าง Debuff + ป้องกันตาย (1 ครั้ง)

---

## 📂 โครงสร้างโปรเจค

```
neoforge-bokkchoy/
├── src/main/java/net/kankrittapon/rpgem/
│   ├── block/entity/          # Alchemy Table + Ancient Forge Logic
│   ├── entity/custom/         # Zombie King + Skeleton Lord
│   ├── event/                 # Combat System (Dodge, Reflect, Savior)
│   ├── init/                  # Registry (Items, Blocks, Effects, Entities)
│   ├── item/                  # Infinite Potion Logic
│   ├── menu/                  # GUI Menus
│   ├── network/               # Client↔Server Packets
│   └── screen/                # GUI Screens
├── moddetails.md              # 📋 รายละเอียด Mod ทั้งหมด (Logic, Stats, Formulas)
├── roadmap.md                 # 🗺️ แผนพัฒนา 7 Phases
├── potionplan.md              # 🧪 Game Design Document ระบบ Potion
└── README.md                  # 📖 ไฟล์นี้
```

---

## 📋 เอกสารสำคัญ

| ไฟล์ | คำอธิบาย |
|------|----------|
| [moddetails.md](moddetails.md) | รายละเอียดทุกระบบ — Items, Blocks, Effects, สูตรคำนวณ, Stats ม็อบ, Config ทั้งหมด |
| [roadmap.md](roadmap.md) | แผนพัฒนา 7 Phases — ตั้งแต่ Core จนถึง RPG Advanced |
| [potionplan.md](potionplan.md) | Game Design ระบบ Potion Effect แบบ RPG (Dodge, Thorns, Savior) |

---

## 🗺️ Roadmap Overview

| Phase | ชื่อ | สถานะ |
|-------|------|-------|
| 1 | Core Foundation (GUI, Network, Registration) | ✅ เสร็จ |
| 2 | Combat & Crafting (Potion, Upgrade, Boss) | 🟡 85% |
| 3 | Item Drop & Loot System | 🔲 รอออกแบบ |
| 4 | **Familia System** (Fairy Companion + Skills) | 🔲 มี Research |
| 5 | Mod Compatibility (Apotheosis, Level Mods, JEI) | 🔲 |
| 6 | Special Mobs & NPCs (Custom Villagers) | 🔲 มีโครง |
| 7 | RPG Core (Player Stats, Quest, Dynamic Leveling) | 🔲 |

> ดูรายละเอียดแต่ละ Phase ใน [roadmap.md](roadmap.md)

---

## 🔧 วิธี Build & Run

### Prerequisites
- **Java 21** (Microsoft OpenJDK หรือ Adoptium)
- **Gradle** (Wrapper มีมาให้แล้ว)

### Commands
```bash
# Build
./gradlew build

# Run Client (ทดสอบ Mod ใน Minecraft)
./gradlew runClient

# Run Server
./gradlew runServer
```

---

## ⚙️ Config (ปรับแต่งได้)

ค่าทั้งหมดปรับได้ใน Config File:

| ค่า | Default | คำอธิบาย |
|-----|---------|----------|
| `dodgeChance` | 0.3 | โอกาสหลบ Evasion (30%) |
| `thornsChance` | 0.1 | โอกาสสะท้อน (10%) |
| `reflectionMultiplier` | 2.0 | ตัวคูณ Damage สะท้อน |
| `upgradeSuccessRateTier1` | 0.7 | อัตราสำเร็จ Upgrade Tier 1 |
| `upgradeSuccessRateTier2` | 0.4 | อัตราสำเร็จ Upgrade Tier 2 |
| `upgradeSuccessRateTier3` | 0.1 | อัตราสำเร็จ Upgrade Tier 3 |

---

## 📝 License

This project is for personal/educational use.

---

*Made with ❤️ by kankrittapon — Powered by NeoForge 1.21.1*
