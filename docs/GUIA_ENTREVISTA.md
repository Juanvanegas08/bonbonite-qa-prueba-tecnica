# Guía para explicar el proyecto

## Presentación

El objetivo fue priorizar registro, actualización de datos y compra. Elegí Java, Serenity BDD, Selenium, Cucumber y Screenplay para separar escenarios de negocio, acciones y verificaciones.

Creé una cuenta real autorizada mediante automatización del navegador y comprobé el reingreso. En el perfil validé el guardado y la persistencia del nombre al cerrar e iniciar sesión, y restauré el nombre original. Encontré que el guardado asignaba Masculino a un género previamente vacío: el caso funcional se considera fallido por ese efecto colateral aunque la persistencia del nombre funcione.

En compra comprobé producto, talla, precio, dos unidades y subtotal de COP 655800, además de su conservación en checkout. No presento ese recorrido como compra completa: no registré una orden real y no recibí medio de pago sandbox ni datos completos de facturación.

La demo permite practicar una compra con confirmación ficticia, pero sus resultados están separados de los del comercio real. También registré un error transitorio de base de datos al volver al carrito, con captura y recuperación al recargar.

## Qué mostrar

1. Excel: alcance y resultado de cada caso, incluyendo el defecto y el bloqueo final de compra.
2. `real.feature`: tres regresiones de la cuenta real. Explicar que CP-03-P comprueba solo el tramo hasta checkout.
3. `RealTasks`: acceso, perfil, preguntas de persistencia, stock del producto y cálculo del carrito.
4. Reporte `evidencias/Real/serenity/index.html`: resultados medidos del código.
5. `docs/DEFECTOS.md`: esperado frente a real, evidencia y límites de reproducción.

## Respuestas que debes comprender

**¿Qué hace Screenplay?** El actor Juan usa BrowseTheWeb. Las tareas agrupan objetivos de negocio y las preguntas consultan valores para las aserciones.

**¿Cómo verificas persistencia?** Guardo, cierro sesión, vuelvo a ingresar y leo el valor nuevamente. No basta con ver lo recién escrito.

**¿Cómo validas la compra?** Verifico selección y disponibilidad de talla, producto, cantidad, precio unitario y multiplicación para el subtotal. Después comparo el subtotal del checkout. La orden y el pago quedan pendientes, claramente identificados.

**¿Por qué no tres escenarios verdes significan tres requisitos aprobados?** Una regresión de un tramo puede pasar mientras otro comportamiento funcional falla o falta un paso final. El Excel distingue ejecución de código y resultado funcional.

**¿Cómo manejas secretos?** QA_LOGIN y QA_PASSWORD llegan como variables de entorno. No se publican en features ni en código. Las capturas automáticas reales están desactivadas para evitar divulgar datos.

**¿Qué ampliarías?** Registro duplicado, contraseñas inválidas, stock agotado, límites de cantidad, pagos rechazados y regresión por módulos. APIs solo con contratos y accesos autorizados.

**¿Usaste IA?** Sí, como apoyo para estructurar y revisar. Estudia las clases y explica qué comprobaciones se ejecutaron. Esta práctica no equivale a años de experiencia profesional.

## Antes de enviar

Verifica el repositorio público https://github.com/Juanvanegas08/bonbonite-qa-prueba-tecnica y la URL incluida en Repositorio C5 del Excel. Ejecuta la demo y revisa al menos un reporte real. El enunciado exige una compra; comunica expresamente que su confirmación requiere condiciones de prueba acordadas.
