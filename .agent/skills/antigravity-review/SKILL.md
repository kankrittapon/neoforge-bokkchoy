---
name: antigravity-code-review
description: ตรวจสอบ Code ของ Mod Antigravity เพื่อหา Bug, ประสิทธิภาพ และความเข้ากันได้ของระบบ RPG
---

# Antigravity Code Review Skill

ใช้ Skill นี้เมื่อมีการสร้างฟีเจอร์ใหม่ หรือต้องการตรวจสอบคุณภาพของ Code ในโปรเจกต์ Antigravity

## รายการตรวจสอบ (Review Checklist)

1. **NeoForge Consistency:** ใช้ Registry, Event Bus และ Capability ของ NeoForge 1.21.1 ได้ถูกต้องหรือไม่?
2. **RPG Logic:** ระบบค่าพลัง, Item RPG หรือระบบพิกัด GPS มีการจัดการ Data Sync ระหว่าง Server/Client ที่ดีพอหรือยัง?
3. **Compatibility:** Code ส่วนนี้ไปแก้ไข Vanilla Code หรือ Mod อื่นโดยตรงจนอาจเกิด Conflict หรือไม่?
4. **Efficiency:** การประมวลผลใน Tick Event มีการคำนวณที่หนักเกินไปจนทำให้ TPS ตกหรือไม่?

## ขั้นตอนการให้ Feedback

- ระบุตำแหน่งบรรทัดที่มีปัญหาให้ชัดเจน
- อธิบายเหตุผลเป็นภาษาไทยว่าทำไมถึงควรแก้ และจะส่งผลกระทบอย่างไร
- เสนอทางเลือกหรือ Code ตัวอย่าง (Best Practice) เสมอ

## การใช้งาน

- วิเคราะห์ไฟล์จาก @filename ที่ผู้ใช้ระบุ
- สรุปผลการ Review และรอคำอนุมัติก่อนเสนอการแก้ไขแบบ Auto
