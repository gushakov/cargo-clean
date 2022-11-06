ALTER TABLE public.cargo
    ADD next_expected_handling_event_type varchar NULL;
ALTER TABLE public.cargo
    ADD next_expected_location varchar NULL;
ALTER TABLE public.cargo
    ADD next_expected_voyage varchar NULL;
ALTER TABLE public.cargo
    ADD CONSTRAINT cargo_next_expected_location_fk FOREIGN KEY (next_expected_location) REFERENCES public."location" (unlocode);

