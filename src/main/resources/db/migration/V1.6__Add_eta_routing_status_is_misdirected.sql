ALTER TABLE public.cargo
    ADD eta timestamp NULL;
ALTER TABLE public.cargo
    ADD routing_status varchar NOT NULL;
ALTER TABLE public.cargo
    ADD is_misdirected boolean NOT NULL;
