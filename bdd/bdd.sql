--------------------------------------------------------
--  Fichier créé - samedi-juin-03-2023   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence API_ADRESSE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "ORA6"."API_ADRESSE_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 166 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
--------------------------------------------------------
--  DDL for Sequence API_LOCATION_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "ORA6"."API_LOCATION_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 187 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
--------------------------------------------------------
--  DDL for Sequence API_TAXI_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "ORA6"."API_TAXI_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 166 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
--------------------------------------------------------
--  DDL for Sequence API_TCLIENT_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "ORA6"."API_TCLIENT_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 306 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         --------------------------------------------------------
--  DDL for Table API_FACTURE
--------------------------------------------------------

  CREATE TABLE "ORA6"."API_FACTURE" 
   (	"ID_LOCATION" NUMBER(*,0), 
	"ID_TAXI" NUMBER(*,0), 
	"COUT" NUMBER(15,2)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table API_LOCATION
--------------------------------------------------------

  CREATE TABLE "ORA6"."API_LOCATION" 
   (	"ID_LOCATION" NUMBER(*,0), 
	"DATELOC" DATE, 
	"KMTOTAL" NUMBER(*,0), 
	"ID_ADRESSE" NUMBER(*,0), 
	"ID_CLIENT" NUMBER(*,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table API_TAXI
--------------------------------------------------------

  CREATE TABLE "ORA6"."API_TAXI" 
   (	"ID_TAXI" NUMBER(*,0), 
	"IMMATRICULATION" VARCHAR2(50 BYTE), 
	"CARBURANT" VARCHAR2(50 BYTE), 
	"PRIXKM" NUMBER(5,2)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table API_TCLIENT
--------------------------------------------------------

  CREATE TABLE "ORA6"."API_TCLIENT" 
   (	"ID_CLIENT" NUMBER(*,0), 
	"MAIL" VARCHAR2(50 BYTE), 
	"NOM" VARCHAR2(50 BYTE), 
	"PRENOM" VARCHAR2(50 BYTE), 
	"TEL" VARCHAR2(50 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for View API_FACTURESLOCATION
--------------------------------------------------------

  CREATE OR REPLACE FORCE EDITIONABLE VIEW "ORA6"."API_FACTURESLOCATION" ("COUT", "ID_TAXI", "IMMATRICULATION", "CARBURANT", "PRIXKM", "ID_LOCATION") AS 
  SELECT COUT, AF.ID_TAXI, IMMATRICULATION, CARBURANT, PRIXKM, AF.ID_LOCATION FROM API_FACTURE AF INNER JOIN API_TAXI T on AF.ID_TAXI = T.ID_TAXI
;
--------------------------------------------------------
--  DDL for View API_KMPARTAXI
--------------------------------------------------------

  CREATE OR REPLACE FORCE EDITIONABLE VIEW "ORA6"."API_KMPARTAXI" ("ID_TAXI", "TAXI", "distance") AS 
  SELECT t.id_taxi, t.immatriculation taxi, SUM(l.kmtotal) as "distance"  FROM API_TAXI t
INNER JOIN API_Facture f
ON f.id_taxi = t.id_taxi
INNER JOIN API_Location l
ON l.id_location = f.id_location
GROUP BY t.id_taxi, t.immatriculation
ORDER BY SUM(l.kmtotal)
;
--------------------------------------------------------
--  DDL for View API_LAFTC
--------------------------------------------------------

  CREATE OR REPLACE FORCE EDITIONABLE VIEW "ORA6"."API_LAFTC" ("ID_CLIENT", "MAIL", "NOM", "PRENOM", "TEL", "ID_LOCATION", "DATELOC", "KMTOTAL", "ID_ADRESSE", "CP", "LOCALITE", "RUE", "NUM", "COUT", "ID_TAXI", "IMMATRICULATION", "CARBURANT", "PRIXKM") AS 
  SELECT
    cli.ID_CLIENT, cli.MAIL, cli.NOM, cli.PRENOM, cli.TEL,
    loc.id_location, loc.dateloc, loc.kmtotal,
    adr.id_adresse, cp, localite, rue, num,
    fac.cout,
    fac.id_taxi, IMMATRICULATION, CARBURANT, PRIXKM
   FROM API_TCLIENT cli
    LEFT JOIN API_LOCATION loc ON cli.ID_CLIENT = loc.ID_CLIENT
    LEFT JOIN API_ADRESSE adr ON adr.id_adresse = loc.id_adresse
    LEFT JOIN API_FACTURE fac ON fac.ID_LOCATION = loc.ID_LOCATION
    LEFT JOIN API_TAXI tax ON tax.ID_TAXI = fac.ID_TAXI
    
    ORDER BY cli.ID_CLIENT
;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            --------------------------------------------------------
--  DDL for View API_LOCATIONPARADRESSE
--------------------------------------------------------

  CREATE OR REPLACE FORCE EDITIONABLE VIEW "ORA6"."API_LOCATIONPARADRESSE" ("ID_ADRESSE", "RUE", "NUM", "ID_LOCATION", "DATELOC", "KMTOTAL", "MAIL", "NOM", "PRENOM", "TEL", "ID_CLIENT") AS 
  SELECT a.id_adresse, a.rue, a.num, l.id_location, l.dateLoc, l.kmTotal, c.mail, c.nom, c.prenom, c.tel, c.id_client
FROM API_Adresse a
JOIN API_Location l ON l.id_adresse = a.id_adresse
JOIN API_TClient c ON c.id_client = l.id_client
ORDER BY a.id_adresse
;
--------------------------------------------------------
--  DDL for View API_TAXIPARCLIENT
--------------------------------------------------------

  CREATE OR REPLACE FORCE EDITIONABLE VIEW "ORA6"."API_TAXIPARCLIENT" ("ID_CLIENT", "NOM", "PRENOM", "TAXI") AS 
  SELECT DISTINCT c.id_client, c.nom, c.prenom, t.immatriculation taxi
FROM API_TClient c
JOIN API_Location l ON l.id_client = c.id_client
JOIN API_Facture f ON f.id_location = l.id_location
JOIN API_Taxi t ON t.id_taxi = f.id_taxi
ORDER BY c.nom ASC, c.prenom ASC
;
REM INSERTING into ORA6.API_ADRESSE
SET DEFINE OFF;
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('1','1000','Bruxelles','Avenue des Arts','10');
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('2','1020','Laeken','Rue de la Reine','22');
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('3','1030','Scaerbeek','Avenue de la chasse','40');
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('4','1050','Ixelles','Avenue Louise','50');
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('5','1150','Uccle','Rue du Marais','60');
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('46','7830','Silly','Chaussée de Ghislenghien','57');
REM INSERTING into ORA6.API_FACTURE
SET DEFINE OFF;
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('7','4','30');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('108','6','19,2');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('7','5','21,6');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('7','6','19,2');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('30','2','2448');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('30','6','1958,4');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('1','1','225');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('2','2','39');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('3','3','161');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('4','4','64');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('5','5','81');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('2','5','5,4');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('30','5','1958,4');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('30','1','1836');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('6','5','32,4');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('1','26','40,5');
REM INSERTING into ORA6.API_LOCATION
SET DEFINE OFF;
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('1',to_date('01/01/23','DD/MM/RR'),'15','1','1');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('2',to_date('02/01/23','DD/MM/RR'),'3','2','2');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('3',to_date('04/01/23','DD/MM/RR'),'7','3','3');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('4',to_date('05/01/23','DD/MM/RR'),'8','4','4');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('5',to_date('06/01/23','DD/MM/RR'),'9','5','5');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('7',to_date('31/03/23','DD/MM/RR'),'12','5','1');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('6',to_date('02/01/23','DD/MM/RR'),'18','2','1');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('30',to_date('31/03/23','DD/MM/RR'),'1224','46','88');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('108',to_date('25/05/23','DD/MM/RR'),'12','46','88');
REM INSERTING into ORA6.API_TAXI
SET DEFINE OFF;
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('1','T-ABC-456','Essence','1,5');
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('2','T-DEF-789','Diesel','2');
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('3','T-GHI-123','Électrique','3');
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('4','T-JKL-456','Hybride','2,5');
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('5','T-MNO-789','Gaz','1,8');
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('6','T-PQR-123','Essence','1,6');
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('26','T-STU-456','Essence','2,7');
REM INSERTING into ORA6.API_TCLIENT
SET DEFINE OFF;
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125');
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('2','anne.martin@outlook.com','Martin','Anne','0456789126');
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('3','nicolas.petit@gmail.com','Petit','Nicolas','0456789127');
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('4','jean.dupont@gmail.com','Dupont','Jean','+32456789123');
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('5','marie.dubois@hotmail.com','Dubois','Marie','0456789124');
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('88','pablo.moulin@gmail.com','Moulin','Pablo','0475555555');
REM INSERTING into ORA6.API_FACTURESLOCATION
SET DEFINE OFF;
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('30','4','T-JKL-456','Hybride','2,5','7');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('19,2','6','T-PQR-123','Essence','1,6','108');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('21,6','5','T-MNO-789','Gaz','1,8','7');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('19,2','6','T-PQR-123','Essence','1,6','7');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('2448','2','T-DEF-789','Diesel','2','30');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('1958,4','6','T-PQR-123','Essence','1,6','30');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('225','1','T-ABC-456','Essence','1,5','1');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('39','2','T-DEF-789','Diesel','2','2');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('161','3','T-GHI-123','Électrique','3','3');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('64','4','T-JKL-456','Hybride','2,5','4');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('81','5','T-MNO-789','Gaz','1,8','5');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('5,4','5','T-MNO-789','Gaz','1,8','2');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('1958,4','5','T-MNO-789','Gaz','1,8','30');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('1836','1','T-ABC-456','Essence','1,5','30');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('32,4','5','T-MNO-789','Gaz','1,8','6');
Insert into ORA6.API_FACTURESLOCATION (COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM,ID_LOCATION) values ('40,5','26','T-STU-456','Essence','2,7','1');
REM INSERTING into ORA6.API_KMPARTAXI
SET DEFINE OFF;
Insert into ORA6.API_KMPARTAXI (ID_TAXI,TAXI,"distance") values ('3','T-GHI-123','7');
Insert into ORA6.API_KMPARTAXI (ID_TAXI,TAXI,"distance") values ('26','T-STU-456','15');
Insert into ORA6.API_KMPARTAXI (ID_TAXI,TAXI,"distance") values ('4','T-JKL-456','20');
Insert into ORA6.API_KMPARTAXI (ID_TAXI,TAXI,"distance") values ('2','T-DEF-789','1227');
Insert into ORA6.API_KMPARTAXI (ID_TAXI,TAXI,"distance") values ('1','T-ABC-456','1239');
Insert into ORA6.API_KMPARTAXI (ID_TAXI,TAXI,"distance") values ('6','T-PQR-123','1248');
Insert into ORA6.API_KMPARTAXI (ID_TAXI,TAXI,"distance") values ('5','T-MNO-789','1266');
REM INSERTING into ORA6.API_LAFTC
SET DEFINE OFF;
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','7',to_date('31/03/23','DD/MM/RR'),'12','5','1150','Uccle','Rue du Marais','60','30','4','T-JKL-456','Hybride','2,5');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','7',to_date('31/03/23','DD/MM/RR'),'12','5','1150','Uccle','Rue du Marais','60','21,6','5','T-MNO-789','Gaz','1,8');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','1',to_date('01/01/23','DD/MM/RR'),'15','1','1000','Bruxelles','Avenue des Arts','10','40,5','26','T-STU-456','Essence','2,7');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','6',to_date('02/01/23','DD/MM/RR'),'18','2','1020','Laeken','Rue de la Reine','22','32,4','5','T-MNO-789','Gaz','1,8');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','1',to_date('01/01/23','DD/MM/RR'),'15','1','1000','Bruxelles','Avenue des Arts','10','225','1','T-ABC-456','Essence','1,5');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','7',to_date('31/03/23','DD/MM/RR'),'12','5','1150','Uccle','Rue du Marais','60','19,2','6','T-PQR-123','Essence','1,6');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('2','anne.martin@outlook.com','Martin','Anne','0456789126','2',to_date('02/01/23','DD/MM/RR'),'3','2','1020','Laeken','Rue de la Reine','22','5,4','5','T-MNO-789','Gaz','1,8');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('2','anne.martin@outlook.com','Martin','Anne','0456789126','2',to_date('02/01/23','DD/MM/RR'),'3','2','1020','Laeken','Rue de la Reine','22','39','2','T-DEF-789','Diesel','2');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('3','nicolas.petit@gmail.com','Petit','Nicolas','0456789127','3',to_date('04/01/23','DD/MM/RR'),'7','3','1030','Scaerbeek','Avenue de la chasse','40','161','3','T-GHI-123','Électrique','3');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('4','jean.dupont@gmail.com','Dupont','Jean','+32456789123','4',to_date('05/01/23','DD/MM/RR'),'8','4','1050','Ixelles','Avenue Louise','50','64','4','T-JKL-456','Hybride','2,5');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('5','marie.dubois@hotmail.com','Dubois','Marie','0456789124','5',to_date('06/01/23','DD/MM/RR'),'9','5','1150','Uccle','Rue du Marais','60','81','5','T-MNO-789','Gaz','1,8');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('88','pablo.moulin@gmail.com','Moulin','Pablo','0475555555','30',to_date('31/03/23','DD/MM/RR'),'1224','46','7830','Silly','Chaussée de Ghislenghien','57','1958,4','6','T-PQR-123','Essence','1,6');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('88','pablo.moulin@gmail.com','Moulin','Pablo','0475555555','108',to_date('25/05/23','DD/MM/RR'),'12','46','7830','Silly','Chaussée de Ghislenghien','57','19,2','6','T-PQR-123','Essence','1,6');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('88','pablo.moulin@gmail.com','Moulin','Pablo','0475555555','30',to_date('31/03/23','DD/MM/RR'),'1224','46','7830','Silly','Chaussée de Ghislenghien','57','1958,4','5','T-MNO-789','Gaz','1,8');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('88','pablo.moulin@gmail.com','Moulin','Pablo','0475555555','30',to_date('31/03/23','DD/MM/RR'),'1224','46','7830','Silly','Chaussée de Ghislenghien','57','2448','2','T-DEF-789','Diesel','2');
Insert into ORA6.API_LAFTC (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM,COUT,ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('88','pablo.moulin@gmail.com','Moulin','Pablo','0475555555','30',to_date('31/03/23','DD/MM/RR'),'1224','46','7830','Silly','Chaussée de Ghislenghien','57','1836','1','T-ABC-456','Essence','1,5');
REM INSERTING into ORA6.API_LOCATIONCLIENTADRESSE
SET DEFINE OFF;
Insert into ORA6.API_LOCATIONCLIENTADRESSE (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','1',to_date('01/01/23','DD/MM/RR'),'15','1','1000','Bruxelles','Avenue des Arts','10');
Insert into ORA6.API_LOCATIONCLIENTADRESSE (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('2','anne.martin@outlook.com','Martin','Anne','0456789126','2',to_date('02/01/23','DD/MM/RR'),'3','2','1020','Laeken','Rue de la Reine','22');
Insert into ORA6.API_LOCATIONCLIENTADRESSE (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('3','nicolas.petit@gmail.com','Petit','Nicolas','0456789127','3',to_date('04/01/23','DD/MM/RR'),'7','3','1030','Scaerbeek','Avenue de la chasse','40');
Insert into ORA6.API_LOCATIONCLIENTADRESSE (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('4','jean.dupont@gmail.com','Dupont','Jean','+32456789123','4',to_date('05/01/23','DD/MM/RR'),'8','4','1050','Ixelles','Avenue Louise','50');
Insert into ORA6.API_LOCATIONCLIENTADRESSE (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('5','marie.dubois@hotmail.com','Dubois','Marie','0456789124','5',to_date('06/01/23','DD/MM/RR'),'9','5','1150','Uccle','Rue du Marais','60');
Insert into ORA6.API_LOCATIONCLIENTADRESSE (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','6',to_date('02/01/23','DD/MM/RR'),'18','2','1020','Laeken','Rue de la Reine','22');
Insert into ORA6.API_LOCATIONCLIENTADRESSE (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','7',to_date('31/03/23','DD/MM/RR'),'12','5','1150','Uccle','Rue du Marais','60');
Insert into ORA6.API_LOCATIONCLIENTADRESSE (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('88','pablo.moulin@gmail.com','Moulin','Pablo','0475555555','30',to_date('31/03/23','DD/MM/RR'),'1224','46','7830','Silly','Chaussée de Ghislenghien','57');
Insert into ORA6.API_LOCATIONCLIENTADRESSE (ID_CLIENT,MAIL,NOM,PRENOM,TEL,ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('88','pablo.moulin@gmail.com','Moulin','Pablo','0475555555','108',to_date('25/05/23','DD/MM/RR'),'12','46','7830','Silly','Chaussée de Ghislenghien','57');
REM INSERTING into ORA6.API_LOCATIONPARADRESSE
SET DEFINE OFF;
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL,NOM,PRENOM,TEL,ID_CLIENT) values ('1','Avenue des Arts','10','1',to_date('01/01/23','DD/MM/RR'),'15','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','1');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL,NOM,PRENOM,TEL,ID_CLIENT) values ('2','Rue de la Reine','22','2',to_date('02/01/23','DD/MM/RR'),'3','anne.martin@outlook.com','Martin','Anne','0456789126','2');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL,NOM,PRENOM,TEL,ID_CLIENT) values ('2','Rue de la Reine','22','6',to_date('02/01/23','DD/MM/RR'),'18','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','1');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL,NOM,PRENOM,TEL,ID_CLIENT) values ('3','Avenue de la chasse','40','3',to_date('04/01/23','DD/MM/RR'),'7','nicolas.petit@gmail.com','Petit','Nicolas','0456789127','3');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL,NOM,PRENOM,TEL,ID_CLIENT) values ('4','Avenue Louise','50','4',to_date('05/01/23','DD/MM/RR'),'8','jean.dupont@gmail.com','Dupont','Jean','+32456789123','4');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL,NOM,PRENOM,TEL,ID_CLIENT) values ('5','Rue du Marais','60','5',to_date('06/01/23','DD/MM/RR'),'9','marie.dubois@hotmail.com','Dubois','Marie','0456789124','5');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL,NOM,PRENOM,TEL,ID_CLIENT) values ('5','Rue du Marais','60','7',to_date('31/03/23','DD/MM/RR'),'12','paul.leroy@yahoo.com','Leroy','Paul','+32456789125','1');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL,NOM,PRENOM,TEL,ID_CLIENT) values ('46','Chaussée de Ghislenghien','57','108',to_date('25/05/23','DD/MM/RR'),'12','pablo.moulin@gmail.com','Moulin','Pablo','0475555555','88');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL,NOM,PRENOM,TEL,ID_CLIENT) values ('46','Chaussée de Ghislenghien','57','30',to_date('31/03/23','DD/MM/RR'),'1224','pablo.moulin@gmail.com','Moulin','Pablo','0475555555','88');
REM INSERTING into ORA6.API_TAXIPARCLIENT
SET DEFINE OFF;
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('5','Dubois','Marie','T-MNO-789');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('4','Dupont','Jean','T-JKL-456');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('1','Leroy','Paul','T-STU-456');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('1','Leroy','Paul','T-ABC-456');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('1','Leroy','Paul','T-PQR-123');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('1','Leroy','Paul','T-JKL-456');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('1','Leroy','Paul','T-MNO-789');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('2','Martin','Anne','T-MNO-789');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('2','Martin','Anne','T-DEF-789');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('88','Moulin','Pablo','T-PQR-123');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('88','Moulin','Pablo','T-ABC-456');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('88','Moulin','Pablo','T-DEF-789');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('88','Moulin','Pablo','T-MNO-789');
Insert into ORA6.API_TAXIPARCLIENT (ID_CLIENT,NOM,PRENOM,TAXI) values ('3','Petit','Nicolas','T-GHI-123');
--------------------------------------------------------
--  DDL for Index API_ADRESSE_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."API_ADRESSE_UK1" ON "ORA6"."API_ADRESSE" ("CP", "LOCALITE", "RUE", "NUM") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index API_TAXI_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."API_TAXI_UK1" ON "ORA6"."API_TAXI" ("IMMATRICULATION") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index API_TCLIENT_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."API_TCLIENT_UK1" ON "ORA6"."API_TCLIENT" ("MAIL") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C0010373
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."SYS_C0010373" ON "ORA6"."API_ADRESSE" ("ID_ADRESSE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index API_ADRESSE_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."API_ADRESSE_UK1" ON "ORA6"."API_ADRESSE" ("CP", "LOCALITE", "RUE", "NUM") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C0010384
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."SYS_C0010384" ON "ORA6"."API_FACTURE" ("ID_LOCATION", "ID_TAXI") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C0010380
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."SYS_C0010380" ON "ORA6"."API_LOCATION" ("ID_LOCATION") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C0010375
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."SYS_C0010375" ON "ORA6"."API_TAXI" ("ID_TAXI") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index API_TAXI_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."API_TAXI_UK1" ON "ORA6"."API_TAXI" ("IMMATRICULATION") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C0010368
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."SYS_C0010368" ON "ORA6"."API_TCLIENT" ("ID_CLIENT") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index API_TCLIENT_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "ORA6"."API_TCLIENT_UK1" ON "ORA6"."API_TCLIENT" ("MAIL") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Trigger API_ADRESSE_TRG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "ORA6"."API_ADRESSE_TRG" 
BEFORE INSERT ON API_ADRESSE 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID_ADRESSE IS NULL THEN
      SELECT API_ADRESSE_SEQ.NEXTVAL INTO :NEW.ID_ADRESSE FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "ORA6"."API_ADRESSE_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger API_LOCATION_TRG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "ORA6"."API_LOCATION_TRG" 
BEFORE INSERT ON API_LOCATION 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID_LOCATION IS NULL THEN
      SELECT API_LOCATION_SEQ.NEXTVAL INTO :NEW.ID_LOCATION FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "ORA6"."API_LOCATION_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger API_TAXI_TRG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "ORA6"."API_TAXI_TRG" 
BEFORE INSERT ON API_TAXI 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID_TAXI IS NULL THEN
      SELECT API_TAXI_SEQ.NEXTVAL INTO :NEW.ID_TAXI FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "ORA6"."API_TAXI_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger API_TCLIENT_TRG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "ORA6"."API_TCLIENT_TRG" 
BEFORE INSERT ON API_TCLIENT 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID_CLIENT IS NULL THEN
      SELECT API_TCLIENT_SEQ.NEXTVAL INTO :NEW.ID_CLIENT FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "ORA6"."API_TCLIENT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger API_TRIGGER_CALCUL_PRIX_FAC
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "ORA6"."API_TRIGGER_CALCUL_PRIX_FAC" 
BEFORE INSERT OR UPDATE ON API_FACTURE
FOR EACH ROW
BEGIN

    :NEW.COUT:=api_fonc_get_prix_newfac(:NEW.ID_TAXI, :NEW.ID_LOCATION);

END;
/
ALTER TRIGGER "ORA6"."API_TRIGGER_CALCUL_PRIX_FAC" ENABLE;
--------------------------------------------------------
--  DDL for Trigger API_ADRESSE_TRG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "ORA6"."API_ADRESSE_TRG" 
BEFORE INSERT ON API_ADRESSE 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID_ADRESSE IS NULL THEN
      SELECT API_ADRESSE_SEQ.NEXTVAL INTO :NEW.ID_ADRESSE FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "ORA6"."API_ADRESSE_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger API_TRIGGER_CALCUL_PRIX_FAC
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "ORA6"."API_TRIGGER_CALCUL_PRIX_FAC" 
BEFORE INSERT OR UPDATE ON API_FACTURE
FOR EACH ROW
BEGIN

    :NEW.COUT:=api_fonc_get_prix_newfac(:NEW.ID_TAXI, :NEW.ID_LOCATION);

END;
/
ALTER TRIGGER "ORA6"."API_TRIGGER_CALCUL_PRIX_FAC" ENABLE;
--------------------------------------------------------
--  DDL for Trigger API_LOCATION_TRG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "ORA6"."API_LOCATION_TRG" 
BEFORE INSERT ON API_LOCATION 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID_LOCATION IS NULL THEN
      SELECT API_LOCATION_SEQ.NEXTVAL INTO :NEW.ID_LOCATION FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "ORA6"."API_LOCATION_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger API_TAXI_TRG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "ORA6"."API_TAXI_TRG" 
BEFORE INSERT ON API_TAXI 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID_TAXI IS NULL THEN
      SELECT API_TAXI_SEQ.NEXTVAL INTO :NEW.ID_TAXI FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "ORA6"."API_TAXI_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger API_TCLIENT_TRG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "ORA6"."API_TCLIENT_TRG" 
BEFORE INSERT ON API_TCLIENT 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID_CLIENT IS NULL THEN
      SELECT API_TCLIENT_SEQ.NEXTVAL INTO :NEW.ID_CLIENT FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "ORA6"."API_TCLIENT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Procedure API_PROC_CREATE_CLIENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "ORA6"."API_PROC_CREATE_CLIENT" (
  pMail IN api_tclient.mail%type, 
  pNom IN api_tclient.nom%type, 
  pPrenom IN api_tclient.prenom%type, 
  pTel IN api_tclient.tel%type, 
  idClient OUT api_tclient.id_client%type
) 
IS 
BEGIN 

  INSERT INTO API_TCLIENT(MAIL, NOM, PRENOM, TEL) 
  VALUES(pMail, pNom, pPrenom, pTel) 
  RETURNING id_client INTO idClient; 

  COMMIT; 

  -- Returning permet de retourner des valeurs, ici l'id du client
  -- Pour valider mon ajout, je dois faire un commit
END;

/
--------------------------------------------------------
--  DDL for Procedure API_PROC_CREATE_LOCATION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "ORA6"."API_PROC_CREATE_LOCATION" (
  pdate IN api_location.dateloc%type, 
  pkmtotal IN api_location.kmtotal%type, 
  pidadresse IN api_location.id_adresse%type, 
  pidclient IN api_location.id_client%type, 
  idLoc OUT api_location.id_location%type
) 
IS 
BEGIN 

  INSERT INTO API_LOCATION(DATELOC, KMTOTAL, ID_ADRESSE, ID_CLIENT) 
  VALUES(pdate, pkmtotal, pidadresse, pidclient) 
  RETURNING id_location INTO idLoc; 

  COMMIT; 
END;

/
--------------------------------------------------------
--  DDL for Procedure API_PROC_INSERT_FAC
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "ORA6"."API_PROC_INSERT_FAC" 
(fidtax api_facture.id_taxi%type, fidloc api_facture.id_location%type, fdateloc api_location.dateloc%type)
IS
    v_utilisation number;

BEGIN

    SELECT COUNT(*) INTO v_utilisation FROM api_facture fac 
    INNER JOIN api_location loc ON loc.id_location = fac.id_location 
    WHERE fac.id_taxi = fidtax AND loc.dateloc = to_date(fdateloc);

    dbms_output.put_line(v_utilisation);

    IF v_utilisation >= 10 THEN
        raise_application_error(-20100,'taxi trop utilisé');
    END IF;

    INSERT INTO api_facture(id_location, id_taxi) VALUES(fidloc, fidtax);

END;

/
--------------------------------------------------------
--  DDL for Function API_FONC_GET_PRIX_NEWFAC
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "ORA6"."API_FONC_GET_PRIX_NEWFAC" 
(fidtax api_facture.id_taxi%type, fidloc api_facture.id_location%type)
RETURN NUMBER
IS

    v_prixkm number;
    v_kmtotal number;

BEGIN

    SELECT prixkm INTO v_prixkm FROM api_taxi WHERE id_taxi=fidtax;
    SELECT kmtotal INTO v_kmtotal FROM api_location WHERE id_location=fidloc;

    RETURN v_prixkm*v_kmtotal;

END;

/
--------------------------------------------------------
--  DDL for Function API_FONC_LOCATIONS_TAXI
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "ORA6"."API_FONC_LOCATIONS_TAXI" 
(fImmatriculation api_taxi.immatriculation%type)
RETURN SYS_REFCURSOR
IS

    c_taxiloc SYS_REFCURSOR;

BEGIN

    OPEN c_taxiloc FOR SELECT loc.id_location id_location, loc.dateloc dateloc, loc.kmtotal kmtotal, adr.id_adresse id_adresse, adr.rue rue, adr.num num, adr.cp cp, adr.localite localite, cli.id_client id_client, cli.mail mail, cli.nom nom, cli.prenom prenom, cli.tel tel
    FROM api_location loc
    JOIN api_adresse adr
    ON loc.id_adresse = adr.id_adresse
    JOIN api_tclient cli
    ON loc.id_client = cli.id_client
    JOIN api_facture fac
    ON loc.id_location = fac.id_location
    JOIN api_taxi t
    ON fac.id_taxi = t.id_taxi
    WHERE t.immatriculation = UPPER(fImmatriculation);

    --Ici je fais une jointure entre les tables pour récupérer les infos de la location, de l'adresse, du client, de la facture et du taxi
    --Je fais un upper sur l'immatriculation pour que la fonction fonctionne même si l'utilisateur rentre une immatriculation en minuscule
    --J'ouvre mon curseur pour lui attribuer les résultats de ma requête

    RETURN c_taxiloc;

END;

/
--------------------------------------------------------
--  DDL for Function API_FONC_NOMBRE_LOCATION_CLIENT
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "ORA6"."API_FONC_NOMBRE_LOCATION_CLIENT" 
(fmail api_tclient.mail%type)
RETURN NUMBER
IS

    nombre number;

BEGIN

    SELECT COUNT(*) INTO nombre FROM api_location al JOIN api_tclient ac ON al.id_client = ac.id_client WHERE upper(ac.mail) = upper(fmail);
    -- utilisation de upper pour éviter les problèmes de casse
    -- COUNT() retourne 0 si aucun résultat n'est trouvé cela évite de gérer une exception


    RETURN nombre;

END;

/
--------------------------------------------------------
--  DDL for Function API_FONC_PRIX_LOCATION
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "ORA6"."API_FONC_PRIX_LOCATION" 
(fid_loc api_location.id_location%type)
RETURN NUMBER
IS

    v_nombre number;
    v_mail api_tclient.mail%type;
    v_reduc number:= 1;

BEGIN

    SELECT mail INTO v_mail FROM api_location al JOIN api_tclient ac ON al.id_client = ac.id_client WHERE al.id_location = fid_loc;

    v_nombre := api_fonc_nombre_location_client(v_mail);
    dbms_output.put_line('Le client possède : ' || v_nombre || ' locations');

    IF v_nombre > 5 THEN
        --Pour essayer ce code, je remplace le 5 par 1 et je regarde
        v_reduc := 0.9;
    END IF;

    SELECT SUM(cout)*v_reduc INTO v_nombre FROM api_facture WHERE id_location = fid_loc;

    --v_reduc est égal a 1 si pas de réduction

    -- exception de mail

    RETURN v_nombre;

    Exception
        WHEN NO_DATA_FOUND THEN
            raise_application_error(-20101,'mail non trouvé');

END;

/
--------------------------------------------------------
--  DDL for Function API_FONC_PRIX_TOTAL_LOC_CLIENT
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "ORA6"."API_FONC_PRIX_TOTAL_LOC_CLIENT" 
(fmail api_tclient.mail%type)
RETURN NUMBER
IS

    nombre number;

BEGIN

    SELECT SUM(cout) INTO nombre FROM api_facture af JOIN api_location al ON af.id_location = al.id_location JOIN api_tclient ac ON al.id_client = ac.id_client WHERE upper(ac.mail) = upper(fmail);

    -- utilisation de upper pour éviter les problèmes de casse
    -- SUM() retourne 0 si aucun résultat n'est trouvé cela évite de gérer une exception
    RETURN nombre;

END;

/
--------------------------------------------------------
--  Constraints for Table API_ADRESSE
--------------------------------------------------------

  ALTER TABLE "ORA6"."API_ADRESSE" MODIFY ("CP" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_ADRESSE" MODIFY ("LOCALITE" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_ADRESSE" MODIFY ("RUE" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_ADRESSE" MODIFY ("NUM" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_ADRESSE" ADD PRIMARY KEY ("ID_ADRESSE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "ORA6"."API_ADRESSE" ADD CONSTRAINT "API_ADRESSE_UK1" UNIQUE ("CP", "LOCALITE", "RUE", "NUM")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "ORA6"."API_ADRESSE" ADD CONSTRAINT "API_ADRESSE_CHK1" CHECK (cp >= 1000 AND cp <= 9999) ENABLE;
--------------------------------------------------------
--  Constraints for Table API_FACTURE
--------------------------------------------------------

  ALTER TABLE "ORA6"."API_FACTURE" MODIFY ("COUT" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_FACTURE" ADD PRIMARY KEY ("ID_LOCATION", "ID_TAXI")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "ORA6"."API_FACTURE" ADD CONSTRAINT "API_FACTURE_CHK1" CHECK (cout >= 0) ENABLE;
--------------------------------------------------------
--  Constraints for Table API_LOCATION
--------------------------------------------------------

  ALTER TABLE "ORA6"."API_LOCATION" MODIFY ("DATELOC" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_LOCATION" MODIFY ("KMTOTAL" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_LOCATION" MODIFY ("ID_ADRESSE" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_LOCATION" MODIFY ("ID_CLIENT" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_LOCATION" ADD PRIMARY KEY ("ID_LOCATION")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "ORA6"."API_LOCATION" ADD CONSTRAINT "API_LOCATION_CHK1" CHECK (kmtotal >= 0) ENABLE;
--------------------------------------------------------
--  Constraints for Table API_TAXI
--------------------------------------------------------

  ALTER TABLE "ORA6"."API_TAXI" ADD PRIMARY KEY ("ID_TAXI")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "ORA6"."API_TAXI" ADD CONSTRAINT "API_TAXI_UK1" UNIQUE ("IMMATRICULATION")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "ORA6"."API_TAXI" ADD CONSTRAINT "API_TAXI_CHK1" CHECK (prixkm >= 0) ENABLE;
  ALTER TABLE "ORA6"."API_TAXI" ADD CONSTRAINT "API_TAXI_CHK2" CHECK (REGEXP_LIKE(immatriculation, 'T-[A-Z]{3}-[0-9]{3}')) DISABLE;
  ALTER TABLE "ORA6"."API_TAXI" MODIFY ("IMMATRICULATION" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table API_TCLIENT
--------------------------------------------------------

  ALTER TABLE "ORA6"."API_TCLIENT" MODIFY ("MAIL" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_TCLIENT" MODIFY ("NOM" NOT NULL ENABLE);
  ALTER TABLE "ORA6"."API_TCLIENT" ADD PRIMARY KEY ("ID_CLIENT")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "ORA6"."API_TCLIENT" ADD CONSTRAINT "API_TCLIENT_UK1" UNIQUE ("MAIL")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "ORA6"."API_TCLIENT" ADD CONSTRAINT "API_TCLIENT_CHK1" CHECK (REGEXP_LIKE(mail, '@[a-zA-Z].[a-zA-Z]')) ENABLE;
  ALTER TABLE "ORA6"."API_TCLIENT" ADD CONSTRAINT "API_TCLIENT_CHK2" CHECK (REGEXP_LIKE(tel, '^(\+32|0)[0-9]{9}$')) DISABLE;
--------------------------------------------------------
--  Ref Constraints for Table API_FACTURE
--------------------------------------------------------

  ALTER TABLE "ORA6"."API_FACTURE" ADD FOREIGN KEY ("ID_LOCATION")
	  REFERENCES "ORA6"."API_LOCATION" ("ID_LOCATION") ENABLE;
  ALTER TABLE "ORA6"."API_FACTURE" ADD FOREIGN KEY ("ID_TAXI")
	  REFERENCES "ORA6"."API_TAXI" ("ID_TAXI") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table API_LOCATION
--------------------------------------------------------

  ALTER TABLE "ORA6"."API_LOCATION" ADD FOREIGN KEY ("ID_ADRESSE")
	  REFERENCES "ORA6"."API_ADRESSE" ("ID_ADRESSE") ENABLE;
  ALTER TABLE "ORA6"."API_LOCATION" ADD FOREIGN KEY ("ID_CLIENT")
	  REFERENCES "ORA6"."API_TCLIENT" ("ID_CLIENT") ENABLE;
