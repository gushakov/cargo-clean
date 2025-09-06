CREATE TABLE public.consignment
(
    consignment_id           varchar NOT NULL,
    quantity_in_containers   int4    NOT NULL,
    cargo_tracking_id        varchar NOT NULL,
    CONSTRAINT consignment_consignment_id_pk PRIMARY KEY (consignment_id)
);

ALTER TABLE public.consignment
    ADD CONSTRAINT consignment_cargo_fk FOREIGN KEY (cargo_tracking_id) REFERENCES public.cargo (tracking_id) ON DELETE CASCADE;