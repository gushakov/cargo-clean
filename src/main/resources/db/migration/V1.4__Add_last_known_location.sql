ALTER TABLE public.cargo ADD last_known_location varchar NULL;
ALTER TABLE public.cargo ADD CONSTRAINT cargo_last_known_location_fk FOREIGN KEY (last_known_location) REFERENCES public."location"(unlocode);
