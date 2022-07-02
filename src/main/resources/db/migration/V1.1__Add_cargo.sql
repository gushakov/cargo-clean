CREATE TABLE public.cargo (
                              id serial4 NOT NULL,
                              tracking_id varchar NULL,
                              origin_id int4 NULL,
                              transport_status varchar NOT NULL,
                              CONSTRAINT cargo_id_pk PRIMARY KEY (id),
                              CONSTRAINT cargo_tracking_id_unique UNIQUE (tracking_id)
);

ALTER TABLE public.cargo ADD CONSTRAINT cargo_fk FOREIGN KEY (origin_id) REFERENCES public."location"(id);
