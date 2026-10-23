# language: es
@publico
Característica: Exploración pública sin crear datos
  Esta comprobación navega formularios y producto sin enviar solicitudes de registro o compra.

  Escenario: SM-01 Consultar el registro y un producto en el sitio público
    Dado que Juan consulta el formulario público de registro
    Entonces identifica los campos obligatorios sin enviar datos
    Y puede consultar un producto con nombre precio y opciones de talla
