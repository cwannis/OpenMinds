@echo off
echo Starting OpenMinds backend...
docker compose up -d --build

echo.
echo Backend started!
echo API URL: http://localhost:8080/openMinds/phpFile/
echo.
echo To stop the backend, run:
echo   docker compose down
echo.
echo To reset the database (wipe + reseed), run:
echo   docker compose down -v ^&^& docker compose up -d --build
pause
