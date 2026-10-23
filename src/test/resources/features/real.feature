# language: es
@real
Característica: Regresión sobre la cuenta real autorizada
  Las credenciales se reciben por variables de entorno.
  No se envían órdenes ni pagos reales.

  @acceso_real
  Escenario: CP-01-R Reingresar con la cuenta creada en el sitio real
    Dado que Juan inicia sesión en Bon-Bonite con su cuenta autorizada
    Entonces puede consultar los datos personales de su cuenta real

  @perfil_real
  Escenario: CP-02-R Guardar y recuperar el nombre tras reautenticarse
    Dado que Juan inicia sesión en Bon-Bonite con su cuenta autorizada
    Cuando cambia temporalmente el nombre de su perfil real
    Entonces recupera el nombre modificado tras cerrar e iniciar sesión
    Y restaura el nombre original de su perfil

  @checkout_real
  Escenario: CP-03-P Verificar la compra hasta el checkout sin crear pedido
    Dado que Juan inicia sesión en Bon-Bonite con su cuenta autorizada
    Cuando selecciona una talla disponible y prepara dos unidades en el carrito real
    Entonces el carrito real conserva el producto la talla y el cálculo correcto
    Y el checkout real conserva el subtotal y ofrece el paso de facturación
