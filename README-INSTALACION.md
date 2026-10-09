# Wake Render Backend

Copia la carpeta `.github` completa en la raíz de tu proyecto `finanzas-backend`.

La estructura final debe quedar:

    finanzas-backend/
    ├── .github/
    │   └── workflows/
    │       └── wake-render.yml
    ├── bd/
    ├── src/
    ├── pom.xml
    └── ...

## Configuración

El workflow llama al endpoint:

    https://finanzas-backend-xap0.onrender.com/api/auth/backend/up

Horarios configurados con zona `America/Lima`:

- 07:50
- 19:50

Hace hasta 10 intentos y espera 30 segundos entre intentos cuando todavía no obtiene HTTP 200.

## Prueba manual

1. Copia `.github` en la raíz del backend.
2. Haz commit y push a GitHub.
3. En GitHub entra a `Actions`.
4. Selecciona `Wake Render Backend`.
5. Pulsa `Run workflow`.
6. Abre la ejecución y revisa los pasos.
7. Cuando obtenga HTTP 200 debe terminar en verde.
8. Comprueba también los logs de Render.

`workflow_dispatch` es lo que habilita la ejecución manual.

No contiene credenciales y no modifica el código Java ni el scheduler de Spring Boot.
