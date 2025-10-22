# 🔧 CONFIGURACIÓN DE GIT EN VISUAL STUDIO CODE

## ✅ CONFIGURACIÓN COMPLETADA

Git ya está configurado y funcionando en VS Code. Aquí está todo lo que se hizo:

---

## 📋 CAMBIOS REALIZADOS

### 1. ✅ Git agregado al PATH del sistema
```powershell
# Se agregó permanentemente al PATH de usuario
C:\Program Files\Git\bin
```

### 2. ✅ Configuración de VS Code actualizada
**Archivo:** `.vscode/settings.json`

**Configuraciones agregadas:**
```json
{
    "git.enabled": true,
    "git.path": "C:\\Program Files\\Git\\bin\\git.exe",
    "git.autofetch": true,
    "git.confirmSync": false,
    "git.enableSmartCommit": true,
    "git.suggestSmartCommit": true,
    "git.autoStash": true,
    "git.showPushSuccessNotification": true
}
```

---

## 🎯 CÓMO USAR GIT EN VS CODE

### Opción 1: Panel de Control de Código Fuente (Source Control)

1. **Abrir el panel:**
   - Presiona `Ctrl + Shift + G`
   - O haz clic en el ícono de ramificación en la barra lateral izquierda

2. **Realizar cambios:**
   - Verás todos los archivos modificados
   - Haz clic en `+` para hacer stage de archivos
   - Escribe un mensaje de commit arriba
   - Presiona `Ctrl + Enter` o haz clic en ✓ para commitear

3. **Push/Pull:**
   - Haz clic en los tres puntos `...` en el panel
   - Selecciona `Push` o `Pull`

### Opción 2: Terminal Integrada

1. **Abrir terminal:**
   - Presiona `` Ctrl + ` `` (tecla acento grave)
   - O ve a: `Terminal > New Terminal`

2. **Comandos básicos:**
   ```bash
   # Ahora puedes usar git directamente
   git status
   git add .
   git commit -m "mensaje"
   git push
   git pull
   ```

### Opción 3: Paleta de Comandos

1. **Abrir paleta:**
   - Presiona `Ctrl + Shift + P`

2. **Escribir comandos Git:**
   - Escribe "Git: " y verás todas las opciones
   - Por ejemplo: "Git: Push", "Git: Pull", "Git: Commit"

---

## 🚀 COMANDOS GIT MÁS USADOS

### Desde la Terminal de VS Code:

```bash
# Ver estado
git status

# Agregar archivos
git add .                    # Agregar todos
git add archivo.java         # Agregar uno específico

# Hacer commit
git commit -m "mensaje descriptivo"

# Subir cambios
git push origin main

# Descargar cambios
git pull origin main

# Ver historial
git log --oneline

# Ver diferencias
git diff

# Ver ramas
git branch

# Crear rama nueva
git checkout -b nombre-rama

# Cambiar de rama
git checkout main
```

---

## ⚙️ ATAJOS DE TECLADO EN VS CODE

| Atajo | Acción |
|-------|--------|
| `Ctrl + Shift + G` | Abrir Control de Código Fuente |
| `Ctrl + Enter` | Commit (con mensaje escrito) |
| `Ctrl + Shift + P` → "Git: Push" | Push rápido |
| `Ctrl + Shift + P` → "Git: Pull" | Pull rápido |
| `` Ctrl + ` `` | Abrir terminal |
| `Ctrl + K Ctrl + O` | Abrir carpeta en Git |

---

## 🎨 CARACTERÍSTICAS HABILITADAS

### ✅ GitLens (Extensión ya instalada)
- Ver quién modificó cada línea de código
- Ver historial de commits por línea
- Comparar cambios fácilmente

**Cómo usar:**
- Pasa el mouse sobre cualquier línea de código
- Verás información del último commit que modificó esa línea

### ✅ Git Graph (Extensión ya instalada)
- Visualizar el árbol de commits
- Ver ramas gráficamente

**Cómo usar:**
- Presiona `Ctrl + Shift + P`
- Escribe "Git Graph: View Git Graph"
- O haz clic en "Git Graph" en la barra de estado

### ✅ Git History (Extensión ya instalada)
- Ver historial completo de un archivo
- Comparar versiones

**Cómo usar:**
- Clic derecho en un archivo
- Selecciona "Git: View File History"

---

## 🔔 NOTIFICACIONES HABILITADAS

### ✅ Configuraciones automáticas activadas:
- ✅ **Auto fetch:** Git descarga cambios automáticamente cada cierto tiempo
- ✅ **Smart commit:** Hace stage automático antes del commit si no hay archivos staged
- ✅ **Auto stash:** Guarda cambios automáticamente al cambiar de rama
- ✅ **Push notifications:** Te notifica cuando el push es exitoso

---

## 🎯 FLUJO DE TRABAJO RECOMENDADO

### Para cambios rápidos:

1. **Hacer cambios en archivos**

2. **Usar el panel de Source Control:**
   - `Ctrl + Shift + G`
   - Ver archivos modificados
   - Escribir mensaje de commit
   - `Ctrl + Enter` para commitear
   - Clic en `...` → `Push`

### Para cambios complejos:

1. **Abrir terminal integrada:**
   - `` Ctrl + ` ``

2. **Usar comandos Git:**
   ```bash
   git status                           # Ver cambios
   git add archivo1.java archivo2.java  # Agregar específicos
   git commit -m "descripción detallada"
   git push origin main
   ```

---

## 🔍 VISUALIZAR CAMBIOS

### Ver diferencias antes de commit:

1. **En el panel Source Control:**
   - Haz clic en cualquier archivo modificado
   - Se abrirá una vista de diferencias lado a lado
   - Izquierda: versión anterior
   - Derecha: versión nueva

2. **En la terminal:**
   ```bash
   git diff                    # Todos los cambios
   git diff archivo.java       # Cambios de un archivo
   git diff --staged           # Cambios en staging area
   ```

---

## 📊 EXTENSIONES GIT YA INSTALADAS

Tienes estas extensiones Git instaladas:

1. ✅ **GitLens** - Información detallada de Git en el código
2. ✅ **Git History** - Historial visual de archivos
3. ✅ **Git Graph** - Visualización del árbol de commits
4. ✅ **Git Extension Pack** - Paquete completo de utilidades
5. ✅ **GitHD** - Comparación de diferencias mejorada
6. ✅ **Git Blame** - Ver autor de cada línea
7. ✅ **Open in GitHub** - Abrir archivos directamente en GitHub
8. ✅ **Start Git Bash** - Iniciar Git Bash desde VS Code

---

## 🎨 INTERFAZ VISUAL - BARRA DE ESTADO

En la parte inferior de VS Code verás:

- **Rama actual:** `main` (puedes hacer clic para cambiar)
- **Sincronizar cambios:** ↓↑ (flechas con números)
- **Conflictos:** Si hay, aparece un ícono de advertencia

---

## 📝 CONFIGURACIÓN DE USUARIO GIT

Tu configuración actual:

```bash
Usuario: Ed217218
Email: turok217@gmail.com
```

### Cambiar configuración:
```bash
git config --global user.name "Tu Nombre"
git config --global user.email "tu@email.com"
```

---

## 🆘 SOLUCIÓN DE PROBLEMAS

### Si Git no funciona en la terminal:

1. **Cerrar y reabrir VS Code** (para que cargue el nuevo PATH)

2. **O agregar manualmente en la sesión actual:**
   ```powershell
   $env:Path += ";C:\Program Files\Git\bin"
   ```

### Si no ves el panel de Source Control:

1. Presiona `Ctrl + Shift + G`
2. O ve a: `View > Source Control`

### Si no detecta el repositorio:

1. Verifica que estás en la carpeta correcta
2. Ve a: `File > Open Folder`
3. Selecciona la carpeta del proyecto

---

## 📚 RECURSOS ADICIONALES

### Documentación oficial:
- VS Code Git: https://code.visualstudio.com/docs/sourcecontrol/overview
- GitLens: https://gitlens.amod.io/
- Git oficial: https://git-scm.com/doc

### Tutoriales en video:
- Buscar en YouTube: "VS Code Git tutorial"
- Buscar: "GitLens VS Code tutorial"

---

## ✅ VERIFICACIÓN

### Para confirmar que todo funciona:

1. **Abrir terminal en VS Code** (`` Ctrl + ` ``)

2. **Ejecutar:**
   ```bash
   git --version
   # Debería mostrar: git version 2.51.1.windows.1
   
   git status
   # Debería mostrar el estado del repositorio
   ```

3. **Abrir Source Control** (`Ctrl + Shift + G`)
   - Deberías ver el repositorio y los archivos

---

## 🎉 ¡LISTO PARA USAR!

Ahora puedes usar Git completamente integrado en VS Code. Todas las funcionalidades están activas:

- ✅ Comandos Git en terminal
- ✅ Panel de Source Control visual
- ✅ GitLens con información en línea
- ✅ Git Graph para visualización
- ✅ Notificaciones de push/pull
- ✅ Auto-fetch habilitado
- ✅ Diferencias visuales

**¡Disfruta trabajando con Git en VS Code!** 🚀✨

---

**Configurado:** 22 de octubre de 2025  
**Repositorio:** AviancaAliados  
**Estado:** ✅ COMPLETADO
