ALTER TABLE album
    ADD created_on TIMESTAMP WITH TIME ZONE DEFAULT now();

ALTER TABLE album
    ADD uuid UUID;

ALTER TABLE artist
    ADD created_on TIMESTAMP WITH TIME ZONE DEFAULT now();

ALTER TABLE artist
    ADD uuid UUID;

ALTER TABLE genre
    ADD created_on TIMESTAMP WITH TIME ZONE DEFAULT now();

ALTER TABLE genre
    ADD uuid UUID;