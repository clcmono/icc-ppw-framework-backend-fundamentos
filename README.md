# Programación y Plataformas Web

**Estudiante:** Claudia Quevedo
**Correo:** 📧 [Claudia Quevedo](cquevedor@est.ups.edu.ec)
💻 **GitHub:** [Claudia Quevedo](https://github.com/clcmono/icc-ppw-framework-backend-fundamentos.git)


## **9. Resultados y Evidencias Requeridas**

Se verificó que la base de datos se pobló correctamente con los datos de prueba, todas las funcionalidades de paginación, filtros y ordenamiento respondieron como se esperaba, y no se encontraron errores durante las pruebas.
### **9.1. Datos para revisión**

**Usar un dataset de al menos 1000 productos**:
Crear un script de carga masiva para poblar la base de datos con datos variados:
- al menos 5 usuarios
- alemnos 2 categorias por producto  
- Precios variados ($10 - $5000)
- Nombres con texto buscable

### **9.2. Evidencias de funcionamiento** Caputuras de Postman
1. **Page response**: `GET /api/products?page=0&size=5` mostrando metadatos completos
![pageResponse](src/assets/pageResponse.jpeg)

2. **Slice response**: `GET /api/products/slice?page=0&size=5` sin totalElements
![SliceResponse](src/assets/SliceResponse.jpeg)

3. **Filtros + paginación**: `GET /api/products/search?name=laptop&page=0&size=3`
![Filtro y paginacion](src/assets/FiltroPaginacion.jpeg)

4. **Ordenamiento**: `GET /api/products?sort=price,desc&page=1&size=5`
![ordenamiento](src/assets/sort.jpeg)


### **9.3. Evidencias de performance**
1. **Comparación**: Tiempos de respuesta Page vs Slice

**Consultas de prueba con volumen**:
 #### PAGE
1. Primera página de productos (page=0, size=10)
![PAGE1](src/assets/page1.jpeg)
2. Página intermedia (page=5, size=10) 
![page2](src/assets/page2.jpeg)
3. Últimas páginas para verificar performance
![page3](src/assets/page3.jpeg)
4. Búsquedas con pocos y muchos resultados
![page4](src/assets/page4asus.jpeg)
5. Ordenamiento por diferentes campos
![page5price](src/assets/page5price.jpeg)
![page5name](src/assets/page5name.jpeg)


#### SLICE
**Consultas de prueba con volumen**:
1. Primera página de productos (page=0, size=10)
![slice1](src/assets/slice1.jpeg)
2. Página intermedia (page=5, size=10) 
![slice2](src/assets/slice2.jpeg)
3. Últimas páginas para verificar performance
![slice3](src/assets/slicee3.jpeg)
4. Búsquedas con pocos y muchos resultados
![slice4](src/assets/slice4teclado.jpeg)
5. Ordenamiento por diferentes campos
![slice5price](src/assets/slice5price.jpeg)
![slice5name](src/assets/slice5name.jpeg)
