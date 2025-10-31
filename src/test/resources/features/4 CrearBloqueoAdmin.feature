Feature: yo como usuario creo bloqueos como administrador

  @Regresion
    @HU004
  Scenario Outline: Crear bloqueos en administracion
  # ===== FASE 1: CREAR SOLICITUD COMO USUARIO =====
    Given abrir el navegador
    And  Iniciar sesion como administrador <emailAdmin> <passwordAdmin>
    And Hacer clic en crear bloqueos
  
  # ===== FASE 2: CREACION DE BLOQUEOS =====
    And Diligenciar bloqueos <negocio> <solicitante> <operador>
  
  # ===== FASE 3: CREACION FECUENCIA DE VUELOS ===== 
    And Diligenciar vuelo <aerolinea> <numeroVuelo> <asientos> <clase> <origen> <destino> <fechaInicial> <fechaFinal>  
    And Diligenciar frcuencia de vuelo admin
  
  # ===== FASE 4: AGREGAR BLOQUEO =====
    And Agregar bloqueo admin
  
  # ===== FASE 2: VALIDACION DEL BLOQUEO =====
    And El usuario hace clic en ejecutar
    And El usuario valida que la solicitud de bloqueo fue creada exitosamente admin
 

    Examples:
  | emailAdmin                | passwordAdmin   | negocio | solicitante  | operador      | aerolinea | numeroVuelo |  asientos  | clase | origen    |   destino     | fechaInicial | fechaFinal |
  | jesus.perdomo@avianca.com | calixtoH2017$   | Test3   | Prueba3      | Quasarnautica | AV        | 1632        |     10     |   E   | UIO,Quito | "GPS,Baltra"  | 2026-02-04   | 2026-02-10 |
