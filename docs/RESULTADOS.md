# Resultados de ejecución

Fecha: 23/10/2026. Equipo Windows, Java 21, Chrome headless.

## DEMO local

Ambiente: http://127.0.0.1:8765. Simulación de soporte, sin conexión transaccional al comercio.

- CP-01 Registrar una cuenta con datos válidos: **APROBADO**.
- CP-02 Persistir una modificación del perfil: **APROBADO**.
- CP-03 Completar una compra con pago de demostración: **APROBADO**.

Total: 3 aprobados de 3. Fallidos: 0.

Evidencia: `evidencias/Demo/serenity/index.html`, `evidencias/Demo/cucumber.json` y XML de Failsafe.

## Sitio público

Ambiente: https://www.bon-bonite.com. Navegación de lectura, sin enviar formularios ni crear pedidos.

- SM-01 Consultar el registro y un producto en el sitio público: **APROBADO**.

Total: 1 aprobados de 1. Fallidos: 0.

Evidencia: `evidencias/Publico/serenity/index.html`, `evidencias/Publico/cucumber.json` y XML de Failsafe.

## Regresión Java sobre Bon-Bonite

- CP-01-R Reingresar con la cuenta creada en el sitio real: **APROBADO**.
- CP-02-R Guardar y recuperar el nombre tras reautenticarse: **APROBADO**.
- CP-03-P Verificar la compra hasta el checkout sin crear pedido: **APROBADO**.

Total seleccionado: 3 aprobados de 3. Fallidos: 0.

Evidencia: `evidencias/Real/serenity/index.html`, `evidencias/Real/cucumber.json` y XML de Failsafe. Capturas automáticas reales desactivadas para evitar publicar información privada.

## Resultado funcional de los tres requisitos

- CP-01: APROBADO. Alta real mediante automatización de navegador, sesión iniciada y reingreso comprobado. La variante Java de alta nueva no se reejecutó con la cuenta existente.
- CP-02: FALLIDO por BUG-01. El nombre se guarda y persiste, pero guardar asignó Masculino al género que estaba vacío sin haber seleccionado ese cambio. El nombre original quedó restaurado.
- CP-03: PARCIAL. Producto, talla 37, dos unidades, subtotal COP 655800 y checkout verificados. La confirmación completa está BLOQUEADA: faltan facturación y condiciones de pedido/pago de prueba. No se pulsó Registrar orden.
La aprobación de CP-03-P en el reporte solo certifica ese tramo previo al envío de la orden.

Ningún resultado de la DEMO debe interpretarse como una compra completada en Bon-Bonite.

## Alcance de las evidencias

Los tres escenarios de DEMO verifican que las acciones y aserciones del proyecto funcionan contra la simulación incluida.
El escenario público verifica visibilidad de los campos de registro, tipos de controles y consulta del producto. Las ejecuciones reales autenticadas se documentan aparte.
Se documentaron BUG-01 (género modificado indirectamente) y BUG-02 (error transitorio de base de datos recuperado al recargar). Ver docs/DEFECTOS.md para límites de reproducción.

Repositorio público de entrega: https://github.com/Juanvanegas08/bonbonite-qa-prueba-tecnica . La URL está incluida en Repositorio C5 del Excel.

## Revisión final de la entrega

Se compiló y ejecutó una copia limpia del código: `mvn clean verify` seleccionó por defecto los tres casos DEMO y terminó con BUILD SUCCESS (3/3 aprobados). La selección explícita `-Dcucumber.filter.tags=@publico` también terminó con BUILD SUCCESS (1/1 aprobado). No se repitieron cambios en la cuenta real durante esta revisión.

Se revisaron las seis pestañas del Excel, las capturas reales, la concordancia de los resultados con Cucumber JSON y los archivos de los reportes. Se retiraron enlaces opcionales CSV sin archivo de destino mediante `tools/finalize-report.ps1`, sin modificar los resultados de ejecución. La compra real continúa parcial y BUG-01 continúa abierto.
