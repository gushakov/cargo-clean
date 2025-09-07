CREATE TABLE public.consignment
(
    consignment_id         varchar NOT NULL,
    cargo_tracking_id      varchar NOT NULL,
    quantity_in_containers int4    NOT NULL,
    "version"              int NULL,
    CONSTRAINT consignment_pk PRIMARY KEY (consignment_id)
);

CREATE TABLE public.cargo_consignment
(
    cargo_tracking_id varchar NOT NULL,
    consignment_id    varchar NOT NULL,
    CONSTRAINT cargo_consignment_pk PRIMARY KEY (cargo_tracking_id, consignment_id)
);

ALTER TABLE public.cargo_consignment
    ADD CONSTRAINT cargo_consignment_cargo_fk FOREIGN KEY (cargo_tracking_id) REFERENCES public.cargo (tracking_id);

ALTER TABLE public.cargo_consignment
    ADD CONSTRAINT cargo_consignment_consignment_fk FOREIGN KEY (consignment_id) REFERENCES public.consignment (consignment_id);