REM 测试的Bat

REM echo off
REM set a=1
REM :A
REM echo hello world !
REM echo %a%
REM echo next..... & pause > nul
REM set /a a = a+1
REM if not %a% == 5 goto :A

REM start explorer E:\MyStudy\BAT

REM assoc .bat && ftype exefile

REM pushd D: && dir && popd && dir

REM call b.bat apple orange
REM b.bat
REM echo %0
REM echo %1
REM echo %2

REM call :A
REM echo BBBBBBBB
REM exit
REM :A
REM echo AAAAAAAA
REM goto :eof

REM attrib ts.bat

REM copy %0 c.bat

REM echo A^
REM     b^
REM     c ^> 0

REM dir | find "bat"
REM find "Cherry" A.bat > a.txt

REM dir,C:;E:

REM for /d %%i in (E:\*) do echo %%i
REM for /r %%i in ("*.bat") do type %%i
REM for /d /r E:\ %%i in ("mysql*") do dir %%i
REM for /l %%i in (1,1,5) do echo %%i
REM for /f "tokens=1,3 delims=," %i in (b.txt) do echo %i %j
REM for /f "tokens=* delims=" %i in ('dir E:') do echo %i

REM set /p var=input:
REM echo %var%
REM pause

REM set b=4
REM set /a x=b+4
REM echo %x%
REM pause

REM set /a b=0 会覆盖 set b=0
REM set /a b = 0
REM :A
REM set /a b += 1
REM echo time:%b%
REM if %b% lss 5 goto :A
REM echo over
REM pause

REM @echo off
REM choice /t 3 /d t /c fst /m [first,second,third]
REM if %errorlevel% == 3 goto :third
REM if %errorlevel% == 2 goto :second
REM if %errorlevel% == 1 goto :fisrt
REM :first
REM echo first
REM goto :exi

REM :second
REM echo second
REM goto :exi

REM :third
REM echo third
REM goto :exi

REM :exi
REM echo exit...... && pause > nul