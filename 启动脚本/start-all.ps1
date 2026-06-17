$rootPath = Split-Path $PSScriptRoot -Parent
$userPath = Join-Path $rootPath "wardrobe_user"
$adminPath = Join-Path $rootPath "wardrobe_admin"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  wardrobe - one-click start" -ForegroundColor White
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "  User  : http://localhost:7070" -ForegroundColor Green
Write-Host "  Admin : http://localhost:7071" -ForegroundColor Green
Write-Host ""
Write-Host "Starting..." -ForegroundColor Yellow

Start-Process powershell -ArgumentList @(
    "-NoExit",
    "-Command",
    "Set-Location '$userPath'; Write-Host '=== wardrobe_user (7070) ===' -ForegroundColor Cyan; npm run dev"
)

Start-Process powershell -ArgumentList @(
    "-NoExit",
    "-Command",
    "Set-Location '$adminPath'; Write-Host '=== wardrobe_admin (7071) ===' -ForegroundColor Cyan; npm run dev"
)

Write-Host "Done! Close the two new windows to stop." -ForegroundColor Green
