# Laerskool Swartland Atletiek Portaal (Android)

Die amptelike Android-toepassing vir Laerskool Swartland se atletiekbyeenkomsbestuur en intydse beoordeling, gebou met **Kotlin** en **Jetpack Compose** volgens Material Design 3 riglyne.

## Kenmerke & Modules

1. **📋 Registrasie & Atlete (Setup & Import)**
   - Registreer nuwe atlete met naam, van, skool, geslag (Seuns / Dogters) en ouderdomsgroep (O/6 tot O/13).
   - Kies items en dissiplines wat spesifiek geldig is vir die atleet se ouderdom en geslag.
   - Filter en soek geregistreerde atlete volgens skool, geslag, ouderdomsgroep of naam.
   - Laai vinnige voorbeelddata in of vee data skoon met 'n veilige reset-dialoog.

2. **📄 Lotingslyste (Draw Sheets)**
   - Kies ouderdomsgroep, geslag en item om amptelike baanplasings en deelnemerslyste te bekyk.
   - Snelbesigtigings-dialoog met amptelike prystoekenningsblok (Goud, Silwer, Brons).
   - Vertoon reeds voltooide uitslae vir die geselekteerde item.

3. **⏱️ Beoordeling & Uitslae (Officiating)**
   - Vertoon die amptelike rekord-maatstaf vir die spesifieke item.
   - Voer tye (baan) of afstande (veld) in met intydse berekening van amptelike ASA-punte.
   - Outomatiese rangskikking: baanitems word gerangskik vanaf vinnigste tyd, velditems vanaf verste afstand.
   - Uitslaebestuur met die vermoë om individuele atlete se inskrywings te skrap of te korrigeer.

4. **🏆 Rekordbestuur (Records)**
   - Volledige oorsig oor alle byeenkomsrekords, georganiseer volgens ouderdomsgroep en geslag.
   - Voeg nuwe rekords handmatig by of wysig bestaande rekords.

5. **📊 Regstreekse Uitslae (Live Feed)**
   - Intydse telbord van alle voltooide items met ASA-punte en podium-kentekens vir ouers en toeskouers.
   - Filter volgens skool of ouderdomsgroep.

6. **🖨️ Byeenkoms Verslae (Reports)**
   - Totale skoolpunte: som van alle ASA-punte per skool met ranglys en vorderingsbalke.
   - Victor en Victrix Ludorum: ranglys van toppresteerders per geslag en oor die algemeen.
   - Volledige amptelike byeenkomsopsomming.

7. **📅 Byeenkoms Program & Tydrooster (Program)**
   - Volledige skedule vir die dag met itemnommers, begintye, plekke/bane en statusse (`Wagtend`, `Besig`, `Voltooi`).
   - Tik op statusse om dit intyds op te dateer namate die byeenkoms vorder.
   - Voeg nuwe programitems by die tydrooster.

## Tegnologieë

- **Platform:** Android (Min SDK 26, Target SDK 35)
- **Taal:** Kotlin
- **UI:** Jetpack Compose met Material 3 (M3)
- **Argitektuur:** MVVM / Unidirectional Data Flow met Kotlin Coroutines en `StateFlow`
- **Branding:** Laerskool Swartland se tradisionele Vlootblou (`#0A192F`), Donkerblou (`#112240`) en Goud (`#F4C430`)
