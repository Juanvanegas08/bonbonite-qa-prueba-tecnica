param([Parameter(Mandatory=$true)][string]$ReportDir)
$ErrorActionPreference='Stop'
$reportPath=(Resolve-Path -LiteralPath $ReportDir).Path
$utf8=New-Object System.Text.UTF8Encoding($false)
# Remove only optional CSV links whose local export does not exist.
# Cucumber JSON, Failsafe XML, Serenity JSON and test results stay unchanged.
$pattern='(?s)\s*\|\s*<a\s+href="(?<csv>[A-Za-z0-9_.-]+\.csv)"[^>]*>.*?</a>'
foreach($file in Get-ChildItem -LiteralPath $reportPath -Filter '*.html' -File){
    $content=[System.IO.File]::ReadAllText($file.FullName,$utf8)
    $updated=[regex]::Replace($content,$pattern,[System.Text.RegularExpressions.MatchEvaluator]{
        param($match)
        if(Test-Path -LiteralPath (Join-Path $reportPath $match.Groups['csv'].Value)){
            return $match.Value
        }
        return ''
    })
    if($updated -cne $content){[System.IO.File]::WriteAllText($file.FullName,$updated,$utf8)}
}
