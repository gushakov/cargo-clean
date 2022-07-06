ALTER TABLE public.cargo ADD spec_origin_id integer NULL;
ALTER TABLE public.cargo ADD spec_destination_id integer NULL;
ALTER TABLE public.cargo ADD spec_arrival_deadline timestamp NOT NULL;
ALTER TABLE public.cargo ADD CONSTRAINT cargo_spec_origin_fk FOREIGN KEY (spec_origin_id) REFERENCES location(id);
ALTER TABLE public.cargo ADD CONSTRAINT cargo_spec_destination_fk FOREIGN KEY (spec_destination_id) REFERENCES location(id);
