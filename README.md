# Prueba técnica QA · Bon-Bonite

Juan José Vanegas. Inicio: 23/10/2026. Entrega prevista: 24/10/2026.

Java 21, Maven, Serenity BDD 5.3.8, Selenium WebDriver, Cucumber 7.34.2 y Screenplay.

## Qué se entregó y qué se comprobó

El Excel está en `entrega/PTP_Juan_Vanegas_23102026.xlsx`. Contiene alcance, los tres casos, riesgos/defectos y la celda para el repositorio.

Se creó una cuenta real con autorización del candidato mediante automatización de navegador. Se verificó el reingreso, se modificó temporalmente el nombre y se comprobó su persistencia tras reautenticarse. El nombre original quedó restaurado. Se verificaron producto, talla 37, dos unidades, subtotal y checkout reales.

**La compra completa sigue pendiente:** el botón «Registrar orden» genera una orden del comercio. No se pulsó; no se pagó ni se reservó producto. No se proporcionó pago sandbox ni datos completos de facturación. Llegar al checkout no equivale a una compra confirmada.

La prueba funcional de perfil detectó un defecto: un género inicialmente vacío pasa a Masculino al guardar solo el nombre. Por ello el resultado funcional CP-02 es FALLIDO, aunque la regresión automatizada de persistencia del nombre pase. Ver `docs/DEFECTOS.md` y `docs/RESULTADOS.md`.

La DEMO local se conserva como soporte para practicar los tres flujos completos con pago ficticio. Sus resultados se distinguen de las ejecuciones reales.

## Ejecutar en Windows

`mvn clean verify` ejecuta por defecto solo los tres escenarios de la DEMO local. Las pruebas del comercio real y el registro de otra cuenta se seleccionan expresamente con sus etiquetas y precondiciones. No necesitan credenciales para revisar la DEMO.

Requisitos: JDK 21, Maven 3.9.11 o superior y Chrome. En el equipo de preparación el script detecta JDK/Maven en `%LOCALAPPDATA%/Programs/QA-Tools`. En otro equipo configura JAVA_HOME y Maven en PATH. La primera ejecución descarga dependencias y ChromeDriver.

```powershell
powershell -ExecutionPolicy Bypass -File .\tools\run.ps1 -Modo Demo
powershell -ExecutionPolicy Bypass -File .\tools\run.ps1 -Modo Publico
```

Para la regresión sobre la cuenta real, define QA_LOGIN (cédula) y QA_PASSWORD como variables de entorno de la sesión. El script no las almacena. Puedes pedir la contraseña sin mostrarla:

```powershell
$env:QA_LOGIN = Read-Host 'Identificador de la cuenta autorizada'
$qaSecret = Read-Host 'Contraseña' -AsSecureString
$env:QA_PASSWORD = [System.Net.NetworkCredential]::new('', $qaSecret).Password
try {
  powershell -ExecutionPolicy Bypass -File .\tools\run.ps1 -Modo Real
} finally {
  Remove-Item Env:\QA_LOGIN, Env:\QA_PASSWORD
}
```

También puedes ejecutar Maven, con esas variables ya definidas:

```shell
mvn clean verify -Dcucumber.filter.tags="@real" -Dserenity.take.screenshots=DISABLED
```

`Real` selecciona tres regresiones: acceso y consulta de datos; persistencia del nombre con restauración; carrito y checkout parcial. No pulsa `place_order`. La prueba de compra agrega el producto configurado y retira esa línea al terminar. Ejecútala con un carrito de pruebas sin compras personales del mismo producto.

Las capturas automáticas de la ejecución real se desactivan para evitar que datos personales aparezcan en las evidencias que se publican. Se incluyen capturas del navegador sin cédula ni contraseña y un acta de pasos. Los resultados medidos están en `docs/RESULTADOS.md`.

Los reportes quedan por modo en `evidencias/Demo/serenity/index.html`, `evidencias/Publico/serenity/index.html` y `evidencias/Real/serenity/index.html`. La selección de etiquetas puede mostrar escenarios omitidos en el XML de JUnit; consulta los escenarios seleccionados en Cucumber y Serenity.

Descarga o clona el repositorio y abre esos archivos `index.html` desde la carpeta local; GitHub muestra el código HTML y no ejecuta el reporte. `tools/finalize-report.ps1` retira únicamente los enlaces opcionales a exportaciones CSV inexistentes. Los resultados Cucumber JSON, Failsafe XML y Serenity JSON se conservan sin cambios.

## Registro inicial de otra cuenta

La cuenta de esta entrega ya existe. No repitas su registro como si fueran datos nuevos. `registro_real.feature` contiene el flujo reutilizable Java de alta y reingreso. Está separado de `@real` y requiere datos únicos proporcionados por su titular y autorización explícita.

Solo para un nuevo registro autorizado, define QA_REGISTER_APPROVED=SI, QA_LOGIN, QA_PASSWORD, QA_EMAIL, QA_FIRST_NAME y QA_LAST_NAME, y ejecuta la etiqueta `@registro_nuevo`. Ese flujo acepta el tratamiento de datos personales y crea una cuenta en el comercio real. La variante Java de alta no se volvió a ejecutar con el mismo usuario porque sería un registro duplicado; el alta realizada está documentada en el acta y la captura 01.

## Organización

- `features`: negocio DEMO, exploración pública, regresión real y alta inicial opcional.
- `steps`: vincula Gherkin con tareas y aserciones.
- `tasks`: CustomerTasks y PurchaseTasks para DEMO; RealTasks para el comercio real.
- `questions`: lecturas de estado y valores. RealTasks también expone preguntas de perfil y subtotal.
- `ui`: StorePage para DEMO y RealPage con controles observados en el sitio real.
- `support`: configuración y servidor DEMO; no reproduce reglas internas del comercio.

El actor tiene BrowseTheWeb. Las tareas separan acciones de negocio, las preguntas leen resultados y las aserciones contrastan valores. Se usan esperas explícitas sin Thread.sleep. Los selectores de perfil real difieren de los WooCommerce genéricos: el sitio usa `profile-update-form`, `input[name=first_name]` y guardado asíncrono.

No hay credenciales ni cédula dentro del repositorio. El código de salida de Maven y los reportes son la evidencia de ejecución. Los casos negativos, API, carga y regresión exhaustiva de todos los módulos quedan como ampliación.

## Publicación

Repositorio público de entrega: [bonbonite-qa-prueba-tecnica](https://github.com/Juanvanegas08/bonbonite-qa-prueba-tecnica). El enlace está incluido en `Repositorio!C5` del Excel. El evaluador puede consultar el código y las evidencias sin invitación. La publicación fue autorizada por el candidato.

En Eclipse: File > Import > Maven > Existing Maven Projects. Selecciona esta carpeta y JDK 21.

## Fuentes

Enunciado: capturas facilitadas por el candidato. Sitio observado: https://www.bon-bonite.com/ . Referencias: https://serenity-bdd.github.io/docs/screenplay/screenplay_fundamentals y https://serenity-bdd.github.io/docs/guide/cucumber . Versiones de referencia: https://github.com/serenity-bdd/serenity-cucumber-starter/blob/master/pom.xml .
