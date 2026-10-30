-- Migration Flyway V1 : schéma initial du suivi de candidatures
-- À placer dans src/main/resources/db/migration/

CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE job_applications (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    company         VARCHAR(255) NOT NULL,
    position        VARCHAR(500) NOT NULL,
    sent_date       DATE,
    type            VARCHAR(30)  NOT NULL,
    source          VARCHAR(255),
    location        VARCHAR(255),
    remote_policy   VARCHAR(255),
    salary_min      INTEGER,
    salary_max      INTEGER,
    salary_period   VARCHAR(10),
    salary_basis    VARCHAR(10),
    public_category VARCHAR(50),
    status          VARCHAR(30)  NOT NULL DEFAULT 'TO_APPLY',
    status_reason   TEXT,
    follow_up_date  DATE,
    offer_url       TEXT,
    notes           TEXT,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT chk_application_type CHECK (type IN
        ('OFFER_RESPONSE', 'SOLICITATION_RESPONSE', 'SPONTANEOUS')),
    CONSTRAINT chk_application_status CHECK (status IN
        ('TO_APPLY', 'SENT', 'TO_FOLLOW_UP', 'FOLLOWED_UP', 'INTERVIEW_SCHEDULED',
         'INTERVIEW_DONE', 'REJECTED', 'ACCEPTED', 'NO_RESPONSE')),
    CONSTRAINT chk_salary_period CHECK (salary_period IS NULL OR salary_period IN ('YEARLY', 'MONTHLY')),
    CONSTRAINT chk_salary_basis  CHECK (salary_basis  IS NULL OR salary_basis  IN ('GROSS', 'NET'))
);

CREATE INDEX idx_applications_user_status    ON job_applications (user_id, status);
CREATE INDEX idx_applications_user_followup  ON job_applications (user_id, follow_up_date);

CREATE TABLE interviews (
    id                 BIGSERIAL PRIMARY KEY,
    job_application_id BIGINT      NOT NULL REFERENCES job_applications(id) ON DELETE CASCADE,
    scheduled_at       TIMESTAMPTZ NOT NULL,
    interviewer        VARCHAR(255),
    format             VARCHAR(255),
    summary            TEXT
);

CREATE INDEX idx_interviews_application ON interviews (job_application_id);

CREATE TABLE contacts (
    id                 BIGSERIAL PRIMARY KEY,
    job_application_id BIGINT       NOT NULL REFERENCES job_applications(id) ON DELETE CASCADE,
    name               VARCHAR(255) NOT NULL,
    role               VARCHAR(255),
    email              VARCHAR(255),
    phone              VARCHAR(50)
);

CREATE INDEX idx_contacts_application ON contacts (job_application_id);
