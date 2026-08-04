ALTER TABLE movie
ALTER COLUMN duration TYPE NUMERIC(21, 0)
USING duration::numeric;