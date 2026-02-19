from PIL import Image, ImageDraw
import os

def generate_template(name, width, height, slots=None, buttons=None, labels=None, custom=None):
    """
    Generate a GUI template PNG based on Minecraft coordinates.
    """
    # Create 256x256 texture map (standard Minecraft size)
    img = Image.new('RGBA', (256, 256), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # 1. Main Background Boundary
    draw.rectangle([0, 0, width, height], outline=(255, 255, 255, 255), width=1)
    
    # 2. Draw Slots
    if slots:
        for s_name, (sx, sy) in slots.items():
            # Standard Slot is 16x16, we draw 18x18 for guide
            draw.rectangle([sx, sy, sx+16, sy+16], outline=(255, 255, 0, 255), width=1)
    
    # 3. Draw Buttons
    if buttons:
        for b_name, (bx, by, bw, bh) in buttons.items():
            draw.rectangle([bx, by, bx+bw, by+bh], outline=(0, 255, 255, 255), width=1)

    # 4. Custom Elements (Bars, Arrows, etc)
    if custom:
        for c_name, (cx, cy, cw, ch, color) in custom.items():
            draw.rectangle([cx, cy, cx+cw, cy+ch], outline=color, width=1)

    # Save
    if not os.path.exists('gui_templates'):
        os.makedirs('gui_templates')
    
    file_path = f'gui_templates/{name}.png'
    img.save(file_path)
    print(f"Generated: {file_path}")

# --- DATA DEFINITIONS ---

# 1. Fairy UI
generate_template(
    name="fairy_gui",
    width=208,
    height=208,
    slots={
        "HP_Potion": (150, 90),
        "MP_Potion": (170, 90),
        "Growth_Input": (200, 70)
    },
    buttons={
        "Growth_G": (21*8, 3*8, 4*8, 2*8),
        "Skill_CS": (22*8, 14*8, 3*8, 8),
        "Release_R": (6*8, 24*8, 11*8, 16),
        "Revive_REV": (18*8, 24*8, 3*8, 16),
        "Summon_SUM": (22*8, 24*8, 3*8, 16)
    },
    custom={
        "EXP_Bar": (40, 56, 120, 4, (0, 255, 0, 255)),
        "Fairy_Model": (3, 144, 40, 40, (255, 0, 255, 255))
    }
)

# 2. Alchemy Table
generate_template(
    name="alchemy_table_gui",
    width=176,
    height=166,
    slots={
        "Input": (80, 11),
        "Ingredient_1": (26, 59),
        "Ingredient_2": (80, 59),
        "Ingredient_3": (134, 59),
        "Output": (80, 79)
    },
    custom={
        "Progress_Arrow": (85, 30, 8, 26, (0, 255, 0, 255))
    }
)

# 3. Ancient Forge
generate_template(
    name="ancient_forge_gui",
    width=176,
    height=166,
    slots={
        "Equipment": (80, 11),
        "Stone": (80, 59),
        "Support": (134, 59)
    },
    buttons={
        "Upgrade": (60, 75, 56, 20)
    }
)

print("\nAll templates generated in 'tools/gui_templates/' folder.")
