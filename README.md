# Automatización Portal Avianca Aliados 🛫

## Descripción del Proyecto 📋

Este proyecto contiene la automatización de pruebas end-to-end para el portal de Aliados de Avianca. Implementa un framework de pruebas automatizadas utilizando Selenium WebDriver, Cucumber y Serenity BDD, siguiendo las mejores prácticas de automatización y el patrón Page Object Model (POM).

## Pre-requisitos 📌

Asegúrate de tener instalado lo siguiente antes de ejecutar el proyecto:

1. **Java Development Kit (JDK)**
   - Versión: 8 o superior
   - [Descargar JDK](https://www.oracle.com/java/technologies/downloads/)
   - Configurar JAVA_HOME en variables de entorno

2. **Maven**
   - Versión: 3.6.0 o superior
   - [Descargar Maven](https://maven.apache.org/download.cgi)
   - Configurar MAVEN_HOME en variables de entorno

3. **Google Chrome**
   - Última versión estable
   - ChromeDriver correspondiente (incluido en el proyecto)

4. **IDE Recomendado**
   - Visual Studio Code con extensiones:
     - Java Extension Pack
     - Cucumber (Gherkin) Full Support
     - Maven for Java

## Estructura del Proyecto 🚀

```
AviancaAliados/
├── src/
│   └── test/
│       ├── java/
│       │   └── Avianca/
│       │       ├── Definitions/     # Step Definitions de Cucumber
│       │       ├── Pages/           # Page Objects 
│       │       ├── Steps/           # Steps de Serenity
│       │       └── Test/            # Runners de pruebas
│       └── resources/
│           └── features/            # Archivos .feature de Cucumber
├── Drivers/                         # WebDrivers
├── target/
│   └── site/
│       └── serenity/               # Reportes generados
├── pom.xml                         # Dependencias Maven
└── README.md
```

## Instalación y Configuración 🔧

1. **Clonar el Repositorio**
   ```bash
   git clone https://github.com/Ed217218/AviancaTest.git
   cd AviancaTest
   ```

2. **Instalar Dependencias**
   ```bash
   mvn clean install
   ```

3. **Verificar Configuración**
   ```bash
   mvn verify
   ```

## Ejecutar las Pruebas ⚡

### Ejecutar Todas las Pruebas
```bash
mvn clean verify
```

### Ejecutar Pruebas por Tags
```bash
mvn clean verify -Dcucumber.filter.tags="@HU001"
```

### Ejecutar Pruebas por Feature Específico
```bash
mvn clean verify -Dcucumber.options="src/test/resources/features/tu_feature.feature"
```

## Estructura de los Features 📝

Los archivos .feature están escritos en Gherkin y siguen esta estructura:

```gherkin
@HU001
Feature: Login al Portal de Aliados
  Como usuario del portal de aliados
  Quiero poder iniciar sesión
  Para acceder a las funcionalidades del sistema

  Scenario: Login exitoso con credenciales válidas
    Given el usuario está en la página de login
    When ingresa credenciales válidas
    Then debería ver la página principal
```

## Reportes 📊

Después de la ejecución, los reportes de Serenity se generan en:
```
target/site/serenity/index.html
```

El reporte incluye:
- Dashboard general
- Detalles de pruebas ejecutadas
- Screenshots de pasos críticos
- Trazabilidad de requerimientos
- Métricas de ejecución

## Convenciones de Código 📌

- **Nombres de Clases**: PascalCase (Ej: `LoginPage`)
- **Nombres de Métodos**: camelCase (Ej: `clickLoginButton()`)
- **Variables**: camelCase (Ej: `userName`)
- **Constantes**: UPPER_CASE (Ej: `BASE_URL`)
- **Features**: snake_case (Ej: `login_portal.feature`)

## Mantenimiento 🔧

### Actualizar WebDriver
1. Descargar la versión correspondiente de ChromeDriver
2. Reemplazar el archivo en la carpeta `Drivers/`
3. Actualizar la versión en `pom.xml` si es necesario

### Agregar Nuevos Features
1. Crear el archivo .feature en `src/test/resources/features/`
2. Implementar los Step Definitions en `src/test/java/Avianca/Definitions/`
3. Crear/Actualizar Page Objects en `src/test/java/Avianca/Pages/`
4. Actualizar el Runner si es necesario

## Control de Versiones 📌

- Versión: 1.0.0
- Última actualización: Septiembre 2025

## Autores ✒️

* **[Tu Nombre]** - *Automatizador QA* - [Ed217218](https://github.com/Ed217218)

## Licencia 📄

Este proyecto está bajo la Licencia [MIT](https://opensource.org/licenses/MIT)

## Contacto 📧

* Email: [tu.email@dominio.com]
* LinkedIn: [Tu perfil de LinkedIn]

---
⌨️ con ❤️ por [Tu Nombre] 😊
# Av
