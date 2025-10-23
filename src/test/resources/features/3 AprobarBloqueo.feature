Feature: yo como usuario valido Solicitud de bloqueos

  @Regresion
    @HU003
  Scenario Outline: Solicitud de bloqueos
    Given abrir el navegador
    And el usuario diligenica usuario <email> diligencia password <password>
    When El usuario navega a Nueva Solicitud
    And El usuario diligencia el formulario de solicitud de bloqueos <solicitante> <tourOperador> <negocio>
    And El usuario diligencia el formulario de informacion del vuelo <aerolinea> <numeroVuelo> <origen> <destino> <fechaInicial> <fechaFinal> <asientos>
    And Diligenciar frcuencia de vuelo
    And Agregar bloqueo
    And El usuario hace clic en Enviar
    And El usuario valida que la solicitud de bloqueo fue creada exitosamente
    And Cerrar sesion
    #And Iniciar sercion como administrador
    #And Bandeja de solicitudes de bloqueo
    #And Gestionar la solicitud de bloqueo creada
    #And Aprobar la solicitud de bloqueo creada

    Examples:
  | email                    | password    | solicitante | tourOperador  | negocio | aerolinea | numeroVuelo | origen    | destino       | fechaInicial | fechaFinal | asientos |
  | patest240221@yopmail.com | TestAv2024% | Test2       | Quasarnautica | Prueba2 | AV        | 1632        | UIO,Quito | "GPS,Baltra"  | 2026-01-04   | 2026-01-10 | 10       |