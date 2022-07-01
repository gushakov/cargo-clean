ALTER TABLE public.cargo ADD spec_origin_id integer NULL;
ALTER TABLE public.cargo ADD spec_destination_id integer NULL;
ALTER TABLE public.cargo ADD spec_arrival_deadline timestamp NOT NULL;
