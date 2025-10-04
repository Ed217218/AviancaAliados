Feature: validar PIM


  @Regresion
    @HU010
  Scenario Outline: Add employee
    Given abrir el navegador
    And el usuario diligenica usuario <userName> diligencia password <password>
    When el usuario ingresa add employee diligencia el formulario <firstName> <middle> <lastName>

    Examples:
      | userName | password | firstName | middle | lastName |
      | Admin    | admin123 | Jhonatan  | tatan  | medina   |