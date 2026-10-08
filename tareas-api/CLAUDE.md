# tareas-api

Gestor de tareas pendientes en Java 17 + Maven + JUnit 5. Sin frameworks web ni base de datos: repositorio en memoria.

## Stack

- **Java:** 17
- **Build:** Maven 3.8+
- **Testing:** JUnit 5.10.2
- **Estilo:** Código y pruebas en español

## Comandos

- `mvn compile` — compila el proyecto
- `mvn test` — corre todas las pruebas (deben quedar en verde antes de cualquier commit)
- `mvn -q exec:java` — corre la demo de consola (App.java)
- `mvn clean` — limpia los archivos compilados

## Estructura del proyecto

```
src/main/java/mx/generation/tareas/
├── App.java                        # Demo de consola
├── Prioridad.java                  # Enum BAJA, MEDIA, ALTA
├── Tarea.java                      # Entidad tarea
├── TareaNoEncontradaException.java # Excepción de dominio
├── TareaRepositorio.java           # Repositorio en memoria
└── TareaServicio.java              # Casos de uso

src/test/java/mx/generation/tareas/
├── TareaRepositorioTest.java       # Pruebas del repositorio
└── TareaServicioTest.java          # Pruebas de los casos de uso
```

## Convenciones

- **Idioma:** Código, nombres de variables, métodos, clases y pruebas en español
- **Pruebas:** Una prueba por comportamiento; nombres descriptivos que explican el caso (`completarInexistenteLanzaExcepcion`, no `test1`)
- **Commits:** Mensajes claros en español con prefijo tipo conventional commits (`test:`, `fix:`, `refactor:`, `docs:`)
- **Dependencias:** No agregues dependencias ni plugins al `pom.xml` sin preguntar primero
- **Cambios pequeños:** Un objetivo por rama y por commit; evita cambios que mezclan múltiples responsabilidades
- **Calidad:** Antes de cada commit, asegúrate de que `mvn test` quede en verde (16 pruebas, 1 deshabilitada)

## Reglas importantes

1. **Siempre en verde:** Ningún commit debe dejar las pruebas en rojo
2. **Refactor con red:** Solo refactoriza código que tiene pruebas que fijan su comportamiento
3. **Review antes de commit:** Usa `git diff` para revisar exactamente qué cambió antes de hacer commit
4. **TODOs son documentación:** Los comentarios TODO deben ser específicos y estar actualizados; elimínalos cuando se resuelvan
