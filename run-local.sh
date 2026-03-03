#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if docker compose version >/dev/null 2>&1; then
  COMPOSE_CMD=(docker compose)
elif command -v docker-compose >/dev/null 2>&1; then
  COMPOSE_CMD=(docker-compose)
else
  echo "Docker Compose is not installed."
  echo "Install one of:"
  echo "  - docker compose plugin"
  echo "  - docker-compose"
  exit 1
fi

ACTION="${1:-start}"

run_compose() {
  "${COMPOSE_CMD[@]}" -f "$ROOT_DIR/docker-compose.yml" "$@"
}

case "$ACTION" in
  start)
    run_compose up --build
    echo ""
    echo "Backend is running in foreground (Ctrl+C to stop)."
    echo "Host URL: http://localhost:8080/openMinds/phpFile/"
    echo "Android emulator URL: http://10.0.2.2:8080/openMinds/phpFile/"
    echo ""
    echo "Quick test:"
    echo "curl -i -H \"X-Api-Key: testAPIKEY\" \"http://localhost:8080/openMinds/phpFile/mailExist.php?email=test@example.com\""
    ;;
  start-detached)
    run_compose up -d --build
    ;;
  stop)
    run_compose down
    ;;
  reset)
    run_compose down -v
    run_compose up -d --build
    ;;
  logs)
    run_compose logs -f web
    ;;
  status)
    run_compose ps
    ;;
  *)
    echo "Unknown action: $ACTION"
    echo "Usage: $0 [start|start-detached|stop|reset|logs|status]"
    exit 1
    ;;
esac
