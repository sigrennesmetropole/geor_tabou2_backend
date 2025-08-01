Cette documentation a vocation à décrire les relations de base de données

## Base de données Tabou
La base de données Tabou a vocation à héberger les données métiers du plugin. Il n'y a pas de schéma spécifique, il doit être défini dans le search_path de l'utilisateur.

### Properties
- spring.tabou2.datasource.jdbc-url
- spring.tabou2.datasource.username
- spring.tabou2.datasource.password
### Tables

- tabou_action_operation
- tabou_acteur
- tabou_agapeo
- tabou_amenageur
- tabou_attribut_plh
- tabou_consommation_espace
- tabou_contact_tiers
- tabou_contribution
- tabou_decision
- tabou_description_concertation
- tabou_description_financement_operation
- tabou_description_foncier
- tabou_entite_referente
- tabou_etape_operation
- tabou_etape_programme
- tabou_evenement_operation
- tabou_evenement_programme
- tabou_fonction_contact
- tabou_information_programmation
- tabou_maitrise_ouvrage
- tabou_mode_amenagement
- tabou_mos
- tabou_nature
- tabou_operation
- tabou_operation_tiers
- tabou_outil_amenagement
- tabou_outil_foncier
- tabou_pc_ddc
- tabou_plh
- tabou_programme
- tabou_programme_tiers
- tabou_projet_urbain
- tabou_tiers
- tabou_type_action_operation
- tabou_type_acteur
- tabou_type_amenageur
- tabou_type_contribution
- tabou_type_document
- tabou_type_evenement
- tabou_type_financement
- tabou_type_financement_operation
- tabou_type_foncier
- tabou_type_occupation
- tabou_type_plh
- tabou_type_programmation
- tabou_type_tiers
- tabou_vocation
- tabou_vocation_za
- v_oa_operation
- v_oa_secteur

## Base de données SIG
La base de données SIG a vocation à fournir des informations externes à l'application mais nécessaires à son fonctionnement. 
Des schémas spécifiques sont attendus par rapport à la connaissance de l'existant des bases RM. Il est prévu de retirer cette adhérence à l'occasion de la V2.

### Properties
- spring.sig.datasource.jdbc-url
- spring.sig.datasource.username
- spring.sig.datasource.password

### Schémas
#### demographie
|  Table   | Colonnes attendues                                                       | R/W  |
|:--------:|:-------------------------------------------------------------------------|:----:|
|   iris   | <ul><li>objectid</li><li>ccom</li><li>code_iris</li><li>nmiris</li></ul> | Read |

#### economie
|  Table  | Colonnes attendues                                        |  R/W  |
|:-------:|:----------------------------------------------------------|:-----:|
|   za    | <ul><li>objectid</li><li>id_tabou</li><li>nomZa</li></ul> | Write |

#### limite_admin
|      Table      | Colonnes attendues                                                              | R/W  |
|:---------------:|:--------------------------------------------------------------------------------|:----:|
| comite_sect_tab | <ul><li>num_secteur</li><li>nom_secteur</li></ul>                               | Read |
| commune_emprise | <ul><li>objectid</li><li>nom</li><li>code_insee</li><li>commune_agglo</li></ul> | Read |
|    quartier     | <ul><li>objectid</li><li>nom</li><li>nuquart</li><li>code_insee</li></ul>       | Read |

#### urba_foncier
|           Table            | Colonnes attendues                                                     |  R/W  |
|:--------------------------:|:-----------------------------------------------------------------------|:-----:|
|   oa_limite_intervention   | <ul><li>objectid</li><li>id_tabou</li><li>nom</li><li>nature</li></ul> | Write |
|       plui_zone_urba       | <ul><li>objectid</li><li>libelle</li></ul>                             | Read  |
|        oa_programme        | <ul><li>objectid</li><li>programme</li><li>id_tabou</li></ul>          | Write |
|    instructeur_secteur     | <ul><li>id</li><li>secteur</li></ul>                                   | Read  |
|         oa_secteur         | <ul><li>objectid</li><li>id_tabou</li><li>secteur</li></ul>            | Write |
| negociateurfoncier_secteur | <ul><li>objectid</li><li>negociateur</li></ul>                         | Read  |
|  chargedoperation_secteur  | <ul><li>id</li><li>nom_secteur</li></ul>                               | Read  |
|            zac             | <ul><li>id_zac</li><li>id_tabou</li><li>nomZac</li></ul>               | Write |
