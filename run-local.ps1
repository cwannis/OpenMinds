param(
    [ValidateSet("start", "start-detached", "stop", "reset", "logs", "status")]
    [string]$Action = "start"
)

$ErrorActionPreference = "Stop"
$RootDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ComposeFile = Join-Path $RootDir "docker-compose.yml"

function Invoke-Compose {
    param([Parameter(ValueFromRemainingArguments = $true)][string[]]$Args)

    docker compose version *> $null
    if ($LASTEXITCODE -eq 0) {
        & docker compose -f $ComposeFile @Args
        return
    }

    $legacy = Get-Command docker-compose -ErrorAction SilentlyContinue
    if ($legacy) {
        & docker-compose -f $ComposeFile @Args
        return
    }

    throw "Docker Compose is not installed. Install Docker Desktop/Compose plugin or docker-compose."
}

switch ($Action) {
    "start" {
        Invoke-Compose up --build
        Write-Host ""
        Write-Host "Backend is running in foreground (Ctrl+C to stop)."
        Write-Host "Host URL: http://localhost:8080/openMinds/phpFile/"
        Write-Host "Android emulator URL: http://10.0.2.2:8080/openMinds/phpFile/"
        Write-Host ""
        Write-Host "Quick test:"
        Write-Host 'curl -i -H "X-Api-Key: testAPIKEY" "http://localhost:8080/openMinds/phpFile/mailExist.php?email=test@example.com"'
    }
    "start-detached" {
        Invoke-Compose up -d --build
    }
    "stop" {
        Invoke-Compose down
    }
    "reset" {
        Invoke-Compose down -v
        Invoke-Compose up -d --build
    }
    "logs" {
        Invoke-Compose logs -f web
    }
    "status" {
        Invoke-Compose ps
    }
}
