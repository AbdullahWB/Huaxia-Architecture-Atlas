-- Huaxia Architecture Atlas - Seed data (Docker init)
SET NAMES utf8mb4;
USE huaxia_atlas;
-- =========================
-- Seed Buildings (NO temples/pagodas)
-- =========================
INSERT INTO buildings (
        name,
        dynasty,
        location,
        type,
        year_built,
        description,
        tags,
        cover_image
    )
VALUES (
        'Zhaozhou Bridge',
        'Sui',
        'Zhao County, Hebei',
        'Bridge',
        'c. 605',
        'Stone segmental arch bridge; major achievement in early bridge engineering.',
        'stone arch,bridge,engineering',
        NULL
    ),
    (
        'Forbidden City (Palace Complex)',
        'Ming',
        'Beijing',
        'Palace',
        '1406–1420',
        'Imperial palace complex with axial planning, courtyards, and timber structures.',
        'palace,courtyard,timber,imperial',
        NULL
    ),
    (
        'Siheyuan Courtyard Residence (Typical)',
        'Qing',
        'Beijing',
        'Residence',
        '18th–19th century',
        'Traditional courtyard house emphasizing symmetry, hierarchy, and enclosed space.',
        'residence,courtyard,vernacular',
        NULL
    ),
    (
        'Hutong Street Layout (Historic)',
        'Ming',
        'Beijing',
        'Urban Fabric',
        '15th–19th century',
        'Lane-based urban pattern supporting courtyard housing and neighborhood life.',
        'urban,streets,housing',
        NULL
    ),
    (
        'Ancient City Wall (Representative Section)',
        'Ming',
        'Xi’an, Shaanxi',
        'Fortification',
        '14th century',
        'Large-scale defensive wall system with gates and watchtowers; masonry construction.',
        'wall,fortification,masonry',
        NULL
    ),
    (
        'Traditional Yamen (Local Government Office)',
        'Qing',
        'Northern China (Representative)',
        'Office',
        '17th–19th century',
        'Local administrative office compound; formal halls and courtyard sequence.',
        'government,office,courtyard',
        NULL
    ),
    (
        'Classical Garden Residence (Scholars)',
        'Ming',
        'Suzhou, Jiangsu',
        'Residence',
        '16th–17th century',
        'Residential garden composition with pavilions, rocks, water features (non-religious).',
        'garden,residence,landscape',
        NULL
    ),
    (
        'Academy Courtyard (Study Hall)',
        'Song',
        'Southern China (Representative)',
        'Academy',
        '11th–13th century',
        'Educational courtyard complex used for teaching and scholarship.',
        'education,academy,courtyard',
        NULL
    );
-- =========================
-- Seed Posts
-- =========================
INSERT INTO posts (
        title,
        content,
        author_name,
        author_email,
        status
    )
VALUES (
        'What makes timber-frame architecture durable?',
        'Timber structures can last centuries when protected from moisture and designed with proper joinery. Share examples from historical sites.',
        'Abdullah',
        'abdullah@example.com',
        'APPROVED'
    ),
    (
        'I found a historic courtyard house in my city',
        'I want to share photos and a short history. Please review and approve my post for the community section.',
        'Student User',
        'student@example.com',
        'PENDING'
    );
-- =========================
-- Seed Messages (Contact → Admin inbox)
-- =========================
INSERT INTO messages (name, email, subject, message, is_read)
VALUES (
        'Visitor',
        'visitor@example.com',
        'Question about sources',
        'Can you share references for the palace layout drawings used in the site?',
        0
    );
-- =========================
-- Seed AI logs (optional)
-- =========================
INSERT INTO search_logs (query_text)
VALUES ('bridge stone arch'),
    ('Ming palace courtyard'),
    ('Qing residence siheyuan');
INSERT INTO chat_logs (user_question, model_answer)
VALUES (
        'Explain what a siheyuan is.',
        'A siheyuan is a traditional courtyard residence organized around an inner courtyard, reflecting hierarchy and privacy.'
    );