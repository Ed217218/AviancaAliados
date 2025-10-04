#Proyecto de pruebas y cargado en Github
#27/01/2020
#Elkin Andres Diaz
Feature: yo como usuario valido login

  @Regresion
    @HU001
  Scenario Outline: Login exitoso
    Given abrir el navegador
    When el usuario diligenica usuario <email> diligencia password <password>
   

    Examples:
      | email                       | password    |
      | patest240221@yopmail.com    | TestAv2024% |
