# Mapa del Proyecto: tareas-api

## 1. Qué hace el proyecto

Gestor de tareas pendientes en Java 17 con repositorio en memoria. Permite crear, completar, eliminar y listar tareas con prioridad y fecha límite, generando reportes de estado.

## 2. Tabla de clases

| Clase | Responsabilidad | Depende de |
|-------|----------------|------------|
| `App` | Demo de consola que crea tareas y muestra el reporte | `TareaServicio`, `TareaRepositorio`, `Prioridad` |
| `Prioridad` | Enum con los niveles de prioridad (BAJA, MEDIA, ALTA) | — |
| `Tarea` | Entidad que representa una tarea con título, descripción, prioridad, fecha límite y estado | `Prioridad` |
| `TareaNoEncontradaException` | Excepción lanzada cuando se busca una tarea inexistente | — |
| `TareaRepositorio` | Repositorio en memoria que guarda y busca tareas, asigna ids consecutivos | `Tarea` |
| `TareaServicio` | Casos de uso: crear, completar, eliminar, listar y generar reportes de tareas | `TareaRepositorio`, `Tarea`, `Prioridad`, `TareaNoEncontradaException` |
| `TareaRepositorioTest` | Pruebas del repositorio: guardar, buscar, eliminar, búsqueda por título | `TareaRepositorio`, `Tarea`, `Prioridad` |
| `TareaServicioTest` | Pruebas de los casos de uso: crear, completar, listar, reportes, eliminar | `TareaServicio`, `TareaRepositorio`, `Tarea`, `Prioridad` |

## 3. Cómo se compila y se prueba

```bash
# Compilar el proyecto
mvn compile

# Correr todas las pruebas
mvn test

# Correr la demo de consola
mvn -q exec:java

# Limpiar y recompilar
mvn clean compile
```

## 4. Tres cosas sospechosas o incompletas

### 1. Validación de título vacío no implementada
**Archivo:** `src/main/java/mx/generation/tareas/Tarea.java:19`  
**Descripción:** Hay un TODO que indica que se debe validar que el título no venga vacío ni en blanco, pero actualmente se acepta `""` como título válido.  
**Estado:** ✅ **Confirmada** — El TODO existe en la línea 19 y la validación no está implementada.

### 2. Búsqueda por título no ignora mayúsculas
**Archivo:** `src/main/java/mx/generation/tareas/TareaRepositorio.java:44`  
**Descripción:** El método `buscarPorTitulo()` usa `contains()` directamente sin convertir a minúsculas, lo que hace que no sea case-insensitive. Hay una prueba deshabilitada en `TareaRepositorioTest.java:33-39` (`buscarPorTituloIgnoraMayusculas`) que confirma este fallo.  
**Estado:** ✅ **Confirmada** — La línea 44 usa `contains()` sin `toLowerCase()`, y la prueba deshabilitada documenta el bug.

### 3. TODO obsoleto sobre prueba ya implementada
**Archivo:** `src/test/java/mx/generation/tareas/TareaServicioTest.java:40`  
**Descripción:** El comentario `// TODO: falta probar que completar(999) lanza TareaNoEncontradaException` está obsoleto porque la prueba `completarInexistenteLanzaExcepcion()` ya existe en las líneas 43-45.  
**Estado:** ✅ **Confirmada** — El TODO en línea 40 está obsoleto; la prueba existe en línea 43-45.
