# language: es
@modulos
Característica: Cobertura complementaria de módulos reales
  Esquema del escenario: CAT-<id> Consultar catálogo y producto de <modulo>
    Dado que se consulta el catálogo "<ruta>"
    Entonces un producto del catálogo conserva nombre precio e imagen en su ficha
    Ejemplos:
      | id | modulo | ruta |
      | 01 | Zapatos | zapatos-mujer |
      | 02 | Bolsos | bolsos-mujer |
      | 03 | Cinturones | cinturones-mujer |
      | 04 | Accesorios | accesorios-mujer |
      | 05 | Outlet | outlet |

  Escenario: PQ-01 Correo inválido y opciones de solicitud PQRS
    Dado que se consulta PQRS
    Entonces el correo inválido no cumple la validación y existen tipos de solicitud

  Escenario: BO-01 Selección de denominación de bono
    Dado que se consulta el bono
    Entonces seleccionar una denominación resuelve su variante

  Escenario: AC-01 Acceso y registro Mi cuenta Account
    Dado que Juan consulta el formulario público de registro
    Entonces identifica los campos obligatorios sin enviar datos
