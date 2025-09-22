CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    vendor TEXT,
    handle TEXT,
    feature_image JSONB,
    variants_json JSONB
);

-- optional GIN index for JSONB querying
CREATE INDEX idx_products_variants_json ON products USING GIN (variants_json);