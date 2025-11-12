Feature: Yo como usuario consulto y modifico los bloqueos

  @Regresion
    @HU005
  Scenario Outline: Modificacion de bloqueos
    Given abrir el navegador
    And el usuario diligenica usuario <email> diligencia password <password>
    And El usuario navega a bloqueos
    And El usuario busca el bloqueo creado previamente con fechas <fechaInicial> <fechaFinal>
    And El usuario selecciona el bloqueo
    When El usuario modifica el bloqueo con asientos <asientos>
    Then El usuario valida que la modificacion del bloqueo fue exitosa


    Examples:
  | email                    | password    | fechaInicial | fechaFinal | asientos |
  | patest240221@yopmail.com | TestAv2024% | 2026-01-04   | 2026-01-10 | 10       |