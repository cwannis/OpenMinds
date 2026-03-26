#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="$ROOT_DIR/docker-compose.yml"
API_TEST_URL="http://localhost:8080/openMinds/phpFile/mailExist.php?email=test@example.com"
API_TEST_KEY="testAPIKEY"

detect_compose() {
  if docker compose version >/dev/null 2>&1; then
    COMPOSE_CMD=(docker compose)
    return
  fi

  if command -v docker-compose >/dev/null 2>&1; then
    COMPOSE_CMD=(docker-compose)
    return
  fi

  echo "Docker Compose is not installed."
  echo "Install one of:"
  echo "  - docker compose plugin"
  echo "  - docker-compose"
  exit 1
}

require_docker() {
  if ! command -v docker >/dev/null 2>&1; then
    echo "Docker is not installed or not in PATH."
    exit 1
  fi

  if ! docker info >/dev/null 2>&1; then
    echo "Docker daemon is not running."
    echo "Start Docker and retry."
    exit 1
  fi
}

ACTION="${1:-start}"

run_compose() {
  "${COMPOSE_CMD[@]}" \
    --project-directory "$ROOT_DIR" \
    -f "$COMPOSE_FILE" \
    "$@"
}

wait_for_services() {
  local attempts=60
  local sleep_seconds=2
  local code=""

  echo "Waiting for containers to become healthy..."
  for ((i=1; i<=attempts; i++)); do
    if code="$(curl -sS -o /dev/null -w "%{http_code}" -H "X-Api-Key: $API_TEST_KEY" "$API_TEST_URL" 2>/dev/null)" && [[ "$code" =~ ^(200|401)$ ]]; then
      echo "Services are ready."
      return 0
    fi
    sleep "$sleep_seconds"
  done

  echo "Services did not become ready in time."
  echo "Run: $0 logs"
  return 1
}

print_usage() {
  echo "Usage: $0 [start|start-detached|stop|reset|logs|status|test]"
}

require_docker
detect_compose

case "$ACTION" in
  start)
    run_compose up -d --build
    wait_for_services
    echo ""
    echo "Backend is running."
    echo "Host URL: http://localhost:8080/openMinds/phpFile/"
    echo "Android emulator URL: http://10.0.2.2:8080/openMinds/phpFile/"
    echo ""
    echo "Quick test:"
    echo "curl -i -H \"X-Api-Key: $API_TEST_KEY\" \"$API_TEST_URL\""
    ;;
  start-detached)
    run_compose up -d --build
    wait_for_services
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
  test)
    curl -i -H "X-Api-Key: $API_TEST_KEY" "$API_TEST_URL"
    ;;
  *)
    echo "Unknown action: $ACTION"
    print_usage
    exit 1
    ;;
esac
