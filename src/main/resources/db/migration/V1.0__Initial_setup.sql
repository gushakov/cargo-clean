CREATE TABLE public.location
(
    id       serial4 NOT NULL,
    unlocode varchar NOT NULL,
    "name"   varchar NULL,
    CONSTRAINT newtable_pk PRIMARY KEY (id),
    CONSTRAINT unlocode_unique UNIQUE (unlocode)
);

INSERT INTO public.location (unlocode, "name")
VALUES ('JNTKO', 'Tokyo');

INSERT INTO public.location (unlocode, "name")
VALUES ('NLRTM', 'Rotterdam');

INSERT INTO public.location (unlocode, "name")
VALUES ('USDAL', 'Dallas');

INSERT INTO public.location (unlocode, "name")
VALUES ('SEGOT', 'Göteborg');

INSERT INTO public.location (unlocode, "name")
VALUES ('CNHGH', 'Hangzhou');

INSERT INTO public.location (unlocode, "name")
VALUES ('CNHKG', 'Hongkong');

INSERT INTO public.location (unlocode, "name")
VALUES ('DEHAM', 'Hamburg');

INSERT INTO public.location (unlocode, "name")
VALUES ('SESTO', 'Stockholm');

INSERT INTO public.location (unlocode, "name")
VALUES ('USNYC', 'New York');

INSERT INTO public.location (unlocode, "name")
VALUES ('FIHEL', 'Helsinki');

INSERT INTO public.location (unlocode, "name")
VALUES ('USCHI', 'Chicago');

INSERT INTO public.location (unlocode, "name")
VALUES ('CNSHA', 'Shanghai');

INSERT INTO public.location (unlocode, "name")
VALUES ('AUMEL', 'Melbourne');

