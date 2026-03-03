@echo off
setlocal EnableExtensions EnableDelayedExpansion

set "ROOT_DIR=%~dp0"
set "COMPOSE_FILE=%ROOT_DIR%docker-compose.yml"
set "ACTION=%~1"
if "%ACTION%"=="" set "ACTION=start"

docker compose version >nul 2>&1
if %ERRORLEVEL%==0 (
  set "COMPOSE_CMD=docker compose"
) else (
  where docker-compose >nul 2>&1
  if %ERRORLEVEL%==0 (
    set "COMPOSE_CMD=docker-compose"
  ) else (
    echo Docker Compose is not installed.
    echo Install Docker Desktop/Compose plugin or docker-compose.
    exit /b 1
  )
)

if /I "%ACTION%"=="start" (
  %COMPOSE_CMD% -f "%COMPOSE_FILE%" up --build
  echo.
  echo Backend is running in foreground (Ctrl+C to stop).
  echo Host URL: http://localhost:8080/openMinds/phpFile/
  echo Android emulator URL: http://10.0.2.2:8080/openMinds/phpFile/
  echo.
  echo Quick test:
  echo curl -i -H "X-Api-Key: testAPIKEY" "http://localhost:8080/openMinds/phpFile/mailExist.php?email=test@example.com"
  exit /b 0
)

if /I "%ACTION%"=="start-detached" (
  %COMPOSE_CMD% -f "%COMPOSE_FILE%" up -d --build
  exit /b %ERRORLEVEL%
)

if /I "%ACTION%"=="stop" (
  %COMPOSE_CMD% -f "%COMPOSE_FILE%" down
  exit /b %ERRORLEVEL%
)

if /I "%ACTION%"=="reset" (
  %COMPOSE_CMD% -f "%COMPOSE_FILE%" down -v
  if not %ERRORLEVEL%==0 exit /b %ERRORLEVEL%
  %COMPOSE_CMD% -f "%COMPOSE_FILE%" up -d --build
  exit /b %ERRORLEVEL%
)

if /I "%ACTION%"=="logs" (
  %COMPOSE_CMD% -f "%COMPOSE_FILE%" logs -f web
  exit /b %ERRORLEVEL%
)

if /I "%ACTION%"=="status" (
  %COMPOSE_CMD% -f "%COMPOSE_FILE%" ps
  exit /b %ERRORLEVEL%
)

echo Unknown action: %ACTION%
echo Usage: %~nx0 [start^|start-detached^|stop^|reset^|logs^|status]
exit /b 1
