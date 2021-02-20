CREATE TABLE IF NOT EXISTS "public"."sale" (
    "id" SERIAL PRIMARY KEY,
    "customer_id" INTEGER REFERENCES customer(id),
    "item_id" INTEGER REFERENCES item(id),
    "date" DATE
);