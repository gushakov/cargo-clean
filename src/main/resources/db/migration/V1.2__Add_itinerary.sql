CREATE TABLE public.leg
(
    cargo_tracking_id varchar   NOT NULL,
    voyage_number     varchar   NOT NULL,
    load_location     varchar   NOT NULL,
    unload_location   varchar   NOT NULL,
    load_time         timestamp NOT NULL,
    unload_time       timestamp NOT NULL,
    leg_index         int4      NOT NULL
);

ALTER TABLE public.leg
    ADD CONSTRAINT leg_cargo_fk FOREIGN KEY (cargo_tracking_id) REFERENCES public.cargo (tracking_id) ON DELETE CASCADE;
ALTER TABLE public.leg
    ADD CONSTRAINT leg_load_location_fk FOREIGN KEY (load_location) REFERENCES public."location" (unlocode);
ALTER TABLE public.leg
    ADD CONSTRAINT leg_unload_location_fk FOREIGN KEY (unload_location) REFERENCES public."location" (unlocode);
