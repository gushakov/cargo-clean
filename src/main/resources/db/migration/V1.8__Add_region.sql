ALTER TABLE public."location"
    ADD region varchar NULL;

UPDATE public."location"
SET region='Asia'
WHERE unlocode = 'JNTKO';
UPDATE public."location"
SET region='Europe'
WHERE unlocode = 'NLRTM';
UPDATE public."location"
SET region='NorthAmerica'
WHERE unlocode = 'USDAL';
UPDATE public."location"
SET region='Europe'
WHERE unlocode = 'SEGOT';
UPDATE public."location"
SET region='Asia'
WHERE unlocode = 'CNHGH';
UPDATE public."location"
SET region='Asia'
WHERE unlocode = 'CNHKG';
UPDATE public."location"
SET region='Europe'
WHERE unlocode = 'DEHAM';
UPDATE public."location"
SET region='Europe'
WHERE unlocode = 'SESTO';
UPDATE public."location"
SET region='NorthAmerica'
WHERE unlocode = 'USNYC';
UPDATE public."location"
SET region='Europe'
WHERE unlocode = 'FIHEL';
UPDATE public."location"
SET region='NorthAmerica'
WHERE unlocode = 'USCHI';
UPDATE public."location"
SET region='Asia'
WHERE unlocode = 'CNSHA';
UPDATE public."location"
SET region='Oceania'
WHERE unlocode = 'AUMEL';

INSERT INTO public."location"
    (unlocode, "name", region)
VALUES ('NZAKL', 'Auckland', 'Oceania');
INSERT INTO public."location"
    (unlocode, "name", region)
VALUES ('PFPPT', 'Papeete', 'Oceania');
