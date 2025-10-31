Feature: yo como usuario valido Solicitud de bloqueos

  @Regresion
    @HU003
  Scenario Outline: Solicitud de bloqueos
  # ===== FASE 1: CREAR SOLICITUD COMO USUARIO =====
    Given abrir el navegador
    And el usuario diligenica usuario <email> diligencia password <password>
    When El usuario navega a Nueva Solicitud
    And El usuario diligencia el formulario de solicitud de bloqueos <solicitante> <tourOperador> <negocio>
    And El usuario diligencia el formulario de informacion del vuelo <aerolinea> <numeroVuelo> <origen> <destino> <fechaInicial> <fechaFinal> <asientos>
    And Diligenciar frcuencia de vuelo
    And Agregar bloqueo
    And El usuario hace clic en Enviar
    And El usuario valida que la solicitud de bloqueo fue creada exitosamente
  
  # ===== FASE 2: CERRAR SESIÓN DEL USUARIO =====
    When Cerrar sesion
     
  # ===== FASE 3: APROBAR SOLICITUD COMO ADMINISTRADOR =====
    And Iniciar sesion como administrador <emailAdmin> <passwordAdmin>
    And Bandeja de solicitudes de bloqueo
    And Gestionar la solicitud de bloqueo creada
    And Aprobar la solicitud de bloqueo creada

    Examples:
  | email                     | password      | solicitante | tourOperador  | negocio | aerolinea | numeroVuelo | origen    | destino       | fechaInicial | fechaFinal | asientos |  emailAdmin                | passwordAdmin |
  | patest240221@yopmail.com  | TestAv2024%   | Test3       | Quasarnautica | Prueba3 | AV        | 1632        | UIO,Quito | "GPS,Baltra"  | 2026-02-04   | 2026-02-10 | 10       |  jesus.perdomo@avianca.com | calixtoH2017$ |
 