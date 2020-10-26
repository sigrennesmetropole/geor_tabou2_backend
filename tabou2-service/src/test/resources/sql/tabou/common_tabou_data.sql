-----------------------------------
-- Insertion des données statiques
-----------------------------------

-- Insertion des étapes d'un programme

INSERT INTO tabou_etape_programme (libelle, code, mode, type) VALUES ('En projet', 'EN_PROJET_OFF', 'OFF', 'START');
INSERT INTO tabou_etape_programme (libelle, code, mode, type) VALUES ('En étude', 'EN_ETUDE_OFF', 'OFF', 'NORMAL');
INSERT INTO tabou_etape_programme (libelle, code, mode, type) VALUES ('Annulé', 'ANNULE_OFF', 'OFF', 'NORMAL');

INSERT INTO tabou_etape_programme (libelle, code, mode, type) VALUES ('En projet', 'EN_PROJET_PUBLIC', 'PUBLIC', 'NORMAL');
INSERT INTO tabou_etape_programme (libelle, code, mode, type) VALUES ('En étude', 'EN_ETUDE_PUBLIC', 'PUBLIC', 'NORMAL');
INSERT INTO tabou_etape_programme (libelle, code, mode, type) VALUES ('En chantier', 'EN_CHANTIER_PUBLIC', 'PUBLIC', 'NORMAL');
INSERT INTO tabou_etape_programme (libelle, code, mode, type) VALUES ('Achevé', 'ACHEVE_PUBLIC', 'PUBLIC', 'NORMAL');
INSERT INTO tabou_etape_programme (libelle, code, mode, type) VALUES ('Clôturé', 'CLOTURE_PUBLIC', 'PUBLIC', 'END');
INSERT INTO tabou_etape_programme (libelle, code, mode, type) VALUES ('Annulé', 'ANNULE_PUBLIC', 'PUBLIC', 'NORMAL');

-- Insertion des étapes suivants pour une étape programme

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'EN_PROJET_OFF' and tep2.code = 'EN_ETUDE_OFF';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'EN_PROJET_OFF' and tep2.code = 'ANNULE_OFF';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'EN_ETUDE_OFF' and tep2.code = 'ANNULE_OFF';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'EN_PROJET_PUBLIC' and tep2.code = 'EN_ETUDE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'EN_PROJET_PUBLIC' and tep2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'EN_ETUDE_PUBLIC' and tep2.code = 'EN_CHANTIER_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'EN_ETUDE_PUBLIC' and tep2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'EN_CHANTIER_PUBLIC' and tep2.code = 'ACHEVE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'EN_CHANTIER_PUBLIC' and tep2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'ACHEVE_PUBLIC' and tep2.code = 'CLOTURE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'ACHEVE_PUBLIC' and tep2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
    select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
    from tabou_etape_programme tep1, tabou_etape_programme tep2
    where tep1.code = 'ANNULE_PUBLIC' and tep2.code = 'CLOTURE_PUBLIC';
