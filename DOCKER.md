# OpenMinds with Docker

This setup runs the PHP API and MariaDB for local Android development.

## Start

```bash
docker compose up -d --build
```

or with helper scripts:

- Linux/macOS: `./run-local.sh` (foreground, `Ctrl+C` to stop)
- PowerShell: `./run-local.ps1` (foreground, `Ctrl+C` to stop)
- Windows cmd: `run-local.bat` (foreground, `Ctrl+C` to stop)
- Detached mode (optional): `start-detached`

API base URL from your host machine:

`http://localhost:8080/openMinds/phpFile/`

API base URL from Android emulator:

`http://10.0.2.2:8080/openMinds/phpFile/`

## Stop

```bash
docker compose down
```

or:

- `./run-local.sh stop`
- `./run-local.ps1 stop`
- `run-local.bat stop`

Detached start (if needed):

- `./run-local.sh start-detached`
- `./run-local.ps1 start-detached`
- `run-local.bat start-detached`

## Reset database

```bash
docker compose down -v
docker compose up -d --build
```

or:

- `./run-local.sh reset`
- `./run-local.ps1 reset`
- `run-local.bat reset`

## Notes

- DB and API credentials are defined in `docker-compose.yml` via environment variables.
- `phpFile/bdd.php` now reads DB credentials from env vars.
- `phpFile/Api.php` now reads API key from env vars.
- Initial tables are created by `docker/mysql/init/01-schema.sql`.
- Default seeded user: `admin@openminds.local` / `admin`.
