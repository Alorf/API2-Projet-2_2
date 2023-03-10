--------------------------------------------------------
--  Fichier créé - jeudi-février-16-2023   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence API_ADRESSE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "ORA6"."API_ADRESSE_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 26 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
--------------------------------------------------------
--  DDL for Sequence API_LOCATION_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "ORA6"."API_LOCATION_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 7 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
--------------------------------------------------------
--  DDL for Sequence API_TAXI_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "ORA6"."API_TAXI_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 6 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
                                                                                                                                                                                                                                                                                                                                            --------------------------------------------------------
--  DDL for Table API_ADRESSE
--------------------------------------------------------

  CREATE TABLE "ORA6"."API_ADRESSE" 
   (	"ID_ADRESSE" NUMBER(*,0), 
	"CP" NUMBER(*,0), 
	"LOCALITE" VARCHAR2(50 BYTE), 
	"RUE" VARCHAR2(50 BYTE), 
	"NUM" VARCHAR2(50 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                --------------------------------------------------------
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
--  DDL for View API_KMPARTAXI
--------------------------------------------------------

  CREATE OR REPLACE FORCE EDITIONABLE VIEW "ORA6"."API_KMPARTAXI" ("TAXI", "distance parcourue") AS 
  SELECT t.immatriculation taxi, SUM(l.kmtotal) || ' km' as "distance parcourue"  FROM API_TAXI t
INNER JOIN API_Facture f
ON f.id_taxi = t.id_taxi
INNER JOIN API_Location l
ON l.id_location = f.id_location
GROUP BY t.id_taxi, t.immatriculation
ORDER BY SUM(l.kmtotal)
;
--------------------------------------------------------
--  DDL for View API_LOCATIONPARADRESSE
--------------------------------------------------------

  CREATE OR REPLACE FORCE EDITIONABLE VIEW "ORA6"."API_LOCATIONPARADRESSE" ("ID_ADRESSE", "RUE", "NUM", "ID_LOCATION", "DATELOC", "KMTOTAL", "MAIL") AS 
  SELECT a.id_adresse, a.rue, a.num, l.id_location, l.dateLoc, l.kmTotal, c.mail
FROM API_Adresse a
JOIN API_Location l ON l.id_adresse = a.id_adresse
JOIN API_TClient c ON c.id_client = l.id_client
ORDER BY a.id_adresse
;
--------------------------------------------------------
--  DDL for View API_TAXIPARCLIENT
--------------------------------------------------------

  CREATE OR REPLACE FORCE EDITIONABLE VIEW "ORA6"."API_TAXIPARCLIENT" ("CLIENT", "TAXI") AS 
  SELECT DISTINCT c.nom ||' '|| c.prenom client, t.immatriculation taxi
FROM API_TClient c
JOIN API_Location l ON l.id_client = c.id_client
JOIN API_Facture f ON f.id_location = l.id_location
JOIN API_Taxi t ON t.id_taxi = f.id_taxi
ORDER BY CLIENT
;
REM INSERTING into ORA6.API_ADRESSE
SET DEFINE OFF;
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('1','1000','Bruxelles','Avenue des Arts','10');
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('2','1020','Laeken','Rue de la Reine','22');
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('3','1030','Scaerbeek','Avenue de la chasse','40');
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('4','1050','Ixelles','Avenue Louise','50');
Insert into ORA6.API_ADRESSE (ID_ADRESSE,CP,LOCALITE,RUE,NUM) values ('5','1150','Uccle','Rue du Marais','60');
REM INSERTING into ORA6.API_FACTURE
SET DEFINE OFF;
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('1','1','225');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('2','2','39');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('3','3','161');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('4','4','64');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('5','5','81');
Insert into ORA6.API_FACTURE (ID_LOCATION,ID_TAXI,COUT) values ('6','5','27');
REM INSERTING into ORA6.API_LOCATION
SET DEFINE OFF;
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('1',to_date('01/01/23','DD/MM/RR'),'15','1','1');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('2',to_date('02/01/23','DD/MM/RR'),'3','2','2');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('3',to_date('04/01/23','DD/MM/RR'),'7','3','3');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('4',to_date('05/01/23','DD/MM/RR'),'8','4','4');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('5',to_date('06/01/23','DD/MM/RR'),'9','5','5');
Insert into ORA6.API_LOCATION (ID_LOCATION,DATELOC,KMTOTAL,ID_ADRESSE,ID_CLIENT) values ('6',to_date('02/01/23','DD/MM/RR'),'18','1','1');
REM INSERTING into ORA6.API_TAXI
SET DEFINE OFF;
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('1','T-ABC-456','Essence','1,5');
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('2','T-DEF-789','Diesel','2');
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('3','T-GHI-123','Électrique','3');
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('4','T-JKL-456','Hybride','2,5');
Insert into ORA6.API_TAXI (ID_TAXI,IMMATRICULATION,CARBURANT,PRIXKM) values ('5','T-MNO-789','Gaz','1,8');
REM INSERTING into ORA6.API_TCLIENT
SET DEFINE OFF;
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('1','paul.leroy@yahoo.com','Leroy','Paul','+32456789125');
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('2','anne.martin@outlook.com','Martin','Anne','0456789126');
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('3','nicolas.petit@gmail.com','Petit','Nicolas','0456789127');
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('4','jean.dupont@gmail.com','Dupont','Jean','+32456789123');
Insert into ORA6.API_TCLIENT (ID_CLIENT,MAIL,NOM,PRENOM,TEL) values ('5','marie.dubois@hotmail.com','Dubois','Marie','0456789124');
REM INSERTING into ORA6.API_KMPARTAXI
SET DEFINE OFF;
Insert into ORA6.API_KMPARTAXI (TAXI,"distance parcourue") values ('T-DEF-789','3 km');
Insert into ORA6.API_KMPARTAXI (TAXI,"distance parcourue") values ('T-GHI-123','7 km');
Insert into ORA6.API_KMPARTAXI (TAXI,"distance parcourue") values ('T-JKL-456','8 km');
Insert into ORA6.API_KMPARTAXI (TAXI,"distance parcourue") values ('T-ABC-456','15 km');
Insert into ORA6.API_KMPARTAXI (TAXI,"distance parcourue") values ('T-MNO-789','27 km');
REM INSERTING into ORA6.API_LOCATIONPARADRESSE
SET DEFINE OFF;
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL) values ('1','Avenue des Arts','10','1',to_date('01/01/23','DD/MM/RR'),'15','paul.leroy@yahoo.com');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL) values ('1','Avenue des Arts','10','6',to_date('02/01/23','DD/MM/RR'),'18','paul.leroy@yahoo.com');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL) values ('2','Rue de la Reine','22','2',to_date('02/01/23','DD/MM/RR'),'3','anne.martin@outlook.com');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL) values ('3','Avenue de la chasse','40','3',to_date('04/01/23','DD/MM/RR'),'7','nicolas.petit@gmail.com');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL) values ('4','Avenue Louise','50','4',to_date('05/01/23','DD/MM/RR'),'8','jean.dupont@gmail.com');
Insert into ORA6.API_LOCATIONPARADRESSE (ID_ADRESSE,RUE,NUM,ID_LOCATION,DATELOC,KMTOTAL,MAIL) values ('5','Rue du Marais','60','5',to_date('06/01/23','DD/MM/RR'),'9','marie.dubois@hotmail.com');
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 --------------------------------------------------------
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
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    --------------------------------------------------------
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
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          --------------------------------------------------------
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
--  DDL for Procedure APICREATECLI
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "ORA6"."APICREATECLI" 
(
NUMCLI OUT NUMBER ,
 NOM IN VARCHAR2  
, PRENOM IN VARCHAR2  
, CP IN NUMBER  
, LOCALITE IN VARCHAR2  
, RUE IN VARCHAR2  
, NUM IN VARCHAR2  
, TEL IN VARCHAR2  
) AS 
BEGIN
INSERT INTO APICLIENT(NOM,PRENOM,CP,LOCALITE,RUE,NUM,TEL) 
VALUES(NOM,PRENOM,CP,LOCALITE,RUE,NUM,TEL);
SELECT APICLIENT_SEQ.CURRVAL INTO NUMCLI FROM DUAL;
COMMIT;
END APICREATECLI;


/
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               --------------------------------------------------------
--  DDL for Procedure APINOUVPROD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "ORA6"."APINOUVPROD" 
(codeprod IN CHAR,
descr IN VARCHAR2,
prix IN NUMBER)
is
begin
insert into apiproduit(numprod,description,phtva,stock,stockmin) values(codeprod,descr,prix,0,0);
commit;
end;


/
--------------------------------------------------------
--  DDL for Procedure APITELCLI
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "ORA6"."APITELCLI" 
(
NUMRECH IN NUMBER  
, TELRECH OUT VARCHAR2  
) AS 
BEGIN
select tel into telrech from apiclient where idclient=numrech;
END APITELCLI;


/
--------------------------------------------------------
--  DDL for Function APILISTECLILOCALITE
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "ORA6"."APILISTECLILOCALITE" 
(
LOCRECH IN VARCHAR2  
) RETURN SYS_REFCURSOR 
AS 
liste SYS_REFCURSOR;
BEGIN
open liste for SELECT * FROM apiclient WHERE localite=locrech;
return liste;
END APILISTECLILOCALITE;


/
--------------------------------------------------------
--  DDL for Function APILISTECLIS
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "ORA6"."APILISTECLIS" RETURN SYS_REFCURSOR  AS 
liste SYS_REFCURSOR;
BEGIN
open liste for SELECT nom,prenom  FROM apiclient ;
RETURN liste;
END APILISTECLIS;


/
--------------------------------------------------------
--  DDL for Function APINOMCLI
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "ORA6"."APINOMCLI" 
(
NUMRECH IN NUMBER  
) RETURN VARCHAR2 AS 
nomtrouve varchar2(30);
BEGIN
select nom into nomtrouve from apiclient where idclient=numrech;
return nomtrouve;
END APINOMCLI;


/
--------------------------------------------------------
--  DDL for Function APIREADCLI
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "ORA6"."APIREADCLI" 
(
NUMRECH IN NUMBER
) RETURN SYS_REFCURSOR 
AS 
UNCLIENT SYS_REFCURSOR;
BEGIN
open UNCLIENT for SELECT * FROM apiclient WHERE idclient=numrech;
return UNCLIENT;
END APIREADCLI;


/
--------------------------------------------------------
--  DDL for Function LISTECLIS
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "ORA6"."LISTECLIS" RETURN SYS_REFCURSOR  AS 
liste SYS_REFCURSOR;
BEGIN
open liste for SELECT nom,prenom  FROM apiclient ;
RETURN liste;
END LISTECLIS;


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
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    --------------------------------------------------------
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
