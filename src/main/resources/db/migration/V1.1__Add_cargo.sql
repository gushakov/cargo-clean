CREATE TABLE public.cargo
(
    tracking_id           varchar   NOT NULL,
    origin                varchar   NOT NULL,
    transport_status      varchar   NOT NULL,
    spec_origin           varchar   NOT NULL,
    spec_destination      varchar   NOT NULL,
    spec_arrival_deadline timestamp NOT NULL,
    "version"             varchar   NOT NULL DEFAULT '1':: character varying,
    CONSTRAINT cargo_tracking_id_pk PRIMARY KEY (tracking_id)
);

ALTER TABLE public.cargo
    ADD CONSTRAINT cargo_origin_fk FOREIGN KEY (origin) REFERENCES public."location" (unlocode);
ALTER TABLE public.cargo
    ADD CONSTRAINT cargo_spec_destination_fk FOREIGN KEY (spec_destination) REFERENCES public."location" (unlocode);
ALTER TABLE public.cargo
    ADD CONSTRAINT cargo_spec_origin_fk FOREIGN KEY (spec_origin) REFERENCES public."location" (unlocode);