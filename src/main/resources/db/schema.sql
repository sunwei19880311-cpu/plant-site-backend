-- ============================================================
-- 植物网站 PostgreSQL 初始化脚本
-- 数据库名建议：plant_site
--   CREATE DATABASE plant_site ENCODING 'UTF8';
-- 注：已修正原 SQL 中 plant_code 行末缺失的逗号
-- ============================================================

-- 1. 植物分类字典表 plant_category
CREATE TABLE IF NOT EXISTS plant_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    category_desc VARCHAR(100),
    icon VARCHAR(50),
    sort INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);
COMMENT ON TABLE plant_category IS '植物分类字典表';

-- 2. 植物主表 plant
CREATE TABLE IF NOT EXISTS plant (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    alias VARCHAR(100),
    plant_code VARCHAR(20) UNIQUE,
    scientific_name VARCHAR(100),
    family_genus VARCHAR(100),
    category_id BIGINT NOT NULL REFERENCES plant_category(id),
    difficulty VARCHAR(10) CHECK (difficulty IN ('新手','中等','进阶')),
    tags VARCHAR(255),
    summary TEXT,
    light_desc TEXT,
    water_desc TEXT,
    soil_desc TEXT,
    temp_desc TEXT,
    fertilize_desc TEXT,
    pest_desc TEXT,
    propagate_desc TEXT,
    poem_content VARCHAR(200),
    poem_explain VARCHAR(300),
    search_tags VARCHAR(255),
    meaning TEXT,
    status SMALLINT DEFAULT 1 CHECK (status IN (0,1)),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);
COMMENT ON TABLE plant IS '植物基础信息主表';
CREATE INDEX IF NOT EXISTS idx_plant_category ON plant(category_id);
CREATE INDEX IF NOT EXISTS idx_plant_status ON plant(status);
CREATE INDEX IF NOT EXISTS idx_plant_tags ON plant(tags);

-- 3. 植物图片附表 plant_image
CREATE TABLE IF NOT EXISTS plant_image (
    id BIGSERIAL PRIMARY KEY,
    plant_id BIGINT NOT NULL REFERENCES plant(id) ON DELETE CASCADE,
    object_key VARCHAR(255) NOT NULL,
    img_desc VARCHAR(128),
    sort INT DEFAULT 0,
    is_cover BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);
COMMENT ON TABLE plant_image IS '植物图片表，轮播图/封面图';
CREATE INDEX IF NOT EXISTS idx_plant_image_plant_id ON plant_image(plant_id);

-- 4. 养护教程分步表 plant_tutorial
CREATE TABLE IF NOT EXISTS plant_tutorial (
    id BIGSERIAL PRIMARY KEY,
    plant_id BIGINT NOT NULL REFERENCES plant(id) ON DELETE CASCADE,
    step_no INT NOT NULL,
    step_title VARCHAR(200) NOT NULL,
    step_content TEXT NOT NULL,
    object_key VARCHAR(255),
    sort INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);
COMMENT ON TABLE plant_tutorial IS '养护教程分步表';
CREATE INDEX IF NOT EXISTS idx_plant_tutorial_plant_id ON plant_tutorial(plant_id);

-- 5. 养护故障问题表 plant_issue
CREATE TABLE IF NOT EXISTS plant_issue (
    id BIGSERIAL PRIMARY KEY,
    plant_id BIGINT NOT NULL REFERENCES plant(id) ON DELETE CASCADE,
    symptom VARCHAR(128) NOT NULL,
    phenomenon TEXT,
    reason TEXT,
    solution TEXT,
    sort INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);
COMMENT ON TABLE plant_issue IS '植物养护故障/黄叶烂根问题表';
CREATE INDEX IF NOT EXISTS idx_plant_issue_plant_id ON plant_issue(plant_id);

-- 6. CPS 商品表 cps_product
CREATE TABLE IF NOT EXISTS cps_product (
    id BIGSERIAL PRIMARY KEY,
    plant_id BIGINT REFERENCES plant(id) ON DELETE SET NULL,
    title VARCHAR(100) NOT NULL,
    product_desc TEXT,
    object_key VARCHAR(255),
    link_url VARCHAR(500) NOT NULL,
    platform VARCHAR(20),
    price NUMERIC(8,2),
    commission_rate NUMERIC(5,2), -- 佣金率 %，例如 12.50 代表 12.5%
    origin_item_id VARCHAR(100),  -- 联盟原始商品ID（淘宝itemId），后期做商品巡检失效检测用
    sort INT DEFAULT 0,
    status SMALLINT DEFAULT 1 CHECK (status IN (0,1)),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);
COMMENT ON TABLE cps_product IS 'CPS推广商品表，plant_id为空=全站通用商品';
CREATE INDEX IF NOT EXISTS idx_cps_product_plant_id ON cps_product(plant_id);
CREATE INDEX IF NOT EXISTS idx_cps_product_status ON cps_product(status);

-- 7. 后台管理员表 admin_user（初始账号由应用启动时自动创建：admin/admin123）
CREATE TABLE IF NOT EXISTS admin_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(10) DEFAULT 'admin',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);
COMMENT ON TABLE admin_user IS '后台管理员账号';
