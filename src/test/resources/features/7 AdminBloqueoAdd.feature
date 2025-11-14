Feature: Yo como usuario consulto y modifico los bloqueos

  @Regresion
    @HU007
  Scenario Outline: Modificacion de bloqueos
    Given abrir el navegador

    And  Iniciar sesion como administrador <emailAdmin> <passwordAdmin>
    
    And El usuario navega a  Administracion de Bloqueos y Bloqueos Creados

    And El usuario busca el bloqueo creado previamente con fechas <fechaInicial> <fechaFinal>
    And El usuario selecciona el bloqueo
    When El usuario modifica el bloqueo con asientos <asientos>
    Then El usuario valida que el ajuste del bloqueo fue exitoso


    Examples:
  | emailAdmin                | passwordAdmin   | fechaInicial | fechaFinal | asientos |
  | jesus.perdomo@avianca.com | calixtoH2017$   | 2026-01-04   | 2026-01-10 | 10       |






  

