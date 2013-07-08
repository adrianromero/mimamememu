@echo off

set DIRNAME=%~dp0
set CP="%DIRNAME%mimamememu.jar"

start /B javaw -cp %CP% -Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel -Djava.util.logging.config.file="%DIRNAME%logging.properties" -Ddirname.path="%DIRNAME%./" com.adr.mmmmm.Main %1
