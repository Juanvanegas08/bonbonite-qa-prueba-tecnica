# Defectos y observaciones del sitio real

Fecha: 23/10/2026. Ambiente: sitio público Bon-Bonite, cuenta autorizada del candidato. Los datos de acceso se omiten.

## BUG-01 · Guardar el nombre asigna un género que no fue seleccionado

- Severidad propuesta: media. Prioridad propuesta: alta por integridad de datos personales.
- Precondición: cuenta recién creada; el perfil muestra Género sin valor.
- Pasos: abrir Datos; pulsar Actualizar información; cambiar solamente Nombres; pulsar Guardar; cerrar sesión; volver a ingresar; consultar Datos.
- Esperado: el nombre cambia y los campos no modificados conservan su estado, incluido Género vacío.
- Real: el nombre persiste, pero Género aparece como Masculino aunque no se interactuó con ese campo.
- Evidencia: observación del estado anterior en el árbol de accesibilidad; el selector de edición tenía Masculino seleccionado por defecto, sin opción vacía. Estado posterior persistido y acta en `evidencias/Real/ACTA.md`.
- Frecuencia: observado una vez en la cuenta recién creada. No se crearon cuentas adicionales para repetirlo. La regresión Java posterior valida persistencia del nombre sobre el estado ya guardado; no reproduce la precondición vacía.
- Estado: abierto. Causa raíz no determinada. Hipótesis a investigar: valor predeterminado del selector al editar.

Se restauró el nombre original al terminar. El género quedó Masculino como efecto del defecto. La interfaz observada solo ofrece Masculino, Femenino y Otro, sin opción vacía para restaurar el estado anterior.

## BUG-02 · Error transitorio de conexión a base de datos en carrito

- Severidad propuesta: alta durante la incidencia. Prioridad propuesta: media, sujeta a frecuencia e impacto.
- Pasos observados: con dos unidades en carrito, abrir checkout; volver al carrito mediante Atrás del navegador.
- Esperado: el carrito carga y conserva su contenido.
- Real: página «Database Error» con «Error establishing a database connection».
- Evidencia: `evidencias/Real/05-error-base-datos.png`.
- Frecuencia: una ocurrencia observada. Se recuperó al recargar una vez; el carrito mantuvo dos unidades y COP 655800.
- Estado: pendiente de investigación. No se atribuye una causa técnica ni se afirma reproducción consistente.
