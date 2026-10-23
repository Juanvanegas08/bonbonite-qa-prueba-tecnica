# language: es
@e2e
Característica: Registro, actualización y compra
  Los tres escenarios se ejecutan por defecto en la tienda local DEMO.
  La demostración no certifica el funcionamiento de Bon-Bonite en producción.

  @registro
  Escenario: CP-01 Registrar una cuenta con datos válidos
    Dado que Juan abre el registro de clientes
    Cuando registra una cuenta de prueba única con datos válidos
    Entonces puede salir e ingresar con la cuenta creada

  @actualizacion
  Escenario: CP-02 Persistir una modificación del perfil
    Dado que Juan ingresa con una cuenta de prueba existente
    Cuando modifica su nombre y guarda los cambios
    Entonces el nombre actualizado permanece después de cerrar e iniciar sesión

  @compra
  Escenario: CP-03 Completar una compra con pago de demostración
    Dado que Juan ingresa con una cuenta de prueba existente
    Cuando agrega dos unidades de un producto disponible al carrito
    Entonces el carrito conserva producto talla cantidad y subtotal
    Cuando completa el pedido con el medio de pago de demostración
    Entonces recibe una confirmación con número de pedido y total correcto
