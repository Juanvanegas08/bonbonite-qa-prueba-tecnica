# Acta de ejecución sobre Bon-Bonite

Fecha: 23/10/2026. Método: automatización del navegador y regresión Java/Serenity. Cuenta y tratamiento de datos autorizados por el candidato en el chat.

1. **Registro:** se diligenció el formulario real con los datos proporcionados y se aceptó la autorización de tratamiento de datos tras confirmación explícita. El sitio redirigió a `/mi-cuenta/orders/` y mostró «Hola, Juan». Captura: `01-registro.png`.
2. **Reingreso:** se cerró sesión desde el menú de cuenta. Se volvió a ingresar con la cuenta creada y se abrió Datos. Se comprobó que no dependía únicamente de la sesión posterior al registro.
3. **Actualización:** nombre inicial Juan José. Se guardó Juan José QA, apareció «Datos personales actualizados correctamente», se cerró sesión, se ingresó otra vez y se observó Juan José QA. Después se restauró Juan José y se recargó para comprobarlo.
4. **Defecto de perfil:** antes de editar, Género estaba sin valor. En edición, el selector mostró Masculino por defecto. Sin interactuar con ese selector, guardar el nombre hizo persistir Masculino. No había opción vacía para revertirlo desde la interfaz. Se documentó BUG-01.
5. **Carrito:** producto Zueco en carnaza chocolate, talla 37, precio unitario COP 327900. Se agregó una unidad y se incrementó a dos. El carrito mostró actualización, cantidad 2 y subtotal COP 655800. Captura: `03-carrito.png`.
6. **Checkout:** se abrió Finalizar compra. Se conservaron producto, talla, cantidad 2 y subtotal COP 655800. El sitio indicó IVA estimado incluido de COP 104708 y envío por calcular. Se accedió al paso de facturación. Captura: `04-checkout-resumen.png`.
7. **Límite:** faltaban teléfono, dirección y ubicación de facturación, además de un mecanismo de prueba para confirmar el pedido. No se pulsó Registrar orden ni se efectuó pago. No se considera compra completa aprobada.
8. **Disponibilidad:** al volver del checkout al carrito se observó Database Error. Se guardó `05-error-base-datos.png` y la página se recuperó al recargar una vez.

La regresión Java posterior y sus resultados están en `cucumber.json` y `serenity/index.html`. La creación inicial fue ejecutada con automatización de navegador; la variante Java de registro nuevo se entrega para futuras cuentas con datos únicos autorizados y no se reejecutó con la cuenta existente.

El acta omite correo, cédula, contraseña, cookies y tokens de sesión. Las capturas del perfil que pudieran revelar información privada no se incluyen en el ZIP público.

Al finalizar la regresión Java se recargó el carrito y se comprobó «Tu carrito está vacío». Se volvió a Pedidos y la página mostró «No has realizado ningún pedido aún». Captura final: `06-sin-pedidos.png`.
