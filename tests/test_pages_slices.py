import requests
import random
import time

BASE_URL = "http://localhost:8080/api"

API_USERS = f"{BASE_URL}/users"
API_CATEGORIES = f"{BASE_URL}/categories"
API_PRODUCTS = f"{BASE_URL}/products"

# =============================
# CONFIGURACIÓN
# =============================
TOTAL_USERS = 5
TOTAL_CATEGORIES = 8
TOTAL_PRODUCTS = 1000

# =============================
# DATOS BUSCABLES (RETocados)
# =============================
ADJECTIVES = ["Elite", "Plus", "Neo", "Prime", "Compact", "Boost", "Air"]
PRODUCT_TYPES = ["Notebook", "Mouse", "Keyboard", "Display", "Tablet", "Phone", "Headset"]
BRANDS = ["Lenovo", "HP", "Dell", "Asus", "Samsung", "Logitech", "Apple"]
SPECS = ["i5", "i7", "i9", "8GB", "16GB", "32GB", "512GB", "1TB"]

# =============================
# CREAR USUARIOS (PERSONALIZADO)
# =============================
def create_users():
    print("📌 Creando usuarios de prueba...")
    user_ids = []

    full_names = [
        "Jeon Jungkook",
        "Jamie Campbell",
        "Xaden Riorson",
        "Gideon de Villiers",
        "Jacob Elordi"
    ]

    for full_name in full_names:

        parts = full_name.split()
        first_name = parts[0]
        last_name = parts[1]

        email = f"{first_name.lower()}.{last_name.lower()}@demo.com"

        payload = {
            "name": full_name,
            "email": email,
            "password": "Password123"
        }

        r = requests.post(API_USERS, json=payload)

        if r.status_code in (200, 201):
            user_ids.append(r.json()["id"])
            print(f"✅ Usuario creado: {full_name} ({email})")
        else:
            print("❌ Error usuario:", r.status_code, r.text)

    return user_ids


# =============================
# CREAR CATEGORÍAS (RETocadas)
# =============================
def create_categories():
    print("\n📌 Creando categorías...")
    category_ids = {}

    categories = [
        {"name": "Portátiles", "description": "Computadoras ligeras y de alto rendimiento"},
        {"name": "Celulares", "description": "Smartphones modernos"},
        {"name": "Periféricos", "description": "Mouse, teclados y accesorios"},
        {"name": "Pantallas", "description": "Monitores y displays"},
        {"name": "Conectividad", "description": "Routers y redes"},
        {"name": "Gaming", "description": "Equipos y accesorios gamer"},
    ]

    for cat in categories:
        r = requests.post(API_CATEGORIES, json=cat)

        if r.status_code in (200, 201):
            cat_id = r.json()["id"]
            category_ids[cat["name"]] = cat_id
            print(f"✅ Categoría creada: {cat['name']}")
        else:
            print("❌ Error categoría:", r.status_code, r.text)

    return category_ids


# =============================
# GENERAR NOMBRE BUSCABLE
# =============================
def generate_product_name():
    return f"{random.choice(PRODUCT_TYPES)} {random.choice(ADJECTIVES)} {random.choice(BRANDS)} {random.choice(SPECS)}"


# =============================
# CREAR PRODUCTOS CON 2 CATEGORÍAS
# =============================
def create_products(user_ids, category_dict):
    print("\n📌 Generando productos masivos...")
    success = 0

    while success < TOTAL_PRODUCTS:

        category_name = random.choice(list(category_dict.keys()))
        primary_category_id = category_dict[category_name]

        if category_name == "Portátiles":
            name = f"Notebook {random.choice(BRANDS)} {random.choice(SPECS)}"
            related_categories = ["Gaming"]

        elif category_name == "Celulares":
            name = f"Phone {random.choice(BRANDS)} 128GB"
            related_categories = ["Periféricos"]

        elif category_name == "Periféricos":
            name = f"{random.choice(['Mouse', 'Teclado', 'Headset'])} {random.choice(BRANDS)}"
            related_categories = ["Gaming"]

        elif category_name == "Pantallas":
            name = f"Monitor {random.choice(BRANDS)} 24 pulgadas"
            related_categories = ["Gaming"]

        elif category_name == "Conectividad":
            name = f"Router {random.choice(BRANDS)} Dual Band"
            related_categories = ["Gaming"]

        elif category_name == "Gaming":
            name = f"Gamer {random.choice(['Notebook', 'Mouse', 'Teclado'])} RGB"
            related_categories = ["Portátiles", "Periféricos"]

        else:
            name = "Producto Genérico"
            related_categories = []

        possible_second = [
            category_dict[cat]
            for cat in related_categories
            if cat in category_dict
        ]

        if possible_second:
            category_ids = [primary_category_id, random.choice(possible_second)]
        else:
            category_ids = [primary_category_id]

        payload = {
            "name": f"{name} #{success}{random.randint(1000,9999)}",
            "price": round(random.uniform(50, 3000), 2),
            "description": f"{category_name} premium de alto rendimiento",
            "userId": random.choice(user_ids),
            "categoryIds": category_ids
        }

        r = requests.post(API_PRODUCTS, json=payload)

        if r.status_code in (200, 201):
            success += 1
            if success % 100 == 0:
                print(f"🚀 {success} productos creados...")
        else:
            print("❌ Error producto:", r.status_code, r.text)

        time.sleep(0.02)

    print("\n🎉 1000 productos creados correctamente.")


# =============================
# MAIN
# =============================
if __name__ == "__main__":

    print("=================================")
    print("  GENERADOR MASIVO DE DATA DEMO  ")
    print("=================================")

    users = create_users()
    categories = create_categories()

    if users and categories:
        create_products(users, categories)
    else:
        print("❌ Error: No se pudieron crear usuarios o categorías.")
