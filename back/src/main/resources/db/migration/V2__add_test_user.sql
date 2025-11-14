INSERT INTO users (
    email,
    password_hash,
    name,
    role,
    provider,
    phone,
    active,
    last_login_at,
    created_at,
    updated_at,
    tendency
) VALUES (
             'test@ozea.org',
             '$2a$10$nc8zTkbZVqzPv5LSeXfhAOHUSRmU8kVHpNDPQP4VyKtX2Sv9STkz6', -- password: 1234
             '테스트유저',
             'USER',
             'LOCAL',
             '01012341234',
             1,
             NOW(),
             NOW(),
             NOW(),
             'NEUTRAL'
         );