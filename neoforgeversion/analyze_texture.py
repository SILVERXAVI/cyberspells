import sys
from PIL import Image

def analyze_image(path):
    print(f"Analyzing {path}...")
    try:
        with Image.open(path) as img:
            img = img.convert("RGBA")
            width, height = img.size
            print(f"Size: {width}x{height}")
            
            transparent_pixels = 0
            opaque_pixels = 0
            semi_transparent_pixels = 0
            
            for y in range(height):
                for x in range(width):
                    r, g, b, a = img.getpixel((x, y))
                    if a == 0:
                        transparent_pixels += 1
                    elif a == 255:
                        opaque_pixels += 1
                    else:
                        semi_transparent_pixels += 1
                        
            total = width * height
            print(f"Pixels: {total}")
            print(f"Transparent (A=0): {transparent_pixels}")
            print(f"Opaque (A=255): {opaque_pixels}")
            print(f"Semi-transparent: {semi_transparent_pixels}")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        analyze_image(sys.argv[1])
