$rootPath = Split-Path $PSScriptRoot -Parent
$userPath = Join-Path $rootPath "wardrobe_user"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  wardrobe - one-click start" -ForegroundColor White
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "  Frontend : http://localhost:7070" -ForegroundColor Green
Write-Host "  (用户登录后进入商城界面，管理员登录后进入管理后台)" -ForegroundColor Yellow
Write-Host ""
Write-Host "Starting..." -ForegroundColor Yellow

Start-Process powershell -ArgumentList @(
    "-NoExit",
    "-Command",
    "Set-Location '$userPath'; Write-Host '=== wardrobe_user (7070) ===' -ForegroundColor Cyan; npm run dev"
)

Write-Host "Done! Close the two windows to stop." -ForegroundColor Green
