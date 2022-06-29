CREATE TABLE public.cargo
(
    id               serial4 NOT NULL,
    tracking_id       varchar NULL,
    origin_id        int4 NULL,
    transport_status varchar NOT NULL,
    CONSTRAINT cargo_pk PRIMARY KEY (id),
    CONSTRAINT tracking_id_unique UNIQUE (tracking_id)
);