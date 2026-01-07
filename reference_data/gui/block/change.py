import os
from PIL import Image
from tkinter import filedialog, Tk, simpledialog, messagebox

def batch_convert():
    root = Tk()
    root.withdraw() # ซ่อนหน้าต่างหลัก

    print("--- โปรแกรมแปลงไฟล์ + ย่อขยายรูปภาพ (ยกโฟลเดอร์) ---")
    
    # 1. เลือกโฟลเดอร์ที่มีรูป
    # print("1. กรุณาเลือกโฟลเดอร์ต้นทาง...") (ไม่ต้อง print แล้ว เพราะเด้งหน้าต่างเลย)
    folder_path = filedialog.askdirectory(title="เลือกโฟลเดอร์ที่มีรูปภาพต้นฉบับ")
    if not folder_path: return

    # 2. (ใหม่) ถามนามสกุลผ่านหน้าต่าง GUI แทน
    target_ext = simpledialog.askstring(
        "ตั้งค่านามสกุล", 
        "ต้องการแปลงเป็นนามสกุลอะไร?\n(เช่น png, jpg, webp):"
    )
    if not target_ext: return # ถ้ากดยกเลิก
    target_ext = target_ext.strip().lower()
    if not target_ext.startswith("."): target_ext = "." + target_ext

    # 3. (ใหม่) ถามขนาด Resize
    size_input = simpledialog.askstring(
        "ตั้งค่าขนาด (Resize)", 
        "ใส่ขนาดที่ต้องการ (เช่น 500 หรือ 64x64)\n(ปล่อยว่าง = ไม่ย่อรูป):"
    )

    # 4. (ใหม่) ถามโหมด Pixel Art
    is_pixel_art = False
    if size_input:
        is_pixel_art = messagebox.askyesno(
            "Pixel Art Mode?", 
            "เปิดโหมด Pixel Art หรือไม่?\n\n- Yes: ภาพคมเหลี่ยม (เหมาะกับไอเทม Minecraft)\n- No: ภาพเนียน (เหมาะกับรูปทั่วไป)"
        )

    # สร้างโฟลเดอร์ output
    output_folder = os.path.join(folder_path, "converted_output")
    os.makedirs(output_folder, exist_ok=True)

    print(f"\nกำลังเริ่มแปลงไฟล์... (บันทึกไปที่ {output_folder})")
    
    count = 0
    # ลูปไฟล์ในโฟลเดอร์
    for filename in os.listdir(folder_path):
        if filename.lower().endswith(('.png', '.jpg', '.jpeg', '.webp', '.bmp', '.tiff', '.ico')):
            try:
                img_path = os.path.join(folder_path, filename)
                img = Image.open(img_path)

                # --- โซนย่อขยายภาพ (Resize Logic) ---
                if size_input:
                    target_w, target_h = 0, 0
                    try:
                        # กรณีใส่มาเป็น 64x64
                        if 'x' in size_input.lower():
                            w_str, h_str = size_input.lower().split('x')
                            target_w = int(w_str.strip())
                            target_h = int(h_str.strip())
                        
                        # กรณีใส่มาตัวเดียว 500 (Auto Height)
                        elif size_input.strip().isdigit():
                            target_w = int(size_input.strip())
                            aspect_ratio = img.height / img.width
                            target_h = int(target_w * aspect_ratio)

                        if target_w > 0 and target_h > 0:
                            # เลือกวิธี Resample (สำคัญมาก)
                            mode = Image.Resampling.NEAREST if is_pixel_art else Image.Resampling.LANCZOS
                            img = img.resize((target_w, target_h), mode)
                            
                    except ValueError:
                        print(f"⚠️ ข้ามการย่อรูป {filename} เพราะรูปแบบขนาดไม่ถูกต้อง")
                # -----------------------------------

                # จัดการเรื่องพื้นใส ถ้าแปลงเป็น jpg (เหมือนโค้ดเดิม)
                if target_ext in ['.jpg', '.jpeg'] and img.mode in ('RGBA', 'LA'):
                    bg = Image.new("RGB", img.size, (255, 255, 255))
                    bg.paste(img, mask=img.split()[3])
                    img = bg
                elif target_ext not in ['.jpg', '.jpeg']:
                    img = img.convert("RGBA")

                # ตั้งชื่อไฟล์ใหม่
                new_filename = os.path.splitext(filename)[0] + target_ext
                save_path = os.path.join(output_folder, new_filename)
                
                img.save(save_path)
                print(f"✅ ทำรายการ: {filename} -> {new_filename}")
                count += 1

            except Exception as e:
                print(f"❌ พลาด: {filename} ({e})")

    print(f"\nสรุป: สำเร็จทั้งหมด {count} รูปครับ!")
    
    # เปิดโฟลเดอร์เมื่อเสร็จ
    try:
        os.startfile(output_folder)
    except:
        pass

if __name__ == "__main__":
    batch_convert()