from PIL import Image

# เปิดไฟล์ภาพต้นฉบับ
img = Image.open("alchemy_table.jpg")

# ปรับขนาดเป็น 256x256
resized = img.resize((256, 256), Image.LANCZOS)

# บันทึกไฟล์ใหม่
resized.save("alchemy_table_256.png")

print("บันทึกภาพขนาด 256x256 เรียบร้อยแล้ว")
