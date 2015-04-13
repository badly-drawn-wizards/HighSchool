@echo off

set PACKAGED_JRE8="%~dp0jre8\bin\java"
set LOCAL_JRE8=java

if exist jre8 (
 choice /M "The programs require JRE8. Use copy of JRE8 included with distribution "
 if errorlevel 1 (
  set JAVA_EXE=%PACKAGED_JRE8%
 )
 if errorlevel 2 (
  set JAVA_EXE=%LOCAL_JRE8%
 )
) else (
 set JAVA_EXE=%LOCAL_JRE8%
)

echo 1) Run PAT
echo 2) Run Data Aware Controls
echo 3) Exit
choice /C 123 /M "What do you want to do: " /N

if errorlevel 3 (
 goto END
)
if errorlevel 2 (
 goto RUN_DAC
)
if errorlevel 1 (
 goto RUN_PAT
)

:RUN_PAT
set /p client_args=<client_args.cfg

pushd bin
choice /M "Run Server "
if not errorlevel 2 (
 start "FancyServer Console" %JAVA_EXE% -jar FancyShaderServer.jar
 echo Waiting for server to start

 rem Using ping to delay. Hacky.
 ping -n 6 localhost >nul 2>&1
)

choice /M "Run Client "
if not errorlevel 2 (
 start "FancyClient Console" %JAVA_EXE% -jar FancyShaderClient.jar %client_args%
)
popd
goto END

:RUN_DAC
pushd bin
start "DataAweControls Console" %JAVA_EXE% -jar DataAwe.jar
popd
goto END

:END