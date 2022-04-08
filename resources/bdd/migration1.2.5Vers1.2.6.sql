-- Correction de la foreign key faisant le lien description_financement/tabou_operation

alter table if exists tabou_description_financement_operation
drop constraint if exists fk_tabou_description_financement_operation_tabou_operation;
alter table if exists tabou_description_financement_operation
    add constraint fk_tabou_description_financement_operation_tabou_operation
    foreign key (id_operation)
    references tabou_operation;