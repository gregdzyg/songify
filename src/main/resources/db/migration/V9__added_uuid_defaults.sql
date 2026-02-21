
ALTER TABLE album
    ALTER COLUMN uuid SET DEFAULT gen_random_uuid();
ALTER TABLE artist
    ALTER COLUMN uuid SET DEFAULT gen_random_uuid();
ALTER TABLE genre
    ALTER COLUMN uuid SET DEFAULT gen_random_uuid();

ALTER TABLE album
    ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE artist
    ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE genre
    ALTER COLUMN uuid SET NOT NULL;