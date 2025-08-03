import easyocr
import sys
import json

reader = easyocr.Reader(['en', 'uz'])

image_path = sys.argv[1]
results = reader.readtext(image_path)

merchant, address, date, total = "", "", "", ""

for res in results:
    text = res[1]
    if not merchant and "MCHJ" in text:
        merchant = text
    if "tumani" in text or "MFY" in text:
        address = text
    if "UZS" in text or "MIQDOR" in text:
        total = text
    if "/" in text or ":" in text:
        date = text

output = {
    "merchantName": merchant,
    "address": address,
    "date": date,
    "total": total
}

print(json.dumps(output))
