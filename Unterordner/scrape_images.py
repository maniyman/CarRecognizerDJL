from icrawler.builtin import GoogleImageCrawler
import time

# Liste der Marken
brands = [
    "Audi car", "BMW car", "Ford car", "Mercedes Benz car",
    "Opel car", "Peugeot car", "Renault car", "Skoda car", "Volkswagen car"
]

# Anzahl der Bilder pro Marke (maximal 1000 pro Anfrage)
IMAGES_PER_BRAND = 100  # Google Limitierung (max 100)

# Crawling für jede Marke
for brand in brands:
    print(f"🔍 Lade Bilder für: {brand}")
    
    # 
    for start in range(0, 70, IMAGES_PER_BRAND):  # 70 Bilder insgesamt
        crawler = GoogleImageCrawler(storage={'root_dir': f'dataset/{brand}'})
        
        try:
            crawler.crawl(
                keyword=brand,
                max_num=IMAGES_PER_BRAND,  # max_num auf 100 pro Anfrage setzen
                offset=start,  # Offset für den nächsten Crawl
            )
        except Exception as e:
            print(f"❌ Fehler bei {brand} bei Start {start}: {str(e)}")
            continue

        # Warte 1 Sekunde zwischen den Crawls
        time.sleep(1)

print("\n✅ Alle Bilder wurden erfolgreich heruntergeladen!")
