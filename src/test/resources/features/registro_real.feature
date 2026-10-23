# language: es
@registro_nuevo
Característica: Registro inicial autorizado de una cuenta real nueva
  Requiere datos de una cuenta que aún no exista y autorización de tratamiento de datos.
  La cuenta usada en esta entrega ya fue creada con automatización de navegador.

  Escenario: CP-01-N Crear una cuenta nueva autorizada y comprobar el reingreso
    Dado que hay datos únicos y autorización explícita para un nuevo registro real
    Cuando Juan envía el formulario real de registro
    Entonces puede cerrar sesión y entrar con esa cuenta nueva
