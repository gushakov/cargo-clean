CREATE TABLE public.consignment
(
    id                     varchar NOT NULL,
    cargo_tracking_id      varchar NULL,
    quantity_in_containers int4    NOT NULL DEFAULT 0,
    "version"              int NULL,
    CONSTRAINT consignment_pk PRIMARY KEY (id)
);

ALTER TABLE public.consignment
    ADD CONSTRAINT consignment_cargo_fk FOREIGN KEY (cargo_tracking_id) REFERENCES public.cargo (tracking_id);