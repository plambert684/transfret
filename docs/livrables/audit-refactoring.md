# Corrigé de l'étape 1 – Diagnostic

## 1. Tableau défaut → emplacement → principe violé

| Principe violé                     | Exemples dans le legacy                                                                                                                                                                                   | Étape |
|------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------|
| **S** – Responsabilité unique      | `Expedition.genererFacture()` : une entité métier qui fait de la mise en forme · `ServiceExpedition` valide, calcule les délais, gère le cycle de vie et envoie les notifications                         | 3     |
| **O** – Ouvert/fermé               | `CalculateurPrix` : `if/else` sur le mode, sur le type de client, et un `if` par supplément · `calculerDelai()` : `switch` sur le mode · ajouter FLUVIAL = modifier 5 fichiers existants                  | 5, 6  |
| **L** – Substitution de Liskov     | `CamionFrigorifique.charger()` refuse les colis non réfrigérés, d'où le `continue` « sinon ça plante » dans `AffectationVehicule` · `Train.faireLePlein()` lève une exception, cachée par un `catch` vide | 7     |
| **I** – Ségrégation des interfaces | `IVehicule` impose 13 méthodes, dont `rouler`, `naviguer`, `voler` : `Camion.voler()`, `Bateau.rouler()`… lèvent `UnsupportedOperationException`                                                          | 7     |
| **D** – Inversion des dépendances  | `Database.getInstance()` (singleton) · `EmailService.envoyer()` statique et `new SmsService()` en dur · `new CalculateurPrix()` dans `ServiceExpedition` · `Flotte.vehicules` statique                    | 4     |

Hors SOLID, à relever aussi : nombres magiques (`CalculateurPrix`, `calculerDelai`), codes en `String` / `int`
(mode, statut, type de colis), code dupliqué (arrondi, détection fragile/dangereux/réfrigéré), code mort
(`Utils.arrondir`, `Utils.emailValide`).

## 2. Endroits à modifier pour ajouter le mode FLUVIAL (dans le legacy)

| # | Fichier                                             | Modification                                                                                                                             |
|---|-----------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| 1 | `CalculateurPrix` l. 25-41                          | nouvelle branche `else if (mode.equals("FLUVIAL"))`, avec le minimum de 300 €                                                            |
| 2 | `ServiceExpedition.calculerDelai()` l. 103          | nouveau `case "FLUVIAL"`                                                                                                                 |
| 3 | `ServiceExpedition.creerExpedition()` l. 39         | ajouter le fluvial aux modes interdits pour le réfrigéré (la règle liste les modes autorisés : rien à changer, mais il faut le vérifier) |
| 4 | `ServiceExpedition.creerExpedition()` l. 43         | interdire l'urgence : `urgent && (mode.equals("MER") \|\| mode.equals("FLUVIAL"))`                                                       |
| 5 | `ServiceExpedition.demarrerExpedition()` l. 142-149 | nouvelle branche pour faire naviguer la péniche                                                                                          |
| 6 | `AffectationVehicule` l. 20-52                      | nouvelle branche `mode.equals("FLUVIAL")` + `instanceof Peniche`                                                                         |
| 7 | `Flotte` l. 12-17                                   | ajouter la péniche `PEN-0001`                                                                                                            |
| 8 | `Utils.getDistance()` l. 30-50                      | ajouter le trajet Paris – Le Havre                                                                                                       |
| 9 | **nouveau** `Peniche.java`                          | implémenter les 13 méthodes de `IVehicule`, dont `rouler`, `voler` et `activerRefrigeration` en `UnsupportedOperationException`          |

**Bilan :** 5 fichiers existants modifiés, à 7 endroits différents, plus une nouvelle classe qui reproduit les
défauts L et I. Oublier l'endroit n° 4 ou n° 6 ne provoque aucune erreur de compilation : le défaut se voit
seulement à l'exécution.

## 3. Endroits à modifier pour le supplément « hors gabarit »

| # | Fichier                    | Modification                                                                                |
|---|----------------------------|---------------------------------------------------------------------------------------------|
| 1 | `CalculateurPrix` l. 13-21 | détecter un colis de plus de 200 cm dans la boucle (nouveau booléen)                        |
| 2 | `CalculateurPrix` l. 44-47 | ajouter `if (horsGabarit) prix = prix * 1.10;` au bon endroit, avant l'urgence et la remise |

**Bilan :** un seul fichier, mais c'est une classe existante qu'il faut rouvrir, et il faut connaître l'ordre
exact des calculs pour placer la ligne. C'est l'argument pour le Decorator : après l'étape 6, ce supplément est
une nouvelle classe plus une ligne dans l'assemblage.

## 4. Points à faire ressortir en correction
- La plupart des défauts O, L et I se concentrent dans trois fichiers : `CalculateurPrix`,
  `AffectationVehicule` et la hiérarchie de véhicules. Ce sont les cibles de la séance 2.
- Les défauts D expliquent pourquoi `LegacyAdapter` doit vider `Database`, `EmailService.journal`,
  `SmsService.journal`, `ServiceExpedition.compteur` et `Flotte` avant chaque test.
- Le code mort (`Utils.emailValide`, `Utils.arrondir`) montre une duplication « ratée » : quelqu'un a créé
  l'utilitaire, mais personne ne s'en sert.
- Le minimum RAIL de 150 € n'est jamais atteint avec la table des distances : aucun test ne le vérifie.
  Le garder ou le supprimer ? C'est une bonne question sur ce que couvrent les tests.
