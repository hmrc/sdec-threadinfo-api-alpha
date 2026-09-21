# --- !Ups

INSERT INTO sdec_thread (
    reference,
    status,
    created_timestamp,
    last_updated_timestamp,
    thread_expiry_date,
    case_reference,
    email,
    nino
) VALUES (
    'THREAD1000AA',
    'Active',
    '2026-09-01 09:30:00',
    '2026-09-01 10:15:00',
    '2026-10-01',
    'CASE-100001',
    'john.smith@example.com',
    'AA123456A'
);

INSERT INTO sdec_thread (reference,
                         status,
                         created_timestamp,
                         last_updated_timestamp,
                         thread_expiry_date,
                         case_reference,
                         email,
                         nino)
VALUES ('THREAD2000BB',
        'Draft',
        '2026-09-02 14:00:00',
        '2026-09-02 14:00:00',
        '2026-10-02',
        'CASE-100002',
        'jane.doe@example.com',
        'BB654321B');

INSERT INTO sdec_thread_details (sdec_thread_id,
                                 message,
                                 required_by)
VALUES (1,
        'Please provide evidence of the change in circumstances.',
        '2026-09-15');

INSERT INTO sdec_thread_details (sdec_thread_id,
                                 message,
                                 required_by)
VALUES (1,
        'Please provide the latest supporting documentation.',
        '2026-09-20');

INSERT INTO sdec_thread_details (sdec_thread_id,
                                 message,
                                 required_by)
VALUES (2,
        'Please provide the requested information.',
        '2026-09-25');

INSERT INTO sdec_recipient (sdec_thread_id,
                            first_name,
                            last_name,
                            email,
                            phone_number,
                            national_insurance_number)
VALUES (1,
        'John',
        'Smith',
        'john.smith@example.com',
        '07123456789',
        'AA123456A');

INSERT INTO sdec_recipient (sdec_thread_id,
                            first_name,
                            last_name,
                            email,
                            phone_number,
                            national_insurance_number)
VALUES (2,
        'Jane',
        'Doe',
        'jane.doe@example.com',
        '07987654321',
        'BB654321B');

# --- !Downs

DELETE
FROM sdec_recipient;
DELETE
FROM sdec_thread_details;
DELETE
FROM sdec_thread;