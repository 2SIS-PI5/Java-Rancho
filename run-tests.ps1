# ==============================================================
#  RanchoPOC - Test Report Runner
#  Roda os testes, parseia os XMLs do Surefire e abre o dashboard
# ==============================================================

$projectRoot = "C:\Users\guelr\Desktop\ranchoPOc\back\Java-Rancho\backend\projeto-rancho"
$reportsDir  = Join-Path $projectRoot "target\surefire-reports"
$outputHtml  = Join-Path $projectRoot "target\test-dashboard.html"

Write-Host ""
Write-Host "  ===========================================" -ForegroundColor Cyan
Write-Host "   RANCHO - Test Runner" -ForegroundColor Cyan
Write-Host "  ===========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "  Rodando os testes..." -ForegroundColor Yellow

Push-Location $projectRoot
& ".\mvnw" test "--no-transfer-progress" 2>&1 | Out-Null
Pop-Location

Write-Host "  Testes concluidos. Gerando dashboard..." -ForegroundColor Yellow

# Parse Surefire XMLs
$xmlFiles = Get-ChildItem -Path $reportsDir -Filter "TEST-*.xml" -ErrorAction SilentlyContinue

if (-not $xmlFiles) {
    Write-Host "  ERRO: Nenhum relatorio XML encontrado em $reportsDir" -ForegroundColor Red
    exit 1
}

$suites     = @()
$totalTests = 0
$totalFail  = 0
$totalErr   = 0
$totalSkip  = 0
$totalTime  = 0.0

foreach ($file in $xmlFiles) {
    [xml]$xml = Get-Content $file.FullName -Encoding UTF8
    $ts = $xml.testsuite

    $tests    = [int]$ts.tests
    $failures = [int]$ts.failures
    $errors   = [int]$ts.errors
    $skipped  = [int]$ts.skipped
    $time     = [double]($ts.time -replace ',','.')
    $passed   = $tests - $failures - $errors - $skipped

    $cases = @()
    foreach ($tc in $ts.testcase) {
        $status = "passed"
        $detail = ""
        if ($null -ne $tc.failure)  { $status = "failure"; $detail = ($tc.failure.'#text' -replace '<','&lt;' -replace '>','&gt;') }
        elseif ($null -ne $tc.error){ $status = "error";   $detail = ($tc.error.'#text'   -replace '<','&lt;' -replace '>','&gt;') }
        elseif ($null -ne $tc.skipped){ $status = "skipped" }

        $cases += [PSCustomObject]@{
            Name   = ($tc.name -replace '<','&lt;' -replace '>','&gt;')
            Status = $status
            Time   = [double]($tc.time -replace ',','.')
            Detail = $detail
        }
    }

    $suites += [PSCustomObject]@{
        Name     = ($ts.name -replace '<','&lt;' -replace '>','&gt;')
        Tests    = $tests
        Passed   = $passed
        Failures = $failures
        Errors   = $errors
        Skipped  = $skipped
        Time     = $time
        Cases    = $cases
    }

    $totalTests += $tests
    $totalFail  += $failures
    $totalErr   += $errors
    $totalSkip  += $skipped
    $totalTime  += $time
}

$totalPassed   = $totalTests - $totalFail - $totalErr - $totalSkip
$passPercent   = if ($totalTests -gt 0) { [math]::Round(($totalPassed / $totalTests) * 100, 1) } else { 0 }
$failPercent   = if ($totalTests -gt 0) { [math]::Round((($totalFail + $totalErr) / $totalTests) * 100, 1) } else { 0 }
$now           = Get-Date -Format "dd/MM/yyyy HH:mm:ss"
$totalMs       = [math]::Round($totalTime * 1000)
$isSuccess     = ($totalFail -eq 0 -and $totalErr -eq 0)
$overallLabel  = if ($isSuccess) { "TODOS OS TESTES PASSARAM" } else { "FALHAS DETECTADAS" }
$bannerColor   = if ($isSuccess) { "#22c55e" } else { "#ef4444" }
$bannerBg      = if ($isSuccess) { "rgba(34,197,94,.12)" } else { "rgba(239,68,68,.12)" }
$bannerBorder  = if ($isSuccess) { "rgba(34,197,94,.35)" } else { "rgba(239,68,68,.35)" }

# Build suite HTML
$suitesHtml = [System.Text.StringBuilder]::new()

foreach ($suite in ($suites | Sort-Object Name)) {
    $sp          = if ($suite.Tests -gt 0) { [math]::Round(($suite.Passed / $suite.Tests) * 100, 0) } else { 0 }
    $fp          = 100 - $sp
    $suiteOk     = ($suite.Failures -eq 0 -and $suite.Errors -eq 0)
    $statusClass = if ($suiteOk) { "ok" } else { "fail" }
    $suitems     = [math]::Round($suite.Time * 1000)

    $passTag = "<span class='tag passed'>$($suite.Passed) passaram</span>"
    $failTag = if ($suite.Failures -gt 0) { "<span class='tag failed'>$($suite.Failures) falhas</span>" } else { "" }
    $errTag  = if ($suite.Errors   -gt 0) { "<span class='tag error'>$($suite.Errors) erros</span>" }    else { "" }
    $skipTag = if ($suite.Skipped  -gt 0) { "<span class='tag skipped'>$($suite.Skipped) pulados</span>" } else { "" }

    # Build cases
    $casesHtml = [System.Text.StringBuilder]::new()
    foreach ($c in $suite.Cases) {
        $cClass  = switch ($c.Status) { "passed" { "c-pass" } "skipped" { "c-skip" } default { "c-fail" } }
        $cSymbol = switch ($c.Status) { "passed" { "+" } "skipped" { "~" } default { "x" } }
        $cms     = [math]::Round($c.Time * 1000)
        $detHtml = if ($c.Detail) { "<div class='c-detail'>$($c.Detail)</div>" } else { "" }
        [void]$casesHtml.Append("<div class='case $cClass'><span class='cicon'>$cSymbol</span><span class='cname'>$($c.Name)</span><span class='ctime'>${cms}ms</span>$detHtml</div>")
    }

    [void]$suitesHtml.Append(@"
<div class='suite $statusClass' onclick='toggle(this)'>
  <div class='suite-header'>
    <div class='suite-meta'>
      <div class='suite-name'>$($suite.Name)</div>
      <div class='suite-stats'>$passTag$failTag$errTag$skipTag<span class='tag time'>${suitems}ms</span></div>
    </div>
    <div class='suite-bar-wrap'>
      <div class='suite-bar'><div class='bar-pass' style='width:${sp}%'></div><div class='bar-fail' style='width:${fp}%'></div></div>
      <div class='suite-pct'>$sp%</div>
    </div>
    <span class='chevron'>v</span>
  </div>
  <div class='suite-cases'>$($casesHtml.ToString())</div>
</div>
"@)
}

# Write HTML
$lines = [System.Collections.Generic.List[string]]::new()
$lines.Add('<!DOCTYPE html>')
$lines.Add('<html lang="pt-BR">')
$lines.Add('<head>')
$lines.Add('<meta charset="UTF-8"/>')
$lines.Add('<meta name="viewport" content="width=device-width, initial-scale=1"/>')
$lines.Add('<title>RanchoPOC - Test Dashboard</title>')
$lines.Add('<link rel="preconnect" href="https://fonts.googleapis.com"/>')
$lines.Add('<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&amp;family=JetBrains+Mono:wght@400;600&amp;display=swap" rel="stylesheet"/>')
$lines.Add('<style>')
$lines.Add(':root{--bg:#0a0f1a;--surface:#111827;--card:#1f2937;--border:#374151;--text:#f1f5f9;--muted:#94a3b8;--primary:#6366f1;--pass:#22c55e;--fail:#ef4444;--skip:#64748b;--pass-bg:rgba(34,197,94,.12);--fail-bg:rgba(239,68,68,.12);}')
$lines.Add('*{margin:0;padding:0;box-sizing:border-box;}')
$lines.Add('body{background:var(--bg);color:var(--text);font-family:Inter,sans-serif;min-height:100vh;}')
$lines.Add('.hero{background:linear-gradient(135deg,#0f172a 0%,#1e1b4b 50%,#0f172a 100%);border-bottom:1px solid var(--border);padding:40px 32px 32px;text-align:center;position:relative;overflow:hidden;}')
$lines.Add('.hero::before{content:"";position:absolute;inset:0;background:radial-gradient(ellipse at 50% 0%,rgba(99,102,241,.2) 0%,transparent 70%);}')
$lines.Add('.hero-badge{display:inline-flex;align-items:center;gap:8px;background:rgba(99,102,241,.15);border:1px solid rgba(99,102,241,.4);border-radius:999px;padding:6px 16px;font-size:.78rem;font-weight:600;color:#a5b4fc;letter-spacing:.06em;text-transform:uppercase;margin-bottom:20px;position:relative;}')
$lines.Add('.hero h1{font-size:2.4rem;font-weight:800;letter-spacing:-.03em;position:relative;}')
$lines.Add('.hero h1 span{color:#818cf8;}')
$lines.Add('.hero-sub{color:var(--muted);font-size:.9rem;margin-top:6px;position:relative;}')
$lines.Add('.hero-time{display:inline-block;margin-top:14px;background:rgba(255,255,255,.05);border:1px solid var(--border);border-radius:8px;padding:5px 14px;font-size:.78rem;font-family:"JetBrains Mono",monospace;color:var(--muted);position:relative;}')
$lines.Add(".status-banner{margin:28px 32px 0;border-radius:14px;padding:20px 28px;display:flex;align-items:center;gap:16px;border:1px solid $bannerBorder;background:$bannerBg;}")
$lines.Add(".status-label{font-size:1.1rem;font-weight:700;color:$bannerColor;}")
$lines.Add('.status-detail{font-size:.84rem;color:var(--muted);margin-top:2px;}')
$lines.Add('.stats-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:16px;margin:24px 32px;}')
$lines.Add('.stat{background:var(--card);border:1px solid var(--border);border-radius:14px;padding:20px 22px;transition:transform .2s;}')
$lines.Add('.stat:hover{transform:translateY(-2px);}')
$lines.Add('.stat-val{font-size:2rem;font-weight:800;font-family:"JetBrains Mono",monospace;line-height:1;}')
$lines.Add('.stat-label{font-size:.78rem;font-weight:600;text-transform:uppercase;letter-spacing:.08em;margin-top:6px;color:var(--muted);}')
$lines.Add('.stat.pass .stat-val{color:var(--pass);}.stat.fail .stat-val{color:var(--fail);}.stat.skip .stat-val{color:var(--skip);}.stat.time .stat-val{color:#a5b4fc;}')
$lines.Add('.master-bar-wrap{margin:20px 32px 28px;}')
$lines.Add('.master-bar-label{display:flex;justify-content:space-between;font-size:.8rem;color:var(--muted);margin-bottom:8px;font-weight:600;}')
$lines.Add('.master-bar{height:20px;border-radius:999px;background:var(--card);overflow:hidden;border:1px solid var(--border);display:flex;}')
$lines.Add('.mb-pass{background:linear-gradient(90deg,#16a34a,#22c55e);height:100%;width:0%;transition:width 1.2s cubic-bezier(.4,0,.2,1);}')
$lines.Add('.mb-fail{background:linear-gradient(90deg,#b91c1c,#ef4444);height:100%;width:0%;transition:width 1.2s cubic-bezier(.4,0,.2,1);}')
$lines.Add('.suites-wrap{padding:0 32px 40px;}')
$lines.Add('.suites-title{font-size:.72rem;font-weight:700;text-transform:uppercase;letter-spacing:.12em;color:var(--muted);margin-bottom:14px;}')
$lines.Add('.suite{background:var(--card);border:1px solid var(--border);border-radius:12px;margin-bottom:10px;overflow:hidden;transition:box-shadow .2s;}')
$lines.Add('.suite:hover{box-shadow:0 4px 24px rgba(0,0,0,.4);}')
$lines.Add('.suite.ok{border-left:4px solid var(--pass);}.suite.fail{border-left:4px solid var(--fail);}')
$lines.Add('.suite-header{display:flex;align-items:center;gap:14px;padding:14px 18px;cursor:pointer;user-select:none;}')
$lines.Add('.suite-meta{flex:1;min-width:0;}')
$lines.Add('.suite-name{font-size:.88rem;font-weight:600;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;}')
$lines.Add('.suite-stats{display:flex;gap:6px;flex-wrap:wrap;margin-top:5px;}')
$lines.Add('.tag{font-size:.68rem;font-weight:700;padding:2px 8px;border-radius:999px;text-transform:uppercase;letter-spacing:.04em;}')
$lines.Add('.tag.passed{background:var(--pass-bg);color:var(--pass);}.tag.failed{background:var(--fail-bg);color:var(--fail);}.tag.error{background:rgba(239,68,68,.15);color:#fca5a5;}.tag.skipped{background:rgba(100,116,139,.15);color:var(--skip);}.tag.time{background:rgba(99,102,241,.12);color:#a5b4fc;}')
$lines.Add('.suite-bar-wrap{display:flex;align-items:center;gap:10px;flex-shrink:0;}')
$lines.Add('.suite-bar{width:110px;height:7px;border-radius:999px;background:var(--bg);overflow:hidden;display:flex;}')
$lines.Add('.bar-pass{background:var(--pass);height:100%;}.bar-fail{background:var(--fail);height:100%;}')
$lines.Add('.suite-pct{font-size:.78rem;font-weight:700;font-family:"JetBrains Mono",monospace;color:var(--pass);min-width:36px;text-align:right;}')
$lines.Add('.suite.fail .suite-pct{color:var(--fail);}')
$lines.Add('.chevron{color:var(--muted);font-size:.75rem;transition:transform .25s;flex-shrink:0;font-weight:700;}')
$lines.Add('.suite.open .chevron{transform:rotate(180deg);}')
$lines.Add('.suite-cases{display:none;border-top:1px solid var(--border);padding:6px 18px 10px;}')
$lines.Add('.suite.open .suite-cases{display:block;}')
$lines.Add('.case{display:flex;align-items:flex-start;gap:10px;padding:7px 8px;border-radius:8px;font-size:.8rem;margin-bottom:2px;transition:background .15s;}')
$lines.Add('.case:hover{background:rgba(255,255,255,.04);}')
$lines.Add('.cicon{flex-shrink:0;font-size:.9rem;font-weight:700;margin-top:1px;font-family:"JetBrains Mono",monospace;}')
$lines.Add('.c-pass .cicon{color:var(--pass);}.c-fail .cicon{color:var(--fail);}.c-skip .cicon{color:var(--skip);}')
$lines.Add('.cname{flex:1;color:var(--text);font-family:"JetBrains Mono",monospace;font-size:.76rem;word-break:break-all;}')
$lines.Add('.ctime{color:var(--muted);font-size:.7rem;flex-shrink:0;font-family:"JetBrains Mono",monospace;}')
$lines.Add('.c-detail{font-size:.7rem;font-family:"JetBrains Mono",monospace;color:#fca5a5;background:rgba(239,68,68,.08);border:1px solid rgba(239,68,68,.2);border-radius:6px;padding:8px 10px;margin-top:6px;width:100%;white-space:pre-wrap;word-break:break-all;}')
$lines.Add('footer{text-align:center;padding:24px;color:var(--muted);font-size:.76rem;border-top:1px solid var(--border);}')
$lines.Add('@keyframes fadeUp{from{opacity:0;transform:translateY(10px);}to{opacity:1;transform:translateY(0);}}')
$lines.Add('.suite{animation:fadeUp .3s ease both;}')
$lines.Add('.suite:nth-child(1){animation-delay:.05s}.suite:nth-child(2){animation-delay:.10s}.suite:nth-child(3){animation-delay:.15s}.suite:nth-child(4){animation-delay:.20s}.suite:nth-child(5){animation-delay:.25s}.suite:nth-child(6){animation-delay:.30s}.suite:nth-child(7){animation-delay:.35s}.suite:nth-child(8){animation-delay:.40s}.suite:nth-child(9){animation-delay:.45s}.suite:nth-child(10){animation-delay:.50s}')
$lines.Add('@media(max-width:700px){.stats-grid{grid-template-columns:1fr 1fr;}.hero h1{font-size:1.6rem;}.suite-bar-wrap{display:none;}}')
$lines.Add('</style>')
$lines.Add('</head>')
$lines.Add('<body>')
$lines.Add('<div class="hero">')
$lines.Add('  <div class="hero-badge">RANCHOPOC</div>')
$lines.Add("  <h1>Test <span>Dashboard</span></h1>")
$lines.Add('  <div class="hero-sub">Relatorio de Testes Unitarios - JUnit 5 + Mockito</div>')
$lines.Add("  <div class='hero-time'>Gerado em $now  |  Total: ${totalMs}ms</div>")
$lines.Add('</div>')
$lines.Add('<div class="status-banner">')
$lines.Add("  <div>")
$lines.Add("    <div class='status-label'>$overallLabel</div>")
$lines.Add("    <div class='status-detail'>$totalPassed de $totalTests testes passaram em $([math]::Round($totalTime,2))s</div>")
$lines.Add("  </div>")
$lines.Add('</div>')
$lines.Add('<div class="stats-grid">')
$lines.Add("  <div class='stat pass'><div class='stat-val'>$totalPassed</div><div class='stat-label'>Passaram</div></div>")
$lines.Add("  <div class='stat fail'><div class='stat-val'>$($totalFail + $totalErr)</div><div class='stat-label'>Falharam</div></div>")
$lines.Add("  <div class='stat skip'><div class='stat-val'>$totalSkip</div><div class='stat-label'>Pulados</div></div>")
$lines.Add("  <div class='stat time'><div class='stat-val'>$([math]::Round($totalTime,2))s</div><div class='stat-label'>Duracao</div></div>")
$lines.Add('</div>')
$lines.Add('<div class="master-bar-wrap">')
$lines.Add("  <div class='master-bar-label'><span>Aprovacao</span><span>$passPercent% OK  /  $failPercent% Falha</span></div>")
$lines.Add("  <div class='master-bar'><div class='mb-pass' id='mbpass'></div><div class='mb-fail' id='mbfail'></div></div>")
$lines.Add('</div>')
$lines.Add('<div class="suites-wrap">')
$lines.Add("  <div class='suites-title'>Suites de Teste - $($suites.Count) arquivos</div>")
$lines.Add($suitesHtml.ToString())
$lines.Add('</div>')
$lines.Add("<footer>RanchoPOC Test Dashboard  |  $now  |  $totalTests testes em $($suites.Count) suites</footer>")
$lines.Add('<script>')
$lines.Add("setTimeout(function(){")
$lines.Add("  document.getElementById('mbpass').style.width='$passPercent%';")
$lines.Add("  document.getElementById('mbfail').style.width='$failPercent%';")
$lines.Add("},150);")
$lines.Add("function toggle(el){el.classList.toggle('open');}")
$lines.Add("document.querySelectorAll('.suite.fail').forEach(function(s){s.classList.add('open');});")
$lines.Add('</script>')
$lines.Add('</body>')
$lines.Add('</html>')

[System.IO.File]::WriteAllLines($outputHtml, $lines, [System.Text.Encoding]::UTF8)

Write-Host ""
Write-Host "  OK  $totalPassed/$totalTests testes passaram" -ForegroundColor Green
if ($totalFail + $totalErr -gt 0) {
    Write-Host "  FAIL  $($totalFail + $totalErr) falhas" -ForegroundColor Red
}
Write-Host "  Dashboard: $outputHtml" -ForegroundColor Cyan
Write-Host ""

Start-Process $outputHtml
Write-Host "  Abrindo no navegador..." -ForegroundColor Green
Write-Host ""
