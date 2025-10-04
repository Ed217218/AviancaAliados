Feature: yo como usuario valido Solicitud de bloqueos

  @Regresion
    @HU002
  Scenario Outline: Solicitud de bloqueos
    Given abrir el navegador
    And el usuario diligenica usuario <email> diligencia password <password>
    When El usuario navega a Nueva Solicitud



    Examples:
      | email                       | password    |
      | patest240221@yopmail.com    | TestAv2024% |

