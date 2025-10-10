Feature: yo como usuario valido Solicitud de bloqueos

  @Regresion
    @HU002
  Scenario Outline: Solicitud de bloqueos
    Given abrir el navegador
    And el usuario diligenica usuario <email> diligencia password <password>
    When El usuario navega a Nueva Solicitud
    And El usuario diligencia el formulario de solicitud de bloqueos <solicitante> <tourOperador> <negocio>
    And El usuario diligencia el formulario de informacion del vuelo <aerolinea> <numeroVuelo> <origen> <destino> <fechaInicial> <fechaFinal> <asientos>
    And Diligenciar frcuencia de vuelo
    And Agregar bloqueo
    And Eliminacion masiva de bloqueos
    And Agregar nuevamente bloqueo
    And El usuario hace clic en Enviar
   

    Examples:
  | email                    | password    | solicitante | tourOperador  | negocio | aerolinea | numeroVuelo | origen    | destino       | fechaInicial | fechaFinal | asientos |
  | patest240221@yopmail.com | TestAv2024% | Test1       | Quasarnautica | Prueba1 | AV        | 1632        | UIO,Quito | "GPS,Baltra"  | 2025-11-10   | 2025-11-11 | 10       |