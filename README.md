# Formulářová knihovna psaná v Javě s důrazem na obecnost, modularitu a nezávislost jádra na konkrétní hostující platformě
* Doplněna nástinem implementace nad herním enginem LibGDX
*
* Pro javadoc dokumentaci viz: https://markussecundus.github.io/GdxForms/
*
*
* Mělo by být zkompilovatelné v Android Studiu 4.0+, testováno s Android API 15, 19, 29
*
*
* Demo s názornou ukázkou použití knihovny... core/src/com/markussecundus/formsgdx/FormsGdxExample
*
* 
*
* struktura projektu:
*        balíček android/...   pouze základní launcher, který spustí LibGDX aplikaci a předá jí řízení
*
*        balíček core/...      obsahuje formulářovou knihovnu
*               - com.markussecundus.forms    ... generické jádro knihovny nezávislé na platformě
*               - com.markussecundus.formsgdx ... aplikace knihovny pro engine LibGDX
*           
*
*
*
*
*
*
*
* Zásluhy: tvůrci Javovské standardní knihovny, tvůrci LibGDX ("https://libgdx.badlogicgames.com/"), Google,...
*
* autor: Jakub Hroník
