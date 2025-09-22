CREATE TABLE images (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    src TEXT NOT NULL
);

-- optional index for fast lookups by product
CREATE INDEX idx_images_product_id ON images(product_id);
