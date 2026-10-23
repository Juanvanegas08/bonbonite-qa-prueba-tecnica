param([ValidateSet('Demo','Publico','Real','Compilar')][string]$Modo='Demo')
$ErrorActionPreference='Stop'
$projectRoot=Split-Path $PSScriptRoot -Parent
$qaTools=Join-Path $env:LOCALAPPDATA 'Programs\QA-Tools'
$installedJdk=Get-ChildItem $qaTools -Directory -Filter 'jdk-21*' -ErrorAction SilentlyContinue | Select-Object -First 1
if($installedJdk){ $env:JAVA_HOME=$installedJdk.FullName }
if([string]::IsNullOrWhiteSpace($env:JAVA_HOME) -or !(Test-Path (Join-Path "$env:JAVA_HOME" 'bin\javac.exe'))){throw 'Se requiere JDK 21. Configura JAVA_HOME con la carpeta del JDK.'}
$env:Path=(Join-Path $env:JAVA_HOME 'bin')+';'+$env:Path
$localMaven=Join-Path $qaTools 'apache-maven-3.9.11\bin\mvn.cmd'
$maven=if(Test-Path $localMaven){$localMaven}else{(Get-Command mvn.cmd -ErrorAction Stop).Source}
Push-Location $projectRoot
try {
    if($Modo -eq 'Compilar'){ & $maven '-B' 'test-compile'; if($LASTEXITCODE -ne 0){throw 'No se pudo compilar'};return }
    $tag=switch($Modo){'Demo'{'@e2e'} 'Publico'{'@publico'} 'Real'{'@real'}}
    $extra=@()
    if($Modo -eq 'Real'){
        if(!$env:QA_LOGIN -or !$env:QA_PASSWORD){throw 'Configura QA_LOGIN y QA_PASSWORD como variables de entorno antes del modo Real.'}
        $extra=@('-Dserenity.take.screenshots=DISABLED','-Dserenity.project.name=Bon-Bonite REAL - acceso perfil y checkout parcial')
    }
    & $maven '-B' 'clean' 'verify' "-Dcucumber.filter.tags=$tag" @extra
    $buildExit=$LASTEXITCODE
    $evidence=Join-Path $projectRoot "evidencias\$Modo"
    New-Item -ItemType Directory -Path $evidence -Force | Out-Null
    if(Test-Path 'target\site\serenity'){
        $reportDir=Join-Path $evidence 'serenity'
        New-Item -ItemType Directory -Path $reportDir -Force | Out-Null
        Copy-Item 'target\site\serenity\*' $reportDir -Recurse -Force
        & (Join-Path $PSScriptRoot 'finalize-report.ps1') -ReportDir $reportDir
    }
    foreach($file in @('target\cucumber.json','target\cucumber.html')){if(Test-Path $file){Copy-Item $file $evidence -Force}}
    if(Test-Path 'target\failsafe-reports'){
        $xmlDir=Join-Path $evidence 'failsafe-reports'
        New-Item -ItemType Directory -Path $xmlDir -Force | Out-Null
        Copy-Item 'target\failsafe-reports\*' $xmlDir -Recurse -Force
    }
    if($buildExit -ne 0){throw "La ejecución terminó con error. Revisa $evidence"}
    Write-Host "Reporte: $evidence\serenity\index.html"
} finally { Pop-Location }
